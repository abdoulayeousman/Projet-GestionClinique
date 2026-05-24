package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Représente l'entité métier d'un rendez-vous (Appointment) au sein du système.
 * Cette classe lie un patient à un médecin à une date et heure précises, et gère
 * les aspects cliniques, financiers ainsi que le suivi des notifications.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class Appointment {

    // ============ ATTRIBUTS ============

    /** Identifiant unique du rendez-vous dans la base de données. */
    private int appointmentId;

    /** Identifiant unique du patient concerné par le rendez-vous. */
    private int patientId;

    /** Identifiant unique du médecin assigné à la consultation. */
    private int doctorId;

    /** Date fixée pour le rendez-vous (LocalDate). */
    private LocalDate appointmentDate;

    /** Heure précise fixée pour le rendez-vous (LocalTime). */
    private LocalTime appointmentTime;

    /** Statut actuel du rendez-vous (ex: "Scheduled", "Completed", "Cancelled", "No-show"). */
    private String status;

    /** Type de la consultation médicale (ex: "Premiere visite", "Suivi", "Urgence"). */
    private String typeConsultation;

    /** Motif ou raison de la visite exprimé par le patient. */
    private String reasonForVisit;

    /** Compte-rendu et observations consignés par le médecin à l'issue de la visite. */
    private String notesMedicales;

    /** Prix de l'acte de consultation exprimé en FCFA. */
    private double prixConsultation;

    /** Moyen financier utilisé pour régler la consultation (ex: "Especes", "T-Money", "Flooz", etc.). */
    private String modePaiement;

    /** État d'avancement du règlement financier (ex: "Paye", "Non paye", "En attente"). */
    private String statutPaiement;

    /** Numéro de pièce comptable unique généré lors de la transaction (ex: "REC-2026-001"). */
    private String numeroRecu;

    /** État d'acheminement du message de rappel au patient (ex: "Non envoyee", "Envoyee", "Echouee"). */
    private String statutNotification;

    /** Horodatage précis marquant l'enregistrement initial de la demande de rendez-vous. */
    private LocalDateTime createdAt;

    // ============ CONSTRUCTEURS ============

    /**
     * Constructeur par défaut (sans arguments).
     * Permet d'instancier un objet de rendez-vous vide avant de configurer ses propriétés.
     */
    public Appointment() {}

    /**
     * Constructeur complet permettant d'initialiser un rendez-vous avec toutes ses données constitutives.
     *
     * @param appointmentId Identifiant unique du rendez-vous.
     * @param patientId Identifiant du patient.
     * @param doctorId Identifiant du médecin.
     * @param appointmentDate Date du rendez-vous.
     * @param appointmentTime Heure du rendez-vous.
     * @param status Statut du rendez-vous.
     * @param typeConsultation Type ou catégorie de la consultation.
     * @param reasonForVisit Motif détaillé de la visite.
     * @param notesMedicales Notes de compte-rendu du médecin.
     * @param prixConsultation Prix de la consultation en FCFA.
     * @param modePaiement Mode ou canal de paiement.
     * @param statutPaiement Statut du règlement financier.
     * @param numeroRecu Référence ou numéro du reçu.
     * @param statutNotification Statut de l'envoi de la notification.
     * @param createdAt Horodatage d'enregistrement du rendez-vous.
     */
    public Appointment(int appointmentId, int patientId, int doctorId,
                       LocalDate appointmentDate, LocalTime appointmentTime,
                       String status, String typeConsultation,
                       String reasonForVisit, String notesMedicales,
                       double prixConsultation, String modePaiement,
                       String statutPaiement, String numeroRecu,
                       String statutNotification, LocalDateTime createdAt) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.typeConsultation = typeConsultation;
        this.reasonForVisit = reasonForVisit;
        this.notesMedicales = notesMedicales;
        this.prixConsultation = prixConsultation;
        this.modePaiement = modePaiement;
        this.statutPaiement = statutPaiement;
        this.numeroRecu = numeroRecu;
        this.statutNotification = statutNotification;
        this.createdAt = createdAt;
    }

    // ============ GETTERS ET SETTERS ============

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTypeConsultation() { return typeConsultation; }
    public void setTypeConsultation(String typeConsultation) { this.typeConsultation = typeConsultation; }

    public String getReasonForVisit() { return reasonForVisit; }
    public void setReasonForVisit(String reasonForVisit) { this.reasonForVisit = reasonForVisit; }

    public String getNotesMedicales() { return notesMedicales; }
    public void setNotesMedicales(String notesMedicales) { this.notesMedicales = notesMedicales; }

    public double getPrixConsultation() { return prixConsultation; }
    public void setPrixConsultation(double prixConsultation) { this.prixConsultation = prixConsultation; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public String getStatutPaiement() { return statutPaiement; }
    public void setStatutPaiement(String statutPaiement) { this.statutPaiement = statutPaiement; }

    public String getNumeroRecu() { return numeroRecu; }
    public void setNumeroRecu(String numeroRecu) { this.numeroRecu = numeroRecu; }

    public String getStatutNotification() { return statutNotification; }
    public void setStatutNotification(String statutNotification) { this.statutNotification = statutNotification; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ============ MÉTHODES UTILES ============

    /**
     * ✅ Valide les données critiques du rendez-vous.
     * @return true si les données sont valides.
     */
    public boolean estValide() {
        return appointmentId > 0 && patientId > 0 && doctorId > 0 &&
                appointmentDate != null && appointmentTime != null &&
                status != null && !status.trim().isEmpty() &&
                prixConsultation >= 0;
    }

    /**
     * ✅ Retourne une représentation textuelle du rendez-vous.
     */
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + appointmentId +
                ", patient=" + patientId +
                ", doctor=" + doctorId +
                ", date=" + appointmentDate +
                ", time=" + appointmentTime +
                ", status='" + status + '\'' +
                ", prix=" + prixConsultation +
                '}';
    }
}