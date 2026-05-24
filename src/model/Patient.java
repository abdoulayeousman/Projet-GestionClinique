package model;

import java.time.LocalDate;

/**
 * Représente un patient au sein du système de gestion hospitalière.
 * Cette classe permet de stocker et de manipuler les informations personnelles,
 * médicales et géographiques d'un patient.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class Patient {

    // ============ ATTRIBUTS ============

    /** L'identifiant unique du patient dans la base de données. */
    private int patientId;

    /** Le nom de famille et le prénom du patient. */
    private String nomComplet;

    /** La date de naissance du patient sous forme d'objet {@link LocalDate}. */
    private LocalDate dateNaissance;

    /** Le sexe du patient (valeurs attendues : "Male", "Female", "Other"). */
    private String sexe;

    /** Le numéro de téléphone de contact du patient. */
    private String telephone;

    /** Le service médical auquel le patient est rattaché (ex: "Cardiologie", "Pédiatrie"). */
    private String service;

    /** Le pays d'origine du patient (représentant l'ENUM Afrique de la base de données). */
    private String pays;

    // ============ CONSTRUCTEUR ============

    /**
     * Constructeur par défaut permettant d'instancier un patient vide.
     */
    public Patient() {}

    // ============ GETTERS ET SETTERS ============

    /**
     * Récupère l'identifiant unique du patient.
     *
     * @return L'identifiant du patient (int).
     */
    public int getPatientId() { return patientId; }

    /**
     * Modifie l'identifiant unique du patient.
     *
     * @param patientId Le nouvel identifiant à attribuer au patient.
     */
    public void setPatientId(int patientId) { this.patientId = patientId; }

    /**
     * Récupère le nom complet du patient.
     *
     * @return Le nom complet sous forme de chaîne de caractères (String).
     */
    public String getNomComplet() { return nomComplet; }

    /**
     * Modifie le nom complet du patient.
     *
     * @param nomComplet Le nouveau nom complet du patient.
     */
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    /**
     * Récupère la date de naissance du patient.
     *
     * @return La date de naissance (LocalDate).
     */
    public LocalDate getDateNaissance() { return dateNaissance; }

    /**
     * Modifie la date de naissance du patient.
     *
     * @param dateNaissance La nouvelle date de naissance du patient.
     */
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    /**
     * Récupère le sexe du patient.
     *
     * @return Le sexe du patient (String).
     */
    public String getSexe() { return sexe; }

    /**
     * Modifie le sexe du patient.
     *
     * @param sexe Le sexe à attribuer ("Male", "Female", "Other").
     */
    public void setSexe(String sexe) { this.sexe = sexe; }

    /**
     * Récupère le numéro de téléphone du patient.
     *
     * @return Le numéro de téléphone (String).
     */
    public String getTelephone() { return telephone; }

    /**
     * Modifie le numéro de téléphone du patient.
     *
     * @param telephone Le nouveau numéro de téléphone du patient.
     */
    public void setTelephone(String telephone) { this.telephone = telephone; }

    /**
     * Récupère le service médical associé au patient.
     *
     * @return Le nom du service médical (String).
     */
    public String getService() { return service; }

    /**
     * Modifie le service médical associé au patient.
     *
     * @param service Le nouveau service médical à affecter.
     */
    public void setService(String service) { this.service = service; }

    /**
     * Récupère le pays d'origine du patient.
     *
     * @return Le pays du patient (String).
     */
    public String getPays() { return pays; }

    /**
     * Modifie le pays d'origine du patient.
     *
     * @param pays Le nouveau pays d'origine du patient.
     */
    public void setPays(String pays) { this.pays = pays; }
}