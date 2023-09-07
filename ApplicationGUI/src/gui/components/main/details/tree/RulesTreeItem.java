package gui.components.main.details.tree;

import dtos.world.RuleDTO;
import javafx.scene.control.TreeItem;

import java.util.List;

public class RulesTreeItem extends TreeItem<String> {
    public RulesTreeItem(List<RuleDTO> rules) {
        super("Rules");
        rules.forEach(rule -> this.getChildren().add(new RuleTreeItem(rule)));
    }
}
