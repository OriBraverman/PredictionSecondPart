package world.factors.function.impl;

import context.Context;
import world.factors.expression.api.Expression;
import world.factors.function.api.AbstractFunction;
import world.factors.function.api.FunctionType;

import java.util.List;

public class TicksFunction extends AbstractFunction {
    public TicksFunction(List<Expression> expressions) {
        super(FunctionType.TICKS, expressions, 1);
    }

    @Override
    public Object execute(Context context) {
        return null;
    }

    @Override
    public boolean isNumericFunction(Object object) {
        return false;
    }
}
