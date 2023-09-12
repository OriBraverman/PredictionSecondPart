package simulation;

import world.World;
import world.factors.entity.execution.manager.EntityInstanceManager;
import world.factors.entity.execution.manager.EntityInstanceManagerImpl;
import world.factors.environment.execution.api.ActiveEnvironment;

import java.text.SimpleDateFormat;
import java.time.Instant;

public class SimulationExecutionDetails {
    private final int id;
    private final ActiveEnvironment activeEnvironment;
    private final World world;
    private final EntityInstanceManager entityInstanceManager;
    private boolean isTerminatedBySecondsCount = false;
    private boolean isTerminatedByTicksCount = false;
    private Instant startTime;
    private String formattedStartTime;
    private int currentTick = 0;

    public SimulationExecutionDetails(int id, ActiveEnvironment activeEnvironment, World world) {
        this.id = id;
        this.activeEnvironment = activeEnvironment;
        this.world = world;
        this.entityInstanceManager = new EntityInstanceManagerImpl();
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
        this.currentTick = currentTick;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
}
