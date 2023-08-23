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
        }
        else if (entityDefinition.getPropertyDefinitionByName(expression) != null) {
            return new PropertyNameExpression(expression);
        }
        else {
            return new FreeValueExpression(expression);
        }
    }
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
}