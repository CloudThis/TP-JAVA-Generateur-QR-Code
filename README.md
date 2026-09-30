# Générateur de QR Code (Java Swing - MVC)

Une application développée en Java Swing en utilisant le patron d'architecture **MVC (Modèle-Vue-Contrôleur)**. Elle permet de générer des QR Codes à partir de texte ou d'URL, d'afficher le résultat et d'exporter au format **PDF**.

---

## Fonctionnalités

- **Génération de QR Code** : encodage d'une chaîne de caractères ou d'une URL en image.
- **Exportation PDF** : génération d'un document PDF contenant le texte saisi et l'image du QR Code.
- **Interface native** : intégration d'un rendu visuel adapté au système d'exploitation.
- **Gestion des erreurs** : remontée des exceptions (champs vides, fichier PDF ouvert dans un autre logiciel, etc.) via des boîtes de dialogue.

---


## Structure du Projet

```text
TP_JAVA_Generateur_QR_Code/
├── pom.xml
├── README.md
└── src/
    ├── controleur/
    │   └── QRCodeController.java   # Point d'entrée principal (main) et gestion des événements
    ├── modele/
    │   ├── QRCodeModel.java        # État et données (PropertyChangeSupport)
    │   ├── QRCodeService.java      # Service de génération d'image (ZXing)
    │   └── PDFService.java         # Service d'exportation de document (iText)
    ├── vue/
    │   ├── QRcodesaisi.java        # Composants de l'interface et dialogues
    │   └── QRcodesaisi.form        # Fichier du GUI Designer
└── test/
    └── modele/                     # Tests unitaires JUnit
        ├── QRCodeModelTest.java
        ├── QRCodeService.java
        └── PDFServiceTest.java