package controllers;

import models.Cell;
import models.MazeResult;

import java.util.*;
import java.util.function.Consumer;

public class MazeSolverBFS implements MazeSolver {
    private Cell[][] uiGrid;
    private Consumer<Cell> exploreCallback;

    public MazeSolverBFS() {}

    public MazeSolverBFS(Cell[][] uiGrid, Consumer<Cell> exploreCallback) {
        this.uiGrid = uiGrid;
        this.exploreCallback = exploreCallback;
    }

    @Override
    public MazeResult getPath(boolean[][] grid, Cell start, Cell end) {
        return getPath(grid, start, end, null);
    }

    @Override
    public MazeResult getPath(boolean[][] grid, Cell start, Cell end, Consumer<Cell> callback) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Map<Cell, Cell> parent = new HashMap<>();

        Queue<Cell> queue = new LinkedList<>();
        queue.add(start);
        visited[start.getRow()][start.getCol()] = true;

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            if (callback != null && uiGrid != null) {
                callback.accept(uiGrid[current.getRow()][current.getCol()]);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (current.equals(end)) {
                List<Cell> path = buildPath(parent, end);
                return new MazeResult(path, null);
            }

            for (int[] dir : directions) {
                int newRow = current.getRow() + dir[0];
                int newCol = current.getCol() + dir[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols &&
                        grid[newRow][newCol] && !visited[newRow][newCol]) {

                    Cell neighbor = new Cell(newRow, newCol);
                    queue.add(neighbor);
                    visited[newRow][newCol] = true;
                    parent.put(neighbor, current);
                }
            }
        }

        return new MazeResult(null, "No se encontró un camino");
    }

    private List<Cell> buildPath(Map<Cell, Cell> parent, Cell end) {
        List<Cell> path = new ArrayList<>();
        for (Cell at = end; at != null; at = parent.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }
}
