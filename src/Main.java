import view.LoginFrame;
import javax.swing.*;

/**
 * Point d'entrée de l'application Gestion Clinique.
 * Lance la fenêtre de connexion sur le thread EDT de Swing.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        // ✅ Lancer l'interface graphique sur le thread EDT
        SwingUtilities.invokeLater(() -> {
            try {
                // ✅ Définir le Look and Feel natif du système
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("[Main] Erreur Look and Feel : " + e.getMessage());
            }

            // Afficher la fenêtre de connexion
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            System.out.println("[Main] Application démarrée avec succès");
        });
    }
}