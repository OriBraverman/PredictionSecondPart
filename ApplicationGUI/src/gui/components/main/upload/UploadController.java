package gui.components.main.upload;


import gui.components.main.app.AppController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;


import java.io.File;

public class UploadController {
    @FXML
    private Button uploadButton;

    @FXML
    private Label fileLabel;

    @FXML
    private Label fileChosenLabel;

    private AppController appController;

    private final SimpleStringProperty fileChosenStringProperty;
    private final SimpleBooleanProperty isMachineLoaded;

    public UploadController() {
        fileChosenStringProperty = new SimpleStringProperty();
        isMachineLoaded = new SimpleBooleanProperty(false);
    }


    @FXML
    public void initialize(){
        fileChosenLabel.textProperty().bind(fileChosenStringProperty);
        fileLabel.visibleProperty().bind(isMachineLoaded);

    }

    public void setFileChosenStringProperty(String fileChosenStringProperty) {
        this.fileChosenStringProperty.set(fileChosenStringProperty);
    }

    @FXML
    void uploadMachineFromXML(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        //appController.uploadPredictionFromXML(selectedFile);
    }


    public SimpleBooleanProperty isMachineLoadedProperty() {
        return isMachineLoaded;
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }
}
