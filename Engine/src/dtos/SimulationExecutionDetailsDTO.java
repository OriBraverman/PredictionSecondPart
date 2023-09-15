package dtos;

import java.util.List;

public class SimulationExecutionDetailsDTO {
    private int id;
    private boolean isTerminatedBySecondsCount;
    private boolean isTerminatedByTicksCount;
    private boolean isRunning;
    private boolean isPaused;
    private int numberOfEntities;
    private List<EntityPopulationDTO> entitiesPopulation;
    private int currentTick;
    private long durationInSeconds;
    public SimulationExecutionDetailsDTO(int id, boolean isTerminatedBySecondsCount, boolean isTerminatedByTicksCount, boolean isRunning, boolean isPaused, int numberOfEntities, List<EntityPopulationDTO> entitiesPopulation, int currentTick, long durationInSeconds) {
        this.id = id;
        this.isTerminatedBySecondsCount = isTerminatedBySecondsCount;
        this.isTerminatedByTicksCount = isTerminatedByTicksCount;
        this.isRunning = isRunning;
        this.isPaused = isPaused;
        this.numberOfEntities = numberOfEntities;
        this.entitiesPopulation = entitiesPopulation;
        this.currentTick = currentTick;
        this.durationInSeconds = durationInSeconds;
    }

    public int getId() {
        return id;
    }

    public boolean isTerminatedBySecondsCount() {
        return isTerminatedBySecondsCount;
    }

    public boolean isTerminatedByTicksCount() {
        return isTerminatedByTicksCount;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getNumberOfEntities() {
        return numberOfEntities;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public List<EntityPopulationDTO> getEntitiesPopulation() {
        return entitiesPopulation;
    }
}
