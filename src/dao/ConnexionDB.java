package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gère la connexion à la base de données MySQL pour l'ensemble de l'application.
 * Cette classe implémente un modèle d'accès centralisé afin d'obtenir et de fermer
 * la connexion de manière unique, sécurisée et asynchrone (Thread-safe).
 *
 * Les identifiants de connexion sont externalisés dans un fichier de configuration.
 *
 * @author Abdoulaye Ousmane
 * @version 1.2
 */
public class ConnexionDB {

    // ============ PARAMÈTRES DE CONFIGURATION ============

    /** URL d'interconnexion JDBC vers MySQL (Ex: jdbc:mysql://localhost:3306/clinique_db). */
    private static String url;

    /** Identifiant de l'utilisateur de la base de données (Ex: root). */
    private static String user;

    /** Mot de passe associé au compte utilisateur de la base de données. */
    private static String password;

    // ============ INSTANCE UNIQUE (SINGLETON) ============

    /** Instance unique partagée de l'objet Connection pour éviter de multiplier les sessions ouvertes. */
    private static Connection connexion = null;

    // ============ BLOC DE CHARGEMENT DYNAMIQUE ============

    /*
     * Ce bloc statique s'exécute automatiquement une seule fois dès que la classe
     * ConnexionDB est appelée en mémoire. Il sert à lire le fichier secret externe.
     */
    static {
        Properties props = new Properties();

        // Tentative de lecture du fichier config.properties situé à la racine du projet
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);

            // Extraction des valeurs clés du fichier texte vers nos variables Java
            url      = props.getProperty("db.url");
            user     = props.getProperty("db.user");
            password = props.getProperty("db.password");

            System.out.println("[ConnexionDB] Fichier config.properties chargé avec succès.");

        } catch (IOException e) {
            // ⚠️ ERREUR : Fallback avec valeurs de secours (À NE PAS UTILISER EN PRODUCTION)
            System.err.println("[ConnexionDB] ATTENTION CRITIQUE : config.properties introuvable !");
            System.err.println("[ConnexionDB] Créez le fichier config.properties avec les propriétés : db.url, db.user, db.password");

            url      = "jdbc:mysql://localhost:3306/clinique_db";
            user     = "root";
            password = "Tarok6680";
            System.err.println("[ConnexionDB] Utilisation des valeurs par défaut DE DÉVELOPPEMENT UNIQUEMENT.");
        }
    }

    // ============ CONTEXTE THREAD-SAFE (OBTENIR LA CONNEXION) ============

    /**
     * Fournit l'instance active de connexion à la base de données.
     * Si aucune connexion n'existe ou si elle a été fermée, une nouvelle session est ouverte.
     *
     * ⚠️ IMPORTANT : Vérifier que la connexion retournée n'est pas NULL avant utilisation.
     * Le mot-clé 'synchronized' empêche deux requêtes de se percuter si l'interface graphique
     * exécute deux actions en même temps.
     *
     * @return L'objet {@link Connection} opérationnel pour exécuter les requêtes SQL, ou null en cas d'erreur.
     * @throws RuntimeException si la connexion ne peut pas être établie (À partir de v2.0)
     */
    public static synchronized Connection getConnexion() {
        try {
            // Si l'objet n'existe pas ENCORE ou si la connexion existante a été rompue/fermée
            if (connexion == null || connexion.isClosed()) {
                // On demande une nouvelle connexion au pilote DriverManager de Java
                connexion = DriverManager.getConnection(url, user, password);
                System.out.println("[ConnexionDB] Connexion MySQL établie avec succès.");
            }
        } catch (SQLException e) {
            // ❌ ERREUR CRITIQUE : Impossible de se connecter
            System.err.println("[ConnexionDB] ERREUR CRITIQUE lors de l'ouverture de la connexion : " + e.getMessage());
            System.err.println("[ConnexionDB] Vérifiez : URL, utilisateur, password, et que MySQL est accessible.");
            e.printStackTrace();
            connexion = null;  // ✅ Réinitialiser à null pour éviter connexion "morte"
        }
        return connexion;
    }

    // ============ CLÔTURE PROPRE (ÉVITER LES FUITES) ============

    /**
     * Procède à la fermeture de l'instance de connexion en cours.
     * Cette méthode doit être appelée pour libérer proprement les ressources du serveur MySQL
     * et éviter les fuites de connexion (connection leak).
     */
    public static synchronized void fermerConnexion() {
        try {
            // On ne ferme que si l'objet existe et qu'il n'est pas déjà clôturé
            if (connexion != null && !connexion.isClosed()) {
                connexion.close();
                connexion = null;  // ✅ Réinitialiser à null après fermeture
                System.out.println("[ConnexionDB] Connexion MySQL fermée proprement.");
            }
        } catch (SQLException e) {
            System.err.println("[ConnexionDB] Erreur SQL lors de la fermeture : " + e.getMessage());
        }
    }
}