package world.factors.entity.execution.manager;


import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;

import java.util.List;

public interface EntityInstanceManager {

    EntityInstance create(EntityDefinition entityDefinition);
    List<EntityInstance> getInstances();
    EntityInstance getEntityInstanceByName(String entityName);
    void killEntity(int id);
    boolean isEntityAlive(int id);
}
