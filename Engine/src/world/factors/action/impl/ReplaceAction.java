package world.factors.action.impl;

import context.Context;
import world.factors.action.api.AbstractAction;
import world.factors.action.api.ActionType;
import world.factors.entity.definition.EntityDefinition;

public class ReplaceAction extends AbstractAction {

    public ReplaceAction(EntityDefinition entityDefinition) {
        super(ActionType.REPLACE, entityDefinition);
    }

    @Override
    public void invoke(Context context) {
        context.removeEntity(context.getPrimaryEntityInstance());
        context.addEntity(context.getSecondaryEntityInstance(), context.getGrid());
    }

    @Override
    public boolean isPropertyExistInEntity() {
        return false;
    }
}
