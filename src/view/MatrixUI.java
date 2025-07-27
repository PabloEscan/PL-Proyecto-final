package view;

import controllers.*;
import models.Cell;
import models.MazeResult;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class MatrixUI extends JFrame {
    private JPanel matrixPanel;
    private JComboBox<String> algoSelector;

    private Cell[][] cellGrid;
    private String currentMode = "wall";
    private Cell startCell = null;
    private Cell endCell = null;

    private List<Cell> pasoACaminar = new java.util.ArrayList<>();
    private int pasoIndex = 0;

    public MatrixUI() {
        super("Interfaz Gráfica de Matriz");
        initMenuBar();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initNorthPanel();
        initMatrixPanel();
        initSouthPanel();

        createMatrixDialog();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu herramientas = new JMenu("Herramientas");
        JMenuItem resetItem = new JMenuItem("Reinicio");
        resetItem.addActionListener(e -> createMatrixDialog());
        JMenuItem salirItem = new JMenuItem("Salir");
        salirItem.addActionListener(e -> System.exit(0));
        herramientas.add(resetItem);
        herramientas.add(salirItem);
        menuBar.add(herramientas);

        JMenu ayudaMenu = new JMenu("Ayuda");
        JMenuItem verAyudaItem = new JMenuItem("Ver ayuda");
        verAyudaItem.addActionListener(e -> {
            String mensajeHTML =
                "<html><body style='width: 300px;'>"
                + "<h2>Autores</h2>"
                + "<ul>"
                + "<li><b>Jaime Ismael Loja Tenesaca</b><br>"
                + "<font color='blue'>jlojat2@est.ups.edu.ec</font></li><br>"
                + "<li><b>Guillermo Daniel Cajas Ortega</b><br>"
                + "<font color='blue'>gcajaso@est.ups.edu.ec</font></li><br>"
                + "<li><b>Pablo Esteban Escandón Lema</b><br><font color='blue'>pescandonl@est.ups.edu.ec</font></li><br>"
                + "<li><b>Kevin Andres Paladines Toledo</b><br><font color='blue'>Kpaladinest@est.ups.edu.ec </font></li>"
                + "</ul></body></html>";

            JOptionPane.showMessageDialog(
                this,
                mensajeHTML,
                "Créditos / Ayuda",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
        ayudaMenu.add(verAyudaItem);
        menuBar.add(ayudaMenu);

        setJMenuBar(menuBar);
    }

    private void initNorthPanel() {
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton setStartButton = new JButton("Set Start");
        JButton setEndButton = new JButton("Set End");
        JButton toggleWallButton = new JButton("Toggle Wall");

        setStartButton.addActionListener(e -> currentMode = "start");
        setEndButton.addActionListener(e -> currentMode = "end");
        toggleWallButton.addActionListener(e -> currentMode = "wall");

        topButtonPanel.add(setStartButton);
        topButtonPanel.add(setEndButton);
        topButtonPanel.add(toggleWallButton);

        add(topButtonPanel, BorderLayout.NORTH);
    }

    private void initMatrixPanel() {
        matrixPanel = new JPanel();
        matrixPanel.setBackground(Color.WHITE);
        add(matrixPanel, BorderLayout.CENTER);
    }

    private void initSouthPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        String[] algorithms = {
                "Recursivo",
                "Recursivo completo",
                "Recursivo completo BT",
                "BFS",
                "DFS"
        };
        algoSelector = new JComboBox<>(algorithms);

        JButton solveButton = new JButton("Resolver");
        JButton stepButton = new JButton("Paso a paso");
        JButton clearButton = new JButton("Limpiar");

        solveButton.addActionListener(e -> resolverCamino());
        stepButton.addActionListener(e -> avanzarPasoManual());
        clearButton.addActionListener(e -> clearMatrix());

        bottomPanel.add(algoSelector);
        bottomPanel.add(solveButton);
        bottomPanel.add(stepButton);
        bottomPanel.add(clearButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createMatrixDialog() {
        JTextField rowsField = new JTextField(5);
        JTextField colsField = new JTextField(5);

        JPanel dialogPanel = new JPanel();
        dialogPanel.add(new JLabel("Filas:"));
        dialogPanel.add(rowsField);
        dialogPanel.add(Box.createHorizontalStrut(15));
        dialogPanel.add(new JLabel("Columnas:"));
        dialogPanel.add(colsField);

        int result = JOptionPane.showConfirmDialog(
                this, dialogPanel,
                "Ingrese dimensiones de la matriz",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int rows = Integer.parseInt(rowsField.getText());
                int cols = Integer.parseInt(colsField.getText());

                if (rows > 0 && cols > 0) {
                    createMatrix(rows, cols);
                } else {
                    showError("Ingrese valores positivos.");
                    createMatrixDialog();
                }
            } catch (NumberFormatException e) {
                showError("Ingrese números válidos.");
                createMatrixDialog();
            }
        }
    }

    private void createMatrix(int rows, int cols) {
        matrixPanel.removeAll();
        matrixPanel.setLayout(new GridLayout(rows, cols, 2, 2));
        cellGrid = new Cell[rows][cols];

        startCell = null;
        endCell = null;
        currentMode = "wall";

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = new Cell(r, c);
                cell.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        switch (currentMode) {
                            case "wall":
                                cell.setWall(!cell.isWall());
                                break;
                            case "start":
                                if (startCell != null) {
                                    startCell.setStart(false);
                                    startCell.setBackground(Color.LIGHT_GRAY);
                                }
                                startCell = cell;
                                cell.setStart(true);
                                break;
                            case "end":
                                if (endCell != null) {
                                    endCell.setEnd(false);
                                    endCell.setBackground(Color.LIGHT_GRAY);
                                }
                                endCell = cell;
                                cell.setEnd(true);
                                break;
                        }
                    }
                });
                cellGrid[r][c] = cell;
                matrixPanel.add(cell);
            }
        }

        matrixPanel.revalidate();
        matrixPanel.repaint();
    }

    private void clearMatrix() {
        for (int r = 0; r < cellGrid.length; r++) {
            for (int c = 0; c < cellGrid[0].length; c++) {
                cellGrid[r][c].setWall(false);
                cellGrid[r][c].setStart(false);
                cellGrid[r][c].setEnd(false);
                cellGrid[r][c].setBackground(Color.LIGHT_GRAY);
            }
        }
        startCell = null;
        endCell = null;
        pasoACaminar.clear();
        pasoIndex = 0;
    }

    private void resolverCamino() {
        if (startCell == null || endCell == null) {
            showError("Seleccione punto de inicio y fin.");
            return;
        }

        boolean[][] grid = new boolean[cellGrid.length][cellGrid[0].length];
        for (int r = 0; r < cellGrid.length; r++) {
            for (int c = 0; c < cellGrid[0].length; c++) {
                grid[r][c] = !cellGrid[r][c].isWall();
            }
        }

        setControlsEnabled(false);

        new SwingWorker<MazeResult, Cell>() {
            @Override
            protected MazeResult doInBackground() {
                Consumer<Cell> consumer = cell -> publish(cell);
                MazeSolver solver = getSelectedSolver(consumer);
                return solver.getPath(grid, startCell, endCell, consumer);
            }

            @Override
            protected void process(List<Cell> chunks) {
                for (Cell c : chunks) {
                    if (!c.isStart() && !c.isEnd()) {
                        cellGrid[c.getRow()][c.getCol()].setBackground(Color.BLUE); // pasos visitados
                    }
                }
            }

            @Override
            protected void done() {
                try {
                    MazeResult result = get();
                    if (result.getPath() != null && !result.getPath().isEmpty()) {
                        for (Cell pathCell : result.getPath()) {
                            int r = pathCell.getRow();
                            int c = pathCell.getCol();
                            if (!cellGrid[r][c].isStart() && !cellGrid[r][c].isEnd()) {
                                cellGrid[r][c].setBackground(Color.GREEN); // camino final
                            }
                        }
                    } else {
                        showError(result.getError() != null ? result.getError() : "No se encontró un camino.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error inesperado durante la solución.");
                } finally {
                    setControlsEnabled(true);
                }
            }
        }.execute();
    }

    private void avanzarPasoManual() {
        if (pasoACaminar.isEmpty()) {
            if (startCell == null || endCell == null) {
                showError("Seleccione punto de inicio y fin.");
                return;
            }

            boolean[][] grid = new boolean[cellGrid.length][cellGrid[0].length];
            for (int r = 0; r < cellGrid.length; r++) {
                for (int c = 0; c < cellGrid[0].length; c++) {
                    grid[r][c] = !cellGrid[r][c].isWall();
                }
            }

            MazeSolver solver = getSelectedSolver(null);
            MazeResult result = solver.getPath(grid, startCell, endCell);

            if (result.getPath() != null && !result.getPath().isEmpty()) {
                pasoACaminar = result.getPath();
                pasoIndex = 0;
            } else {
                showError(result.getError() != null ? result.getError() : "No se encontró un camino.");
                return;
            }
        }

        if (pasoIndex < pasoACaminar.size()) {
            Cell c = pasoACaminar.get(pasoIndex++);
            if (!c.equals(startCell) && !c.equals(endCell)) {
                cellGrid[c.getRow()][c.getCol()].setBackground(Color.GREEN);
            }
        }
    }

    private MazeSolver getSelectedSolver(Consumer<Cell> callback) {
        String selected = (String) algoSelector.getSelectedItem();
        switch (selected) {
            case "BFS":
                return new MazeSolverBFS(cellGrid, callback);
            case "DFS":
                return new MazeSolverDFS(cellGrid, callback);
            case "Recursivo":
                return new MaseSolverRecursivo();
            case "Recursivo completo":
                return new MaseSolverRecursivoCompleto(cellGrid, callback);
            case "Recursivo completo BT":
                return new MazeSolverRecursivoBT(cellGrid, callback);
            default:
                return new MaseSolverRecursivo();
        }
    }

    private void setControlsEnabled(boolean enabled) {
        for (Component comp : ((JPanel)getContentPane().getComponent(0)).getComponents()) {
            comp.setEnabled(enabled);
        }
        for (Component comp : ((JPanel)getContentPane().getComponent(2)).getComponents()) {
            comp.setEnabled(enabled);
        }
        algoSelector.setEnabled(enabled);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
