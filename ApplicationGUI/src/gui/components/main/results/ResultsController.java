package gui.components.main.results;

import dtos.*;
import dtos.world.EntityDefinitionDTO;
import gui.components.main.app.AppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;


public class ResultsController {
    @FXML private ListView<String> executionList;
    @FXML private Label entitiesCountDisplay;
    @FXML private FlowPane executionResult;

    private AppController appController;
    public void initialize(){
        ObservableList<String> items = FXCollections.observableArrayList();
        executionList.setItems(items);
    }
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void resetExecutionList() {
        if (executionList.getItems() != null) {
            executionList.getItems().clear();
        }
    }

    public void updateExecutionList(SimulationResultDTO simulationResultDTO) {
        SimulationIDListDTO simulationIDListDTO = appController.getSimulationListDTO();
        for (SimulationIDDTO simulationIDDTO : simulationIDListDTO.getSimulationIDDTOS()) {
            if (simulationIDDTO.getId() == simulationResultDTO.getId()) {
                executionList.getItems().add("simulation " + simulationIDDTO.getId() + " - date: " + simulationIDDTO.getStartTime());
            }
        }
    }

    // if one of the items in the list executionList is clicked - reveal her result component
    @FXML
    public void onExecutionListClicked(MouseEvent event) {
        String selectedItem = executionList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String[] split = selectedItem.split(" ");
            int id = Integer.parseInt(split[1]);
            SimulationResultByAmountDTO simulationResultByAmountDTO = appController.getSimulationResultByAmountDTO(id);
            int entitiesCount = 0;
            for (int i = 0; i < simulationResultByAmountDTO.getEntityInstanceResults().length; i++) {
                entitiesCount += simulationResultByAmountDTO.getEntityInstanceResults()[i].getEndingPopulation();
            }
            entitiesCountDisplay.setText(entitiesCount + "");
            updateSimulationHistograms(id);
        }
    }

    private void updateSimulationHistograms(int id) {
        /*if (executionResult.getChildren() != null) {
            executionResult.getChildren().clear();
        }
        SimulationDetailsDTO simulationDetailsDTO = appController.getSimulationDetailsDTO();
        for (EntityDefinitionDTO entityDefinitionDTO : simulationDetailsDTO.getEntities()) {
            for (EntityPropertyDefinitionDTO entityPropertyDefinitionDTO : entityDefinitionDTO.getProperties()) {
                HistogramDTO histogramDTO = appController.getHistogramDTO(id, entityDefinitionDTO.getName(), entityPropertyDefinitionDTO.getName());
                // histogramDTO is a map of <Object, Integer> where the object can be:
                // 1. String
                // 2. Integer
                // 3. Float
                // 4. Boolean
                // create a bar chart for the histogramDTO
                BarChart<String, Number> barChart = new BarChart<>(new javafx.scene.chart.CategoryAxis(), new javafx.scene.chart.NumberAxis());
                barChart.setTitle(entityDefinitionDTO.getName() + " - " + entityPropertyDefinitionDTO.getName());
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
        }*/
    }
}