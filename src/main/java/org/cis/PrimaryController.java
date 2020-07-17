package org.cis;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.cis.controllo.CommonEvents;
import org.cis.controllo.Operator;
import org.cis.controllo.TaskSaveRepository;
import org.cis.controllo.Utils;
import org.cis.modello.*;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PrimaryController {

    @FXML
    private Button bottoneCerca, buttonFilterLanguage, bottoneSalva, bottoneAggiungiQuery,
            bottoneEliminaQuery, bottoneStop, bottoneEliminaBulk, bottoneEliminaSelezionato, buttonFilterProgrLanguage;

    @FXML
    private TextField campoToken, campoParametroQ1, campoParametroQ2, campoParametroQ3,
            campoCercaTabella, campoQ3, campoSort, campoOrder, campoQ1, campoQ2, campoKeyOrder, campoKeySort;

    @FXML
    private TableView<Repository> tableRepository;

    @FXML
    private TableColumn<Repository, String> columnRepo, columnDataCommit, columnURL, columnLingua, columnLinguaggio, columnDimensione, columnStars;

    @FXML
    private ComboBox comboParametriRicerca;

    @FXML
    private AnchorPane anchorQuery;

    @FXML
    private Label labelErrori, labelProgress;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ImageView iconFilterLang, iconAddQuery, iconSave, iconSearch, iconRemoveQuery,
            iconStop, iconDeleteBulk, iconDeleteSelected, iconFilterProgr;

    @FXML
    private CheckBox checkStrictMode;

    @FXML
    private DatePicker datePickerStart, datePickerEnd;

    @FXML
    private Tab tabResults, tabQueries;

    @FXML
    private TabPane tabbedPane;

    private List<TextField> listaCampiQuery = new ArrayList<>();
    private List<TextField> listaCampiChiavi = new ArrayList<>();
    private boolean hoursFlag = false;


    @FXML
    private void initialize() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        this.eventiCampi();
        bottoneCerca.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Constants.HOVER_COLOR);}});
        bottoneCerca.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Constants.COLORE_BUTTON);}});
        bottoneCerca.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {actionCerca();}});

        // Setting Listener Tap 2.
        buttonFilterProgrLanguage.setOnAction(actionEvent -> cloneRepositories(() -> filterByProgrammingLanguage()));
        buttonFilterLanguage.setOnAction(actionEvent -> cloneRepositories(() -> filterByLanguage()));

        buttonFilterLanguage.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterLanguage, Constants.HOVER_COLOR);}});
        buttonFilterLanguage.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterLanguage, Constants.COLORE_BUTTON);}});
        bottoneSalva.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, "#00ff00");}});
        bottoneSalva.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, "#99ff33");}});
        bottoneAggiungiQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Constants.HOVER_COLOR);}});
        bottoneAggiungiQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Constants.COLORE_BUTTON);}});
        bottoneAggiungiQuery.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {aggiungiCampoQuery();}});
        bottoneEliminaQuery.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {eliminaCampoQuery();}});
        bottoneEliminaQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Constants.HOVER_COLOR);}});
        bottoneEliminaQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Constants.COLORE_BUTTON);}});
        bottoneStop.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, "#ff0000");}});
        bottoneStop.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, "#cc3333");}});
        bottoneStop.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) { Applicazione.getInstance().getModello().addObject(Constants.MESSAGGIO_FINE_RICERCA,"Ricerca Stoppata!");stopThread();}});

        bottoneEliminaSelezionato.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, "#ff0000");}});
        bottoneEliminaSelezionato.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, "#cc3333");}});
        bottoneEliminaSelezionato.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {deleteSelectedItem();}});
        bottoneEliminaBulk.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, "#ff0000");}});
        bottoneEliminaBulk.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, "#cc3333");}});
        bottoneEliminaBulk.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {deleteInBulk();}});
        buttonFilterProgrLanguage.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterProgrLanguage, Constants.HOVER_COLOR);}});
        buttonFilterProgrLanguage.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterProgrLanguage, Constants.COLORE_BUTTON);}});

        checkStrictMode.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {cercaInTabella();}});

        this.bottoneCerca.setDisable(true);
        this.tabResults.setDisable(true);
        this.enableDisableRemoveButton(true);
        this.bottoneEliminaSelezionato.setDisable(true);
        initIcons();
        initCombo();
        initDatePickers();
        this.initTable();
        this.initProgressBar();
    }

    private void eventiCampi() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        campoToken.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoToken, Constants.HOVER_COLOR);}});
        campoToken.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoToken, Constants.COLORE_BUTTON);}});
        campoToken.setOnKeyReleased(new EventHandler<KeyEvent>() {@Override public void handle(KeyEvent keyEvent) {enableDisableSearchButton();}});
        campoToken.setOnKeyPressed(new EventHandler<KeyEvent>() {@Override public void handle(KeyEvent keyEvent) {enableDisableSearchButton();}});
        campoParametroQ1.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ1, Constants.HOVER_COLOR);}});
        campoParametroQ1.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ1, Constants.COLORE_BUTTON);}});
        campoParametroQ2.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ2, Constants.HOVER_COLOR);}});
        campoParametroQ2.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ2, Constants.COLORE_BUTTON);}});
        campoParametroQ3.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ3, Constants.HOVER_COLOR);}});
        campoParametroQ3.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ3, Constants.COLORE_BUTTON);}});
        campoCercaTabella.setOnKeyPressed(new EventHandler<KeyEvent>() {@Override public void handle(KeyEvent keyEvent) {cercaInTabella();}});
        campoCercaTabella.setOnKeyReleased(new EventHandler<KeyEvent>() {@Override public void handle(KeyEvent keyEvent) {cercaInTabella();}});
        campoCercaTabella.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoCercaTabella, Constants.HOVER_COLOR);}});
        campoCercaTabella.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoCercaTabella, Constants.COLORE_BUTTON);}});
        campoQ1.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ1, Constants.HOVER_COLOR);}});
        campoQ1.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ1, Constants.COLORE_BUTTON);}});
        campoQ2.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ2, Constants.HOVER_COLOR);}});
        campoQ2.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ2, Constants.COLORE_BUTTON);}});
        campoQ3.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ3, Constants.HOVER_COLOR);}});
        campoQ3.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ3, Constants.COLORE_BUTTON);}});
        campoSort.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoSort, Constants.HOVER_COLOR);}});
        campoSort.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoSort, Constants.COLORE_BUTTON);}});
        campoOrder.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoOrder, Constants.HOVER_COLOR);}});
        campoOrder.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoOrder, Constants.COLORE_BUTTON);}});
        campoKeyOrder.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoKeyOrder, Constants.HOVER_COLOR);}});
        campoKeyOrder.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoKeyOrder, Constants.COLORE_BUTTON);}});
        campoKeySort.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoKeySort, Constants.HOVER_COLOR);}});
        campoKeySort.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoKeySort, Constants.COLORE_BUTTON);}});

        Applicazione.getInstance().getModello().addObject(Constants.ULTIMO_CAMPO_KEY, campoQ3);
        initListaTextField();
    }

    private void initProgressBar() {
        Float[] values = Constants.values;
        Applicazione.getInstance().getModello().addObject(Constants.PROGRESS_BAR, progressBar);
        Applicazione.getInstance().getModello().addObject(Constants.LABEL_PROGRESS, labelProgress);
        progressBar.setProgress(values[0]);
        int maxStatus = 12;
        // Create the Property that holds the current status count
        IntegerProperty statusCountProperty = new SimpleIntegerProperty(1);
        // Create the timeline that loops the statusCount till the maxStatus
        Timeline timelineBar = new Timeline(new KeyFrame(Duration.millis(1000), new KeyValue(statusCountProperty, maxStatus)));
        // The animation should be infinite
        timelineBar.setCycleCount(Timeline.INDEFINITE);
        timelineBar.play();
        // Add a listener to the statusproperty
        statusCountProperty.addListener((ov, statusOld, statusNewNumber) -> {
            int statusNew = statusNewNumber.intValue();
            // Remove old status pseudo from progress-bar
            progressBar.pseudoClassStateChanged(PseudoClass.getPseudoClass("status" + statusOld.intValue()), false);
            // Add current status pseudo from progress-bar
            progressBar.pseudoClassStateChanged(PseudoClass.getPseudoClass("status" + statusNew), true);
        });
    }

    private void initTableCells() {
        columnRepo.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnDataCommit.setCellValueFactory(cellData -> cellData.getValue().getDataProperty());
        columnURL.setCellValueFactory(cellData -> cellData.getValue().urlProjectProperty());
        columnDimensione.setCellValueFactory(cellData -> cellData.getValue().turnIntToStringProperty());
        columnLingua.setCellValueFactory(cellData -> cellData.getValue().languagePropertyProperty());
        columnLinguaggio.setCellValueFactory(cellData -> cellData.getValue().programmingLanguagesPropertyProperty());
        columnStars.setCellValueFactory(cellData -> cellData.getValue().starsProperty());
    }

    private void initListaTextField() {
        this.listaCampiQuery.add(this.campoParametroQ1);
        this.listaCampiQuery.add(this.campoParametroQ2);
        this.listaCampiQuery.add(this.campoParametroQ3);

        this.listaCampiChiavi.add(this.campoQ1);
        this.listaCampiChiavi.add(this.campoQ2);
        this.listaCampiChiavi.add(this.campoQ3);
    }

    private void initIcons() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        this.iconAddQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Constants.HOVER_COLOR);}});
        this.iconAddQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Constants.COLORE_BUTTON);}});
        this.iconSearch.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Constants.HOVER_COLOR);}});
        this.iconSearch.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Constants.COLORE_BUTTON);}});
        this.iconSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneCerca.fire();}});
        this.iconFilterLang.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterLanguage, Constants.HOVER_COLOR);}});
        this.iconFilterLang.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterLanguage, Constants.COLORE_BUTTON);}});
        this.iconFilterLang.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonFilterLanguage.fire();}});
        this.iconSave.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, "#00ff00");}});
        this.iconSave.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, "#99ff33");}});
        this.iconSave.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneSalva.fire();}});

        this.iconFilterLang.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonFilterLanguage.fire();}});
        this.iconAddQuery.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {aggiungiCampoQuery();}});;
        this.iconRemoveQuery.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {eliminaCampoQuery();}});
        this.iconRemoveQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Constants.HOVER_COLOR);}});
        this.iconRemoveQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Constants.COLORE_BUTTON);}});

        this.iconStop.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, "#ff0000");}});
        this.iconStop.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, "#cc3333");}});
        iconStop.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {stopThread();}});

        iconDeleteBulk.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneEliminaBulk.fire();}});
        iconDeleteBulk.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, "#ff0000");}});
        iconDeleteBulk.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, "#cc3333");}});

        iconDeleteSelected.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneEliminaSelezionato.fire();}});
        iconDeleteSelected.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, "#ff0000");}});
        iconDeleteSelected.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, "#cc3333");}});

        iconFilterProgr.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterProgrLanguage, Constants.HOVER_COLOR);}});
        iconFilterProgr.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterProgrLanguage, Constants.COLORE_BUTTON);}});
        iconFilterProgr.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonFilterProgrLanguage.fire();}});
    }

    private LocalDate getValueDataPicker(DatePicker datePicker) {
        if(datePicker.getValue() == null) {
            return null;
        }
        LocalDate localDate =  datePicker.getValue();
        return localDate;
    }

    private void initDatePickers() {
        datePickerStart.valueProperty().addListener((ov, oldValue, newValue) -> {
            LocalDate localDateStart = getValueDataPicker(datePickerStart);
            LocalDate localDateEnd = getValueDataPicker(datePickerEnd);
            if(datePickerEnd.getValue() == null || localDateStart.compareTo(localDateEnd) > 0) {
                datePickerEnd.setValue(localDateStart);
            }
        });

        datePickerEnd.valueProperty().addListener((ov, oldValue, newValuw) -> {
            LocalDate localDateStart = getValueDataPicker(datePickerStart);
            LocalDate localDateEnd = getValueDataPicker(datePickerEnd);
            if(datePickerStart.getValue() == null || localDateEnd.compareTo(localDateStart) < 0) {
                datePickerStart.setValue(localDateEnd);
            }
        });

        LocalDate dateStart = LocalDate.of(2007, 10, 29);
        datePickerStart.setValue(dateStart);
        datePickerEnd.setValue(LocalDate.now());
    }

    public String getDatePickerStartInstant() {
        LocalDate localDate = datePickerStart.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        instant = instant.plus(1, ChronoUnit.DAYS);
        String dateInstant = instant.toString();
        String formattedDate = dateInstant.substring(0, dateInstant.length() - 1) + "..";
        formattedDate = formattedDate.replace("T23", "T00");
        formattedDate = formattedDate.replace("T22", "T00");
        System.out.println("Data formattata Start!!! *** " + formattedDate);
        return formattedDate;
    }

    public String getDatePickerEndInstant() {
        LocalDate localDate = datePickerEnd.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        instant = instant.plus(1, ChronoUnit.DAYS);
        String dateInstant = instant.toString();
        String formattedDate = dateInstant.substring(0, dateInstant.length() - 1);
        formattedDate = formattedDate.replace("T22", "T00");
        formattedDate = formattedDate.replace("T23", "T00");
        System.out.println("Data formattata End!!! *** " + formattedDate);
        return formattedDate;
    }

    private void enableDisableRemoveButton(boolean b) {
        this.bottoneEliminaQuery.setDisable(b);
        this.iconRemoveQuery.setDisable(b);
    }

    private void enableDisableSearchButton() {
        if(campoToken.getText().isEmpty()) {
            bottoneCerca.setDisable(true);
        } else {
            bottoneCerca.setDisable(false);
        }
    }

    private void initTable() {
        this.tableRepository.setOnMouseClicked(mouseEvent -> selectItemTableEvent());
    }


    private void setTable() {
        ObservableList<Repository> listaRepoAgg = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO_AGGIORNATA);
        System.out.println("Lista size: " + listaRepoAgg.size());
        initTableCells();
        this.tableRepository.setItems(listaRepoAgg);
        this.tableRepository.refresh();
    }

    private void cercaInTabella() {
        this.bottoneEliminaSelezionato.setDisable(true);
        eventoCercaInTabella();
        setTable();
    }

    private void eventoCercaInTabella() {
        Modello modello = Applicazione.getInstance().getModello();
        ObservableList<Repository> listaOriginale = (ObservableList<Repository>) modello.getObject(Constants.LISTA_REPO);
        boolean strict = this.checkStrictMode.isSelected();
        String daCercare = this.campoCercaTabella.getText();
        ObservableList<Repository> listaAgg = Operator.cercaPerNome(listaOriginale, daCercare, getSelectedComboLanguage(), strict);
        modello.addObject(Constants.LISTA_REPO_AGGIORNATA, listaAgg);
    }


    private String getSelectedComboLanguage() {
        Object valore = this.comboParametriRicerca.getValue();
        if(valore != null) {
            return valore.toString();
        }
        return Constants.PARAM_REPOSITORIES;
    }

    public void initCombo() {
        comboParametriRicerca.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {getSelectedComboLanguage(); cercaInTabella();});
        String parametri[] = {Constants.PARAM_REPOSITORIES, Constants.PARAM_LANGUAGE, Constants.PARAM_PROGR_LANGUAGE, Constants.PARAM_DATE_COMMIT, Constants.PARAM_URL, Constants.PARAM_DIMENSION, Constants.PARAM_STARS};
        ObservableList<String> listaLinguaggi = FXCollections.observableArrayList(parametri);
        comboParametriRicerca.setItems(listaLinguaggi);
    }

    private void aggiungiCampoQuery() {
        TextField nuovaQuery = new TextField();
        TextField nuovaKey = new TextField();
        TextField vecchiaKey = (TextField) Applicazione.getInstance().getModello().getObject(Constants.ULTIMO_CAMPO_KEY);
        nuovaKey.setEditable(true);
        this.listaCampiChiavi.add(nuovaKey);

        anchorQuery.setLeftAnchor(nuovaQuery, 240.0);
        anchorQuery.setRightAnchor(nuovaQuery, 90.0);
        anchorQuery.setLeftAnchor(nuovaKey, 95.0);

        nuovaQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaQuery, Constants.HOVER_COLOR);}});
        nuovaQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaQuery, Constants.COLORE_BUTTON);}});
        nuovaKey.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaKey, Constants.HOVER_COLOR);}});
        nuovaKey.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaKey, Constants.COLORE_BUTTON);}});

        TextField ultimaInserita = this.listaCampiQuery.get(this.listaCampiQuery.size()-1);
        setTextFieldProperties(ultimaInserita, nuovaQuery, ultimaInserita.getPromptText());
        setTextFieldProperties(vecchiaKey, nuovaKey, "Key");

        Applicazione.getInstance().getModello().addObject(Constants.ULTIMO_CAMPO_KEY, nuovaKey);
        this.listaCampiQuery.add(nuovaQuery);
        this.anchorQuery.getChildren().add(nuovaQuery);
        this.anchorQuery.getChildren().add(nuovaKey);
        this.enableDisableRemoveButton(false);
    }

    private void setTextFieldProperties(TextField ultimaInserita, TextField nuova, String promptText) {
        nuova.setPrefHeight(ultimaInserita.getPrefHeight());
        nuova.setPrefWidth(ultimaInserita.getPrefWidth());
        nuova.setFont(ultimaInserita.getFont());
        nuova.setPromptText(promptText);
        nuova.setStyle("-fx-background-color:" + Constants.COLORE_PRIMARIO + ";" + " -fx-border-color:" + Constants.COLORE_BUTTON + ";" +" -fx-border-radius: 20; -fx-background-insets: 0; -fx-text-fill: #fff;");
        nuova.setLayoutX(ultimaInserita.getLayoutX());
        nuova.setMaxHeight(ultimaInserita.getMaxHeight());
        nuova.setMaxWidth(ultimaInserita.getMaxWidth());
        nuova.setLayoutY(ultimaInserita.getLayoutY() + 40);
    }

    private void eliminaCampoQuery() {
        this.anchorQuery.getChildren().remove(this.listaCampiQuery.get(this.listaCampiQuery.size()-1));
        this.anchorQuery.getChildren().remove(this.listaCampiChiavi.get(this.listaCampiChiavi.size()-1));
        this.listaCampiQuery.remove(this.listaCampiQuery.size()-1);
        this.listaCampiChiavi.remove(this.listaCampiChiavi.size()-1);
        Applicazione.getInstance().getModello().addObject(Constants.ULTIMO_CAMPO_KEY, this.listaCampiChiavi.get(this.listaCampiChiavi.size()-1));
        if(this.listaCampiChiavi.size() == 3) {
            this.enableDisableRemoveButton(true);
        }
    }

    private void filterByLanguage() {
        Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Detecting language in progress...")), 1500);
        // ANAS CODE...
        Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Detecting language complete")), 1500);
        Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 2500);
    }

    private void filterByProgrammingLanguage() {


        Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Rilevamento del linguaggio di programmazione/markup in corso...")), 1500);

        List<Repository> repositories = (List<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO);
        for (int i = 0; i < repositories.size(); i++) {
            repositories.get(i).displayProgrammingLanguages();
            int taskWorkProgress = (int) ((100.0 / repositories.size()) * (i + 1));
            // todo: rivedi come settare il valore della progressBar.
            if (taskWorkProgress < 17) {
                progressBar.setProgress(Constants.values[0]);
            } else if (taskWorkProgress >= 33) {
                progressBar.setProgress(Constants.values[1]);
            } else if (taskWorkProgress >= 50) {
                progressBar.setProgress(Constants.values[2]);
            } else if (taskWorkProgress >= 67) {
                progressBar.setProgress(Constants.values[3]);
            } else if (taskWorkProgress >= 83) {
                progressBar.setProgress(Constants.values[4]);
            } else if (taskWorkProgress == 100) {
                progressBar.setProgress(Constants.values[5]);
            }

            System.out.println("Percentuale progressBar: " + taskWorkProgress);
        }
        // Reset progressBar.
        progressBar.setProgress(Constants.values[0]);

        Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Detecting programming language completed")), 1500);
        Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 2500);
        disableAllUIElementsResults(false);

    }

    private void cloneRepositories(Runnable postExecute) {

        disableAllUIElementsResults(true);


        List<Repository> repositories = (List<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO);

        if (repositories == null || repositories.isEmpty()) {
            System.out.println("Esegui prima una query di ricerca \uD83D\uDE0E");
            labelProgress.setText("You must run a search query first");
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 1500);
            disableAllUIElementsResults(false);
            return;
        }

        int indexLastClonedRepository = (int) Applicazione.getInstance().getModello().getObject(Constants.INDEX_LAST_CLONED_REPOSITORY);

        if ((indexLastClonedRepository + 1) == repositories.size()) {
            //Tutti i repository sono già stati clonati per questa sessione di ricerca.
            postExecute.run();
            disableAllUIElementsResults(false);

            return;
        }

        int firstNonClonedRepositoryIndex = 0;
        if (indexLastClonedRepository != -1) {
            // Qualcosa è andato storto...riprendo a clonare dal repo il cui indice è: indexLastClonedRepository + 1.
            firstNonClonedRepositoryIndex = indexLastClonedRepository + 1;
        }

        String token = Applicazione.getInstance().getSessionManager().getCurrentSession().getQuery().getToken();
        TaskSaveRepository task = new TaskSaveRepository(repositories, firstNonClonedRepositoryIndex, token);
        //# Setting event handler on task
        task.setOnSucceeded(workerStateEvent -> {
            // Reset progressBar, labelProgress.
            progressBar.progressProperty().unbind();
            progressBar.setProgress(Constants.values[0]);
            labelProgress.textProperty().unbind();


            System.out.println("Tutti i repository sono stati clonati correttamente per questa sessione di ricerca");
            labelProgress.setText("Repositories cloned correctly");

            postExecute.run();
        });

        task.setOnFailed(workerStateEvent -> {
            workerStateEvent.getSource().getException().printStackTrace();
            task.cancel(true);

            // Reset progressBar, labelProgress.
            progressBar.progressProperty().unbind();
            progressBar.setProgress(Constants.values[0]);
            labelProgress.textProperty().unbind();

            System.out.println("Qualcosa è andato storto...limit rate raggiunto o problemi di connessione");
            labelProgress.setText("Something went wrong...limit rate reached or connection issues");
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 1500);
        });

        progressBar.progressProperty().bind(task.progressProperty());
        labelProgress.textProperty().bind(task.messageProperty());
        //Applicazione.getInstance().getSingleThread().executeTask(task);
        Thread exe = new Thread(task);
        exe.setName("Thread-Cloning");
        exe.start();
    }

    private void actionCerca() {
        this.labelErrori.setText("");
        List<Qualifier> qualifiers = createListQualifiers();
        if(qualifiers != null) {
            Query query = new Query(qualifiers);
            setOptionalFields(query);
            Session session = new Session(null);
            session.setQuery(query);
            Applicazione.getInstance().getSessionManager().addSession(session);
            System.out.println("Session created!");
            System.out.println("Numero sessioni: " + Applicazione.getInstance().getSessionManager().getSessions().size());

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (Applicazione.getInstance().getModello().getObject(Constants.THREAD_DOWNLOAD_REPO)) {
                        try {
                            Applicazione.getInstance().getModello().getObject(Constants.THREAD_DOWNLOAD_REPO).wait();
                        } catch (Exception ex) {
                            Platform.runLater(new Runnable() {@Override public void run() {Applicazione.getInstance().getCommonEvents().showExceptionDialog(ex);}});
                            ex.printStackTrace();
                        }
                    }
                    //Applicazione.getInstance().getSessionManager().getSessions().clear();
                    boolean delete = (boolean) Applicazione.getInstance().getModello().getObject(Constants.ACCEPT_DELETION_PROCESS);
                    if(delete == false) {
                        Platform.runLater(new Runnable() {@Override public void run() {labelErrori.setText("Operation aborted.");}});
                        return;
                    }
                    if(query.getToken() != null) {
                        disableAllUIElements(true);

                        Operator.createConfigProperties();
                        System.out.println("Properties! creato");

                        if (Operator.avvioGHRepoSearcher()) {
                            System.out.println("fine GHrepoSearcher!");
                            String path = new File("").getAbsolutePath();
                            path = path + "\\risorse\\json";
                            System.out.println("***Path: " + path + "***");
                            List<Repository> lista = Applicazione.getInstance().getDaoRepositoryJSON().loadRepositories(path);
                            ObservableList<Repository> tabList = FXCollections.observableArrayList(lista);
                            Applicazione.getInstance().getModello().addObject(Constants.LISTA_REPO_AGGIORNATA, tabList);
                            Applicazione.getInstance().getModello().addObject(Constants.LISTA_REPO, tabList);
                            // Init Cloning.
                            Applicazione.getInstance().getModello().addObject(Constants.INDEX_LAST_CLONED_REPOSITORY, -1);
                            Platform.runLater(new Runnable() {@Override public void run() {setTable();}});
                            //lancio loadRepo da Dao

                            //ottengo lista Repo

                            //Utilizzo dati per riempire Tabella
                            Applicazione.getInstance().getModello().addObject(Constants.MESSAGGIO_FINE_RICERCA,"The search went just AWSOME!");

                            stopThread();
                        } else {
                            System.out.println("Errore GHrepoSearcher!");
                            stopThread();
                        }

                    } else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                labelErrori.setText("Insert token!");
                                Applicazione.getInstance().getCommonEvents().changeBorderColor(campoToken, "#ff0000");
                            }
                        });
                    }
                    disableAllUIElements(false);
                }
            });

            Applicazione.getInstance().getModello().addObject(Constants.THREAD_DOWNLOAD_REPO, thread);
            Applicazione.getInstance().getCommonEvents().loadPanel("WarningPanel", Modality.APPLICATION_MODAL, false, "Filtro", StageStyle.UNDECORATED, true);
            thread.start();
        }
    }

    private List<Qualifier> createListQualifiers() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        List<Qualifier> listQualifiers = new ArrayList<>();
        int i = 0;
        for(TextField t : this.listaCampiChiavi) {

            boolean presenza = checkKeyPresence(t.getText());
            if(presenza && !t.getText().isEmpty() && !this.listaCampiQuery.get(i).getText().isEmpty()) {
                listQualifiers.add(new Qualifier(t.getText(), this.listaCampiQuery.get(i).getText()));
                System.out.println("qual added");
            } else if(presenza && !t.getText().isEmpty() && this.listaCampiQuery.get(i).getText().isEmpty()) {
                commonEvents.changeBorderColor(t, "#ff0000");
                this.labelErrori.setText("If you put a key you must put a value too!");
                return null;
            } else if(listQualifiers.size() > 0 && t.getText().isEmpty()) {
                return listQualifiers;
            } else {
                commonEvents.changeBorderColor(t, "#ff0000");
                this.labelErrori.setText("Check the queries in red!");
            }
            System.out.println("key: " + t.getText());
            System.out.println("valore: " + this.listaCampiQuery.get(i).getText());
            i++;
        }
        return listQualifiers;
    }

    private void setOptionalFields(Query q) {
        if(!this.campoOrder.getText().isEmpty()) {
            q.setOrder(this.campoOrder.getText());
        }
        if(!this.campoSort.getText().isEmpty()) {
            q.setSort(this.campoSort.getText());
        }
        if(!this.campoToken.getText().isEmpty()) {
            q.setToken(this.campoToken.getText());
        }
        q.setDate(getDatePickerStartInstant() + getDatePickerEndInstant());
    }

    private boolean checkKeyPresence(String key) {
        for(int i = 0; i < Constants.LISTA_QUAL.length; i++) {
            if(Constants.LISTA_QUAL[i].equalsIgnoreCase(key.trim())) {
                return true;
            }
        }
        return false;
    }

    private void stopThread() {
        Thread thread = (Thread) Applicazione.getInstance().getModello().getObject(Constants.THREAD_DOWNLOAD_REPO);
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        if(thread != null) {
            thread.interrupt();
            String messaggio = (String) Applicazione.getInstance().getModello().getObject(Constants.MESSAGGIO_FINE_RICERCA);
            Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar(messaggio, 5);}});
            Applicazione.getInstance().getModello().addObject(Constants.MESSAGGIO_FINE_RICERCA,null);
        } else {
            return;
        }
        Process process = (Process) Applicazione.getInstance().getModello().getObject(Constants.THREAD_REPO_SEARCHER);
        if(process != null){
            process.destroy();
        }
        Applicazione.getInstance().getModello().addObject(Constants.THREAD_DOWNLOAD_REPO, null);
        Applicazione.getInstance().getModello().addObject(Constants.THREAD_REPO_SEARCHER, null);
        ObservableList<Repository> lista = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO);
        if(lista == null) {
            return;
        }
        disableAllUIElements(false);
        if(!lista.isEmpty()) {
            tabbedPane.getSelectionModel().select(tabResults);
        }
    }

    private void deleteInBulk() {
        this.bottoneEliminaSelezionato.setDisable(true);
        ObservableList<Repository> listaAgg = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO_AGGIORNATA);
        ObservableList<Repository> listaCompleta = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO);
        if(listaAgg == null) {
            listaCompleta.clear();
            this.tableRepository.setItems(listaCompleta);
            return;
        }
        listaCompleta.removeAll(listaAgg);
        listaAgg.removeAll(listaAgg);
        this.tableRepository.setItems(listaAgg);
        checkListEmpty();
    }

    private void deleteSelectedItem() {
        ObservableList<Repository> listaAgg = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO_AGGIORNATA);
        ObservableList<Repository> listaCompleta = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO);
        int selectedIndex = this.tableRepository.getSelectionModel().getSelectedIndex();
        this.bottoneEliminaSelezionato.setDisable(true);
        if(selectedIndex != -1) {
            if(listaAgg == null) {
                listaCompleta.remove(selectedIndex);
                this.tableRepository.setItems(listaCompleta);
                return;
            }
            Repository repo = listaAgg.get(selectedIndex);
            listaCompleta.remove(repo);
            listaAgg.remove(repo);
            this.tableRepository.setItems(listaAgg);
        }
        checkListEmpty();
    }

    private void checkListEmpty() {
        ObservableList<Repository> lista = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO);
        if(lista.isEmpty()) {
            tabbedPane.getSelectionModel().select(tabQueries);
            tabResults.setDisable(true);
        }
    }

    private void selectItemTableEvent() {
        if(this.tableRepository.getSelectionModel().getSelectedIndex() != -1) {
            this.bottoneEliminaSelezionato.setDisable(false);
        } else {
            this.bottoneEliminaSelezionato.setDisable(true);
        }
    }

    private void disableAllUIElements(boolean value) {
        for(int i = 0; i < listaCampiChiavi.size(); i++) {
            listaCampiChiavi.get(i).setDisable(value);
            listaCampiQuery.get(i).setDisable(value);
        }
        campoToken.setDisable(value);
        campoSort.setDisable(value);
        campoOrder.setDisable(value);
        datePickerStart.setDisable(value);
        datePickerEnd.setDisable(value);
        if(value == false && this.listaCampiQuery.size() > 3) {
            iconRemoveQuery.setDisable(value);
            bottoneEliminaQuery.setDisable(value);
        }
        iconAddQuery.setDisable(value);
        bottoneAggiungiQuery.setDisable(value);
        bottoneCerca.setDisable(value);
        iconSearch.setDisable(value);
        campoKeyOrder.setDisable(value);
        campoKeySort.setDisable(value);
        ObservableList<Repository> lista = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO);
        if(value == false) {
            if(lista != null && !lista.isEmpty()) {
                tabResults.setDisable(value);
                tabbedPane.getSelectionModel().select(tabResults);
            }
        } else {
            tabResults.setDisable(value);
        }
    }

    private void disableAllUIElementsResults(boolean value) {
        System.out.println("disabilito");

        tableRepository.setDisable(value);
        iconFilterLang.setDisable(value);
        iconDeleteBulk.setDisable(value);
        iconSave.setDisable(value);
        iconSearch.setDisable(value);
        iconFilterProgr.setDisable(value);
        buttonFilterLanguage.setDisable(value);
        buttonFilterProgrLanguage.setDisable(value);
        bottoneEliminaBulk.setDisable(value);
        tabbedPane.setDisable(value);
    }

    private void filterByIdiom() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Repository> lista = (List<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LISTA_REPO);
                if(lista == null) {
                    return;
                }
                Operator.actionDetectIdiom();
            }
        });
    }

}
