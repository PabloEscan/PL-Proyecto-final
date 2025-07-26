package models;

import java.util.List;

public class MazeResult {
    private List<Cell> path;
    private String error;

    public MazeResult(List<Cell> path, String error) {
        this.path = path;
        this.error = error;
    }

    public List<Cell> getPath() {
        return path;
    }

    public String getError() {
        return error;
    }
}
