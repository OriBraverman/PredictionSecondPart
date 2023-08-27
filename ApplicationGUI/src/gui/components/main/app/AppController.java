package gui.components.main.app;

import dtos.*;
import engine.Engine;
import gui.components.main.details.DetailsController;
import gui.components.main.execution.NewExecutionController;
import gui.components.main.results.ResultsController;
import gui.components.main.upload.UploadController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    @FXML private AnchorPane resultsComponent;
    @FXML private ResultsController resultsComponentController;
    @FXML private TabPane tabPane;
    private final Engine engine = new Engine();
    private final SimpleBooleanProperty isXMLLoaded;
    private final SimpleBooleanProperty isSimulationExecuted;

    public AppController() {
        this.isXMLLoaded = new SimpleBooleanProperty(false);
        this.isSimulationExecuted = new SimpleBooleanProperty(false);
    }

    @FXML public void initialize(){
        tabPane.getTabs().get(1).disableProperty().bind(isXMLLoaded.not());
        tabPane.getTabs().get(2).disableProperty().bind(isSimulationExecuted.not());
        if (uploadComponentController != null && detailsComponentController != null && newExecutionComponentController != null
                && resultsComponentController != null) {
            uploadComponentController.setAppController(this);
            detailsComponentController.setAppController(this);
            newExecutionComponentController.setAppController(this);
            resultsComponentController.setAppController(this);
        }
    }

    public void uploadWorldFromXML(File selectedFile) {
        try {
                engine.loadXML(selectedFile.toPath());
                uploadComponentController.setFileChosenStringProperty(selectedFile.toString());
                uploadComponentController.isXMLLoadedProperty().set(true);
                detailsComponentController.updateDetailsTreeView(engine.getSimulationDetailsDTO());
                newExecutionComponentController.updateEnvVariablesInputVBox(engine.getEnvVariablesDTO());
                resultsComponentController.resetExecutionList();
                isXMLLoaded.set(true);
                isSimulationExecuted.set(false);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
    }

    public boolean validateEnvVariableValue(EnvVariableValueDTO envVariableValueDTO) {
        return  engine.validateEnvVariableValue(envVariableValueDTO);
    }

    public void activateSimulation(EnvVariablesValuesDTO envVariablesValuesDTO) {
        isSimulationExecuted.set(true);
        engine.updateActiveEnvironmentAndInformUser(envVariablesValuesDTO);
        SimulationResultDTO simulationResultDTO = engine.activateSimulation();
        resultsComponentController.updateExecutionList(simulationResultDTO);
    }

    public SimulationIDListDTO getSimulationListDTO() {
        return engine.getSimulationListDTO();
    }

    public SimulationResultByAmountDTO getSimulationResultByAmountDTO(int simulationID) {
        return engine.getSimulationResultByAmountDTO(simulationID);
    }

    public SimulationDetailsDTO getSimulationDetailsDTO() {
        return engine.getSimulationDetailsDTO();
    }

    public HistogramDTO getHistogramDTO(int simulationID, String entityName, String propertyName) {
        return engine.getHistogramDTO(simulationID, entityName, propertyName);
    }
}