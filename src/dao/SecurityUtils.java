package dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilitaire de sécurité pour le hachage et la protection des informations sensibles.
 *
 * @author Abdoulaye Ousmane
 * @version 1.1
 */
public class SecurityUtils {

    /** Longueur du salt en bytes pour le hachage sécurisé */
    private static final int SALT_LENGTH = 16;

    /**
     * Génère une empreinte SHA-256 à partir d'une chaîne de caractères en clair.
     * ⚠️ IMPORTANT : Cette méthode utilise SHA-256 SANS SALT.
     * Pour la production, préférer BCrypt ou PBKDF2 avec salt aléatoire.
     *
     * @param password Le mot de passe en clair à sécuriser.
     * @return Le hash hexadécimal unique de 64 caractères, ou {@code null} si l'entrée est nulle.
     * @deprecated À remplacer par {@link #hashPasswordWithSalt(String)} en production
     */
    @Deprecated
    public static String hashSHA256(String password) {
        if (password == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');  // ✅ Padding correct pour octets < 16
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur critique : algorithme SHA-256 indisponible.", e);
        }
    }

    /**
     * Génère une empreinte SHA-256 avec SALT pour renforcer la sécurité.
     * Combine le hash avec un salt aléatoire pour prévenir les attaques par rainbow table.
     *
     * Format stocké en base : base64(salt + hash)
     *
     * @param password Le mot de passe en clair à sécuriser.
     * @return Une chaîne Base64 contenant le salt + le hash, ou {@code null} si l'entrée est nulle.
     * @since 1.1
     */
    public static String hashPasswordWithSalt(String password) {
        if (password == null) return null;

        try {
            // Générer un salt aléatoire
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Combiner salt + password et hacher
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Combiner salt + hash
            byte[] saltAndHash = new byte[salt.length + hashBytes.length];
            System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
            System.arraycopy(hashBytes, 0, saltAndHash, salt.length, hashBytes.length);

            // Encoder en Base64 pour stockage
            return Base64.getEncoder().encodeToString(saltAndHash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur critique : algorithme SHA-256 indisponible.", e);
        }
    }

    /**
     * Vérifie qu'un mot de passe correspond au hash salé stocké.
     * Extrait le salt du hash stocké et compare avec le nouveau hash.
     *
     * @param password Le mot de passe en clair à vérifier.
     * @param storedHash Le hash salé stocké en base (format Base64).
     * @return {@code true} si le password correspond au hash.
     * @since 1.1
     */
    public static boolean verifyPasswordWithSalt(String password, String storedHash) {
        if (password == null || storedHash == null) return false;

        try {
            // Décoder le hash stocké
            byte[] saltAndHash = Base64.getDecoder().decode(storedHash);

            // Extraire le salt
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(saltAndHash, 0, salt, 0, SALT_LENGTH);

            // Hacher le password avec le même salt
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Comparer les hashes
            byte[] storedHashBytes = new byte[saltAndHash.length - SALT_LENGTH];
            System.arraycopy(saltAndHash, SALT_LENGTH, storedHashBytes, 0, storedHashBytes.length);

            return MessageDigest.isEqual(hashBytes, storedHashBytes);

        } catch (Exception e) {
            System.err.println("[SecurityUtils] Erreur lors de la vérification du password : " + e.getMessage());
            return false;
        }
    }
}