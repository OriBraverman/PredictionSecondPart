package dtos.world;

public class TerminationDTO {
    private int secondsCount = -1;
    private int ticksCount = -1;

    public TerminationDTO(int secondsCount, int ticksCount) {
        this.secondsCount = secondsCount;
        this.ticksCount = ticksCount;
    }

    public int getSecondsCount() {
        return secondsCount;
    }

    public int getTicksCount() {
        return ticksCount;
    }
}
