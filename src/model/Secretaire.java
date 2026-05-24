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

    // Identifiant
    /** Identifiant unique du secrétaire dans la base de données. */
    private int secretaireId;

    // Lien avec users
    /** Identifiant unique de l'utilisateur associé (clé étrangère vers la table des utilisateurs). */
    private int userId;

    // Informations personnelles
    /** Nom complet (nom et prénom) du secrétaire. */
    private String fullName;

    /** Numéro de téléphone de contact du secrétaire. */
    private String phoneNumber;

    // Traçabilité
    /** Date et heure de la dernière connexion réussie au système. */
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

    /**
     * Récupère l'identifiant du secrétaire.
     * @return L'identifiant unique du secrétaire.
     */
    public int getSecretaireId() { return secretaireId; }

    /**
     * Modifie l'identifiant du secrétaire.
     * @param secretaireId Le nouvel identifiant du secrétaire.
     */
    public void setSecretaireId(int secretaireId) { this.secretaireId = secretaireId; }

    /**
     * Récupère l'identifiant de l'utilisateur lié.
     * @return L'identifiant du compte utilisateur associé.
     */
    public int getUserId() { return userId; }

    /**
     * Associe le secrétaire à un compte utilisateur spécifique.
     * @param userId L'identifiant de l'utilisateur à lier.
     */
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * Récupère le nom complet du secrétaire.
     * @return Le nom complet sous forme de chaîne de caractères.
     */
    public String getFullName() { return fullName; }

    /**
     * Modifie le nom complet du secrétaire.
     * @param fullName Le nouveau nom complet.
     */
    public void setFullName(String fullName) { this.fullName = fullName; }

    /**
     * Récupère le numéro de téléphone du secrétaire.
     * @return Le numéro de téléphone de contact.
     */
    public String getPhoneNumber() { return phoneNumber; }

    /**
     * Modifie le numéro de téléphone du secrétaire.
     * @param phoneNumber Le nouveau numéro de téléphone.
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /**
     * Récupère la date et l'heure de la dernière connexion.
     * @return L'horodatage de la dernière connexion (LocalDateTime).
     */
    public LocalDateTime getLastLogin() { return lastLogin; }

    /**
     * Met à jour l'horodatage de la dernière connexion du secrétaire.
     * @param lastLogin La date et l'heure de la connexion actuelle.
     */
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
}