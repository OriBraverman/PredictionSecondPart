package world.factors.entity.execution.manager;


import context.ContextImpl;
import world.factors.action.api.SecondaryEntity;
import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.entity.execution.EntityInstanceImpl;
import world.factors.environment.execution.api.ActiveEnvironment;
import world.factors.grid.Cell;
import world.factors.grid.Grid;
import world.factors.grid.execution.GridInstance;
import world.factors.property.definition.api.PropertyDefinition;
import world.factors.property.execution.PropertyInstance;
import world.factors.property.execution.PropertyInstanceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static validator.StringValidator.validateStringIsInteger;

public class EntityInstanceManagerImpl implements EntityInstanceManager, Serializable {

    private int count;
    private List<EntityInstance> instances;
    private Map<EntityDefinition, Integer> entityPopulationMap;

    public EntityInstanceManagerImpl() {
        count = 0;
        instances = new ArrayList<>();
        entityPopulationMap = new HashMap<>();
    }

    @Override
    public EntityInstance create(EntityDefinition entityDefinition, GridInstance grid) {
        count++;
        EntityInstance newEntityInstance = new EntityInstanceImpl(entityDefinition, count, grid.getRandomAvailableCell());
        instances.add(newEntityInstance);

        for (PropertyDefinition propertyDefinition : entityDefinition.getProps()) {
            Object value = propertyDefinition.generateValue();
            PropertyInstance newPropertyInstance = new PropertyInstanceImpl(propertyDefinition, value);
            newEntityInstance.addPropertyInstance(newPropertyInstance);
        }
        newEntityInstance.getEntityDefinition().increasePopulation();
        return newEntityInstance;
    }

    @Override
    public void replaceDerived(EntityInstance entityInstance, EntityDefinition entityDefinition) {
        EntityInstance newEntityInstance = new EntityInstanceImpl(entityDefinition, entityInstance.getId(), entityInstance.getCell());
        instances.remove(entityInstance);
        instances.add(newEntityInstance);

        for (PropertyDefinition propertyDefinition : entityDefinition.getProps()) {
            try {
                Object value = entityInstance.getPropertyByName(propertyDefinition.getName()).getValue();
                PropertyInstance newPropertyInstance = new PropertyInstanceImpl(propertyDefinition, value);
                newEntityInstance.addPropertyInstance(newPropertyInstance);
            } catch (IllegalArgumentException e) {
                Object value = propertyDefinition.generateValue();
                PropertyInstance newPropertyInstance = new PropertyInstanceImpl(propertyDefinition, value);
                newEntityInstance.addPropertyInstance(newPropertyInstance);
            }
        }
    }

    @Override
    public List<EntityInstance> getInstances() {
        return instances;
    }

    @Override
    public synchronized void setInstances(List<EntityInstance> instances) {
        this.instances = instances;
    }

    @Override
    public List<EntityInstance> getEntityInstancesByName(String entityName) {
        List<EntityInstance> res = new ArrayList<>();
        for (EntityInstance entityInstance : instances) {
            if (entityInstance.getEntityDefinition().getName().equals(entityName)) {
                res.add(entityInstance);
            }
        }
        return res;
    }

    @Override
    public void killEntity(int id) {
        for (EntityInstance entityInstance : instances) {
            if (entityInstance.getId() == id) {
                entityInstance.getCell().setOccupied(false);
                instances.remove(entityInstance);
                entityInstance.getEntityDefinition().decreasePopulation();
                return;
            }
        }
    }

    @Override
    public  boolean isEntityAlive(int id)
    {
        for (EntityInstance entityInstance: this.instances)
        {
            if (entityInstance.getId() == id){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getAliveEntityCount() {
        return instances.size();
    }

    @Override
    public int getEntityCountByName(String entityName) {
        int count = 0;
        for (EntityInstance entityInstance : instances) {
            if (entityInstance.getEntityDefinition().getName().equals(entityName)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void moveEntity(EntityInstance entityInstance, GridInstance grid) {
        Cell newCell;
        if ((newCell = grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.UP)) != null) {
            entityInstance.setCell(newCell);
        } else if ((newCell = grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.DOWN)) != null) {
            entityInstance.setCell(newCell);
        } else if ((newCell = grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.RIGHT)) != null) {
            entityInstance.setCell(newCell);
        } else if ((newCell = grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.LEFT)) != null) {
            entityInstance.setCell(newCell);
        }
    }

    @Override
    public void moveAllInstances(GridInstance grid) {
        for (EntityInstance entityInstance : instances) {
            moveEntity(entityInstance, grid);
        }
    }

    @Override
    public List<EntityInstance> getSelectedSeconderyEntites(SecondaryEntity secondaryEntity, ActiveEnvironment activeEnvironment, GridInstance grid, int currentTick) {
        List<EntityInstance> secondaryEntityInstances = instances
                .stream()
                .filter(instance -> instance.getEntityDefinition().getName().equals(secondaryEntity.getSecondaryEntityDefinition().getName()))
                .collect(Collectors.toList());
        if (secondaryEntity.getSelectionCount() == "ALL") {
            return secondaryEntityInstances;
        } else if (validateStringIsInteger(secondaryEntity.getSelectionCount())) {
            int count = Integer.parseInt(secondaryEntity.getSelectionCount());
            //need to select all the secondary entities that are satisfy the condition if the condition is not null
            List<EntityInstance> selectedSecondaryEntityInstances = null;
            if (secondaryEntity.getSelectionCondition() != null) {
                selectedSecondaryEntityInstances = instances
                        .stream()
                        .filter(instance -> secondaryEntity.getSelectionCondition().assertCondition(new ContextImpl(instance, this, activeEnvironment, grid, currentTick)))
                        .collect(Collectors.toList());
            }
            if (count < 0) {
                throw new IllegalArgumentException("secondary entity selection count is negative");
            } else if (count >= secondaryEntityInstances.size()) {
                return secondaryEntityInstances;
            } else if (selectedSecondaryEntityInstances != null) {
                List<EntityInstance> res = new ArrayList<>();
                if (selectedSecondaryEntityInstances.size() == 0) {
                    return res;
                }
                for (int i = 0; i < count; i++) {
                    res.add(selectedSecondaryEntityInstances.get((int) (Math.random() * selectedSecondaryEntityInstances.size())));
                }
                return res;
            } else {
                List<EntityInstance> res = new ArrayList<>();
                if (secondaryEntityInstances.size() == 0) {
                    return res;
                }
                for (int i = 0; i < count; i++) {
                    res.add(secondaryEntityInstances.get((int) (Math.random() * secondaryEntityInstances.size())));
                }
                return res;
            }
        } else {
            throw new IllegalArgumentException("secondary entity selection count is not a number");

        }
    }

    @Override
    public void addEntityDefinitionPopulation(EntityDefinition entityDefinition, int population){
        this.entityPopulationMap.put(entityDefinition, population);
    }

    @Override
    public int getPopulationByEntityDefinition(EntityDefinition entityDefinition){
        return this.entityPopulationMap.get(entityDefinition);
    }

    @Override
    public Map<EntityDefinition, Integer> getEntityPopulationMap() {
        return entityPopulationMap;
    }
}
