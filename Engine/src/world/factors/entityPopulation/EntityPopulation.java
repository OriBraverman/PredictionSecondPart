package world.factors.entityPopulation;

import world.factors.entity.definition.EntityDefinition;

public class EntityPopulation {
    private String entityName;
    private int population;

    public EntityPopulation(String entityName, int population) {
        this.entityName = entityName;
        this.population = population;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getPopulation() {
        return population;
    }
}
