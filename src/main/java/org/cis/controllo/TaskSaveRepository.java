package org.cis.controllo;

import javafx.concurrent.Task;
import org.cis.Applicazione;
import org.cis.Constants;
import org.cis.DAO.DAORepositoryJSON;
import org.cis.modello.Repository;
import org.cis.modello.SessionManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TaskSaveRepository extends Task<Void> {

    private Path pathSelectedDirectory;

    public TaskSaveRepository(Path pathSelectedDirectory) {
        this.pathSelectedDirectory = pathSelectedDirectory;
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

        List<Path> pathsRepositories = Files.list(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY))
                                            .collect(Collectors.toList());

        Path pathCloneRepositories = FileUtils.createDirectory(Paths.get(pathGHRepoResult.toString(), "CloneRepositories"));

        if (pathsRepositories.isEmpty()) {
            // cacheCloneRepositories is empty.
            // Language detection has been launched...
            List<Path> pathLanguanges = Files.list(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_LANGUAGE_REPOSITORIES))
                                             .collect(Collectors.toList());

            Path pathLingua = FileUtils.createDirectory(Paths.get(pathCloneRepositories.toString(), "lingua"));

            for (Path pathLanguage: pathLanguanges) {
                pathsRepositories = Files.list(pathLanguage)
                                         .collect(Collectors.toList());
                Path pathNewLanguage = FileUtils.createDirectory(Paths.get(pathLingua.toString(), pathLanguage.getFileName().toString()));
                moveRepositories(pathNewLanguage, pathsRepositories);
            }
        } else {
            moveRepositories(pathCloneRepositories, pathsRepositories);
        }

        updateMessage("Saving results in the JSON folder");
        Path pathJSON = FileUtils.createDirectory(Paths.get(pathGHRepoResult.toString(), "JSON"));
        List<Repository> repositories = (List<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO);
        DAORepositoryJSON daoRepositoryJSON = Applicazione.getInstance().getDaoRepositoryJSON();
        daoRepositoryJSON.saveRepositories(pathJSON.toString(), repositories);

        return null;
    }

    private void moveRepositories(Path pathGHRepoResult, List<Path> pathsRepositories) {
        updateProgress(0, pathsRepositories.size());
        for (int i = 0; i < pathsRepositories.size(); i++) {
            Path path = pathsRepositories.get(i);
            String message = "Moving " + path.getFileName() + " to the " + pathSelectedDirectory.getFileName() + " folder";
            System.out.println(message);
            updateMessage(message);
            FileUtils.moveDirTree(path, Paths.get(pathGHRepoResult.toString(), path.getFileName().toString()));
            updateProgress(i + 1, pathsRepositories.size());
        }
    }
}
