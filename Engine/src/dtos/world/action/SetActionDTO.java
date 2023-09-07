package dtos.world.action;

import dtos.world.EntityDefinitionDTO;

public class SetActionDTO extends ActionDTO {
    private final String propName;
    private final String value;

    public SetActionDTO(String type, EntityDefinitionDTO primatyEntity, String propName, String value) {
        super(type, primatyEntity);
        this.propName = propName;
        this.value = value;
    }

    public String getPropName() {
        return propName;
    }

    public String getValue() {
        return value;
    }
}