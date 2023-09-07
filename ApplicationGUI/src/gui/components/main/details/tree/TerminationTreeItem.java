package gui.components.main.details.tree;

import dtos.world.TerminationDTO;
import javafx.scene.control.TreeItem;
import world.factors.termination.Termination;

public class TerminationTreeItem extends TreeItem<String> implements OpenableItem {
    private TerminationDTO termination;
    public TerminationTreeItem(TerminationDTO termination) {
        super("Termination Conditions");
        this.termination = termination;
    }

    @Override
    public TreeItem<String> getFullView() {
        TreeItem<String> root = new TreeItem<>("Termination Details:");
        return root;
    }
}
