package simulation;

import dtos.SimulationIDDTO;
import world.World;
import world.factors.environment.execution.api.ActiveEnvironment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationManager implements Serializable {
    private Map<Integer, Simulation> simulations;
    private int currentSimulationIndex;

    public SimulationManager() {
        this.simulations = new HashMap<>();
        this.currentSimulationIndex = 0;
    }

    public Simulation createSimulation(World world, ActiveEnvironment activeEnvironment) {
        currentSimulationIndex++;
        Simulation simulation = new Simulation(activeEnvironment, world, currentSimulationIndex);
        simulations.put(currentSimulationIndex, simulation);
        return simulation;
    }


    public SimulationIDDTO[] getSimulationIDDTOS() {
        List<SimulationIDDTO> simulationIDDTOS = new ArrayList<>();
        for (Map.Entry<Integer, Simulation> entry : simulations.entrySet()) {
            Simulation simulation = entry.getValue();
            simulationIDDTOS.add(new SimulationIDDTO(simulation.getId(), simulation.getFormattedStartTime()));
        }
        return simulationIDDTOS.toArray(new SimulationIDDTO[0]);
    }

    public boolean isSimulationIDExists(int userChoice) {
        return simulations.containsKey(userChoice);
    }

    public Simulation getSimulationByID(int simulationID) {
        return simulations.get(simulationID);
    }
}
