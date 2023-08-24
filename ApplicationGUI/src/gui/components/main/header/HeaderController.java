package gui.components.main.header;


import gui.components.main.app.AppController;
import gui.components.main.upload.UploadController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class HeaderController {
    @FXML private Button DetailsButton;
    @FXML private Label HeaderLabel;
    @FXML private Button LoadFileButton;
    @FXML private Button NewExecutionButton;
    @FXML private Button ResultsButton;
    @FXML private HBox uploadComponent;
    @FXML private UploadController uploadComponentController;
    private AppController appController;

    @FXML
    public void initialize() {
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
        this.uploadComponentController.setAppController(appController);
    }

    public UploadController getUploadComponentController() {
        return uploadComponentController;
    }
}