package view;

import dao.PatientDAO;
import model.Patient;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Fenêtre de dialogue modale (JDialog) représentant le formulaire de saisie
 * pour créer un nouveau patient ou modifier un patient existant.
 * Elle gère l'interface Swing, la validation des données saisies (comme les dates)
 * et communique avec la couche DAO pour la persistance.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class PatientFormFrame extends JDialog {

    // ============ COMPOSANTS ============

    /** Champ de texte pour saisir le nom complet du patient. */
    private JTextField txtNomComplet;

    /** Champ de texte pour la date de naissance. Format attendu : JJ/MM/AAAA. */
    private JTextField txtDateNaissance;

    /** Liste déroulante pour la sélection du genre ("Male", "Female", "Other"). */
    private JComboBox<String> comboSexe;

    /** Champ de texte pour le numéro de téléphone de contact. */
    private JTextField txtTelephone;

    /** Liste déroulante pour sélectionner le service médical. */
    private JComboBox<String> comboService;

    /** Liste déroulante pour sélectionner le pays d'origine du patient. */
    private JComboBox<String> comboPays;

    /** Bouton pour valider et enregistrer le formulaire. */
    private JButton btnEnregistrer;

    /** Bouton pour fermer la fenêtre sans enregistrer. */
    private JButton btnAnnuler;

    // ============ DONNÉES ============

    /** L'instance du patient à modifier, ou null si l'on se trouve en mode création. */
    private Patient patientExistant;

    /** Référence vers le panneau parent afin de pouvoir rafraîchir son tableau après soumission. */
    private PatientPanel parentPanel;

    /** Formateur temporel configuré sur le modèle français "dd/MM/yyyy" pour l'analyse des saisies. */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ============ CONSTRUCTEUR ============

    /**
     * Initialise et affiche le formulaire de saisie pour un patient.
     * Détermine automatiquement si la fenêtre s'ouvre en mode ajout ou modification.
     *
     * @param parent La fenêtre principale {@link JFrame} servant d'ancrage à cette boîte modale.
     * @param parentPanel Le panneau d'affichage {@link PatientPanel} demandant l'ouverture du formulaire.
     * @param patient L'objet {@link Patient} à modifier, ou null s'il s'agit d'une insertion.
     */
    public PatientFormFrame(JFrame parent, PatientPanel parentPanel, Patient patient) {
        super(parent, "Formulaire Patient", true); // true = fenêtre modale
        this.parentPanel = parentPanel;
        this.patientExistant = patient;

        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);  // ✅ Nettoyage ressources

        // Construction de l'interface
        initComposants();

        // Si modification → pré-remplir les champs et changer le titre
        if (patientExistant != null) {
            setTitle("✏️ Modifier le Patient");
            remplirChamps();
        } else {
            setTitle("➕ Nouveau Patient");
        }
    }

    // ============ INTERFACE ============

    /**
     * Crée, configure et organise graphiquement l'ensemble des composants de la fenêtre
     * à l'aide d'un gestionnaire de placement de type GridBagLayout.
     */
    private void initComposants() {

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---- CHAMPS DE SAISIE ----
        txtNomComplet = new JTextField(20);

        txtDateNaissance = new JTextField(20);
        txtDateNaissance.setToolTipText("Format: JJ/MM/AAAA");

        comboSexe = new JComboBox<>(new String[]{
                "Male", "Female", "Other"
        });

        txtTelephone = new JTextField(20);

        comboService = new JComboBox<>(new String[]{
                "Général", "Cardiologie", "Pédiatrie",
                "Gynécologie", "Neurologie", "Urgences",
                "Chirurgie", "Oncologie"
        });

        comboPays = new JComboBox<>(new String[]{
                "Togo", "Benin", "Ghana", "Nigeria",
                "Senegal", "Mali", "Burkina Faso",
                "Cote d Ivoire", "Niger", "Guinee",
                "Cameroun", "Gabon", "Congo", "Autre"
        });

        // ---- PLACEMENT DES CHAMPS ----
        ajouterLigne(mainPanel, gbc, 0, "Nom complet :",                 txtNomComplet);
        ajouterLigne(mainPanel, gbc, 1, "Date naissance (JJ/MM/AAAA) :", txtDateNaissance);
        ajouterLigne(mainPanel, gbc, 2, "Sexe :",                        comboSexe);
        ajouterLigne(mainPanel, gbc, 3, "Téléphone :",                   txtTelephone);
        ajouterLigne(mainPanel, gbc, 4, "Service :",                     comboService);
        ajouterLigne(mainPanel, gbc, 5, "Pays :",                        comboPays);

        // ---- BOUTONS ----
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoutons.setOpaque(false);

        // Bouton Annuler
        btnAnnuler = new JButton("Annuler");
        btnAnnuler.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAnnuler.addActionListener(e -> dispose());

        // Bouton Enregistrer
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

        // Placement des boutons
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        mainPanel.add(panelBoutons, gbc);

        add(mainPanel);
    }

    // ============ METHODE UTILITAIRE — PLACEMENT LIGNES ============

    /**
     * Méthode utilitaire interne facilitant l'alignement d'un libellé (JLabel)
     * et de son composant de saisie associé sur une ligne du layout.
     *
     * @param panel Le panneau cible dans lequel injecter les composants.
     * @param gbc L'objet de contraintes GridBagConstraints configuré pour le positionnement.
     * @param ligne L'index de la ligne du layout (axe Y) à attribuer.
     * @param labelText Le texte à afficher dans le libellé indicatif.
     * @param comp Le composant graphique de saisie à positionner en vis-à-vis.
     */
    private void ajouterLigne(JPanel panel, GridBagConstraints gbc,
                              int ligne, String labelText, Component comp) {
        gbc.gridy = ligne;
        gbc.gridwidth = 1;

        // Colonne gauche → label
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);

        // Colonne droite → composant
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    // ============ PRE-REMPLISSAGE EN MODE MODIFICATION ============

    /**
     * Extrait les informations de l'entité stockée dans l'attribut patientExistant
     * afin de pré-remplir l'ensemble des champs graphiques de l'interface.
     */
    private void remplirChamps() {
        if (patientExistant == null) return;  // ✅ Sécurité

        txtNomComplet.setText(patientExistant.getNomComplet() != null ? patientExistant.getNomComplet() : "");

        // Conversion LocalDate → String pour affichage
        if (patientExistant.getDateNaissance() != null) {
            txtDateNaissance.setText(patientExistant.getDateNaissance().format(formatter));
        }

        if (patientExistant.getSexe() != null) {
            comboSexe.setSelectedItem(patientExistant.getSexe());
        }

        txtTelephone.setText(patientExistant.getTelephone() != null ? patientExistant.getTelephone() : "");

        if (patientExistant.getService() != null) {
            comboService.setSelectedItem(patientExistant.getService());
        }

        if (patientExistant.getPays() != null) {
            comboPays.setSelectedItem(patientExistant.getPays());
        }
    }

    // ============ ENREGISTREMENT ============

    /**
     * Récupère l'ensemble des saisies utilisateur, procède à des validations strictes
     * (champs obligatoires vides, format de date correct), construit ou met à jour
     * l'objet métier, puis invoque la couche DAO appropriée pour la sauvegarde.
     */
    private void enregistrer() {

        // 1. Récupération des valeurs saisies
        String nom     = txtNomComplet.getText().trim();
        String dateStr = txtDateNaissance.getText().trim();
        String tel     = txtTelephone.getText().trim();

        // 2. Validation des champs obligatoires
        if (nom.isEmpty() || dateStr.isEmpty()) {  // ✅ Téléphone optionnel
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs obligatoires (Nom, Date naissance) !",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Validation téléphone si fourni
        if (!tel.isEmpty() && !tel.matches("\\d{8,}")) {
            JOptionPane.showMessageDialog(this,
                    "Téléphone invalide ! Doit contenir au moins 8 chiffres",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Validation et conversion de la date JJ/MM/AAAA → LocalDate
        LocalDate dateNaissance;
        try {
            dateNaissance = LocalDate.parse(dateStr, formatter);

            // ✅ Validation métier de la date
            if (dateNaissance.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(this,
                        "La date de naissance ne peut pas être dans le futur !",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dateNaissance.isBefore(LocalDate.now().minusYears(150))) {
                JOptionPane.showMessageDialog(this,
                        "La date de naissance n'est pas plausible !",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Format de date invalide ! Utilisez JJ/MM/AAAA",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Construction de l'objet Patient
        // Si ajout → nouvel objet, si modification → on réutilise l'existant
        Patient p = (patientExistant == null) ? new Patient() : patientExistant;
        p.setNomComplet(nom);
        p.setDateNaissance(dateNaissance);
        p.setSexe((String) comboSexe.getSelectedItem());
        p.setTelephone(tel.isEmpty() ? null : tel);  // ✅ Null si vide
        p.setService((String) comboService.getSelectedItem());
        p.setPays((String) comboPays.getSelectedItem());

        // 5. Envoi au DAO selon le mode
        PatientDAO dao = new PatientDAO();
        boolean succes;

        if (patientExistant == null) {
            succes = dao.ajouter(p);   // Mode Ajout
        } else {
            succes = dao.modifier(p);  // Mode Modification
        }

        // 6. Retour visuel et rafraîchissement du tableau
        if (succes) {
            JOptionPane.showMessageDialog(this, "Opération réussie !");
            parentPanel.rechercherPatient(); // Rafraîchit le tableau
            dispose();                       // Ferme le formulaire
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement en base de données.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}