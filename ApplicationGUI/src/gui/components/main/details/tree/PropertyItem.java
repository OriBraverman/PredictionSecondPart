package gui.components.main.details.tree;

import dtos.world.PropertyDefinitionDTO;
import javafx.scene.control.TreeItem;

public class PropertyItem extends TreeItem<String> implements OpenableItem{
    private PropertyDefinitionDTO propertyDefinitionDTO;
    public PropertyItem(PropertyDefinitionDTO envVariable) {
        super(envVariable.getName());
        setExpanded(true);
        this.propertyDefinitionDTO = envVariable;
    }

    @Override
    public TreeItem<String> getFullView() {
        TreeItem<String> root = new TreeItem<>("Property Details:");
        root.getChildren().add(new TreeItem<>("Name: " + propertyDefinitionDTO.getName()));
        root.getChildren().add(new TreeItem<>("Type: " + propertyDefinitionDTO.getType()));
        if (propertyDefinitionDTO.getFromRange() != null) {
            root.getChildren().add(new TreeItem<>("From Range: " + propertyDefinitionDTO.getFromRange()));
            root.getChildren().add(new TreeItem<>("To Range: " + propertyDefinitionDTO.getToRange()));
        }
        return root;
    }
}
