package org.cis.controllo;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cis.App;
import org.cis.Applicazione;
import org.cis.Constants;

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
            Platform.exit();
            System.exit(0);
        });
    }

    public void minimizeStageOfNode(Node node) {
        ((Stage) (node).getScene().getWindow()).setIconified(true);
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
            Applicazione.getInstance().getModello().addObject(Constants.ROOT, root);
            stage.setResizable(resizable);
            stage.initModality(modal);
            stage.setScene(scene);
            stage.setTitle(title);
            if(!isWindows()) {
                stage.initStyle(StageStyle.UNDECORATED);
            } else {
                stage.initStyle(stageStyle);
            }
            if(fxmlFile.equals("primary")) {
                stage.setMinHeight(800);
                stage.setMinWidth(1200);
            }
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

    public boolean isWindows() {
        String os = System.getProperty("os.name");
        if(os.startsWith("Windows")) {
            return true;
        }
        return false;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void changeBorderColor(TextField t, String color) {
        t.setStyle("-fx-border-color:" + color + ";" + "-fx-background-color:" + Constants.COLOR_PRIMARY + ";" + "-fx-background-insets: 0;" + "-fx-border-radius: 8; -fx-text-fill: #000;");
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
        ProgressBar pb = (ProgressBar) Applicazione.getInstance().getModello().getObject(Constants.PROGRESS_BAR);
        Label labelProgress = (Label) Applicazione.getInstance().getModello().getObject(Constants.LABEL_PROGRESS);
        if(index > 5 || index < 0) {
            pb.setProgress(Constants.values[1]);
        } else {
            pb.setProgress(Constants.values[index]);
        }
        labelProgress.setText(message);
    }

    private void notifyThread() {
        Thread thread = (Thread) Applicazione.getInstance().getModello().getObject(Constants.THREAD_WARNING_PANEL);
        synchronized (thread) {
            thread.notify();
        }
    }

    public void actionAccept(ActionEvent actionEvent) {
        Applicazione.getInstance().getModello().addObject(Constants.ACCEPT_WARNING_MEX, true);
        notifyThread();
        Applicazione.getInstance().getCommonEvents().hideWindow(actionEvent);
    }

    public void actionCancel(ActionEvent actionEvent) {
        Applicazione.getInstance().getModello().addObject(Constants.ACCEPT_WARNING_MEX, false);
        notifyThread();
        Applicazione.getInstance().getCommonEvents().hideWindow(actionEvent);
    }

    public void highlightBorders(ImageView imageView, Color color) {
        DropShadow ds = new DropShadow( 6, color);

        imageView.setOnMouseEntered( ( MouseEvent event ) -> {
            imageView.requestFocus();
        });

        imageView.setOnMouseExited((MouseEvent event) -> {
            Parent root = (Parent) Applicazione.getInstance().getModello().getObject(Constants.ROOT);
            root.requestFocus();
        });

        imageView.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> {
            if ( newValue ) {
                imageView.setEffect( ds );
            } else {
                imageView.setEffect(null);
            }
        });
    }
}
