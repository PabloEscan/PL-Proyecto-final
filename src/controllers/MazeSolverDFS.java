package controllers;

import models.Cell;
import models.MazeResult;
import java.util.*;
import java.util.function.Consumer;

public class MazeSolverDFS implements MazeSolver {
    private final Cell[][] cellGrid;
    private final Consumer<Cell> callback;

    public MazeSolverDFS(Cell[][] cellGrid, Consumer<Cell> callback) {
        this.cellGrid = cellGrid;
        this.callback = callback;
    }

    @Override
    public MazeResult getPath(boolean[][] grid, Cell start, Cell end) {
        return getPath(grid, start, end, callback);
    }

    @Override
    public MazeResult getPath(boolean[][] grid, Cell start, Cell end, Consumer<Cell> callback) {
        int rows = grid.length;
        int cols = grid[0].length;

        boolean[][] visited = new boolean[rows][cols];
        Map<Cell, Cell> cameFrom = new HashMap<>();
        Stack<Cell> stack = new Stack<>();
        stack.push(start);
        visited[start.getRow()][start.getCol()] = true;

        while (!stack.isEmpty()) {
            Cell current = stack.pop();

            if (callback != null) {
                callback.accept(current);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (current.equals(end)) break;

            for (Cell neighbor : getNeighbors(current, grid)) {
                int r = neighbor.getRow();
                int c = neighbor.getCol();
                if (!visited[r][c]) {
                    visited[r][c] = true;
                    stack.push(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        // Reconstruir el camino
        List<Cell> path = new ArrayList<>();
        Cell step = end;
        while (step != null && cameFrom.containsKey(step)) {
            path.add(step);
            step = cameFrom.get(step);
        }

        if (step == null || !step.equals(start)) {
            return new MazeResult(null, "No se encontró un camino.");
        }

        path.add(start);
        Collections.reverse(path);
        return new MazeResult(path, null);
    }

    private List<Cell> getNeighbors(Cell cell, boolean[][] grid) {
        List<Cell> neighbors = new ArrayList<>();
        int r = cell.getRow();
        int c = cell.getCol();
        int rows = grid.length;
        int cols = grid[0].length;

        int[][] directions = { {1,0}, {-1,0}, {0,1}, {0,-1} };

        for (int[] dir : directions) {
            int nr = r + dir[0];
            int nc = c + dir[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc]) {
                neighbors.add(new Cell(nr, nc));
            }
        }
        return neighbors;
    }
}