package world.factors.property.definition.impl.env;

import world.factors.property.definition.api.AbstractPropertyDefinition;
import world.factors.property.definition.api.PropertyType;

public class StringEnvPropertyDefinition extends AbstractPropertyDefinition {

    public StringEnvPropertyDefinition(String name) {
        super(name, PropertyType.STRING);
    }

}