package world.factors.expression.impl;

import context.Context;
import world.factors.entity.definition.EntityDefinition;
import world.factors.environment.definition.impl.EnvVariableManagerImpl;
import world.factors.expression.api.AbstractExpression;
import world.factors.expression.api.Expression;
import world.factors.expression.api.ExpressionType;
import world.factors.function.api.Function;
import world.factors.function.api.FunctionType;
import world.factors.function.impl.EnvironmentFunction;
import world.factors.function.impl.RandomFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilFunctionExpression extends AbstractExpression {
    public UtilFunctionExpression(String expression) {
        super(expression, ExpressionType.UTIL_FUNCTION);
    }

    @Override
    public Function evaluate(Context context) {
        // this function receives only function expression
        // the function expression structure is: functionName(arg1,arg2,arg3,...)
        // so we need to extract the function name and the arguments
        List<String> elements = splitExpressionString(this.expression);
        List<Expression> args = new ArrayList<>();
        for (int i = 1; i < elements.size(); i++) {
            args.add(getExpressionByString(elements.get(i), context.getPrimaryEntityInstance().getEntityDefinition()));
        }
        switch(FunctionType.getFunctionType(elements.get(0))) {
            case ENVIRONMENT:
                if (elements.size() != 2) {
                    throw new IllegalArgumentException("environment function must have only one argument");
                }
                return new EnvironmentFunction(args);
            case RANDOM:
                if (elements.size() != 2) {
                    throw new IllegalArgumentException("random function must have only one argument");
                }
                return new RandomFunction(args);
            default:
                throw new IllegalArgumentException("function [" + elements.get(0) + "] is not exist");
        }
    }

    @Override
    public boolean isNumericExpression(List<EntityDefinition> entityDefinitions, EnvVariableManagerImpl envVariableManagerImpl) {
        Function function = getFunctionByExpression(expression, entityDefinitions.get(0));
        return function.isNumericFunction(envVariableManagerImpl);
    }
    private Function getFunctionByExpression(String functionExpression, EntityDefinition entityDefinition) {
        // this function receives only function expression
        // the function expression structure is: functionName(arg1,arg2,arg3,...)
        // so we need to extract the function name and the arguments
        List<String> elements = splitExpressionString(functionExpression);
        List<Expression> args = new ArrayList<>();
        for (int i = 1; i < elements.size(); i++) {
            args.add(getExpressionByString(elements.get(i), entityDefinition));
        }
        switch(FunctionType.getFunctionType(elements.get(0))) {
            case ENVIRONMENT:
                if (elements.size() != 2) {
                    throw new IllegalArgumentException("environment function must have only one argument");
                }
                return new EnvironmentFunction(args);
            case RANDOM:
                if (elements.size() != 2) {
                    throw new IllegalArgumentException("random function must have only one argument");
                }
                return new RandomFunction(args);
            default:
                throw new IllegalArgumentException("function [" + elements.get(0) + "] is not exist");
        }
    }
    private static List<String> splitExpressionString(String expression) {
        // this function receives only function expression
        // the function expression structure is: functionName(arg1,arg2,arg3,...)
        // the return list will be: [functionName, arg1, arg2, arg3, ...]
        List<String> elements = new ArrayList<>();
        Pattern pattern = Pattern.compile("([a-zA-Z]+)\\((.*)\\)");
        Matcher matcher = pattern.matcher(expression);
        if (matcher.find()) {
            elements.add(matcher.group(1));
            String[] args = matcher.group(2).split(",");
            for (String arg : args) {
                elements.add(arg);
            }
        }
        return elements;
    }
}
