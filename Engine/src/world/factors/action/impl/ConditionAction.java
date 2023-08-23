package world.factors.action.impl;

import context.Context;
import world.factors.action.api.AbstractAction;
import world.factors.action.api.ActionType;
import world.factors.condition.Condition;
import world.factors.entity.definition.EntityDefinition;

import java.util.List;

public class ConditionAction extends AbstractAction {
    private Condition condition;
    private List<AbstractAction> thenActions;
    private List<AbstractAction> elseActions;

    public ConditionAction(EntityDefinition entityDefinition, Condition condition, List<AbstractAction> thenActions, List<AbstractAction> elseActions) {
        super(ActionType.CONDITION, entityDefinition);
        this.condition = condition;
        this.thenActions = thenActions;
        this.elseActions = elseActions;
    }

    @Override
    public void invoke(Context context) {
        if (condition.assertCondition(context)) {
            for (AbstractAction thenAction : thenActions) {
                thenAction.invoke(context);
            }
        } else {
            for (AbstractAction elseAction : elseActions) {
                elseAction.invoke(context);
            }
        }
    }
}
