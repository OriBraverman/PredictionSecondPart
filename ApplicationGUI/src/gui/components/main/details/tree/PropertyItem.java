package gui.components.main.details.tree;

import dtos.world.PropertyDefinitionDTO;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;

public class PropertyItem extends TreeItem<String> implements OpenableItem{
    private PropertyDefinitionDTO propertyDefinitionDTO;
    public PropertyItem(PropertyDefinitionDTO envVariable) {
        super(envVariable.getName());
        this.propertyDefinitionDTO = envVariable;
    }

    @Override
    public Parent getFullView() {
        TreeItem<String> root = new TreeItem<>("Property Details:");
        root.getChildren().add(new TreeItem<>("Name: " + propertyDefinitionDTO.getName()));
        root.getChildren().add(new TreeItem<>("Type: " + propertyDefinitionDTO.getType()));
        if (propertyDefinitionDTO.getFrom() != null) {
            root.getChildren().add(new TreeItem<>("From Range: " + propertyDefinitionDTO.getFromRange()));
            root.getChildren().add(new TreeItem<>("To Range: " + propertyDefinitionDTO.getToRange()));
        }
    }
}
