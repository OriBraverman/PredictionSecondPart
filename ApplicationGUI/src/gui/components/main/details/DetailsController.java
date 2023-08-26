package gui.components.main.details;

import dtos.*;
import gui.components.main.app.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import world.factors.property.definition.api.PropertyDefinition;

public class DetailsController {
    @FXML private AnchorPane detailsComponent;
    @FXML private TreeView<String> detailsTreeView;
    @FXML private TreeView<String> fullInfoTree;

    private AppController appController;

    @FXML public void initialize(){
        if (detailsTreeView != null) {
            detailsTreeView.setRoot(null);
        }
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void updateDetailsTreeView(SimulationDetailsDTO simulationDetailsDTO) {
        TreeItem<String> root = detailsTreeView.getRoot();
        if (root == null) {
            detailsTreeView.setRoot(new TreeItem<>("Simulation Details:"));
        } else {
            root.getChildren().clear();
        }
        root = detailsTreeView.getRoot();
        root.getChildren().add(new TreeItem<>("Entities"));
        for (EntityDefinitionDTO entityDefinitionDTO : simulationDetailsDTO.getEntities()) {
            TreeItem<String> entityItem = new TreeItem<>("");
            // Create a button for each entity
            Button entityButton = new Button(entityDefinitionDTO.getName());
            entityButton.setOnAction(event -> {
                updateEntityComponent(entityDefinitionDTO);
            });
            entityItem.setGraphic(entityButton); // Set the button as the graphic
            root.getChildren().get(0).getChildren().add(entityItem);
        }
        root.getChildren().add(new TreeItem<>("Rules"));
        for (RuleDTO rule : simulationDetailsDTO.getRules()) {
            TreeItem<String> ruleItem = new TreeItem<>("");
            // Create a button for each rule
            Button ruleButton = new Button(rule.getName());
            ruleButton.setOnAction(event -> {
                updateRuleComponent(rule);
            });
            ruleItem.setGraphic(ruleButton); // Set the button as the graphic
            root.getChildren().get(1).getChildren().add(ruleItem);
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

    private void updateRuleComponent(RuleDTO rule) {
        TreeItem<String> root = fullInfoTree.getRoot();
        fullInfoTree.setRoot(new TreeItem<>("Rule Details:"));
        if (root != null) {
            root.getChildren().clear();
        }
        root = fullInfoTree.getRoot();
        root.getChildren().add(new TreeItem<>("Name: " + rule.getName()));
        root.getChildren().add(new TreeItem<>("Activation Condition: "));
        root.getChildren().get(1).getChildren().add(new TreeItem<>("Ticks: " + rule.getActivation().getTicks()));
        root.getChildren().get(1).getChildren().add(new TreeItem<>("Probability: " + rule.getActivation().getProbability()));
        root.getChildren().add(new TreeItem<>("Number of Actions: " + rule.getNumberOfActions()));
        root.getChildren().add(new TreeItem<>("Actions: "));
        for (int i = 0; i < rule.getActions().length; i++) {
            root.getChildren().get(3).getChildren().add(new TreeItem<>(rule.getActions()[i]));
        }
    }

    private void updateEntityComponent(EntityDefinitionDTO entityDefinitionDTO) {
        TreeItem<String> root = fullInfoTree.getRoot();
        fullInfoTree.setRoot(new TreeItem<>("Entity Details:"));
        if (root != null) {
            root.getChildren().clear();
        }
        root = fullInfoTree.getRoot();
        root.getChildren().add(new TreeItem<>("Name: " + entityDefinitionDTO.getName()));
        root.getChildren().add(new TreeItem<>("Population: " + entityDefinitionDTO.getPopulation()));
        root.getChildren().add(new TreeItem<>("Properties: "));
        EntityPropertyDefinitionDTO[] properties = entityDefinitionDTO.getProperties();
        for (int i = 0; i < properties.length; i++) {
            root.getChildren().get(2).getChildren().add(new TreeItem<>(properties[i].getName()));
            root.getChildren().get(2).getChildren().get(i).getChildren().add(new TreeItem<>("Type: " + properties[i].getType()));
            if (properties[i].getFromRange() != null) {
                root.getChildren().get(2).getChildren().get(i).getChildren().add(new TreeItem<>("From Range: " + properties[i].getFromRange()));
                root.getChildren().get(2).getChildren().get(i).getChildren().add(new TreeItem<>("To Range: " + properties[i].getToRange()));
            }
            root.getChildren().get(2).getChildren().get(i).getChildren().add(new TreeItem<>("Value Generated: " + properties[i].getValueGenerated()));

        }
    }
}
