package view;

import dao.ConnexionDB;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Fenêtre principale de l'application.
 * Gère l'affichage, la navigation dynamique et le filtrage des accès par rôle.
 *
 * @author Abdoulaye Ousmane
 * @version 1.5
 */
public class DashboardFrame extends JFrame {

    private User userConnecte;
    private JPanel panelMenu;
    private JPanel panelContenu;

    /**
     * Construit le tableau de bord pour l'utilisateur authentifié.
     * * @param user L'instance de l'utilisateur connecté contenant son rôle et son nom.
     */
    public DashboardFrame(User user) {
        this.userConnecte = user;

        setTitle("Système Clinique - Tableau de Bord");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Écouteur pour fermer la connexion à la fermeture de la fenêtre
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConnexionDB.fermerConnexion();
            }
        });

        initialiserInterface();
    }

    /**
     * Configure et assemble les composants graphiques (header, menu, contenu).
     */
    private void initialiserInterface() {
        setLayout(new BorderLayout());

        // Header
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(43, 43, 66));
        panelHeader.setPreferredSize(new Dimension(1000, 60));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitre = new JLabel("Système Clinique — Espace " + userConnecte.getRole());
        lblTitre.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitre.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("Connecté : " + userConnecte.getUsername());
        lblUser.setFont(new Font("Arial", Font.ITALIC, 13));
        lblUser.setForeground(new Color(200, 200, 220));

        panelHeader.add(lblTitre, BorderLayout.WEST);
        panelHeader.add(lblUser, BorderLayout.EAST);

        // Menu latéral
        panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(new Color(30, 30, 47));
        panelMenu.setPreferredSize(new Dimension(220, 540));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        panelContenu = new JPanel(new BorderLayout());
        panelContenu.setBackground(Color.WHITE);

        construireMenuDeNavigation();

        add(panelHeader, BorderLayout.NORTH);
        add(panelMenu, BorderLayout.WEST);
        add(panelContenu, BorderLayout.CENTER);

        permuterVue(new PatientPanel(this));
    }

    /**
     * Construit le menu en filtrant les boutons selon le rôle de l'utilisateur.
     * Utilise les permissions pour décider quels accès afficher.
     */
    private void construireMenuDeNavigation() {
        String role = userConnecte.getRole();

        ajouterBoutonMenu("Gestion Patients", e -> permuterVue(new PatientPanel(this)));
        ajouterBoutonMenu("Rendez-vous", e -> permuterVue(new AppointmentPanel(this)));

        if ("Admin".equalsIgnoreCase(role)) {
            ajouterBoutonMenu("Gestion Médecins", e -> permuterVue(new DoctorPanel(this)));
        }

        if ("Admin".equalsIgnoreCase(role) || "Medecin".equalsIgnoreCase(role)) {
            ajouterBoutonMenu("Consultations", e -> permuterVue(new ConsultationPanel(this)));
        }

        panelMenu.add(Box.createVerticalGlue());
        ajouterBoutonMenu("Déconnexion", e -> deconnecter());
    }

    /**
     * Crée et ajoute un bouton stylisé au menu latéral.
     * * @param texte  Le libellé à afficher sur le bouton.
     * @param action L'action (ActionListener) à exécuter lors du clic.
     */
    private void ajouterBoutonMenu(String texte, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texte);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 30, 47));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(action);
        panelMenu.add(btn);
        panelMenu.add(Box.createVerticalStrut(10));
    }

    /**
     * Remplace le contenu central du tableau de bord par un nouveau panneau.
     * * @param nouveauPanneau Le JPanel à afficher au centre de l'interface.
     */
    public void permuterVue(JPanel nouveauPanneau) {
        panelContenu.removeAll();
        panelContenu.add(nouveauPanneau, BorderLayout.CENTER);
        panelContenu.revalidate();
        panelContenu.repaint();
    }

    /**
     * Demande confirmation puis ferme la session en cours.
     */
    private void deconnecter() {
        if (JOptionPane.showConfirmDialog(this, "Voulez-vous vous déconnecter ?", "Déconnexion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}