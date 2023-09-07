package gui.components.main.details.tree;

import dtos.world.action.AbstructActionDTO;
import javafx.scene.control.TreeItem;

import java.util.List;

public class ActionsTreeItem extends TreeItem<String> {
    private List<AbstructActionDTO> actions;
    public ActionsTreeItem(List<AbstructActionDTO> actions) {
        super("Actions");
        this.actions = actions;
        actions.forEach(action -> this.getChildren().add(new ActionTreeItem(action)));
    }
}
