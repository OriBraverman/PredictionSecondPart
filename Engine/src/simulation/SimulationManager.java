package simulation;

import world.World;
import world.factors.environment.execution.api.ActiveEnvironment;

import java.util.ArrayList;
import java.util.List;

public class SimulationManager {
    private List<Simulation> simulations;
    private int currentSimulationIndex;

    public SimulationManager() {
        this.simulations = new ArrayList<>();
        this.currentSimulationIndex = 0;
    }

    public Simulation createSimulation(World world, ActiveEnvironment activeEnvironment) {
        currentSimulationIndex++;
        Simulation simulation = new Simulation(activeEnvironment, world, currentSimulationIndex);
        simulations.add(simulation);
        return simulation;
    }


}
