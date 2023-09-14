package gui.components.main.results.simulation.grid;

import dtos.gridView.EntityInstanceDTO;
import dtos.gridView.GridViewDTO;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;

public class DynamicGridView {

    public ScrollPane createDynamicGrid(GridViewDTO gridViewDTO) {
        // Create a ScrollPane to contain the GridPane
        ScrollPane scrollPane = new ScrollPane();

        // Create a GridPane
        GridPane dynamicGrid = new GridPane();
        dynamicGrid.setHgap(5.0);
        dynamicGrid.setVgap(5.0);

        int gridWidth = gridViewDTO.getGridWidth();
        int gridHeight = gridViewDTO.getGridHeight();

        // Define column constraints for the specified width
        for (int i = 0; i < gridWidth; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            dynamicGrid.getColumnConstraints().add(colConstraints);
            dynamicGrid.getColumnConstraints().add(new ColumnConstraints(50));
        }

        // Define row constraints for the specified height
        for (int i = 0; i < gridHeight; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);
            dynamicGrid.getRowConstraints().add(rowConstraints);
            dynamicGrid.getRowConstraints().add(new RowConstraints(50));
        }

        // Create and add labels with different colors for each entity
        int entityIndex = 0;
        for (EntityInstanceDTO entityInstanceDTO : gridViewDTO.getEntityInstances()) {
            String entityName = entityInstanceDTO.getEntityName();
            int x = entityInstanceDTO.getX();
            int y = entityInstanceDTO.getY();
            Color entityColor = calculateColor(entityName);
            if (entityColor != null) {
                Label entityLabel = createColoredLabel(entityName, entityColor);
                dynamicGrid.add(entityLabel, x, y);
                entityIndex++;
            }
        }

        // Set the dynamicGrid as the content of the ScrollPane
        scrollPane.setContent(dynamicGrid);

        // Add a black border around the grid
        BorderStroke borderStroke = new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1));
        dynamicGrid.setBorder(new Border(borderStroke));

        // Add black strokes to each cell (horizontal and vertical lines)
        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                Region cell = new Region();
                cell.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
                dynamicGrid.add(cell, col, row);
            }
        }

        return scrollPane;
    }

    private Color calculateColor(String entityName) {
        // Here, you can implement a logic to calculate the color based on the entity name.
        // For example, you can use a hash of the entity name to generate a color.
        int hashCode = entityName.hashCode();
        int red = (hashCode & 0xFF0000) >> 16;
        int green = (hashCode & 0x00FF00) >> 8;
        int blue = hashCode & 0x0000FF;

        return Color.rgb(red, green, blue);
    }

    private Label createColoredLabel(String text, Color color) {
        Label label = new Label(text);
        label.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        label.setTextFill(Color.WHITE); // Set text color to white for better visibility
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setAlignment(javafx.geometry.Pos.CENTER);
        return label;
    }
}
