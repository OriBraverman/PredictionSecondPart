package world.factors.action.impl;

import world.factors.action.api.AbstractAction;
import world.factors.action.api.ActionType;

import world.factors.entity.definition.EntityDefinition;
import world.factors.property.definition.api.PropertyType;
import context.Context;
import world.factors.property.execution.PropertyInstance;

public class DecreaseAction extends AbstractAction {

    private final String property;
    private final String byExpression;

    public DecreaseAction(EntityDefinition entityDefinition, String property, String byExpression) {
        super(ActionType.DECREASE, entityDefinition);
        this.property = property;
        this.byExpression = byExpression;
    }

    public String getProperty() {
        return property;
    }

    public String getByExpression() {
        return byExpression;
    }

    @Override
    public void invoke(Context context) {
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(property);

        if (propertyInstance.getType() == PropertyType.DECIMAL) {
            /*forum question:
             * As part of calculation operations calculation \ increase \ decrease
             * what do we do in case the result of the operation is real and the PropertyType is an integer?
             * Answer:
             * In this case, of course, it is not possible to insert a real number into an integer, so it will be considered an error.
             * But if it was the other way around (for example an increase operation with a by of 3 for a property that is real)
             *  - of course it makes sense to allow since a real number can deal with an integer...*/
            Integer v = PropertyType.DECIMAL.convert(propertyInstance.getValue());
            // if the string byExpression is a real number (for example 3.5) then throw an exception
            if (byExpression.contains(".")) {
                throw new IllegalArgumentException("increase action of a real number can't operate on an integer property [" + property + "]");
            }
            propertyInstance.updateValue(v - Integer.parseInt(this.byExpression));
        }

        else if (propertyInstance.getType() == PropertyType.FLOAT) {
            Float v = PropertyType.FLOAT.convert(propertyInstance.getValue());
            propertyInstance.updateValue(v - Float.parseFloat(this.byExpression));
        }

        else {
            throw new IllegalArgumentException("increase action can't operate on a none number property [" + property + "]");
        }
    }
}
