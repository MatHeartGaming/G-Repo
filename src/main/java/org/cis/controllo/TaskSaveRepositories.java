package org.cis.controllo;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.cis.Applicazione;
import org.cis.Constants;
import org.cis.DAO.DAORepositoryJSON;
import org.cis.modello.Repository;
import org.cis.modello.RepositoryLanguage;
import org.cis.modello.SessionManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskSaveRepositories extends Task<Void> {

    private final Path pathSelectedDirectory;
    private final List<Repository> repositories;
    private final AtomicInteger count = new AtomicInteger(0);


    public TaskSaveRepositories(Path pathSelectedDirectory, List<Repository> repositories) {
        this.pathSelectedDirectory = pathSelectedDirectory;
        this.repositories = repositories;
    }

    @Override
    protected Void call() throws Exception {
        Path pathGRepoResult;

        if (!this.pathSelectedDirectory.toString().contains("G-RepoResult")) {
            pathGRepoResult = createFolderResult();
        } else {
            pathGRepoResult = this.pathSelectedDirectory;
        }

        final int indexLastClonedRepository = (int) Applicazione.getInstance().getModello().getObject(Constants.INDEX_LAST_CLONED_REPOSITORY);
        if (indexLastClonedRepository != -1) {
            saveCloneRepositories(pathGRepoResult);
        }


        saveRepositoriesToJson(pathGRepoResult);
        return null;
    }

    private Path createFolderResult() {
        // Creation of the name of G-RepoResult.
        // e.g. G-RepoResult_2_2020-07-22_16-38-49.

        SessionManager sessionManager = Applicazione.getInstance().getSessionManager();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String suffixGHRepoResult = sessionManager.getSessions().size() + "_" + LocalDateTime.now().format(formatter);

        Path pathGRepoResult = Paths.get(this.pathSelectedDirectory.toString(), "G-RepoResult_" + suffixGHRepoResult);
        return FileUtils.createDirectory(pathGRepoResult);
    }

    private void saveRepositoriesToJson(Path pathGRepoResult) {
        updateMessage("Saving results in the JSON folder");
        Path pathJSON = FileUtils.createDirectory(Paths.get(pathGRepoResult.toString(), "JSON"));
        DAORepositoryJSON daoRepositoryJSON = Applicazione.getInstance().getDaoRepositoryJSON();
        daoRepositoryJSON.saveRepositories(String.valueOf(pathJSON), this.repositories);
    }

    private void saveCloneRepositories(Path pathGHRepoResult) {
        final Path pathCloneRepositories = FileUtils.createDirectory(Paths.get(pathGHRepoResult.toString(), "CloneRepositories"));

        String toReplace = "risorse" + FileUtils.PATH_SEPARATOR + "cacheCloneRepositories" + FileUtils.PATH_SEPARATOR;
        //Path pathCacheCloneDirectory = FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY);
        final boolean isLanguageDetection = (boolean) Applicazione.getInstance().getModello().getObject(Constants.IS_LANGUAGE_DETECTION);
        if (isLanguageDetection) {
            // Language detection has been launched...
            // Folder creation.
            Path pathLingua = FileUtils.createDirectory(Paths.get(pathCloneRepositories.toString(), "language"));

            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "english"));
            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "not_english"));
            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "mixed"));
            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "unknown"));
        }


        updateProgress(0, this.repositories.size());

        final Path pathBase = Paths.get(FileUtils.getRootPath());
        final Map<String, RepositoryLanguage> repositoryLanguageMap =
                (Map<String, RepositoryLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_LANGUAGE);

        this.repositories.parallelStream()
                .filter(repository -> repository.getCloneDirectory() != null)
                .forEach(repository -> {

                    this.updateSafeMessage();

                    Path pathCloneDirectory = Paths.get(repository.getCloneDirectory());
                    System.out.println("pathCloneDirectory: " + pathCloneDirectory);
                    Path pathRelativeCloneDirectory = pathBase.relativize(pathCloneDirectory);
                    pathRelativeCloneDirectory = Paths.get(pathRelativeCloneDirectory.toString().replace(toReplace, ""));
                    if (isLanguageDetection) {
                        RepositoryLanguage repositoryLanguage = repositoryLanguageMap.get(repository.getId());
                        pathRelativeCloneDirectory = Paths.get("language" + FileUtils.PATH_SEPARATOR + String.join("_", repositoryLanguage.getLanguage().split(" ")) + FileUtils.PATH_SEPARATOR + pathRelativeCloneDirectory);
                    }
                    Path pathCopy = pathCloneRepositories.resolve(pathRelativeCloneDirectory);
                    System.out.println("pathCopy: " + pathCopy);
                    FileUtils.copyDirTree(pathCloneDirectory, pathCopy);
                    FileUtils.deleteDirTree(pathCloneDirectory);
                    this.updateSafeProgress();
                });
    }

    private void updateSafeMessage() {
        Platform.runLater(() -> {
            int numPoint = count.get() % 4;
            String point = ".";
            for (int i = 0; i < numPoint; i++) {
                point = point + ".";
            }
            String messageLabel = "Moving repositories in " +  this.pathSelectedDirectory.getFileName() + " folder" + point;
            updateMessage(messageLabel);
        });
    }

    private void updateSafeProgress() {
        Platform.runLater(() -> {
            updateProgress(count.getAndIncrement(), this.repositories.size());
        });
    }

    /*@Override
    protected Void call() throws Exception {
        // Creation of the name of GHRepoResult.
        // e.g. GHRepoResult_2_2020-07-22_16-38-49.
        SessionManager sessionManager = Applicazione.getInstance().getSessionManager();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String suffixGHRepoResult = sessionManager.getSessions().size() + "_" + sessionManager.getCurrentSession().getDate().format(formatter);

        Path pathGHRepoResult = Paths.get(this.pathSelectedDirectory.toString(), "GHRepoResult_" + suffixGHRepoResult);
        pathGHRepoResult = FileUtils.createDirectory(pathGHRepoResult);

        final Path pathCloneRepositories = FileUtils.createDirectory(Paths.get(pathGHRepoResult.toString(), "CloneRepositories"));

        String toReplace = "risorse" + FileUtils.PATH_SEPARATOR + "cacheCloneRepositories" + FileUtils.PATH_SEPARATOR;
        //Path pathCacheCloneDirectory = FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY);
        final boolean isLanguageDetection = (boolean) Applicazione.getInstance().getModello().getObject(Constants.IS_LANGUAGE_DETECTION);
        if (isLanguageDetection) {
            // Language detection has been launched...
            Path pathLingua = FileUtils.createDirectory(Paths.get(pathCloneRepositories.toString(), "lingua"));

            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "english"));
            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "not_english"));
            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "mixed"));
            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "unknown"));
        }


        final Map<String, RepositoryLanguage> repositoryLanguageMap =
                (Map<String, RepositoryLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_LANGUAGE);
        final Path pathBase = Paths.get(FileUtils.getRootPath());
        updateProgress(0, this.repositories.size());
        for (int i = 0; i < this.repositories.size(); i++) {
            Repository repository = this.repositories.get(i);

            // The repository in question has not been cloned.
            if (repository.getCloneDirectory() == null) continue;

            String message = "Moving " + repository.getName() + " to the " + this.pathSelectedDirectory.getFileName() + " folder";
            System.out.println(message);
            updateMessage(message);
            Path pathCloneDirectory = Paths.get(repository.getCloneDirectory());

            System.out.println("pathCloneDirectory: " + pathCloneDirectory);

            Path pathRelativeCloneDirectory = pathBase.relativize(pathCloneDirectory);

            pathRelativeCloneDirectory = Paths.get(pathRelativeCloneDirectory.toString().replace(toReplace, ""));

            if (isLanguageDetection) {
                RepositoryLanguage repositoryLanguage = repositoryLanguageMap.get(repository.getId());
                pathRelativeCloneDirectory = Paths.get("lingua" + FileUtils.PATH_SEPARATOR + String.join("_", repositoryLanguage.getLanguage().split(" ")) + FileUtils.PATH_SEPARATOR + pathRelativeCloneDirectory);
            }

            Path pathCopy = pathCloneRepositories.resolve(pathRelativeCloneDirectory);

            System.out.println("pathCopy: " + pathCopy);
            FileUtils.copyDirTree(pathCloneDirectory, pathCopy);
            FileUtils.deleteDirTree(pathCloneDirectory);

            updateProgress(i + 1, this.repositories.size());
        }
        updateMessage("Saving results in the JSON folder");
        Path pathJSON = FileUtils.createDirectory(Paths.get(pathGHRepoResult.toString(), "JSON"));
        DAORepositoryJSON daoRepositoryJSON = Applicazione.getInstance().getDaoRepositoryJSON();
        daoRepositoryJSON.saveRepositories(pathJSON.toString(), this.repositories);
        return null;
    }*/

}
