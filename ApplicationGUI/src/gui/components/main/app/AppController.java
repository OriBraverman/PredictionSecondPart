package gui.components.main.app;

import dtos.*;
import engine.Engine;
import gui.components.main.details.DetailsController;
import gui.components.main.execution.NewExecutionController;
import gui.components.main.results.ResultsController;
import gui.components.main.upload.UploadController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;

public class AppController {

    @FXML private HBox uploadComponent;
    @FXML private UploadController uploadComponentController;
    @FXML private AnchorPane detailsComponent;
    @FXML private DetailsController detailsComponentController;
    @FXML private AnchorPane newExecutionComponent;
    @FXML private NewExecutionController newExecutionComponentController;
    @FXML private ResultsController resultsComponentController;
    private final Engine engine = new Engine();
    private final SimpleBooleanProperty isXMLLoaded;

    public AppController() {
        this.isXMLLoaded = new SimpleBooleanProperty(false);
    }

    @FXML public void initialize(){
        if (uploadComponentController != null && detailsComponentController != null && newExecutionComponentController != null) {
            uploadComponentController.setAppController(this);
            detailsComponentController.setAppController(this);
            newExecutionComponentController.setAppController(this);
        }
    }

    public void uploadWorldFromXML(File selectedFile) {
        try {
                engine.loadXML(selectedFile.toPath());
                uploadComponentController.setFileChosenStringProperty(selectedFile.toString());
                uploadComponentController.isXMLLoadedProperty().set(true);
                detailsComponentController.updateDetailsTreeView(engine.getSimulationDetailsDTO());
                newExecutionComponentController.updateEnvVariablesInputVBox(engine.getEnvVariablesDTO());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
    }

    public boolean validateEnvVariableValue(EnvVariableValueDTO envVariableValueDTO) {
        return  engine.validateEnvVariableValue(envVariableValueDTO);
    }

    public void activateSimulation(EnvVariablesValuesDTO envVariablesValuesDTO) {
        engine.activateSimulation(envVariablesValuesDTO);
        //resultsComponentController.updateResultsTreeView(engine.getResultsDTO());
    }
}