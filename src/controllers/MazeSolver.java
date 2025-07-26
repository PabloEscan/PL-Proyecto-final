package controllers;

import models.Cell;
import models.MazeResult;

public interface MazeSolver {
    MazeResult getPath(boolean[][] grid, Cell start, Cell end);
}
