package org.cis;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import javafx.util.Duration;
import org.cis.DAO.DAORepositoryCSV;
import org.cis.controllo.*;
import org.cis.modello.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimaryController extends Window {

    private static final Logger LOG = LoggerFactory.getLogger(PrimaryController.class);


    @FXML
    private Button bottoneCerca, buttonFilterLanguage, bottoneSalva, bottoneAggiungiQuery, buttonClone,
            bottoneEliminaQuery, bottoneStop, bottoneEliminaBulk, bottoneEliminaSelezionato, buttonFilterProgrLanguage;

    @FXML
    private TextField campoToken, campoParametroQ1, campoParametroQ2, campoParametroQ3, fieldPercentage,
            campoCercaTabella, campoQ3, campoQ1, campoQ2;

    @FXML
    private TableView<Repository> tableRepository;

    @FXML
    private TableColumn<Repository, String> columnRepo, columnDataCommit, columnURL, columnLingua, columnLinguaggio, columnDimensione, columnStars;

    @FXML
    private ComboBox comboParametriRicerca;

    @FXML
    private AnchorPane anchorQuery;

    @FXML
    private Label labelErrori, labelProgress, labelRepoNumber;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ImageView iconFilterLang, iconAddQuery, iconSave, iconSearch, iconRemoveQuery,
            iconStop, iconDeleteBulk, iconDeleteSelected, iconFilterProgr, iconClone, imgUnibas, iconExit, iconMinimize;

    @FXML
    private CheckBox checkStrictMode;

    @FXML
    public DatePicker datePickerStart, datePickerEnd;

    @FXML
    private Tab tabResults, tabQueries;

    @FXML
    private TabPane tabbedPane;

    private List<TextField> listaCampiQuery = new ArrayList<>();
    private List<TextField> listaCampiChiavi = new ArrayList<>();
    private List<Button> buttonList = new ArrayList<>();
    private boolean isKeyEmpty = false;



    @FXML
    private void initialize() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        Applicazione.getInstance().getModello().addObject(Constants.LEGACY_MODE_ON, false);
        createButtonList();
        this.eventiCampi();

        bottoneCerca.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Constants.BUTTON_HOVER_COLOR);}});
        bottoneCerca.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Constants.COLOR_BUTTON);}});
        bottoneCerca.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {
            searchAction();}});

        // Setting Listener Tap 2.
        buttonFilterProgrLanguage.setOnAction(actionEvent -> cloneRepositories(() -> filterByProgrammingLanguage()));
        buttonFilterLanguage.setOnAction(actionEvent -> cloneRepositories(() -> filterByLanguage()));

        buttonFilterLanguage.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterLanguage, Constants.BUTTON_HOVER_COLOR);}});
        buttonFilterLanguage.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterLanguage, Constants.COLOR_BUTTON);}});
        bottoneSalva.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, Constants.BUTTON_HOVER_COLOR);}});
        bottoneSalva.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, Constants.COLOR_BUTTON);}});
        bottoneSalva.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {actionSaveClone();}});
        bottoneAggiungiQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Constants.COLOR_BUTTON_CLEARER_HOVER);}});
        bottoneAggiungiQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Constants.COLOR_BUTTON_CLEARER);}});
        bottoneAggiungiQuery.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {aggiungiCampoQuery();}});
        bottoneEliminaQuery.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {eliminaCampoQuery();}});
        bottoneEliminaQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Constants.COLOR_BUTTON_CLEARER_HOVER);}});
        bottoneEliminaQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Constants.COLOR_BUTTON_CLEARER);}});
        bottoneStop.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, Constants.BUTTON_HOVER_COLOR);}});
        bottoneStop.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, Constants.COLOR_BUTTON);}});

        bottoneStop.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) { Applicazione.getInstance().getModello().addObject(Constants.MESSAGE_END_SEARCH,"Search Stopped!");stopThread();}});

        bottoneEliminaSelezionato.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, Constants.BUTTON_HOVER_COLOR);}});
        bottoneEliminaSelezionato.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, Constants.COLOR_BUTTON);}});
        bottoneEliminaSelezionato.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {deleteSelectedItem();}});
        bottoneEliminaBulk.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, Constants.BUTTON_HOVER_COLOR);}});
        bottoneEliminaBulk.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, Constants.COLOR_BUTTON);}});
        bottoneEliminaBulk.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {deleteInBulk();}});
        buttonFilterProgrLanguage.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterProgrLanguage, Constants.BUTTON_HOVER_COLOR);}});
        buttonFilterProgrLanguage.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterProgrLanguage, Constants.COLOR_BUTTON);}});

        checkStrictMode.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {cercaInTabella();}});
        buttonClone.setOnAction(actionEvent -> cloneRepositories(() -> {
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 2500);
            disableAllUIElementsResults(false);
        }));
        buttonClone.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonClone, Constants.BUTTON_HOVER_COLOR);}});
        buttonClone.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonClone, Constants.COLOR_BUTTON);}});


        this.bottoneCerca.setDisable(true);
        this.tabResults.setDisable(true);
        fieldPercentage.setVisible(false);
        this.enableDisableRemoveButton(true);
        this.bottoneEliminaSelezionato.setDisable(true);
        initIcons();
        initCombo();
        initDatePickers();
        this.initTable();
        this.initProgressBar();
        this.setToolTipTexts();
    }

    private void createButtonList() {
        buttonList.add(bottoneEliminaQuery);
        buttonList.add(bottoneAggiungiQuery);
        buttonList.add(bottoneCerca);
        buttonList.add(bottoneStop);
        buttonList.add(bottoneEliminaBulk);
        buttonList.add(bottoneEliminaSelezionato);
        buttonList.add(buttonFilterProgrLanguage);
        buttonList.add(buttonFilterLanguage);
        buttonList.add(buttonClone);
        buttonList.add(bottoneSalva);
    }

    private void eventiCampi() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        campoCercaTabella.textProperty().addListener((observable, oldValue, newValue) -> {
            cercaInTabella();
        });
        fieldPercentage.textProperty().addListener((observable, oldValue, newValue) -> {
            cercaInTabella();
        });
        campoToken.textProperty().addListener((observable, oldValue, newValue) -> {
            enableDisableSearchButton();
        });
        this.campoParametroQ1.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ1, Constants.COLOR_HOVER_TEXTFIELD);}});
        this.campoParametroQ1.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ1, Constants.COLOR_TEXTFIELD);}});
        this.campoParametroQ2.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ2, Constants.COLOR_HOVER_TEXTFIELD);}});
        this.campoParametroQ2.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ2, Constants.COLOR_TEXTFIELD);}});
        this.campoParametroQ3.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ3, Constants.COLOR_HOVER_TEXTFIELD);}});
        this.campoParametroQ3.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoParametroQ3, Constants.COLOR_TEXTFIELD);}});
        this.campoQ1.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ1, Constants.COLOR_HOVER_TEXTFIELD);}});
        this.campoQ1.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ1, Constants.COLOR_TEXTFIELD);}});
        this.campoQ2.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ2, Constants.COLOR_HOVER_TEXTFIELD);}});
        this.campoQ2.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ2, Constants.COLOR_TEXTFIELD);}});
        this.campoQ3.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ3, Constants.COLOR_HOVER_TEXTFIELD);}});
        this.campoQ3.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(campoQ3, Constants.COLOR_TEXTFIELD);}});

        Applicazione.getInstance().getModello().addObject(Constants.ULTIMO_CAMPO_KEY, campoQ3);
        initListaTextField();
    }

    private void setToolTipTexts() {
        bottoneEliminaQuery.setTooltip(new Tooltip("Remove last inserted query field."));
        Tooltip.install(iconRemoveQuery, new Tooltip("Remove last inserted query field."));
        bottoneAggiungiQuery.setTooltip(new Tooltip("Add a query field."));
        Tooltip.install(iconAddQuery, new Tooltip("Add a query field."));
        bottoneCerca.setTooltip(new Tooltip("Start a search with parameters and queries you set above."));
        Tooltip.install(iconSearch, new Tooltip("Start a search with parameters and queries you set above."));
        bottoneStop.setTooltip(new Tooltip("Stop and Kill current thread/operation."));
        Tooltip.install(iconStop, new Tooltip("Stop and Kill current thread/operation."));
        bottoneEliminaBulk.setTooltip(new Tooltip("Delete all of the items currently displayed in the table."));
        Tooltip.install(iconDeleteBulk, new Tooltip("Delete all of the items currently displayed in the table."));
        bottoneEliminaSelezionato.setTooltip(new Tooltip("Delete the selected item in the table (if any)."));
        Tooltip.install(iconDeleteSelected, new Tooltip("Delete the selected item in the table (if any)."));
        buttonFilterProgrLanguage.setTooltip(new Tooltip("Detect the programming language of every repo (Cloning required)."));
        Tooltip.install(iconFilterProgr, new Tooltip("Detect the programming language of every repo (Cloning required)."));
        bottoneSalva.setTooltip(new Tooltip("Choose a dir where to move/save currently cloned repos."));
        Tooltip.install(iconSave, new Tooltip("Choose a dir where to move/save currently cloned repos."));
        buttonFilterLanguage.setTooltip(new Tooltip("Install Python first to use this function."));
        Tooltip.install(iconFilterLang, new Tooltip("Install Python first to use this function."));
        buttonClone.setTooltip(new Tooltip("Start the cloning process."));
        Tooltip.install(iconClone, new Tooltip("Start the cloning process."));

        comboParametriRicerca.setTooltip(new Tooltip("Choose a search parameter to be applied when using the search bar."));
        checkStrictMode.setTooltip(new Tooltip("Forces the search to work only on exact matches."));
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
        this.tableRepository.getSelectionModel().setCellSelectionEnabled(true);
        Sorter sorter = new Sorter();

        columnStars.setComparator(sorter.new SortByStars());
        columnDimensione.setComparator(sorter.new SortByDimension());
        columnLinguaggio.setComparator(sorter.new SortByProgrammingLanguage());
        columnDataCommit.setComparator(sorter.new SortByLastCommitDate());
    }

    private void addTooltipToRow() {
        LOG.info("Add Tooltip To Row");

        tableRepository.setRowFactory(repositoryTableView -> new TableRow<>() {
            @Override
            protected void updateItem(Repository repository, boolean empty) {
                super.updateItem(repository, empty);

                if (repository == null) return;

                Map<String, RepositoryLanguage> repositoryLanguageMap =
                        (Map<String, RepositoryLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_LANGUAGE);
                if (repositoryLanguageMap == null) return;
                RepositoryLanguage repositoryLanguage = repositoryLanguageMap.get(repository.getId());

                if (repositoryLanguage != null) {
                    String messageTooltip = "Language Detection of " + repository.getName() + "\n"
                            + "Readme Analyzed: " + repositoryLanguage.getReadmeAnalyzed() + ".";
                    this.setTooltip(new Tooltip(messageTooltip));
                }
            }
        });
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
        this.iconAddQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Constants.COLOR_BUTTON_CLEARER_HOVER);}});
        this.iconAddQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Constants.COLOR_BUTTON_CLEARER);}});
        this.iconSearch.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Constants.BUTTON_HOVER_COLOR);}});
        this.iconSearch.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Constants.COLOR_BUTTON);}});
        this.iconSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneCerca.fire();}});
        this.iconFilterLang.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterLanguage, Constants.BUTTON_HOVER_COLOR);}});
        this.iconFilterLang.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterLanguage, Constants.COLOR_BUTTON);}});
        this.iconFilterLang.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonFilterLanguage.fire();}});
        this.iconSave.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, Constants.BUTTON_HOVER_COLOR);}});
        this.iconSave.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, Constants.COLOR_BUTTON);}});
        this.iconSave.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneSalva.fire();}});

        this.iconFilterLang.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonFilterLanguage.fire();}});
        this.iconAddQuery.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneAggiungiQuery.fire();}});;
        this.iconRemoveQuery.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneEliminaQuery.fire();}});
        this.iconRemoveQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Constants.COLOR_BUTTON_CLEARER_HOVER);}});
        this.iconRemoveQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Constants.COLOR_BUTTON_CLEARER);}});

        this.iconStop.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, Constants.BUTTON_HOVER_COLOR);}});
        this.iconStop.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, Constants.COLOR_BUTTON);}});
        iconStop.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneStop.fire();}});

        iconDeleteBulk.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneEliminaBulk.fire();}});
        iconDeleteBulk.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, Constants.BUTTON_HOVER_COLOR);}});
        iconDeleteBulk.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, Constants.COLOR_BUTTON);}});

        iconDeleteSelected.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {bottoneEliminaSelezionato.fire();}});
        iconDeleteSelected.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, Constants.BUTTON_HOVER_COLOR);}});
        iconDeleteSelected.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, Constants.COLOR_BUTTON);}});

        iconFilterProgr.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterProgrLanguage, Constants.BUTTON_HOVER_COLOR);}});
        iconFilterProgr.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterProgrLanguage, Constants.COLOR_BUTTON);}});
        iconFilterProgr.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonFilterProgrLanguage.fire();}});

        iconClone.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {buttonClone.fire();}});
        iconClone.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonClone, Constants.BUTTON_HOVER_COLOR);}});
        iconClone.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonClone, Constants.COLOR_BUTTON);}});
        imgUnibas.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {
                setLegacyButtonColors(mouseEvent);
            }});
        imgUnibas.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {setLegacyButtonColors(mouseEvent);}});
    }

    private void setLegacyButtonColors(MouseEvent mouseEvent) {
        Applicazione.getInstance().getModello().addObject(Constants.LEGACY_MODE_ON, true);
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        if(mouseEvent.getClickCount() == 5) {
            setLegacyColorEventsIcons();
            boolean wasDisabled = false;
            boolean elimQueryDisabled = false;
            boolean searchDisabled = false;
            if(tabResults.isDisabled()) {
                disableElemLegacy(false);
                wasDisabled = true;
            }
            if(bottoneEliminaQuery.isDisabled()) {
                bottoneEliminaQuery.setDisable(false);
                elimQueryDisabled = true;
            }
            if(bottoneCerca.isDisabled()) {
                bottoneCerca.setDisable(false);
                searchDisabled = true;
            }
            for(Button b : buttonList) {
                commonEvents.changeButtonColor(b, Constants.COLOR_BUTTON_LEGACY_PRIMARY);
                b.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(b, Constants.COLOR_BUTTON_LEGACY_HOVER);}});
                b.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(b, Constants.COLOR_BUTTON_LEGACY_PRIMARY);}});
                if(b == bottoneStop || b == bottoneEliminaBulk || b == bottoneEliminaSelezionato) {
                    commonEvents.changeButtonColor(b, Constants.COLOR_BUTTON_LEGACY_RED);
                    b.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(b, Constants.COLOR_BUTTON_LEGACY_RED_HOVER);}});
                    b.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(b, Constants.COLOR_BUTTON_LEGACY_RED);}});
                } else if(b == buttonClone || b == bottoneSalva) {
                    commonEvents.changeButtonColor(b, Constants.COLOR_BUTTON_LEGACY_GREEN);
                    b.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(b, Constants.COLOR_BUTTON_LEGACY_GREEN_HOVER);}});
                    b.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(b, Constants.COLOR_BUTTON_LEGACY_GREEN);}});
                }
            }
            if(wasDisabled) {
                disableElemLegacy(true);
            }
            if(elimQueryDisabled) {
                bottoneEliminaQuery.setDisable(true);
            }
            if(searchDisabled) {
                bottoneCerca.setDisable(true);
            }
            imgUnibas.setDisable(true);
        }
    }

    private void setLegacyColorEventsIcons() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        iconAddQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Constants.COLOR_BUTTON_LEGACY_HOVER);}});
        iconAddQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneAggiungiQuery, Constants.COLOR_BUTTON_LEGACY_PRIMARY);}});
        iconRemoveQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Constants.COLOR_BUTTON_LEGACY_HOVER);}});
        iconRemoveQuery.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaQuery, Constants.COLOR_BUTTON_LEGACY_PRIMARY);}});
        iconSearch.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Constants.COLOR_BUTTON_LEGACY_HOVER);}});
        iconSearch.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneCerca, Constants.COLOR_BUTTON_LEGACY_PRIMARY);}});
        iconFilterProgr.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterProgrLanguage, Constants.COLOR_BUTTON_LEGACY_HOVER);}});
        iconFilterProgr.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterProgrLanguage, Constants.COLOR_BUTTON_LEGACY_PRIMARY);}});
        iconFilterLang.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterLanguage, Constants.COLOR_BUTTON_LEGACY_HOVER);}});
        iconFilterLang.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonFilterLanguage, Constants.COLOR_BUTTON_LEGACY_PRIMARY);}});
        iconStop.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, Constants.COLOR_BUTTON_LEGACY_RED_HOVER);}});
        iconStop.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneStop, Constants.COLOR_BUTTON_LEGACY_RED);}});
        iconDeleteBulk.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, Constants.COLOR_BUTTON_LEGACY_RED_HOVER);}});
        iconDeleteBulk.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaBulk, Constants.COLOR_BUTTON_LEGACY_RED);}});
        iconDeleteSelected.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, Constants.COLOR_BUTTON_LEGACY_RED_HOVER);}});
        iconDeleteSelected.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneEliminaSelezionato, Constants.COLOR_BUTTON_LEGACY_RED);}});
        iconClone.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonClone, Constants.COLOR_BUTTON_LEGACY_GREEN_HOVER);}});
        iconClone.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(buttonClone, Constants.COLOR_BUTTON_LEGACY_GREEN);}});
        iconSave.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, Constants.COLOR_BUTTON_LEGACY_GREEN_HOVER);}});
        iconSave.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeButtonColor(bottoneSalva, Constants.COLOR_BUTTON_LEGACY_GREEN);}});
    }

    private void disableElemLegacy(boolean value) {
        tabResults.setDisable(value);
        bottoneSalva.setDisable(value);
        bottoneEliminaSelezionato.setDisable(value);
        bottoneEliminaBulk.setDisable(value);
        buttonFilterLanguage.setDisable(value);
        buttonFilterProgrLanguage.setDisable(value);
        buttonClone.setDisable(value);
    }

    private LocalDate getValueDataPicker(DatePicker datePicker) {
        if(datePicker.getValue() == null) {
            return null;
        }
        LocalDate localDate =  datePicker.getValue();
        return localDate;
    }

    public void initDatePickers() {
        datePickerStart.setEditable(false);
        datePickerEnd.setEditable(false);
        LocalDate maxDate = LocalDate.now();
        datePickerStart.valueProperty().addListener((ov, oldValue, newValue) -> {
            LocalDate localDateStart = getValueDataPicker(datePickerStart);
            LocalDate localDateEnd = getValueDataPicker(datePickerEnd);
            if (datePickerEnd.getValue() == null || localDateStart.compareTo(localDateEnd) > 0) {
                datePickerEnd.setValue(localDateStart);
            }
        });

        datePickerEnd.valueProperty().addListener((ov, oldValue, newValuw) -> {
            LocalDate localDateStart = getValueDataPicker(datePickerStart);
            LocalDate localDateEnd = getValueDataPicker(datePickerEnd);
            if (datePickerStart.getValue() == null || localDateEnd.compareTo(localDateStart) < 0) {
                datePickerStart.setValue(localDateEnd);
            }
            if(datePickerEnd.getValue().isAfter(maxDate)) {
                datePickerEnd.setValue(maxDate);
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
        LOG.info("* Formatted date Start!!! *** " + formattedDate);
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
        LOG.info("* Formatted Date End!!! *** " + formattedDate);
        return formattedDate;
    }

    private void enableDisableRemoveButton(boolean b) {
        this.bottoneEliminaQuery.setDisable(b);
        this.iconRemoveQuery.setDisable(b);
    }

    private void enableDisableSearchButton() {
        if (campoToken.getText().length() < 40 || campoToken.getText().length() > 40) {
            bottoneCerca.setDisable(true);
        } else {
            bottoneCerca.setDisable(false);
        }
    }

    private void initTable() {
        initTableCells();
        tableRepository.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.C && e.isControlDown()) {
                StringBuilder clipboardString = new StringBuilder();

                ObservableList<TablePosition> positionList = tableRepository.getSelectionModel().getSelectedCells();

                int prevRow = -1;

                for (TablePosition position : positionList) {

                    int row = position.getRow();
                    int col = position.getColumn();


                    Object cell = (Object) tableRepository.getColumns().get(col).getCellData(row);

                    // null-check: provide empty string for nulls
                    if (cell == null) {
                        cell = "";
                    }

                    // determine whether we advance in a row (tab) or a column
                    // (newline).
                    if (prevRow == row) {

                        clipboardString.append('\t');

                    } else if (prevRow != -1) {

                        clipboardString.append('\n');

                    }

                    // create string from cell
                    String text = cell.toString();

                    // add new item to clipboard
                    clipboardString.append(text);

                    // remember previous
                    prevRow = row;
                }

                // create clipboard content
                final ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(clipboardString.toString());

                // set clipboard content
                Clipboard.getSystemClipboard().setContent(clipboardContent);
            }

        });
        this.tableRepository.setOnMouseClicked(mouseEvent -> selectItemTableEvent());
        addTooltipToRow();
    }


    private void updateTable() {
        ObservableList<Repository> listaRepoAgg = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO_UPDATED);
        ObservableList<Repository> listaRepoCompl = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO);
        this.labelRepoNumber.setText("Total amount of repos: " + listaRepoCompl.size() + ", Displayed: " + listaRepoAgg.size());
        this.tableRepository.setItems(listaRepoAgg);
        this.tableRepository.refresh();
    }

    private void cercaInTabella() {
        this.bottoneEliminaSelezionato.setDisable(true);
        eventoCercaInTabella();
        updateTable();
    }

    private void eventoCercaInTabella() {
        Modello modello = Applicazione.getInstance().getModello();
        String percent = this.fieldPercentage.getText();
        ObservableList<Repository> listaOriginale = (ObservableList<Repository>) modello.getObject(Constants.LIST_REPO);
        boolean strict = this.checkStrictMode.isSelected();
        String daCercare = this.campoCercaTabella.getText();
        ObservableList<Repository> listaAgg = Operator.searchByName(listaOriginale, daCercare, getSelectedComboLanguage(), strict, percent);
        modello.addObject(Constants.LIST_REPO_UPDATED, listaAgg);
    }

    private String getSelectedComboLanguage() {
        Object valore = this.comboParametriRicerca.getValue();
        if (valore != null) {
            return valore.toString();
        }
        return Constants.PARAM_REPOSITORIES;
    }

    public void initCombo() {
        comboParametriRicerca.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue.equals(Constants.PARAM_PROGR_LANGUAGE_GREATER) || newValue.equals(Constants.PARAM_PROGR_LANGUAGE_SMALLER) || newValue.equals(Constants.PARAM_LANGUAGE_GREATER) || newValue.equals(Constants.PARAM_LANGUAGE_SMALLER)) {
                fieldPercentage.setVisible(true);
            } else {
                fieldPercentage.setVisible(false);
            }
            getSelectedComboLanguage();
            cercaInTabella();
        });
        String parametri[] = {Constants.PARAM_REPOSITORIES, Constants.PARAM_LANGUAGE_GREATER, Constants.PARAM_LANGUAGE_SMALLER, Constants.PARAM_PROGR_LANGUAGE_GREATER, Constants.PARAM_PROGR_LANGUAGE_SMALLER, Constants.PARAM_DATE_COMMIT,
                Constants.PARAM_URL, Constants.PARAM_DIMENSION_GREATER, Constants.PARAM_DIMENSION_SMALLER, Constants.PARAM_STARS_GREATER, Constants.PARAM_STARS_SMALLER};
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

        nuovaQuery.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaQuery, Constants.COLOR_HOVER_TEXTFIELD);
            }
        });
        nuovaQuery.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaQuery, Constants.COLOR_TEXTFIELD);
            }
        });
        nuovaKey.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaKey, Constants.COLOR_HOVER_TEXTFIELD);
            }
        });
        nuovaKey.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Applicazione.getInstance().getCommonEvents().changeBorderColor(nuovaKey, Constants.COLOR_TEXTFIELD);
            }
        });

        TextField ultimaInserita = this.listaCampiQuery.get(this.listaCampiQuery.size() - 1);
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
        nuova.setStyle("-fx-background-color:" + Constants.COLOR_PRIMARY + ";" + " -fx-border-color:" + Constants.COLOR_TEXTFIELD + ";" + " -fx-border-radius: 8; -fx-background-insets: 0; -fx-text-fill: #000;");
        nuova.setLayoutX(ultimaInserita.getLayoutX());
        nuova.setMaxHeight(ultimaInserita.getMaxHeight());
        nuova.setMaxWidth(ultimaInserita.getMaxWidth());
        nuova.setLayoutY(ultimaInserita.getLayoutY() + 40);
    }

    private void eliminaCampoQuery() {
        this.anchorQuery.getChildren().remove(this.listaCampiQuery.get(this.listaCampiQuery.size() - 1));
        this.anchorQuery.getChildren().remove(this.listaCampiChiavi.get(this.listaCampiChiavi.size() - 1));
        this.listaCampiQuery.remove(this.listaCampiQuery.size() - 1);
        this.listaCampiChiavi.remove(this.listaCampiChiavi.size() - 1);
        Applicazione.getInstance().getModello().addObject(Constants.ULTIMO_CAMPO_KEY, this.listaCampiChiavi.get(this.listaCampiChiavi.size() - 1));
        if (this.listaCampiChiavi.size() == 3) {
            this.enableDisableRemoveButton(true);
        }
    }

    private void filterByLanguage() {
        LOG.info("\n\t--------------------------------------------------------------\n\t                Language Detection In Progress\n\t--------------------------------------------------------------");
        imgUnibas.setDisable(true);
        Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Language detection in progress...")), 1500);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                List<Repository> repositories = (List<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO);
                // Create CSV file...
                Path inputCSV = FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_INPUT_CSV);
                DAORepositoryCSV daoRepositoryCSV = Applicazione.getInstance().getDaoRepositoryCSV();
                daoRepositoryCSV.saveRepositories(inputCSV, repositories, new String[]{"Index", "CloneDirectory"}, (repository, index) -> {
                    return String.join(",", "" + index, repository.getCloneDirectory());
                });

                // Language Detection.
                if (!Operator.actionDetectIdiom()) {
                    throw new RuntimeException((String) Applicazione.getInstance().getModello().getObject(Constants.MESSAGE_LANGUAGE_DETECTION));
                }

                // Update repositories from CSV [Index, CloneDirectory, ReadmeAnalyzed, Language, Code1, Percentage1, Code2, Percentage2]
                Path outputCSV = FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_OUTPUT_CSV);
                daoRepositoryCSV.updateRepositories(outputCSV, s -> {
                    String[] values = s.split(",");

                    if (values.length == 0) {
                        imgUnibas.setDisable(false);
                        return;
                    }
                    // Reading columns from csv.

                    // The repository in question has never been cloned.
                    if (values[1].equals("null")) {
                        imgUnibas.setDisable(false);
                        return;
                    }

                    // Reading the column Index.
                    Repository repository = repositories.get(Integer.parseInt(values[0]));

                    // Reading the column CloneDirectory.
                    repository.setCloneDirectory(values[1]);

                    RepositoryLanguage repositoryLanguage = new RepositoryLanguage();
                    // Reading the column ReadmeAnalyzed.
                    repositoryLanguage.setReadmeAnalyzed(Integer.parseInt(values[2]));

                    // Reading the column Language.
                    repositoryLanguage.setLanguage(values[3]);

                    if (values.length == 6) {
                        // Reading the columns: Code1, Percentage1.
                        repositoryLanguage.setDetection1(values[4], Double.parseDouble(values[5]));
                    } else if (values.length == 8) {
                        // Reading the columns:  Code1, Percentage1, Code2, Percentage2.
                        repositoryLanguage.setDetection1(values[4], Double.parseDouble(values[5]));
                        repositoryLanguage.setDetection2(values[6], Double.parseDouble(values[7]));
                    }
                    // Displays the date in the table.
                    repository.setLanguageProperty(repositoryLanguage.toString());

                    Map<String, RepositoryLanguage> repositoryLanguageMap =
                            (Map<String, RepositoryLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_LANGUAGE);
                    repositoryLanguageMap.put(repository.getId(), repositoryLanguage);
                });
                imgUnibas.setDisable(false);
                return null;
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            // Language detection completed.
            LOG.info("\n\t--------------------------------------------------------------\n\t                Language Detection Completed\n\t--------------------------------------------------------------");
            Applicazione.getInstance().getModello().addObject(Constants.IS_LANGUAGE_DETECTION, true);
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Language detection completed!")), 1500);
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 2500);
            disableAllUIElementsResults(false);
            tableRepository.refresh();
            stopThread();
        });

        task.setOnFailed(workerStateEvent -> {
            //workerStateEvent.getSource().getException().printStackTrace();
            LOG.error("Something went wrong", workerStateEvent.getSource().getException());
            task.cancel(true);

            labelProgress.setText("Something went wrong: see the log file");
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 1500);
            LOG.info(String.valueOf(Applicazione.getInstance().getModello().getObject(Constants.MESSAGE_LANGUAGE_DETECTION)));
            labelProgress.setText((String) Applicazione.getInstance().getModello().getObject(Constants.MESSAGE_LANGUAGE_DETECTION));
            disableAllUIElementsResults(false);
            stopThread();
        });
        Thread exe = new Thread(task);
        exe.setName("Thread-FilterByLanguage");
        exe.start();
    }

    private void filterByProgrammingLanguage() {
        LOG.info("\n\t--------------------------------------------------------------\n\t        Detecting Of Programming Language In Progress\n\t--------------------------------------------------------------");
        Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Detecting of programming language in progress...")), 1500);

        List<Repository> repositories = (List<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO);
        Map<String, StatisticsProgrammingLanguage> languageProgrammingMap =
                (Map<String, StatisticsProgrammingLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_PROGRAMMING_LANGUAGE);

        for (int i = 0; i < repositories.size(); i++) {

            Repository repository = repositories.get(i);

            // The repository in question has not been cloned.
            if (repository.getCloneDirectory() == null) continue;

            StatisticsProgrammingLanguage statisticsProgrammingLanguage = languageProgrammingMap.get(repository.getId());
            repository.displayProgrammingLanguages(statisticsProgrammingLanguage.toString());

            int taskWorkProgress = (int) ((100.0 / repositories.size()) * (i + 1));
            if (taskWorkProgress < 17) {
                progressBar.setProgress(Constants.values[0]);
            } else if (taskWorkProgress >= 30 && taskWorkProgress < 50) {
                progressBar.setProgress(Constants.values[2]);
            } else if (taskWorkProgress >= 50 && taskWorkProgress < 90) {
                progressBar.setProgress(Constants.values[4]);
            } else if (taskWorkProgress >= 90) {
                progressBar.setProgress(Constants.values[5]);
            }

            //LOG.info("ProgressBar percentage: " + taskWorkProgress);
        }

        LOG.info("\n\t--------------------------------------------------------------\n\t        Detecting Of Programming Language Completed\n\t--------------------------------------------------------------");
        Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Programming language/markup detection complete")), 1500);
        Utils.setTimeout(() -> Platform.runLater(() -> {
            labelProgress.setText("Waiting for something to do...");
            // Reset progressBar.
            progressBar.setProgress(Constants.values[0]);
        }), 3500);
        disableAllUIElementsResults(false);
    }

    private void cloneRepositories(Runnable postExecute) {
        imgUnibas.setDisable(true);
        Runnable runnable = postExecute == null ? () -> {} : postExecute;

        disableAllUIElementsResults(true);
        List<Repository> repositories = (List<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO);
        if (repositories == null || repositories.isEmpty()) {
            LOG.info("You must run a search query first \uD83D\uDE0E");
            labelProgress.setText("You must run a search query first");
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 1500);
            disableAllUIElementsResults(false);
            imgUnibas.setDisable(false);
            return;
        }

        int indexLastClonedRepository = (int) Applicazione.getInstance().getModello().getObject(Constants.INDEX_LAST_CLONED_REPOSITORY);
        if ((indexLastClonedRepository + 1) == repositories.size()) {
            // All repositories have already been cloned for this search session.
            labelProgress.setText("Cloned repositories");
            runnable.run();
            disableAllUIElementsResults(false);
            imgUnibas.setDisable(false);
            return;
        }

        int firstNonClonedRepositoryIndex = 0;
        if (indexLastClonedRepository != -1) {
            // Something went wrong ... I start cloning from the repo whose index is: indexLastClonedRepository + 1.
            firstNonClonedRepositoryIndex = indexLastClonedRepository + 1;
        }

        String token = Applicazione.getInstance().getSessionManager().getCurrentSession().getQuery().getToken();
        TaskCloneRepositories task = new TaskCloneRepositories(repositories, firstNonClonedRepositoryIndex, token);

        Applicazione.getInstance().getModello().addObject(Constants.TASK_CLONE_REPOSITORIES, task);

        //# Setting event handler on task
        task.setOnSucceeded(workerStateEvent -> {
            // Reset progressBar, labelProgress.
            progressBar.progressProperty().unbind();
            progressBar.setProgress(Constants.values[0]);
            labelProgress.textProperty().unbind();

            LOG.info("\n\t--------------------------------------------------------------\nAll Repositories Have Been Successfully Cloned For This Search Session\n\t--------------------------------------------------------------");

            runnable.run();
        });

        task.setOnCancelled(workerStateEvent -> {
            // Reset progressBar, labelProgress.
            progressBar.progressProperty().unbind();
            progressBar.setProgress(Constants.values[0]);
            labelProgress.textProperty().unbind();

            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 1500);
            disableAllUIElementsResults(false);
            imgUnibas.setDisable(false);
        });

        task.setOnFailed(workerStateEvent -> {
            //workerStateEvent.getSource().getException().printStackTrace();
            LOG.error("Something went wrong:", workerStateEvent.getSource().getException());
            task.cancel(true);

            // Reset progressBar, labelProgress.
            progressBar.progressProperty().unbind();
            progressBar.setProgress(Constants.values[0]);
            labelProgress.textProperty().unbind();

            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 1500);
            disableAllUIElementsResults(false);
            imgUnibas.setDisable(false);
        });

        progressBar.progressProperty().bind(task.progressProperty());
        labelProgress.textProperty().bind(task.messageProperty());
        Thread exe = new Thread(task);
        exe.setName("Thread-Cloning");
        exe.start();
    }

    private void searchAction() {
        LOG.info("\n\t--------------------------------------------------------------\n\t                       Search Launched\n\t--------------------------------------------------------------");
        this.labelErrori.setText("");
        List<Qualifier> qualifiers = createListQualifiers();
        if (qualifiers != null) {
            Query query = new Query(qualifiers);
            setOptionalFields(query);
            Session session = new Session(null);
            session.setQuery(query);
            Applicazione.getInstance().getSessionManager().addSession(session);
            LOG.info("\n\t** Session created\n\t* Date: " + session.getDate() + "\n\t** Number of sessions: " + Applicazione.getInstance().getSessionManager().getSessions().size());

            String qualifiersLog = "\n\t** Query";
            for (Qualifier qualifier : query.getQualifiers()) {
                qualifiersLog = qualifiersLog + "\n\t* Key=" + qualifier.getKey() + ", Value=" + qualifier.getValue();
            }
            LOG.info(qualifiersLog);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    imgUnibas.setDisable(true);
                    synchronized (Applicazione.getInstance().getModello().getObject(Constants.THREAD_WARNING_PANEL)) {
                        try {
                            Applicazione.getInstance().getModello().getObject(Constants.THREAD_WARNING_PANEL).wait();
                        } catch (Exception ex) {
                            Platform.runLater(new Runnable() {@Override public void run() {Applicazione.getInstance().getCommonEvents().showExceptionDialog(ex);}});
                            ex.printStackTrace();
                        }
                    }
                    //Applicazione.getInstance().getSessionManager().getSessions().clear();
                    boolean delete = (boolean) Applicazione.getInstance().getModello().getObject(Constants.ACCEPT_WARNING_MEX);
                    if(delete == false) {
                        Platform.runLater(new Runnable() {@Override public void run() {labelErrori.setText("Operation aborted.");}});
                        return;
                    }

                    if(query.getToken() != null) {
                        disableAllUIElements(true);

                        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
                        Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Creating properties file", 1);}});
                        Operator.createConfigProperties();
                        Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Properties file created! It's been a pleasure working for you!", 5);}});

                        LOG.info("Properties! created");

                        if (Operator.startGHRepoSearcher()) {
                            LOG.info("\n\t--------------------------------------------------------------\n\t                Search Completed Successfully!\n\t--------------------------------------------------------------");
                            String path = FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_JSON).toString();
                            LOG.info("** Path: " + path);
                            List<Repository> lista = Applicazione.getInstance().getDaoRepositoryJSON().loadRepositories(path);
                            ObservableList<Repository> tabList = FXCollections.observableArrayList(lista);
                            Applicazione.getInstance().getModello().addObject(Constants.LIST_REPO_UPDATED, tabList);
                            Applicazione.getInstance().getModello().addObject(Constants.LIST_REPO, tabList);
                            // Init Cloning.
                            Applicazione.getInstance().getModello().addObject(Constants.INDEX_LAST_CLONED_REPOSITORY, -1);

                            // Init by Programming Language.
                            // map<Key=IDRepository, Value=StatisticsLanguageProgramming>
                            Map<String, StatisticsProgrammingLanguage> mapRepositoryLangProg = new HashMap<>();
                            Applicazione.getInstance().getModello().addObject(Constants.MAP_REPOSITORY_PROGRAMMING_LANGUAGE, mapRepositoryLangProg);

                            // Init by Language Detection
                            // map<Key=IDRepository, Value=RepositoryLanguage>
                            Map<String, RepositoryLanguage> repositoryLanguageMap = new HashMap<>();
                            Applicazione.getInstance().getModello().addObject(Constants.MAP_REPOSITORY_LANGUAGE, repositoryLanguageMap);

                            // Init by Save Repositories.
                            Applicazione.getInstance().getModello().addObject(Constants.IS_SAVE_REPOSITORIES, false);

                            // Init by Language Detection.
                            Applicazione.getInstance().getModello().addObject(Constants.IS_LANGUAGE_DETECTION, false);

                            Platform.runLater(() -> updateTable());

                            if(tabList.isEmpty()) {
                                Applicazione.getInstance().getModello().addObject(Constants.MESSAGE_END_SEARCH,"Last search found ZERO results :(");
                            } else {
                                Applicazione.getInstance().getModello().addObject(Constants.MESSAGE_END_SEARCH,"The search went just AWESOME!");
                            }

                            stopThread();
                        } else {
                            LOG.error("Error GHRepoSearcher!");
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
                    imgUnibas.setDisable(false);
                }
            });

            Applicazione.getInstance().getModello().addObject(Constants.THREAD_WARNING_PANEL, thread);
            if(Utils.isWindows()) {
                Applicazione.getInstance().getCommonEvents().loadPanel("WarningPanel", Modality.APPLICATION_MODAL, false, "Warning", StageStyle.UNDECORATED, true);
            } else {
                Applicazione.getInstance().getCommonEvents().loadPanel("WarningPanel", Modality.APPLICATION_MODAL, false, "Warning", StageStyle.UTILITY, true);
            }
            thread.start();
        }
    }

    private List<Qualifier> createListQualifiers() {
        boolean[] arrayKeyPresence = checkKeyFields();
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        List<Qualifier> result = new ArrayList<>();
        boolean error = false;
        for(int i = 0; i < arrayKeyPresence.length; i++) {
            if(arrayKeyPresence[i]) {
                continue;
            }
            TextField textFieldKey = this.listaCampiChiavi.get(i);
            TextField textFieldQ = this.listaCampiQuery.get(i);
            boolean presence = checkKeyPresence(textFieldKey.getText());
            if(!presence) {
                commonEvents.changeBorderColor(textFieldKey, "#ff0000");
                this.labelErrori.setText("Check the queries in red! Keys must correspond to values.");
                error = true;
            } else if(presence && textFieldQ.getText().isEmpty()) {
                commonEvents.changeBorderColor(textFieldQ, "#ff0000");
                this.labelErrori.setText("Check the queries in red! Keys must correspond to values.");
                error = true;
            } else {
                result.add(new Qualifier(textFieldKey.getText(), textFieldQ.getText()));
            }
        }

        if(error) {
            return null;
        }
        restoreStockColorTextFields();
        return result;
    }

    private boolean[] checkKeyFields() {
        boolean keyPresence[] = new boolean[this.listaCampiQuery.size()];
        this.isKeyEmpty = true;
        int i = 0;
        for (TextField t : this.listaCampiChiavi) {
            if(t.getText().isEmpty()) {
                keyPresence[i] = true;
            } else {
                keyPresence[i] = false;
                this.isKeyEmpty = false;
            }
            i++;
        }
        return keyPresence;
    }


    private void restoreStockColorTextFields() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        int i = 0;
        for (TextField t : this.listaCampiChiavi) {
            TextField query = this.listaCampiQuery.get(i);
            commonEvents.changeBorderColor(t, Constants.COLOR_TEXTFIELD);
            commonEvents.changeBorderColor(query, Constants.COLOR_TEXTFIELD);
            t.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(t, Constants.COLOR_HOVER_TEXTFIELD);}});
            t.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(t, Constants.COLOR_TEXTFIELD);}});
            query.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(query, Constants.COLOR_HOVER_TEXTFIELD);}});
            query.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent mouseEvent) {commonEvents.changeBorderColor(query, Constants.COLOR_TEXTFIELD);}});
        }
    }

    private void setOptionalFields(Query q) {
        if (!this.campoToken.getText().isEmpty()) {
            q.setToken(this.campoToken.getText());
        }
        q.setDate(getDatePickerStartInstant() + getDatePickerEndInstant());
    }

    private boolean checkKeyPresence(String key) {
        for (int i = 0; i < Constants.LISTA_QUAL.length; i++) {
            if (Constants.LISTA_QUAL[i].equalsIgnoreCase(key.trim())) {
                return true;
            }
        }
        return false;
    }

    private void stopThread() {
        LOG.info("\n\t--------------------------------------------------------------\n\t                      Operation Aborted\n\t--------------------------------------------------------------");
        TaskCloneRepositories task = (TaskCloneRepositories) Applicazione.getInstance().getModello().getObject(Constants.TASK_CLONE_REPOSITORIES);
        if(task != null) {
            task.close();
        }

       /* Process processoLan = (Process) Applicazione.getInstance().getModello().getObject(Constants.PROCESS_LANGUAGE_DETECTION);
        if(processoLan!=null){
            processoLan.destroy();
        }*/

        Thread thread = (Thread) Applicazione.getInstance().getModello().getObject(Constants.THREAD_WARNING_PANEL);
        /*
       Process processLan = (Process) Applicazione.getInstance().getModello().getObject(Constants.PROCESS_LANGUAGE_DETECTION);
        if(processLan!=null){
            processLan.destroy();
        }
        */
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        if(thread != null) {
            thread.interrupt();
            String message = (String) Applicazione.getInstance().getModello().getObject(Constants.MESSAGE_END_SEARCH);
            Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar(message, 0);}});
            Applicazione.getInstance().getModello().addObject(Constants.MESSAGE_END_SEARCH,null);
        }


        Process process = (Process) Applicazione.getInstance().getModello().getObject(Constants.THREAD_REPO_SEARCHER);
        if(process != null) {
            process.destroy();
        }


        Applicazione.getInstance().getModello().addObject(Constants.THREAD_WARNING_PANEL, null);
        Applicazione.getInstance().getModello().addObject(Constants.THREAD_REPO_SEARCHER, null);
        Applicazione.getInstance().getModello().addObject(Constants.PROCESS_LANGUAGE_DETECTION,null);
        Applicazione.getInstance().getModello().addObject(Constants.TASK_CLONE_REPOSITORIES,null);

        ObservableList<Repository> lista = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO);
        if(lista == null) {
            imgUnibas.setDisable(false);
            return;
        }
        disableAllUIElements(false);
        disableAllUIElementsResults(false);
        if(!lista.isEmpty()) {
            tabbedPane.getSelectionModel().select(tabResults);
        }
        imgUnibas.setDisable(false);
    }

    private void deleteInBulk() {
        this.bottoneEliminaSelezionato.setDisable(true);
        ObservableList<Repository> listaAgg = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO_UPDATED);
        ObservableList<Repository> listaCompleta = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO);
        if (listaCompleta == null || listaCompleta.size() == 0) {
            return;
        }
        if (listaAgg == null) {
            listaCompleta.clear();
            this.tableRepository.setItems(listaCompleta);
            return;
        }
        listaCompleta.removeAll(listaAgg);
        listaAgg.removeAll(listaAgg);
        updateTable();
        checkListEmpty();
    }

    private void deleteSelectedItem() {
        ObservableList<Repository> listaAgg = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO_UPDATED);
        ObservableList<Repository> listaCompleta = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO);
        int selectedIndex = this.tableRepository.getSelectionModel().getSelectedIndex();
        this.bottoneEliminaSelezionato.setDisable(true);
        if (selectedIndex != -1) {
            if (listaAgg == null) {
                listaCompleta.remove(selectedIndex);
                this.tableRepository.setItems(listaCompleta);
                return;
            }
            Repository repo = listaAgg.get(selectedIndex);
            listaCompleta.remove(repo);
            listaAgg.remove(repo);
            updateTable();
        }
        checkListEmpty();
    }

    private void checkListEmpty() {
        ObservableList<Repository> lista = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO);
        if (lista.isEmpty()) {
            tabbedPane.getSelectionModel().select(tabQueries);
            tabResults.setDisable(true);
        }
    }

    private void selectItemTableEvent() {
        if (this.tableRepository.getSelectionModel().getSelectedIndex() != -1) {
            this.bottoneEliminaSelezionato.setDisable(false);
        } else {
            this.bottoneEliminaSelezionato.setDisable(true);
        }
    }

    private void disableAllUIElements(boolean value) {
        for (int i = 0; i < listaCampiChiavi.size(); i++) {
            listaCampiChiavi.get(i).setDisable(value);
            listaCampiQuery.get(i).setDisable(value);
        }
        campoToken.setDisable(value);
        datePickerStart.setDisable(value);
        datePickerEnd.setDisable(value);
        if (value == false && this.listaCampiQuery.size() > 3) {
            iconRemoveQuery.setDisable(value);
            bottoneEliminaQuery.setDisable(value);
        }
        iconAddQuery.setDisable(value);
        bottoneAggiungiQuery.setDisable(value);
        bottoneCerca.setDisable(value);
        iconSearch.setDisable(value);
        ObservableList<Repository> lista = (ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO);
        if (value == false) {
            if (lista != null && !lista.isEmpty()) {
                tabResults.setDisable(value);
                tabbedPane.getSelectionModel().select(tabResults);
            }
        } else {
            tabResults.setDisable(value);
        }
    }

    private void disableAllUIElementsResults(boolean value) {
        tableRepository.setDisable(value);
        buttonFilterLanguage.setDisable(value);
        buttonFilterProgrLanguage.setDisable(value);
        bottoneEliminaBulk.setDisable(value);
        bottoneSalva.setDisable(value);
        buttonClone.setDisable(value);
        tabbedPane.setDisable(value);
    }

    private void actionSaveClone() {
        imgUnibas.setDisable(true);
        final List<Repository> repositories = (List<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO);
        if (repositories == null || repositories.isEmpty()) {
            LOG.info("You must run a search query first \uD83D\uDE0E");
            labelProgress.setText("You must run a search query first");
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 2500);
            imgUnibas.setDisable(false);
            return;
        }

        /*int indexLastClonedRepository = (int) Applicazione.getInstance().getModello().getObject(Constants.INDEX_LAST_CLONED_REPOSITORY);
        if (indexLastClonedRepository == -1) {
            // No cloning started.
            labelProgress.setText("You must clone first");
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 2500);
            imgUnibas.setDisable(false);
            return;
        }*/

        boolean isSaveRepositories = (boolean) Applicazione.getInstance().getModello().getObject(Constants.IS_SAVE_REPOSITORIES);
        if (isSaveRepositories) {
            // Saving already started previously.
            labelProgress.setText("The repositories have already been saved");
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 3500);
            imgUnibas.setDisable(false);
            return;
        }

        Stage stage = (Stage) Applicazione.getInstance().getModello().getObject(Constants.PRIMARY_STAGE);
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("G-Repo - Choose where to save your repos");
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        chooser.setInitialDirectory(new File(currentPath));
        disableAllUIElementsResults(true);
        bottoneStop.setDisable(true);
        File selectedDirectory = chooser.showDialog(stage);

        if (selectedDirectory == null) {
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Canceled operation")), 1500);
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 3500);
            disableAllUIElementsResults(false);
            bottoneStop.setDisable(false);
            imgUnibas.setDisable(false);
            return;
        }


        TaskSaveRepositories task = new TaskSaveRepositories(selectedDirectory.toPath(), repositories);

        //# Setting event handler on task
        task.setOnSucceeded(workerStateEvent -> {
            LOG.info("\n\t--------------------------------------------------------------\n\t                      Save Successful!\n\t--------------------------------------------------------------");
            // Reset progressBar, labelProgress.
            progressBar.progressProperty().unbind();
            progressBar.setProgress(Constants.values[0]);
            labelProgress.textProperty().unbind();

            // Null the clone directory cache of the repository. This is only if we move the repositories.
            repositories.forEach(repository -> repository.setCloneDirectory(null));

            // Save completed.
            int indexLastClonedRepository = (int) Applicazione.getInstance().getModello().getObject(Constants.INDEX_LAST_CLONED_REPOSITORY);
            if ((indexLastClonedRepository + 1) == repositories.size()) {
                Applicazione.getInstance().getModello().addObject(Constants.IS_SAVE_REPOSITORIES, true);
                // Disable button.
                buttonFilterLanguage.setDisable(true);
                buttonFilterProgrLanguage.setDisable(true);
                buttonClone.setDisable(true);
            }

            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("All repositories moved")), 1500);
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 3500);
        });

        task.setOnFailed(workerStateEvent -> {
            //workerStateEvent.getSource().getException().printStackTrace();
            LOG.error("Something went wrong ", workerStateEvent.getSource().getException());
            task.cancel(true);

            // Reset progressBar, labelProgress.
            progressBar.progressProperty().unbind();
            progressBar.setProgress(Constants.values[0]);
            labelProgress.textProperty().unbind();

            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Something went wrong: see the log file")), 1500);
            Utils.setTimeout(() -> Platform.runLater(() -> labelProgress.setText("Waiting for something to do...")), 3500);
        });

        progressBar.progressProperty().bind(task.progressProperty());
        labelProgress.textProperty().bind(task.messageProperty());

        Thread exe = new Thread(task);
        exe.setName("Thread-Save-Repository");
        exe.start();

        disableAllUIElementsResults(false);
        bottoneStop.setDisable(false);
        imgUnibas.setDisable(false);
    }

}
