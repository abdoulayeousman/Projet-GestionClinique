package dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitaire de sécurité pour le hachage et la protection des informations sensibles.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class SecurityUtils {

    /**
     * Génère une empreinte SHA-256 à partir d'une chaîne de caractères en clair.
     *
     * @param password Le mot de passe en clair à sécuriser.
     * @return Le hash hexadécimal unique de 64 caractères, ou {@code null} si l'entrée est nulle.
     */
    public static String hashSHA256(String password) {
        if (password == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Correction ici
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur critique : algorithme SHA-256 indisponible.", e);
        }
    }
}