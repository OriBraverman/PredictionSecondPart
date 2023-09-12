package gui.components.main.app;

import dtos.*;
import dtos.world.WorldDTO;
import engine.Engine;
import gui.components.main.details.scene.DetailsController;
import gui.components.main.execution.scene.NewExecutionController;
import gui.components.main.results.scene.ResultsController;
import gui.components.main.upload.UploadController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

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
                && resultsComponentController != null && resultsComponentController.getSimulationComponentController() != null) {
            uploadComponentController.setAppController(this);
            detailsComponentController.setAppController(this);
            newExecutionComponentController.setAppController(this);
            resultsComponentController.setAppController(this);
            resultsComponentController.getSimulationComponentController().setAppController(this);
        }
    }

    public void uploadWorldFromXML(File selectedFile) {
        try {
                engine.loadXML(selectedFile.toPath());
                uploadComponentController.setFileChosenStringProperty(selectedFile.toString());
                uploadComponentController.isXMLLoadedProperty().set(true);
                detailsComponentController.updateDetailsTreeView(engine.getWorldDTO());
                newExecutionComponentController.updateEnvVariablesInputVBox(engine.getNewExecutionInputDTO());
                newExecutionComponentController.updateEntityPopulationInputVBox(engine.getNewExecutionInputDTO());
                resultsComponentController.resetExecutionList();
                isXMLLoaded.set(true);
                isSimulationExecuted.set(false);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
    }

    public void validateEnvVariableValue(EnvVariableValueDTO envVariableValueDTO) throws IllegalArgumentException {
        engine.validateEnvVariableValue(envVariableValueDTO);
    }

    public void activateSimulation(EnvVariablesValuesDTO envVariablesValuesDTO, EntitiesPopulationDTO entityPopulationDTO) {
        isSimulationExecuted.set(true);
        engine.updateActiveEnvironmentAndInformUser(envVariablesValuesDTO);
        engine.updateActiveEntityPopulation(entityPopulationDTO);
        SimulationIDDTO simulationIDDTO = engine.activateSimulation();
        resultsComponentController.addSimulationToExecutionList(simulationIDDTO);
        resultsComponentController.setIsActive(true);

    }

    public SimulationIDListDTO getSimulationListDTO() {
        return engine.getSimulationListDTO();
    }

    public SimulationResultByAmountDTO getSimulationResultByAmountDTO(int simulationID) {
        return engine.getSimulationResultByAmountDTO(simulationID);
    }

    public WorldDTO getWorldDTO() {
        return engine.getWorldDTO();
    }

    public HistogramDTO getHistogramDTO(int simulationID, String entityName, String propertyName) {
        return engine.getHistogramDTO(simulationID, entityName, propertyName);
    }

    public void validateEntitiesPopulation(EntitiesPopulationDTO entityPopulationDTOS) throws IllegalArgumentException{
        engine.validateEntitiesPopulation(entityPopulationDTOS);
    }

    public SimulationExecutionDetailsDTO getSimulationExecutionDetailsDTO(int simulationID) {
        return engine.getSimulationExecutionDetailsDTO(simulationID);
    }
}