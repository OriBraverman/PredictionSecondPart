package world.factors.property.definition.api.env;

import world.factors.property.definition.api.AbstractPropertyDefinition;
import world.factors.property.definition.api.NumericPropertyDefinition;
import world.factors.property.definition.api.PropertyType;
import world.factors.property.definition.api.Range;

public abstract class AbstractNumericPropertyDefinition extends AbstractPropertyDefinition implements NumericPropertyDefinition {
    private Range range;

    public AbstractNumericPropertyDefinition(String name, PropertyType propertyType, Range range) {
        super(name, propertyType);
        this.range = range;
    }

    @Override
    public Range getRange() {
        return range;
    }
}
