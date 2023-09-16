package gui.components.main.results.simulation;

import dtos.EntityPopulationDTO;
import dtos.HistogramDTO;
import dtos.SimulationExecutionDetailsDTO;
import dtos.gridView.GridViewDTO;
import dtos.world.EntityDefinitionDTO;
import dtos.world.PropertyDefinitionDTO;
import dtos.world.WorldDTO;
import gui.components.main.app.AppController;
import gui.components.main.results.simulation.grid.DynamicGridView;
import gui.components.main.results.simulation.tableView.EntityPopulationTableView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class SimulationController {
    @FXML private Label entitiesCountDisplay;
    @FXML private FlowPane executionResult;
    @FXML private Label currentTickDisplay;
    @FXML private Label timeSinceSimulationStartedDisplay;
    @FXML private Button rerunSimulationButton;
    @FXML private Button pauseSimulationButton;
    @FXML private Button resumeSimulationButton;
    @FXML private Button stopSimulationButton;
    @FXML private ScrollPane entityPopulationScrollPane;
    @FXML private RadioButton entityPopulationByTicksRadioButton;
    @FXML private RadioButton propertyHistogramRadioButton;
    @FXML private Button gridViewButton;

    private AppController appController;

    private SimpleIntegerProperty currentSimulationID;
    private SimpleIntegerProperty entitiesCount;
    private SimpleIntegerProperty currentTick;
    private SimpleLongProperty timeSinceSimulationStarted;
    private SimpleBooleanProperty isRunning;
    private SimpleBooleanProperty isPaused;
    private SimpleBooleanProperty entityPopulationByTicks;
    private SimpleBooleanProperty propertyHistogram;
    private SimpleBooleanProperty propertyConsistency;

    public void initialize() {
        currentSimulationID = new SimpleIntegerProperty();
        entitiesCount = new SimpleIntegerProperty();
        currentTick = new SimpleIntegerProperty();
        timeSinceSimulationStarted = new SimpleLongProperty();
        isRunning = new SimpleBooleanProperty(false);
        isPaused = new SimpleBooleanProperty(false);
        entityPopulationByTicks = new SimpleBooleanProperty(true);
        propertyHistogram = new SimpleBooleanProperty(false);
        propertyConsistency = new SimpleBooleanProperty(false);
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

        // Present the simulation results
        entityPopulationByTicksRadioButton.selectedProperty().bindBidirectional(entityPopulationByTicks);
        propertyHistogramRadioButton.selectedProperty().bindBidirectional(propertyHistogram);
        entityPopulationByTicksRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                propertyHistogram.set(false);
                propertyConsistency.set(false);
                updateSimulationEntityPopulation(appController.getSimulationExecutionDetailsDTO(currentSimulationID.get()));
            }
        });
        propertyHistogramRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                entityPopulationByTicks.set(false);
                propertyConsistency.set(false);
                updateSimulationHistograms(currentSimulationID.get());
            }
        });
        entityPopulationByTicksRadioButton.disableProperty().bind(isRunning);
        propertyHistogramRadioButton.disableProperty().bind(isRunning);
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
        if (isRunning.get() && executionResult.getChildren() != null) {
            executionResult.getChildren().clear();
        }
    }

    private void updateSimulationPropertyConsistency(SimulationExecutionDetailsDTO simulationEDDTO) {

    }

    private void updateSimulationEntityPopulation(SimulationExecutionDetailsDTO simulationEDDTO) {
    }
    public void updateSimulationHistograms(int id) {
        if (executionResult.getChildren() != null) {
            executionResult.getChildren().clear();
        }
        WorldDTO worldDTO = appController.getWorldDTO();
        for (EntityDefinitionDTO entityDefinitionDTO : worldDTO.getEntities()) {
            for (PropertyDefinitionDTO propertyDefinitionDTO : entityDefinitionDTO.getProperties()) {
                HistogramDTO histogramDTO = appController.getHistogramDTO(id, entityDefinitionDTO.getName(), propertyDefinitionDTO.getName());
                // histogramDTO is a map of <Object, Integer> where the object can be:
                // 1. String
                // 2. Integer
                // 3. Float
                // 4. Boolean
                // create a bar chart for the histogramDTO
                BarChart<String, Number> barChart = new BarChart<>(new javafx.scene.chart.CategoryAxis(), new javafx.scene.chart.NumberAxis());
                barChart.setTitle(entityDefinitionDTO.getName() + " - " + propertyDefinitionDTO.getName());
                javafx.scene.chart.CategoryAxis xAxis = (javafx.scene.chart.CategoryAxis) barChart.getXAxis();
                xAxis.setLabel("Value of property");
                javafx.scene.chart.NumberAxis yAxis = (javafx.scene.chart.NumberAxis) barChart.getYAxis();
                yAxis.setLabel("Amount of entities");
                javafx.scene.chart.XYChart.Series<String, Number> series = new javafx.scene.chart.XYChart.Series<>();
                for (Object key : histogramDTO.getHistogram().keySet()) {
                    series.getData().add(new javafx.scene.chart.XYChart.Data<>(key.toString(), histogramDTO.getHistogram().get(key)));
                }
                barChart.getData().add(series);
                executionResult.getChildren().add(barChart);
            }
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
}
