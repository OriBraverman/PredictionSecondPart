package dtos.world.action;

import dtos.world.EntityDefinitionDTO;

public class ReplaceActionDTO extends ActionDTO {
    private final EntityDefinitionDTO createdEntity;
    private final String mode;

    public ReplaceActionDTO(String type, EntityDefinitionDTO primatyEntity, EntityDefinitionDTO createdEntity, String mode) {
        super(type, primatyEntity);
        this.createdEntity = createdEntity;
        this.mode = mode;
    }

    public EntityDefinitionDTO getCreatedEntity() {
        return createdEntity;
    }

    public String getMode() {
        return mode;
    }
}