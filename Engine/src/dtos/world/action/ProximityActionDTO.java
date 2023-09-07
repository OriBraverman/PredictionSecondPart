package dtos.world.action;

import dtos.world.EntityDefinitionDTO;

import java.util.List;

public class ProximityActionDTO extends ActionDTO {
    private final String ofValue;
    private final List<ActionDTO> thenActions;

    public ProximityActionDTO(EntityDefinitionDTO primatyEntity, String ofValue, List<ActionDTO> thenActions) {
        super("Proximity", primatyEntity);
        this.ofValue = ofValue;
        this.thenActions = thenActions;
    }

    public String getOfValue() {
        return ofValue;
    }

    public List<ActionDTO> getThenActions() {
        return thenActions;
    }
}