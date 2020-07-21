package org.cis.controllo;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.cis.Applicazione;
import org.cis.Constants;
import org.cis.modello.Repository;
import org.cis.modello.StatisticsProgrammingLanguage;
import org.eclipse.jgit.lib.ProgressMonitor;

import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TaskCloneRepositories extends Task<Void> {

    private ProgressMonitor monitor = new Monitor();
    private String currentNameRepository;
    private boolean cancel = false;
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
            System.out.println("Cancellazione cache repository");
            Files.list(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY)).forEach(FileUtils::deleteDirTree);
        }

        GitCommand gitCommand = new GitCommand();
        RepositoryVisitor repositoryVisitor = new RepositoryVisitor();

        updateMessage(this.firstNonClonedRepositoryIndex == 0 ? "Clone all repositories" : "Cloning resumption");
        // Inizializzo la barra o con 0 oppure con l'elemento già clonato (che a partire da 1 coincide con firstNonClonedRepositoryIndex, rispetto a size()).
        updateProgress(this.firstNonClonedRepositoryIndex, this.repositories.size());
        System.out.println(" Inizio Clonazione");
        for (int i = this.firstNonClonedRepositoryIndex; i < this.repositories.size(); i++) {
            //# Repositories with the cloneDirectory property equal to null have not yet been cloned.
            Repository repository = this.repositories.get(i);
            if (repository.getCloneDirectory() == null) {
                //# Cloning.
                currentNameRepository = repository.getName();
                String cloneDirectory = FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY + "\\" + repository.getName()).toString();
                System.out.println("Clonazione del repo: " + cloneDirectory);
                try {
                    gitCommand.cloneRepository(repository.getCloneUrl(), cloneDirectory, this.token, monitor);
                } catch (Exception e) {
                    if (this.cancel == true) {
                        updateMessage("Stop Cloning");
                        this.cancel(true);
                        break;
                    }
                    updateMessage("Something went wrong...limit rate reached or connection issues");
                    throw e;
                }
                //## I imposed the clone Directory if and only if the cloning was successful.
                repository.setCloneDirectory(cloneDirectory);
                Applicazione.getInstance().getModello().addObject(Constants.INDEX_LAST_CLONED_REPOSITORY, i);


                //# Anticipated the detection of the programming language.
                System.out.println("Calcolo linguaggio: " + cloneDirectory);
                StatisticsProgrammingLanguage statisticsProgrammingLanguage = repositoryVisitor.programmingLanguageDetection(repository.getCloneDirectory());
                System.out.println(statisticsProgrammingLanguage);
                Map<String, StatisticsProgrammingLanguage> mapRepositoryLangProg =
                        (Map<String, StatisticsProgrammingLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_PROGRAMMING_LANGUAGE);
                mapRepositoryLangProg.put(repository.getId(), statisticsProgrammingLanguage);


                //# Calculation of the date of the last commit.
                LocalDate dataCommit = gitCommand.lastDateCommit(repository.getCloneDirectory());
                repository.setLastCommitDate(dataCommit);
                //## Displays the date in the table.
                Platform.runLater(() -> repository.displayLastCommitDate());

                updateProgress(i + 1, this.repositories.size());
            }

        }
        if (this.cancel == false) updateMessage("Repositories cloned correctly");
        System.out.println(" Fine Clonazione");
        return null;
    }

    public void close() {
        this.cancel = true;
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
            return cancel;
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
