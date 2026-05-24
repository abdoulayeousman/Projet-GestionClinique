package view;

import dao.DoctorDAO;
import model.Doctor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Boîte de dialogue modale ({@link JDialog}) représentant le formulaire de saisie des médecins.
 * Cette classe prend en charge à la fois la création (mode Ajout) et la mise à jour (mode Modification)
 * d'un profil médical en base de données. Elle intègre des mécanismes de validation de types
 * (numériques pour l'expérience et temporels pour les plages horaires de service).
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class DoctorFormFrame extends JDialog {

    // ============ COMPOSANTS ============

    /** Champ de saisie pour le nom complet du médecin. */
    private JTextField txtNomComplet;

    /** Champ de saisie pour le numéro de téléphone. */
    private JTextField txtTelephone;

    /** Liste déroulante pour la spécialité médicale principale. */
    private JComboBox<String> comboSpecialite;

    /** Champ de saisie pour le numéro d'inscription à l'Ordre des médecins (obligatoire). */
    private JTextField txtLicenseNumber;

    /** Liste déroulante pour le grade ou titre académique/hospitalier. */
    private JComboBox<String> comboGrade;

    /** Champ de saisie pour l'ancienneté professionnelle (en années). */
    private JTextField txtYearsExperience;

    /** Liste déroulante affectant le praticien à un service de la clinique. */
    private JComboBox<String> comboService;

    /** Champ de saisie identifiant le bureau ou la salle de consultation. */
    private JTextField txtBureau;

    /** Liste déroulante caractérisant l'état de disponibilité instantané. */
    private JComboBox<String> comboStatut;

    /** Champ de saisie libre énumérant les jours hebdomadaires de garde/présence. */
    private JTextField txtJoursDisponibles;

    /** Champ de saisie de la plage horaire d'ouverture de garde (Format attendu : HH:MM). */
    private JTextField txtHeureDebut;

    /** Champ de saisie de la plage horaire de clôture de garde (Format attendu : HH:MM). */
    private JTextField txtHeureFin;

    /** Liste déroulante pour le pays d'origine ou d'exercice de la zone. */
    private JComboBox<String> comboPays;

    /** Champ de saisie pour la commune ou ville de rattachement. */
    private JTextField txtVille;

    /** Bouton de soumission déclenchant la validation et la persistance des données. */
    private JButton btnEnregistrer;

    /** Bouton d'annulation interrompant la saisie sans sauvegarder. */
    private JButton btnAnnuler;

    // ============ DONNÉES ============

    /**
     * Référence vers l'instance en cours de modification.
     * Si cette variable vaut {@code null}, l'interface se configure automatiquement en mode création.
     */
    private Doctor doctorExistant;

    /** Référence vers le panneau métier parent pour commander la réévaluation de la grille d'affichage. */
    private DoctorPanel parentPanel;

    // ============ CONSTRUCTEUR ============

    /**
     * Initialise et affiche la fenêtre de saisie pour l'entité Médecin.
     * Configure le mode bloquant (modal), applique les dimensions fixes, appelle l'assemblage
     * des composants, puis examine l'état du paramètre {@code doctor} pour orienter la nature
     * du traitement (insertion ou mise à jour).
     *
     * @param parent La fenêtre graphique propriétaire de type {@link JFrame}.
     * @param parentPanel Le conteneur fonctionnel {@link DoctorPanel} à l'origine de l'appel.
     * @param doctor L'entité cible {@link Doctor} à éditer, ou {@code null} pour un nouveau profil.
     */
    public DoctorFormFrame(JFrame parent, DoctorPanel parentPanel, Doctor doctor) {
        super(parent, "Formulaire Médecin", true); // true = fenêtre modale
        this.parentPanel = parentPanel;
        this.doctorExistant = doctor;

        setSize(500, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);  // ✅ Nettoyage ressources

        // Construction de l'interface
        initComposants();

        // Si modification → pré-remplir les champs et changer le titre
        if (doctorExistant != null) {
            setTitle("✏️ Modifier le Médecin");
            remplirChamps();
        } else {
            setTitle("➕ Nouveau Médecin");
        }
    }

    // ============ INTERFACE ============

    /**
     * Instancie les éléments graphiques et orchestre l'alignement des lignes de saisie.
     * L'agencement repose sur un gestionnaire GridBagLayout pour garantir une distribution
     * symétrique entre les étiquettes de description et leurs contrôles respectifs, le tout
     * encapsulé dans un conteneur défilant JScrollPane.
     */
    private void initComposants() {

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---- CHAMPS DE SAISIE ----
        txtNomComplet = new JTextField(20);

        txtTelephone = new JTextField(20);

        comboSpecialite = new JComboBox<>(new String[]{
                "Médecine Générale", "Cardiologie", "Pédiatrie",
                "Gynécologie", "Neurologie", "Urgences",
                "Chirurgie", "Oncologie", "Dermatologie",
                "Ophtalmologie", "ORL", "Psychiatrie"
        });

        txtLicenseNumber = new JTextField(20);
        txtLicenseNumber.setToolTipText("Numéro d'ordre obligatoire");

        comboGrade = new JComboBox<>(new String[]{
                "Interne", "Résident", "Assistant",
                "Docteur", "Professeur"
        });

        txtYearsExperience = new JTextField(20);
        txtYearsExperience.setToolTipText("Nombre d'années ex: 5");

        comboService = new JComboBox<>(new String[]{
                "Général", "Cardiologie", "Pédiatrie",
                "Gynécologie", "Neurologie", "Urgences",
                "Chirurgie", "Oncologie"
        });

        txtBureau = new JTextField(20);
        txtBureau.setToolTipText("ex: Bureau 12 ou Salle 3");

        comboStatut = new JComboBox<>(new String[]{
                "Disponible", "En consultation", "Absent", "En conge"
        });

        txtJoursDisponibles = new JTextField(20);
        txtJoursDisponibles.setToolTipText("ex: Lundi, Mercredi, Vendredi");

        txtHeureDebut = new JTextField(20);
        txtHeureDebut.setToolTipText("Format: HH:MM ex: 08:00");

        txtHeureFin = new JTextField(20);
        txtHeureFin.setToolTipText("Format: HH:MM ex: 17:00");

        comboPays = new JComboBox<>(new String[]{
                "Togo", "Benin", "Ghana", "Nigeria",
                "Senegal", "Mali", "Burkina Faso",
                "Cote d Ivoire", "Niger", "Guinee", "Autre"
        });

        txtVille = new JTextField(20);

        // ---- PLACEMENT DES CHAMPS ----
        ajouterLigne(mainPanel, gbc, 0,  "Nom complet :",          txtNomComplet);
        ajouterLigne(mainPanel, gbc, 1,  "Téléphone :",            txtTelephone);
        ajouterLigne(mainPanel, gbc, 2,  "Spécialité :",           comboSpecialite);
        ajouterLigne(mainPanel, gbc, 3,  "N° Ordre (obligatoire):", txtLicenseNumber);
        ajouterLigne(mainPanel, gbc, 4,  "Grade :",                comboGrade);
        ajouterLigne(mainPanel, gbc, 5,  "Années d'expérience :",  txtYearsExperience);
        ajouterLigne(mainPanel, gbc, 6,  "Service :",              comboService);
        ajouterLigne(mainPanel, gbc, 7,  "Bureau/Salle :",         txtBureau);
        ajouterLigne(mainPanel, gbc, 8,  "Statut :",               comboStatut);
        ajouterLigne(mainPanel, gbc, 9,  "Jours disponibles :",    txtJoursDisponibles);
        ajouterLigne(mainPanel, gbc, 10, "Heure début (HH:MM) :",  txtHeureDebut);
        ajouterLigne(mainPanel, gbc, 11, "Heure fin (HH:MM) :",    txtHeureFin);
        ajouterLigne(mainPanel, gbc, 12, "Pays :",                 comboPays);
        ajouterLigne(mainPanel, gbc, 13, "Ville :",                txtVille);

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
        gbc.gridy = 14;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        mainPanel.add(panelBoutons, gbc);

        // Scroll pour le formulaire
        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        add(scroll);
    }

    // ============ METHODE UTILITAIRE — PLACEMENT LIGNES ============

    /**
     * Positionne de façon standardisée un couple étiquette/composant sur une ligne de la grille.
     * Configure les poids proportionnels des colonnes pour conserver un alignement vertical rigoureux.
     *
     * @param panel Le panneau cible recevant les éléments.
     * @param gbc Le configurateur de contraintes géométriques du Layout.
     * @param ligne L'indice de rangée verticale (coordonnée Y).
     * @param labelText La valeur textuelle à inscrire dans le libellé indicateur.
     * @param comp Le champ d'édition (zone de texte, liste) à insérer en vis-à-vis.
     */
    private void ajouterLigne(JPanel panel, GridBagConstraints gbc,
                              int ligne, String labelText, Component comp) {
        gbc.gridy = ligne;
        gbc.gridwidth = 1;

        // Colonne gauche → label
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);

        // Colonne droite → composant
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(comp, gbc);
    }

    // ============ PRE-REMPLISSAGE EN MODE MODIFICATION ============

    /**
     * Extrait les attributs du modèle référencé pour pré-charger les champs graphiques.
     * Assure la conversion des types spécifiques, notamment l'évaluation des structures
     * de type {@link LocalTime} en représentations textuelles modifiables.
     */
    private void remplirChamps() {
        if (doctorExistant == null) return;  // ✅ Sécurité

        txtNomComplet.setText(doctorExistant.getFullName() != null ? doctorExistant.getFullName() : "");
        txtTelephone.setText(doctorExistant.getPhoneNumber() != null ? doctorExistant.getPhoneNumber() : "");

        if (doctorExistant.getSpecialization() != null) {
            comboSpecialite.setSelectedItem(doctorExistant.getSpecialization());
        }

        txtLicenseNumber.setText(doctorExistant.getLicenseNumber() != null ? doctorExistant.getLicenseNumber() : "");

        if (doctorExistant.getGrade() != null) {
            comboGrade.setSelectedItem(doctorExistant.getGrade());
        }

        txtYearsExperience.setText(String.valueOf(doctorExistant.getYearsOfExperience()));

        if (doctorExistant.getService() != null) {
            comboService.setSelectedItem(doctorExistant.getService());
        }

        txtBureau.setText(doctorExistant.getBureau() != null ? doctorExistant.getBureau() : "");

        if (doctorExistant.getStatut() != null) {
            comboStatut.setSelectedItem(doctorExistant.getStatut());
        }

        txtJoursDisponibles.setText(doctorExistant.getJoursDisponibles() != null ? doctorExistant.getJoursDisponibles() : "");

        // Conversion LocalTime → String pour affichage
        if (doctorExistant.getHeureDebut() != null) {
            txtHeureDebut.setText(doctorExistant.getHeureDebut().toString());
        }
        if (doctorExistant.getHeureFin() != null) {
            txtHeureFin.setText(doctorExistant.getHeureFin().toString());
        }

        if (doctorExistant.getPays() != null) {
            comboPays.setSelectedItem(doctorExistant.getPays());
        }

        txtVille.setText(doctorExistant.getVille() != null ? doctorExistant.getVille() : "");
    }

    // ============ ENREGISTREMENT ============

    /**
     * Valide l'intégrité de la saisie utilisateur et ordonne sa persistance.
     * Cette méthode applique un contrôle par étapes :
     * <ol>
     *   <li>Validation de la présence des attributs obligatoires (nom, numéro d'ordre).</li>
     *   <li>Contrôle du format numérique de l'ancienneté (interception des {@link NumberFormatException}).</li>
     *   <li>Validation syntaxique des plages horaires (interception des {@link DateTimeParseException}).</li>
     *   <li>Hydratation ou instanciation du modèle métier {@link Doctor}.</li>
     *   <li>Délégation de la sauvegarde à la couche d'accès aux données via {@link DoctorDAO}.</li>
     * </ol>
     */
    private void enregistrer() {

        // 1. Récupération des valeurs saisies
        String nom     = txtNomComplet.getText().trim();
        String tel     = txtTelephone.getText().trim();
        String license = txtLicenseNumber.getText().trim();
        String years   = txtYearsExperience.getText().trim();

        // 2. Validation des champs obligatoires
        if (nom.isEmpty() || license.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nom complet et N° Ordre sont obligatoires !",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Validation années d'expérience
        int yearsInt = 0;
        if (!years.isEmpty()) {
            try {
                yearsInt = Integer.parseInt(years);
                // ✅ Validation métier
                if (yearsInt < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Années d'expérience ne peut pas être négative !",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (yearsInt > 80) {
                    JOptionPane.showMessageDialog(this,
                            "Années d'expérience non plausible (max 80) !",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Années d'expérience doit être un nombre !",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 4. Validation heures
        LocalTime heureDebut = null;
        LocalTime heureFin = null;
        try {
            if (!txtHeureDebut.getText().trim().isEmpty()) {
                heureDebut = LocalTime.parse(txtHeureDebut.getText().trim());
            }
            if (!txtHeureFin.getText().trim().isEmpty()) {
                heureFin = LocalTime.parse(txtHeureFin.getText().trim());
            }

            // ✅ Validation métier : heureDebut < heureFin
            if (heureDebut != null && heureFin != null && heureDebut.isAfter(heureFin)) {
                JOptionPane.showMessageDialog(this,
                        "L'heure de début doit être avant l'heure de fin !",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Format d'heure invalide ! Utilisez HH:MM",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 5. Construction de l'objet Doctor
        Doctor d = (doctorExistant == null) ? new Doctor() : doctorExistant;
        d.setFullName(nom);
        d.setPhoneNumber(tel);
        d.setSpecialization((String) comboSpecialite.getSelectedItem());
        d.setLicenseNumber(license);
        d.setGrade((String) comboGrade.getSelectedItem());
        d.setYearsOfExperience(yearsInt);
        d.setService((String) comboService.getSelectedItem());
        d.setBureau(txtBureau.getText().trim());
        d.setStatut((String) comboStatut.getSelectedItem());
        d.setJoursDisponibles(txtJoursDisponibles.getText().trim());
        d.setHeureDebut(heureDebut);
        d.setHeureFin(heureFin);
        d.setPays((String) comboPays.getSelectedItem());
        d.setVille(txtVille.getText().trim());

        // 6. Envoi au DAO selon le mode
        DoctorDAO dao = new DoctorDAO();
        boolean succes;

        if (doctorExistant == null) {
            succes = dao.ajouter(d);   // Mode Ajout
        } else {
            succes = dao.modifier(d);  // Mode Modification
        }

        // 7. Retour visuel et rafraîchissement du tableau
        if (succes) {
            JOptionPane.showMessageDialog(this, "Opération réussie !");
            parentPanel.rechercherMedecin(); // Rafraîchit le tableau
            dispose();                       // Ferme le formulaire
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement en base de données.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}