package world.factors.property.definition.impl.entity;

import value.generator.api.ValueGenerator;
import world.factors.property.definition.api.entity.AbstractEntityPropertyDefinition;
import world.factors.property.definition.api.PropertyType;

public class BooleanEntityPropertyDefinition extends AbstractEntityPropertyDefinition<Boolean> {
    public BooleanEntityPropertyDefinition(String name, ValueGenerator<Boolean> valueGenerator) {
        super(name, PropertyType.BOOLEAN, valueGenerator);
    }
}
