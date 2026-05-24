package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre graphique de connexion ({@link JFrame}) pour l'application de gestion clinique.
 * Cette interface fournit les champs nécessaires à l'authentification des utilisateurs,
 * en ciblant principalement l'espace secrétariat. Elle gère la validation locale des saisies
 * ainsi que l'interconnexion avec la couche de persistance pour autoriser l'accès.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class LoginFrame extends JFrame {

    // ============ COMPOSANTS ============

    /** Champ de saisie pour l'identifiant (login) de l'utilisateur. */
    private JTextField txtUsername;

    /** Champ de saisie sécurisé pour le mot de passe. */
    private JPasswordField txtPassword;

    /** Bouton déclenchant l'action de vérification des identifiants. */
    private JButton btnLogin;

    /** Étiquette textuelle dynamique dédiée à l'affichage des alertes ou des confirmations. */
    private JLabel lblMessage;

    // ============ CONSTRUCTEUR ============

    /**
     * Initialise et configure les propriétés fondamentales de la fenêtre de connexion.
     * Définit le titre, le dimensionnement initial, le comportement à la fermeture,
     * positionne la fenêtre au centre de l'écran et lance la construction de l'interface.
     */
    public LoginFrame() {
        // Configuration de la fenêtre
        setTitle("Gestion Clinique - Connexion");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre
        setResizable(false);

        // Icône de l'application (Chemin absolu dynamique)
        try {
            String iconPath = System.getProperty("user.dir") + "/resources/icon.png";
            ImageIcon icon = new ImageIcon(iconPath);
            setIconImage(icon.getImage());
            System.out.println("Icône chargée depuis : " + iconPath);
        } catch (Exception e) {
            System.out.println("Erreur icône : " + e.getMessage());
        }

        // Construction de l'interface
        initComposants();
    }

    // ============ INTERFACE ============

    /**
     * Instancie et positionne les composants graphiques à l'intérieur de la fenêtre.
     * L'agencement utilise un {@link GridBagLayout} pour organiser les éléments de manière
     * équilibrée (titres, champs de texte et boutons) au sein d'un panneau principal gris clair.
     */
    private void initComposants() {

        // Panneau principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Titre
        JLabel lblTitre = new JLabel("🏥 Gestion Clinique");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitre.setForeground(new Color(0, 102, 153));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitre, gbc);

        // Sous-titre
        JLabel lblSousTitre = new JLabel("Espace Secrétaire");
        lblSousTitre.setFont(new Font("Arial", Font.ITALIC, 12));
        lblSousTitre.setForeground(Color.GRAY);
        gbc.gridy = 1;
        panel.add(lblSousTitre, gbc);

        // Label Username
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Login :"), gbc);

        // Champ Username
        txtUsername = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtUsername, gbc);

        // Label Password
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Mot de passe :"), gbc);

        // Champ Password
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtPassword, gbc);

        // Bouton Login amélioré
        btnLogin = new JButton("Se connecter");
        btnLogin.setBackground(new Color(0, 102, 153));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorderPainted(false);  // Supprime contour gris
        btnLogin.setFocusPainted(false);   // Supprime pointillé au clic
        btnLogin.setOpaque(true);          // Couleur de fond visible
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnLogin, gbc);

        // Message erreur/succès
        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("Arial", Font.ITALIC, 11));
        lblMessage.setForeground(Color.RED);
        gbc.gridy = 5;
        panel.add(lblMessage, gbc);

        // Action bouton login
        btnLogin.addActionListener(e -> verifierLogin());

        // Action touche Entrée sur le champ password
        txtPassword.addActionListener(e -> verifierLogin());

        add(panel);
    }

    // ============ LOGIQUE LOGIN ============

    /**
     * Extrait les informations saisies et pilote le processus d'authentification.
     * La procédure effectue d'abord un contrôle de surface pour bloquer les champs vides.
     * Elle interroge ensuite le {@link UserDAO} pour confronter les identifiants à la base de données.
     * <ul>
     *   <li>Si l'accès est validé : la fenêtre de login est détruite et le tableau de bord principal s'ouvre.</li>
     *   <li>Si l'accès est refusé : un message d'erreur s'affiche et le champ du mot de passe est réinitialisé.</li>
     * </ul>
     */
    private void verifierLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // Vérification champs vides
        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Veuillez remplir tous les champs !");
            return;
        }

        // Vérification en base de données
        UserDAO userDAO = new UserDAO();
        User user = userDAO.login(username, password);

        if (user != null) {
            lblMessage.setForeground(new Color(0, 153, 0));
            lblMessage.setText("Connexion réussie !");

            // Fermer la fenêtre de login
            dispose();

            // Ouvrir le tableau de bord
            new DashboardFrame(user).setVisible(true);

        } else {
            lblMessage.setForeground(Color.RED);
            lblMessage.setText("Login ou mot de passe incorrect !");
            txtPassword.setText("");
        }
    }
}