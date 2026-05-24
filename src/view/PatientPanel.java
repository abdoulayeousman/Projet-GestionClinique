package view;

import dao.PatientDAO;
import model.Patient;
import java.time.LocalDate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panneau d'interface graphique ({@link JPanel}) dédié à la gestion des patients.
 * Permet l'affichage, la recherche et les actions CRUD via des formulaires dédiés.
 *
 * @author Abdoulaye Ousmane
 * @version 1.5
 */
public class PatientPanel extends JPanel {

    // --- Constantes de couleurs ---
    private final Color FOND_PANEL = new Color(245, 245, 250);
    private final Color BLEU_ACTION = new Color(0, 102, 153);
    private final Color VERT_SUCCES = new Color(0, 153, 76);
    private final Color ORANGE_MODIFIER = new Color(255, 153, 0);
    private final Color ROUGE_SUPPRIMER = new Color(200, 0, 0);

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtRecherche;
    private DashboardFrame dashboard;

    /**
     * Initialise le panneau de gestion des patients.
     * @param dashboard La fenêtre principale pour naviguer entre les vues.
     */
    public PatientPanel(DashboardFrame dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout(10, 10));
        setBackground(FOND_PANEL);
        initComposants();
        chargerPatients();
    }

    /**
     * Configure et assemble graphiquement les composants.
     */
    private void initComposants() {
        // ---- HEADER & RECHERCHE ----
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(FOND_PANEL);

        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelRecherche.setBackground(FOND_PANEL);

        txtRecherche = new JTextField(20);
        JButton btnRechercher = creerBouton("Rechercher", BLEU_ACTION);
        btnRechercher.addActionListener(e -> rechercherPatient());

        panelRecherche.add(new JLabel("Nom du Patient :"));
        panelRecherche.add(txtRecherche);
        panelRecherche.add(btnRechercher);

        JButton btnNouveau = creerBouton("+ Nouveau Patient", VERT_SUCCES);
        btnNouveau.addActionListener(e -> ouvrirFormulaireAjout());

        panelTop.add(panelRecherche, BorderLayout.WEST);
        panelTop.add(btnNouveau, BorderLayout.EAST);

        // ---- TABLEAU ----
        String[] colonnes = {"ID", "Nom complet", "Date naissance", "Sexe", "Téléphone", "Service", "Pays"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // ---- ACTIONS ----
        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelActions.setBackground(FOND_PANEL);

        JButton btnModifier = creerBouton("Modifier", ORANGE_MODIFIER);
        btnModifier.addActionListener(e -> modifierPatient());

        JButton btnSupprimer = creerBouton("Supprimer", ROUGE_SUPPRIMER);
        btnSupprimer.addActionListener(e -> supprimerPatient());

        panelActions.add(btnModifier);
        panelActions.add(btnSupprimer);

        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelActions, BorderLayout.SOUTH);
    }

    /**
     * Méthode utilitaire pour créer des boutons uniformes.
     * @param texte   Le texte du bouton.
     * @param couleur La couleur de fond.
     * @return Le bouton configuré.
     */
    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        return btn;
    }

    /**
     * Charge tous les patients depuis la base de données.
     */
    public void chargerPatients() {
        tableModel.setRowCount(0);
        for (Patient p : new PatientDAO().tousLesPatients()) {
            ajouterLigneTableau(p);
        }
    }

    /**
     * Filtre les patients par nom.
     */
    public void rechercherPatient() {
        String recherche = txtRecherche.getText().trim();
        tableModel.setRowCount(0);
        List<Patient> patients = recherche.isEmpty() ? new PatientDAO().tousLesPatients() : new PatientDAO().rechercherParNom(recherche);
        for (Patient p : patients) {
            ajouterLigneTableau(p);
        }
    }

    /**
     * Ajoute un patient au modèle du tableau.
     * @param p L'objet Patient à afficher.
     */
    private void ajouterLigneTableau(Patient p) {
        tableModel.addRow(new Object[]{p.getPatientId(), p.getNomComplet(), p.getDateNaissance(), p.getSexe(), p.getTelephone(), p.getService(), p.getPays()});
    }

    /**
     * Ouvre le formulaire d'ajout.
     */
    private void ouvrirFormulaireAjout() {
        new PatientFormFrame(dashboard, this, null).setVisible(true);
    }

    /**
     * Ouvre le formulaire de modification pour le patient sélectionné.
     */
    private void modifierPatient() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un patient !");
            return;
        }
        // ... (Logique de récupération patient inchangée)
        int id = (int) tableModel.getValueAt(ligne, 0);
        Patient p = new PatientDAO().trouverParId(id);
        new PatientFormFrame(dashboard, this, p).setVisible(true);
    }

    /**
     * Supprime le patient sélectionné après confirmation.
     */
    private void supprimerPatient() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un patient !");
            return;
        }
        int id = (int) tableModel.getValueAt(ligne, 0);
        if (JOptionPane.showConfirmDialog(this, "Supprimer le patient ID : " + id + " ?") == JOptionPane.YES_OPTION) {
            if (new PatientDAO().supprimer(id)) chargerPatients();
        }
    }
}