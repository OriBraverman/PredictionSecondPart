package dtos;

public class SimulationIDListDTO {
    private final SimulationIDDTO[] simulationIDDTOS;

    public SimulationIDListDTO(SimulationIDDTO[] simulationIDDTOS) {
        this.simulationIDDTOS = simulationIDDTOS;
    }

    public SimulationIDDTO[] getSimulationIDDTOS() {
        return simulationIDDTOS;
    }
}
