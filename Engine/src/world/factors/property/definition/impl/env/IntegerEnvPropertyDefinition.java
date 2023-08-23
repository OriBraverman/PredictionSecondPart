package world.factors.property.definition.impl.env;

import value.generator.api.ValueGenerator;
import world.factors.property.definition.api.PropertyType;
import world.factors.property.definition.api.Range;
import world.factors.property.definition.api.entity.AbstractNumericEntityPropertyDefinition;
import world.factors.property.definition.api.env.AbstractNumericPropertyDefinition;

public class IntegerEnvPropertyDefinition extends AbstractNumericPropertyDefinition {
    public IntegerEnvPropertyDefinition(String name, Range range) {
        super(name, PropertyType.DECIMAL, range);
    }
}
