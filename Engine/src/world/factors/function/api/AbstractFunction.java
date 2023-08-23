package world.factors.function.api;

import world.factors.expression.api.AbstractExpression;
import world.factors.expression.api.Expression;

import java.util.List;

public abstract class AbstractFunction implements Function {
    protected final FunctionType functionType;
    protected final List<Expression> expressions;
    protected final int numArgs;

    public AbstractFunction(FunctionType functionType, List<Expression> expressions, int numArgs) {
        this.functionType = functionType;
        this.expressions = expressions;
        this.numArgs = numArgs;
    }

    @Override
    public FunctionType getFunctionType() {
        return this.functionType;
    }

    @Override
    public int getNumArguments() {
        return this.numArgs;
    }
}
