package dtos;

public class SimulationDetailsDTO {
    private EntityDefinitionDTO[] entities;
    private RuleDTO[] rules;
    private EnvVariableDefinitionDTO[] envVariables;
    private TerminationDTO termination;

    public SimulationDetailsDTO(EntityDefinitionDTO[] entities, RuleDTO[] rules, EnvVariableDefinitionDTO[] envVariables, TerminationDTO termination) {
        this.entities = entities;
        this.rules = rules;
        this.envVariables = envVariables;
        this.termination = termination;
    }

    public EntityDefinitionDTO[] getEntities() {
        return entities;
    }

    public RuleDTO[] getRules() {
        return rules;
    }

    public TerminationDTO getTermination() {
        return termination;
    }

    public EnvVariableDefinitionDTO[] getEnvVariables() {
        return envVariables;
    }
}
