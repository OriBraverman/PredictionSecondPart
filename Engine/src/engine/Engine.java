package engine;

import convertor.Convertor;
import dtos.*;
import resources.schema.generatedWorld.PRDWorld;

import static java.util.Arrays.stream;
import static validator.XMLValidator.*;

import simulation.Simulation;
import simulation.SimulationManager;
import world.World;
import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.environment.definition.impl.EnvVariableManagerImpl;
import world.factors.environment.execution.api.ActiveEnvironment;
import world.factors.property.definition.api.NumericPropertyDefinition;
import world.factors.property.definition.api.PropertyDefinition;
import world.factors.property.execution.PropertyInstance;
import world.factors.property.execution.PropertyInstanceImpl;
import world.factors.rule.Rule;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Engine implements Serializable {
    private World world;
    private SimulationManager simulationManager;
    private ActiveEnvironment activeEnvironment;

    public Engine() {
        this.world = null;
        this.simulationManager = new SimulationManager();
        this.activeEnvironment = null;
    }

    private static PRDWorld fromXmlFileToObject(Path path) {
        try {
            File file = new File(path.toString());
            JAXBContext jaxbContext = JAXBContext.newInstance(PRDWorld.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PRDWorld generatedWorld = (PRDWorld) jaxbUnmarshaller.unmarshal(file);
            return generatedWorld;

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void loadXML(Path xmlPath) throws FileNotFoundException {
        validateFileExists(xmlPath);
        validateFileIsXML(xmlPath);
        PRDWorld generatedWorld = fromXmlFileToObject(xmlPath);
        validateXMLContent(generatedWorld);
        Convertor convertor = new Convertor();
        convertor.setGeneratedWorld(generatedWorld);
        World tempWorld = convertor.convertPRDWorldToWorld();
        validateAllActionsReferToExistingEntityProperties(tempWorld);
        validateMathActionHasNumericArgs(tempWorld.getRules(), tempWorld.getEntities(), (EnvVariableManagerImpl) tempWorld.getEnvironment());
        // if loaded successfully, clear the old engine and set the new one
        this.world = tempWorld;
        this.simulationManager = new SimulationManager();
        this.activeEnvironment = null;
    }

    public EnvVariablesValuesDTO updateActiveEnvironmentAndInformUser(EnvVariablesValuesDTO envVariablesValuesDTO) {
        ActiveEnvironment activeEnvironment = this.world.getEnvironment().createActiveEnvironment();
        for (int i = 0; i < envVariablesValuesDTO.getEnvVariablesValues().length; i++) {
            EnvVariableValueDTO envVariableValueDTO = envVariablesValuesDTO.getEnvVariablesValues()[i];
            Object value = envVariableValueDTO.getValue();
            if (value.equals("")) {
                value = this.world.getEnvironment().getPropertyDefinitionByName(envVariableValueDTO.getName()).generateValue();
            }
            PropertyInstance propertyInstance = new PropertyInstanceImpl(this.world.getEnvironment().getPropertyDefinitionByName(envVariableValueDTO.getName()), value);
            activeEnvironment.addPropertyInstance(propertyInstance);
            envVariablesValuesDTO.getEnvVariablesValues()[i] = new EnvVariableValueDTO(envVariableValueDTO.getName(), value.toString(), true);
        }
        this.activeEnvironment = activeEnvironment;
        return envVariablesValuesDTO;
    }
    public SimulationResultDTO activateSimulation() {
        Simulation simulation = this.simulationManager.createSimulation(this.world, this.activeEnvironment);
        simulation.run();
        return new SimulationResultDTO(simulation.getId(), simulation.isTerminatedBySecondsCount(), simulation.isTerminatedByTicksCount());
    }

    public SimulationDetailsDTO getSimulationDetailsDTO() {
        EntityDefinitionDTO[] entityDefinitionDTOS = getEntitiesDTO();
        RuleDTO[] ruleDTOS = getRulesDTO();
        TerminationDTO terminationDTO = getTerminationDTO();
        return new SimulationDetailsDTO(entityDefinitionDTOS, ruleDTOS, terminationDTO);
    }

    private TerminationDTO getTerminationDTO() {
        return new TerminationDTO(this.world.getTermination().getTicksCount(), this.world.getTermination().getSecondsCount());
    }

    private RuleDTO[] getRulesDTO() {
        List<Rule> rules = this.world.getRules();
        RuleDTO[] ruleDTOS = new RuleDTO[rules.size()];
        for (int i = 0; i < rules.size(); i++) {
            ruleDTOS[i] = getRuleDTO(rules.get(i));
        }
        return ruleDTOS;
    }

    private RuleDTO getRuleDTO(Rule rule) {
        String name = rule.getName();
        ActivationDTO activationDTO = new ActivationDTO(rule.getActivation().getTicks(), rule.getActivation().getProbabilty());
        int numberOfActions = rule.getActionsToPerform().size();
        String[] actionNames = rule.getActionsToPerform()
                .stream()
                .map(action -> action.getActionType().toString())
                .toArray(String[]::new);
        return new RuleDTO(name, activationDTO, numberOfActions, actionNames);
    }

    private EntityDefinitionDTO[] getEntitiesDTO() {
        List<EntityDefinition> entities = this.world.getEntities();
        EntityDefinitionDTO[] entityDefinitionDTOS = new EntityDefinitionDTO[entities.size()];
        for (int i = 0; i < entities.size(); i++) {
            entityDefinitionDTOS[i] = getEntityDTO(entities.get(i));
        }
        return entityDefinitionDTOS;
    }

    private EntityDefinitionDTO getEntityDTO(EntityDefinition entityDefinition) {
        String name = entityDefinition.getName();
        int population = entityDefinition.getPopulation();
        EntityPropertyDefinitionDTO[] entityPropertyDefinitionDTOS = getEntityPropertyDefinitionDTOS(entityDefinition.getProps());
        return new EntityDefinitionDTO(name, population, entityPropertyDefinitionDTOS);
    }

    private EntityPropertyDefinitionDTO[] getEntityPropertyDefinitionDTOS(List<PropertyDefinition> properties) {
        EntityPropertyDefinitionDTO[] entityPropertyDefinitionDTOS = new EntityPropertyDefinitionDTO[properties.size()];
        for (int i = 0; i < properties.size(); i++) {
            entityPropertyDefinitionDTOS[i] = getEntityPropertyDefinitionDTO(properties.get(i));
        }
        return entityPropertyDefinitionDTOS;
    }

    private EntityPropertyDefinitionDTO getEntityPropertyDefinitionDTO(PropertyDefinition property) {
        String name = property.getName();
        String type = property.getType().toString();
        String valueGenerated = property.generateValue().toString();
        if (property.isNumeric()) {
            NumericPropertyDefinition property1 = (NumericPropertyDefinition) property;
            String fromRange = property1.getRange().getFrom().toString();
            String toRange = property1.getRange().getTo().toString();
            return new EntityPropertyDefinitionDTO(name, type, toRange, fromRange, valueGenerated);
        }
        return new EntityPropertyDefinitionDTO(name, type, valueGenerated);
    }

    public EnvVariablesDTO getEnvVariablesDTO() {
        Collection<PropertyDefinition> envVariables = this.world.getEnvironment().getEnvVariables();
        EnvVariableDefinitionDTO[] envVariableDefinitionDTOS = new EnvVariableDefinitionDTO[envVariables.size()];
        for (int i = 0; i < envVariables.size(); i++) {
            envVariableDefinitionDTOS[i] = getEnvVariableDefinitionDTO((PropertyDefinition) envVariables.toArray()[i]);        }
        return new EnvVariablesDTO(envVariableDefinitionDTOS);
    }

    private EnvVariableDefinitionDTO getEnvVariableDefinitionDTO(PropertyDefinition propertyDefinition) {
        String name = propertyDefinition.getName();
        String type = propertyDefinition.getType().toString();
        if (propertyDefinition instanceof NumericPropertyDefinition) {
            NumericPropertyDefinition property = (NumericPropertyDefinition) propertyDefinition;
            String fromRange = property.getRange().getFrom().toString();
            String toRange = property.getRange().getTo().toString();
            return new EnvVariableDefinitionDTO(name, type, fromRange, toRange);
        }
        return new EnvVariableDefinitionDTO(name, type);

    }

    public boolean validateEnvVariableValue(EnvVariableValueDTO envVariableValueDTO) {
        PropertyDefinition propertyDefinition = this.world.getEnvironment().getPropertyDefinitionByName(envVariableValueDTO.getName());
        return propertyDefinition.getType().isMyType(envVariableValueDTO.getValue());
    }

    public SimulationIDListDTO getSimulationListDTO() {
        SimulationIDDTO[] simulationIDDTOS = this.simulationManager.getSimulationIDDTOS();
        return new SimulationIDListDTO(simulationIDDTOS);
    }

    public boolean validateSimulationID(int userChoice) {
        return this.simulationManager.isSimulationIDExists(userChoice);
    }

    public SimulationResultByAmountDTO getSimulationResultByAmountDTO(int simulationID) {
        Simulation simulation = this.simulationManager.getSimulationByID(simulationID);
        return new SimulationResultByAmountDTO(simulation.getId(), getEntityResultsDTO(simulation));
    }

    private EntityResultDTO[] getEntityResultsDTO(Simulation simulation) {
        //use stream
        EntityResultDTO[] entityResultDTOS = stream(this.world.getEntities().toArray())
                .map(entityDefinition -> {
                    String name = ((EntityDefinition) entityDefinition).getName();
                    int startingPopulation = ((EntityDefinition) entityDefinition).getPopulation();
                    int endingPopulation = simulation.getEntityInstanceManager().getEntityCountByName(name);
                    return new EntityResultDTO(name, startingPopulation, endingPopulation);
                })
                .toArray(EntityResultDTO[]::new);
        return entityResultDTOS;
    }


    public HistogramDTO getHistogramDTO(int simulationID, String entityName, String propertyName) {
        Map<Object, Integer> histogram = new HashMap<>();
        EntityDefinition entityDefinition = this.world.getEntityByName(entityName);
        PropertyDefinition propertyDefinition = entityDefinition.getPropertyDefinitionByName(propertyName);
        for (EntityInstance entityInstance : this.simulationManager.getSimulationByID(simulationID).getEntityInstanceManager().getInstances()) {
            if (entityInstance.getEntityDefinition().getName().equals(entityName)) {
                // we already know that the property is there
                PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);
                Object value = propertyInstance.getValue();
                if (histogram.containsKey(value)) {
                    histogram.put(value, histogram.get(value) + 1);
                } else {
                    histogram.put(value, 1);
                }
            }
        }
        return new HistogramDTO(histogram);
    }

    public boolean isXMLLoaded() {
        return this.world != null;
    }
}


