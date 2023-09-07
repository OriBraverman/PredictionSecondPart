package dtos.world.action;

import dtos.world.EntityDefinitionDTO;

public class KillActionDTO extends ActionDTO{
    public KillActionDTO(EntityDefinitionDTO primatyEntity) {
        super("Kill", primatyEntity);
    }
}