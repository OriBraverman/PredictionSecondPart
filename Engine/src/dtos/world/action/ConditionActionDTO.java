package dtos.world.action;

import dtos.world.EntityDefinitionDTO;

import java.util.List;

public class ConditionActionDTO extends ActionDTO {
    private final String conditionExpression;
    private final List<ActionDTO> thenActions;
    private final List<ActionDTO> elseActions;

    public ConditionActionDTO(String type, EntityDefinitionDTO primatyEntity, String conditionExpression, List<ActionDTO> thenActions, List<ActionDTO> elseActions) {
        super(type, primatyEntity);
        this.conditionExpression = conditionExpression;
        this.thenActions = thenActions;
        this.elseActions = elseActions;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public List<ActionDTO> getThenActions() {
        return thenActions;
    }

    public List<ActionDTO> getElseActions() {
        return elseActions;
    }
}