package gui.components.main.app;

import dtos.*;
import dtos.gridView.GridViewDTO;
import dtos.result.EntityPopulationByTicksDTO;
import dtos.result.HistogramDTO;
import dtos.result.PropertyAvaregeValueDTO;
import dtos.result.PropertyConstistencyDTO;
import dtos.world.WorldDTO;
import engine.Engine;
import gui.components.main.PredictionApplication;
import gui.components.main.animations.FadeInAnimation;
import gui.components.main.animations.RotateAnimation;
import gui.components.main.details.scene.DetailsController;
import gui.components.main.execution.scene.NewExecutionController;
import gui.components.main.results.scene.ResultsController;
import gui.components.main.upload.UploadController;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppController {
    @FXML private Label HeaderLabel;
    @FXML private ScrollPane applicationScrollPane;
    @FXML private VBox MainVBox;
    @FXML private GridPane UpperGridPane;
    @FXML private HBox uploadComponent;
    @FXML private UploadController uploadComponentController;
    @FXML private AnchorPane detailsComponent;
    @FXML private DetailsController detailsComponentController;
    @FXML private AnchorPane newExecutionComponent;
    @FXML private NewExecutionController newExecutionComponentController;
    @FXML private AnchorPane resultsComponent;
    @FXML private ResultsController resultsComponentController;
    @FXML private TabPane tabPane;
    @FXML private ListView queueManagementListView;
    @FXML private AnchorPane TitleRow;
    @FXML private CheckBox animationsCheckBox;
    @FXML private MenuButton SkinsMenuButton;
    private final Engine engine = new Engine();

    private final SimpleBooleanProperty isXMLLoaded;
    private final SimpleBooleanProperty isSimulationExecuted;
    private final SimpleBooleanProperty isAnimationChecked;
    private static final List<String> cssList =
            new ArrayList<>(Arrays.asList(
                    "Application.css", "DarkMode-theme.css",
                    "HappyMode-theme.css", "OrangeApplication.css",
                    "GreenApplication.css"));

    public enum Tab {
        DETAILS, NEW_EXECUTION, RESULTS
    };

    private ScheduledExecutorService queueManagement;

    public AppController() {
        this.isXMLLoaded = new SimpleBooleanProperty(false);
        this.isSimulationExecuted = new SimpleBooleanProperty(false);
        this.isAnimationChecked = new SimpleBooleanProperty(false);
    }

    @FXML public void initialize(){
        setColorThemeComponents();
        tabPane.getTabs().get(1).disableProperty().bind(isXMLLoaded.not());
        tabPane.getTabs().get(2).disableProperty().bind(isSimulationExecuted.not());
        animationsCheckBox.selectedProperty().bindBidirectional(isAnimationChecked);
        if (uploadComponentController != null && detailsComponentController != null && newExecutionComponentController != null
                && resultsComponentController != null && resultsComponentController.getSimulationComponentController() != null
                && resultsComponentController.getSimulationComponentController().getInformationComponentController() != null) {
            uploadComponentController.setAppController(this);
            detailsComponentController.setAppController(this);
            newExecutionComponentController.setAppController(this);
            resultsComponentController.setAppController(this);
            resultsComponentController.getSimulationComponentController().setAppController(this);
            resultsComponentController.getSimulationComponentController().getInformationComponentController().setAppController(this);
        }
        queueManagement = Executors.newScheduledThreadPool(1);
        queueManagement.scheduleAtFixedRate(this::updateQueueManagement, 0, 200, TimeUnit.MILLISECONDS);
        Platform.runLater(() -> {
            // Set applicationScrollPane to be the same size as the window
            applicationScrollPane.prefWidthProperty().bind(PredictionApplication.getStage().widthProperty());
            applicationScrollPane.prefHeightProperty().bind(PredictionApplication.getStage().heightProperty());
            applicationScrollPane.setFitToWidth(true);
            applicationScrollPane.setFitToHeight(true);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                engine.deleteInDepthMemoryFolder();
                resultsComponentController.stopExecutorService();
                engine.stopThreadPool();
            }));
        });
    }

    private void setColorThemeComponents() {
        // set skins menu button
        applyDesign("Application.css");
        SkinsMenuButton.getItems().clear();
        List<MenuItem> menuItems = new ArrayList<>();
        MenuItem defaultSkin = new MenuItem("Default");
        defaultSkin.setOnAction(event -> {
            switchColorMode("Application.css");
        });
        MenuItem darkModeSkin = new MenuItem("Dark Mode");
        darkModeSkin.setOnAction(event -> {
            switchColorMode("DarkMode-theme.css");
        });
        MenuItem happyModeSkin = new MenuItem("Happy Mode");
        happyModeSkin.setOnAction(event -> {
            switchColorMode("HappyMode-theme.css");
        });
        MenuItem orangeSkin = new MenuItem("Orange Mode");
        orangeSkin.setOnAction(event -> {
            switchColorMode("OrangeApplication.css");
        });
        MenuItem greenSkin = new MenuItem("Green Mode");
        greenSkin.setOnAction(event -> {
            switchColorMode("GreenApplication.css");
        });
        menuItems.add(defaultSkin);
        menuItems.add(darkModeSkin);
        menuItems.add(happyModeSkin);
        menuItems.add(orangeSkin);
        menuItems.add(greenSkin);
        SkinsMenuButton.getItems().addAll(menuItems);
    }

    private void updateQueueManagement() {
        QueueManagementDTO queueManagementDTO = engine.getQueueManagementDTO();
        Platform.runLater(() -> {
            queueManagementListView.getItems().clear();
            Label label = new Label("Queue Management");
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-underline: true;");
            queueManagementListView.getItems().add(label);
            queueManagementListView.getItems().add("Pending: " + queueManagementDTO.getPending());
            queueManagementListView.getItems().add("Active: " + queueManagementDTO.getActive());
            queueManagementListView.getItems().add("Completed: " + queueManagementDTO.getCompleted());
        });
    }

    public void uploadWorldFromXML(File selectedFile) {
        try {
                engine.loadXML(selectedFile.toPath());
                uploadComponentController.setFileChosenStringProperty(selectedFile.toString());
                uploadComponentController.isXMLLoadedProperty().set(true);
                detailsComponentController.updateDetailsTreeView(engine.getWorldDTO());
                newExecutionComponentController.updateEnvVariablesInputVBox(engine.getNewExecutionInputDTO());
                newExecutionComponentController.updateEntityPopulationInputVBox(engine.getNewExecutionInputDTO());
                resultsComponentController.resetExecutionList();
                isXMLLoaded.set(true);
                isSimulationExecuted.set(false);
                resultsComponentController.getSimulationComponentController().getInformationComponentController().defineEntityChoiceBox();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error loading XML file");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
    }

    public void validateEnvVariableValue(EnvVariableValueDTO envVariableValueDTO) throws IllegalArgumentException {
        engine.validateEnvVariableValue(envVariableValueDTO);
    }

    public void activateSimulation(EnvVariablesValuesDTO envVariablesValuesDTO, EntitiesPopulationDTO entityPopulationDTO, boolean isBonusActivated) {
        isSimulationExecuted.set(true);
        engine.updateActiveEnvironmentAndInformUser(envVariablesValuesDTO);
        engine.updateActiveEntityPopulation(entityPopulationDTO);
        SimulationIDDTO simulationIDDTO = engine.activateSimulation(isBonusActivated);
        resultsComponentController.addSimulationToExecutionList(simulationIDDTO);
        resultsComponentController.setIsActive(true);

    }

    public void selectTab(Tab tab) {
        switch (tab) {
            case DETAILS:
                tabPane.getSelectionModel().select(0);
                break;
            case NEW_EXECUTION:
                tabPane.getSelectionModel().select(1);
                break;
            case RESULTS:
                tabPane.getSelectionModel().select(2);
                break;
        }
    }

    public void updateNewExecutionByPrevSimulation(int simulationID) {
        EnvVariablesValuesDTO envVariablesValuesDTO = engine.getEnvVariablesValuesDTO(simulationID);
        EntitiesPopulationDTO entityPopulationDTO = engine.getEntityPopulationDTO(simulationID);
        newExecutionComponentController.fillEnvVariablesInputVBox(envVariablesValuesDTO);
        newExecutionComponentController.fillEntityPopulationInputVBox(entityPopulationDTO);
    }

    public TabPane getTabPane(){ return tabPane; }

    public SimulationIDListDTO getSimulationListDTO() {
        return engine.getSimulationListDTO();
    }

    public SimulationResultByAmountDTO getSimulationResultByAmountDTO(int simulationID) {
        return engine.getSimulationResultByAmountDTO(simulationID);
    }

    public WorldDTO getWorldDTO() {
        return engine.getWorldDTO();
    }

    public HistogramDTO getHistogramDTO(int simulationID, String entityName, String propertyName) {
        return engine.getHistogramDTO(simulationID, entityName, propertyName);
    }

    public void validateEntitiesPopulation(EntitiesPopulationDTO entityPopulationDTOS) throws IllegalArgumentException{
        engine.validateEntitiesPopulation(entityPopulationDTOS);
    }

    public SimulationExecutionDetailsDTO getSimulationExecutionDetailsDTO(int simulationID) {
        return engine.getSimulationExecutionDetailsDTO(simulationID);
    }

    public void stopSimulation(int simulationID) {
        engine.stopSimulation(simulationID);
    }

    public void pauseSimulation(int simulationID) {
        engine.pauseSimulation(simulationID);
    }

    public void resumeSimulation(int simulationID) {
        engine.resumeSimulation(simulationID);
    }

    public GridViewDTO getGridViewDTO(int simulationID) {
        return engine.getGridViewDTO(simulationID);
    }

    public boolean isSimulationCompleted(int simulationID) {
        return engine.isSimulationCompleted(simulationID);
    }

    public PropertyConstistencyDTO getPropertyConsistencyDTO(int currentSimulationID, String entityName, String propertyName) {
        return engine.getPropertyConsistencyDTO(currentSimulationID, entityName, propertyName);
    }

    public PropertyAvaregeValueDTO getPropertyAvaregeValueDTO(int currentSimulationID, String entityName, String propertyName) {
        return engine.getPropertyAvaregeValueDTO(currentSimulationID, entityName, propertyName);
    }

    public EntityPopulationByTicksDTO getEntityPopulationByTicksDTO(int simulationID) {
        return engine.getEntityPopulationByTicksDTO(simulationID);
    }

    public  EntitiesPopulationDTO getEntitiesPopulationDTO(int simulationID){
        return this.engine.getEntityPopulationDTO(simulationID);
    }

    public void setPreviousTick(int simulationID) {
        engine.setPreviousTick(simulationID);
    }

    public void getToNextTick(int simulationID) {
        engine.getToNextTick(simulationID);
    }

    private void applyDesign(String cssPath){
        if (getClass().getResource(cssPath) != null) {
            MainVBox.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            UpperGridPane.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            uploadComponent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }

    private void removeDesign(String cssPath){
        if (getClass().getResource(cssPath) != null) {
            MainVBox.getStylesheets().remove(getClass().getResource(cssPath).toExternalForm());
            UpperGridPane.getStylesheets().remove(getClass().getResource(cssPath).toExternalForm());
            uploadComponent.getStylesheets().remove(getClass().getResource(cssPath).toExternalForm());
        }
    }

    private void switchColorMode(String cssPath){
        for (String css : cssList) {
            if (!css.equals(cssPath)) {
                removeDesign(css);
            }
        }
        applyDesign(cssPath);
    }

    public void OnAnimationClickAction(){
        RotateAnimation.applyRotateAnimation(HeaderLabel, 1000, 0, 360);
        FadeInAnimation.applyFadeInAnimation(queueManagementListView, 1000);
    }
}