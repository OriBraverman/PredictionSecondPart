package dtos;

public class SimulationResultDTO {
    private int id;
    private boolean isTerminatedBySecondsCount;
    private boolean isTerminatedByTicksCount;

    public SimulationResultDTO(int id, boolean isTerminatedBySecondsCount, boolean isTerminatedByTicksCount) {
        this.id = id;
        this.isTerminatedBySecondsCount = isTerminatedBySecondsCount;
        this.isTerminatedByTicksCount = isTerminatedByTicksCount;
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
}
