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
import world.factors.grid.execution.GridInstance;

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
            for (int i = 0; i < this.simulationED.getEntityInstanceManager().getPopulationByEntityDefinition(entityDefinition); i++) {
                this.simulationED.getEntityInstanceManager().create(entityDefinition, this.simulationED.getGridInstance());
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
        try {
            this.simulationED.setStatus("Running");
            runSimulation();
            this.simulationED.setStatus("Finished Successfully");
            if (simulationED.getTerminationReason() == null) {
                if (simulationED.isTerminatedBySecondsCount()) {
                    this.simulationED.setTerminationReason("Terminated by seconds count");
                } else if (simulationED.isTerminatedByTicksCount()) {
                    this.simulationED.setTerminationReason("Terminated by ticks count");
                }
            }
        } catch (Exception e) {
            this.simulationED.setStatus("Failed");
            this.simulationED.setTerminationReason(e.getMessage());
            this.simulationED.setRunning(false);
        }
    }

    private void runSimulation() {
        synchronized (simulationED) {
            this.simulationED.setSimulationThread(Thread.currentThread());
            simulationED.setRunning(true);
        }
        Date date = new Date();
        this.simulationED.setFormattedStartTime(this.dateFormat.format(date));

        initEntityInstancesArray();
        int currentTick = 0;
        while (!simulationED.getWorld().getTermination().isTerminated(currentTick, simulationED.getSimulationSeconds()) && simulationED.isRunning()) {
            while (this.simulationED.isPaused()) {
                try {
                    pauseSemaphore.acquire();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            pauseSemaphore.release();

            currentTick++;
            simulationED.setCurrentTick(currentTick);
            simulationED.getEntityInstanceManager().moveAllInstances(simulationED.getGridInstance());
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

            for (EntityInstance entityInstance : simulationED.getEntityInstanceManager().getInstances()) {
                for (Action action : actionableRules) {
                    if (action.getPrimaryEntityDefinition().getName().equals(entityInstance.getEntityDefinition().getName())) {
                        if (action.getSecondaryEntity() != null) {
                            List<EntityInstance> selectedSecondaryEntityInstances = simulationED.getEntityInstanceManager().getSelectedSeconderyEntites(action.getSecondaryEntity(),  simulationED.getActiveEnvironment(), simulationED.getGridInstance(), currentTick);
                            for (EntityInstance secondaryEntityInstance : selectedSecondaryEntityInstances) {
                                action.invoke(new ContextImpl(entityInstance, secondaryEntityInstance, simulationED.getEntityInstanceManager(), simulationED.getActiveEnvironment(), simulationED.getGridInstance(), currentTick));
                            }
                        } else {
                            action.invoke(new ContextImpl(entityInstance, simulationED.getEntityInstanceManager(), simulationED.getActiveEnvironment(), simulationED.getGridInstance(), currentTick));
                        }
                    }
                }
            }
        }
        simulationED.setIsTerminatedBySecondsCount(simulationED.getWorld().getTermination().isTerminatedBySecondsCount(simulationED.getSimulationSeconds()));
        simulationED.setIsTerminatedByTicksCount(simulationED.getWorld().getTermination().isTerminatedByTicksCount(currentTick));
        simulationED.setRunning(false);
    }
    public void printDebug(List<EntityInstance> instances) {
        GridInstance gridInstance = simulationED.getGridInstance();
        for (int y = 0; y < gridInstance.getHeight(); y++) {
            for (int x = 0; x < gridInstance.getWidth(); x++) {
                if (!gridInstance.isCellFree(gridInstance.getCoordinate(x, y))) {
                    boolean found = false;
                    for (EntityInstance instance : instances) {
                        if (instance.getCoordinate().getX() == x && instance.getCoordinate().getY() == y) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        System.out.print("1 ");
                    } else {
                        System.out.print("2 ");
                    }
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    private void printReplacedEntities(List<EntityInstance> entityInstances) {
        for (EntityInstance entityInstance : simulationED.getEntityInstanceManager().getInstances()) {
            boolean Found = false;
            for (EntityInstance temp : entityInstances) {
                if (entityInstance.getId() == temp.getId()) {
                    Found = true;
                    break;
                }
            }
            if (!Found) {
                System.out.println("Entity " + entityInstance.getId() + " " + entityInstance.getCell().getCoordinate().getX() + " " + entityInstance.getCell().getCoordinate().getY() + " is replaced");
            }
        }
    }
}
