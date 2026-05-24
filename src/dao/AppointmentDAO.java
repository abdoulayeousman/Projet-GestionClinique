package dao;

import model.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pour l'entité {@link Appointment}.
 * Cette classe orchestre la persistance des données liées aux rendez-vous,
 * incluant la gestion des états, le suivi des paiements et la recherche multicritères.
 *
 * @author Abdoulaye Ousmane
 * @version 1.4
 */
public class AppointmentDAO {

    /**
     * Récupère la liste exhaustive des rendez-vous stockés en base.
     *
     * @return Une {@link List} contenant toutes les instances de {@link Appointment}.
     */
    public List<Appointment> tousLesRendezVous() {
        List<Appointment> liste = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_id DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                liste.add(mapperAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("[AppointmentDAO] Erreur lors du listage : " + e.getMessage());
            e.printStackTrace();
        }
        return liste;
    }

    /**
     * Recherche les rendez-vous associés à un patient spécifique via une jointure SQL.
     * Cette méthode permet de filtrer les rendez-vous par le nom du patient.
     *
     * @param nomPatient Le nom ou fragment de nom du patient recherché.
     * @return Une {@link List} de rendez-vous correspondant au nom du patient.
     */
    public List<Appointment> rechercherParPatient(String nomPatient) {
        List<Appointment> liste = new ArrayList<>();
        // Jointure pour filtrer les rendez-vous via le nom du patient
        String sql = "SELECT a.* FROM appointments a JOIN patients p ON a.patient_id = p.patient_id " +
                "WHERE p.full_name LIKE ? ORDER BY a.appointment_id DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomPatient + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapperAppointment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[AppointmentDAO] Erreur lors de la recherche par patient : " + e.getMessage());
            e.printStackTrace();
        }
        return liste;
    }

    /**
     * Persiste un nouveau rendez-vous dans la base de données.
     *
     * @param a L'instance du rendez-vous à enregistrer.
     * @return {@code true} si l'insertion est effective ; {@code false} sinon.
     */
    public boolean ajouter(Appointment a) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, " +
                "type_consultation, reason_for_visit, prix_consultation, mode_paiement, " +
                "statut_paiement, status, numero_recu, statut_notification) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, a.getPatientId());
            stmt.setInt(2, a.getDoctorId());
            stmt.setDate(3, Date.valueOf(a.getAppointmentDate()));
            stmt.setTime(4, Time.valueOf(a.getAppointmentTime()));
            stmt.setString(5, a.getTypeConsultation());
            stmt.setString(6, a.getReasonForVisit());
            stmt.setDouble(7, a.getPrixConsultation());
            stmt.setString(8, a.getModePaiement());
            stmt.setString(9, a.getStatutPaiement());
            stmt.setString(10, a.getStatus());
            stmt.setString(11, a.getNumeroRecu());
            stmt.setString(12, a.getStatutNotification());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AppointmentDAO] Erreur lors de l'ajout : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour les informations d'un rendez-vous existant.
     *
     * @param a Le rendez-vous mis à jour.
     * @return {@code true} si la mise à jour a réussi.
     */
    public boolean modifier(Appointment a) {
        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, appointment_date=?, appointment_time=?, " +
                "type_consultation=?, reason_for_visit=?, prix_consultation=?, mode_paiement=?, " +
                "statut_paiement=?, status=?, numero_recu=? WHERE appointment_id=?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, a.getPatientId());
            stmt.setInt(2, a.getDoctorId());
            stmt.setDate(3, Date.valueOf(a.getAppointmentDate()));
            stmt.setTime(4, Time.valueOf(a.getAppointmentTime()));
            stmt.setString(5, a.getTypeConsultation());
            stmt.setString(6, a.getReasonForVisit());
            stmt.setDouble(7, a.getPrixConsultation());
            stmt.setString(8, a.getModePaiement());
            stmt.setString(9, a.getStatutPaiement());
            stmt.setString(10, a.getStatus());
            stmt.setString(11, a.getNumeroRecu());
            stmt.setInt(12, a.getAppointmentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AppointmentDAO] Erreur lors de la modification : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un rendez-vous selon son ID.
     *
     * @param id L'identifiant unique du rendez-vous.
     * @return {@code true} si l'enregistrement est supprimé.
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AppointmentDAO] Erreur lors de la suppression : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Méthode utilitaire de mapping pour convertir une ligne SQL en objet Java.
     *
     * @param rs Le ResultSet courant à transformer.
     * @return Une instance {@link Appointment} peuplée.
     * @throws SQLException Si une colonne est inaccessible.
     */
    private Appointment mapperAppointment(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setPatientId(rs.getInt("patient_id"));
        a.setDoctorId(rs.getInt("doctor_id"));
        if (rs.getDate("appointment_date") != null) a.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
        if (rs.getTime("appointment_time") != null) a.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
        a.setTypeConsultation(rs.getString("type_consultation"));
        a.setReasonForVisit(rs.getString("reason_for_visit"));
        a.setPrixConsultation(rs.getDouble("prix_consultation"));
        a.setModePaiement(rs.getString("mode_paiement"));
        a.setStatutPaiement(rs.getString("statut_paiement"));
        a.setStatus(rs.getString("status"));
        a.setNumeroRecu(rs.getString("numero_recu"));
        a.setStatutNotification(rs.getString("statut_notification"));
        return a;
    }
}