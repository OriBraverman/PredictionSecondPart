package gui.components.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class AppController {

@FXML
private Button LoadFileButton;

@FXML
private Button DetailsButton;

@FXML
private Button NewExecutionButton;

@FXML
private Button ResultsButton;

@FXML
    void DetailsButtonActionListener(ActionEvent event) {

            }

@FXML
    void LoadFileButtonActionListener(ActionEvent event) {
        if (event.getSource() == LoadFileButton) {
            System.out.println("LoadFileButtonActionListener");
        }
            }

@FXML
    void NewExecutionButtonActionListener(ActionEvent event) {

            }

@FXML
    void ResultsButtonActionListener(ActionEvent event) {

            }

}
