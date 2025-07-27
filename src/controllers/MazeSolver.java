package controllers;

import models.Cell;
import models.MazeResult;

import java.util.function.Consumer;

public interface MazeSolver {

    // Método básico sin callback para exploración paso a paso
    MazeResult getPath(boolean[][] grid, Cell start, Cell end);

    // Método con callback para reportar cada celda visitada (exploración paso a paso)
    MazeResult getPath(boolean[][] grid, Cell start, Cell end, Consumer<Cell> exploreCallback);
}
