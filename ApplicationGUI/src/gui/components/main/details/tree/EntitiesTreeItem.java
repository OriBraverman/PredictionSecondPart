package gui.components.main.details.tree;

import dtos.world.EntityDefinitionDTO;
import javafx.scene.control.TreeItem;

import java.util.List;

public class EntitiesTreeItem extends TreeItem<String> {
    private List<EntityDefinitionDTO> entities;
    public EntitiesTreeItem(List<EntityDefinitionDTO> entities) {
        super("Entities");
        this.entities = entities;
        entities.forEach(entity -> this.getChildren().add(new EntityTreeItem(entity)));

    }
}
