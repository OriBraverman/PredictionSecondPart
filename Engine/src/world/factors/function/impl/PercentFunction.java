package world.factors.function.impl;

import context.Context;
import world.factors.expression.api.Expression;
import world.factors.function.api.AbstractFunction;
import world.factors.function.api.FunctionType;

import java.util.List;

public class PercentFunction extends AbstractFunction {
    public PercentFunction(List<Expression> expressions) {
        super(FunctionType.PERCENT, expressions, 2);
    }

    @Override
    public Object execute(Context context) {
        if (!(expressions.get(0).evaluate(context) instanceof Float) || !(expressions.get(1).evaluate(context) instanceof Float)) {
            throw new IllegalArgumentException("percent function arguments must be numeric");
        }
        float num1 = (float) expressions.get(0).evaluate(context);
        float num2 = (float) expressions.get(1).evaluate(context);
        if (num2 == 0) {
            throw new IllegalArgumentException("percent function second argument must be non-zero");
        }
        return num1 / num2;
    }

    @Override
    public boolean isNumericFunction(Object object) {
        return true;
    }
}
