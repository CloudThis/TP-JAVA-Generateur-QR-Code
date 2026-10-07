# Générateur de QR Code (Java Swing - MVC)

Une application développée en Java Swing en utilisant le patron d'architecture **MVC (Modèle-Vue-Contrôleur)**. Elle permet de générer des QR Codes à partir de texte ou d'URL, d'afficher le résultat et d'exporter au format **PDF**.

---

## Fonctionnalités

- **Génération de QR Code** : Encodage rapide de texte ou d'URL sous forme d'image matricielle via la bibliothèque ZXing.
- **Exportation PDF avancée** : Génération de documents PDF (via iText) intégrant le QR Code et une image additionnelle personnalisable (gestion de la taille et de l'alignement : gauche, centre, droite).
- **Sauvegarde & Chargement (Projets et Profils)** :
    - **Projets** : Enregistrement et restauration de l'état complet de la session (texte, paramètres d'image).
    - **Profils** : Sauvegarde et application de chartes graphiques sur mesure (polices, couleurs, tailles).
- **Interface réactive & Ergonomie (UX)** :
    - Indication visuelle de progression (`JProgressBar`) et messages de statut colorés.
    - Infobulles explicatives sur l'ensemble des éléments interactifs.
- **Suite de tests unitaires** : Couverture complète de la couche modèle avec JUnit.

---


## Structure du Projet

```text
TP_JAVA_Generateur_QR_Code/
├── pom.xml
├── README.md
├── src/
│   ├── controleur/
│   │   └── QRCodeController.java   # Point d'entrée principal (main) et gestion des événements
│   ├── modele/
│   │   ├── QRCodeModel.java        # État et données (PropertyChangeSupport)
│   │   ├── QRCodeService.java      # Service de génération d'image (ZXing)
│   │   ├── ProfilData.java
│   │   ├── ProjetData.java
│   │   ├── ProjetService.java
│   │   └── PDFService.java         # Service d'exportation de document (iText)
│   ├── vue/
│   │   ├── QRcodesaisi.java        # Composants de l'interface et dialogues
│   │   └── QRcodesaisi.form        # Fichier du GUI Designer
└── test/
    └── modele/                     # Tests unitaires JUnit
        ├── QRCodeModelTest.java
        ├── QRCodeService.java
        ├── ProfilDataTest.java
        ├── ProjetDataTest.java
        ├── ProjetServiceTest.java
        └── PDFServiceTest.java
```

---

## 1. Introduction et Objectifs
L'application initiale permettait la création simple de QR Codes et l'exportation au format PDF. L'objectif de cette phase d'évolution était d'améliorer l'application sur trois points :
1. **La personnalisation du PDF** (ajout d'images secondaires, positionnement, dimensions).
2. **La persistance des données** (sauvegarde et chargement de projets et de profils graphiques).
3. **L'ergonomie et la réactivité** (barres de progression, couverture de tests).

---

---

## 2. Évolution de l'Architecture MVC et des Fonctionnalités

### A. Couche Modèle (`modele`)
- **Gestion des projets et des styles** : Création de deux types de fichiers pour sauvegarder l'état complet du projet (`.qrp`) ainsi que la charte graphique (`.prf` pour les couleurs et polices).
- **Enregistrement sur disque** : Ajout d'un service capable de sauvegarder et recharger ces fichiers facilement.
- **Amélioration du PDF** : Prise en compte de la charte graphique et gestion des images secondaires (choix de la taille et de l'alignement).

### B. Couche Vue (`vue`)
- **Panneau d'image** : Ajout de boutons et de champs simples pour choisir une image, régler ses dimensions et choisir son placement.
- **Barre de menu** : Intégration des menus *Projet* et *Profil/Style* pour enregistrer ou ouvrir des éléments en deux clics.
- **Confort d'utilisation** : Ajout d'une barre de chargement, d'infobulles explicatives au survol des boutons et de messages de statut colorés (vert pour le succès, rouge en cas d'erreur).

### C. Couche Contrôleur (`controleur`)
- **Gestion des actions** : Liaison entre les clics de l'utilisateur (boutons, menus) et les fonctionnalités du programme.
- **Calculs en tâche de fond** : Exécution de la génération du QR Code et du PDF en arrière-plan pour garder la fenêtre fluide et réactive.

---

## 3. Défis Techniques Rencontrés et Solutions

| Défi rencontré | Cause | Solution apportée |
| :--- | :--- | :--- |
| **Fenêtre qui ne répond plus (écran figé)** | La création du QR Code et du PDF bloquait toute l'interface pendant la durée des calculs. | Exécution des tâches lourdes en arrière-plan pour garder l'application fluide à tout moment. |
| **Erreurs d'importation (`cannot find symbol class QRcodesaisi`)** | Découpage des paquets MVC : la classe du contrôleur manquait de l'importation vers le paquet `vue`. | Ajout de l'import `import vue.QRcodesaisi;` dans `QRCodeController.java`. |
| **Conflits de versionnement Git (`non-fast-forward` / `MERGE_HEAD`)** | Décalage entre l'historique local et la branche distante suite à des commits divergents. | Annulation du merge bloqué via `git merge --abort`, suivi d'un `git pull --rebase origin main` pour réaligner l'historique. |

---

## 4. Assurance Qualité et Tests
Une suite de **tests unitaires JUnit** a été mise en place dans le dossier `test/modele/` afin de garantir la non-régression de l'application :
- Validation de l'encodage du QR Code dans `QRCodeServiceTest`.
- Vérification de la sérialisation / désérialisation des fichiers dans `ProjetServiceTest`.
- Contrôle de la création de documents et de la gestion des erreurs d'accès fichiers dans `PDFServiceTest`.

---