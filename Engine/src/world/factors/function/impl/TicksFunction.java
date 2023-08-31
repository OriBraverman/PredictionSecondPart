package world.factors.function.impl;

import context.Context;
import world.factors.expression.api.Expression;
import world.factors.expression.impl.PropertyNameExpression;
import world.factors.function.api.AbstractFunction;
import world.factors.function.api.FunctionType;

import java.util.List;

public class TicksFunction extends AbstractFunction {
    public TicksFunction(List<Expression> expressions) {
        super(FunctionType.TICKS, expressions, 1);
    }

    @Override
    public Object execute(Context context) {
        if (!(this.expressions.get(0) instanceof PropertyNameExpression)) {
            throw new IllegalArgumentException("evaluate function must have property name expression");
        }
        PropertyNameExpression propertyNameExpression = (PropertyNameExpression) this.expressions.get(0);
        return context.getNumberOfTicksPropertyHasentChanged(propertyNameExpression.getStringExpression());
    }

    @Override
    public boolean isNumericFunction(Object object) {
        return false;
    }
}
