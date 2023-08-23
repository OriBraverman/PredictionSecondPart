package world.factors.property.definition.impl.entity;

import world.factors.property.definition.api.Range;
import world.factors.property.definition.api.entity.AbstractEntityPropertyDefinition;
import world.factors.property.definition.api.PropertyType;
import value.generator.api.ValueGenerator;
import world.factors.property.definition.api.entity.AbstractNumericEntityPropertyDefinition;

public class IntegerEntityPropertyDefinition extends AbstractNumericEntityPropertyDefinition<Integer> {
    public IntegerEntityPropertyDefinition(String name, ValueGenerator<Integer> valueGenerator, Range range) {
        super(name, PropertyType.DECIMAL, valueGenerator, range);
    }
}
