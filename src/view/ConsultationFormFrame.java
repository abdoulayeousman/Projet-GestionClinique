package view;

import dao.AppointmentDAO;
import dao.ConsultationDAO;
import model.Appointment;
import model.Consultation;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Fenêtre modale de formulaire pour la création et la modification
 * des dossiers de consultation médicale.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class ConsultationFormFrame extends JDialog {

    private JComboBox<String> comboRendezVous;
    private JTextField txtDureeSymptomes;
    private JSpinner spinnerNiveauDouleur;
    private JTextArea txtDiagnostic;
    private JTextArea txtNotesMedicales;
    private JTextArea txtAnalyseDemandee;
    private JTextArea txtResultatAnalyse;
    private JTextArea txtOrdonnance;
    private JTextField txtDosage;
    private JTextField txtDureeTraitement;
    private JTextField txtDateProchainControle;
    private JTextArea txtInstructionsControle;
    private JComboBox<String> comboStatut;
    private JButton btnEnregistrer;
    private JButton btnAnnuler;

    private Consultation consultationExistante;
    private ConsultationPanel parentPanel;
    private List<Appointment> listeRendezVous;
    private final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construit le formulaire de consultation.
     *
     * @param parent           La fenêtre parente.
     * @param parentPanel      Le panneau d'origine à rafraîchir.
     * @param consultation      null en mode Ajout, objet existant en mode Modification.
     */
    public ConsultationFormFrame(JFrame parent, ConsultationPanel parentPanel, Consultation consultation) {
        super(parent, "Formulaire Consultation", true);
        this.parentPanel = parentPanel;
        this.consultationExistante = consultation;

        setSize(650, 750);
        setLocationRelativeTo(parent);
        setResizable(false);

        chargerRendezVous();
        initComposants();

        if (consultationExistante != null) {
            setTitle("✏️ Modifier la Consultation");
            remplirChamps();
        } else {
            setTitle("➕ Nouvelle Consultation");
        }
    }

    private void chargerRendezVous() {
        listeRendezVous = new AppointmentDAO().tousLesRendezVous();
    }

    private void initComposants() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---- SECTION 1 : INFORMATIONS GÉNÉRALES ----
        ajouterTitreSeparateur(mainPanel, gbc, 0, "📌 Informations Générales");

        comboRendezVous = new JComboBox<>();
        comboRendezVous.addItem("-- Sélectionner un rendez-vous --");
        for (Appointment a : listeRendezVous) {
            comboRendezVous.addItem(
                    a.getAppointmentId() + " | Patient:" + a.getPatientId()
                            + " | Dr:" + a.getDoctorId()
                            + " | " + a.getAppointmentDate()
                            + " " + a.getAppointmentTime()
            );
        }
        ajouterLigne(mainPanel, gbc, 1, "Rendez-vous lié :", comboRendezVous);

        txtDureeSymptomes = new JTextField(20);
        txtDureeSymptomes.setToolTipText("ex: Depuis 3 jours");
        ajouterLigne(mainPanel, gbc, 2, "Durée des symptômes :", txtDureeSymptomes);

        spinnerNiveauDouleur = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        ajouterLigne(mainPanel, gbc, 3, "Niveau douleur (0-10) :", spinnerNiveauDouleur);

        comboStatut = new JComboBox<>(new String[]{"En cours", "Terminee", "A suivre"});
        ajouterLigne(mainPanel, gbc, 4, "Statut :", comboStatut);

        // ---- SECTION 2 : DIAGNOSTIC ----
        ajouterTitreSeparateur(mainPanel, gbc, 5, "🩺 Diagnostic Médical");

        txtDiagnostic = new JTextArea(3, 20);
        txtDiagnostic.setLineWrap(true);
        txtDiagnostic.setWrapStyleWord(true);
        ajouterLigneTextArea(mainPanel, gbc, 6, "Diagnostic* :", txtDiagnostic);

        txtNotesMedicales = new JTextArea(3, 20);
        txtNotesMedicales.setLineWrap(true);
        txtNotesMedicales.setWrapStyleWord(true);
        ajouterLigneTextArea(mainPanel, gbc, 7, "Notes médicales :", txtNotesMedicales);

        // ---- SECTION 3 : EXAMENS ET ANALYSES ----
        ajouterTitreSeparateur(mainPanel, gbc, 8, "🔬 Examens et Analyses");

        txtAnalyseDemandee = new JTextArea(2, 20);
        txtAnalyseDemandee.setLineWrap(true);
        txtAnalyseDemandee.setWrapStyleWord(true);
        ajouterLigneTextArea(mainPanel, gbc, 9, "Analyses demandées :", txtAnalyseDemandee);

        txtResultatAnalyse = new JTextArea(2, 20);
        txtResultatAnalyse.setLineWrap(true);
        txtResultatAnalyse.setWrapStyleWord(true);
        ajouterLigneTextArea(mainPanel, gbc, 10, "Résultats analyses :", txtResultatAnalyse);

        // ---- SECTION 4 : ORDONNANCE ----
        ajouterTitreSeparateur(mainPanel, gbc, 11, "💊 Ordonnance et Traitement");

        txtOrdonnance = new JTextArea(3, 20);
        txtOrdonnance.setLineWrap(true);
        txtOrdonnance.setWrapStyleWord(true);
        ajouterLigneTextArea(mainPanel, gbc, 12, "Ordonnance :", txtOrdonnance);

        txtDosage = new JTextField(20);
        ajouterLigne(mainPanel, gbc, 13, "Dosage :", txtDosage);

        txtDureeTraitement = new JTextField(20);
        ajouterLigne(mainPanel, gbc, 14, "Durée traitement :", txtDureeTraitement);

        // ---- SECTION 5 : CONTRÔLE ----
        ajouterTitreSeparateur(mainPanel, gbc, 15, "📅 Prochain Contrôle");

        txtDateProchainControle = new JTextField(20);
        txtDateProchainControle.setToolTipText("Format: JJ/MM/AAAA");
        ajouterLigne(mainPanel, gbc, 16, "Date contrôle :", txtDateProchainControle);

        txtInstructionsControle = new JTextArea(2, 20);
        txtInstructionsControle.setLineWrap(true);
        txtInstructionsControle.setWrapStyleWord(true);
        ajouterLigneTextArea(mainPanel, gbc, 17, "Instructions :", txtInstructionsControle);

        // ---- PIED : BOUTONS D'ACTIONS ----
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
        gbc.gridy = 18;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(panelBoutons, gbc);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll);
    }

    private void ajouterTitreSeparateur(JPanel panel, GridBagConstraints gbc, int ligne, String titre) {
        gbc.gridy = ligne;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 8, 5, 8);

        JLabel lblSection = new JLabel(titre);
        lblSection.setFont(new Font("Arial", Font.BOLD, 13));
        lblSection.setForeground(new Color(102, 102, 102));
        panel.add(lblSection, gbc);
        gbc.insets = new Insets(5, 8, 5, 8);
    }

    private void ajouterLigne(JPanel panel, GridBagConstraints gbc, int ligne, String labelText, Component comp) {
        gbc.gridy = ligne;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        panel.add(comp, gbc);
    }

    private void ajouterLigneTextArea(JPanel panel, GridBagConstraints gbc, int ligne, String labelText, JTextArea textArea) {
        gbc.gridy = ligne;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        JScrollPane pane = new JScrollPane(textArea);
        panel.add(pane, gbc);
    }

    private void remplirChamps() {
        for (int i = 0; i < comboRendezVous.getItemCount(); i++) {
            String item = comboRendezVous.getItemAt(i);
            if (item.startsWith(consultationExistante.getAppointmentId() + " | ")) {
                comboRendezVous.setSelectedIndex(i);
                break;
            }
        }

        txtDureeSymptomes.setText(consultationExistante.getDureeSymptomes());
        spinnerNiveauDouleur.setValue(consultationExistante.getNiveauDouleur());
        comboStatut.setSelectedItem(consultationExistante.getStatut());
        txtDiagnostic.setText(consultationExistante.getDiagnostic());
        txtNotesMedicales.setText(consultationExistante.getNotesMedicales());
        txtAnalyseDemandee.setText(consultationExistante.getAnalyseDemandee());
        txtResultatAnalyse.setText(consultationExistante.getResultatAnalyse());
        txtOrdonnance.setText(consultationExistante.getOrdonnance());
        txtDosage.setText(consultationExistante.getDosage());
        txtDureeTraitement.setText(consultationExistante.getDureeTraitement());

        if (consultationExistante.getDateProchainControle() != null) {
            txtDateProchainControle.setText(consultationExistante.getDateProchainControle().format(formatterDate));
        }
        txtInstructionsControle.setText(consultationExistante.getInstructionsControle());
    }

    private void enregistrer() {
        if (comboRendezVous.getSelectedIndex() == 0) {
            afficherErreur("Veuillez sélectionner un rendez-vous lié !");
            return;
        }

        String diagnostic = txtDiagnostic.getText().trim();
        if (diagnostic.isEmpty()) {
            afficherErreur("Le diagnostic médical est obligatoire !");
            return;
        }

        LocalDate dateControle = null;
        String dateTxt = txtDateProchainControle.getText().trim();
        if (!dateTxt.isEmpty()) {
            try {
                dateControle = LocalDate.parse(dateTxt, formatterDate);
            } catch (DateTimeParseException e) {
                afficherErreur("Format de la date de contrôle incorrect ! Utilisez JJ/MM/AAAA");
                return;
            }
        }

        String rdvSelected = (String) comboRendezVous.getSelectedItem();
        String[] segments = rdvSelected.split(" \\| ");
        int appointmentId = Integer.parseInt(segments[0].trim());

        int patientId = Integer.parseInt(segments[1].replace("Patient:", "").trim());
        int doctorId = Integer.parseInt(segments[2].replace("Dr:", "").trim());

        Consultation c = (consultationExistante == null) ? new Consultation() : consultationExistante;

        c.setAppointmentId(appointmentId);
        c.setPatientId(patientId);
        c.setDoctorId(doctorId);
        c.setDureeSymptomes(txtDureeSymptomes.getText().trim());
        c.setNiveauDouleur((int) spinnerNiveauDouleur.getValue());
        c.setStatut((String) comboStatut.getSelectedItem());
        c.setDiagnostic(diagnostic);
        c.setNotesMedicales(txtNotesMedicales.getText().trim());
        c.setAnalyseDemandee(txtAnalyseDemandee.getText().trim());
        c.setResultatAnalyse(txtResultatAnalyse.getText().trim());
        c.setOrdonnance(txtOrdonnance.getText().trim());
        c.setDosage(txtDosage.getText().trim());
        c.setDureeTraitement(txtDureeTraitement.getText().trim());
        c.setDateProchainControle(dateControle);
        c.setInstructionsControle(txtInstructionsControle.getText().trim());

        if (consultationExistante == null) {
            c.setDateConsultation(LocalDateTime.now());
        }

        ConsultationDAO dao = new ConsultationDAO();
        boolean succes = (consultationExistante == null) ? dao.ajouter(c) : dao.modifier(c);

        if (succes) {
            JOptionPane.showMessageDialog(this, "Enregistrement de la consultation réussi !");
            parentPanel.chargerConsultations();
            dispose();
        } else {
            afficherErreur("Échec de la sauvegarde des données en base MySQL.");
        }
    }

    private void afficherErreur(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur de validation", JOptionPane.ERROR_MESSAGE);
    }
}