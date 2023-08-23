package dtos;

public class EntityDefinitionDTO {
    private String name;
    private int population;
    private EntityPropertyDefinitionDTO[] properties;

    public EntityDefinitionDTO(String name, int population, EntityPropertyDefinitionDTO[] properties) {
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

    public EntityPropertyDefinitionDTO[] getProperties() {
        return properties;
    }
}
