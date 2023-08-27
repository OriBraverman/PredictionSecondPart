package ui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import dtos.*;
import engine.Serialization;
import sun.java2d.marlin.stats.Histogram;
import ui.input.InputManager;
import ui.menu.Menu;
import ui.menu.MenuItem;
import ui.menu.MenuOptions;
import engine.Engine;

public class PredictionConsuleUI {

    private final Menu mainMenu;
    private String currentLoadedPathString;
    private Engine engine = new Engine();
    private Serialization serialization = new Serialization();
    private final InputManager inputManager = new InputManager();
    public PredictionConsuleUI(){
        this.currentLoadedPathString = "no path loaded";
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem ("Load System info from XML","Please enter the full path to the XML file:", null));
        menuItems.add(new MenuItem ("Show Simulation info", null, null));
        menuItems.add(new MenuItem ("Activate Simulation", null, null));
        menuItems.add(new MenuItem ("Show Full Past Simulation info", null, null));
        menuItems.add(new MenuItem ("Exit", null, null));
        menuItems.add(new MenuItem ("Save System State", null, null));
        menuItems.add(new MenuItem ("Load System State", null, null));
        mainMenu = new Menu(menuItems);
    }

    public void Run(){
        Scanner scanner = new Scanner(System.in);
        MenuOptions userChoice = null;
        while(userChoice != MenuOptions.EXIT){
            System.out.println(mainMenu.toString());
            userChoice = MenuOptions.getChoice(inputManager.getIntInRange(1, MenuOptions.values().length));
            switch(userChoice){
                case LOAD_WORLD_XML:
                    System.out.println(this.mainMenu.getMenuItemInstructions(userChoice.ordinal()));
                    this.currentLoadedPathString = scanner.nextLine();
                    try {
                        this.engine.loadXML(this.currentLoadedPathString);
                        System.out.println("XML file loaded successfully\n");
                    } catch (Exception e) {
                        System.out.println("Error loading XML file: " + e.getMessage() + "\n");
                    }
                    break;
                case SHOW_SIMULATION_DETAILS:
                    if (!this.engine.isXMLLoaded()) {
                        System.out.println("No XML file was loaded yet\n");
                        break;
                    }
                    showSimulationDetails(engine.getSimulationDetailsDTO());
                    break;
                case ACTIVATE_SIMULATION:
                    if (!this.engine.isXMLLoaded()) {
                        System.out.println("No XML file was loaded yet\n");
                        break;
                    }
                    System.out.println("Simulation activated");
                    activateSimulation();
                    break;
                case FULL_PAST_SIMULATION_DETAILS:
                    showFullPastSimulationDetails();
                    break;
                case EXIT:
                    System.out.println("Goodbye!");
                    break;
                case SAVE_SYSTEM_STATE:
                    System.out.println("Saving system state");
                    String path = getFullPathFromUser();
                    saveSystemState(path);
                    break;
                case LOAD_SYSTEM_STATE:
                    System.out.println("Loading system state");
                    String pathToLoad = getFullPathFromUser();
                    loadSystemState(pathToLoad);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    private void loadSystemState(String pathToLoad) {
        try {
            engine = serialization.readSystemFromFile(pathToLoad);
            System.out.println("System state loaded successfully\n");
        } catch (Exception e) {
            System.out.println("Error loading system state: " + e.getMessage() + "\n");
        }
    }

    private void saveSystemState(String path) {
        try {
            serialization.writeSystemToFile(path, engine);
            System.out.println("System state saved successfully\n");
        } catch (Exception e) {
            System.out.println("Error saving system state: " + e.getMessage() + "\n");
        }
    }

    private String getFullPathFromUser() {
        System.out.println("Please enter the full path to the file including the file name:");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        return path;
    }

    private void showFullPastSimulationDetails() {
        SimulationIDListDTO simulationIDListDTO = engine.getSimulationListDTO();
        if(simulationIDListDTO.getSimulationIDDTOS().length == 0){
            System.out.println("No simulations were run yet\n");
            return;
        }
        System.out.println("Simulation List:\n");
        for (SimulationIDDTO simulationIDDTO : simulationIDListDTO.getSimulationIDDTOS()) {
            System.out.println("Simulation ID: " + simulationIDDTO.getId() + "\n"
                    + "Simulation Start Time: " + simulationIDDTO.getStartTime() + "\n");
        }
        System.out.println("Please enter the ID of the simulation you want to see:");
        Scanner scanner = new Scanner(System.in);
        int simulationID = inputManager.getInt();
        while (!engine.validateSimulationID(simulationID)) {
            System.out.println("Invalid ID, please try again: ");
            simulationID = inputManager.getInt();
        }
        System.out.println("Please choose the display mode:\n"
                + "1. Display by amount\n"
                + "2. Display by characteristic histogram\n");
        int displayMode = inputManager.getInt();
        while (displayMode != 1 && displayMode != 2) {
            System.out.println("Invalid choice, please try again: ");
            displayMode = inputManager.getInt();
        }
        if (displayMode == 1) {
            showSimulationResultByAmount(simulationID);
        } else {
            showSimulationResultByCharacteristicHistogram(simulationID);
        }
    }

    private void showSimulationResultByAmount(int simulationID) {
        SimulationResultByAmountDTO simulationResultByAmountDTO = engine.getSimulationResultByAmountDTO(simulationID);
        System.out.println("Simulation ID: " + simulationResultByAmountDTO.getSimulationID() + "\n"
                + "Entities:\n"
                + "--------------------------------------");
        for (EntityResultDTO entityResultDTO : simulationResultByAmountDTO.getEntityInstanceResults()) {
            System.out.println("Entity Name: " + entityResultDTO.getEntityName() + "\n"
                    + "Entity Start Population: " + entityResultDTO.getStartingPopulation() + "\n"
                    + "Entity End Population: " + entityResultDTO.getEndingPopulation() + "\n"
                    + "--------------------------------------");
        }
    }

    private void showSimulationResultByCharacteristicHistogram(int simulationID) {
        EntityDefinitionDTO[] entities = engine.getSimulationDetailsDTO().getEntities();
        System.out.println("Please choose Entity From the list:\n");
        for (int i = 0; i < entities.length; i++) {
            System.out.println((i + 1) + ". " + entities[i].getName() + "\n");
        }
        Scanner scanner = new Scanner(System.in);
        int entityNumber = scanner.nextInt();
        while (entityNumber < 1 || entityNumber > entities.length) {
            System.out.println("Invalid choice, please try again: ");
            entityNumber = scanner.nextInt();
        }
        EntityDefinitionDTO entity = entities[entityNumber - 1];
        System.out.println("Please choose Property From the list:\n");
        for (int i = 0; i < entity.getProperties().length; i++) {
            System.out.println((i + 1) + ". " + entity.getProperties()[i].getName() + "\n");
        }
        int propertyNumber = scanner.nextInt();
        while (propertyNumber < 1 || propertyNumber > entity.getProperties().length) {
            System.out.println("Invalid choice, please try again: ");
            propertyNumber = scanner.nextInt();
        }
        EntityPropertyDefinitionDTO property = entity.getProperties()[propertyNumber - 1];
        //display histogram that shows for each value how many instances have it
        System.out.println("Histogram for " + entity.getName() + " " + property.getName() + ":\n");
        HistogramDTO histogramDTO = engine.getHistogramDTO(simulationID, entity.getName(), property.getName());
        Map<Object, Integer> histogram = histogramDTO.getHistogram();
        for (Map.Entry<Object, Integer> entry : histogram.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void activateSimulation() {
        EnvVariablesDTO envVariablesDTO = engine.getEnvVariablesDTO();
        EnvVariablesValuesDTO envVariablesValuesDTO = getEnvVariablesValuesDTOFromUser(envVariablesDTO);
        EnvVariablesValuesDTO updatedEnvVariablesValuesDTO = engine.updateActiveEnvironmentAndInformUser(envVariablesValuesDTO);
        displayEnvVariablesValuesDTO(updatedEnvVariablesValuesDTO);
        SimulationResultDTO simulationResultDTO = engine.activateSimulation();
        System.out.println("Simulation ended");
        System.out.println("Simulation ID: " + simulationResultDTO.getId());
        if(simulationResultDTO.isTerminatedBySecondsCount()){
            System.out.println("Simulation terminated by seconds count");
        }
        if(simulationResultDTO.isTerminatedByTicksCount()){
            System.out.println("Simulation terminated by ticks count");
        }
    }

    private void displayEnvVariablesValuesDTO(EnvVariablesValuesDTO envVariablesValuesDTO) {
        System.out.println("Environment Variables Values Chosen:\n"
                + "--------------------------------------");
        for (EnvVariableValueDTO envVariableValueDTO : envVariablesValuesDTO.getEnvVariablesValues()) {
            System.out.println("Name: " + envVariableValueDTO.getName() + "\n"
                    + "Value: " + envVariableValueDTO.getValue() + "\n"
                    + "--------------------------------------");
        }
    }

    private EnvVariablesValuesDTO getEnvVariablesValuesDTOFromUser(EnvVariablesDTO envVariablesDTO) {
        EnvVariableValueDTO[] envVariableValues = new EnvVariableValueDTO[envVariablesDTO.getEnvVariables().length];
        showEnvVariablesListOfNames(envVariablesDTO);
        System.out.println("Please enter the number of variable you want to insert value for, or 0 to skip: ");
        int envVariablesNumber = inputManager.getIntInRange(0, envVariablesDTO.getEnvVariables().length);
        while (envVariablesNumber != 0) {
            //Show the user the additional details of the variable (type, range if relevant)
            EnvVariableDefinitionDTO envVariable = envVariablesDTO.getEnvVariables()[envVariablesNumber - 1];
            //Get the user's input for this variable
            envVariableValues[envVariablesNumber - 1] = getEnvVariableValueDTOFromUser(envVariable, envVariablesNumber);
            showEnvVariablesListOfNames(envVariablesDTO);
            System.out.println("Please enter the number of variable you want to insert value for, or 0 to skip: ");
            envVariablesNumber = inputManager.getIntInRange(0, envVariablesDTO.getEnvVariables().length);
        }
        for (int i = 0; i < envVariableValues.length; i++) {
            if (envVariableValues[i] == null) {
                envVariableValues[i] = new EnvVariableValueDTO(envVariablesDTO.getEnvVariables()[i].getName(), "", false);
            }
        }
        return new EnvVariablesValuesDTO(envVariableValues);
    }

    private EnvVariableValueDTO getEnvVariableValueDTOFromUser(EnvVariableDefinitionDTO envVariable, int envVariablesNumber) {
        showEnvVariableDetails(envVariable, envVariablesNumber);
        System.out.println("Please enter a value for this variable: ");
        Scanner scanner = new Scanner(System.in);
        String value = scanner.next();
        boolean isValidInput = this.engine.validateEnvVariableValue(new EnvVariableValueDTO(envVariable.getName(), value, true));
        while(!isValidInput){
            System.out.println("Invalid value, please try again: ");
            value = scanner.next();
            isValidInput = this.engine.validateEnvVariableValue(new EnvVariableValueDTO(envVariable.getName(), value, true));
        }
        return new EnvVariableValueDTO(envVariable.getName(), value, true);
    }

    private void showEnvVariableDetails(EnvVariableDefinitionDTO envVariable, int envVariablesNumber) {
        System.out.println("Environment Variable number " + envVariablesNumber + ":\n"
                + "--------------------------------------");
        System.out.println("Name: " + envVariable.getName() + "\n"
                + "Type: " + envVariable.getType());
        if (!envVariable.getFromRange().equals("")) {
            System.out.println("From Range: " + envVariable.getFromRange() + "\n"
                    + "To Range: " + envVariable.getToRange());
        }
    }

    private void showEnvVariablesListOfNames(EnvVariablesDTO envVariablesDTO) {
        //Show the user a numbered list (starting from 1) of all the names of the environment variables
        // (only a name is allowed. No additional details are needed).
        System.out.println("Environment Variables:\n"
                + "--------------------------------------");
        for (int i = 0; i < envVariablesDTO.getEnvVariables().length; i++) {
            System.out.println((i + 1) + ". " + envVariablesDTO.getEnvVariables()[i].getName() + "\n");
        }
        System.out.println("--------------------------------------");
    }
    private void showSimulationDetails(SimulationDetailsDTO simulationDetailsDTO) {
        System.out.println("Simulation Details:\n"
                + "--------------------------------------\n"
                + "Entities:\n"
                + "--------------------------------------\n");
        for (EntityDefinitionDTO entity : simulationDetailsDTO.getEntities()) {
            System.out.println("Name: " + entity.getName() + "\n"
                    + "Population: " + entity.getPopulation() + "\n"
                    + "Properties:\n"
                    + "--------------------------------------\n");
            for (EntityPropertyDefinitionDTO property : entity.getProperties()) {
                System.out.println("Name: " + property.getName() + "\n"
                        + "Type: " + property.getType() + "\n");
                if (!property.getFromRange().equals("")) {
                    System.out.println("From Range: " + property.getFromRange() + "\n"
                            + "To Range: " + property.getToRange() + "\n");
                }
                System.out.println("Value Generated: " + property.getValueGenerated() + "\n"
                        + "--------------------------------------\n");
            }
        }
        System.out.println("Rules:\n"
                + "--------------------------------------\n");
        int counter = 0;
        for (RuleDTO rule : simulationDetailsDTO.getRules()) {
            counter++;
            System.out.println("Rule number " + counter + ":\n"
                    + "--------------------------------------\n");
            System.out.println("Name: " + rule.getName() + "\n"
                        + "Activation Condition: " + rule.getActivation().getTicks() + " ticks, " + rule.getActivation().getProbability() + " probability\n"
                    + "Number of Actions: " + rule.getNumberOfActions() + "\n"
                    + "Actions:");
            for (int i = 0; i < rule.getActions().length; i++) {
                System.out.println((i + 1) + ". " + rule.getActions()[i] + "\n");
            }
        }
        System.out.println("Termination:\n"
                + "--------------------------------------");
        if (simulationDetailsDTO.getTermination().getTicksCount() != -1) {
            System.out.println("Ticks: " + simulationDetailsDTO.getTermination().getTicksCount());
        }
        if (simulationDetailsDTO.getTermination().getSecondsCount() != -1) {
            System.out.println("Seconds: " + simulationDetailsDTO.getTermination().getSecondsCount());
        }
        System.out.println("--------------------------------------\n");
    }


}