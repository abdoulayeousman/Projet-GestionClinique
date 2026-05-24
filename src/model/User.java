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
    private String passwordHash; // Inchangé, comme demandé
    private String email;
    private String role;
    private String statut;
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
                LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
        this.statut = statut;
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ============ MÉTHODES UTILES ============

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", statut='" + statut + '\'' +
                ", crééLe=" + createdAt +
                '}';
    }
}