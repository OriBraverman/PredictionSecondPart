package gui.components.main.results.simulation.grid;

import dtos.gridView.EntityInstanceDTO;
import dtos.gridView.GridViewDTO;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Map;

public class DynamicGridView {

    public ScrollPane createDynamicGrid(GridViewDTO gridViewDTO) {
        // Create a ScrollPane to contain the GridPane
        ScrollPane scrollPane = new ScrollPane();
        // set scrollPane size
        scrollPane.setPrefSize(600, 400);

        // Create a GridPane
        GridPane dynamicGrid = new GridPane();

        int gridWidth = gridViewDTO.getGridWidth();
        int gridHeight = gridViewDTO.getGridHeight();

        // Define column constraints for the specified width
        //<ColumnConstraints hgrow="NEVER" maxWidth="-Infinity" minWidth="50.0" prefWidth="80.0" />
        for (int i = 0; i < gridWidth; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.NEVER);
            colConstraints.setMaxWidth(Double.MAX_VALUE);
            colConstraints.setMinWidth(5.0);
            colConstraints.setPrefWidth(10.0);
            dynamicGrid.getColumnConstraints().add(colConstraints);
        }

        // Define row constraints for the specified height
        //<RowConstraints maxHeight="-Infinity" minHeight="30.0" prefHeight="50.0" vgrow="SOMETIMES" />
        for (int i = 0; i < gridHeight; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMaxHeight(Double.MAX_VALUE);
            rowConstraints.setMinHeight(5.0);
            rowConstraints.setPrefHeight(10.0);
            rowConstraints.setVgrow(Priority.SOMETIMES);
            dynamicGrid.getRowConstraints().add(rowConstraints);
        }

        // Add an empty label to each cell of the grid
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                dynamicGrid.add(createColoredRectangle(Color.WHITE), i, j);
            }
        }

        // Create and add labels with different colors for each entity
        for (EntityInstanceDTO entityInstanceDTO : gridViewDTO.getEntityInstances()) {
            String entityName = entityInstanceDTO.getEntityName();
            int x = entityInstanceDTO.getX();
            int y = entityInstanceDTO.getY();
            Color entityColor = calculateColor(entityName);
            if (entityColor != null) {
                //paint the cell with the entity color
                dynamicGrid.add(createColoredRectangle(entityColor), x, y);
            }
        }

        // Set the dynamicGrid as the content of the ScrollPane
        scrollPane.setContent(dynamicGrid);

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

    private Rectangle createColoredRectangle(Color color) {
        Rectangle rectangle = new Rectangle(10, 10);
        rectangle.setFill(color);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }
}
