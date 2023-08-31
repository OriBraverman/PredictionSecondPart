package world.factors.function.impl;

import context.Context;
import world.factors.entity.execution.EntityInstance;
import world.factors.expression.api.Expression;
import world.factors.expression.impl.PropertyNameExpression;
import world.factors.function.api.AbstractFunction;
import world.factors.function.api.FunctionType;
import world.factors.property.execution.PropertyInstance;

import java.util.List;

public class EvaluateFunction extends AbstractFunction {
    public EvaluateFunction(List<Expression> expressions) {
        super(FunctionType.EVALUATE, expressions, 1);
    }

    @Override
    public Object execute(Context context) {
        Expression propertyNameExpression = this.expressions.get(0);
        if (!(propertyNameExpression instanceof PropertyNameExpression)) {
            throw new IllegalArgumentException("evaluate function must have property name expression");
        }
        return propertyNameExpression.evaluate(context);
    }

    @Override
    public boolean isNumericFunction(Object object) {
        return false;
    }


}
