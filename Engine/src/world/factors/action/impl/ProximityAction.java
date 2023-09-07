package world.factors.action.impl;

import context.Context;
import world.factors.action.api.AbstractAction;
import world.factors.action.api.Action;
import world.factors.action.api.ActionType;
import world.factors.entity.definition.EntityDefinition;
import world.factors.expression.api.Expression;
import world.factors.grid.Coordinate;

import java.util.Collection;
import java.util.List;

public class ProximityAction extends AbstractAction {
    private final EntityDefinition targetEntityDefinition;
    private final Expression of;
    private List<AbstractAction> thenActions;

    public ProximityAction(EntityDefinition sourceEntityDefinition, EntityDefinition targetEntityDefinition, Expression of, List<AbstractAction> thenActions){
        super(ActionType.PROXIMITY, sourceEntityDefinition);
        this.targetEntityDefinition = targetEntityDefinition;
        this.of = of;
        this.thenActions = thenActions;
    }

    @Override
    public void invoke(Context context) {

        if (shouldActivateActions(context)) {

            for (AbstractAction thenAction : thenActions) {
                thenAction.invoke(context);
            }
        }
    }

    private boolean shouldActivateActions(Context context){
        boolean matchesDefintions = context.getPrimaryEntityInstance().getEntityDefinition().getName().equals(sourceEntityDefinition.getName())
                && context.getSecondaryEntityInstance().getEntityDefinition().getName().equals(targetEntityDefinition.getName());
        int rank = (int)context.getValueByExpression(this.of);
        Collection<Coordinate> envCells = context.getGrid().findEnvironmentCells(context.getPrimaryEntityInstance().getCoordinate(), rank);
        boolean areClose = envCells.contains(context.getSecondaryEntityInstance().getCoordinate());

        return matchesDefintions && areClose;
    }

    @Override
    public boolean isPropertyExistInEntity() {
        return true;
    }

    @Override
    public boolean isEntityExistInWorld(List<EntityDefinition> entities) {
        return super.isEntityExistInWorld(entities) && entities.contains(targetEntityDefinition);
    }

    public EntityDefinition getTargetEntityDefinition() {
        return targetEntityDefinition;
    }

    public String getStringOf() {
        return of.toString();
    }

    public List<AbstractAction> getThenActions() {
        return thenActions;
    }
}
