package gui.components.main.app;

import engine.Engine;
import gui.components.main.details.DetailsController;
import gui.components.main.execution.NewExecutionController;
import gui.components.main.header.HeaderController;
import gui.components.main.results.ResultsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;

public class AppController {
    @FXML private GridPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private DetailsController detailsComponentController;
    @FXML private NewExecutionController newExecutionComponentController;
    @FXML private ResultsController resultsComponentController;
    private final Engine engine = new Engine();
    private final SimpleBooleanProperty isXMLLoaded;

    public AppController() {
        this.isXMLLoaded = new SimpleBooleanProperty(false);
    }

    @FXML public void initialize(){
        if (headerComponentController != null) {
            headerComponentController.setAppController(this);
        }
    }

    public void uploadWorldFromXML(File selectedFile) {
        try {
                engine.loadXML(selectedFile.toPath());
                headerComponentController.getUploadComponentController().setFileChosenStringProperty(selectedFile.toString());
                headerComponentController.getUploadComponentController().isXMLLoadedProperty().set(true);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
    }
}