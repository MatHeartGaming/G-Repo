package org.cis.controllo;

import javafx.concurrent.Task;
import org.cis.Applicazione;
import org.cis.Constants;
import org.cis.DAO.DAORepositoryJSON;
import org.cis.modello.Repository;
import org.cis.modello.RepositoryLanguage;
import org.cis.modello.SessionManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskSaveRepository extends Task<Void> {

    private Path pathSelectedDirectory;
    private List<Repository> repositories;

    public TaskSaveRepository(Path pathSelectedDirectory, List<Repository> repositories) {
        this.pathSelectedDirectory = pathSelectedDirectory;
        this.repositories = repositories;
    }

    @Override
    protected Void call() throws Exception {
        // Creation of the name of GHRepoResult.
        // e.g. GHRepoResult_2_2020-07-22_16-38-49.
        SessionManager sessionManager = Applicazione.getInstance().getSessionManager();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String suffixGHRepoResult = sessionManager.getSessions().size() + "_" + sessionManager.getCurrentSession().getDate().format(formatter);

        Path pathGHRepoResult = Paths.get(this.pathSelectedDirectory.toString(), "GHRepoResult_" + suffixGHRepoResult);
        pathGHRepoResult = FileUtils.createDirectory(pathGHRepoResult);

        Path pathCloneRepositories = FileUtils.createDirectory(Paths.get(pathGHRepoResult.toString(), "CloneRepositories"));

        String toReplace = "risorse" + FileUtils.PATH_SEPARATOR + "cacheCloneRepositories" + FileUtils.PATH_SEPARATOR;
        //Path pathCacheCloneDirectory = FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY);
        boolean isLanguageDetection = (boolean) Applicazione.getInstance().getModello().getObject(Constants.IS_LANGUAGE_DETECTION);
        if (isLanguageDetection) {
            // Language detection has been launched...
            Path pathLingua = FileUtils.createDirectory(Paths.get(pathCloneRepositories.toString(), "lingua"));

            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "english"));
            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "not_english"));
            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "mixed"));
            FileUtils.createDirectory(Paths.get(pathLingua.toString(), "unknown"));
        }


        updateProgress(0, this.repositories.size());
        for (int i = 0; i < this.repositories.size(); i++) {
            Repository repository = this.repositories.get(i);

            // The repository in question has not been cloned.
            if (repository.getCloneDirectory() == null) continue;

            String message = "Moving " + repository.getName() + " to the " + this.pathSelectedDirectory.getFileName() + " folder";
            System.out.println(message);
            updateMessage(message);
            Path pathBase = Paths.get(FileUtils.getRootPath());
            Path pathCloneDirectory = Paths.get(repository.getCloneDirectory());

            System.out.println("pathCloneDirectory: " + pathCloneDirectory);

            Path pathRelativeCloneDirectory = pathBase.relativize(pathCloneDirectory);

            pathRelativeCloneDirectory = Paths.get(pathRelativeCloneDirectory.toString().replace(toReplace, ""));

            if (isLanguageDetection) {
                Map<String, RepositoryLanguage> repositoryLanguageMap =
                        (Map<String, RepositoryLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_LANGUAGE);
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
    }

}
