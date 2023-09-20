package gui.components.main.results.simulation.information;

import dtos.EntitiesPopulationDTO;
import dtos.EntityPopulationDTO;
import dtos.result.EntityPopulationByTicksDTO;
import dtos.result.HistogramDTO;
import dtos.result.PropertyAvaregeValueDTO;
import dtos.result.PropertyConstistencyDTO;
import dtos.SimulationExecutionDetailsDTO;
import dtos.world.EntityDefinitionDTO;
import dtos.world.PropertyDefinitionDTO;
import dtos.world.WorldDTO;
import gui.components.main.app.AppController;
import gui.components.main.results.simulation.SimulationController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class InformationController {
    @FXML private FlowPane executionResult;
    @FXML private RadioButton entityPopulationByTicksRadioButton;
    @FXML private RadioButton propertyHistogramRadioButton;
    @FXML private Label consistencyDisplay;
    @FXML private Label avarageValueDisplay;
    @FXML private ChoiceBox<String> entityChoiceBox;
    @FXML private ChoiceBox<String> propertyChoiceBox;
    @FXML private Label chooseEntityandPropertyLabel;
    @FXML private Label consistencyLabel;
    @FXML private Label avarageValueLabel;

    private AppController appController;
    private SimulationController simulationController;

    private SimpleBooleanProperty entityPopulationByTicks;
    private SimpleBooleanProperty propertyHistogram;



    public void initialize() {
        entityPopulationByTicks = new SimpleBooleanProperty(false);
        propertyHistogram = new SimpleBooleanProperty(false);
        // Present the simulation results
        entityPopulationByTicksRadioButton.selectedProperty().bindBidirectional(entityPopulationByTicks);
        propertyHistogramRadioButton.selectedProperty().bindBidirectional(propertyHistogram);
        entityPopulationByTicksRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                propertyHistogram.set(false);
                updateSimulationEntityPopulation(simulationController.getCurrentSimulationID());
            }
        });
        propertyHistogramRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                entityPopulationByTicks.set(false);
            }
        });
        bindVisibilityOfComponentsToPropertyHistogram();
        entityChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            definePropertyChoiceBox(newValue);
        });
        propertyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updatePropertyStatistics();
        });
    }

    private void bindVisibilityOfComponentsToPropertyHistogram() {
        chooseEntityandPropertyLabel.visibleProperty().bind(propertyHistogram);
        consistencyLabel.visibleProperty().bind(propertyHistogram);
        avarageValueLabel.visibleProperty().bind(propertyHistogram);
        entityChoiceBox.visibleProperty().bind(propertyHistogram);
        propertyChoiceBox.visibleProperty().bind(propertyHistogram);
        consistencyDisplay.visibleProperty().bind(propertyHistogram);
        avarageValueDisplay.visibleProperty().bind(propertyHistogram);
    }

    private void updatePropertyStatistics() {
        int id = simulationController.getCurrentSimulationID();
        String entityName = entityChoiceBox.getSelectionModel().getSelectedItem();
        String propertyName = propertyChoiceBox.getSelectionModel().getSelectedItem();
        if (id == 0 || entityName == null || propertyName == null || !appController.isSimulationCompleted(id)) {
            return;
        }
        updatePropertyConsistency(id, entityName, propertyName);
        updatePropertyAvarageValue(id, entityName, propertyName);
        updatePropertyHistogram(id, entityName, propertyName);
    }

    private void updatePropertyAvarageValue(int id, String entityName, String propertyName) {
        PropertyAvaregeValueDTO propertyAvaregeValueDTO = appController.getPropertyAvaregeValueDTO(id, entityName, propertyName);
        if (propertyAvaregeValueDTO != null) {
            avarageValueDisplay.setText(propertyAvaregeValueDTO.getAvarageValue());
        }
    }

    private void updatePropertyConsistency(int id, String entityName, String propertyName) {
        PropertyConstistencyDTO propertyConsistencyDTO = appController.getPropertyConsistencyDTO(id, entityName, propertyName);
        if (propertyConsistencyDTO != null) {
            consistencyDisplay.setText(propertyConsistencyDTO.getConsistency());
        }
    }

    private void updatePropertyHistogram(int id, String entityName, String propertyName) {
        if (executionResult.getChildren() != null) {
            executionResult.getChildren().clear();
        }

        HistogramDTO histogramDTO = appController.getHistogramDTO(id, entityName, propertyName);
        // histogramDTO is a map of <Object, Integer> where the object can be:
        // 1. String
        // 2. Integer
        // 3. Float
        // 4. Boolean
        // create a bar chart for the histogramDTO
        BarChart<String, Number> barChart = new BarChart<>(new javafx.scene.chart.CategoryAxis(), new javafx.scene.chart.NumberAxis());
        barChart.setTitle(entityName + " - " + propertyName);
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

    public void updateInformationComponent(int simulationID) {
        if (propertyHistogram.get()) {
            updatePropertyStatistics();
        } else {
            updateSimulationEntityPopulation(simulationID);
        }
    }

    public void defineEntityChoiceBox() {
        entityChoiceBox.getItems().clear();
        WorldDTO worldDTO = appController.getWorldDTO();
        for (EntityDefinitionDTO entityDefinitionDTO : worldDTO.getEntities()) {
            entityChoiceBox.getItems().add(entityDefinitionDTO.getName());
        }
    }

    public void definePropertyChoiceBox(String entityName) {
        propertyChoiceBox.getItems().clear();
        WorldDTO worldDTO = appController.getWorldDTO();
        for (EntityDefinitionDTO entityDefinitionDTO : worldDTO.getEntities()) {
            if (entityDefinitionDTO.getName().equals(entityName)) {
                for (PropertyDefinitionDTO propertyDefinitionDTO : entityDefinitionDTO.getProperties()) {
                    propertyChoiceBox.getItems().add(propertyDefinitionDTO.getName());
                }
            }
        }
        propertyChoiceBox.getSelectionModel().selectFirst();
    }
    public void setAppController(AppController appController) {
        this.appController = appController;
    }


    private void updateSimulationEntityPopulation(int simulationID) {
        if (executionResult.getChildren() != null) {
            executionResult.getChildren().clear();
        }

        EntityPopulationByTicksDTO entityPopulationByTicksDTO = appController.getEntityPopulationByTicksDTO(simulationID);
        Map<Integer, List<EntityPopulationDTO>> entityPopulationByTicks = entityPopulationByTicksDTO.getEntityPopulationByTicks();
        List<String> entityNames = entityPopulationByTicksDTO.getEntityNames();

        BarChart<String, Number> barChart = new BarChart<>(new javafx.scene.chart.CategoryAxis(), new javafx.scene.chart.NumberAxis());
        barChart.setTitle("Entity population by ticks");
        javafx.scene.chart.CategoryAxis xAxis = (javafx.scene.chart.CategoryAxis) barChart.getXAxis();
        xAxis.setLabel("Tick");
        javafx.scene.chart.NumberAxis yAxis = (javafx.scene.chart.NumberAxis) barChart.getYAxis();
        yAxis.setLabel("Amount of entities");

        // Create a list of ticks sorted in ascending order
        List<Integer> sortedTicks = new ArrayList<>(entityPopulationByTicks.keySet());
        Collections.sort(sortedTicks);

        for (String entityName : entityNames) {
            javafx.scene.chart.XYChart.Series<String, Number> series = new javafx.scene.chart.XYChart.Series<>();
            series.setName(entityName);

            for (Integer tick : sortedTicks) {
                for (EntityPopulationDTO entityPopulationDTO : entityPopulationByTicks.get(tick)) {
                    if (entityPopulationDTO.getName().equals(entityName)) {
                        series.getData().add(new javafx.scene.chart.XYChart.Data<>(tick.toString(), Integer.parseInt(entityPopulationDTO.getPopulation())));
                    }
                }
            }
            barChart.getData().add(series);
        }

        executionResult.getChildren().add(barChart);
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
