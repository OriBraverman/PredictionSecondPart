package world.factors.action.api;


import context.Context;
import world.factors.entity.definition.EntityDefinition;

public interface Action {
    void invoke(Context context);
    ActionType getActionType();
    EntityDefinition getContextEntity();
}
