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
    @FXML private RadioButton entityPopulationRadioButton;
    @FXML private RadioButton propertyHistogramRadioButton;
    @FXML private RadioButton propertyConsistencyRadioButton;
    @FXML private Button gridViewButton;

    private AppController appController;

    private SimpleIntegerProperty currentSimulationID;
    private SimpleIntegerProperty entitiesCount;
    private SimpleIntegerProperty currentTick;
    private SimpleLongProperty timeSinceSimulationStarted;
    private SimpleBooleanProperty rerunSimulation;
    private SimpleBooleanProperty pauseSimulation;
    private SimpleBooleanProperty resumeSimulation;
    private SimpleBooleanProperty stopSimulation;
    private SimpleBooleanProperty entityPopulation;
    private SimpleBooleanProperty propertyHistogram;
    private SimpleBooleanProperty propertyConsistency;
    private static int counter = 0;

    public void initialize() {
        currentSimulationID = new SimpleIntegerProperty();
        entitiesCount = new SimpleIntegerProperty();
        currentTick = new SimpleIntegerProperty();
        timeSinceSimulationStarted = new SimpleLongProperty();
        rerunSimulation = new SimpleBooleanProperty(false);
        pauseSimulation = new SimpleBooleanProperty(false);
        resumeSimulation = new SimpleBooleanProperty(false);
        stopSimulation = new SimpleBooleanProperty(false);
        entityPopulation = new SimpleBooleanProperty(true);
        propertyHistogram = new SimpleBooleanProperty(false);
        propertyConsistency = new SimpleBooleanProperty(false);
        entitiesCountDisplay.textProperty().bind(entitiesCount.asString());
        currentTickDisplay.textProperty().bind(currentTick.asString());
        timeSinceSimulationStartedDisplay.textProperty().bind(timeSinceSimulationStarted.asString());
        rerunSimulationButton.visibleProperty().bind(rerunSimulation);
        pauseSimulationButton.disableProperty().bind(pauseSimulation.not());
        resumeSimulationButton.disableProperty().bind(resumeSimulation.not());
        stopSimulationButton.disableProperty().bind(stopSimulation.not());
        entityPopulationRadioButton.selectedProperty().bindBidirectional(entityPopulation);
        propertyHistogramRadioButton.selectedProperty().bindBidirectional(propertyHistogram);
        propertyConsistencyRadioButton.selectedProperty().bindBidirectional(propertyConsistency);
        entityPopulationRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                propertyHistogram.set(false);
                propertyConsistency.set(false);
            }
        });
        propertyHistogramRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                entityPopulation.set(false);
                propertyConsistency.set(false);
            }
        });
        propertyConsistencyRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                entityPopulation.set(false);
                propertyHistogram.set(false);
            }
        });
    }
    public void setAppController(AppController appController) {
        this.appController = appController;
    }
    public void setEntitiesCountDisplay(String entitiesCountDisplayText) {
        this.entitiesCountDisplay.setText(entitiesCountDisplayText);
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

    public void updateSimulationComponent(SimulationExecutionDetailsDTO simulationEDDTO) {
        currentSimulationID.set(simulationEDDTO.getId());
        entitiesCount.set(simulationEDDTO.getNumberOfEntities());
        currentTick.set(simulationEDDTO.getCurrentTick());
        timeSinceSimulationStarted.set(simulationEDDTO.getDurationInSeconds());
        if (simulationEDDTO.isRunning()) {
            if (simulationEDDTO.isPaused())
            {
                rerunSimulation.set(false);
                pauseSimulation.set(false);
                resumeSimulation.set(true);
                stopSimulation.set(true);
            } else {
                rerunSimulation.set(false);
                pauseSimulation.set(true);
                resumeSimulation.set(false);
                stopSimulation.set(true);
            }
        } else {
            rerunSimulation.set(true);
            pauseSimulation.set(false);
            resumeSimulation.set(false);
            stopSimulation.set(false);
        }
        EntityPopulationTableView epTableView = new EntityPopulationTableView();
        HBox entityPopulationHBox = new HBox();
        entityPopulationScrollPane.setContent(entityPopulationHBox);
        entityPopulationHBox.getChildren().clear();
        entityPopulationHBox.getChildren().addAll(epTableView.createEntityPopulationTableView(simulationEDDTO));
        counter = (counter + 1) % 5;
        if (counter == 0) {
            updateSimulationInfoBasedOnSelectedRadioButton(simulationEDDTO);
        }
    }

    private void updateSimulationInfoBasedOnSelectedRadioButton(SimulationExecutionDetailsDTO simulationEDDTO) {
        if (entityPopulation.get()) {
            //updateSimulationEntityPopulation(simulationEDDTO);
        } else if (propertyHistogram.get()) {
            updateSimulationHistograms(simulationEDDTO.getId());
        } else if (propertyConsistency.get()) {
            //updateSimulationPropertyConsistency(simulationEDDTO);
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
