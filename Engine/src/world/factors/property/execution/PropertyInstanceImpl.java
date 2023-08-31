package world.factors.property.execution;

import world.factors.property.definition.api.PropertyDefinition;
import world.factors.property.definition.api.PropertyType;

import java.io.Serializable;

public class PropertyInstanceImpl implements PropertyInstance, Serializable {

    private PropertyDefinition propertyDefinition;
    private Object value;
    private int lastUpdatedTick;

    public PropertyInstanceImpl(PropertyDefinition propertyDefinition, Object value) {
        this.propertyDefinition = propertyDefinition;
        this.value = value;
    }

    @Override
    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void updateValue(Object val) {
        this.value = val;
    }

    @Override
    public PropertyType getType() {
        return propertyDefinition.getType();
    }
}
