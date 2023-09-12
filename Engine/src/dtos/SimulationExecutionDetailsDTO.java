package dtos;

public class SimulationExecutionDetailsDTO {
    private int id;
    private boolean isTerminatedBySecondsCount;
    private boolean isTerminatedByTicksCount;
    private int numberOfEntities;
    private int currentTick;
    private long durationInSeconds;
    public SimulationExecutionDetailsDTO(int id, boolean isTerminatedBySecondsCount, boolean isTerminatedByTicksCount, int numberOfEntities, int currentTick, long durationInSeconds) {
        this.id = id;
        this.isTerminatedBySecondsCount = isTerminatedBySecondsCount;
        this.isTerminatedByTicksCount = isTerminatedByTicksCount;
        this.numberOfEntities = numberOfEntities;
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

    public int getNumberOfEntities() {
        return numberOfEntities;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }
}
