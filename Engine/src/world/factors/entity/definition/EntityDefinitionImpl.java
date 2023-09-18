package world.factors.entity.definition;

import world.factors.property.definition.api.PropertyDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntityDefinitionImpl implements EntityDefinition, Serializable {

    private final String name;
    private final List<PropertyDefinition> properties;
    private int population;

    public EntityDefinitionImpl(String name) {
        this.name = name;
        this. properties = new ArrayList<>();
        this.population = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addProperty(PropertyDefinition propertyDefinition) {
        properties.add(propertyDefinition);
    }

    @Override
    public List<PropertyDefinition> getProps() {
        return properties;
    }

    @Override
    public void increasePopulation(){ this.population++; }

    @Override
    public void decreasePopulation(){ this.population--; }

    @Override
    public int getPopulation(){ return this.population; }

    @Override
    public PropertyDefinition getPropertyDefinitionByName(String name) {
        for (PropertyDefinition propertyDefinition : properties) {
            if (propertyDefinition.getName().equals(name)) {
                return propertyDefinition;
            }
        }
        return null;
    }
}
