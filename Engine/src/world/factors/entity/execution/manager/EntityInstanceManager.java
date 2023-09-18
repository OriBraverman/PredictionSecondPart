package world.factors.entity.execution.manager;


import world.factors.action.api.SecondaryEntity;
import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.environment.execution.api.ActiveEnvironment;
import world.factors.grid.Grid;
import world.factors.grid.execution.GridInstance;

import java.util.List;

public interface EntityInstanceManager {

    EntityInstance create(EntityDefinition entityDefinition, GridInstance grid);
    List<EntityInstance> getInstances();

    void setInstances(List<EntityInstance> instances);

    List<EntityInstance> getEntityInstancesByName(String entityName);

    void killEntity(int id);
    boolean isEntityAlive(int id);
    int getEntityCountByName(String entityName);
    void replaceDerived(EntityInstance entityInstance, EntityDefinition entityDefinition);
    void moveEntity(EntityInstance entityInstance, GridInstance grid);
    void moveAllInstances(GridInstance grid);
    int getAliveEntityCount();
    void addEntityDefinitionPopulation(EntityDefinition entityDefinition, int population);
    int getPopulationByEntityDefinition(EntityDefinition entityDefinition);
    List<EntityInstance> getSelectedSeconderyEntites(SecondaryEntity secondaryEntity, ActiveEnvironment activeEnvironment, GridInstance grid, int currentTick);
}
