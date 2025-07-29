package controllers;
import models.Cell;
import models.MazeResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MaseSolverRecursivoCompleto implements MazeSolver {

    private boolean[][] visited;
    private List<Cell> bestPath;
    private List<Cell> currentPath;
    private Cell[][] uiGrid;
    private Consumer<Cell> exploreCallback;
     private static final int MAX_DEPTH = 200;

    public MaseSolverRecursivoCompleto() {}

    public MaseSolverRecursivoCompleto(Cell[][] uiGrid, Consumer<Cell> exploreCallback) {
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
        this.visited = new boolean[grid.length][grid[0].length];
        this.bestPath = new ArrayList<>();
        this.currentPath = new ArrayList<>();

        if (uiGrid != null && exploreCallback != null) {
            findAllPathsStep(grid, start.getRow(), start.getCol(), end);
        } else {
            findAllPaths(grid, start.getRow(), start.getCol(), end);
        }

        if (!bestPath.isEmpty()) {
            return new MazeResult(bestPath, null);
        } else {
            return new MazeResult(null, "No se encontró camino");
        }
    }

    private void findAllPaths(boolean[][] grid, int row, int col, Cell end) {
        if (!isValid(grid, row, col)) return;

        visited[row][col] = true;
        currentPath.add(new Cell(row, col));

        if (currentPath.size() > MAX_DEPTH) {
            visited[row][col] = false;
            currentPath.remove(currentPath.size() - 1);
            return;
        }

        if (!bestPath.isEmpty() && currentPath.size() >= bestPath.size()) {
            visited[row][col] = false;
            currentPath.remove(currentPath.size() - 1);
            return;
        }

        if (row == end.getRow() && col == end.getCol()) {
            if (bestPath.isEmpty() || currentPath.size() < bestPath.size()) {
                bestPath = new ArrayList<>(currentPath);
            }
        } else {
            int[][] directions = { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };
            for (int[] d : directions) {
                findAllPaths(grid, row + d[0], col + d[1], end);
            }
        }

        visited[row][col] = false;
        currentPath.remove(currentPath.size() - 1);
    }

    private void findAllPathsStep(boolean[][] grid, int row, int col, Cell end) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!isValid(grid, row, col)) return;

        visited[row][col] = true;
        currentPath.add(new Cell(row, col));

        if (!bestPath.isEmpty() && currentPath.size() >= bestPath.size()) {
        visited[row][col] = false;
        currentPath.remove(currentPath.size() - 1);
        return;
    }

        if (exploreCallback != null) {
            exploreCallback.accept(uiGrid[row][col]);
        }

        if (row == end.getRow() && col == end.getCol()) {
            if (bestPath.isEmpty() || currentPath.size() < bestPath.size()) {
                bestPath = new ArrayList<>(currentPath);
            }
        } else {
            int[][] directions = { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };
            for (int[] d : directions) {
                findAllPathsStep(grid, row + d[0], col + d[1], end);
            }
        }

        visited[row][col] = false;
        currentPath.remove(currentPath.size() - 1);
    }

    private boolean isValid(boolean[][] grid, int row, int col) {
        return row >= 0 && col >= 0 &&
               row < grid.length && col < grid[0].length &&
               grid[row][col] && !visited[row][col];
    }
}
