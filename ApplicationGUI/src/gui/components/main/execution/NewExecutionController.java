package gui.components.main.execution;

import dtos.*;
import dtos.world.EntityDefinitionDTO;
import dtos.world.PropertyDefinitionDTO;
import gui.components.main.app.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;

public class NewExecutionController {
    @FXML private Accordion envVariablesAccordion;
    @FXML private Accordion entityPopulationAccordion;
    @FXML private Button clearSimulationButton;
    @FXML private Button startSimulationButton;
    private AppController appController;
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML public void initialize(){
    }


    public void updateEnvVariablesInputVBox(NewExecutionInputDTO newExecutionInputDTO) {
        if (envVariablesAccordion.getPanes() != null) {
            envVariablesAccordion.getPanes().clear();
        }
        for (PropertyDefinitionDTO propertyDefinitionDTO : newExecutionInputDTO.getEnvVariables()) {
            envVariablesAccordion.getPanes().add(createEnvVarTitledPane(propertyDefinitionDTO));
        }
    }

    public void updateEntityPopulationInputVBox(NewExecutionInputDTO newExecutionInputDTO) {
        if (entityPopulationAccordion.getPanes() != null) {
            entityPopulationAccordion.getPanes().clear();
        }
        for (EntityDefinitionDTO entityDefinitionDTO : newExecutionInputDTO.getEntityDefinitions()) {
            entityPopulationAccordion.getPanes().add(createEntitiesTitledPane(entityDefinitionDTO));
        }
    }

    public TitledPane createEnvVarTitledPane(PropertyDefinitionDTO propertyDefinitionDTO) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText(propertyDefinitionDTO.getName());
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(401.0, 232.0);

        CheckBox checkBox = new CheckBox("Want to add value?");
        checkBox.setLayoutX(64.0);
        checkBox.setLayoutY(185.0);

        TextField textField = new TextField();
        textField.setLayoutX(201.0);
        textField.setLayoutY(181.0);

        // bind the text field to the checkbox
        textField.disableProperty().bind(checkBox.selectedProperty().not());

        Label nameLabel = new Label("Name:");
        nameLabel.setLayoutX(68.0);
        nameLabel.setLayoutY(52.0);

        Label nameLabel2 = new Label(propertyDefinitionDTO.getName());
        nameLabel2.setLayoutX(137.0);
        nameLabel2.setLayoutY(52.0);

        Label typeLabel = new Label("Type:");
        typeLabel.setLayoutX(72.0);
        typeLabel.setLayoutY(85.0);

        Label typeValueLabel = new Label(propertyDefinitionDTO.getType().toString());
        typeValueLabel.setLayoutX(137.0);
        typeValueLabel.setLayoutY(85.0);

        if (propertyDefinitionDTO.getFromRange() != null) {
            Label rangeLabel = new Label("Range:");
            rangeLabel.setLayoutX(72.0);
            rangeLabel.setLayoutY(116.0);

            Label fromLabel = new Label("From");
            fromLabel.setLayoutX(137.0);
            fromLabel.setLayoutY(116.0);

            Label fromValueLabel = new Label(propertyDefinitionDTO.getFromRange().toString());
            fromValueLabel.setLayoutX(187.0);
            fromValueLabel.setLayoutY(116.0);

            Label toLabel = new Label("To");
            toLabel.setLayoutX(240.0);
            toLabel.setLayoutY(116.0);

            Label toValueLabel = new Label(propertyDefinitionDTO.getToRange().toString());
            toValueLabel.setLayoutX(286.0);
            toValueLabel.setLayoutY(116.0);

            anchorPane.getChildren().addAll(
                    checkBox, nameLabel, textField, typeLabel, rangeLabel,
                    fromLabel, fromValueLabel, toLabel, toValueLabel,
                    typeValueLabel, nameLabel2
            );
        } else {
            anchorPane.getChildren().addAll(
                    checkBox, nameLabel, textField, typeLabel,
                    typeValueLabel, nameLabel2
            );
        }

        titledPane.setContent(anchorPane);
        return titledPane;
    }
    public TitledPane createEntitiesTitledPane(EntityDefinitionDTO entityDefinitionDTO) {
        return null;
    }
    @FXML
    void startSimulationAction(ActionEvent event) {
        EnvVariableValueDTO[] envVariablesDTOS = new EnvVariableValueDTO[envVariablesAccordion.getPanes().size()];
        for (int i = 0; i < envVariablesAccordion.getPanes().size(); i++) {
            TitledPane titledPane = envVariablesAccordion.getPanes().get(i);
            AnchorPane anchorPane = (AnchorPane) titledPane.getContent();
            CheckBox checkBox = (CheckBox) anchorPane.getChildren().get(0);
            EnvVariableValueDTO envVariableValueDTO;
            if (checkBox.isSelected()) {
                TextField textField = (TextField) anchorPane.getChildren().get(2);
                envVariableValueDTO = new EnvVariableValueDTO(titledPane.getText(), textField.getText(), true);
                if (!appController.validateEnvVariableValue(envVariableValueDTO)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid value");
                    alert.setContentText("Please enter a valid value for " + titledPane.getText());
                    alert.showAndWait();
                    return;
                }
            } else {
                envVariableValueDTO = new EnvVariableValueDTO(titledPane.getText(), "", false);
            }
            envVariablesDTOS[i] = envVariableValueDTO;
        }
    EnvVariablesValuesDTO envVariablesValuesDTO = new EnvVariablesValuesDTO(envVariablesDTOS);
    appController.activateSimulation(envVariablesValuesDTO);
    }

    @FXML
    void clearSimulationButtonAction(ActionEvent event) {
        for (TitledPane titledPane : envVariablesAccordion.getPanes()) {
            AnchorPane anchorPane = (AnchorPane) titledPane.getContent();
            CheckBox checkBox = (CheckBox) anchorPane.getChildren().get(0);
            checkBox.setSelected(false);
            TextField textField = (TextField) anchorPane.getChildren().get(2);
            textField.setText("");
        }
    }
}
