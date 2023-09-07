package gui.components.main.details;

import dtos.*;
import dtos.world.EntityDefinitionDTO;
import dtos.world.RuleDTO;
import dtos.world.TerminationDTO;
import dtos.world.WorldDTO;
import gui.components.main.app.AppController;
import gui.components.main.details.tree.OpenableItem;
import gui.components.main.details.tree.WorldDetailsItem;
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
        TreeItem<String> root = new WorldDetailsItem(worldDTO);
        detailsTreeView.setRoot(root);
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
                            TreeItem<String> root = openableItem.getFullView();
                            fullInfoTree.setRoot(root);
                        }
                    }
                });
            }
        });
    }
}
