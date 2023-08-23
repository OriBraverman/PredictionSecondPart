package world.factors.property.definition.impl.entity;

import world.factors.property.definition.api.entity.AbstractEntityPropertyDefinition;
import world.factors.property.definition.api.PropertyType;
import value.generator.api.ValueGenerator;

public class StringEntityPropertyDefinition extends AbstractEntityPropertyDefinition<String> {

    public StringEntityPropertyDefinition(String name, ValueGenerator<String> valueGenerator) {
        super(name, PropertyType.STRING, valueGenerator);
    }

}