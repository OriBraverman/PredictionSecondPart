package gui.components.main.results.simulation.information;

import dtos.HistogramDTO;
import dtos.SimulationExecutionDetailsDTO;
import dtos.gridView.GridViewDTO;
import dtos.world.EntityDefinitionDTO;
import dtos.world.PropertyDefinitionDTO;
import dtos.world.WorldDTO;
import gui.components.main.app.AppController;
import gui.components.main.results.simulation.SimulationController;
import gui.components.main.results.simulation.grid.DynamicGridView;
import gui.components.main.results.simulation.tableView.EntityPopulationTableView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class InformationController {
    @FXML private FlowPane executionResult;
    @FXML private RadioButton entityPopulationByTicksRadioButton;
    @FXML private RadioButton propertyHistogramRadioButton;
    @FXML private Label consistencyDisplay;
    @FXML private Label avarageValueDisplay;
    @FXML private ChoiceBox<String> entityChoiceBox;
    @FXML private ChoiceBox<String> propertyChoiceBox;

    private AppController appController;
    private SimulationController simulationController;

    private SimpleBooleanProperty entityPopulationByTicks;
    private SimpleBooleanProperty propertyHistogram;
    private SimpleBooleanProperty propertyConsistency;



    public void initialize() {
        entityPopulationByTicks = new SimpleBooleanProperty(true);
        propertyHistogram = new SimpleBooleanProperty(false);
        propertyConsistency = new SimpleBooleanProperty(false);
        // Present the simulation results
        entityPopulationByTicksRadioButton.selectedProperty().bindBidirectional(entityPopulationByTicks);
        propertyHistogramRadioButton.selectedProperty().bindBidirectional(propertyHistogram);
        entityPopulationByTicksRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                propertyHistogram.set(false);
                propertyConsistency.set(false);
                updateSimulationEntityPopulation(appController.getSimulationExecutionDetailsDTO(simulationController.getCurrentSimulationID()));
            }
        });
        propertyHistogramRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                entityPopulationByTicks.set(false);
                propertyConsistency.set(false);
                updateSimulationHistograms(simulationController.getCurrentSimulationID());
            }
        });
    }
    public void setAppController(AppController appController) {
        this.appController = appController;
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

    public void setSimulationComponentController(SimulationController simulationController) {
        this.simulationController = simulationController;
    }

    public FlowPane getExecutionResult() {
        return executionResult;
    }

    public void setExecutionResult(FlowPane executionResult) {
        this.executionResult = executionResult;
    }
}
