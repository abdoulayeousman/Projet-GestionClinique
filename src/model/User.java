package model;

import java.time.LocalDateTime;

/**
 * Représente un utilisateur du système de gestion de la clinique.
 * Cette classe gère les informations d'authentification, les rôles d'accès,
 * les statuts des comptes et le suivi de leur création.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class User {

    // ============ ATTRIBUTS ============

    private int userId;
    private String username;
    private String passwordHash;
    private String email;
    private String role;
    private String statut;
    private LocalDateTime lastLogin;  // ✅ Nouveau : pour audit trail
    private LocalDateTime createdAt;

    // ============ CONSTRUCTEURS ============

    /**
     * Constructeur par défaut.
     */
    public User() {}

    /**
     * Constructeur complet.
     */
    public User(int userId, String username, String passwordHash,
                String email, String role, String statut,
                LocalDateTime lastLogin, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
        this.statut = statut;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
    }

    // ============ GETTERS ET SETTERS ============

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ============ MÉTHODES UTILES ============

    /**
     * ✅ Valide les données critiques de l'utilisateur.
     * @return true si les données sont valides.
     */
    public boolean estValide() {
        return userId > 0 &&
                username != null && !username.trim().isEmpty() &&
                passwordHash != null && !passwordHash.trim().isEmpty() &&
                email != null && !email.trim().isEmpty() &&
                role != null && !role.trim().isEmpty() &&
                statut != null && !statut.trim().isEmpty();
    }

    /**
     * ✅ Vérifie si l'utilisateur a un rôle spécifique.
     * @param roleToCheck Le rôle à vérifier.
     * @return true si l'utilisateur a ce rôle.
     */
    public boolean aRoleOf(String roleToCheck) {
        return role != null && role.equalsIgnoreCase(roleToCheck);
    }

    /**
     * ✅ Vérifie si l'utilisateur est actif.
     * @return true si le statut est "Actif".
     */
    public boolean estActif() {
        return statut != null && "Actif".equalsIgnoreCase(statut);
    }

    /**
     * ✅ Retourne une représentation textuelle de l'utilisateur.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", statut='" + statut + '\'' +
                ", lastLogin=" + lastLogin +
                ", createdAt=" + createdAt +
                '}';
    }
}