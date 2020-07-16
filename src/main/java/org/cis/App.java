package org.cis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cis.controllo.CommonEvents;
import org.cis.controllo.FileUtils;
import org.cis.modello.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Applicazione.getInstance().getSingleThread().start();
        //# Init Folder:
        //## By cloning.
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY));

        //##By Language Detection
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_LANGUAGE_REPOSITORIES));

        //# Init GUI.
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        commonEvents.loadPanel("primary", Modality.NONE, true, "G-Repo", StageStyle.DECORATED, false);
        /*scene = new Scene(loadFXML("primary"));
        Parent root = scene.getRoot();
        stage.setScene(scene);
        Applicazione.getInstance().getCommonEvents().moveWindow(root, stage);
        //stage.setResizable(false);
        stage.setTitle("G-Repo");*/
        //todo: rimuovere dopo i test.
        //Applicazione.getInstance().getModello().addObject(Constants.INDEX_LAST_CLONED_REPOSITORY, -1);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}