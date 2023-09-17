package gui.components.main.results.simulation;

import dtos.SimulationExecutionDetailsDTO;
import dtos.gridView.GridViewDTO;
import gui.components.main.app.AppController;
import gui.components.main.results.simulation.grid.DynamicGridView;
import gui.components.main.results.simulation.information.InformationController;
import gui.components.main.results.simulation.tableView.EntityPopulationTableView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class SimulationController {
    @FXML private Label entitiesCountDisplay;
    @FXML private Label currentTickDisplay;
    @FXML private Label timeSinceSimulationStartedDisplay;
    @FXML private Button rerunSimulationButton;
    @FXML private Button pauseSimulationButton;
    @FXML private Button resumeSimulationButton;
    @FXML private Button stopSimulationButton;
    @FXML private ScrollPane entityPopulationScrollPane;
    @FXML private Button gridViewButton;

    @FXML private InformationController informationComponentController;
    @FXML private AnchorPane informationComponent;
    private AppController appController;

    private SimpleIntegerProperty currentSimulationID;
    private SimpleIntegerProperty entitiesCount;
    private SimpleIntegerProperty currentTick;
    private SimpleLongProperty timeSinceSimulationStarted;
    private SimpleBooleanProperty isRunning;
    private SimpleBooleanProperty isPaused;

    public void initialize() {
        if (informationComponentController != null) {
            informationComponentController.setSimulationComponentController(this);
        }
        currentSimulationID = new SimpleIntegerProperty();
        entitiesCount = new SimpleIntegerProperty();
        currentTick = new SimpleIntegerProperty();
        timeSinceSimulationStarted = new SimpleLongProperty();
        isRunning = new SimpleBooleanProperty(false);
        isPaused = new SimpleBooleanProperty(false);
        entitiesCountDisplay.textProperty().bind(entitiesCount.asString());
        currentTickDisplay.textProperty().bind(currentTick.asString());
        timeSinceSimulationStartedDisplay.textProperty().bind(timeSinceSimulationStarted.asString());

        // Simulation control buttons
        rerunSimulationButton.visibleProperty().bind(isRunning.not());
        // See pause when running and not paused --> Disable pause when not running or paused
        pauseSimulationButton.disableProperty().bind(isRunning.not().or(isPaused));
        // See resume when running and paused --> Disable resume when not running or not paused
        resumeSimulationButton.disableProperty().bind(isRunning.not().or(isPaused.not()));
        stopSimulationButton.disableProperty().bind(isRunning.not());
        informationComponent.visibleProperty().bind(isRunning.not());
    }
    public void setAppController(AppController appController) {
        this.appController = appController;
    }
    public void setEntitiesCountDisplay(String entitiesCountDisplayText) {
        this.entitiesCountDisplay.setText(entitiesCountDisplayText);
    }

    public void updateSimulationComponent(SimulationExecutionDetailsDTO simulationEDDTO) {
        currentSimulationID.set(simulationEDDTO.getId());
        entitiesCount.set(simulationEDDTO.getNumberOfEntities());
        currentTick.set(simulationEDDTO.getCurrentTick());
        timeSinceSimulationStarted.set(simulationEDDTO.getDurationInSeconds());
        isRunning.set(simulationEDDTO.isRunning());
        isPaused.set(simulationEDDTO.isPaused());
        EntityPopulationTableView epTableView = new EntityPopulationTableView();
        HBox entityPopulationHBox = new HBox();
        entityPopulationScrollPane.setContent(entityPopulationHBox);
        entityPopulationHBox.getChildren().clear();
        entityPopulationHBox.getChildren().addAll(epTableView.createEntityPopulationTableView(simulationEDDTO));
        if (isRunning.get() && informationComponentController.getExecutionResult().getChildren() != null) {
            informationComponentController.getExecutionResult().getChildren().clear();
        }
    }
    @FXML
    void pauseSimulationButtonAction(ActionEvent event) {
        appController.pauseSimulation(currentSimulationID.get());
    }

    @FXML
    void rerunSimulationButtonAction(ActionEvent event) {
        appController.rerunSimulation(currentSimulationID.get());
    }

    @FXML
    void resumeSimulationButtonAction(ActionEvent event) {
        appController.resumeSimulation(currentSimulationID.get());
    }

    @FXML
    void stopSimulationButtonAction(ActionEvent event) {
        appController.stopSimulation(currentSimulationID.get());
    }

    @FXML
    void gridViewButtonAction(ActionEvent event) {
        // Create an instance of the DynamicGridView class to generate the dynamic grid
        DynamicGridView dynamicGridView = new DynamicGridView();

        GridViewDTO gridViewDTO = appController.getGridViewDTO(currentSimulationID.get());
        // Call the createDynamicGrid method to create the dynamic grid
        ScrollPane dynamicGridScrollPane = dynamicGridView.createDynamicGrid(gridViewDTO);

        // Create a new stage for the grid view
        Stage gridViewStage = new Stage();
        gridViewStage.setTitle("Grid View");

        // Set the scene with the dynamic grid wrapped in the ScrollPane
        Scene scene = new Scene(dynamicGridScrollPane);
        gridViewStage.setScene(scene);

        // Show the grid view window
        gridViewStage.show();
    }

    public int getCurrentSimulationID() {
        return currentSimulationID.get();
    }

    public InformationController getInformationComponentController() {
        return informationComponentController;
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public SimpleBooleanProperty isRunningProperty() {
        return isRunning;
    }
}
