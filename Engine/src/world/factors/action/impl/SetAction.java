package world.factors.action.impl;

import context.Context;
import world.factors.action.api.AbstractAction;
import world.factors.action.api.ActionType;
import world.factors.entity.definition.EntityDefinition;
import world.factors.expression.api.Expression;

public class SetAction extends AbstractAction {
    private final String property;
    private final String value;

    public SetAction(EntityDefinition entityDefinition, String property, String value) {
        super(ActionType.SET, entityDefinition);
        this.property = property;
        this.value = value;
    }

    @Override
    public void invoke(Context context) {
        context.setPropertyValue(this.sourceEntityDefinition.getName(), this.property, this.value);
    }

    @Override
    public boolean isPropertyExistInEntity() {
        return sourceEntityDefinition.getPropertyDefinitionByName(property) != null;
    }
}
