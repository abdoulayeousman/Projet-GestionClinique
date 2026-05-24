package model;

import java.time.LocalDateTime;

/**
 * Représente l'entité métier d'un secrétaire (Secretaire) au sein du système.
 * Cette classe gère les informations personnelles d'un membre du personnel de secrétariat,
 * son lien avec son compte utilisateur global, ainsi que son dernier horodatage de connexion.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class Secretaire {

    // ============ ATTRIBUTS ============

    private int secretaireId;
    private int userId;
    private String fullName;
    private String phoneNumber;
    private LocalDateTime lastLogin;

    // ============ CONSTRUCTEURS ============

    /**
     * Constructeur par défaut (sans arguments).
     * Permet d'instancier un secrétaire vide avant de renseigner ses attributs.
     */
    public Secretaire() {}

    /**
     * Constructeur complet permettant d'initialiser un secrétaire avec toutes ses données.
     *
     * @param secretaireId Identifiant unique du secrétaire.
     * @param userId Identifiant du compte utilisateur lié.
     * @param fullName Nom et prénom du secrétaire.
     * @param phoneNumber Numéro de téléphone de contact.
     * @param lastLogin Date et heure de la dernière connexion au système.
     */
    public Secretaire(int secretaireId, int userId,
                      String fullName, String phoneNumber,
                      LocalDateTime lastLogin) {
        this.secretaireId = secretaireId;
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.lastLogin = lastLogin;
    }

    // ============ GETTERS ET SETTERS ============

    public int getSecretaireId() { return secretaireId; }
    public void setSecretaireId(int secretaireId) { this.secretaireId = secretaireId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    // ============ MÉTHODES UTILES ============

    /**
     * ✅ Valide les données critiques du secrétaire.
     * @return true si les données sont valides.
     */
    public boolean estValide() {
        return secretaireId > 0 &&
                userId > 0 &&
                fullName != null && !fullName.trim().isEmpty() &&
                phoneNumber != null && !phoneNumber.trim().isEmpty();
    }

    /**
     * ✅ Retourne une représentation textuelle du secrétaire.
     */
    @Override
    public String toString() {
        return "Secretaire{" +
                "id=" + secretaireId +
                ", userId=" + userId +
                ", nom='" + fullName + '\'' +
                ", telephone='" + phoneNumber + '\'' +
                ", lastLogin=" + lastLogin +
                '}';
    }
}