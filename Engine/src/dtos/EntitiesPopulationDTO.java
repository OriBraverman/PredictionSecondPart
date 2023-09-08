package dtos;

import java.util.List;

public class EntitiesPopulationDTO {
    private final List<EntityPopulationDTO> entitiesPopulation;

    public EntitiesPopulationDTO(List<EntityPopulationDTO> entitiesPopulation) {
        this.entitiesPopulation = entitiesPopulation;
    }

    public List<EntityPopulationDTO> getEntitiesPopulation() {
        return entitiesPopulation;
    }
}
