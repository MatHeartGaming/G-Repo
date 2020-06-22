package org.cis;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import org.cis.controllo.CommonEvents;
import org.cis.controllo.Operatore;
import org.cis.modello.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class PrimaryController {

    @FXML
    private Button bottoneCerca, bottoneFiltra, bottoneSalva, bottoneAggiungiQuery,
            bottoneEliminaQuery, bottoneStop, bottoneEliminaBulk, bottoneEliminaSelezionato;

    @FXML
    private TextField campoToken, campoParametroQ1, campoParametroQ2, campoParametroQ3,
            campoCercaTabella, campoQ3, campoSort, campoOrder, campoQ1, campoQ2, campoKeyOrder, campoKeySort;

    @FXML
    private TableView<Repository> tableRepository;

    @FXML
    private TableColumn<Repository, String> columnRepo, columnDataCommit, columnURL, columnLingua, columnLinguaggio, columnDimensione;

    @FXML
    private ComboBox comboParametriRicerca;

    @FXML
    private AnchorPane anchorQuery;

    @FXML
    private Label labelErrori, labelProgress;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ImageView iconFilter, iconAddQuery, iconSave, iconSearch, iconRemoveQuery, iconStop, iconDeleteBulk, iconDeleteSelected;

    @FXML
    private CheckBox checkStrictMode;

    @FXML
    private DatePicker datePickerStart, datePickerEnd;

    private List<TextField> listaCampiQuery = new ArrayList<>();
    private List<TextField> listaCampiChiavi = new ArrayList<>();


    @FXML
    private void initialize() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        this.eventiCampi();
        bottoneCerca.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Costanti.HOVER_COLOR);}});
        bottoneCerca.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Costanti.COLORE_BUTTON);}});
        bottoneCerca.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {actionCerca();}});
        bottoneFiltra.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {commonEvents.loadPanel("WarningPanel", Modality.APPLICATION_MODAL, false, "Filtro", StageStyle.UNDECORATED, true);}});
        bottoneFiltra.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneFiltra, Costanti.HOVER_COLOR);}});
        bottoneFiltra.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneFiltra, Costanti.COLORE_BUTTON);}});
        bottoneSalva.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, "#00ff00");}});
        bottoneSalva.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, "#99ff33");}});
        bottoneAggiungiQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Costanti.HOVER_COLOR);}});
        bottoneAggiungiQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Costanti.COLORE_BUTTON);}});
        bottoneAggiungiQuery.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {aggiungiCampoQuery();}});
        bottoneEliminaQuery.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {eliminaCampoQuery();}});
        bottoneEliminaQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Costanti.HOVER_COLOR);}});
        bottoneEliminaQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Costanti.COLORE_BUTTON);}});
        bottoneStop.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, "#ff0000");}});
        bottoneStop.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, "#cc3333");}});
        bottoneStop.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {stopThread();}});

        bottoneEliminaSelezionato.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, "#ff0000");}});
        bottoneEliminaSelezionato.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, "#cc3333");}});
        bottoneEliminaSelezionato.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {deleteSelectedItem();}});
        bottoneEliminaBulk.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, "#ff0000");}});
        bottoneEliminaBulk.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, "#cc3333");}});
        bottoneEliminaBulk.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {deleteInBulk();}});

        checkStrictMode.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {cercaInTabella();}});

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
        campoToken.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoToken, Costanti.HOVER_COLOR);}});
        campoToken.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoToken, Costanti.COLORE_BUTTON);}});
        campoParametroQ1.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ1, Costanti.HOVER_COLOR);}});
        campoParametroQ1.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ1, Costanti.COLORE_BUTTON);}});
        campoParametroQ2.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ2, Costanti.HOVER_COLOR);}});
        campoParametroQ2.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ2, Costanti.COLORE_BUTTON);}});
        campoParametroQ3.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ3, Costanti.HOVER_COLOR);}});
        campoParametroQ3.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ3, Costanti.COLORE_BUTTON);}});
        campoCercaTabella.setOnKeyPressed(new EventHandler<KeyEvent>() {@Override public void handle(KeyEvent keyEvent) {cercaInTabella();}});
        campoCercaTabella.setOnKeyReleased(new EventHandler<KeyEvent>() {@Override public void handle(KeyEvent keyEvent) {cercaInTabella();}});
        campoCercaTabella.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoCercaTabella, Costanti.HOVER_COLOR);}});
        campoCercaTabella.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoCercaTabella, Costanti.COLORE_BUTTON);}});
        campoQ1.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ1, Costanti.HOVER_COLOR);}});
        campoQ1.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ1, Costanti.COLORE_BUTTON);}});
        campoQ2.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ2, Costanti.HOVER_COLOR);}});
        campoQ2.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ2, Costanti.COLORE_BUTTON);}});
        campoQ3.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ3, Costanti.HOVER_COLOR);}});
        campoQ3.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ3, Costanti.COLORE_BUTTON);}});
        campoSort.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoSort, Costanti.HOVER_COLOR);}});
        campoSort.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoSort, Costanti.COLORE_BUTTON);}});
        campoOrder.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoOrder, Costanti.HOVER_COLOR);}});
        campoOrder.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoOrder, Costanti.COLORE_BUTTON);}});
        campoKeyOrder.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoKeyOrder, Costanti.HOVER_COLOR);}});
        campoKeyOrder.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoKeyOrder, Costanti.COLORE_BUTTON);}});
        campoKeySort.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoKeySort, Costanti.HOVER_COLOR);}});
        campoKeySort.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoKeySort, Costanti.COLORE_BUTTON);}});

        Applicazione.getInstance().getModello().addObject(Costanti.ULTIMO_CAMPO_KEY, campoQ3);
        initListaTextField();
    }

    private void initProgressBar() {
        Float[] values = Costanti.values;
        Applicazione.getInstance().getModello().addObject(Costanti.PROGRESS_BAR, progressBar);
        Applicazione.getInstance().getModello().addObject(Costanti.LABEL_PROGRESS, labelProgress);
        progressBar.setProgress(values[0]);
    }

    private void initTableCells() {
        columnRepo.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnDataCommit.setCellValueFactory(cellData -> cellData.getValue().getDataProperty());
        columnURL.setCellValueFactory(cellData -> cellData.getValue().urlProjectProperty());
        columnDimensione.setCellValueFactory(cellData -> cellData.getValue().turnIntToStringProperty());
        columnLingua.setCellValueFactory(cellData -> cellData.getValue().linguaProperty());
        columnLinguaggio.setCellValueFactory(cellData -> cellData.getValue().programmingLanguageProperty());
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
        this.iconAddQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Costanti.HOVER_COLOR);}});
        this.iconAddQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Costanti.COLORE_BUTTON);}});
        this.iconSearch.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Costanti.HOVER_COLOR);}});
        this.iconSearch.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Costanti.COLORE_BUTTON);}});
        this.iconFilter.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneFiltra, Costanti.HOVER_COLOR);}});
        this.iconFilter.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneFiltra, Costanti.COLORE_BUTTON);}});
        this.iconSave.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, "#00ff00");}});
        this.iconSave.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, "#99ff33");}});

        this.iconFilter.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.loadPanel("PannelloFiltra", Modality.APPLICATION_MODAL, false, "Filtro", StageStyle.UNDECORATED, true);}});
        this.iconAddQuery.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {aggiungiCampoQuery();}});;
        this.iconSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {actionCerca();}});
        this.iconRemoveQuery.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {eliminaCampoQuery();}});
        this.iconRemoveQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Costanti.HOVER_COLOR);}});
        this.iconRemoveQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Costanti.COLORE_BUTTON);}});

        this.iconStop.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, "#ff0000");}});
        this.iconStop.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, "#cc3333");}});
        iconStop.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {stopThread();}});

        iconDeleteBulk.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {deleteInBulk();}});
        iconDeleteBulk.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, "#ff0000");}});
        iconDeleteBulk.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, "#cc3333");}});

        iconDeleteSelected.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {deleteSelectedItem();}});
        iconDeleteSelected.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, "#ff0000");}});
        iconDeleteSelected.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, "#cc3333");}});
    }

    private LocalDate getValueDataPicker(DatePicker datePicker) {
        if(datePicker.getValue() == null) {
            return null;
        }
        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        System.out.println(localDate + "\n" + instant + "\n" + date);
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
    }

    public Instant getDatePickerStartInstant() {
        LocalDate localDate = datePickerStart.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return instant;
    }

    public Instant getDatePickerEndInstant() {
        LocalDate localDate = datePickerEnd.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return instant;
    }

    private void enableDisableRemoveButton(boolean b) {
        this.bottoneEliminaQuery.setDisable(b);
        this.iconRemoveQuery.setDisable(b);
    }



    private void initTable() {
        this.tableRepository.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {selectItemTableEvent();}});
        /*new Thread(() -> {
            ObservableList<Repository> listaRepo = FXCollections.observableArrayList(Applicazione.getInstance().getDaoRepositoryMock().loadRepositories(""));
            try {
                //System.out.println(Thread.currentThread().getName());// Thread-3.
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Per non violare la regola del Thread singolo.
            // È equivalente ad SwingUtilities.invokeLater(...).
            Platform.runLater(() -> {
                //System.out.println(Thread.currentThread().getName());// JavaFX Application Thread.
                Applicazione.getInstance().getModello().addObject(Costanti.LISTA_REPO, listaRepo);
                initTableCells();
                this.tableRepository.setItems(listaRepo);
            });
        }).start();*/

        // È equivalente ad SwingWorker.
        Task<ObservableList<Repository>> task = new Task<>() {
            @Override
            protected ObservableList<Repository> call() throws Exception {
                //System.out.println(Thread.currentThread().getName());// Thread-3.
                return FXCollections.observableArrayList(Applicazione.getInstance().getDaoRepositoryMock().loadRepositories(""));
            }
        };
        task.setOnSucceeded(workerStateEvent -> {
            //System.out.println(Thread.currentThread().getName());// JavaFX Application Thread.
            Applicazione.getInstance().getModello().addObject(Costanti.LISTA_REPO, workerStateEvent.getSource().getValue());
            initTableCells();
            this.tableRepository.setItems(task.getValue());
        });

        //new Thread(task).start();
        Applicazione.getInstance().getSingleThread().executeTask(task);
    }


    private void setTable() {
        ObservableList<Repository> listaRepoAgg = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Costanti.LISTA_REPO_AGGIORNATA);
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
        ObservableList<Repository> listaOriginale = (ObservableList<Repository>) modello.getObject(Costanti.LISTA_REPO);
        boolean strict = this.checkStrictMode.isSelected();
        String daCercare = this.campoCercaTabella.getText();
        ObservableList<Repository> listaAgg = Operatore.cercaPerNome(listaOriginale, daCercare, getSelectedComboLanguage(), strict);
        modello.addObject(Costanti.LISTA_REPO_AGGIORNATA, listaAgg);
    }


    private String getSelectedComboLanguage() {
        Object valore = this.comboParametriRicerca.getValue();
        if(valore != null) {
            return valore.toString();
        }
        return Costanti.PARAM_REPOSITORIES;
    }

    public void initCombo() {
        comboParametriRicerca.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {getSelectedComboLanguage(); cercaInTabella();});
        String parametri[] = {Costanti.PARAM_REPOSITORIES, Costanti.PARAM_LINGUA, Costanti.PARAM_LINGUAGGIO, Costanti.PARAM_DATA_COMMIT, Costanti.PARAM_URL, Costanti.PARAM_DIMENSIONE};
        ObservableList<String> listaLinguaggi = FXCollections.observableArrayList(parametri);
        comboParametriRicerca.setItems(listaLinguaggi);
    }

    private void aggiungiCampoQuery() {
        TextField nuovaQuery = new TextField();
        TextField nuovaKey = new TextField();
        TextField vecchiaKey = (TextField) Applicazione.getInstance().getModello().getObject(Costanti.ULTIMO_CAMPO_KEY);
        nuovaKey.setEditable(true);
        this.listaCampiChiavi.add(nuovaKey);

        anchorQuery.setLeftAnchor(nuovaQuery, 240.0);
        anchorQuery.setRightAnchor(nuovaQuery, 90.0);
        anchorQuery.setLeftAnchor(nuovaKey, 95.0);

        nuovaQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaQuery, Costanti.HOVER_COLOR);}});
        nuovaQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaQuery, Costanti.COLORE_BUTTON);}});
        nuovaKey.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaKey, Costanti.HOVER_COLOR);}});
        nuovaKey.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaKey, Costanti.COLORE_BUTTON);}});



        TextField ultimaInserita = this.listaCampiQuery.get(this.listaCampiQuery.size()-1);

        setTextFieldProperties(ultimaInserita, nuovaQuery, ultimaInserita.getPromptText());
        setTextFieldProperties(vecchiaKey, nuovaKey, "Key");

        Applicazione.getInstance().getModello().addObject(Costanti.ULTIMO_CAMPO_KEY, nuovaKey);
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
        nuova.setStyle("-fx-background-color:" + Costanti.COLORE_PRIMARIO + ";" + " -fx-border-color:" + Costanti.COLORE_BUTTON + ";" +" -fx-border-radius: 20; -fx-background-insets: 0; -fx-text-fill: #fff;");
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
        Applicazione.getInstance().getModello().addObject(Costanti.ULTIMO_CAMPO_KEY, this.listaCampiChiavi.get(this.listaCampiChiavi.size()-1));
        if(this.listaCampiChiavi.size() == 3) {
            this.enableDisableRemoveButton(true);
        }
    }

    private void actionCerca() {
        this.labelErrori.setText("");
        List<Qualifier> qualifiers = createListQualifiers();
        if(qualifiers != null && !qualifiers.isEmpty()) {
            Query query = new Query(qualifiers);
            setOptionalFields(query);
            Session session = new Session(FXCollections.observableArrayList(Applicazione.getInstance().getDaoRepositoryMock().loadRepositories("")));
            session.setQuery(query);
            Applicazione.getInstance().getSessionManager().addSession(session);
            System.out.println("Session created!");
            System.out.println("Numero sessioni: " + Applicazione.getInstance().getSessionManager().getSessions().size());

            /*Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(query.getToken() != null) {

                        Operatore.createConfigProperties();
                        System.out.println("Properties! creato");

                        if (Operatore.avvioGHRepoSearcher()) {
                            System.out.println("fine GHrepoSearcher!");
                        } else {
                            System.out.println("Errore GHrepoSearcher!");
                        }

                    } else {
                        labelErrori.setText("Inserire il token!");
                        Applicazione.getInstance().getCommonEvents().changeBorderColor(campoToken, "#ff0000");
                    }
                }
            });*/

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(query.getToken() != null) {

                        Operatore.createConfigProperties();
                        System.out.println("Properties! creato");

                        if (Operatore.avvioGHRepoSearcher()) {
                            System.out.println("fine GHrepoSearcher!");
                        } else {
                            System.out.println("Errore GHrepoSearcher!");
                        }

                    } else {
                        labelErrori.setText("Inserire il token!");
                        Applicazione.getInstance().getCommonEvents().changeBorderColor(campoToken, "#ff0000");
                    }
                }
            });
            Applicazione.getInstance().getModello().addObject(Costanti.THREAD_DOWNLOAD_REPO, thread);
            thread.start();


        }

    }

    private List<Qualifier> createListQualifiers() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        List<Qualifier> listQualifiers = new ArrayList<>();
        int i = 0;
        for(TextField t : this.listaCampiChiavi) {
            if(t.getText().isEmpty() && i == 0) {
                return null;
            }

            boolean presenza = checkKeyPresence(t.getText());
            if(presenza && !t.getText().isEmpty() && !this.listaCampiQuery.get(i).getText().isEmpty()) {
                listQualifiers.add(new Qualifier(t.getText(), this.listaCampiQuery.get(i).getText()));
                System.out.println("qual added");
            } else if(listQualifiers.size() > 0 && t.getText().isEmpty()) {
                return listQualifiers;
            } else {
                commonEvents.changeBorderColor(t, "#ff0000");
                this.labelErrori.setText("Controlla le query in rosso!");
                return null;
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
    }

    private boolean checkKeyPresence(String key) {
        for(int i = 0; i < Costanti.LISTA_QUAL.length; i++) {
            if(Costanti.LISTA_QUAL[i].equalsIgnoreCase(key.trim())) {
                return true;
            }
        }
        return false;
    }

    private void stopThread() {
        Thread thread = (Thread) Applicazione.getInstance().getModello().getObject(Costanti.THREAD_DOWNLOAD_REPO);
        if(thread != null) {
            thread.interrupt();
            Applicazione.getInstance().getCommonEvents().setProgressBar("Operazione interrotta dall'utente...", 0);
        }
        Applicazione.getInstance().getModello().addObject(Costanti.THREAD_DOWNLOAD_REPO, null);
    }

    private boolean verifyValueFields() {

        return false;
    }

    private void deleteInBulk() {
        this.bottoneEliminaSelezionato.setDisable(true);
        ObservableList<Repository> listaAgg = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Costanti.LISTA_REPO_AGGIORNATA);
        ObservableList<Repository> listaCompleta = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Costanti.LISTA_REPO);
        if(listaAgg == null) {
            listaCompleta.clear();
            this.tableRepository.setItems(listaCompleta);
            return;
        }
        listaCompleta.removeAll(listaAgg);
        listaAgg.removeAll(listaAgg);
        this.tableRepository.setItems(listaAgg);
    }

    private void deleteSelectedItem() {
        ObservableList<Repository> listaAgg = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Costanti.LISTA_REPO_AGGIORNATA);
        ObservableList<Repository> listaCompleta = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Costanti.LISTA_REPO);
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
            listaAgg.remove(selectedIndex);
            this.tableRepository.setItems(listaAgg);
        }
    }

    private void selectItemTableEvent() {
        if(this.tableRepository.getSelectionModel().getSelectedIndex() != -1) {
            this.bottoneEliminaSelezionato.setDisable(false);
        } else {
            this.bottoneEliminaSelezionato.setDisable(true);
        }
    }


}
