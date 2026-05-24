import view.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Lancer l'interface graphique
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}