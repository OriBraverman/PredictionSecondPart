package engine;

import convertor.Convertor;
import dtos.*;
import dtos.world.*;
import dtos.world.action.ActionDTO;
import resources.schema.generatedWorld.PRDWorld;

import static java.util.Arrays.stream;
import static validator.XMLValidator.*;

import simulation.Simulation;
import simulation.SimulationManager;
import world.World;
import world.factors.action.api.Action;
import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.environment.definition.impl.EnvVariableManagerImpl;
import world.factors.environment.execution.api.ActiveEnvironment;
import world.factors.property.definition.api.NumericPropertyDefinition;
import world.factors.property.definition.api.PropertyDefinition;
import world.factors.property.definition.impl.FloatPropertyDefinition;
import world.factors.property.definition.impl.IntegerPropertyDefinition;
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
import java.util.*;


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
        validateAllActionsReferToExistingEntities(tempWorld);
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

    public WorldDTO getWorldDTO() {
        List<PropertyDefinitionDTO> environment = getEnvironmentDTO();
        List<EntityDefinitionDTO> entities = getEntitiesDTO();
        List<RuleDTO> rules = getRulesDTO();
        TerminationDTO termination = getTerminationDTO();
        int gridWidth = this.world.getGrid().getWidth();
        int gridHeight = this.world.getGrid().getHeight();
        int threadCount = this.world.getThreadCount();
        return new WorldDTO(environment, entities, rules, termination, gridWidth, gridHeight, threadCount);
    }

    private List<PropertyDefinitionDTO> getEnvironmentDTO() {
        Collection<PropertyDefinition> envVariables = this.world.getEnvironment().getEnvVariables();
        List<PropertyDefinitionDTO> propertyDefinitionDTOS = new ArrayList<>();
        envVariables.forEach(propertyDefinition -> propertyDefinitionDTOS.add(getPropertyDefinitionDTO(propertyDefinition)));
    }

    private TerminationDTO getTerminationDTO() {
        return new TerminationDTO(this.world.getTermination().getTicksCount(), this.world.getTermination().getSecondsCount());
    }

    private List<RuleDTO> getRulesDTO() {
        List<Rule> rules = this.world.getRules();
        List<RuleDTO> ruleDTOS = new ArrayList<>();
        rules.forEach(rule -> ruleDTOS.add(getRuleDTO(rule)));
        return ruleDTOS;
    }

    private RuleDTO getRuleDTO(Rule rule) {
        String name = rule.getName();
        ActivationDTO activationDTO = new ActivationDTO(rule.getActivation().getTicks(), rule.getActivation().getProbabilty());
        int numberOfActions = rule.getActionsToPerform().size();
        List<ActionDTO> actions = new ArrayList<>();
        rule.getActionsToPerform().forEach(action -> actions.add(new ActionDTO(action.getActionType().toString(), getEntityDefinitionDTO(action))));
        return new RuleDTO(name, activationDTO, numberOfActions, actions);
    }

    private EntityDefinitionDTO getEntityDefinitionDTO(Action action) {
        EntityDefinition entityDefinition = action.getPrimaryEntityDefinition();
        String name = entityDefinition.getName();
        int population = entityDefinition.getPopulation();
        List<PropertyDefinitionDTO> entityPropertyDefinitionDTOS = new ArrayList<>();
        entityDefinition.getProps().forEach(propertyDefinition -> entityPropertyDefinitionDTOS.add(getPropertyDefinitionDTO(propertyDefinition)));
        return new EntityDefinitionDTO(name, population, entityPropertyDefinitionDTOS);
    }

    private PropertyDefinitionDTO getPropertyDefinitionDTO(PropertyDefinition propertyDefinition) {
        String name = propertyDefinition.getName();
        String type = propertyDefinition.getType().toString();
        boolean randomInit = propertyDefinition.isRandomInit();
        if (propertyDefinition instanceof NumericPropertyDefinition) {
            NumericPropertyDefinition property = (NumericPropertyDefinition) propertyDefinition;
            String fromRange = property.getRange().getFrom().toString();
            String toRange = property.getRange().getTo().toString();
            if (!randomInit)
                return new PropertyDefinitionDTO(name, type, fromRange, toRange, property.generateValue().toString(), randomInit);
            return new PropertyDefinitionDTO(name, type, fromRange, toRange, null, randomInit);
        }
        if (!randomInit)
            return new PropertyDefinitionDTO(name, type, null, null, propertyDefinition.generateValue().toString(), randomInit);
        return new PropertyDefinitionDTO(name, type, null, null, null, randomInit);
    }

    private List<EntityDefinitionDTO> getEntitiesDTO() {
        List<EntityDefinition> entities = this.world.getEntities();
        List<EntityDefinitionDTO> entityDefinitionDTOS = new ArrayList<>();
        entities.forEach(entityDefinition -> entityDefinitionDTOS.add(getEntityDTO(entityDefinition)));
        return entityDefinitionDTOS;
    }

    private EntityDefinitionDTO getEntityDTO(EntityDefinition entityDefinition) {
        String name = entityDefinition.getName();
        int population = entityDefinition.getPopulation();
        List<PropertyDefinitionDTO> entityPropertyDefinitionDTOS = getEntityPropertyDefinitionDTOS(entityDefinition.getProps());
        return new EntityDefinitionDTO(name, population, entityPropertyDefinitionDTOS);
    }

    private List<PropertyDefinitionDTO> getEntityPropertyDefinitionDTOS(List<PropertyDefinition> properties) {
        List<PropertyDefinitionDTO> entityPropertyDefinitionDTOS = new ArrayList<>();
        properties.forEach(propertyDefinition -> entityPropertyDefinitionDTOS.add(getPropertyDefinitionDTO(propertyDefinition)));
        return entityPropertyDefinitionDTOS;
    }

    public boolean validateEnvVariableValue(EnvVariableValueDTO envVariableValueDTO) {
        PropertyDefinition propertyDefinition = this.world.getEnvironment().getPropertyDefinitionByName(envVariableValueDTO.getName());
        if (propertyDefinition instanceof IntegerPropertyDefinition) {
            IntegerPropertyDefinition property = (IntegerPropertyDefinition) propertyDefinition;
            return property.isInRange(envVariableValueDTO.getValue());
        } else if (propertyDefinition instanceof FloatPropertyDefinition) {
            FloatPropertyDefinition property = (FloatPropertyDefinition) propertyDefinition;
            return property.isInRange(envVariableValueDTO.getValue());
        }
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

    public NewExecutionInputDTO getNewExecutionInputDTO() {
        List<PropertyDefinitionDTO> envVariables = getEnvironmentDTO();
        List<EntityDefinitionDTO> entityDefinitionDTOS = getEntitiesDTO();
        return new NewExecutionInputDTO(envVariables, entityDefinitionDTOS);
    }
}


