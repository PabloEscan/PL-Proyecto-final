package models;

import java.awt.Color;
import javax.swing.JPanel;

public class Cell extends JPanel {
    private int row, col;
    private boolean isWall;
    private boolean isStart;
    private boolean isEnd;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.isWall = false;
        this.isStart = false;
        this.isEnd = false;
        setBackground(Color.LIGHT_GRAY);
        setBorder(javax.swing.BorderFactory.createLineBorder(Color.DARK_GRAY));
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public boolean isWall() { return isWall; }
    public void setWall(boolean wall) {
        isWall = wall;
        if (wall) setBackground(Color.BLACK);
        else if (!isStart && !isEnd) setBackground(Color.LIGHT_GRAY);
    }

    public void setStart(boolean start) {
        isStart = start;
        if (start) setBackground(Color.RED);
        else if (!isWall && !isEnd) setBackground(Color.LIGHT_GRAY);
    }

    public void setEnd(boolean end) {
        isEnd = end;
        if (end) setBackground(Color.RED);
        else if (!isWall && !isStart) setBackground(Color.LIGHT_GRAY);
    }

    public boolean isStart() { return isStart; }
    public boolean isEnd() { return isEnd; }
}
