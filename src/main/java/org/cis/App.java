package org.cis;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cis.controllo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *  G-Repo Application.
 *
 */
public class App extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    private static final String LOGO = "                                                                               \n" +
            "                               ,.  ., . ,.  ,  .                                \n" +
            "                           ., , .    ** ,* ,.  ., ,  .                          \n" +
            "                          ,.   ,.*.,**..,,,..*,.*.  .,                          \n" +
            "                         *  *.*,.      .......  *,,,. ,..,                      \n" +
            "                     ,  , *,,,        ./,  ...     *.*. .,,                     \n" +
            "    @&&%#%&&&        .*. *.,. ,&&&&&&&&&&   ..      ..** .                      \n" +
            "  &&*.      ,&#.     ., ..,. .(&/, ...,,&&,.....,,,. ..*, ,*...          ..     \n" +
            " #&*.         .    ,,,, ..,   (&#,...,,/&#(, &&*,**/&#..#&&**,,*&%.  /&#*,,,%&, \n" +
            " %&,    (%%%%&&.   ...*./**.  *&%#(((#&&&,.*&&*,.,,,*&(*#&/,    .&%.*&/.     #&,\n" +
            "  &&.        &&, .,,,,,,/(/,..,&///,   .&&. %&*****,,,*/#&*.,    &#..&(.     %&,\n" +
            "   *&&%,  ,%&&%,    ,  , ,*,  ,&/.*** ..#&*./#&%. .&&((,#&#&.  &&(,  .&&*  (&&*.\n" +
            "     ..,,,,,.        . .,  *,,...   *//**//.   .,,****. (&/,.,,,.       .,,,.   \n" +
            "                       .,,. .*.,*               ,*./.   (&*                     \n" +
            "                         .,    ,*.**.,**..**,.*,,. ..  .                        \n" +
            "                           ....* .  ...,.,,.    ,.*,,.                          \n" +
            "                                ,  ,             .                              \n" +
            "                                                                                ";

    @Override
    public void start(Stage stage) {
        LOG.info(LOGO + "\n\t--------------------------------------------------------------\n\t               Launch of the G-Repo application\n\t--------------------------------------------------------------");
        Applicazione.getInstance().getModello().addObject(Constants.PRIMARY_STAGE, stage);

        //# Init Folder:
        LOG.info("Init Folder json and cacheCloneRepositories");
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
        FileUtils.createDirectory(FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY));
    }

    public static void main(String[] args) {
        launch();
    }

}