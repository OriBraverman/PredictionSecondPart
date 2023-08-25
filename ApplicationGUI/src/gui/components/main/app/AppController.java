package gui.components.main.app;

import dtos.EntityDefinitionDTO;
import dtos.RuleDTO;
import dtos.SimulationDetailsDTO;
import dtos.TerminationDTO;
import engine.Engine;
import gui.components.main.details.DetailsController;
import gui.components.main.execution.NewExecutionController;
import gui.components.main.results.ResultsController;
import gui.components.main.upload.UploadController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;

public class AppController {

    @FXML private HBox uploadComponent;
    @FXML private UploadController uploadComponentController;
    @FXML private DetailsController detailsComponentController;
    @FXML private NewExecutionController newExecutionComponentController;
    @FXML private ResultsController resultsComponentController;
    @FXML private TreeView<String> detailsTreeView;
    private final Engine engine = new Engine();
    private final SimpleBooleanProperty isXMLLoaded;

    public AppController() {
        this.isXMLLoaded = new SimpleBooleanProperty(false);
    }

    @FXML public void initialize(){
        if (uploadComponentController != null) {
            uploadComponentController.setAppController(this);
        }
    }

    public void uploadWorldFromXML(File selectedFile) {
        try {
                engine.loadXML(selectedFile.toPath());
                uploadComponentController.setFileChosenStringProperty(selectedFile.toString());
                uploadComponentController.isXMLLoadedProperty().set(true);
                updateDetailsTreeView();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
    }

    private void updateDetailsTreeView() {
        SimulationDetailsDTO simulationDetailsDTO = engine.getSimulationDetailsDTO();
        TreeItem<String> root = detailsTreeView.getRoot();
        if (root == null) {
            detailsTreeView.setRoot(new TreeItem<>("Simulation Details:"));
        } else {
            root.getChildren().clear();
        }
        root = detailsTreeView.getRoot();
        root.getChildren().add(new TreeItem<>("Entities"));
        for (EntityDefinitionDTO entityDefinitionDTO : simulationDetailsDTO.getEntities()) {
            root.getChildren().get(0).getChildren().add(new TreeItem<>(entityDefinitionDTO.getName()));
        }
        root.getChildren().add(new TreeItem<>("Rules"));
        for (RuleDTO rule : simulationDetailsDTO.getRules()) {
            root.getChildren().get(1).getChildren().add(new TreeItem<>(rule.getName()));
        }
        root.getChildren().add(new TreeItem<>("Environment Properties"));
        root.getChildren().add(new TreeItem<>("Termination Conditions"));
        TerminationDTO termination = simulationDetailsDTO.getTermination();
        if (termination.getTicksCount() != -1) {
            root.getChildren().get(3).getChildren().add(new TreeItem<>("Ticks: " + termination.getTicksCount()));
        }
        if (termination.getSecondsCount() != -1) {
            root.getChildren().get(3).getChildren().add(new TreeItem<>("Seconds: " + termination.getSecondsCount()));
        }
    }


}