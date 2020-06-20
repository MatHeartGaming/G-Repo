package org.cis;

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
        buttonCancel.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, "#cc3333");}});

        buttonAccept.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Costanti.HOVER_COLOR);}});
        buttonAccept.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Costanti.COLORE_BUTTON);}});

    }

    private void initIcons() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        iconCancel.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, "#ff0000");}});
        iconCancel.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonCancel, "#cc3333");}});

        iconAccept.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Costanti.HOVER_COLOR);}});
        iconAccept.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonAccept, Costanti.COLORE_BUTTON);}});
    }

}
