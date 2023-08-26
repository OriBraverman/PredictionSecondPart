package world;

import engine.Serialization;
import world.factors.entity.definition.EntityDefinition;
import world.factors.environment.definition.api.EnvVariablesManager;
import world.factors.rule.Rule;
import world.factors.termination.Termination;

import java.io.Serializable;
import java.util.List;

public class World implements Serializable {
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

    public EntityDefinition getEntityByName(String name) {
        for (EntityDefinition entity : entities) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Termination getTermination() {
        return termination;
    }
}
