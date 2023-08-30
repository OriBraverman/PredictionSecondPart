package dtos;

public class NewExecutionInputDTO {
    EnvVariableDefinitionDTO[] envVariables;
    EntityDefinitionDTO[] entityDefinitions;

    public NewExecutionInputDTO(EnvVariableDefinitionDTO[] envVariables, EntityDefinitionDTO[] entityDefinitions) {
        this.envVariables = envVariables;
        this.entityDefinitions = entityDefinitions;
    }

    public EnvVariableDefinitionDTO[] getEnvVariables() {
        return envVariables;
    }

    public EntityDefinitionDTO[] getEntityDefinitions() {
        return entityDefinitions;
    }
}
