package simulation;

import dtos.SimulationIDListDTO;
import world.World;
import world.factors.entity.execution.manager.EntityInstanceManager;
import world.factors.environment.execution.api.ActiveEnvironment;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

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

    public int createSimulation(World world, ActiveEnvironment activeEnvironment, EntityInstanceManager entityInstanceManager) {
        currentSimulationIndex++;
        SimulationExecutionDetails simulationExecutionDetails = new SimulationExecutionDetails(currentSimulationIndex, activeEnvironment, entityInstanceManager, world);
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
            //simulationThread.interrupt();
            simulationExecutionDetails.setRunning(false);
            if (simulationExecutionDetails.isPaused()) {
                simulationExecutionDetails.setPaused(false);
                SimulationRunnerImpl simulationRunnerImpl = (SimulationRunnerImpl) simulations.get(simulationID);
                simulationRunnerImpl.resumeSimulation();
            }
        }
    }

    public void pauseSimulation(int simulationID) {
        SimulationExecutionDetails simulationExecutionDetails = simulationDetails.get(simulationID);
        Thread simulationThread = simulationExecutionDetails.getSimulationThread();
        if (simulationThread != null && simulationExecutionDetails.isRunning()) {
            simulationExecutionDetails.setPaused(true);
            SimulationRunnerImpl simulationRunnerImpl = (SimulationRunnerImpl) simulations.get(simulationID);
            simulationRunnerImpl.pauseSimulation();
            simulationExecutionDetails.addDuration(Duration.between(simulationExecutionDetails.getCurrStartTime(), java.time.Instant.now()));
        }
    }

    public void resumeSimulation(int simulationID) {
        SimulationExecutionDetails simulationExecutionDetails = simulationDetails.get(simulationID);
        Thread simulationThread = simulationExecutionDetails.getSimulationThread();
        if (simulationThread != null) {
            simulationExecutionDetails.setPaused(false);
            SimulationRunnerImpl simulationRunnerImpl = (SimulationRunnerImpl) simulations.get(simulationID);
            simulationRunnerImpl.resumeSimulation();
            simulationExecutionDetails.setCurrStartTime(java.time.Instant.now());
        }
    }

    public int getPendingSimulationsCount() {
        int count = 0;
        for (SimulationExecutionDetails simulationExecutionDetails : simulationDetails.values()) {
            if (simulationExecutionDetails.getSimulationThread() == null) {
                count++;
            }
        }
        return count;
    }
    public int getActiveSimulationsCount() {
        int count = 0;
        for (SimulationExecutionDetails simulationExecutionDetails : simulationDetails.values()) {
            if (simulationExecutionDetails.isRunning() && simulationExecutionDetails.getSimulationThread() != null) {
                count++;
            }
        }
        return count;
    }
    public int getCompletedSimulationsCount() {
        int count = 0;
        for (SimulationExecutionDetails simulationExecutionDetails : simulationDetails.values()) {
            if (!simulationExecutionDetails.isRunning() && simulationExecutionDetails.getSimulationThread() != null) {
                count++;
            }
        }
        return count;
    }

    public boolean isSimulationCompleted(int simulationID) {
        SimulationExecutionDetails simulationExecutionDetails = simulationDetails.get(simulationID);
        return !simulationExecutionDetails.isRunning() && simulationExecutionDetails.getSimulationThread() != null;
    }

}
