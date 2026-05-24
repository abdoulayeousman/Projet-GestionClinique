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

    /** Identifiant unique du médecin en base de données. */
    private int doctorId;

    /** Identifiant de l'utilisateur associé à ce médecin pour la gestion des accès et de la connexion. */
    private int userId;

    /** Nom complet du médecin (Nom et Prénoms). */
    private String fullName;

    /** Numéro de téléphone de contact du médecin. */
    private String phoneNumber;

    /** Spécialité médicale du médecin (ex: "Cardiologue", "Pédiatre", etc.). */
    private String specialization;

    /** Numéro d'ordre national ou numéro de licence obligatoire pour exercer. */
    private String licenseNumber;

    /** Grade académique ou professionnel du médecin (ex: "Interne", "Assistant", "Professeur"). */
    private String grade;

    /** Nombre d'années d'expérience professionnelle du médecin. */
    private int yearsOfExperience;

    /** Service médical d'affectation au sein de la clinique (ex: "Urgences", "Cardiologie", etc.). */
    private String service;

    /** Emplacement ou numéro de bureau affecté (ex: "Bureau 12", "Salle 3"). */
    private String bureau;

    /** Statut actuel d'activité du médecin (ex: "Disponible", "En consultation", "Absent", "En conge"). */
    private String statut;

    /** Liste ou chaîne décrivant les jours de la semaine où le médecin consulte (ex: "Lundi, Mercredi, Vendredi"). */
    private String joursDisponibles;

    /** Heure de début standard des consultations (ex: 08:00). */
    private LocalTime heureDebut;

    /** Heure de fin standard des consultations (ex: 17:00). */
    private LocalTime heureFin;

    /** Pays d'origine ou d'exercice (issu d'une énumération des pays d'Afrique). */
    private String pays;

    /** Ville de résidence ou d'exercice du médecin. */
    private String ville;

    /** Date et heure de création de la fiche du médecin dans le système. */
    private LocalDateTime createdAt;

    // ============ CONSTRUCTEURS ============

    /**
     * Constructeur par défaut (sans arguments).
     * Permet d'instancier un médecin vierge avant de renseigner ses attributs via les setters.
     */
    public Doctor() {}

    /**
     * Constructeur complet permettant d'initialiser un médecin avec l'ensemble de ses informations.
     *
     * @param doctorId Identifiant unique du médecin.
     * @param userId Identifiant de l'utilisateur lié.
     * @param fullName Nom complet du médecin.
     * @param phoneNumber Numéro de téléphone.
     * @param specialization Spécialité médicale.
     * @param licenseNumber Numéro de licence/ordre.
     * @param grade Grade du médecin.
     * @param yearsOfExperience Nombre d'années d'expérience.
     * @param service Service d'affectation.
     * @param bureau Bureau ou salle attribuée.
     * @param statut Statut de disponibilité actuel.
     * @param joursDisponibles Jours de consultation.
     * @param heureDebut Heure de début de service.
     * @param heureFin Heure de fin de service.
     * @param pays Pays d'origine.
     * @param ville Ville de résidence.
     * @param createdAt Horodatage de création du profil.
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

    /**
     * Récupère l'identifiant unique du médecin.
     * @return L'identifiant du médecin.
     */
    public int getDoctorId() { return doctorId; }

    /**
     * Définit l'identifiant unique du médecin.
     * @param doctorId Le nouvel identifiant du médecin.
     */
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    /**
     * Récupère l'identifiant de l'utilisateur associé.
     * @return L'identifiant utilisateur.
     */
    public int getUserId() { return userId; }

    /**
     * Associe un identifiant utilisateur à ce médecin.
     * @param userId Le nouvel identifiant utilisateur.
     */
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * Récupère le nom complet du médecin.
     * @return Le nom complet.
     */
    public String getFullName() { return fullName; }

    /**
     * Définit le nom complet du médecin.
     * @param fullName Le nouveau nom complet.
     */
    public void setFullName(String fullName) { this.fullName = fullName; }

    /**
     * Récupère le numéro de téléphone du médecin.
     * @return Le numéro de téléphone.
     */
    public String getPhoneNumber() { return phoneNumber; }

    /**
     * Définit le numéro de téléphone du médecin.
     * @param phoneNumber Le nouveau numéro de téléphone.
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /**
     * Récupère la spécialité médicale du médecin.
     * @return La spécialisation.
     */
    public String getSpecialization() { return specialization; }

    /**
     * Définit la spécialité médicale du médecin.
     * @param specialization La nouvelle spécialisation.
     */
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    /**
     * Récupère le numéro de licence professionnelle.
     * @return Le numéro de licence.
     */
    public String getLicenseNumber() { return licenseNumber; }

    /**
     * Définit le numéro de licence professionnelle.
     * @param licenseNumber Le nouveau numéro de licence.
     */
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    /**
     * Récupère le grade du médecin.
     * @return Le grade actuel.
     */
    public String getGrade() { return grade; }

    /**
     * Définit le grade du médecin.
     * @param grade Le nouveau grade.
     */
    public void setGrade(String grade) { this.grade = grade; }

    /**
     * Récupère le nombre d'années d'expérience.
     * @return Le nombre d'années d'expérience.
     */
    public int getYearsOfExperience() { return yearsOfExperience; }

    /**
     * Définit le nombre d'années d'expérience.
     * @param yearsOfExperience Le nouveau nombre d'années d'expérience.
     */
    public void setYearsOfExperience(int yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    /**
     * Récupère le service hospitalier d'affectation.
     * @return Le nom du service.
     */
    public String getService() { return service; }

    /**
     * Définit le service hospitalier d'affectation.
     * @param service Le nouveau nom du service.
     */
    public void setService(String service) { this.service = service; }

    /**
     * Récupère le bureau ou la salle de consultation.
     * @return Le libellé du bureau.
     */
    public String getBureau() { return bureau; }

    /**
     * Configuration du bureau ou de la salle de consultation.
     * @param bureau Le nouveau libellé du bureau.
     */
    public void setBureau(String bureau) { this.bureau = bureau; }

    /**
     * Récupère le statut actuel de disponibilité.
     * @return Le statut.
     */
    public String getStatut() { return statut; }

    /**
     * Met à jour le statut de disponibilité du médecin.
     * @param statut Le nouveau statut.
     */
    public void setStatut(String statut) { this.statut = statut; }

    /**
     * Récupère les jours de la semaine où le médecin est disponible.
     * @return Les jours disponibles sous forme de texte.
     */
    public String getJoursDisponibles() { return joursDisponibles; }

    /**
     * Définit les jours de la semaine où le médecin est disponible.
     * @param joursDisponibles Les nouveaux jours disponibles.
     */
    public void setJoursDisponibles(String joursDisponibles) { this.joursDisponibles = joursDisponibles; }

    /**
     * Récupère l'heure de début des services.
     * @return L'heure de début.
     */
    public LocalTime getHeureDebut() { return heureDebut; }

    /**
     * Ajuste l'heure de début des services.
     * @param heureDebut La nouvelle heure de début.
     */
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    /**
     * Récupère l'heure de fin des services.
     * @return L'heure de fin.
     */
    public LocalTime getHeureFin() { return heureFin; }

    /**
     * Ajuste l'heure de fin des services.
     * @param heureFin La nouvelle heure de fin.
     */
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    /**
     * Récupère le pays d'origine ou d'exercice.
     * @return Le pays.
     */
    public String getPays() { return pays; }

    /**
     * Définit le pays d'origine ou d'exercice.
     * @param pays Le nouveau pays.
     */
    public void setPays(String pays) { this.pays = pays; }

    /**
     * Récupère la ville d'activité.
     * @return La ville.
     */
    public String getVille() { return ville; }

    /**
     * Définit la ville d'activité.
     * @param ville La nouvelle ville.
     */
    public void setVille(String ville) { this.ville = ville; }

    /**
     * Récupère la date et l'heure d'enregistrement du profil.
     * @return Le moment de création sous forme de LocalDateTime.
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Spécifie la date et l'heure d'enregistrement du profil.
     * @param createdAt Le moment de création.
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}