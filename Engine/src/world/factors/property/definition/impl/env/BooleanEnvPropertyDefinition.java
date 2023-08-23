package world.factors.property.definition.impl.env;

import value.generator.api.ValueGenerator;
import world.factors.property.definition.api.AbstractPropertyDefinition;
import world.factors.property.definition.api.PropertyType;
import world.factors.property.definition.api.entity.AbstractEntityPropertyDefinition;

public class BooleanEnvPropertyDefinition extends AbstractPropertyDefinition {
    public BooleanEnvPropertyDefinition(String name) {
        super(name, PropertyType.BOOLEAN);
    }
}
