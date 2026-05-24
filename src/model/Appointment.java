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

    /**
     * Récupère l'identifiant unique du rendez-vous.
     * @return L'identifiant du rendez-vous.
     */
    public int getAppointmentId() { return appointmentId; }

    /**
     * Définit l'identifiant unique du rendez-vous.
     * @param appointmentId Le nouvel identifiant du rendez-vous.
     */
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    /**
     * Récupère l'identifiant du patient concerné.
     * @return L'identifiant du patient.
     */
    public int getPatientId() { return patientId; }

    /**
     * Assigne un patient au rendez-vous via son identifiant.
     * @param patientId L'identifiant du patient.
     */
    public void setPatientId(int patientId) { this.patientId = patientId; }

    /**
     * Récupère l'identifiant du médecin assigné.
     * @return L'identifiant du médecin.
     */
    public int getDoctorId() { return doctorId; }

    /**
     * Assigne un médecin au rendez-vous via son identifiant.
     * @param doctorId L'identifiant du médecin.
     */
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    /**
     * Récupère la date fixée pour la consultation.
     * @return La date du rendez-vous (LocalDate).
     */
    public LocalDate getAppointmentDate() { return appointmentDate; }

    /**
     * Modifie la date de la consultation.
     * @param appointmentDate La nouvelle date du rendez-vous.
     */
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    /**
     * Récupère l'heure planifiée pour la consultation.
     * @return L'heure du rendez-vous (LocalTime).
     */
    public LocalTime getAppointmentTime() { return appointmentTime; }

    /**
     * Modifie l'heure de la consultation.
     * @param appointmentTime La nouvelle heure du rendez-vous.
     */
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    /**
     * Récupère le statut opérationnel du rendez-vous.
     * @return Le statut du rendez-vous.
     */
    public String getStatus() { return status; }

    /**
     * Modifie le statut opérationnel du rendez-vous.
     * @param status Le nouveau statut (ex: "Completed", "Cancelled").
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Récupère la typologie ou nature de la consultation.
     * @return Le type de consultation.
     */
    public String getTypeConsultation() { return typeConsultation; }

    /**
     * Définit la typologie ou nature de la consultation.
     * @param typeConsultation Le nouveau type (ex: "Suivi", "Urgence").
     */
    public void setTypeConsultation(String typeConsultation) { this.typeConsultation = typeConsultation; }

    /**
     * Récupère le motif médical de la visite.
     * @return La raison de la consultation.
     */
    public String getReasonForVisit() { return reasonForVisit; }

    /**
     * Enregistre le motif médical de la visite.
     * @param reasonForVisit La raison invoquée pour la consultation.
     */
    public void setReasonForVisit(String reasonForVisit) { this.reasonForVisit = reasonForVisit; }

    /**
     * Récupère les annotations cliniques prises par le médecin.
     * @return Le texte des notes médicales.
     */
    public String getNotesMedicales() { return notesMedicales; }

    /**
     * Permet au médecin d'ajouter ou modifier des annotations cliniques.
     * @param notesMedicales Les comptes-rendus ou observations.
     */
    public void setNotesMedicales(String notesMedicales) { this.notesMedicales = notesMedicales; }

    /**
     * Récupère la tarification appliquée à l'acte médical.
     * @return Le prix en FCFA.
     */
    public double getPrixConsultation() { return prixConsultation; }

    /**
     * Ajuste la tarification de l'acte médical.
     * @param prixConsultation Le montant de l'acte en FCFA.
     */
    public void setPrixConsultation(double prixConsultation) { this.prixConsultation = prixConsultation; }

    /**
     * Récupère le mode de règlement de la facture.
     * @return Le mode de paiement.
     */
    public String getModePaiement() { return modePaiement; }

    /**
     * Définit le mode de règlement utilisé pour la facture.
     * @param modePaiement Le mode de paiement sélectionné (ex: "T-Money").
     */
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    /**
     * Récupère le statut financier du paiement.
     * @return L'état du paiement.
     */
    public String getStatutPaiement() { return statutPaiement; }

    /**
     * Modifie l'état financier du paiement.
     * @param statutPaiement Le nouvel état (ex: "Paye", "En attente").
     */
    public void setStatutPaiement(String statutPaiement) { this.statutPaiement = statutPaiement; }

    /**
     * Récupère la référence alphanumérique du reçu d'encaissement.
     * @return Le numéro du reçu de caisse.
     */
    public String getNumeroRecu() { return numeroRecu; }

    /**
     * Assigne un numéro de pièce comptable justificatif au rendez-vous.
     * @param numeroRecu La chaîne de caractères du reçu.
     */
    public void setNumeroRecu(String numeroRecu) { this.numeroRecu = numeroRecu; }

    /**
     * Récupère l'état de transmission de la notification associée.
     * @return Le statut de la notification.
     */
    public String getStatutNotification() { return statutNotification; }

    /**
     * Modifie l'état de transmission de la notification associée.
     * @param statutNotification Le nouveau statut d'envoi.
     */
    public void setStatutNotification(String statutNotification) { this.statutNotification = statutNotification; }

    /**
     * Récupère la date et l'heure exactes de l'enregistrement système.
     * @return L'horodatage de création (LocalDateTime).
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Fixe le moment d'enregistrement initial du rendez-vous dans le système.
     * @param createdAt Le timestamp de création.
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}