package world.factors.entity.definition;

import world.factors.property.definition.api.PropertyDefinition;

import java.util.List;

public interface EntityDefinition {
    String getName();
    int getPopulation();
    void addProperty(PropertyDefinition propertyDefinition);
    List<PropertyDefinition> getProps();
    PropertyDefinition getPropertyDefinitionByName(String name);
}
