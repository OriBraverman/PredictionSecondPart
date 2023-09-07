package gui.components.main.details;

import dtos.*;
import dtos.world.EntityDefinitionDTO;
import dtos.world.RuleDTO;
import dtos.world.TerminationDTO;
import dtos.world.WorldDTO;
import gui.components.main.app.AppController;
import gui.components.main.details.tree.OpenableItem;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

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

    public void updateDetailsTreeView(WorldDTO worldDTO) {
        TreeItem<String> root = detailsTreeView.getRoot();
        if (root == null) {
            detailsTreeView.setRoot(new TreeItem<>("Simulation Details:"));
        } else {
            root.getChildren().clear();
        }
        root = detailsTreeView.getRoot();
        detailsTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        detailsTreeView.setCellFactory(param -> new TreeCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(item);

                selectedProperty().addListener(e -> {
                    if (((ReadOnlyBooleanProperty)e).getValue()) {
                        if (getTreeItem() instanceof OpenableItem) {
                            OpenableItem openableItem = (OpenableItem) getTreeItem();
                            Parent root = openableItem.getFullView();
                            if (root instanceof TreeView) {
                                TreeView<String> treeView = (TreeView<String>) root;
                                treeView.setRoot(null);
                                treeView.setRoot(openableItem.getFullInfo());
                            }
                        }
                    }
                });
            }
        });
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

    private void updateEntityPropertyComponent(EntityPropertyDefinitionDTO entityProperty) {
        TreeItem<String> root = fullInfoTree.getRoot();
        fullInfoTree.setRoot(new TreeItem<>("Entity Property Details:"));
        if (root != null) {
            root.getChildren().clear();
        }
        root = fullInfoTree.getRoot();
        root.getChildren().add(new TreeItem<>("Name: " + entityProperty.getName()));
        root.getChildren().add(new TreeItem<>("Type: " + entityProperty.getType()));
        if (entityProperty.getFromRange() != null) {
            root.getChildren().add(new TreeItem<>("From Range: " + entityProperty.getFromRange()));
            root.getChildren().add(new TreeItem<>("To Range: " + entityProperty.getToRange()));
        }
    }
}
