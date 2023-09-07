package dtos;

import dtos.world.EntityDefinitionDTO;

import java.util.List;

public class NewExecutionInputDTO {
    List<EnvVariableDefinitionDTO> envVariables;
    List<EntityDefinitionDTO> entityDefinitions;

    public NewExecutionInputDTO(List<EnvVariableDefinitionDTO> envVariables, List<EntityDefinitionDTO> entityDefinitions) {
        this.envVariables = envVariables;
        this.entityDefinitions = entityDefinitions;
    }

    public List<EnvVariableDefinitionDTO> getEnvVariables() {
        return envVariables;
    }

    public List<EntityDefinitionDTO> getEntityDefinitions() {
        return entityDefinitions;
    }
}
