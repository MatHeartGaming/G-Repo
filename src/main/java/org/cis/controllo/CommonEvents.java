package org.cis.controllo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cis.App;
import org.cis.Applicazione;
import org.cis.Costanti;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class CommonEvents {

    private double x, y;

    public void moveWindow(Parent root, Stage stage) {

        root.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });
    }

    public void closeApp(Stage stage) {
        //stage.setOnHiding(windowEvent -> System.out.println("Closing"));
        stage.setOnCloseRequest(windowEvent -> {
            System.out.println("Closing");
            Applicazione.getInstance().getSingleThread().close();
            Platform.exit();
            System.exit(0);
        });
    }

    public void showExceptionDialog(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Look! An Exception Dialog");
        alert.setContentText("Something went wrong... :(");

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public void loadPanel(String fxmlFile, Modality modal, boolean resizable, String title, StageStyle stageStyle, boolean moveWindow) {
        Parent root;
        try {
            Scene scene = new Scene(loadFXML(fxmlFile));
            Stage stage = new Stage();
            root = scene.getRoot();
            stage.setResizable(resizable);
            stage.initModality(modal);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.initStyle(stageStyle);
            stage.setMinHeight(600);
            stage.setMinWidth(850);
            if(moveWindow) {
                Applicazione.getInstance().getCommonEvents().moveWindow(root, stage);
            }
            stage.show();
            closeApp(stage);
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (Exception e) {
            showExceptionDialog(e);
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void changeBorderColor(TextField t, String color) {
        t.setStyle("-fx-border-color:" + color + ";" + "-fx-background-color:" + Costanti.COLORE_PRIMARIO + ";" + "-fx-background-insets: 0;" + "-fx-border-radius: 20; -fx-text-fill: #fff;");
    }

    public void changeButtonColor(Button b, String color) {
        if(!b.isDisabled()) {
            b.setStyle("-fx-background-color:" + color);
        }
    }

    public void hideWindow(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public void setProgressBar(String message, int index) {
        ProgressBar pb = (ProgressBar) Applicazione.getInstance().getModello().getObject(Costanti.PROGRESS_BAR);
        Label labelProgress = (Label) Applicazione.getInstance().getModello().getObject(Costanti.LABEL_PROGRESS);
        if(index > 5 || index < 0) {
            pb.setProgress(Costanti.values[1]);
        } else {
            pb.setProgress(Costanti.values[index]);
        }
        labelProgress.setText(message);
    }


}
