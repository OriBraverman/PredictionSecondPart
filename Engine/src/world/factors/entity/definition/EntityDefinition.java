package world.factors.entity.definition;

import world.factors.property.definition.api.EntityPropertyDefinition;
import world.factors.property.definition.api.PropertyDefinition;

import java.util.List;

public interface EntityDefinition {
    String getName();
    int getPopulation();
    void addProperty(EntityPropertyDefinition propertyDefinition);
    List<EntityPropertyDefinition> getProps();
    EntityPropertyDefinition getPropertyDefinitionByName(String name);
}
