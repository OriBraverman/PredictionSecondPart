package world;

import world.factors.entity.definition.EntityDefinition;
import world.factors.environment.definition.api.EnvVariablesManager;
import world.factors.rule.Rule;
import world.factors.termination.Termination;

import java.util.List;

public class World {
    private EnvVariablesManager environment;
    private List<EntityDefinition> entities;
    private List<Rule> rules;
    private Termination termination;

    public World(EnvVariablesManager environment, List<EntityDefinition> entities, List<Rule> rules, Termination termination) {
        this.environment = environment;
        this.entities = entities;
        this.rules = rules;
        this.termination = termination;
    }

    public EnvVariablesManager getEnvironment() {
        return environment;
    }

    public List<EntityDefinition> getEntities() {
        return entities;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Termination getTermination() {
        return termination;
    }
}
