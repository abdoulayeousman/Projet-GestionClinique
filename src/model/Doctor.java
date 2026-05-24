package model;

import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Représente l'entité métier d'un médecin (Doctor) au sein du système.
 * Cette classe contient toutes les informations personnelles, professionnelles,
 * de localisation et de disponibilité d'un médecin, ainsi que son lien avec un compte utilisateur.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class Doctor {

    // ============ ATTRIBUTS ============

    private int doctorId;
    private int userId;
    private String fullName;
    private String phoneNumber;
    private String specialization;
    private String licenseNumber;
    private String grade;
    private int yearsOfExperience;
    private String service;
    private String bureau;
    private String statut;
    private String joursDisponibles;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String pays;
    private String ville;
    private LocalDateTime createdAt;

    // ============ CONSTRUCTEURS ============

    /**
     * Constructeur par défaut (sans arguments).
     * Permet d'instancier un médecin vierge avant de renseigner ses attributs via les setters.
     */
    public Doctor() {}

    /**
     * Constructeur complet permettant d'initialiser un médecin avec l'ensemble de ses informations.
     */
    public Doctor(int doctorId, int userId,
                  String fullName, String phoneNumber,
                  String specialization, String licenseNumber,
                  String grade, int yearsOfExperience,
                  String service, String bureau,
                  String statut, String joursDisponibles,
                  LocalTime heureDebut, LocalTime heureFin,
                  String pays, String ville,
                  LocalDateTime createdAt) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.grade = grade;
        this.yearsOfExperience = yearsOfExperience;
        this.service = service;
        this.bureau = bureau;
        this.statut = statut;
        this.joursDisponibles = joursDisponibles;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.pays = pays;
        this.ville = ville;
        this.createdAt = createdAt;
    }

    // ============ GETTERS ET SETTERS ============

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getBureau() { return bureau; }
    public void setBureau(String bureau) { this.bureau = bureau; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getJoursDisponibles() { return joursDisponibles; }
    public void setJoursDisponibles(String joursDisponibles) { this.joursDisponibles = joursDisponibles; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ============ MÉTHODES UTILES ============

    /**
     * ✅ Valide les données critiques du médecin.
     * @return true si les données sont valides.
     */
    public boolean estValide() {
        return doctorId > 0 &&
                fullName != null && !fullName.trim().isEmpty() &&
                licenseNumber != null && !licenseNumber.trim().isEmpty() &&
                specialization != null && !specialization.trim().isEmpty() &&
                yearsOfExperience >= 0 && yearsOfExperience <= 80;
    }

    /**
     * ✅ Vérifie si le médecin est actuellement disponible.
     * @return true si le statut est "Disponible".
     */
    public boolean estDisponible() {
        return statut != null && "Disponible".equalsIgnoreCase(statut);
    }

    /**
     * ✅ Retourne une représentation textuelle du médecin.
     */
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + doctorId +
                ", nom='" + fullName + '\'' +
                ", specialite='" + specialization + '\'' +
                ", licence='" + licenseNumber + '\'' +
                ", statut='" + statut + '\'' +
                ", service='" + service + '\'' +
                '}';
    }
}