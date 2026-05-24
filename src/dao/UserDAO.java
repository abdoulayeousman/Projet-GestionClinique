package dao;

import java.sql.*;
import model.User;

/**
 * Gestionnaire d'accès aux données pour la table 'users'.
 * Centralise les opérations d'authentification et de sécurité.
 */
public class UserDAO {

    /** Nombre maximum de tentatives de connexion avant blocage */
    private static final int MAX_LOGIN_ATTEMPTS = 5;

    /**
     * Tente de connecter l'utilisateur en vérifiant ses identifiants.
     * @param username Le nom d'utilisateur saisi.
     * @param password Le mot de passe saisi en clair.
     * @return L'objet User si les accès sont corrects, null sinon.
     */
    public User login(String username, String password) {
        // ✅ Vérification des paramètres
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            System.err.println("[UserDAO] Tentative de login avec paramètres vides");
            return null;
        }

        // Sécurisation : on hache le mot de passe avant de l'envoyer à la base
        String hash = SecurityUtils.hashSHA256(password);
        if (hash == null) {
            System.err.println("[UserDAO] Erreur : impossible de hacher le password");
            return null;
        }

        // Requête SQL utilisant la colonne 'password_hash' (conforme au schéma SQL)
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";

        Connection conn = null;
        try {
            conn = ConnexionDB.getConnexion();
            // ✅ Vérification que la connexion n'est pas null
            if (conn == null) {
                System.err.println("[UserDAO] Erreur : connexion à la base de données impossible");
                return null;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

                        // ✅ Enregistrer le dernier login
                        updateLastLogin(user.getUserId());

                        System.out.println("[UserDAO] Authentification réussie pour : " + username);
                        return user;  // Retourne l'utilisateur authentifié
                    }
                }
            }

        } catch (SQLException e) {
            // Log de l'erreur pour le développeur en cas de problème de connexion DB
            System.err.println("[UserDAO] Erreur lors de la tentative de login : " + e.getMessage());
            e.printStackTrace();
        }

        System.err.println("[UserDAO] Authentification échouée pour : " + username);
        return null;  // Retourne null si aucun utilisateur ne correspond
    }

    /**
     * Enregistre la date/heure du dernier login de l'utilisateur.
     * Utile pour l'audit et la détection d'anomalies.
     *
     * @param userId L'ID de l'utilisateur connecté
     */
    private void updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn != null) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("[UserDAO] Erreur lors de la mise à jour last_login : " + e.getMessage());
        }
    }

    /**
     * Force une mise à jour du mot de passe dans la base de données.
     * @param username Identifiant de l'utilisateur concerné.
     * @param nouveauMotDePasse Le nouveau mot de passe à hacher et stocker.
     * @return true si la modification est bien enregistrée en base.
     */
    public boolean restaurerAncienMotDePasse(String username, String nouveauMotDePasse) {
        // ✅ Vérifications
        if (username == null || username.trim().isEmpty() || nouveauMotDePasse == null || nouveauMotDePasse.isEmpty()) {
            System.err.println("[UserDAO] Tentative de changement de password avec paramètres vides");
            return false;
        }

        String hash = SecurityUtils.hashSHA256(nouveauMotDePasse);
        if (hash == null) {
            System.err.println("[UserDAO] Erreur : impossible de hacher le nouveau password");
            return false;
        }

        String sql = "UPDATE users SET password_hash = ? WHERE username = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // ✅ Vérification que la connexion n'est pas null
            if (conn == null) {
                System.err.println("[UserDAO] Erreur : connexion à la base de données impossible");
                return false;
            }

            stmt.setString(1, hash);
            stmt.setString(2, username);

            // Retourne vrai si au moins une ligne a été modifiée
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[UserDAO] Password changé avec succès pour : " + username);
                return true;
            } else {
                System.err.println("[UserDAO] Aucun utilisateur trouvé pour : " + username);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("[UserDAO] Erreur lors de la mise à jour : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}