package world.factors.action.impl;

import context.Context;
import world.factors.action.api.AbstractAction;
import world.factors.action.api.Action;
import world.factors.action.api.ActionType;
import world.factors.entity.definition.EntityDefinition;
import world.factors.expression.api.Expression;

import java.util.List;

public class ProximityAction extends AbstractAction {
    private final EntityDefinition targetEntityDefinition;
    private final Expression of;
    private List<Action> actions;

    public ProximityAction(EntityDefinition sourceEntityDefinition, EntityDefinition targetEntityDefinition, Expression of, List<Action> actions){
        super(ActionType.PROXIMITY, sourceEntityDefinition);
        this.targetEntityDefinition = targetEntityDefinition;
        this.of = of;
        this.actions = actions;
    }

    @Override
    public void invoke(Context context) {
    }

    @Override
    public boolean isPropertyExistInEntity() {
        return false;
    }
}
