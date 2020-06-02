package org.cis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.cis.controllo.CommonEvents;

public class SecondaryController {

    @FXML
    private Button bottoneAnnulla, bottoneFiltra;

    @FXML
    private ComboBox comboLanguage;

    @FXML
    private ImageView iconFilter, iconCancel;

    @FXML
    public void initialize() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        bottoneAnnulla.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {commonEvents.hideWindow(actionEvent);}});
        bottoneFiltra.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneFiltra, "#ff8000");}});
        bottoneFiltra.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneFiltra, "#eda678");}});
        bottoneAnnulla.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAnnulla, "#ff0000");}});
        bottoneAnnulla.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAnnulla, "#cc3333");}});
        initCombo();
        initImages();
    }

    private void initImages() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        iconFilter.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneFiltra, "#ff8000");}});
        iconFilter.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneFiltra, "#eda678");}});
        iconCancel.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAnnulla, "#ff0000");}});
        iconCancel.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAnnulla, "#cc3333");}});
    }

    private void initCombo() {
        String linguaggi[] = {"Java", "C#", "PHP", "C", "C++", "Pyhton"};
        ObservableList<String> listaLinguaggi = FXCollections.observableArrayList(linguaggi);
        comboLanguage.setItems(listaLinguaggi);
    }



}
