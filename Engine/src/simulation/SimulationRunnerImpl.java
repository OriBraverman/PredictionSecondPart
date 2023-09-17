package simulation;

import context.ContextImpl;
import world.World;
import world.factors.action.api.Action;
import world.factors.action.api.ActionType;
import world.factors.entity.definition.EntityDefinition;
import world.factors.entity.execution.EntityInstance;
import world.factors.entity.execution.manager.EntityInstanceManager;
import world.factors.entity.execution.manager.EntityInstanceManagerImpl;
import world.factors.environment.execution.api.ActiveEnvironment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class SimulationRunnerImpl implements Serializable, Runnable, SimulationRunner {
    private final int id;
    private final SimulationExecutionDetails simulationED;
    private final Semaphore pauseSemaphore = new Semaphore(1);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");
    public SimulationRunnerImpl(int id, SimulationExecutionDetails simulationExecutionDetails) {
        this.id = id;
        this.simulationED = simulationExecutionDetails;
    }

    public int getId() {
        return id;
    }

    private void initEntityInstancesArray() {
        List<EntityDefinition> entityDefinitions = this.simulationED.getWorld().getEntities();
        for (EntityDefinition entityDefinition : entityDefinitions) {
            for (int i = 0; i < entityDefinition.getPopulation(); i++) {
                this.simulationED.getEntityInstanceManager().create(entityDefinition, this.simulationED.getWorld().getGrid());
            }
        }
    }

    public void pauseSimulation() {
        try {
            pauseSemaphore.acquire(); // Acquire the semaphore to pause the simulation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void resumeSimulation() {
        pauseSemaphore.release(); // Release the semaphore to resume the simulation
    }

    @Override
    public void run() {
        this.simulationED.setSimulationThread(Thread.currentThread());
        this.simulationED.setRunning(true);
        this.simulationED.setCurrStartTime(Instant.now());
        Date date = new Date();
        this.simulationED.setFormattedStartTime(this.dateFormat.format(date));

        initEntityInstancesArray();
        int currentTick = 0;
        while (!simulationED.getWorld().getTermination().isTerminated(currentTick, simulationED.getSimulationSeconds()) && !Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                while (this.simulationED.isPaused()) {
                    try {
                        pauseSemaphore.acquire();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                pauseSemaphore.release();
            }

            currentTick++;
            simulationED.setCurrentTick(currentTick);
            simulationED.getEntityInstanceManager().moveAllInstances(simulationED.getWorld().getGrid());
            int finalCurrentTick = currentTick;
            // get all runnable rules
            List<Action> actionableRules = simulationED.getWorld().getRules()
                    .stream()
                    .filter(rule -> rule.isRuleActive(finalCurrentTick))
                    .flatMap(rule -> rule.getActionsToPerform().stream())
                    .collect(Collectors.toList());
            /*List<Action> firstActions = actionableRules.stream()
                    .filter(action -> action.isFirstAction())
                    .collect(Collectors.toList());
            List<Action> lastActions = actionableRules.stream()
                    .filter(action -> action.isLastAction())
                    .collect(Collectors.toList());*/

            List<EntityInstance> tempInstances = new ArrayList<>(simulationED.getEntityInstanceManager().getInstances());
            for (EntityInstance entityInstance : tempInstances) {
                for (Action action : actionableRules) {
                    if (action.getPrimaryEntityDefinition().getName().equals(entityInstance.getEntityDefinition().getName())) {
                        if (action.getSecondaryEntity() != null) {
                            List<EntityInstance> selectedSecondaryEntityInstances = simulationED.getEntityInstanceManager().getSelectedSeconderyEntites(action.getSecondaryEntity(),  simulationED.getActiveEnvironment(), simulationED.getWorld().getGrid(), currentTick);
                            for (EntityInstance secondaryEntityInstance : selectedSecondaryEntityInstances) {
                                action.invoke(new ContextImpl(entityInstance, secondaryEntityInstance, simulationED.getEntityInstanceManager(), simulationED.getActiveEnvironment(), simulationED.getWorld().getGrid(), currentTick));
                            }
                        } else {
                            action.invoke(new ContextImpl(entityInstance, simulationED.getEntityInstanceManager(), simulationED.getActiveEnvironment(), simulationED.getWorld().getGrid(), currentTick));
                        }
                    }
                }
            }
            // move all instances from tempInstances to simulationED.getEntityInstanceManager().getInstances()
            simulationED.getEntityInstanceManager().setInstances(tempInstances);

        }
        simulationED.setIsTerminatedBySecondsCount(simulationED.getWorld().getTermination().isTerminatedBySecondsCount(simulationED.getSimulationSeconds()));
        simulationED.setIsTerminatedByTicksCount(simulationED.getWorld().getTermination().isTerminatedByTicksCount(currentTick));
        simulationED.setRunning(false);
    }
}
