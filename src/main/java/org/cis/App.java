package org.cis;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cis.controllo.CommonEvents;
import org.cis.controllo.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Applicazione.getInstance().getSingleThread().start();
        //# Init Folder:
        initFolder();

        // Init GUI.
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        commonEvents.loadPanel("primary", Modality.NONE, true, "G-Repo", StageStyle.DECORATED, false);
        Applicazione.getInstance().getModello().addObject(Constants.PRIMARY_STAGE, stage);
        /*scene = new Scene(loadFXML("primary"));
        Parent root = scene.getRoot();
        stage.setScene(scene);
        Applicazione.getInstance().getCommonEvents().moveWindow(root, stage);
        //stage.setResizable(false);
        stage.setTitle("G-Repo");*/

        // C:\Users\ilari\Documents\G-Repo\risorse\cacheCloneRepositories\Jetpack-MVVM-Best-Practice_1
        /*String toReplace = "risorse" + FileUtils.PATH_SEPARATOR + "cacheCloneRepositories" + FileUtils.PATH_SEPARATOR;
        Path pathBase = Paths.get(FileUtils.getRootPath());

        Path pathCloneDirectory = Paths.get("C:\\Users\\ilari\\Documents\\G-Repo\\risorse\\cacheCloneRepositories\\Jetpack-MVVM-Best-Practice_1");

        System.out.println("pathCloneDirectory: " + pathCloneDirectory);

        Path pathRelativeCloneDirectory = pathBase.relativize(pathCloneDirectory);

        System.out.println("1. pathRelativeCloneDirectory: " + pathRelativeCloneDirectory);

        pathRelativeCloneDirectory = Paths.get(pathRelativeCloneDirectory.toString().replace(toReplace, ""));
        String english = "english";
        if (english.equals("english")) {
            pathRelativeCloneDirectory = Paths.get("lingua" + "\\" + english + "\\" + pathRelativeCloneDirectory);
        }

        System.out.println("2. pathRelativeCloneDirectory (Replace): " + pathRelativeCloneDirectory);

        Path pathCloneRepositories = FileUtils.createDirectory(Paths.get("C:\\Users\\ilari\\Desktop\\TOMOVE", "CloneRepositories"));

        // mia radice di salvataggio
        System.out.println("pathCloneRepositories: " + pathCloneRepositories);

        //Destinazione.
        Path pathCopy = pathCloneRepositories.resolve(pathRelativeCloneDirectory);

        System.out.println("pathCopy: " + pathCopy);*/

        // no anas
        // C:\Users\ilari\Desktop\TOMOVE\CloneRepositories\Jetpack-MVVM-Best-Practice_1
        //2. pathRelativeCloneDirectory (Replace): Jetpack-MVVM-Best-Practice_1

        //si anas
        // pathCopy: C:\Users\ilari\Desktop\TOMOVE\CloneRepositories\lingua\english\Jetpack-MVVM-Best-Practice_1
        //2. pathRelativeCloneDirectory (Replace): lingua\english\Jetpack-MVVM-Best-Practice_1
    }

    private void initFolder() {
        //## By Search query.
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_JSON));

        //## By cloning.
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY));

        //## By Language Detection
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_LANGUAGE_REPOSITORIES));
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_ENGLISH));
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_MIXED));
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_NOT_ENGLISH));
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_UNKNOWN));
    }

    public static void main(String[] args) {
        launch();
    }

}