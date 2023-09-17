package dtos.result;

import java.util.Map;

public class EntityPopulationByTicksDTO {
    private final Map<Integer, Integer> entityPopulationByTicks;

    public EntityPopulationByTicksDTO(Map<Integer, Integer> entityPopulationByTicks) {
        this.entityPopulationByTicks = entityPopulationByTicks;
    }

    public Map<Integer, Integer> getEntityPopulationByTicks() {
        return entityPopulationByTicks;
    }
}
