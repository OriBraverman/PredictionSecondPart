package dtos;

public class SimulationDetailsDTO {
    private EntityDefinitionDTO[] entities;
    private RuleDTO[] rules;
    private TerminationDTO termination;

    public SimulationDetailsDTO(EntityDefinitionDTO[] entities, RuleDTO[] rules, TerminationDTO termination) {
        this.entities = entities;
        this.rules = rules;
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
}
