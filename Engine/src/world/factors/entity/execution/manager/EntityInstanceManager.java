package world.factors.entity.execution.manager;


import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.grid.Grid;

import java.util.List;

public interface EntityInstanceManager {

    EntityInstance create(EntityDefinition entityDefinition, Grid grid);
    List<EntityInstance> getInstances();
    EntityInstance getEntityInstanceByName(String entityName);
    void killEntity(int id);
    boolean isEntityAlive(int id);
    int getEntityCountByName(String entityName);
    void replaceDerived(EntityInstance entityInstance, EntityDefinition entityDefinition);
    void moveEntity(EntityInstance entityInstance, Grid grid);
}
