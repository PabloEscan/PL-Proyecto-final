package controllers;

import models.Cell;
import models.MazeResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MaseSolverRecursivo implements MazeSolver {

    private boolean[][] visited;
    private List<Cell> path;
    private Cell[][] uiGrid;
    private Consumer<Cell> exploreCallback;

    public MaseSolverRecursivo() {
    }

    public MaseSolverRecursivo(Cell[][] uiGrid, Consumer<Cell> exploreCallback) {
        this.uiGrid = uiGrid;
        this.exploreCallback = exploreCallback;
    }

    @Override
    public MazeResult getPath(boolean[][] grid, Cell start, Cell end) {
        return getPath(grid, start, end, null);
    }

    @Override
    public MazeResult getPath(boolean[][] grid, Cell start, Cell end, Consumer<Cell> exploreCallback) {
        this.exploreCallback = exploreCallback;
        path = new ArrayList<>();
        visited = new boolean[grid.length][grid[0].length];

        if (grid == null || grid.length == 0) {
            return new MazeResult(path, "Grid vacía");
        }

        boolean found;
        if (uiGrid != null && exploreCallback != null) {
            found = findPathStep(grid, start.getRow(), start.getCol(), end);
        } else {
            found = findPath(grid, start.getRow(), start.getCol(), end);
        }

        if (found) {
            return new MazeResult(path, null);
        } else {
            return new MazeResult(null, "No se encontró camino");
        }
    }

    private boolean findPath(boolean[][] grid, int row, int col, Cell end) {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length) {
            return false;
        }
        if (!grid[row][col] || visited[row][col]) {
            return false;
        }

        visited[row][col] = true;

        if (row == end.getRow() && col == end.getCol()) {
            path.add(new Cell(row, col));
            return true;
        }

        int[][] directions = { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };

        for (int[] d : directions) {
            int newRow = row + d[0];
            int newCol = col + d[1];

            if (findPath(grid, newRow, newCol, end)) {
                path.add(new Cell(row, col));
                return true;
            }
        }
        return false;
    }

    private boolean findPathStep(boolean[][] grid, int row, int col, Cell end) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length) {
            return false;
        }
        if (!grid[row][col] || visited[row][col]) {
            return false;
        }

        visited[row][col] = true;

        if (exploreCallback != null) {
            exploreCallback.accept(uiGrid[row][col]);
        }

        if (row == end.getRow() && col == end.getCol()) {
            path.add(new Cell(row, col));
            return true;
        }

        int[][] directions = { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };

        for (int[] d : directions) {
            int newRow = row + d[0];
            int newCol = col + d[1];

            if (findPathStep(grid, newRow, newCol, end)) {
                path.add(new Cell(row, col));
                return true;
            }
        }
        return false;
    }
}
