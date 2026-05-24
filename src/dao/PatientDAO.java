package dao;

import model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) qui assure la gestion de la persistance
 * et des opérations CRUD pour la table 'patients' dans la base de données.
 *
 * @author Abdoulaye Ousmane
 * @version 1.2
 */
public class PatientDAO {

    // ============ LISTER TOUS LES PATIENTS ============

    /**
     * Récupère la liste complète de tous les patients enregistrés.
     * * @return Une liste d'objets {@link Patient} triée par ID décroissant.
     */
    public List<Patient> tousLesPatients() {
        List<Patient> liste = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY patient_id DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (conn == null) {
                System.err.println("[PatientDAO] Erreur : connexion à la base de données impossible");
                return liste;
            }

            while (rs.next()) {
                liste.add(mapperPatient(rs));
            }

        } catch (SQLException e) {
            System.err.println("[PatientDAO] Erreur liste patients : " + e.getMessage());
            e.printStackTrace();
        }
        return liste;
    }

    // ============ RECHERCHER PAR ID ============

    /**
     * Recherche un patient unique par son identifiant unique.
     *
     * @param id L'identifiant unique (patient_id) du patient.
     * @return L'instance {@link Patient} correspondante si trouvée, sinon null.
     */
    public Patient trouverParId(int id) {
        if (id <= 0) {
            System.err.println("[PatientDAO] Erreur : patient_id obligatoire et > 0");
            return null;
        }

        String sql = "SELECT * FROM patients WHERE patient_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("[PatientDAO] Erreur : connexion à la base de données impossible");
                return null;
            }

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapperPatient(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[PatientDAO] Erreur recherche patient par ID : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ============ RECHERCHER PAR NOM ============

    /**
     * Recherche les patients dont le nom contient la chaîne fournie.
     *
     * @param texte Chaîne de caractères de recherche.
     * @return Une liste de patients filtrée.
     */
    public List<Patient> rechercherParNom(String texte) {
        List<Patient> liste = new ArrayList<>();

        // ✅ Validation du paramètre
        if (texte == null || texte.trim().isEmpty()) {
            System.err.println("[PatientDAO] Erreur : texte de recherche ne peut pas être vide");
            return liste;
        }

        String sql = "SELECT * FROM patients WHERE full_name LIKE ? ORDER BY patient_id DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("[PatientDAO] Erreur : connexion à la base de données impossible");
                return liste;
            }

            stmt.setString(1, "%" + texte.trim() + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapperPatient(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("[PatientDAO] Erreur recherche patients : " + e.getMessage());
            e.printStackTrace();
        }
        return liste;
    }

    // ============ AJOUTER UN NOUVEAU PATIENT ============

    /**
     * Enregistre un nouveau patient dans la base de données.
     * ✅ Validations : nom, date naissance, genre obligatoires
     *
     * @param p Objet {@link Patient} à insérer.
     * @return true si l'opération a réussi, false sinon.
     */
    public boolean ajouter(Patient p) {
        // ✅ Validations métier
        if (p == null || p.getNomComplet() == null || p.getNomComplet().trim().isEmpty()) {
            System.err.println("[PatientDAO] Erreur : nom complet obligatoire");
            return false;
        }

        if (p.getDateNaissance() == null) {
            System.err.println("[PatientDAO] Erreur : date naissance obligatoire");
            return false;
        }

        if (p.getSexe() == null || p.getSexe().trim().isEmpty()) {
            System.err.println("[PatientDAO] Erreur : genre obligatoire");
            return false;
        }

        String sql = "INSERT INTO patients (full_name, date_of_birth, gender, phone_number, service, pays) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("[PatientDAO] Erreur : connexion à la base de données impossible");
                return false;
            }

            stmt.setString(1, p.getNomComplet().trim());
            stmt.setDate(2, Date.valueOf(p.getDateNaissance()));
            stmt.setString(3, p.getSexe());
            stmt.setString(4, p.getTelephone() != null ? p.getTelephone().trim() : null);
            stmt.setString(5, p.getService() != null ? p.getService() : null);
            stmt.setString(6, p.getPays() != null ? p.getPays() : "Togo");

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[PatientDAO] Erreur ajout patient : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ============ MODIFIER UN PATIENT ============

    /**
     * Met à jour les informations d'un patient existant.
     * ✅ Vérifie que l'ID patient existe
     *
     * @param p Objet {@link Patient} mis à jour.
     * @return true si la mise à jour est effectuée.
     */
    public boolean modifier(Patient p) {
        // ✅ Validations métier
        if (p == null || p.getPatientId() <= 0) {
            System.err.println("[PatientDAO] Erreur : patient_id obligatoire et > 0");
            return false;
        }

        if (p.getNomComplet() == null || p.getNomComplet().trim().isEmpty()) {
            System.err.println("[PatientDAO] Erreur : nom complet obligatoire");
            return false;
        }

        if (p.getDateNaissance() == null) {
            System.err.println("[PatientDAO] Erreur : date naissance obligatoire");
            return false;
        }

        String sql = "UPDATE patients SET full_name = ?, date_of_birth = ?, gender = ?, phone_number = ?, service = ?, pays = ? WHERE patient_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("[PatientDAO] Erreur : connexion à la base de données impossible");
                return false;
            }

            stmt.setString(1, p.getNomComplet().trim());
            stmt.setDate(2, Date.valueOf(p.getDateNaissance()));
            stmt.setString(3, p.getSexe());
            stmt.setString(4, p.getTelephone() != null ? p.getTelephone().trim() : null);
            stmt.setString(5, p.getService() != null ? p.getService() : null);
            stmt.setString(6, p.getPays() != null ? p.getPays() : "Togo");
            stmt.setInt(7, p.getPatientId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[PatientDAO] Patient ID " + p.getPatientId() + " modifié avec succès");
                return true;
            } else {
                System.err.println("[PatientDAO] Aucun patient trouvé pour l'ID : " + p.getPatientId());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("[PatientDAO] Erreur modification patient : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ============ SUPPRIMER ============

    /**
     * Supprime un patient de la base de données par son ID.
     * ⚠️ ATTENTION : Supprime également les rendez-vous associés (CASCADE).
     *
     * @param id Identifiant du patient.
     * @return true si la suppression est réussie.
     */
    public boolean supprimer(int id) {
        if (id <= 0) {
            System.err.println("[PatientDAO] Erreur : patient_id obligatoire et > 0");
            return false;
        }

        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("[PatientDAO] Erreur : connexion à la base de données impossible");
                return false;
            }

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("[PatientDAO] Patient ID " + id + " supprimé avec succès (CASCADE)");
                return true;
            } else {
                System.err.println("[PatientDAO] Aucun patient trouvé pour l'ID : " + id);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("[PatientDAO] Erreur suppression patient : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ============ MAPPER ============

    /**
     * Transforme une ligne de {@link ResultSet} en objet {@link Patient}.
     *
     * @param rs Le résultat de la requête SQL.
     * @return L'objet {@link Patient} mappé.
     * @throws SQLException En cas d'erreur de lecture.
     */
    private Patient mapperPatient(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setPatientId(rs.getInt("patient_id"));
        p.setNomComplet(rs.getString("full_name"));

        Date dateSql = rs.getDate("date_of_birth");
        if (dateSql != null) {
            p.setDateNaissance(dateSql.toLocalDate());
        }

        p.setSexe(rs.getString("gender"));
        p.setTelephone(rs.getString("phone_number"));
        p.setService(rs.getString("service"));
        p.setPays(rs.getString("pays"));

        return p;
    }
}