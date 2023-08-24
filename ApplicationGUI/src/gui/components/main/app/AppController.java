package gui.components.main.app;

import gui.components.main.upload.UploadController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;

public class AppController {
    //@FXML private HeaderController headerComponentController;
    @FXML private UploadController uploadComponentController;
    private final SimpleBooleanProperty isXMLLoaded;

    public AppController() {
        this.isXMLLoaded = new SimpleBooleanProperty(false);
    }

    @FXML public void initialize(){
        uploadComponentController.isXMLLoadedProperty().bind(this.isXMLLoaded);
    }
}