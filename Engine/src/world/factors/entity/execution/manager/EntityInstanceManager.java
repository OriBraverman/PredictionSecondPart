package world.factors.entity.execution.manager;


import world.factors.action.api.SecondaryEntity;
import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.environment.execution.api.ActiveEnvironment;
import world.factors.grid.Grid;

import java.util.List;

public interface EntityInstanceManager {

    EntityInstance create(EntityDefinition entityDefinition, Grid grid);
    List<EntityInstance> getInstances();

    void setInstances(List<EntityInstance> instances);

    List<EntityInstance> getEntityInstancesByName(String entityName);

    void killEntity(int id);
    boolean isEntityAlive(int id);
    int getEntityCountByName(String entityName);
    void replaceDerived(EntityInstance entityInstance, EntityDefinition entityDefinition);
    void moveEntity(EntityInstance entityInstance, Grid grid);
    void moveAllInstances(Grid grid);
    int getAliveEntityCount();
    List<EntityInstance> getSelectedSeconderyEntites(SecondaryEntity secondaryEntity, ActiveEnvironment activeEnvironment, Grid grid, int currentTick);
}
