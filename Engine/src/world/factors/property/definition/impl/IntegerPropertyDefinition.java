package world.factors.property.definition.impl;

import world.factors.property.definition.api.AbstractNumericPropertyDefinition;
import world.factors.property.definition.api.Range;
import world.factors.property.definition.api.PropertyType;
import value.generator.api.ValueGenerator;

public class IntegerPropertyDefinition extends AbstractNumericPropertyDefinition<Integer> {
    public IntegerPropertyDefinition(String name, ValueGenerator valueGenerator, Range range) {
        super(name, PropertyType.DECIMAL, valueGenerator, range);
    }

    @Override
    public boolean isNumeric() {
        return true;
    }
}
