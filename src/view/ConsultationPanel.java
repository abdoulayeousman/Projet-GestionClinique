package view;

import dao.ConsultationDAO;
import model.Consultation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panneau d'interface graphique dédié à la gestion et au suivi des consultations médicales.
 * Permet l'affichage tabulaire, l'ajout, la modification et la suppression.
 *
 * @author Abdoulaye Ousmane
 * @version 1.5
 */
public class ConsultationPanel extends JPanel {

    // Couleurs de la charte graphique
    private final Color FOND_PANEL = new Color(245, 245, 250);
    private final Color BLEU_ACTION = new Color(0, 102, 153);
    private final Color ORANGE_MODIFIER = new Color(255, 153, 0);
    private final Color ROUGE_SUPPRIMER = new Color(200, 0, 0);
    private final Color VERT_SUCCES = new Color(0, 153, 76);

    private DefaultTableModel tableModel;
    private JTable table;
    private DashboardFrame dashboard;
    private final ConsultationDAO consultationDAO;  // ✅ Instance unique

    /**
     * Initialise le panneau de gestion des consultations.
     * @param dashboard La fenêtre de bord principale servant d'ancrage aux dialogues et à la navigation via permuterVue().
     */
    public ConsultationPanel(DashboardFrame dashboard) {
        this.dashboard = dashboard;
        this.consultationDAO = new ConsultationDAO();  // ✅ Créé UNE SEULE FOIS
        setLayout(new BorderLayout(10, 10));
        setBackground(FOND_PANEL);
        initComposants();
        chargerConsultations();
    }

    /**
     * Instancie et assemble les composants graphiques du panneau.
     */
    private void initComposants() {
        // ---- HEADER ----
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(FOND_PANEL);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JLabel lblTitre = new JLabel("📋 Gestion des Consultations");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitre.setForeground(BLEU_ACTION);
        panelHeader.add(lblTitre, BorderLayout.WEST);

        JButton btnAjout = creerBouton("+ Nouvelle Consultation", VERT_SUCCES);
        btnAjout.addActionListener(e -> ouvrirFormulaireAjout());
        panelHeader.add(btnAjout, BorderLayout.EAST);

        // ---- TABLEAU ----
        String[] colonnes = {"ID", "RDV ID", "Patient ID", "Médecin ID", "Date", "Diagnostic", "Ordonnance", "Statut"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        // ---- ACTIONS ----
        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelActions.setBackground(FOND_PANEL);

        JButton btnModifier = creerBouton("Modifier", ORANGE_MODIFIER);
        btnModifier.addActionListener(e -> modifierConsultation());

        JButton btnSupprimer = creerBouton("Supprimer", ROUGE_SUPPRIMER);
        btnSupprimer.addActionListener(e -> supprimerConsultation());

        panelActions.add(btnModifier);
        panelActions.add(btnSupprimer);

        add(panelHeader, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelActions, BorderLayout.SOUTH);
    }

    /**
     * Méthode utilitaire pour créer des boutons uniformes.
     * @param texte   Le libellé du bouton.
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Charge les données depuis le DAO vers le tableau.
     * ✅ Utilise l'instance unique consultationDAO
     */
    public void chargerConsultations() {
        tableModel.setRowCount(0);
        // ✅ CORRECTION : Utilise consultationDAO une seule fois
        for (Consultation c : consultationDAO.toutesLesConsultations()) {
            if (c != null) {  // ✅ Sécurité
                tableModel.addRow(new Object[]{
                        c.getConsultationId(), c.getAppointmentId(), c.getPatientId(),
                        c.getDoctorId(), c.getDateConsultation(), c.getDiagnostic(),
                        c.getOrdonnance(), c.getStatut()
                });
            }
        }
    }

    private void ouvrirFormulaireAjout() {
        new ConsultationFormFrame(dashboard, this, null).setVisible(true);
    }

    /**
     * Modifie la consultation sélectionnée.
     * ✅ CORRECTION : Utilise parId() au lieu de charger TOUTES les consultations O(n²) → O(1)
     */
    private void modifierConsultation() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une consultation !");
            return;
        }

        int id = (int) tableModel.getValueAt(table.convertRowIndexToModel(ligne), 0);
        // ✅ CORRECTION : Utilise parId() pour récupérer UNE SEULE consultation
        Consultation c = consultationDAO.parId(id);

        if (c != null) {
            new ConsultationFormFrame(dashboard, this, c).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Consultation non trouvée !");
        }
    }

    /**
     * Supprime la consultation sélectionnée.
     */
    private void supprimerConsultation() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une consultation !");
            return;
        }

        int id = (int) tableModel.getValueAt(table.convertRowIndexToModel(ligne), 0);
        if (JOptionPane.showConfirmDialog(this, "Supprimer la consultation ID : " + id + " ?") == JOptionPane.YES_OPTION) {
            // ✅ CORRECTION : Supprime juste la ligne
            if (consultationDAO.supprimer(id)) {
                tableModel.removeRow(ligne);
                JOptionPane.showMessageDialog(this, "Consultation supprimée avec succès.");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        }
    }
}