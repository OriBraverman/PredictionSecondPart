package simulation;

import world.World;
import world.factors.entity.execution.manager.EntityInstanceManager;
import world.factors.entity.execution.manager.EntityInstanceManagerImpl;
import world.factors.environment.execution.api.ActiveEnvironment;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationExecutionDetails {
    private final int id;
    private final ActiveEnvironment activeEnvironment;
    private final World world;
    private final EntityInstanceManager entityInstanceManager;
    private boolean isTerminatedBySecondsCount = false;
    private boolean isTerminatedByTicksCount = false;
    private AtomicBoolean isRunning;
    private AtomicBoolean isPaused;
    private Instant currStartTime;
    private List<Duration> durations;
    private String formattedStartTime;
    private int currentTick = 0;
    private Thread simulationThread;
    private Map<Integer, Integer> entityPopulationByTicks;

    public SimulationExecutionDetails(int id, ActiveEnvironment activeEnvironment, World world) {
        this.id = id;
        this.activeEnvironment = activeEnvironment;
        this.world = world;
        this.entityInstanceManager = new EntityInstanceManagerImpl();
        this.currStartTime = Instant.now();
        this.durations = new ArrayList<>();
        this.isRunning = new AtomicBoolean(false);
        this.isPaused = new AtomicBoolean(false);
        this.entityPopulationByTicks = new HashMap<>();
    }

    public ActiveEnvironment getActiveEnvironment() { return activeEnvironment; }

    public World getWorld() { return world; }

    public int getId(){
        return this.id;
    }

    public String getFormattedStartTime(){
        return this.formattedStartTime;
    }

    public void setFormattedStartTime(String formattedStartTime){ this.formattedStartTime = formattedStartTime; }

    public boolean isTerminatedBySecondsCount() {
        return isTerminatedBySecondsCount;
    }

    public boolean isTerminatedByTicksCount() {
        return isTerminatedByTicksCount;
    }

    public void setIsTerminatedBySecondsCount(boolean isTerminatedBySecondsCount){ this.isTerminatedBySecondsCount = isTerminatedBySecondsCount; }

    public void setIsTerminatedByTicksCount(boolean isTerminatedByTicksCount){ this.isTerminatedByTicksCount = isTerminatedByTicksCount; }

    public EntityInstanceManager getEntityInstanceManager() { return this.entityInstanceManager; }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.entityPopulationByTicks.put(currentTick, entityInstanceManager.getAliveEntityCount());
        this.currentTick = currentTick;
    }

    public Instant getCurrStartTime() {
        return currStartTime;
    }

    public void setCurrStartTime(Instant currStartTime) {
        this.currStartTime = currStartTime;
    }

    public long getSimulationSeconds() {
        if (isRunning.get() && !isPaused.get()) {
            Instant now = Instant.now();
            Duration duration = Duration.between(currStartTime, now);
            return duration.getSeconds() + durations.stream().mapToLong(Duration::getSeconds).sum();
        } else {
            return durations.stream().mapToLong(Duration::getSeconds).sum();
        }
    }

    public void addDuration(Duration duration) {
        durations.add(duration);
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    public boolean isPaused() {
        return isPaused.get();
    }

    public void setTerminatedBySecondsCount(boolean terminatedBySecondsCount) {
        isTerminatedBySecondsCount = terminatedBySecondsCount;
    }

    public void setTerminatedByTicksCount(boolean terminatedByTicksCount) {
        isTerminatedByTicksCount = terminatedByTicksCount;
    }

    public void setRunning(boolean running) {
        isRunning.set(running);
    }

    public void setPaused(boolean paused) {
        isPaused.set(paused);
    }

    public Thread getSimulationThread() {
        return simulationThread;
    }

    public void setSimulationThread(Thread simulationThread) {
        this.simulationThread = simulationThread;
    }

    public Map<Integer, Integer> getEntityPopulationByTicks() {
        return entityPopulationByTicks;
    }
}
