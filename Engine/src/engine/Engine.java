package engine;

import convertor.Convertor;
import dtos.*;
import resources.schema.generatedWorld.PRDWorld;
import static validator.XMLValidator.*;

import simulation.Simulation;
import simulation.SimulationManager;
import value.generator.api.ValueGenerator;
import value.generator.api.ValueGeneratorFactory;
import world.World;
import world.factors.entity.definition.EntityDefinition;
import world.factors.environment.definition.impl.EnvVariableManagerImpl;
import world.factors.environment.execution.api.ActiveEnvironment;
import world.factors.property.definition.api.EntityPropertyDefinition;
import world.factors.property.definition.api.NumericPropertyDefinition;
import world.factors.property.definition.api.PropertyDefinition;
import world.factors.property.definition.api.PropertyType;
import world.factors.property.execution.PropertyInstance;
import world.factors.property.execution.PropertyInstanceImpl;
import world.factors.rule.Rule;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

public class Engine {
    World world;
    Convertor convertor;
    SimulationManager simulationManager;

    public Engine() {
        this.world = null;
        this.convertor = new Convertor();
        this.simulationManager = new SimulationManager();
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

    public void loadXML(String path) throws FileNotFoundException {
        Path xmlPath = Paths.get(path);
        validateFileExists(xmlPath);
        validateFileIsXML(xmlPath);
        PRDWorld generatedWorld = fromXmlFileToObject(xmlPath);
        validateXMLContent(generatedWorld);
        this.convertor.setGeneratedWorld(generatedWorld);
        this.world = convertor.convertPRDWorldToWorld();
        validateMathActionHasNumericArgs(this.world.getRules(), this.world.getEntities(), (EnvVariableManagerImpl) this.world.getEnvironment());

    }


    public SimulationResultDTO activateSimulation(EnvVariablesValuesDTO envVariablesValuesDTO) {
        ActiveEnvironment activeEnvironment = this.world.getEnvironment().createActiveEnvironment();
        for (EnvVariableValueDTO envVariableValueDTO : envVariablesValuesDTO.getEnvVariablesValues()) {
            Object value = envVariableValueDTO.getValue();
            if (envVariableValueDTO.getValue().equals("")) {
                ValueGenerator valueGenerator = createValueGenerator(envVariableValueDTO.getName());
                value = valueGenerator.generateValue();
            }
            PropertyInstance propertyInstance = new PropertyInstanceImpl(this.world.getEnvironment().getPropertyDefinitionByName(envVariableValueDTO.getName()), value);
            activeEnvironment.addPropertyInstance(propertyInstance);
        }
        Simulation simulation = this.simulationManager.createSimulation(this.world, activeEnvironment);
        simulation.run();
        return new SimulationResultDTO(simulation.getId(), simulation.isTerminatedBySecondsCount(), simulation.isTerminatedByTicksCount());
    }

    private ValueGenerator createValueGenerator(String name) {
        PropertyDefinition propertyDefinition = this.world.getEnvironment().getPropertyDefinitionByName(name);
        if (propertyDefinition.getType() == PropertyType.BOOLEAN) {
            ValueGeneratorFactory.createRandomBoolean();
        } else if (propertyDefinition.getType() == PropertyType.DECIMAL) {
            NumericPropertyDefinition numericPropertyDefinition = (NumericPropertyDefinition) propertyDefinition;
            return ValueGeneratorFactory.createRandomInteger((int)numericPropertyDefinition.getRange().getFrom(), (int)numericPropertyDefinition.getRange().getTo());
        } else if (propertyDefinition.getType() == PropertyType.FLOAT) {
            NumericPropertyDefinition numericPropertyDefinition = (NumericPropertyDefinition) propertyDefinition;
            return ValueGeneratorFactory.createRandomFloat((float)numericPropertyDefinition.getRange().getFrom(), (float)numericPropertyDefinition.getRange().getTo());
        }
        return ValueGeneratorFactory.createRandomString();
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

    private EntityPropertyDefinitionDTO[] getEntityPropertyDefinitionDTOS(List<EntityPropertyDefinition> properties) {
        EntityPropertyDefinitionDTO[] entityPropertyDefinitionDTOS = new EntityPropertyDefinitionDTO[properties.size()];
        for (int i = 0; i < properties.size(); i++) {
            entityPropertyDefinitionDTOS[i] = getEntityPropertyDefinitionDTO(properties.get(i));
        }
        return entityPropertyDefinitionDTOS;
    }

    private EntityPropertyDefinitionDTO getEntityPropertyDefinitionDTO(EntityPropertyDefinition property) {
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
}


