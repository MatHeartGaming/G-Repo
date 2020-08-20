package org.cis.controllo;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.cis.Applicazione;
import org.cis.Constants;
import org.cis.modello.*;
import org.eclipse.jgit.dircache.InvalidPathException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskCloneRepositories extends Task<Void> {

    private static final Logger LOG = LoggerFactory.getLogger(TaskCloneRepositories.class);

    private ProgressMonitor monitor = new Monitor();
    private String currentNameRepository;
    private volatile boolean cancelled = false;
    private int firstNonClonedRepositoryIndex;
    private List<Repository> repositories;
    private String token;

    public TaskCloneRepositories(List<Repository> repositories, int firstNonClonedRepositoryIndex, String token) {
        this.repositories = repositories;
        this.firstNonClonedRepositoryIndex = firstNonClonedRepositoryIndex;
        this.token = token;
    }

    @Override
    protected Void call() throws Exception {
        //# Clearing the repositories in the cacheCloneRepositories directory.
        if (this.firstNonClonedRepositoryIndex == 0) {
            updateMessage("Clone cache cleanup");
            LOG.info("Clone Cache Cleanup");

            List<Path> paths = Files.list(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY))
                                    .collect(Collectors.toList());

            for (int i = 0; i < paths.size(); i++) {
                FileUtils.deleteDirTree(paths.get(i));
                updateProgress(i + 1, paths.size());
            }
        }

        SessionManager sessionManager = Applicazione.getInstance().getSessionManager();
        Session session = sessionManager.getCurrentSession();
        Query query = session.getQuery();
        Qualifier qualifier = query.searchQualifierByCriteria(qualifier1 -> qualifier1.getKey().trim().toLowerCase().equals("language"));

        RepositoryVisitor repositoryVisitor = qualifier == null ? new RepositoryVisitor() : new RepositoryVisitor(qualifier.getValue().trim());
        GitCommand gitCommand = new GitCommand();

        String messageInitCloning = this.firstNonClonedRepositoryIndex == 0 ? "Clone all repositories" : "Cloning resumption";
        updateMessage(messageInitCloning);

        LOG.info("--------------------------------------------------------------\n                   " + messageInitCloning + "                    \n--------------------------------------------------------------");

        updateProgress(this.firstNonClonedRepositoryIndex, this.repositories.size());
        LOG.info("Init Cloning");
        for (int i = this.firstNonClonedRepositoryIndex; i < this.repositories.size(); i++) {
            //# Repositories with the cloneDirectory property equal to null have not yet been cloned.
            Repository repository = this.repositories.get(i);
            if (repository.getCloneDirectory() == null) {
                //# Cloning.
                currentNameRepository = repository.getName();
                String cloneDirectory = FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY + "\\" + (i + "_" + repository.getId() + "_" + repository.getName())).toString();
                String messageLog = "\n\t** Cloning of " + repository.getName() + "\n\t* ID " + repository.getId() + "\n\t* Clone Url" + repository.getCloneUrl() + "\n\t* URL Project " + repository.getUrlProject();
                try {
                    gitCommand.cloneRepository(repository.getCloneUrl(), cloneDirectory, this.token, monitor);
                } catch (InvalidPathException e) {
                    // Cloning must proceed for other pending repositories.
                    Platform.runLater(() -> {
                        repository.displayLastCommitDate(Constants.MESSAGE_NOT_EXISTS);
                        repository.displayProgrammingLanguages(Constants.MESSAGE_NOT_EXISTS);
                        repository.setLanguageProperty(Constants.MESSAGE_NOT_EXISTS);
                    });
                    messageLog = messageLog + "\n\t* Outcome of cloning: the repository cannot be cloned, cause: " + "\n\t" + e.getLocalizedMessage() + "\n\t* Clone Directory: not exists";
                    LOG.info(messageLog);
                    continue;
                } catch(Exception e) {
                    if (this.cancelled == true) {
                        updateMessage("Stop Cloning");
                        LOG.info("Stop Cloning");
                        this.cancel(true);
                        break;
                    }
                    updateMessage("Something went wrong: see the log file");
                    LOG.info("Something went wrong: " + e.getLocalizedMessage());
                    throw e;
                }
                //## I imposed the clone Directory if and only if the cloning was successful.
                repository.setCloneDirectory(cloneDirectory);
                Applicazione.getInstance().getModello().addObject(Constants.INDEX_LAST_CLONED_REPOSITORY, i);

                messageLog = messageLog + "\n\t* Outcome of cloning: repository successfully cloned" + "\n\t* Clone Directory: " + cloneDirectory;
                LOG.info(messageLog);


                //# Anticipated the detection of the programming language.
                LOG.info("Calculation of programming language");
                StatisticsProgrammingLanguage statisticsProgrammingLanguage = repositoryVisitor.programmingLanguageDetection(repository.getCloneDirectory());
                LOG.info(String.valueOf(statisticsProgrammingLanguage));
                Map<String, StatisticsProgrammingLanguage> mapRepositoryLangProg =
                        (Map<String, StatisticsProgrammingLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_PROGRAMMING_LANGUAGE);
                mapRepositoryLangProg.put(repository.getId(), statisticsProgrammingLanguage);


                //# Calculation of the date of the last commit.
                LOG.info("Calculation of the date of the last commit");
                LocalDate dataCommit = gitCommand.lastDateCommit(repository.getCloneDirectory());
                String dataCommitString = Constants.MESSAGE_NOT_EXISTS;
                if (dataCommit != null) {
                    repository.setLastCommitDate(dataCommit);
                    dataCommitString = dataCommit.toString();
                }
                //## Displays the date in the table.
                String finalDataCommitString = dataCommitString;
                Platform.runLater(() -> repository.displayLastCommitDate(finalDataCommitString));
                LOG.info("Last commit date: " + finalDataCommitString);

                updateProgress(i + 1, this.repositories.size());
            }

        }
        if (this.cancelled == false) {
            updateMessage("Repositories cloned correctly");
            LOG.info("Repositories cloned correctly");
        }
        LOG.info("--------------------------------------------------------------\n                   End Cloning                    \n--------------------------------------------------------------");
        return null;
    }

    public void close() {
        this.cancelled = true;
    }

    private class Monitor implements ProgressMonitor {

        private int progress = 0;
        private int totalWork = 0;
        private int workCompleted = 0;
        private String title;
        private int completed;

        @Override
        public void start(int totalTasks) {
            //updateMessage("Cloning of " + currentNameRepository);
        }

        @Override
        public void beginTask(String title, int totalWork) {
            this.totalWork = totalWork;
            this.workCompleted = 0;
            this.title = title;
            if (this.totalWork != 0) {
                updateMessage(this.title + ": " + this.totalWork);
            }
        }

        @Override
        public void update(int completed) {
            if (this.totalWork == 0) {
                this.completed = completed;
                updateMessage(this.title + ": " + this.completed);
                return;
            }
            this.workCompleted += completed;
            int newProgress = getProgress();

            if(this.progress != newProgress) {
                this.progress = newProgress;
                updateMessage("Cloning of " + currentNameRepository + ", " + this.title + ": " + this.progress + "% " + "(" + this.workCompleted + "/" + this.totalWork + ")");
            }
        }

        @Override
        public void endTask() {
            if (this.totalWork == 0) {
                updateMessage(this.title + ": " + this.completed + ", done");
                return;
            }
            updateMessage("Cloning of " + currentNameRepository + ", " + this.title + ": " +  this.progress + "% " + "(" + this.workCompleted + "/" + this.totalWork + ") " + ", done");
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        private int getProgress() {
            if(totalWork == 0)
                return 0;

            final int taskWorkProgress = (int) ((100.0 / totalWork)
                    * workCompleted);
            return taskWorkProgress;
        }

    }
}
