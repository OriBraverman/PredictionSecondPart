package world.factors.entity.definition;

import world.factors.property.definition.api.EntityPropertyDefinition;
import world.factors.property.definition.api.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class EntityDefinitionImpl implements EntityDefinition {

    private final String name;
    private final int population;
    private final List<EntityPropertyDefinition> properties;

    public EntityDefinitionImpl(String name, int population) {
        this.name = name;
        this.population = population;
        properties = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPopulation() {
        return population;
    }

    @Override
    public void addProperty(EntityPropertyDefinition propertyDefinition) {
        properties.add(propertyDefinition);
    }

    @Override
    public List<EntityPropertyDefinition> getProps() {
        return properties;
    }

    @Override
    public EntityPropertyDefinition getPropertyDefinitionByName(String name) {
        for (EntityPropertyDefinition propertyDefinition : properties) {
            if (propertyDefinition.getName().equals(name)) {
                return propertyDefinition;
            }
        }
        return null;
    }
}
