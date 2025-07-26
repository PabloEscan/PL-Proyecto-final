import view.MatrixUI;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MatrixUI ui = new MatrixUI();
            ui.setVisible(true);
        });
    }
}
