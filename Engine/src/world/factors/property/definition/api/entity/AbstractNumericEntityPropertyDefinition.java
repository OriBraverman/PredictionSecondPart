package world.factors.property.definition.api.entity;

import value.generator.api.ValueGenerator;
import world.factors.property.definition.api.NumericPropertyDefinition;
import world.factors.property.definition.api.PropertyType;
import world.factors.property.definition.api.Range;

public abstract class AbstractNumericEntityPropertyDefinition<T> extends AbstractEntityPropertyDefinition implements NumericPropertyDefinition{
    private Range range;

    public AbstractNumericEntityPropertyDefinition(String name, PropertyType propertyType, ValueGenerator<T> valueGenerator, Range range) {
        super(name, propertyType, valueGenerator);
        this.range = range;
    }

    @Override
    public Range getRange() {
        return range;
    }

}
