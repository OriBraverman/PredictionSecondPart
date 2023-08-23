package world.factors.property.definition.api;

public interface EntityPropertyDefinition extends PropertyDefinition{
    Object generateValue();
    boolean isNumeric();
}
