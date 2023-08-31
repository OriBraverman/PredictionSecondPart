package world.factors.action.api;


import world.World;
import world.factors.entity.definition.EntityDefinition;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractAction implements Action, Serializable {

    protected final ActionType actionType;
    protected final EntityDefinition sourceEntityDefinition;

    protected AbstractAction(ActionType actionType, EntityDefinition sourceEntityDefinition) {
        this.actionType = actionType;
        this.sourceEntityDefinition = sourceEntityDefinition;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return sourceEntityDefinition;
    }

    @Override
    public boolean isEntityExistInWorld(List<EntityDefinition> entities) {
        return entities.contains(sourceEntityDefinition);
    }
}
