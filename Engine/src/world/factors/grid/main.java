package grid;

import java.util.Collection;

public class main {
    public static void main(String[] args) {
        Grid grid = new Grid(13, 8);
        /*for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(grid.getCoordinate(i, j).getX() + " " + grid.getCoordinate(i, j).getY() + " | ");
            }
            System.out.println();
        }*/
        Collection<Coordinate> list = grid.findEnvironmentCells(grid.getCoordinate(6, 6), 7);
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 8; j++) {
                if (list.contains(grid.getCoordinate(i, j))) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }
}
