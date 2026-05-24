package dao;

import java.sql.*;
import model.User;

/**
 * Gestionnaire d'accès aux données pour la table 'users'.
 * Centralise les opérations d'authentification et de sécurité.
 */
public class UserDAO {

    /**
     * Tente de connecter l'utilisateur en vérifiant ses identifiants.
     * * @param username Le nom d'utilisateur saisi.
     * @param password Le mot de passe saisi en clair.
     * @return L'objet User si les accès sont corrects, null sinon.
     */
    public User login(String username, String password) {
        // Sécurisation : on hache le mot de passe avant de l'envoyer à la base
        String hash = SecurityUtils.hashSHA256(password);

        // Requête SQL utilisant la colonne 'password_hash' (conforme au schéma SQL)
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hash);

            try (ResultSet rs = stmt.executeQuery()) {
                // Si une ligne est retournée, l'authentification est réussie
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    return user; // Retourne l'utilisateur authentifié
                }
            }
        } catch (SQLException e) {
            // Log de l'erreur pour le développeur en cas de problème de connexion DB
            System.err.println("[UserDAO] Erreur lors de la tentative de login : " + e.getMessage());
        }
        return null; // Retourne null si aucun utilisateur ne correspond
    }

    /**
     * Force une mise à jour du mot de passe dans la base de données.
     * * @param username Identifiant de l'utilisateur concerné.
     * @param ancienMotDePasse Le nouveau mot de passe à hacher et stocker.
     * @return true si la modification est bien enregistrée en base.
     */
    public boolean restaurerAncienMotDePasse(String username, String ancienMotDePasse) {
        String hash = SecurityUtils.hashSHA256(ancienMotDePasse);
        String sql = "UPDATE users SET password_hash = ? WHERE username = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hash);
            stmt.setString(2, username);

            // Retourne vrai si au moins une ligne a été modifiée
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[UserDAO] Erreur lors de la mise à jour : " + e.getMessage());
            return false;
        }
    }
}