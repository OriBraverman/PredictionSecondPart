package gui.components.main.results.simulation.grid;

import dtos.gridView.GridViewDTO;
import gui.components.main.app.AppController;
import gui.components.main.results.simulation.SimulationController;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GridController {
    @FXML private ScrollPane dynamicGrid;
    @FXML private ListView<?> listView;

    private AppController appController;

    private SimpleIntegerProperty currentSimulationID;

    @FXML
    public void initialize() {
        currentSimulationID = new SimpleIntegerProperty();
        Platform.runLater(() -> {
            updateGrid();
        });
    }

    public void updateGrid() {
        // Create an instance of the DynamicGridView class to generate the dynamic grid
        DynamicGridView dynamicGridView = new DynamicGridView();

        GridViewDTO gridViewDTO = appController.getGridViewDTO(currentSimulationID.get());
        ScrollPane scrollPane = dynamicGridView.createDynamicGrid(gridViewDTO);
        dynamicGrid.setContent(scrollPane);
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void setCurrentSimulationID(int simulationID) {
        currentSimulationID.set(simulationID);
    }

    @FXML
    void leftButtonAction(ActionEvent event) {

    }

    @FXML
    void rightButtonAction(ActionEvent event) {

    }
}
