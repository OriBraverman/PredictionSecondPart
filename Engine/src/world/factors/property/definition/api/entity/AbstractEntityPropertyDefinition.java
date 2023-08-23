package world.factors.property.definition.api.entity;

import value.generator.api.ValueGenerator;
import world.factors.property.definition.api.AbstractPropertyDefinition;
import world.factors.property.definition.api.EntityPropertyDefinition;
import world.factors.property.definition.api.PropertyType;

public abstract class AbstractEntityPropertyDefinition<T> extends AbstractPropertyDefinition implements EntityPropertyDefinition {
    private final ValueGenerator<T> valueGenerator;

    public AbstractEntityPropertyDefinition(String name, PropertyType propertyType, ValueGenerator<T> valueGenerator) {
        super(name, propertyType);
        this.valueGenerator = valueGenerator;
    }

    @Override
    public T generateValue() {
        return valueGenerator.generateValue();
    }

    @Override
    public boolean isNumeric() {
        return this.propertyType == PropertyType.DECIMAL || this.propertyType == PropertyType.FLOAT;
    }
}
