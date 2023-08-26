package gui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dtos.*;
import gui.menu.Menu;
import gui.menu.MenuItem;
import gui.menu.MenuOptions;
import engine.Engine;

public class PredictionConsuleUI {

    private final Menu mainMenu;
    private String currentLoadedPathString;
    private Engine engine = new Engine();
    public PredictionConsuleUI(){
        this.currentLoadedPathString = "no path loaded";
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem ("Load System info from XML","Please enter the full path to the XML file:", null));
        menuItems.add(new MenuItem ("Show Simulation info", null, null));
        menuItems.add(new MenuItem ("Activate Simulation", null, null));
        menuItems.add(new MenuItem ("Show Full Past Simulation info", null, null));
        menuItems.add(new MenuItem ("Exit", null, null));
        mainMenu = new Menu(menuItems);
    }

    public void Run(){
        Scanner scanner = new Scanner(System.in);
        MenuOptions userChoice = null;
        while(userChoice != MenuOptions.EXIT){
            System.out.println(mainMenu.toString());
            userChoice = MenuOptions.getChoice(scanner.nextInt());
            switch(userChoice){
                case LOAD_WORLD_XML:
                    System.out.println(this.mainMenu.getMenuItemInstructions(userChoice.ordinal()));
                    this.currentLoadedPathString = scanner.next();
                    /*try {
                        this.engine.loadXML(this.currentLoadedPathString);
                        System.out.println("XML file loaded successfully\n");
                    } catch (FileNotFoundException e) {
                        System.out.println("Error loading XML file: " + e.getMessage() + "\n");
                    } catch (Exception e) {
                        System.out.println("Error loading XML file: " + e.getMessage() + "\n");
                    }*/
                    break;
                case SHOW_SIMULATION_DETAILS:
                    showSimulationDetails(engine.getSimulationDetailsDTO());
                    break;
                case ACTIVATE_SIMULATION:
                    System.out.println("Simulation activated");
                    activateSimulation();
                    break;
                case FULL_PAST_SIMULATION_DETAILS:
                    System.out.println("Current loaded path: " + this.currentLoadedPathString);
                    break;
                case EXIT:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    private void activateSimulation() {
       /* EnvVariablesDTO envVariablesDTO = engine.getEnvVariablesDTO();
        EnvVariablesValuesDTO envVariablesValuesDTO = getEnvVariablesValuesDTOFromUser(envVariablesDTO);
        SimulationResultDTO simulationResultDTO = engine.activateSimulation(envVariablesValuesDTO);
        System.out.println("Simulation ended");
        System.out.println("Simulation ID: " + simulationResultDTO.getId());
        if(simulationResultDTO.isTerminatedBySecondsCount()){
            System.out.println("Simulation terminated by seconds count");
        }
        if(simulationResultDTO.isTerminatedByTicksCount()){
            System.out.println("Simulation terminated by ticks count");
        }*/
    }

    private EnvVariablesValuesDTO getEnvVariablesValuesDTOFromUser(EnvVariablesDTO envVariablesDTO) {
        EnvVariableValueDTO[] envVariableValueDTOS = new EnvVariableValueDTO[envVariablesDTO.getEnvVariables().length];
        for (int i = 0; i < envVariablesDTO.getEnvVariables().length; i++) {
            envVariableValueDTOS[i] = getEnvVariableValueDTOFromUser(envVariablesDTO.getEnvVariables()[i]);
        }
        return new EnvVariablesValuesDTO(envVariableValueDTOS);
    }

    private EnvVariableValueDTO getEnvVariableValueDTOFromUser(EnvVariableDefinitionDTO envVariable) {
        // display the env variable to the user
        showEnvVariableDetails(envVariable);
        //ask the user if he wants to insert value
        System.out.println("Do you want to insert a value for this variable? (y/n)\n");
        Scanner scanner = new Scanner(System.in);
        String userChoice = scanner.next();
        while (!userChoice.equals("y") && !userChoice.equals("n")) {
            System.out.println("Invalid choice, please try again: ");
            userChoice = scanner.next();
        }
        if (userChoice.equals("y")) {
            // ask for value
            System.out.println("Please enter a value for this variable: ");
            String value = scanner.next();
            boolean isValidInput = this.engine.validateEnvVariableValue(new EnvVariableValueDTO(envVariable.getName(), value, true));
            while(!isValidInput){
                System.out.println("Invalid value, please try again: ");
                value = scanner.next();
                isValidInput = this.engine.validateEnvVariableValue(new EnvVariableValueDTO(envVariable.getName(), value, true));
            }
            return new EnvVariableValueDTO(envVariable.getName(), value, true);
        }
        return new EnvVariableValueDTO(envVariable.getName(), "", false);

    }
    private void showEnvVariableDetails(EnvVariableDefinitionDTO envVariable) {
        System.out.println("Name: " + envVariable.getName() + "\n"
                + "Type: " + envVariable.getType());
        if (!envVariable.getFromRange().equals("")) {
            System.out.println("From Range: " + envVariable.getFromRange()
                    + " To Range: " + envVariable.getToRange() + "\n");
        }
        System.out.println("--------------------------------------\n");
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