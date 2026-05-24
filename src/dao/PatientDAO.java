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

            while (rs.next()) {
                liste.add(mapperPatient(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erreur liste patients : " + e.getMessage());
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
        String sql = "SELECT * FROM patients WHERE patient_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapperPatient(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur recherche patient par ID : " + e.getMessage());
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
        String sql = "SELECT * FROM patients WHERE full_name LIKE ? ORDER BY patient_id DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + texte + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapperPatient(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur recherche patients : " + e.getMessage());
        }
        return liste;
    }

    // ============ AJOUTER UN NOUVEAU PATIENT ============

    /**
     * Enregistre un nouveau patient dans la base de données.
     *
     * @param p Objet {@link Patient} à insérer.
     * @return true si l'opération a réussi, false sinon.
     */
    public boolean ajouter(Patient p) {
        String sql = "INSERT INTO patients (full_name, date_of_birth, gender, phone_number, service, pays) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNomComplet());
            if (p.getDateNaissance() != null) {
                stmt.setDate(2, Date.valueOf(p.getDateNaissance()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            stmt.setString(3, p.getSexe());
            stmt.setString(4, p.getTelephone());
            stmt.setString(5, p.getService());
            stmt.setString(6, p.getPays());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur ajout patient : " + e.getMessage());
            return false;
        }
    }

    // ============ MODIFIER UN PATIENT ============

    /**
     * Met à jour les informations d'un patient existant.
     *
     * @param p Objet {@link Patient} mis à jour.
     * @return true si la mise à jour est effectuée.
     */
    public boolean modifier(Patient p) {
        String sql = "UPDATE patients SET full_name = ?, date_of_birth = ?, gender = ?, phone_number = ?, service = ?, pays = ? WHERE patient_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNomComplet());
            if (p.getDateNaissance() != null) {
                stmt.setDate(2, Date.valueOf(p.getDateNaissance()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            stmt.setString(3, p.getSexe());
            stmt.setString(4, p.getTelephone());
            stmt.setString(5, p.getService());
            stmt.setString(6, p.getPays());
            stmt.setInt(7, p.getPatientId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur modification patient : " + e.getMessage());
            return false;
        }
    }

    // ============ SUPPRIMER ============

    /**
     * Supprime un patient de la base de données par son ID.
     *
     * @param id Identifiant du patient.
     * @return true si la suppression est réussie.
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression patient : " + e.getMessage());
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