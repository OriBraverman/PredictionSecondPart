package world.factors.function.impl;

import context.Context;
import world.factors.environment.definition.api.EnvVariablesManager;
import world.factors.expression.api.Expression;
import world.factors.function.api.AbstractFunction;
import world.factors.function.api.FunctionType;

import java.util.ArrayList;
import java.util.List;

public class RandomFunction extends AbstractFunction {
    public RandomFunction(List<Expression> args) {
        super(FunctionType.RANDOM, args, 1);
    }

    @Override
    public Object execute(Context context) {
        //validate that the argument is an Integer
        //the function will return a random number between 0 and the argument (inclusive)
        if (!(this.expressions.get(0).evaluate(context) instanceof Integer)) {
            throw new IllegalArgumentException("argument must be a Integer");
        }
        return (int) (Math.random() * ((Integer) this.expressions.get(0).evaluate(context) + 1));
    }

    @Override
    public boolean isNumericFunction(Object object) {
        return true;
    }
}