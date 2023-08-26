package world.factors.action.api;


import context.Context;
import world.World;
import world.factors.entity.definition.EntityDefinition;

public interface Action {
    void invoke(Context context);
    ActionType getActionType();
    EntityDefinition getContextEntity();
    boolean isPropertyExistInEntity();
}
