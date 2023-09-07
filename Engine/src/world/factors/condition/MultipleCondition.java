package world.factors.condition;

import context.Context;

import java.io.Serializable;
import java.util.List;

public class MultipleCondition implements Condition, Serializable {
    private final LogicalType logical;
    private final List<Condition> conditions;

    public MultipleCondition(LogicalType logical, List<Condition> conditions) {
        this.logical = logical;
        this.conditions = conditions;
    }
    @Override
    public boolean assertCondition(Context context) {
        switch (this.logical){
            case AND:
                for (Condition condition : conditions) {
                    if (!condition.assertCondition(context))
                        return false;
                }
                return true;
            case OR:
                for (Condition condition : conditions) {
                    if (condition.assertCondition(context))
                        return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public boolean isPropertyExistInEntity() {
        return true;
    }

    @Override
    public String toString() {
        return "MultipleCondition{" +
                "logical=" + logical +
                ", conditions=" + conditions +
                '}';
    }
}
