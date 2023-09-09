package world.factors.function.impl;

import context.Context;
import world.factors.function.api.AbstractFunction;
import world.factors.function.api.FunctionType;
import world.factors.functionArgument.api.FunctionArgument;

import java.util.List;

public class PercentFunction extends AbstractFunction {
    public PercentFunction(List<FunctionArgument> functionArguments) {
        super(FunctionType.PERCENT, functionArguments, 2);
    }

    @Override
    public Object execute(Context context) {
        if (!(functionArguments.get(0).evaluate(context) instanceof Float) || !(functionArguments.get(1).evaluate(context) instanceof Float)) {
            throw new IllegalArgumentException("percent function: arguments must be numeric");
        }
        float num1 = (float) functionArguments.get(0).evaluate(context);
        float num2 = (float) functionArguments.get(1).evaluate(context);
        if (num2 == 0) {
            throw new IllegalArgumentException("percent function: second argument must be non-zero");
        }
        return num1 / num2;
    }

    @Override
    public boolean isNumericFunction(Object object) {
        return true;
    }
}
