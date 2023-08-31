package world.factors.expression.impl;

import context.Context;
import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.environment.definition.impl.EnvVariableManagerImpl;
import world.factors.expression.api.AbstractExpression;
import world.factors.expression.api.ExpressionType;
import world.factors.property.definition.api.PropertyDefinition;
import world.factors.property.definition.api.PropertyType;

import java.util.List;

public class PropertyNameExpression extends AbstractExpression {
    String entityName;
    public PropertyNameExpression(String expression) {
        this(expression, null);
    }
    public PropertyNameExpression(String propertyName, String entityName) {
        super(propertyName, ExpressionType.PROPERTY_NAME);
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public PropertyDefinition evaluate(Context context) {
        if (entityName != null && !context.getPrimaryEntityInstance().getEntityDefinition().getName().equals(entityName)) {
            throw new IllegalArgumentException("entity [" + entityName + "] is not the primary entity");
        }
        EntityInstance entityInstance = context.getPrimaryEntityInstance();
        if (entityInstance.getPropertyByName(expression) != null) {
            return entityInstance.getPropertyByName(expression).getPropertyDefinition();
        }
        throw new IllegalArgumentException("property [" + expression + "] is not exist");
    }

    @Override
    public boolean isNumericExpression(List<EntityDefinition> entityDefinitions, EnvVariableManagerImpl envVariableManagerImpl) {
        // check if the property type of the property of the entity is numeric
        for (EntityDefinition entityDefinition : entityDefinitions) {
            PropertyDefinition entityPropertyDefinition = entityDefinition.getPropertyDefinitionByName(expression);
            if (entityPropertyDefinition.getType() == PropertyType.FLOAT || entityPropertyDefinition.getType() == PropertyType.DECIMAL) {
                return true;
            }
        }
        return false;
    }
}
