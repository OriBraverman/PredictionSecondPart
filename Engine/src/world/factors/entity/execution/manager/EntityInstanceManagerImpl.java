package world.factors.entity.execution.manager;


import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.entity.execution.EntityInstanceImpl;
import world.factors.grid.Grid;
import world.factors.property.definition.api.PropertyDefinition;
import world.factors.property.execution.PropertyInstance;
import world.factors.property.execution.PropertyInstanceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntityInstanceManagerImpl implements EntityInstanceManager, Serializable {

    private int count;
    private List<EntityInstance> instances;

    public EntityInstanceManagerImpl() {
        count = 0;
        instances = new ArrayList<>();
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
            if (entityInstance.getPropertyByName(propertyDefinition.getName()) != null) {
                Object value = entityInstance.getPropertyByName(propertyDefinition.getName()).getValue();
                PropertyInstance newPropertyInstance = new PropertyInstanceImpl(propertyDefinition, value);
                newEntityInstance.addPropertyInstance(newPropertyInstance);
            } else {
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
        if (grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.UP) != null) {
            entityInstance.setCoordinate(grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.UP));
        } else if (grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.DOWN) != null) {
            entityInstance.setCoordinate(grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.DOWN));
        } else if (grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.RIGHT) != null) {
            entityInstance.setCoordinate(grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.RIGHT));
        } else if (grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.LEFT) != null) {
            entityInstance.setCoordinate(grid.moveEntity(entityInstance.getCoordinate(), Grid.Direction.LEFT));
        }
    }
}
