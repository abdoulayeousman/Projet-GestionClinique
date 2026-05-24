package dao;

import model.Consultation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Couche d'accès aux données (DAO) pour la gestion des consultations médicales.
 * Assure la liaison entre l'application Java et la table <code>consultations</code>
 * de la base de données MySQL en gérant le cycle de vie CRUD des dossiers de consultation.
 *
 * @author Abdoulaye Ousmane
 * @version 1.1
 */
public class ConsultationDAO {

    // ============ LISTER TOUTES LES CONSULTATIONS ============

    /**
     * Récupère l'intégralité des consultations enregistrées dans la base de données,
     * triées par identifiant décroissant (les plus récentes en premier).
     *
     * @return Une liste d'objets {@link Consultation}. La liste est vide si aucun enregistrement n'est trouvé.
     */
    public List<Consultation> toutesLesConsultations() {
        List<Consultation> liste = new ArrayList<>();
        String sql = "SELECT * FROM consultations ORDER BY consultation_id DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                liste.add(mapperConsultation(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // ============ HISTORIQUE PAR PATIENT ============

    /**
     * Filtre et récupère l'historique des consultations d'un patient spécifique.
     *
     * @param patientId L'identifiant unique du patient concerné.
     * @return Une liste contenant les consultations du patient, triées par ordre chronologique inversé.
     */
    public List<Consultation> parPatient(int patientId) {
        List<Consultation> liste = new ArrayList<>();
        String sql = "SELECT * FROM consultations WHERE patient_id = ? ORDER BY date_consultation DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapperConsultation(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // ============ RECHERCHE PAR ID UNIQUE ============

    /**
     * Recherche une consultation spécifique par son identifiant unique.
     * Useful pour les besoins d'affichage détaillé ou de génération de rapports.
     *
     * @param id L'identifiant de la consultation recherchée.
     * @return L'objet {@link Consultation} trouvé, ou <code>null</code> si aucun enregistrement ne correspond.
     */
    public Consultation parId(int id) {
        String sql = "SELECT * FROM consultations WHERE consultation_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapperConsultation(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ============ AJOUTER UNE CONSULTATION ============

    /**
     * Insère un nouvel enregistrement de consultation dans la base de données.
     * Gère la conversion des types temporels Java 8 vers les types SQL appropriés et
     * récupère automatiquement l'ID auto-incrémenté généré par la base de données.
     *
     * @param c L'objet {@link Consultation} contenant les données à insérer.
     * @return <code>true</code> si l'insertion a réussi, <code>false</code> sinon.
     */
    public boolean ajouter(Consultation c) {
        String sql = "INSERT INTO consultations (appointment_id, patient_id, doctor_id, " +
                "date_consultation, duree_symptomes, niveau_douleur, diagnostic, " +
                "notes_medicales, chemin_photo, ancienne_ordonnance, " +
                "ancien_resultat_analyse, analyse_demandee, resultat_analyse, " +
                "ordonnance, dosage, duree_traitement, date_prochain_controle, " +
                "instructions_controle, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Utilisation de RETURN_GENERATED_KEYS pour intercepter l'ID créé par MySQL
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, c.getAppointmentId());
            stmt.setInt(2, c.getPatientId());
            stmt.setInt(3, c.getDoctorId());

            if (c.getDateConsultation() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(c.getDateConsultation()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.setString(5, c.getDureeSymptomes());
            stmt.setInt(6, c.getNiveauDouleur());
            stmt.setString(7, c.getDiagnostic());
            stmt.setString(8, c.getNotesMedicales());
            stmt.setString(9, c.getCheminPhoto());
            stmt.setString(10, c.getAncienneOrdonnance());
            stmt.setString(11, c.getAncienResultatAnalyse());
            stmt.setString(12, c.getAnalyseDemandee());
            stmt.setString(13, c.getResultatAnalyse());
            stmt.setString(14, c.getOrdonnance());
            stmt.setString(15, c.getDosage());
            stmt.setString(16, c.getDureeTraitement());

            if (c.getDateProchainControle() != null) {
                stmt.setDate(17, Date.valueOf(c.getDateProchainControle()));
            } else {
                stmt.setNull(17, Types.DATE);
            }

            stmt.setString(18, c.getInstructionsControle());
            stmt.setString(19, c.getStatut());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Liaison de l'ID généré directement dans l'objet métier
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        c.setConsultationId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ============ MODIFIER UNE CONSULTATION ============

    /**
     * Met à jour les informations cliniques d'une consultation existante.
     * Les identifiants de structure (patient, médecin, rendez-vous) restent invariants.
     *
     * @param c L'objet {@link Consultation} modifié contenant son identifiant unique.
     * @return <code>true</code> si la mise à jour en base de données a été confirmée, <code>false</code> sinon.
     */
    public boolean modifier(Consultation c) {
        String sql = "UPDATE consultations SET diagnostic = ?, notes_medicales = ?, " +
                "duree_symptomes = ?, niveau_douleur = ?, " +
                "analyse_demandee = ?, resultat_analyse = ?, " +
                "ordonnance = ?, dosage = ?, duree_traitement = ?, " +
                "date_prochain_controle = ?, instructions_controle = ?, " +
                "statut = ? WHERE consultation_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getDiagnostic());
            stmt.setString(2, c.getNotesMedicales());
            stmt.setString(3, c.getDureeSymptomes());
            stmt.setInt(4, c.getNiveauDouleur());
            stmt.setString(5, c.getAnalyseDemandee());
            stmt.setString(6, c.getResultatAnalyse());
            stmt.setString(7, c.getOrdonnance());
            stmt.setString(8, c.getDosage());
            stmt.setString(9, c.getDureeTraitement());

            if (c.getDateProchainControle() != null) {
                stmt.setDate(10, Date.valueOf(c.getDateProchainControle()));
            } else {
                stmt.setNull(10, Types.DATE);
            }

            stmt.setString(11, c.getInstructionsControle());
            stmt.setString(12, c.getStatut());
            stmt.setInt(13, c.getConsultationId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ============ SUPPRIMER UNE CONSULTATION ============

    /**
     * Supprime définitivement une consultation de la base de données.
     *
     * @param id L'identifiant unique de la consultation à effacer.
     * @return <code>true</code> si la ligne a bien été supprimée, <code>false</code> sinon.
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM consultations WHERE consultation_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ============ CHANGER LE STATUT UNIQUEMENT ============

    /**
     * Modifie uniquement le statut d'un dossier de consultation (ex: En attente, Clôturée).
     *
     * @param id L'identifiant de la consultation à modifier.
     * @param nouveauStatut La nouvelle chaîne de caractères représentant le statut.
     * @return <code>true</code> si le changement a été appliqué, <code>false</code> sinon.
     */
    public boolean changerStatut(int id, String nouveauStatut) {
        String sql = "UPDATE consultations SET statut = ? WHERE consultation_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nouveauStatut);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ============ MAPPER — centralisation du couplage relationnel ============

    /**
     * Convertit une ligne de curseur SQL {@link ResultSet} en une instance d'objet métier {@link Consultation}.
     * Encapsule l'extraction sécurisée des données et les conversions de formats de date.
     *
     * @param rs Le ResultSet positionné sur la ligne courante à extraire.
     * @return Un objet {@link Consultation} entièrement modélisé.
     * @throws SQLException Si une erreur survient lors de la lecture des colonnes SQL.
     */
    private Consultation mapperConsultation(ResultSet rs) throws SQLException {
        Consultation c = new Consultation();
        c.setConsultationId(rs.getInt("consultation_id"));
        c.setAppointmentId(rs.getInt("appointment_id"));
        c.setPatientId(rs.getInt("patient_id"));
        c.setDoctorId(rs.getInt("doctor_id"));

        Timestamp ts = rs.getTimestamp("date_consultation");
        if (ts != null) {
            c.setDateConsultation(ts.toLocalDateTime());
        }

        c.setDureeSymptomes(rs.getString("duree_symptomes"));
        c.setNiveauDouleur(rs.getInt("niveau_douleur"));
        c.setDiagnostic(rs.getString("diagnostic"));
        c.setNotesMedicales(rs.getString("notes_medicales"));
        c.setCheminPhoto(rs.getString("chemin_photo"));
        c.setAncienneOrdonnance(rs.getString("ancienne_ordonnance"));
        c.setAncienResultatAnalyse(rs.getString("ancien_resultat_analyse"));
        c.setAnalyseDemandee(rs.getString("analyse_demandee"));
        c.setResultatAnalyse(rs.getString("resultat_analyse"));
        c.setOrdonnance(rs.getString("ordonnance"));
        c.setDosage(rs.getString("dosage"));
        c.setDureeTraitement(rs.getString("duree_traitement"));

        Date dateSql = rs.getDate("date_prochain_controle");
        if (dateSql != null) {
            c.setDateProchainControle(dateSql.toLocalDate());
        }

        c.setInstructionsControle(rs.getString("instructions_controle"));
        c.setStatut(rs.getString("statut"));
        return c;
    }
}