package view;

import dao.AppointmentDAO;
import model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panneau d'interface graphique pour la gestion des rendez-vous.
 * Organise l'affichage, la recherche et les actions (confirmation, annulation, suppression).
 * * @author Abdoulaye Ousmane
 * @version 1.5
 */
public class AppointmentPanel extends JPanel {

    // Couleurs de la charte graphique
    private final Color FOND_PANEL = new Color(245, 245, 250);
    private final Color BLEU_ACTION = new Color(0, 102, 153);
    private final Color VERT_SUCCES = new Color(0, 153, 76);
    private final Color ROUGE_SUPPRIMER = new Color(200, 0, 0);

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtRecherche;
    private JButton btnNouveauRDV, btnConfirmer, btnAnnuler, btnSupprimer;
    private DashboardFrame dashboard;
    private final AppointmentDAO appointmentDAO;

    /**
     * Initialise le panneau avec la référence du tableau de bord.
     * @param dashboard La fenêtre principale pour la navigation via permuterVue().
     */
    public AppointmentPanel(DashboardFrame dashboard) {
        this.dashboard = dashboard;
        this.appointmentDAO = new AppointmentDAO();
        initialiserComposants();
        chargerRendezVous();
    }

    /**
     * Configure la disposition en BorderLayout : recherche en haut,
     * tableau au centre, et boutons d'action stylisés en bas à droite.
     */
    private void initialiserComposants() {
        setLayout(new BorderLayout(10, 10));
        setBackground(FOND_PANEL);

        // Panneau supérieur : Filtre de recherche
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelTop.setBackground(FOND_PANEL);
        txtRecherche = new JTextField(20);
        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.setBackground(BLEU_ACTION);
        btnRechercher.setForeground(Color.WHITE);
        btnRechercher.addActionListener(e -> chargerRendezVous());
        panelTop.add(new JLabel("Nom du Patient :"));
        panelTop.add(txtRecherche);
        panelTop.add(btnRechercher);

        // Tableau des données
        String[] colonnes = {"ID", "Patient ID", "Médecin ID", "Date", "Heure", "Type", "Statut", "Paiement", "N° Reçu"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panneau d'actions
        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelActions.setBackground(FOND_PANEL);

        btnNouveauRDV = creerBouton("+ Nouveau RDV", BLEU_ACTION);
        btnConfirmer = creerBouton("Confirmer", VERT_SUCCES);
        btnAnnuler = creerBouton("Annuler", Color.GRAY);
        btnSupprimer = creerBouton("Supprimer", ROUGE_SUPPRIMER);

        panelActions.add(btnNouveauRDV);
        panelActions.add(btnConfirmer);
        panelActions.add(btnAnnuler);
        panelActions.add(btnSupprimer);

        // Événements
        btnNouveauRDV.addActionListener(e -> new AppointmentFormFrame(dashboard, this, null).setVisible(true));
        btnConfirmer.addActionListener(e -> modifierStatutSelection("Completed"));
        btnAnnuler.addActionListener(e -> modifierStatutSelection("Cancelled"));
        btnSupprimer.addActionListener(e -> supprimerRDV());

        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelActions, BorderLayout.SOUTH);
    }

    /**
     * Méthode utilitaire pour créer des boutons uniformes.
     * @param texte   Le libellé du bouton.
     * @param couleur La couleur de fond du bouton.
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
     * Extrait les enregistrements et met à jour la table.
     */
    public void chargerRendezVous() {
        String recherche = txtRecherche.getText().trim();
        List<Appointment> liste = recherche.isEmpty() ? appointmentDAO.tousLesRendezVous() : appointmentDAO.rechercherParPatient(recherche);
        tableModel.setRowCount(0);
        for (Appointment a : liste) {
            tableModel.addRow(new Object[]{a.getAppointmentId(), a.getPatientId(), a.getDoctorId(), a.getAppointmentDate(), a.getAppointmentTime(), a.getTypeConsultation(), a.getStatus(), a.getStatutPaiement(), a.getNumeroRecu()});
        }
    }

    /**
     * Modifie l'état opérationnel du rendez-vous sélectionné.
     * @param nouveauStatut Le statut à appliquer (ex: "Completed", "Cancelled").
     */
    private void modifierStatutSelection(String nouveauStatut) {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rendez-vous.");
            return;
        }
        int id = (int) tableModel.getValueAt(table.convertRowIndexToModel(ligne), 0);
        Appointment cible = appointmentDAO.tousLesRendezVous().stream().filter(a -> a.getAppointmentId() == id).findFirst().orElse(null);
        if (cible != null) {
            cible.setStatus(nouveauStatut);
            if (appointmentDAO.modifier(cible)) chargerRendezVous();
        }
    }

    /**
     * Supprime définitivement l'enregistrement sélectionné après confirmation.
     */
    private void supprimerRDV() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rendez-vous.");
            return;
        }
        int id = (int) tableModel.getValueAt(table.convertRowIndexToModel(ligne), 0);
        if (JOptionPane.showConfirmDialog(this, "Supprimer le RDV ID : " + id + " ?") == JOptionPane.YES_OPTION) {
            if (appointmentDAO.supprimer(id)) chargerRendezVous();
        }
    }
}