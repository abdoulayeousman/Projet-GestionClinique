package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Représente l'entité métier d'une consultation (Consultation) au sein du système.
 * Cette classe centralise l'ensemble des données cliniques recueillies par le médecin,
 * les diagnostics, les documents médicaux joints, les examens demandés, les ordonnances
 * émises ainsi que la planification du suivi du patient.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class Consultation {

    // ============ ATTRIBUTS ============

    private int consultationId;
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private LocalDateTime dateConsultation;
    private String dureeSymptomes;
    private int niveauDouleur;
    private String diagnostic;
    private String notesMedicales;
    private String cheminPhoto;
    private String ancienneOrdonnance;
    private String ancienResultatAnalyse;
    private String analyseDemandee;
    private String resultatAnalyse;
    private String ordonnance;
    private String dosage;
    private String dureeTraitement;
    private LocalDate dateProchainControle;
    private String instructionsControle;
    private String statut;
    private LocalDateTime createdAt;

    // ============ CONSTRUCTEURS ============

    /**
     * Constructeur par défaut (sans arguments).
     * Permet d'instancier une consultation vide avant de configurer ses valeurs.
     */
    public Consultation() {}

    /**
     * Constructeur complet permettant d'initialiser une consultation avec toutes ses données constitutives.
     */
    public Consultation(int consultationId, int appointmentId,
                        int patientId, int doctorId,
                        LocalDateTime dateConsultation,
                        String dureeSymptomes, int niveauDouleur,
                        String diagnostic, String notesMedicales,
                        String cheminPhoto, String ancienneOrdonnance,
                        String ancienResultatAnalyse,
                        String analyseDemandee, String resultatAnalyse,
                        String ordonnance, String dosage,
                        String dureeTraitement,
                        LocalDate dateProchainControle,
                        String instructionsControle,
                        String statut, LocalDateTime createdAt) {
        this.consultationId = consultationId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateConsultation = dateConsultation;
        this.dureeSymptomes = dureeSymptomes;
        this.niveauDouleur = niveauDouleur;
        this.diagnostic = diagnostic;
        this.notesMedicales = notesMedicales;
        this.cheminPhoto = cheminPhoto;
        this.ancienneOrdonnance = ancienneOrdonnance;
        this.ancienResultatAnalyse = ancienResultatAnalyse;
        this.analyseDemandee = analyseDemandee;
        this.resultatAnalyse = resultatAnalyse;
        this.ordonnance = ordonnance;
        this.dosage = dosage;
        this.dureeTraitement = dureeTraitement;
        this.dateProchainControle = dateProchainControle;
        this.instructionsControle = instructionsControle;
        this.statut = statut;
        this.createdAt = createdAt;
    }

    // ============ GETTERS ET SETTERS ============

    public int getConsultationId() { return consultationId; }
    public void setConsultationId(int consultationId) { this.consultationId = consultationId; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public LocalDateTime getDateConsultation() { return dateConsultation; }
    public void setDateConsultation(LocalDateTime dateConsultation) { this.dateConsultation = dateConsultation; }

    public String getDureeSymptomes() { return dureeSymptomes; }
    public void setDureeSymptomes(String dureeSymptomes) { this.dureeSymptomes = dureeSymptomes; }

    public int getNiveauDouleur() { return niveauDouleur; }
    public void setNiveauDouleur(int niveauDouleur) { this.niveauDouleur = niveauDouleur; }

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    public String getNotesMedicales() { return notesMedicales; }
    public void setNotesMedicales(String notesMedicales) { this.notesMedicales = notesMedicales; }

    public String getCheminPhoto() { return cheminPhoto; }
    public void setCheminPhoto(String cheminPhoto) { this.cheminPhoto = cheminPhoto; }

    public String getAncienneOrdonnance() { return ancienneOrdonnance; }
    public void setAncienneOrdonnance(String ancienneOrdonnance) { this.ancienneOrdonnance = ancienneOrdonnance; }

    public String getAncienResultatAnalyse() { return ancienResultatAnalyse; }
    public void setAncienResultatAnalyse(String ancienResultatAnalyse) { this.ancienResultatAnalyse = ancienResultatAnalyse; }

    public String getAnalyseDemandee() { return analyseDemandee; }
    public void setAnalyseDemandee(String analyseDemandee) { this.analyseDemandee = analyseDemandee; }

    public String getResultatAnalyse() { return resultatAnalyse; }
    public void setResultatAnalyse(String resultatAnalyse) { this.resultatAnalyse = resultatAnalyse; }

    public String getOrdonnance() { return ordonnance; }
    public void setOrdonnance(String ordonnance) { this.ordonnance = ordonnance; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getDureeTraitement() { return dureeTraitement; }
    public void setDureeTraitement(String dureeTraitement) { this.dureeTraitement = dureeTraitement; }

    public LocalDate getDateProchainControle() { return dateProchainControle; }
    public void setDateProchainControle(LocalDate dateProchainControle) { this.dateProchainControle = dateProchainControle; }

    public String getInstructionsControle() { return instructionsControle; }
    public void setInstructionsControle(String instructionsControle) { this.instructionsControle = instructionsControle; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ============ MÉTHODES UTILES ============

    /**
     * ✅ Valide les données critiques de la consultation.
     * @return true si les données sont valides.
     */
    public boolean estValide() {
        return consultationId > 0 && appointmentId > 0 && patientId > 0 && doctorId > 0 &&
                dateConsultation != null &&
                niveauDouleur >= 0 && niveauDouleur <= 10 &&
                diagnostic != null && !diagnostic.trim().isEmpty() &&
                statut != null && !statut.trim().isEmpty();
    }

    /**
     * ✅ Retourne une représentation textuelle de la consultation.
     */
    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + consultationId +
                ", appointment=" + appointmentId +
                ", patient=" + patientId +
                ", doctor=" + doctorId +
                ", date=" + dateConsultation +
                ", diagnostic='" + diagnostic + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}