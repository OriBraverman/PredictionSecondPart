package world.factors.expression.api;

import world.factors.entity.definition.EntityDefinition;
import world.factors.expression.impl.FreeValueExpression;
import world.factors.expression.impl.PropertyNameExpression;
import world.factors.expression.impl.UtilFunctionExpression;
import world.factors.function.api.FunctionType;

public abstract class AbstractExpression implements Expression {
    protected final ExpressionType expressionType;
    protected final String expression;

    protected AbstractExpression(String expression, ExpressionType expressionType) {
        this.expression = expression;
        this.expressionType = expressionType;
    }

    @Override
    public ExpressionType getExpressionType() {
        return expressionType;
    }

    @Override
    public String getStringExpression() {
        return expression;
    }
    public static Expression getExpressionByString(String expression, EntityDefinition entityDefinition) {
        if (isFunctionExpression(expression)) {
            return new UtilFunctionExpression(expression);
        } else if (entityDefinition != null && entityDefinition.getPropertyDefinitionByName(expression) != null) {
            // first type of PropertyNameExpression
            return new PropertyNameExpression(expression);
        }
        else if (entityDefinition != null && expression.contains(".")
                && entityDefinition.getName().equals(getEntityName(expression))
                && entityDefinition.getPropertyDefinitionByName(getPropertyName(expression)) != null) {
            // second type of PropertyNameExpression
            return new PropertyNameExpression(getEntityName(expression), getPropertyName(expression));
        }
        else {
            return new FreeValueExpression(expression);
        }
    }// todo: add envVariableExpression
    // todo: add
    public static boolean isFunctionExpression(String expression) {
        if (expression.charAt(expression.length() - 1) != ')') {
            return false;
        }
        int firstOpenParenthesis = expression.indexOf('(');
        if (firstOpenParenthesis == -1) {
            return false;
        }
        return true;
    }

    @Override
    public FunctionType getFunctionTypeByExpression(String expression) {
        String functionName = expression.substring(0, expression.indexOf("("));
        return FunctionType.getFunctionType(functionName);
    }

    private static String getEntityName(String expression) {
        return expression.substring(0, expression.indexOf("."));
    }

    private static String getPropertyName(String expression) {
        return expression.substring(expression.indexOf(".") + 1);
    }
}