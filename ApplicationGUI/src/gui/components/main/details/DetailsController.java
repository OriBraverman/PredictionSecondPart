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
            makeButton(entityDefinitionDTO, root.getChildren().get(0));
        }
        root.getChildren().add(new TreeItem<>("Rules"));
        for (RuleDTO rule : simulationDetailsDTO.getRules()) {
            makeButton(rule, root.getChildren().get(1));
        }
        root.getChildren().add(new TreeItem<>("Environment Properties"));
        for (EnvVariableDefinitionDTO envVariable : simulationDetailsDTO.getEnvVariables()) {
            makeButton(envVariable, root.getChildren().get(2));
        }
        root.getChildren().add(new TreeItem<>("Termination Conditions"));
        TerminationDTO termination = simulationDetailsDTO.getTermination();
        makeButton(termination, root.getChildren().get(3));
    }

    private void makeButton(Object object, TreeItem<String> root) {
        TreeItem<String> item = new TreeItem<>("");

        Button button = new Button(getObjectString(object));
        button.setOnAction(event -> {
            if (object instanceof EntityDefinitionDTO) {
                updateEntityComponent((EntityDefinitionDTO) object);
            } else if (object instanceof RuleDTO) {
                updateRuleComponent((RuleDTO) object);
            } else if (object instanceof EnvVariableDefinitionDTO) {
                updateEnvVariableComponent((EnvVariableDefinitionDTO) object);
            } else if (object instanceof TerminationDTO) {
                updateTerminationComponent((TerminationDTO) object);
            }
        });
        item.setGraphic(button);
        root.getChildren().add(item);
    }

    private String getObjectString(Object object) {
        if (object instanceof EntityDefinitionDTO) {
            return ((EntityDefinitionDTO) object).getName();
        } else if (object instanceof RuleDTO) {
            return ((RuleDTO) object).getName();
        } else if (object instanceof EnvVariableDefinitionDTO) {
            return ((EnvVariableDefinitionDTO) object).getName();
        } else if (object instanceof TerminationDTO) {
            return "Termination";
        }
        return "";
    }

    private void updateTerminationComponent(TerminationDTO object) {
        TreeItem<String> root = fullInfoTree.getRoot();
        fullInfoTree.setRoot(new TreeItem<>("Termination Conditions:"));
        if (root != null) {
            root.getChildren().clear();
        }
        root = fullInfoTree.getRoot();
        root.getChildren().add(new TreeItem<>("Ticks: " + object.getTicksCount()));
        root.getChildren().add(new TreeItem<>("Probability: " + object.getSecondsCount()));
    }

    private void updateEnvVariableComponent(EnvVariableDefinitionDTO envVariable) {
        TreeItem<String> root = fullInfoTree.getRoot();
        fullInfoTree.setRoot(new TreeItem<>("Environment Variable Details:"));
        if (root != null) {
            root.getChildren().clear();
        }
        root = fullInfoTree.getRoot();
        root.getChildren().add(new TreeItem<>("Name: " + envVariable.getName()));
        root.getChildren().add(new TreeItem<>("Type: " + envVariable.getType()));
        if (envVariable.getFromRange() != null) {
            root.getChildren().add(new TreeItem<>("From Range: " + envVariable.getFromRange()));
            root.getChildren().add(new TreeItem<>("To Range: " + envVariable.getToRange()));
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
