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
    String propertyName;
    public PropertyNameExpression(String expression, String entityName, String propertyName) {
        super(expression, ExpressionType.PROPERTY_NAME);
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    @Override
    public PropertyDefinition evaluate(Context context) {
        if (!context.getPrimaryEntityInstance().getEntityDefinition().getName().equals(entityName)) {
            throw new IllegalArgumentException("entity [" + entityName + "] is not the primary entity");
        }
        EntityInstance entityInstance = context.getPrimaryEntityInstance();
        if (entityInstance.getPropertyByName(propertyName) != null) {
            return entityInstance.getPropertyByName(propertyName).getPropertyDefinition();
        }
        throw new IllegalArgumentException("property [" + propertyName + "] is not exist");
    }

    @Override
    public boolean isNumericExpression(List<EntityDefinition> entityDefinitions, EnvVariableManagerImpl envVariableManagerImpl) {
        // check if the property type of the property of the entity is numeric
        for (EntityDefinition entityDefinition : entityDefinitions) {
            PropertyDefinition entityPropertyDefinition = entityDefinition.getPropertyDefinitionByName(propertyName);
            if (entityPropertyDefinition.getType() == PropertyType.FLOAT || entityPropertyDefinition.getType() == PropertyType.DECIMAL) {
                return true;
            }
        }
        return false;
    }
}
