package world.factors.condition;

import context.Context;
import world.factors.entity.definition.EntityDefinition;
import world.factors.expression.api.AbstractExpression;
import world.factors.expression.api.Expression;
import world.factors.property.definition.api.PropertyDefinition;

import java.io.Serializable;

public class SingleCondition implements Condition, Serializable {
    private final EntityDefinition entityDefinition;
    private final PropertyDefinition propertyDefinition;
    private final OperatorType operator;
    private final String value;

    public SingleCondition(EntityDefinition entityDefinition, PropertyDefinition propertyDefinition, OperatorType operator, String value)
    {
        this.entityDefinition = entityDefinition;
        this.propertyDefinition = propertyDefinition;
        this.operator = operator;
        this.value = value;
    }

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    public String getValue() {
        return value;
    }

    public OperatorType getOperator() {
        return operator;
    }

    @Override
    public boolean assertCondition(Context context) {
        Object propertyValue = context.getPropertyInstanceByPropertyDefinition(this.propertyDefinition).getValue();
        Expression expression = AbstractExpression.getExpressionByString(this.value, this.entityDefinition);
        Object value = context.getValueByExpression(expression);
        // check if the property value is the same type as the value
        if (propertyValue.getClass() != value.getClass())
            return false;
        switch (this.operator){
            case EQUALS:
                return propertyValue.equals(value);
            case NOT_EQUALS:
                return !propertyValue.equals(value);
            case BIGGER_THAN:
                if (propertyValue instanceof Number && value instanceof Number)
                    return ((Number) propertyValue).doubleValue() > ((Number) value).doubleValue();
                break;
            case LOWER_THAN:
                if (propertyValue instanceof Number && value instanceof Number)
                    return ((Number) propertyValue).doubleValue() < ((Number) value).doubleValue();
                break;
        }
        return false;
    }

    @Override
    public boolean isPropertyExistInEntity() {
        if (entityDefinition.getPropertyDefinitionByName(propertyDefinition.getName()) == null)
            return false;
        return true;
    }
}
