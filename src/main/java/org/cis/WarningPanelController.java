package org.cis;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.cis.controllo.CommonEvents;

public class WarningPanelController {

    @FXML
    private Button buttonCancel, buttonAccept;

    @FXML
    private ImageView iconCancel, iconAccept;

    @FXML
    private void initialize() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        buttonCancel.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, "#ff0000");}});
        buttonCancel.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, "#cc3333");}});
        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {actionCancel(actionEvent);}});

        buttonAccept.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.HOVER_COLOR);}});
        buttonAccept.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.COLORE_BUTTON);}});
        buttonAccept.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {actionAccept(actionEvent);}});


        Applicazione.getInstance().getModello().addObject(Constants.ACCEPT_DELETION_PROCESS, false);
        initIcons();
    }

    private void initIcons() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        iconCancel.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, "#ff0000");}});
        iconCancel.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, "#cc3333");}});
        iconCancel.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonCancel.fire();}});

        iconAccept.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.HOVER_COLOR);}});
        iconAccept.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.COLORE_BUTTON);}});
        iconAccept.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonAccept.fire();}});
    }

    private void actionAccept(ActionEvent actionEvent) {
        Applicazione.getInstance().getModello().addObject(Constants.ACCEPT_DELETION_PROCESS, true);
        notifyThread();
        Applicazione.getInstance().getCommonEvents().hideWindow(actionEvent);
    }

    private void actionCancel(ActionEvent actionEvent) {
        Applicazione.getInstance().getModello().addObject(Constants.ACCEPT_DELETION_PROCESS, false);
        notifyThread();
        Applicazione.getInstance().getCommonEvents().hideWindow(actionEvent);
    }

    private void notifyThread() {
        Thread thread = (Thread) Applicazione.getInstance().getModello().getObject(Constants.THREAD_DOWNLOAD_REPO);
        synchronized (thread) {
            thread.notify();
        }
    }

}
