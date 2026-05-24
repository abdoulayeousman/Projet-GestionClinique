package view;

import dao.DoctorDAO;
import model.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panneau d'interface graphique ({@link JPanel}) dédié à la gestion des médecins.
 * Permet l'affichage, la recherche, l'ajout, la modification et la suppression
 * des profils des professionnels de santé.
 * * @author Abdoulaye Ousmane
 * @version 1.5
 */
public class DoctorPanel extends JPanel {

    // Couleurs de la charte graphique
    private final Color FOND_PANEL = new Color(245, 245, 250);
    private final Color BLEU_ACTION = new Color(0, 102, 153);
    private final Color ORANGE_MODIFIER = new Color(255, 153, 0);
    private final Color ROUGE_SUPPRIMER = new Color(200, 0, 0);

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtRecherche;
    private JButton btnNouveauMedecin, btnModifier, btnSupprimer;
    private DashboardFrame dashboard;
    private final DoctorDAO doctorDAO;

    public DoctorPanel(DashboardFrame dashboard) {
        this.dashboard = dashboard;
        this.doctorDAO = new DoctorDAO();
        initialiserComposants();
        rechercherMedecin();
    }

    private void initialiserComposants() {
        setLayout(new BorderLayout(10, 10));
        setBackground(FOND_PANEL);

        // --- Zone de recherche (NORTH) ---
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelTop.setBackground(FOND_PANEL);

        txtRecherche = new JTextField(20);

        // Configuration du bouton Rechercher en bleu
        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.setBackground(BLEU_ACTION);
        btnRechercher.setForeground(Color.WHITE);
        btnRechercher.setFocusPainted(false);
        btnRechercher.setBorderPainted(false);
        btnRechercher.setOpaque(true);

        btnRechercher.addActionListener(e -> rechercherMedecin());

        panelTop.add(new JLabel("Nom du médecin :"));
        panelTop.add(txtRecherche);
        panelTop.add(btnRechercher);

        // --- Grille de données (CENTER) ---
        String[] colonnes = {"ID", "Nom Complet", "Téléphone", "Spécialité", "Grade", "Service", "Statut"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // --- Panneau d'actions (SOUTH) ---
        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelActions.setBackground(FOND_PANEL);

        btnNouveauMedecin = creerBouton("+ Nouveau Médecin", BLEU_ACTION);
        btnModifier = creerBouton("Modifier", ORANGE_MODIFIER);
        btnSupprimer = creerBouton("Supprimer", ROUGE_SUPPRIMER);

        panelActions.add(btnNouveauMedecin);
        panelActions.add(btnModifier);
        panelActions.add(btnSupprimer);

        // --- Événements ---
        btnNouveauMedecin.addActionListener(e -> new DoctorFormFrame(dashboard, this, null).setVisible(true));
        btnModifier.addActionListener(e -> ouvrirFormulaireModification());
        btnSupprimer.addActionListener(e -> supprimerMedecin());

        // --- Assemblage final ---
        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelActions, BorderLayout.SOUTH);
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        return btn;
    }

    public void rechercherMedecin() {
        String recherche = txtRecherche.getText().trim();
        List<Doctor> liste = recherche.isEmpty() ? doctorDAO.tousLesMedecins() : doctorDAO.rechercherParNom(recherche);

        tableModel.setRowCount(0);
        for (Doctor d : liste) {
            if (d != null) {
                tableModel.addRow(new Object[]{
                        d.getDoctorId(), d.getFullName(), d.getPhoneNumber(),
                        d.getSpecialization(), d.getGrade(), d.getService(), d.getStatut()
                });
            }
        }
    }

    private void ouvrirFormulaireModification() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médecin à modifier.", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(ligne, 0);
        Doctor d = doctorDAO.trouverParId(id);

        if (d != null) {
            new DoctorFormFrame(dashboard, this, d).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Médecin non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerMedecin() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médecin à supprimer.", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(ligne, 0);
        int choix = JOptionPane.showConfirmDialog(this, "Confirmez-vous la suppression du médecin ID : " + id + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            if (doctorDAO.supprimer(id)) {
                tableModel.removeRow(ligne);
                JOptionPane.showMessageDialog(this, "Profil supprimé avec succès.");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}