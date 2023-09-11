package gui.components.main.results.scene;

import dtos.*;
import dtos.world.EntityDefinitionDTO;
import dtos.world.PropertyDefinitionDTO;
import dtos.world.WorldDTO;
import gui.components.main.app.AppController;
import gui.components.main.results.simulation.SimulationController;
import gui.components.main.upload.UploadController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;


public class ResultsController {
    @FXML private ListView<String> executionList;
    @FXML private SimulationController simulationComponentController;
    @FXML private AnchorPane simulationComponent;
    //when the first simulation starts initiate thread that will every 200 ms update the results list & the current selected simulation result
    private Thread updateResultThread;
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
            simulationComponentController.setEntitiesCountDisplay(entitiesCount + "");
            simulationComponentController.updateSimulationHistograms(id);
        }
    }
    public SimulationController getSimulationComponentController() {
        return simulationComponentController;

    }
}