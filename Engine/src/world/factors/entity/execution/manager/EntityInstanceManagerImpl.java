package world.factors.entity.execution.manager;


import context.ContextImpl;
import world.factors.action.api.SecondaryEntity;
import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.entity.execution.EntityInstanceImpl;
import world.factors.environment.execution.api.ActiveEnvironment;
import world.factors.grid.Coordinate;
import world.factors.grid.Grid;
import world.factors.property.definition.api.PropertyDefinition;
import world.factors.property.execution.PropertyInstance;
import world.factors.property.execution.PropertyInstanceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static validator.StringValidator.validateStringIsInteger;

public class EntityInstanceManagerImpl implements EntityInstanceManager, Serializable {

    private int count;
    private List<EntityInstance> instances;

    public EntityInstanceManagerImpl() {
        count = 0;
        instances = new CopyOnWriteArrayList<>();
    }

    @Override
    public EntityInstance create(EntityDefinition entityDefinition, Grid grid) {
        count++;
        EntityInstance newEntityInstance = new EntityInstanceImpl(entityDefinition, count, grid.getRandomAvailableCoordinate());
        instances.add(newEntityInstance);

        for (PropertyDefinition propertyDefinition : entityDefinition.getProps()) {
            Object value = propertyDefinition.generateValue();
            PropertyInstance newPropertyInstance = new PropertyInstanceImpl(propertyDefinition, value);
            newEntityInstance.addPropertyInstance(newPropertyInstance);
        }
        return newEntityInstance;
    }

    @Override
    public void replaceDerived(EntityInstance entityInstance, EntityDefinition entityDefinition) {
        EntityInstance newEntityInstance = new EntityInstanceImpl(entityDefinition, entityInstance.getId(), entityInstance.getCoordinate());
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
    public EntityInstance getEntityInstanceByName(String entityName) {
        for (EntityInstance entityInstance : instances) {
            if (entityInstance.getEntityDefinition().getName().equals(entityName)) {
                return entityInstance;
            }
        }
        return null;
    }

    @Override
    public void killEntity(int id) {
        for (EntityInstance entityInstance : instances) {
            if (entityInstance.getId() == id) {
                instances.remove(entityInstance);
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
    public void moveEntity(EntityInstance entityInstance, Grid grid) {
        Coordinate newCoordinate;
        if ((newCoordinate = grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.UP)) != null) {
            entityInstance.setCoordinate(newCoordinate);
        } else if ((newCoordinate = grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.DOWN)) != null) {
            entityInstance.setCoordinate(newCoordinate);
        } else if ((newCoordinate = grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.RIGHT)) != null) {
            entityInstance.setCoordinate(newCoordinate);
        } else if ((newCoordinate = grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.LEFT)) != null) {
            entityInstance.setCoordinate(newCoordinate);
        }
    }

    @Override
    public void moveAllInstances(Grid grid) {
        for (EntityInstance entityInstance : instances) {
            moveEntity(entityInstance, grid);
        }
    }

    @Override
    public List<EntityInstance> getSelectedSeconderyEntites(SecondaryEntity secondaryEntity, ActiveEnvironment activeEnvironment, Grid grid, int currentTick) {
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
}
