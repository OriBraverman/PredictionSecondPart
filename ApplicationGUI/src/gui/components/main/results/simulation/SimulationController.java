package gui.components.main.results.simulation;

import dtos.HistogramDTO;
import dtos.world.EntityDefinitionDTO;
import dtos.world.PropertyDefinitionDTO;
import dtos.world.WorldDTO;
import gui.components.main.app.AppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class SimulationController {
    @FXML private Label entitiesCountDisplay;
    @FXML private FlowPane executionResult;
    private AppController appController;
    public void initialize(){

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

}
