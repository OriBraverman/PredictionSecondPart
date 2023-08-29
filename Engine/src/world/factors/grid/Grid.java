package world.factors.grid;

import java.util.ArrayList;
import java.util.Collection;

public class Grid {
    // the grid is a 2D array of Coordinates
    private Coordinate[][] grid;

    public Grid(int n, int m) {
        grid = new Coordinate[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = new Coordinate(i, j);
            }
        }
    }
    //write a function, that, given a source coordinate and a rank (integer), returns the (accumulative) list of cells (coordinates) that surrounds the given source coordinate.
    //Note: for Rank X, the list will include all the cells of Rank x-1 and x-2 until Rank = 1
    Collection<Coordinate> findEnvironmentCells(Coordinate source, int rank) {
        Collection<Coordinate> environmentCells = new ArrayList<>();
        for (int i = 1; i <= rank; i++) {
            environmentCells.addAll(getRankCells(source, i));
        }
        return environmentCells;
    }

    Collection<Coordinate> getRankCells(Coordinate source, int rank) {
        Collection<Coordinate> rankCells = new ArrayList<>();


    }
}