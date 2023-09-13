package simulation;

import dtos.SimulationIDListDTO;
import world.World;
import world.factors.environment.execution.api.ActiveEnvironment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationExecutionManager implements Serializable {
    private Map<Integer, SimulationRunner> simulations;
    private Map<Integer, SimulationExecutionDetails> simulationDetails;
    private int currentSimulationIndex;
    ExecutorService threadExecutor;

    public SimulationExecutionManager(int threadCount) {
        this.simulations = new HashMap<>();
        this.simulationDetails = new HashMap<>();
        this.currentSimulationIndex = 0;
        this.threadExecutor = Executors.newFixedThreadPool(threadCount);

    }

    public int createSimulation(World world, ActiveEnvironment activeEnvironment) {
        currentSimulationIndex++;
        SimulationExecutionDetails simulationExecutionDetails = new SimulationExecutionDetails(currentSimulationIndex, activeEnvironment, world);
        SimulationRunner simulationRunner = new SimulationRunnerImpl(currentSimulationIndex, simulationExecutionDetails);
        simulations.put(currentSimulationIndex, simulationRunner);
        simulationDetails.put(currentSimulationIndex, simulationExecutionDetails);
        return currentSimulationIndex;
    }


    public SimulationIDListDTO getSimulationIDListDTO() {
        List<Integer> simulationIDs = new ArrayList<>();
        simulationIDs.addAll(simulationDetails.keySet());
        return new SimulationIDListDTO(simulationIDs);
    }

    public boolean isSimulationIDExists(int userChoice) {
        return simulations.containsKey(userChoice);
    }

    public SimulationExecutionDetails getSimulationDetailsByID(int simulationID) {
        return simulationDetails.get(simulationID);
    }

    public void runSimulation(int simulationId) {
        SimulationRunnerImpl simulationRunnerImpl = (SimulationRunnerImpl) simulations.get(simulationId);
        threadExecutor.execute(simulationRunnerImpl);
    }

    public void stopSimulation(int simulationID) {
        SimulationExecutionDetails simulationExecutionDetails = simulationDetails.get(simulationID);
        Thread simulationThread = simulationExecutionDetails.getSimulationThread();
        if (simulationThread != null) {
            simulationThread.interrupt();
            simulationExecutionDetails.setRunning(false);
        }
    }

    public void pauseSimulation(int simulationID) {
        SimulationExecutionDetails simulationExecutionDetails = simulationDetails.get(simulationID);
        Thread simulationThread = simulationExecutionDetails.getSimulationThread();
        if (simulationThread != null) {
            simulationThread.suspend();
            simulationExecutionDetails.setPaused(true);
        }
    }

    public void resumeSimulation(int simulationID) {
        SimulationExecutionDetails simulationExecutionDetails = simulationDetails.get(simulationID);
        Thread simulationThread = simulationExecutionDetails.getSimulationThread();
        if (simulationThread != null) {
            simulationThread.resume();
            simulationExecutionDetails.setPaused(false);
        }
    }
}
