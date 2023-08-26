package world.factors.action.api;


import world.factors.entity.definition.EntityDefinition;

import java.io.Serializable;

public abstract class AbstractAction implements Action, Serializable {

    protected final ActionType actionType;
    protected final EntityDefinition entityDefinition;

    protected AbstractAction(ActionType actionType, EntityDefinition entityDefinition) {
        this.actionType = actionType;
        this.entityDefinition = entityDefinition;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return entityDefinition;
    }
}
