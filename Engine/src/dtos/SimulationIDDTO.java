package dtos;

public class SimulationIDDTO {
    private final int id;
    private final String startTime;

    public SimulationIDDTO(int id, String startTime) {
        this.id = id;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }
}
