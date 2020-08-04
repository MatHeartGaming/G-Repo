package org.cis;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cis.controllo.CommonEvents;
import org.cis.controllo.FileUtils;
import org.cis.controllo.Utils;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        Applicazione.getInstance().getModello().addObject(Constants.PRIMARY_STAGE, stage);
        //# Init Folder:
        initFolder();

        // Init GUI.
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        commonEvents.loadPanel("primary", Modality.NONE, true, "G-Repo", StageStyle.DECORATED, false);
        /*scene = new Scene(loadFXML("primary"));
        Parent root = scene.getRoot();
        stage.setScene(scene);
        Applicazione.getInstance().getCommonEvents().moveWindow(root, stage);
        //stage.setResizable(false);
        stage.setTitle("G-Repo");*/
    }

    private void initFolder() {
        // # By Search query.
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_JSON));

        // # By cloning.
        //# By Search query.
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_JSON));

        //# By cloning.
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY));
    }

    public static void main(String[] args) {
        launch();
    }

}