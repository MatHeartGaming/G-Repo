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
        boolean legacy = (boolean) Applicazione.getInstance().getModello().getObject(Constants.LEGACY_MODE_ON);
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        if(legacy) {
            initLegacy();
        } else {
            buttonCancel.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, Constants.COLOR_BUTTON_CLEARER_HOVER);}});
            buttonCancel.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, Constants.COLOR_BUTTON_CLEARER);}});
            buttonAccept.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.COLOR_BUTTON_CLEARER_HOVER);}});
            buttonAccept.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.COLOR_BUTTON_CLEARER);}});
            initIcons();
        }
        buttonAccept.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {commonEvents.actionAccept(actionEvent);}});
        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {commonEvents.actionCancel(actionEvent);}});
        iconAccept.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonAccept.fire();}});
        iconCancel.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonCancel.fire();}});
        Applicazione.getInstance().getModello().addObject(Constants.ACCEPT_WARNING_MEX, false);
    }

    private void initIcons() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        iconCancel.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, Constants.COLOR_BUTTON_CLEARER_HOVER);}});
        iconCancel.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, Constants.COLOR_BUTTON_CLEARER);}});
        iconAccept.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.COLOR_BUTTON_CLEARER_HOVER);}});
        iconAccept.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.COLOR_BUTTON_CLEARER);}});
    }

    private void initLegacy() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        commonEvents.changeButtonColor(buttonAccept, Constants.COLOR_BUTTON_LEGACY_PRIMARY);
        commonEvents.changeButtonColor(buttonCancel, Constants.COLOR_BUTTON_LEGACY_RED);
        buttonCancel.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, Constants.COLOR_BUTTON_LEGACY_RED_HOVER);}});
        buttonCancel.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, Constants.COLOR_BUTTON_LEGACY_RED);}});
        buttonAccept.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.COLOR_BUTTON_LEGACY_HOVER);}});
        buttonAccept.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.COLOR_BUTTON_LEGACY_PRIMARY);}});
        iconAccept.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.COLOR_BUTTON_LEGACY_HOVER);}});
        iconAccept.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Constants.COLOR_BUTTON_LEGACY_PRIMARY);}});
        iconCancel.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, Constants.COLOR_BUTTON_LEGACY_RED_HOVER);}});
        iconCancel.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, Constants.COLOR_BUTTON_LEGACY_RED);}});

    }


}
