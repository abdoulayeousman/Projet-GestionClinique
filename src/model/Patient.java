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

    private int patientId;
    private String nomComplet;
    private LocalDate dateNaissance;
    private String sexe;
    private String telephone;
    private String service;
    private String pays;

    // ============ CONSTRUCTEUR ============

    /**
     * Constructeur par défaut permettant d'instancier un patient vide.
     */
    public Patient() {}

    /**
     * Constructeur complet permettant d'initialiser un patient avec toutes ses données.
     *
     * @param patientId Identifiant unique du patient.
     * @param nomComplet Nom complet du patient.
     * @param dateNaissance Date de naissance du patient.
     * @param sexe Sexe du patient.
     * @param telephone Numéro de téléphone du patient.
     * @param service Service médical du patient.
     * @param pays Pays d'origine du patient.
     */
    public Patient(int patientId, String nomComplet, LocalDate dateNaissance,
                   String sexe, String telephone, String service, String pays) {
        this.patientId = patientId;
        this.nomComplet = nomComplet;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.telephone = telephone;
        this.service = service;
        this.pays = pays;
    }

    // ============ GETTERS ET SETTERS ============

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    // ============ MÉTHODES UTILES ============

    /**
     * ✅ Valide les données critiques du patient.
     * @return true si les données sont valides.
     */
    public boolean estValide() {
        return patientId > 0 &&
                nomComplet != null && !nomComplet.trim().isEmpty() &&
                dateNaissance != null &&
                sexe != null && !sexe.trim().isEmpty() &&
                (sexe.equalsIgnoreCase("Male") ||
                        sexe.equalsIgnoreCase("Female") ||
                        sexe.equalsIgnoreCase("Other"));
    }

    /**
     * ✅ Calcule l'âge actuel du patient.
     * @return L'âge en années, ou -1 si la date de naissance est null.
     */
    public int calculerAge() {
        if (dateNaissance == null) return -1;
        return LocalDate.now().getYear() - dateNaissance.getYear();
    }

    /**
     * ✅ Retourne une représentation textuelle du patient.
     */
    @Override
    public String toString() {
        return "Patient{" +
                "id=" + patientId +
                ", nom='" + nomComplet + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", sexe='" + sexe + '\'' +
                ", telephone='" + telephone + '\'' +
                ", service='" + service + '\'' +
                ", pays='" + pays + '\'' +
                '}';
    }
}