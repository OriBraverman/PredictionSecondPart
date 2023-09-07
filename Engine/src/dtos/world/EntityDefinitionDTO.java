package dtos.world;

import dtos.EntityPropertyDefinitionDTO;

import java.util.List;

public class EntityDefinitionDTO {
    private String name;
    private int population;
    private List<PropertyDefinitionDTO> properties;

    public EntityDefinitionDTO(String name, int population, List<PropertyDefinitionDTO> properties) {
        this.name = name;
        this.population = population;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public List<PropertyDefinitionDTO> getProperties() {
        return properties;
    }
}
