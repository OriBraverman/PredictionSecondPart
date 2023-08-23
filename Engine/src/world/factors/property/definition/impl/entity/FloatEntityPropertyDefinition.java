package world.factors.property.definition.impl.entity;

import value.generator.api.ValueGenerator;
import world.factors.property.definition.api.NumericPropertyDefinition;
import world.factors.property.definition.api.Range;
import world.factors.property.definition.api.entity.AbstractEntityPropertyDefinition;
import world.factors.property.definition.api.PropertyType;
import world.factors.property.definition.api.entity.AbstractNumericEntityPropertyDefinition;

public class FloatEntityPropertyDefinition extends AbstractNumericEntityPropertyDefinition<Float> {
    public FloatEntityPropertyDefinition(String name, ValueGenerator<Float> valueGenerator, Range range) {
        super(name, PropertyType.FLOAT, valueGenerator, range);
    }
}
