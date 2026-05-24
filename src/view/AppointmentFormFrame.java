package view;

import dao.AppointmentDAO;
import dao.PatientDAO;
import dao.DoctorDAO;
import model.Appointment;
import model.Patient;
import model.Doctor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Fenêtre modale de formulaire pour la création et la modification des rendez-vous.
 * Cette classe gère le chargement dynamique des listes de patients et médecins
 * depuis la base de données MySQL et applique les validations métiers nécessaires
 * (formats de date, heure, prix, champs obligatoires) avant l'envoi au DAO.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class AppointmentFormFrame extends JDialog {

    /** Liste déroulante pour la sélection du patient */
    private JComboBox<String> comboPatient;

    /** Liste déroulante pour la sélection du médecin */
    private JComboBox<String> comboMedecin;

    /** Champ de saisie pour la date de consultation (Format: JJ/MM/AAAA) */
    private JTextField txtDate;

    /** Champ de saisie pour l'heure de consultation (Format: HH:MM) */
    private JTextField txtHeure;

    /** Liste déroulante pour le type de consultation (Première visite, Suivi, Urgence) */
    private JComboBox<String> comboType;

    /** Champ de saisie pour le motif de la visite médicale */
    private JTextField txtMotif;

    /** Champ de saisie pour le prix de la consultation en FCFA */
    private JTextField txtPrix;

    /** Liste déroulante pour le mode de paiement */
    private JComboBox<String> comboPaiement;

    /** Liste déroulante pour le suivi du statut du paiement */
    private JComboBox<String> comboStatutPaiement;

    /** Liste déroulante pour le statut global du rendez-vous */
    private JComboBox<String> comboStatut;

    /** Champ de saisie pour la référence ou le numéro de reçu d'encaissement */
    private JTextField txtNumeroRecu;

    /** Bouton de validation et d'enregistrement des données */
    private JButton btnEnregistrer;

    /** Bouton d'annulation et de fermeture de la boîte de dialogue */
    private JButton btnAnnuler;

    /** Instance de rendez-vous existante (vaut null en mode Ajout, contient l'objet en mode Modification) */
    private Appointment appointmentExistant;

    /** Référence vers le panneau de gestion parent pour rafraîchir la liste après traitement */
    private AppointmentPanel parentPanel;

    /** Liste mémoire des patients récupérés depuis la base de données */
    private List<Patient> listePatients;

    /** Liste mémoire des médecins récupérés depuis la base de données */
    private List<Doctor> listeMedecins;

    /** Formateur standardisé pour la validation et l'affichage des dates */
    private final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Formateur standardisé pour la validation et l'affichage des heures */
    private final DateTimeFormatter formatterHeure = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Construit une nouvelle fenêtre de formulaire pour les rendez-vous.
     *
     * @param parent La fenêtre de l'application parente pour la gestion du focus modal.
     * @param parentPanel Le panneau d'affichage d'origine nécessitant une mise à jour suite à l'action.
     * @param appointment L'entité à modifier, ou null s'il s'agit d'une création de rendez-vous.
     */
    public AppointmentFormFrame(JFrame parent, AppointmentPanel parentPanel, Appointment appointment) {
        super(parent, "Formulaire Rendez-vous", true);
        this.parentPanel = parentPanel;
        this.appointmentExistant = appointment;

        setSize(500, 520);
        setLocationRelativeTo(parent);
        setResizable(false);

        chargerDonnees();
        initComposants();

        if (appointmentExistant != null) {
            setTitle("✏️ Modifier le Rendez-vous");
            remplirChamps();
        } else {
            setTitle("➕ Nouveau Rendez-vous");
        }
    }

    /**
     * Charge l'ensemble des patients et des médecins disponibles en base de données
     * afin de peupler les composants de sélection graphique.
     */
    private void chargerDonnees() {
        listePatients = new PatientDAO().tousLesPatients();
        listeMedecins = new DoctorDAO().tousLesMedecins();
    }

    /**
     * Initialise, configure et positionne graphiquement les éléments de l'interface (Swing).
     * Utilise un agencement de type {@link GridBagLayout} pour garantir un alignement rigoureux des champs.
     */
    private void initComposants() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuration des listes déroulantes
        comboPatient = new JComboBox<>();
        comboPatient.addItem("-- Sélectionner un patient --");
        for (Patient p : listePatients) {
            comboPatient.addItem(p.getPatientId() + " - " + p.getNomComplet());
        }

        comboMedecin = new JComboBox<>();
        comboMedecin.addItem("-- Sélectionner un médecin --");
        for (Doctor d : listeMedecins) {
            comboMedecin.addItem(d.getDoctorId() + " - " + d.getFullName() + " (" + d.getSpecialization() + ")");
        }

        // Instanciation des champs de texte et options
        txtDate = new JTextField(20);
        txtDate.setToolTipText("Format: JJ/MM/AAAA ex: 22/05/2026");

        txtHeure = new JTextField(20);
        txtHeure.setToolTipText("Format: HH:MM ex: 09:30");

        comboType = new JComboBox<>(new String[]{"Premiere visite", "Suivi", "Urgence"});

        txtMotif = new JTextField(20);
        txtMotif.setToolTipText("Motif de la consultation — OBLIGATOIRE");

        txtPrix = new JTextField(20);
        txtPrix.setToolTipText("Prix en FCFA ex: 5000");

        comboPaiement = new JComboBox<>(new String[]{"Especes", "T-Money", "Flooz", "Carte Bancaire", "Assurance"});
        comboStatutPaiement = new JComboBox<>(new String[]{"Non paye", "Paye", "En attente"});
        comboStatut = new JComboBox<>(new String[]{"Scheduled", "Completed", "Cancelled", "No-show"});

        txtNumeroRecu = new JTextField(20);
        txtNumeroRecu.setToolTipText("ex: REC-2026-001");

        // Placement ordonné des lignes du formulaire
        ajouterLigne(mainPanel, gbc, 0,  "Patient :",             comboPatient);
        ajouterLigne(mainPanel, gbc, 1,  "Médecin :",             comboMedecin);
        ajouterLigne(mainPanel, gbc, 2,  "Date (JJ/MM/AAAA) :",    txtDate);
        ajouterLigne(mainPanel, gbc, 3,  "Heure (HH:MM) :",        txtHeure);
        ajouterLigne(mainPanel, gbc, 4,  "Type consultation :",    comboType);
        ajouterLigne(mainPanel, gbc, 5,  "Motif (obligatoire) :",   txtMotif);
        ajouterLigne(mainPanel, gbc, 6,  "Prix (FCFA) :",           txtPrix);
        ajouterLigne(mainPanel, gbc, 7,  "Mode paiement :",         comboPaiement);
        ajouterLigne(mainPanel, gbc, 8,  "Statut paiement :",       comboStatutPaiement);
        ajouterLigne(mainPanel, gbc, 9,  "Statut RDV :",            comboStatut);
        ajouterLigne(mainPanel, gbc, 10, "N° Reçu :",               txtNumeroRecu);

        // Barre d'actions (Boutons inférieurs)
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoutons.setOpaque(false);

        btnAnnuler = new JButton("Annuler");
        btnAnnuler.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAnnuler.addActionListener(e -> dispose());

        btnEnregistrer = new JButton("Enregistrer");
        btnEnregistrer.setBackground(new Color(0, 102, 153));
        btnEnregistrer.setForeground(Color.WHITE);
        btnEnregistrer.setOpaque(true);
        btnEnregistrer.setBorderPainted(false);
        btnEnregistrer.setFocusPainted(false);
        btnEnregistrer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnregistrer.addActionListener(e -> enregistrer());

        panelBoutons.add(btnAnnuler);
        panelBoutons.add(btnEnregistrer);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        mainPanel.add(panelBoutons, gbc);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        add(scroll);
    }

    /**
     * Méthode utilitaire facilitant l'insertion structurée d'un composant précédé de son libellé
     * dans le conteneur GridBagLayout.
     *
     * @param panel Le panneau cible recevant les éléments.
     * @param gbc Le configurateur de contraintes géométriques Swing.
     * @param ligne L'index de la rangée (Y) d'insertion.
     * @param labelText L'intitulé textuel affiché à gauche.
     * @param comp Le composant de saisie ou de sélection positionné à droite.
     */
    private void ajouterLigne(JPanel panel, GridBagConstraints gbc, int ligne, String labelText, Component comp) {
        gbc.gridy = ligne;
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.weightx = 0.4;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(comp, gbc);
    }

    /**
     * Pré-remplit l'ensemble des champs du formulaire avec les attributs de
     * l'objet {@link Appointment} existant lors d'une action de modification.
     */
    private void remplirChamps() {
        for (int i = 0; i < comboPatient.getItemCount(); i++) {
            String item = comboPatient.getItemAt(i);
            if (item.startsWith(appointmentExistant.getPatientId() + " - ")) {
                comboPatient.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < comboMedecin.getItemCount(); i++) {
            String item = comboMedecin.getItemAt(i);
            if (item.startsWith(appointmentExistant.getDoctorId() + " - ")) {
                comboMedecin.setSelectedIndex(i);
                break;
            }
        }

        if (appointmentExistant.getAppointmentDate() != null) {
            txtDate.setText(appointmentExistant.getAppointmentDate().format(formatterDate));
        }

        if (appointmentExistant.getAppointmentTime() != null) {
            txtHeure.setText(appointmentExistant.getAppointmentTime().format(formatterHeure));
        }

        comboType.setSelectedItem(appointmentExistant.getTypeConsultation());
        txtMotif.setText(appointmentExistant.getReasonForVisit());
        txtPrix.setText(String.valueOf(appointmentExistant.getPrixConsultation()));
        comboPaiement.setSelectedItem(appointmentExistant.getModePaiement());
        comboStatutPaiement.setSelectedItem(appointmentExistant.getStatutPaiement());
        comboStatut.setSelectedItem(appointmentExistant.getStatus());
        txtNumeroRecu.setText(appointmentExistant.getNumeroRecu());
    }

    /**
     * Lit, valide la cohérence des données saisies par l'utilisateur et procède
     * à la persistance du rendez-vous en base de données.
     * Déclenche une alerte bloquante en cas d'erreur de format ou d'omission de champ requis.
     */
    private void enregistrer() {
        if (comboPatient.getSelectedIndex() == 0) {
            afficherErreur("Veuillez sélectionner un patient !");
            return;
        }

        if (comboMedecin.getSelectedIndex() == 0) {
            afficherErreur("Veuillez sélectionner un médecin !");
            return;
        }

        String motif = txtMotif.getText().trim();
        if (motif.isEmpty()) {
            afficherErreur("Le motif de consultation est obligatoire !");
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(txtDate.getText().trim(), formatterDate);
        } catch (DateTimeParseException e) {
            afficherErreur("Format de date invalide ! Utilisez JJ/MM/AAAA");
            return;
        }

        LocalTime heure;
        try {
            heure = LocalTime.parse(txtHeure.getText().trim(), formatterHeure);
        } catch (DateTimeParseException e) {
            afficherErreur("Format d'heure invalide ! Utilisez HH:MM");
            return;
        }

        double prix = 0;
        if (!txtPrix.getText().trim().isEmpty()) {
            try {
                prix = Double.parseDouble(txtPrix.getText().trim());
            } catch (NumberFormatException e) {
                afficherErreur("Le prix doit être un nombre valide !");
                return;
            }
        }

        String patientSelected = (String) comboPatient.getSelectedItem();
        int patientId = Integer.parseInt(patientSelected.split(" - ")[0]);

        String medecinSelected = (String) comboMedecin.getSelectedItem();
        int medecinId = Integer.parseInt(medecinSelected.split(" - ")[0]);

        Appointment a = (appointmentExistant == null) ? new Appointment() : appointmentExistant;

        a.setPatientId(patientId);
        a.setDoctorId(medecinId);
        a.setAppointmentDate(date);
        a.setAppointmentTime(heure);
        a.setTypeConsultation((String) comboType.getSelectedItem());
        a.setReasonForVisit(motif);
        a.setPrixConsultation(prix);
        a.setModePaiement((String) comboPaiement.getSelectedItem());
        a.setStatutPaiement((String) comboStatutPaiement.getSelectedItem());
        a.setStatus((String) comboStatut.getSelectedItem());
        a.setNumeroRecu(txtNumeroRecu.getText().trim());
        a.setStatutNotification("Non envoyee");

        AppointmentDAO dao = new AppointmentDAO();
        boolean succes = (appointmentExistant == null) ? dao.ajouter(a) : dao.modifier(a);

        if (succes) {
            JOptionPane.showMessageDialog(this, "Opération réussie !");
            parentPanel.chargerRendezVous();
            dispose();
        } else {
            afficherErreur("Erreur lors de l'enregistrement en base de données.");
        }
    }

    /**
     * Centralise l'affichage des alertes d'erreurs graphiques via des boîtes de dialogue.
     *
     * @param message Message textuel décrivant l'anomalie constatée.
     */
    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur de validation", JOptionPane.ERROR_MESSAGE);
    }
}