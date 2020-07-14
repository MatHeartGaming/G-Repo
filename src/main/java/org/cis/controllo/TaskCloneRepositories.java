package org.cis.controllo;

import javafx.concurrent.Task;
import org.cis.Applicazione;
import org.cis.Constants;
import org.cis.modello.Repository;
import org.eclipse.jgit.lib.ProgressMonitor;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class TaskCloneRepositories extends Task<Void> {

    private ProgressMonitor monitor = new Monitor();
    private String currentNameRepository;
    private boolean cancelled = false;
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
            System.out.println("Cancellazione cache repository");
            Files.list(Paths.get(Constants.CLONING_DIRECTORY)).forEach(FileUtils::deleteDirTree);
            //FileUtils.deleteDirTree(Paths.get(Constants.CLONING_DIRECTORY));
        }

        GitCommand gitCommand = new GitCommand();
        RepositoryVisitor repositoryVisitor = new RepositoryVisitor();

        //todo: LE STRINGHE MESSAGGIO, INSERIRLE NELLA CLASSE COSTANTI (O FARE UNA CLASSE ANNIDATA "Messaggi")
        updateMessage("Clone all repositories");
        // Inizializzo la barra o con 0 oppure con l'elemento già clonato (che a partire da 1 coincide con firstNonClonedRepositoryIndex, rispetto a size()).
        updateProgress(this.firstNonClonedRepositoryIndex, this.repositories.size());
        System.out.println(" Inizio Clonazione");
        for (int i = this.firstNonClonedRepositoryIndex; i < this.repositories.size(); i++) {
            //# Repositories with the cloneDirectory property equal to null have not yet been cloned.
            Repository repository = this.repositories.get(i);
            if (repository.getCloneDirectory() == null) {
                //todo: fare refactoring di queste fasi.
                //# Cloning.
                currentNameRepository = repository.getName();
                String cloneDirectory = Constants.CLONING_DIRECTORY + "\\" + repository.getName();
                System.out.println("Clonazione del repo: " + cloneDirectory);
                gitCommand.cloneRepository(repository.getCloneUrl(), cloneDirectory, this.token, monitor);
                //## I imposed the clone Directory if and only if the cloning was successful.
                repository.setCloneDirectory(cloneDirectory);
                Applicazione.getInstance().getModello().addObject(Constants.INDEX_LAST_CLONED_REPOSITORY, i);


                //# Anticipated the detection of the programming language.
                System.out.println("Calcolo linguaggio: " + cloneDirectory);
                String programmingLanguage = repositoryVisitor.programmingLanguageDetection(repository.getCloneDirectory());
                System.out.println(programmingLanguage);
                repository.setProgrammingLanguages(programmingLanguage);
                repository.setListProgrammingLanguages(Repository.programmingLanguagesToList(repository.getProgrammingLanguages()));


                //# Calculation of the date of the last commit.
                LocalDate dataCommit = gitCommand.lastDateCommit(repository.getCloneDirectory());
                repository.setLastCommitDate(dataCommit);
                //## Displays the date in the table.
                repository.displayLastCommitDate();

                updateProgress(i + 1, this.repositories.size());
            }

        }
        System.out.println(" Fine Clonazione");
        return null;
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
