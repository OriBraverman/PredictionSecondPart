package dtos.gridView;

import java.util.List;

public class GridViewDTO {
    private final int gridWidth;
    private final int gridHeight;
    private final List<EntityInstanceDTO> entityInstances;

    public GridViewDTO(int gridWidth, int gridHeight, List<EntityInstanceDTO> entityInstances) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.entityInstances = entityInstances;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public List<EntityInstanceDTO> getEntityInstances() {
        return entityInstances;
    }
}
