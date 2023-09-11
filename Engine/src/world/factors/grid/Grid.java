package world.factors.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Grid {
    // the grid is a 2D array of Coordinates
    private Cell[][] grid;
    private int horizontalDiameter;
    private int verticalDiameter;
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    public Grid(int n, int m) {
        horizontalDiameter = n;
        verticalDiameter = m;
        grid = new Cell[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = new Cell(new Coordinate(i, j));
            }
        }
    }
    public Coordinate getCoordinate(int x, int y) {
        return grid[x][y].getCoordinate();
    }

    public boolean isCellFree(Coordinate coordinate) {
        return !grid[coordinate.getX()][coordinate.getY()].isOccupied();
    }

    public Coordinate moveEntity(Coordinate source, Direction direction) {
        Coordinate destination = getCoordinateInDirection(source, direction);
        if (isCellFree(destination)) {
            grid[source.getX()][source.getY()].setOccupied(false);
            grid[destination.getX()][destination.getY()].setOccupied(true);
            return destination;
        }
        return null;
    }

    public int getWidth() {
        return horizontalDiameter;
    }

    public int getHeight() {
        return verticalDiameter;
    }

    private Coordinate getCoordinateInDirection(Coordinate source, Direction direction) {
        switch (direction) {
            case UP:
                return getCoordinate(source.getX(), (source.getY() - 1 + verticalDiameter) % verticalDiameter);
            case DOWN:
                return getCoordinate(source.getX(), (source.getY() + 1 + verticalDiameter) % verticalDiameter);
            case LEFT:
                return getCoordinate((source.getX() - 1 + horizontalDiameter) % horizontalDiameter, source.getY());
            case RIGHT:
                return getCoordinate((source.getX() + 1 + horizontalDiameter) % horizontalDiameter, source.getY());
            default:
                return source;
        }
    }

    //write a function, that, given a source coordinate and a rank (integer), returns the (accumulative) list of cells (coordinates) that surrounds the given source coordinate.
    //Note: for Rank X, the list will include all the cells of Rank x-1 and x-2 until Rank = 1
    public Collection<Coordinate> findEnvironmentCells(Coordinate source, int rank) {
        Collection<Coordinate> environmentCells = new ArrayList<>();
        for (int i = 1; i <= rank; i++) {
            environmentCells.addAll(getRankCells(source, i));
        }
        return environmentCells;
    }

    private Collection<Coordinate> getRankCells(Coordinate source, int rank) {
        Collection<Coordinate> rankCells = new HashSet<>();
        for (Direction d: Direction.values()){
            rankCells.addAll(getCellsInDirection(source, rank, d));
        }
        return rankCells;
    }

    private Collection<Coordinate> getCellsInDirection(Coordinate source, int rank, Direction direction) {
        Collection<Coordinate> directionCells = new HashSet<>();
        int rankDiameter = 2 * rank + 1;
        if (rankDiameter <= verticalDiameter && rankDiameter <= horizontalDiameter) {
            directionCells.addAll(getCellsInDirectionHelper(source, rank, direction));
        } else if ((direction == Direction.UP || direction == Direction.DOWN) && rankDiameter > horizontalDiameter) {
            directionCells.addAll(getLineInDirection(source, rank, direction));
        } else if ((direction == Direction.LEFT || direction == Direction.RIGHT) && rankDiameter > verticalDiameter) {
            directionCells.addAll(getLineInDirection(source, rank, direction));
        } else if (direction == Direction.LEFT && rankDiameter == horizontalDiameter + 1 && rankDiameter <= verticalDiameter) {
            directionCells.addAll(getCellsInDirectionHelper(source, rank, Direction.LEFT));
        } else if (direction == Direction.UP && rankDiameter == verticalDiameter + 1 && rankDiameter <= horizontalDiameter) {
            directionCells.addAll(getCellsInDirectionHelper(source, rank, Direction.UP));
        }
        return directionCells;
    }

    private Collection<Coordinate> getLineInDirection(Coordinate source, int rank, Direction direction) {
        Coordinate from, to;
        int hDiameter = grid.length, vDiameter = grid[0].length;
        switch (direction) {
            case UP:
                from = getCoordinate(0, (source.getY() - rank + vDiameter) % vDiameter);
                to = getCoordinate(hDiameter - 1, (source.getY() - rank + vDiameter) % vDiameter);
                break;
            case DOWN:
                from = getCoordinate(0, (source.getY() + rank + vDiameter) % vDiameter);
                to = getCoordinate(hDiameter - 1, (source.getY() + rank + vDiameter) % vDiameter);
                break;
            case LEFT:
                from = getCoordinate((source.getX() - rank + hDiameter) % hDiameter, 0);
                to = getCoordinate((source.getX() - rank + hDiameter) % hDiameter, vDiameter - 1);
                break;
            case RIGHT:
                from = getCoordinate((source.getX() + rank + hDiameter) % hDiameter, 0);
                to = getCoordinate((source.getX() + rank + hDiameter) % hDiameter, vDiameter - 1);
                break;
            default:
                return new HashSet<>();
        }
        return getCellsInTheSameLine(from, to);
    }
    private Collection<Coordinate> getCellsInDirectionHelper(Coordinate source, int rank, Direction direction) {
        Coordinate from, to;
        int hDiameter = horizontalDiameter, vDiameter = verticalDiameter;
        switch (direction) {
            case LEFT:
                from = getCoordinate((source.getX() - rank + hDiameter) % hDiameter, (source.getY() - rank + vDiameter) % vDiameter);
                to = getCoordinate((source.getX() - rank + hDiameter) % hDiameter, (source.getY() + rank + vDiameter) % vDiameter);
                break;
            case RIGHT:
                from = getCoordinate((source.getX() + rank + hDiameter) % hDiameter, (source.getY() - rank + vDiameter) % vDiameter);
                to = getCoordinate((source.getX() + rank + hDiameter) % hDiameter, (source.getY() + rank + vDiameter) % vDiameter);
                break;
            case UP:
                from = getCoordinate((source.getX() - rank + hDiameter) % hDiameter, (source.getY() - rank + vDiameter) % vDiameter);
                to = getCoordinate((source.getX() + rank + hDiameter) % hDiameter, (source.getY() - rank + vDiameter) % vDiameter);
                break;
            case DOWN:
                from = getCoordinate((source.getX() - rank + hDiameter) % hDiameter, (source.getY() + rank + vDiameter) % vDiameter);
                to = getCoordinate((source.getX() + rank + hDiameter) % hDiameter, (source.getY() + rank + vDiameter) % vDiameter);
                break;
            default:
                return new HashSet<>();
        }
        return getCellsInTheSameLine(from, to);
    }

    private Collection<Coordinate> getCellsInTheSameLine(Coordinate from, Coordinate to) {
        Collection<Coordinate> lineCells = new HashSet<>();
        if (from.getX() != to.getX() && from.getY() != to.getY()) {
            return lineCells;
        }
        else if (from.getX() == to.getX()) {
            int y = from.getY();
            int x = from.getX();
            while (y != to.getY()) {
                lineCells.add(getCoordinate(x, y));
                y = (y + 1) % verticalDiameter;
            }
            lineCells.add(getCoordinate(x, y));
            return lineCells;
        }
        else {
            int x = from.getX();
            int y = from.getY();
            while (x != to.getX()) {
                lineCells.add(getCoordinate(x, y));
                x = (x + 1) % horizontalDiameter;
            }
            lineCells.add(getCoordinate(x, y));
            return lineCells;
        }
    }

    public Coordinate getRandomAvailableCoordinate() {
        List<Coordinate> availableCoordinates = new ArrayList<>();
        for (int i = 0; i < horizontalDiameter; i++) {
            for (int j = 0; j < verticalDiameter; j++) {
                if (!grid[i][j].isOccupied()) {
                    availableCoordinates.add(grid[i][j].getCoordinate());
                }
            }
        }
        if (availableCoordinates.isEmpty()) {
            throw new IllegalStateException("Grid is full");
        }
        Coordinate coordinate = availableCoordinates.get((int) (Math.random() * availableCoordinates.size()));
        grid[coordinate.getX()][coordinate.getY()].setOccupied(true);
        return coordinate;
    }
}