package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Représente l'entité métier d'une consultation (Consultation) au sein du système.
 * Cette classe centralise l'ensemble des données cliniques recueillies par le médecin,
 * les diagnostics, les documents médicaux joints, les examens demandés, les ordonnances
 * émises ainsi que la planification du suivi du patient.
 *
 * @author Abdoulaye Ousmane
 * @version 1.0
 */
public class Consultation {

    // ============ ATTRIBUTS ============

    /** Identifiant unique de la consultation dans la base de données. */
    private int consultationId;

    /** Identifiant unique du rendez-vous associé à cette consultation. */
    private int appointmentId;

    /** Identifiant unique du patient ausculté. */
    private int patientId;

    /** Identifiant unique du médecin ayant mené la consultation. */
    private int doctorId;

    /** Date et heure exactes du déroulement de la consultation. */
    private LocalDateTime dateConsultation;

    /** Description textuelle de la durée des symptômes (ex: "Depuis 3 jours"). */
    private String dureeSymptomes;

    /** Évaluation quantitative du niveau de douleur du patient, sur une échelle allant de 0 à 10. */
    private int niveauDouleur;

    /** Conclusion médicale ou diagnostic posé par le médecin (Champ obligatoire). */
    private String diagnostic;

    /** Remarques, observations cliniques générales et notes diverses du médecin. */
    private String notesMedicales;

    /** Lien ou chemin d'accès local vers un fichier d'imagerie médicale (radio, scanner, examen). */
    private String cheminPhoto;

    /** Description ou contenu textuel de l'ancienne ordonnance présentée par le patient. */
    private String ancienneOrdonnance;

    /** Description ou historique des anciens résultats d'analyse fournis par le patient. */
    private String ancienResultatAnalyse;

    /** Liste ou libellé des examens ou analyses complémentaires prescrits au laboratoire (ex: "Bilan sanguin, Radio"). */
    private String analyseDemandee;

    /** Transcription ou compte-rendu des résultats d'analyse retournés par le laboratoire. */
    private String resultatAnalyse;

    /** Liste des médicaments prescrits lors de cette session. */
    private String ordonnance;

    /** Précisions sur la posologie et la répartition des prises (ex: "2 comprimés par jour"). */
    private String dosage;

    /** Période de validité ou temps d'administration du traitement (ex: "7 jours"). */
    private String dureeTraitement;

    /** Date planifiée pour la prochaine visite de contrôle ou de suivi (LocalDate). */
    private LocalDate dateProchainControle;

    /** Recommandations et consignes transmises au patient pour son retour (ex: "Revenir dans 3 semaines"). */
    private String instructionsControle;

    /** État d'avancement de la consultation (ex: "En cours", "Terminee", "A suivre"). */
    private String statut;

    /** Horodatage système automatique indiquant la création de la fiche de consultation. */
    private LocalDateTime createdAt;

    // ============ CONSTRUCTEURS ============

    /**
     * Constructeur par défaut (sans arguments).
     * Permet d'instancier une consultation vide avant de configurer ses valeurs.
     */
    public Consultation() {}

    /**
     * Constructeur complet permettant d'initialiser une consultation avec toutes ses données constitutives.
     *
     * @param consultationId Identifiant unique de la consultation.
     * @param appointmentId Identifiant du rendez-vous lié.
     * @param patientId Identifiant du patient concerné.
     * @param doctorId Identifiant du médecin traitant.
     * @param dateConsultation Date et heure de la séance.
     * @param dureeSymptomes Période écoulée depuis l'apparition des symptômes.
     * @param niveauDouleur Indice de douleur évalué de 0 à 10.
     * @param diagnostic Diagnostic principal établi.
     * @param notesMedicales Observations et commentaires textuels du praticien.
     * @param cheminPhoto Chemin du fichier d'imagerie ou d'examen joint.
     * @param ancienneOrdonnance Références aux traitements antérieurs du patient.
     * @param ancienResultatAnalyse Historique des analyses de biologie médicale transmises.
     * @param analyseDemandee Examens de laboratoire requis à la suite de la visite.
     * @param resultatAnalyse Données chiffrées ou conclusions du laboratoire.
     * @param ordonnance Liste nominative des traitements prescrits.
     * @param dosage Spécification du rythme de prise des médicaments.
     * @param dureeTraitement Temps global d'exécution du protocole de soins.
     * @param dateProchainControle Date limite conseillée pour le contrôle.
     * @param instructionsControle Directives de surveillance données au patient.
     * @param statut État actuel du dossier de consultation.
     * @param createdAt Timestamp d'écriture en base de données.
     */
    public Consultation(int consultationId, int appointmentId,
                        int patientId, int doctorId,
                        LocalDateTime dateConsultation,
                        String dureeSymptomes, int niveauDouleur,
                        String diagnostic, String notesMedicales,
                        String cheminPhoto, String ancienneOrdonnance,
                        String ancienResultatAnalyse,
                        String analyseDemandee, String resultatAnalyse,
                        String ordonnance, String dosage,
                        String dureeTraitement,
                        LocalDate dateProchainControle,
                        String instructionsControle,
                        String statut, LocalDateTime createdAt) {
        this.consultationId = consultationId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateConsultation = dateConsultation;
        this.dureeSymptomes = dureeSymptomes;
        this.niveauDouleur = niveauDouleur;
        this.diagnostic = diagnostic;
        this.notesMedicales = notesMedicales;
        this.cheminPhoto = cheminPhoto;
        this.ancienneOrdonnance = ancienneOrdonnance;
        this.ancienResultatAnalyse = ancienResultatAnalyse;
        this.analyseDemandee = analyseDemandee;
        this.resultatAnalyse = resultatAnalyse;
        this.ordonnance = ordonnance;
        this.dosage = dosage;
        this.dureeTraitement = dureeTraitement;
        this.dateProchainControle = dateProchainControle;
        this.instructionsControle = instructionsControle;
        this.statut = statut;
        this.createdAt = createdAt;
    }

    // ============ GETTERS ET SETTERS ============

    /**
     * Récupère l'identifiant de la consultation.
     * @return L'identifiant de la consultation.
     */
    public int getConsultationId() { return consultationId; }

    /**
     * Modifie l'identifiant de la consultation.
     * @param consultationId Le nouvel identifiant.
     */
    public void setConsultationId(int consultationId) { this.consultationId = consultationId; }

    /**
     * Récupère l'identifiant du rendez-vous lié.
     * @return L'identifiant du rendez-vous.
     */
    public int getAppointmentId() { return appointmentId; }

    /**
     * Lie la consultation à un rendez-vous spécifique.
     * @param appointmentId L'identifiant du rendez-vous.
     */
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    /**
     * Récupère l'identifiant du patient.
     * @return L'identifiant du patient.
     */
    public int getPatientId() { return patientId; }

    /**
     * Modifie l'identifiant du patient.
     * @param patientId Le nouvel identifiant du patient.
     */
    public void setPatientId(int patientId) { this.patientId = patientId; }

    /**
     * Récupère l'identifiant du médecin.
     * @return L'identifiant du médecin.
     */
    public int getDoctorId() { return doctorId; }

    /**
     * Modifie l'identifiant du médecin.
     * @param doctorId Le nouvel identifiant du médecin.
     */
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    /**
     * Récupère la date et l'heure de la consultation.
     * @return La date de consultation (LocalDateTime).
     */
    public LocalDateTime getDateConsultation() { return dateConsultation; }

    /**
     * Enregistre la date et l'heure de la consultation.
     * @param dateConsultation La date de la consultation.
     */
    public void setDateConsultation(LocalDateTime dateConsultation) { this.dateConsultation = dateConsultation; }

    /**
     * Récupère les données sur l'ancienneté des symptômes.
     * @return La durée des symptômes.
     */
    public String getDureeSymptomes() { return dureeSymptomes; }

    /**
     * Spécifie la durée d'évolution des symptômes constatés.
     * @param dureeSymptomes Le texte sur la durée des symptômes.
     */
    public void setDureeSymptomes(String dureeSymptomes) { this.dureeSymptomes = dureeSymptomes; }

    /**
     * Récupère la note d'évaluation de la douleur.
     * @return L'indice de douleur entre 0 et 10.
     */
    public int getNiveauDouleur() { return niveauDouleur; }

    /**
     * Modifie l'évaluation du niveau de douleur.
     * @param niveauDouleur L'indice de douleur ajusté.
     */
    public void setNiveauDouleur(int niveauDouleur) { this.niveauDouleur = niveauDouleur; }

    /**
     * Récupère le diagnostic médical formulé.
     * @return Le texte du diagnostic.
     */
    public String getDiagnostic() { return diagnostic; }

    /**
     * Renseigne le diagnostic obligatoire de la visite.
     * @param diagnostic Le libellé du diagnostic.
     */
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    /**
     * Récupère les notes de synthèse clinique.
     * @return Le contenu des notes médicales.
     */
    public String getNotesMedicales() { return notesMedicales; }

    /**
     * Permet d'insérer des remarques ou des observations complémentaires.
     * @param notesMedicales Les nouvelles notes du praticien.
     */
    public void setNotesMedicales(String notesMedicales) { this.notesMedicales = notesMedicales; }

    /**
     * Récupère l'emplacement physique du document d'imagerie.
     * @return Le chemin système de l'image.
     */
    public String getCheminPhoto() { return cheminPhoto; }

    /**
     * Enregistre le chemin d'accès au document d'imagerie attaché.
     * @param cheminPhoto Le chemin d'accès ou l'URL du fichier.
     */
    public void setCheminPhoto(String cheminPhoto) { this.cheminPhoto = cheminPhoto; }

    /**
     * Récupère le descriptif de l'ancienne ordonnance.
     * @return Les détails de l'ancienne ordonnance.
     */
    public String getAncienneOrdonnance() { return ancienneOrdonnance; }

    /**
     * Met à jour les éléments de l'ancienne ordonnance.
     * @param ancienneOrdonnance Les détails textuels à retenir.
     */
    public void setAncienneOrdonnance(String ancienneOrdonnance) { this.ancienneOrdonnance = ancienneOrdonnance; }

    /**
     * Récupère l'historique des anciennes analyses biologiques reçues.
     * @return Le résumé textuel des anciens examens.
     */
    public String getAncienResultatAnalyse() { return ancienResultatAnalyse; }

    /**
     * Intègre le compte-rendu des précédents examens de laboratoire.
     * @param ancienResultatAnalyse Le texte descriptif des anciens résultats.
     */
    public void setAncienResultatAnalyse(String ancienResultatAnalyse) { this.ancienResultatAnalyse = ancienResultatAnalyse; }

    /**
     * Récupère le libellé des bilans demandés.
     * @return Les détails des examens demandés.
     */
    public String getAnalyseDemandee() { return analyseDemandee; }

    /**
     * Enregistre la liste des nouveaux examens à effectuer en laboratoire.
     * @param analyseDemandee Le texte spécifiant les examens.
     */
    public void setAnalyseDemandee(String analyseDemandee) { this.analyseDemandee = analyseDemandee; }

    /**
     * Récupère les conclusions analytiques du laboratoire.
     * @return Le compte-rendu des résultats d'analyses reçus.
     */
    public String getResultatAnalyse() { return resultatAnalyse; }

    /**
     * Enregistre les valeurs ou conclusions biologiques remises par le laboratoire.
     * @param resultatAnalyse Les résultats de laboratoire correspondants.
     */
    public void setResultatAnalyse(String resultatAnalyse) { this.resultatAnalyse = resultatAnalyse; }

    /**
     * Récupère la liste des prescriptions de médicaments.
     * @return Le détail de l'ordonnance.
     */
    public String getOrdonnance() { return ordonnance; }

    /**
     * Remplit ou modifie l'ordonnance médicamenteuse.
     * @param ordonnance Le descriptif des produits de santé prescrits.
     */
    public void setOrdonnance(String ordonnance) { this.ordonnance = ordonnance; }

    /**
     * Récupère les doses prescrites.
     * @return Les conditions de dosage.
     */
    public String getDosage() { return dosage; }

    /**
     * Définit le dosage adéquat pour le traitement.
     * @param dosage La chaîne de caractères du dosage.
     */
    public void setDosage(String dosage) { this.dosage = dosage; }

    /**
     * Récupère la durée fixée pour la prise du traitement.
     * @return La durée du traitement en cours.
     */
    public String getDureeTraitement() { return dureeTraitement; }

    /**
     * Modifie la durée du traitement.
     * @param dureeTraitement L'indication temporelle du traitement.
     */
    public void setDureeTraitement(String dureeTraitement) { this.dureeTraitement = dureeTraitement; }

    /**
     * Récupère la date prévue pour le contrôle médical.
     * @return La date de suivi (LocalDate).
     */
    public LocalDate getDateProchainControle() { return dateProchainControle; }

    /**
     * Ajuste la planification temporelle du prochain contrôle.
     * @param dateProchainControle La date cible retenue.
     */
    public void setDateProchainControle(LocalDate dateProchainControle) { this.dateProchainControle = dateProchainControle; }

    /**
     * Récupère les instructions spéciales de suivi.
     * @return Les recommandations de contrôle.
     */
    public String getInstructionsControle() { return instructionsControle; }

    /**
     * Ajoute des consignes particulières pour la prochaine venue du patient.
     * @param instructionsControle Les directives textuelles à suivre.
     */
    public void setInstructionsControle(String instructionsControle) { this.instructionsControle = instructionsControle; }

    /**
     * Récupère l'état d'avancement opérationnel de la fiche.
     * @return Le statut de la consultation.
     */
    public String getStatut() { return statut; }

    /**
     * Met à jour le statut du cycle de vie de la consultation.
     * @param statut Le nouveau statut.
     */
    public void setStatut(String statut) { this.statut = statut; }

    /**
     * Récupère la date et l'heure système de la saisie.
     * @return Le timestamp d'insertion.
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Consigne la date et l'heure d'enregistrement initial.
     * @param createdAt Le timestamp de création.
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}