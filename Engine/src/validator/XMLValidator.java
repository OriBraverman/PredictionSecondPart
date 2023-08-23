package validator;

import context.Context;
import resources.schema.generatedWorld.*;
import world.World;
import world.factors.action.api.Action;
import world.factors.action.api.ActionType;
import world.factors.action.impl.CalculationAction;
import world.factors.action.impl.DecreaseAction;
import world.factors.action.impl.IncreaseAction;
import world.factors.entity.definition.EntityDefinition;
import world.factors.environment.definition.api.EnvVariablesManager;
import world.factors.environment.definition.impl.EnvVariableManagerImpl;
import world.factors.expression.api.Expression;
import world.factors.rule.Rule;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

import static world.factors.expression.api.AbstractExpression.getExpressionByString;

public class XMLValidator {
    public static void validateFileExists(Path file) throws FileNotFoundException {
        if(!file.toFile().exists()) {
            throw new FileNotFoundException(file.getFileName().toString());
        }
    }

    public static void validateFileIsXML(Path file) {
        if(!file.getFileName().toString().endsWith(".xml")) {
            throw new IllegalArgumentException("File is not XML");
        }
    }

    public static void validateXMLContent(PRDWorld world) {
        validateEnvironmentPropertyUniqueness(world);
        validateAllEntitiesPropertyUniqueness(world);
        validateAllActionsReferToExistingEntities(world);
        validateAllActionsReferToExistingEntityProperties(world);
    }

    //2
    public static void validateEnvironmentPropertyUniqueness(PRDWorld world) {
        PRDEvironment environment = world.getPRDEvironment();
        for(int i=0; i<environment.getPRDEnvProperty().size(); i++) {
            String currName = environment.getPRDEnvProperty().get(i).getPRDName();
            for(int j=i+1; j<environment.getPRDEnvProperty().size(); j++) {
                String otherName = environment.getPRDEnvProperty().get(j).getPRDName();
                if(currName.equals(otherName)) {
                    throw new IllegalArgumentException("Environment name is not unique");
                }
            }
        }
    }

    public static void validateEntityNameUniqueness(PRDWorld world) {
        PRDEntities entities = world.getPRDEntities();
        for(int i=0; i<entities.getPRDEntity().size(); i++) {
            String currName = entities.getPRDEntity().get(i).getName();
            for(int j=i+1; j<entities.getPRDEntity().size(); j++) {
                String otherName = entities.getPRDEntity().get(j).getName();
                if(currName.equals(otherName)) {
                    throw new IllegalArgumentException("Entity name is not unique");
                }
            }
        }
    }

    //3

    public static void validateAllEntitiesPropertyUniqueness(PRDWorld world) {
        PRDEntities entities = world.getPRDEntities();
        for(int i=0; i<entities.getPRDEntity().size(); i++) {
            validateEntityPropertyUniqueness(entities.getPRDEntity().get(i));
        }
    }

    private static void validateEntityPropertyUniqueness(PRDEntity entity) {
        PRDProperties properties = entity.getPRDProperties();
        for(int i=0; i<properties.getPRDProperty().size(); i++) {
            String currName = properties.getPRDProperty().get(i).getPRDName();
            for(int j=i+1; j<properties.getPRDProperty().size(); j++) {
                String otherName = properties.getPRDProperty().get(j).getPRDName();
                if(currName.equals(otherName)) {
                    throw new IllegalArgumentException("Entity property name is not unique");
                }
            }
        }
    }
    //4
    public static void validateAllActionsReferToExistingEntities(PRDWorld world) {
        for(PRDRule rule: world.getPRDRules().getPRDRule()) {
            for(PRDAction action: rule.getPRDActions().getPRDAction()) {
                if(!isEntityExist(world, action.getEntity())) {
                    throw new IllegalArgumentException("Action refers to non-existing entity");
                }
            }
        }
    }

    private static boolean isEntityExist(PRDWorld world, String entityName) {
        PRDEntities entities = world.getPRDEntities();
        for(PRDEntity entity: entities.getPRDEntity()) {
            if(entity.getName().equals(entityName)) {
                return true;
            }
        }
        return false;
    }

    //5
    public static void validateAllActionsReferToExistingEntityProperties(PRDWorld world) {
        for(PRDRule rule: world.getPRDRules().getPRDRule()) {
            for(PRDAction action: rule.getPRDActions().getPRDAction()) {
                if(action.getProperty() !=null && !isEntityPropertyExist(world, action.getEntity(), action.getProperty())) {
                    throw new IllegalArgumentException("Action refers to non-existing entity");
                } else if (action.getResultProp() != null && !isEntityPropertyExist(world, action.getEntity(), action.getResultProp())) {
                    throw new IllegalArgumentException("Action refers to non-existing entity");
                } else {
                    continue;
                }
            }
        }
    }

    private static boolean isEntityPropertyExist(PRDWorld world, String entityName, String propertyName) {
        PRDEntities entities = world.getPRDEntities();
        for(PRDEntity entity: entities.getPRDEntity()) {
            if(entity.getName().equals(entityName)) {
                for(PRDProperty property: entity.getPRDProperties().getPRDProperty()) {
                    if(property.getPRDName().equals(propertyName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //6
    public static void validateMathActionHasNumericArgs(List<Rule> rules, List<EntityDefinition> entities, EnvVariableManagerImpl envVariableManagerImpl) {
        for (Rule rule : rules) {
            for (Action action : rule.getActionsToPerform()) {
                ActionType actionType = action.getActionType();
                switch (actionType) {
                    case INCREASE:
                        IncreaseAction increaseAction = (IncreaseAction) action;
                        String byExpression = increaseAction.getByExpression();
                        Expression expression = getExpressionByString(byExpression, entities.get(0));
                        if (!(expression.isNumericExpression(entities, envVariableManagerImpl))) {
                            throw new IllegalArgumentException("Increase action has non-numeric argument");
                        }
                        break;
                    case DECREASE:
                        DecreaseAction decreaseAction = (DecreaseAction) action;
                        String byExpression2 = decreaseAction.getByExpression();
                        Expression expression2 = getExpressionByString(byExpression2, entities.get(0));
                        if (!(expression2.isNumericExpression(entities, envVariableManagerImpl))) {
                            throw new IllegalArgumentException("Decrease action has non-numeric argument");
                        }
                        break;
                    case CALCULATION:
                        CalculationAction calculationAction = (CalculationAction) action;
                        String argument1 = calculationAction.getArgument1();
                        String argument2 = calculationAction.getArgument2();
                        Expression arg1Expression = getExpressionByString(argument1, entities.get(0));
                        Expression arg2Expression = getExpressionByString(argument2, entities.get(0));
                        if (!(arg1Expression.isNumericExpression(entities, envVariableManagerImpl)) || !(arg2Expression.isNumericExpression(entities, envVariableManagerImpl))) {
                            throw new IllegalArgumentException("Calculation action has non-numeric argument");
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }


}
