package dao;

import model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pour l'entité {@link Doctor}.
 * Cette classe centralise toutes les opérations CRUD (Création, Lecture, Mise à jour, Suppression)
 * ainsi que les recherches spécifiques liées aux professionnels de santé dans la base de données.
 *
 * @author Abdoulaye Ousmane
 * @version 1.4
 */
public class DoctorDAO {

    // ============ LISTER TOUS LES MEDECINS ============

    /**
     * Récupère la liste complète de tous les médecins enregistrés dans la base de données.
     * Les résultats sont triés par identifiant unique dans un ordre décroissant.
     *
     * @return Une {@link List} d'objets {@link Doctor} ; la liste est vide si aucun enregistrement n'est trouvé.
     */
    public List<Doctor> tousLesMedecins() {
        List<Doctor> liste = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY doctor_id DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (conn == null) {
                System.err.println("[DoctorDAO] Erreur : connexion à la base de données impossible");
                return liste;
            }

            while (rs.next()) {
                liste.add(mapperDoctor(rs));
            }

        } catch (SQLException e) {
            System.err.println("[DoctorDAO] Erreur lors du listage des médecins : " + e.getMessage());
            e.printStackTrace();
        }
        return liste;
    }

    // ============ RECHERCHER PAR NOM ============

    /**
     * Recherche les médecins dont le nom complet contient une séquence de caractères spécifique.
     *
     * @param texte Le fragment de nom recherché.
     * @return Une {@link List} de médecins correspondants.
     */
    public List<Doctor> rechercherParNom(String texte) {
        List<Doctor> liste = new ArrayList<>();

        // ✅ Validation du paramètre
        if (texte == null || texte.trim().isEmpty()) {
            System.err.println("[DoctorDAO] Erreur : texte de recherche ne peut pas être vide");
            return liste;
        }

        String sql = "SELECT * FROM doctors WHERE full_name LIKE ? ORDER BY doctor_id DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("[DoctorDAO] Erreur : connexion à la base de données impossible");
                return liste;
            }

            stmt.setString(1, "%" + texte.trim() + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapperDoctor(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("[DoctorDAO] Erreur lors de la recherche par nom : " + e.getMessage());
            e.printStackTrace();
        }
        return liste;
    }

    // ============ RECHERCHER PAR ID UNIQUE ============

    /**
     * Recherche un médecin unique par son identifiant unique.
     * Cette méthode est utilisée par l'interface pour charger les détails avant modification.
     *
     * @param id L'identifiant unique du médecin.
     * @return L'instance {@link Doctor} si trouvée, sinon null.
     */
    public Doctor trouverParId(int id) {
        if (id <= 0) {
            System.err.println("[DoctorDAO] Erreur : doctor_id obligatoire et > 0");
            return null;
        }

        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("[DoctorDAO] Erreur : connexion à la base de données impossible");
                return null;
            }

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapperDoctor(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[DoctorDAO] Erreur lors de la recherche du médecin par ID : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ============ AJOUTER UN MEDECIN ============

    /**
     * Insère un nouveau médecin dans la base de données.
     * ✅ Validations : nom, license_number obligatoires
     *
     * @param d L'objet {@link Doctor} contenant les informations à insérer.
     * @return {@code true} si l'ajout a réussi ; {@code false} sinon.
     */
    public boolean ajouter(Doctor d) {
        // ✅ Validations métier
        if (d == null || d.getFullName() == null || d.getFullName().trim().isEmpty()) {
            System.err.println("[DoctorDAO] Erreur : nom complet obligatoire");
            return false;
        }

        if (d.getLicenseNumber() == null || d.getLicenseNumber().trim().isEmpty()) {
            System.err.println("[DoctorDAO] Erreur : numéro de licence obligatoire");
            return false;
        }

        if (d.getSpecialization() == null || d.getSpecialization().trim().isEmpty()) {
            System.err.println("[DoctorDAO] Erreur : spécialité obligatoire");
            return false;
        }

        String sql = "INSERT INTO doctors (full_name, phone_number, specialization, " +
                "license_number, grade, years_of_experience, service, bureau, " +
                "statut, jours_disponibles, heure_debut, heure_fin, pays, ville) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("[DoctorDAO] Erreur : connexion à la base de données impossible");
                return false;
            }

            stmt.setString(1, d.getFullName().trim());
            stmt.setString(2, d.getPhoneNumber() != null ? d.getPhoneNumber().trim() : null);
            stmt.setString(3, d.getSpecialization().trim());
            stmt.setString(4, d.getLicenseNumber().trim());
            stmt.setString(5, d.getGrade() != null ? d.getGrade() : null);
            stmt.setInt(6, d.getYearsOfExperience());
            stmt.setString(7, d.getService() != null ? d.getService() : null);
            stmt.setString(8, d.getBureau() != null ? d.getBureau() : null);
            stmt.setString(9, d.getStatut() != null ? d.getStatut() : "Disponible");
            stmt.setString(10, d.getJoursDisponibles() != null ? d.getJoursDisponibles() : null);

            if (d.getHeureDebut() != null) {
                stmt.setTime(11, Time.valueOf(d.getHeureDebut()));
            } else {
                stmt.setNull(11, Types.TIME);
            }

            if (d.getHeureFin() != null) {
                stmt.setTime(12, Time.valueOf(d.getHeureFin()));
            } else {
                stmt.setNull(12, Types.TIME);
            }

            stmt.setString(13, d.getPays() != null ? d.getPays() : "Togo");
            stmt.setString(14, d.getVille() != null ? d.getVille() : null);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DoctorDAO] Erreur lors de l'ajout : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ============ MODIFIER UN MEDECIN ============

    /**
     * Met à jour les informations d'un médecin existant.
     *
     * @param d L'instance de {@link Doctor} contenant les nouvelles données.
     * @return {@code true} si la mise à jour a réussi.
     */
    public boolean modifier(Doctor d) {
        // ✅ Validations métier
        if (d == null || d.getDoctorId() <= 0) {
            System.err.println("[DoctorDAO] Erreur : doctor_id obligatoire et > 0");
            return false;
        }

        if (d.getFullName() == null || d.getFullName().trim().isEmpty()) {
            System.err.println("[DoctorDAO] Erreur : nom complet obligatoire");
            return false;
        }

        String sql = "UPDATE doctors SET full_name = ?, phone_number = ?, " +
                "specialization = ?, grade = ?, years_of_experience = ?, " +
                "service = ?, bureau = ?, statut = ?, jours_disponibles = ?, " +
                "heure_debut = ?, heure_fin = ?, pays = ?, ville = ? " +
                "WHERE doctor_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("[DoctorDAO] Erreur : connexion à la base de données impossible");
                return false;
            }

            stmt.setString(1, d.getFullName().trim());
            stmt.setString(2, d.getPhoneNumber() != null ? d.getPhoneNumber().trim() : null);
            stmt.setString(3, d.getSpecialization() != null ? d.getSpecialization() : null);
            stmt.setString(4, d.getGrade() != null ? d.getGrade() : null);
            stmt.setInt(5, d.getYearsOfExperience());
            stmt.setString(6, d.getService() != null ? d.getService() : null);
            stmt.setString(7, d.getBureau() != null ? d.getBureau() : null);
            stmt.setString(8, d.getStatut() != null ? d.getStatut() : "Disponible");
            stmt.setString(9, d.getJoursDisponibles() != null ? d.getJoursDisponibles() : null);

            if (d.getHeureDebut() != null) {
                stmt.setTime(10, Time.valueOf(d.getHeureDebut()));
            } else {
                stmt.setNull(10, Types.TIME);
            }

            if (d.getHeureFin() != null) {
                stmt.setTime(11, Time.valueOf(d.getHeureFin()));
            } else {
                stmt.setNull(11, Types.TIME);
            }

            stmt.setString(12, d.getPays() != null ? d.getPays() : "Togo");
            stmt.setString(13, d.getVille() != null ? d.getVille() : null);
            stmt.setInt(14, d.getDoctorId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[DoctorDAO] Médecin ID " + d.getDoctorId() + " modifié avec succès");
                return true;
            } else {
                System.err.println("[DoctorDAO] Aucun médecin trouvé pour l'ID : " + d.getDoctorId());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("[DoctorDAO] Erreur lors de la modification : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ============ SUPPRIMER UN MEDECIN ============

    /**
     * Supprime le profil d'un médecin.
     * ⚠️ ATTENTION : Si des appointments existent, la suppression est refusée (RESTRICT).
     *
     * @param id L'identifiant unique du médecin.
     * @return {@code true} si la suppression a réussi.
     */
    public boolean supprimer(int id) {
        if (id <= 0) {
            System.err.println("[DoctorDAO] Erreur : doctor_id obligatoire et > 0");
            return false;
        }

        String sql = "DELETE FROM doctors WHERE doctor_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("[DoctorDAO] Erreur : connexion à la base de données impossible");
                return false;
            }

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("[DoctorDAO] Médecin ID " + id + " supprimé avec succès");
                return true;
            } else {
                System.err.println("[DoctorDAO] Aucun médecin trouvé pour l'ID : " + id);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("[DoctorDAO] Erreur lors de la suppression : " + e.getMessage());
            if (e.getMessage().contains("foreign key")) {
                System.err.println("[DoctorDAO] Impossible de supprimer : des rendez-vous sont liés à ce médecin");
            }
            e.printStackTrace();
            return false;
        }
    }

    // ============ MAPPER ============

    /**
     * Transforme une ligne de {@link ResultSet} en objet {@link Doctor}.
     *
     * @param rs Le résultat de la requête SQL.
     * @return L'objet {@link Doctor} mappé.
     * @throws SQLException En cas d'erreur de lecture.
     */
    private Doctor mapperDoctor(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setDoctorId(rs.getInt("doctor_id"));
        d.setFullName(rs.getString("full_name"));
        d.setPhoneNumber(rs.getString("phone_number"));
        d.setSpecialization(rs.getString("specialization"));
        d.setLicenseNumber(rs.getString("license_number"));
        d.setGrade(rs.getString("grade"));
        d.setYearsOfExperience(rs.getInt("years_of_experience"));
        d.setService(rs.getString("service"));
        d.setBureau(rs.getString("bureau"));
        d.setStatut(rs.getString("statut"));
        d.setJoursDisponibles(rs.getString("jours_disponibles"));

        Time heureDebut = rs.getTime("heure_debut");
        if (heureDebut != null) d.setHeureDebut(heureDebut.toLocalTime());

        Time heureFin = rs.getTime("heure_fin");
        if (heureFin != null) d.setHeureFin(heureFin.toLocalTime());

        d.setPays(rs.getString("pays"));
        d.setVille(rs.getString("ville"));
        return d;
    }
}