package controleur;

import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import modele.*;
import vue.QRcodesaisi;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class QRCodeController {
    private final QRCodeModel model;
    private final QRcodesaisi view;

    public QRCodeController(QRCodeModel model, QRcodesaisi view) {
        this.model = model;
        this.view = view;

        this.view.addGenerateListener(e -> genererQRCode());
        this.view.addExportListener(e -> exporterPDF());
        this.view.addSelectImageListener(e -> choisirImage());

        this.view.addSauvegarderProjetListener(e -> sauvegarderProjet());
        this.view.addChargerProjetListener(e -> chargerProjet());

        this.view.addSauvegarderProfilListener(e -> sauvegarderProfil());
        this.view.addChargerProfilListener(e -> chargerProfil());

        this.model.addPropertyChangeListener(evt -> {
            if ("qrCodeData".equals(evt.getPropertyName())) {
                this.view.setQRCodeImage(this.model.getQrCodeImage());
                this.view.setStatusMessage("QR Code généré avec succès.");
            }
        });
    }

    private void choisirImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choisir une image pour le PDF");
        chooser.setFileFilter(new FileNameExtensionFilter("Images (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            model.setImagePath(selectedFile.getAbsolutePath());
            view.setImagePathText(selectedFile.getName());
        }
    }

    private void genererQRCode() {
        String texte = view.getInputText();
        if (texte.isEmpty()) {
            view.setStatusMessage("Erreur : Champ vide.");
            view.afficherErreur("Saisie invalide", "Veuillez saisir un texte ou une URL.");
            return;
        }

        try {
            BufferedImage image = QRCodeService.genererQRCode(texte, 200, 200);
            model.setQrCodeData(texte, image);
        } catch (Exception ex) {
            view.setStatusMessage("Erreur lors de la génération.");
            view.afficherErreur("Erreur QR Code", ex.getMessage());
        }
    }


    private void sauvegarderProjet() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sauvegarder le Projet");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers Projet (*.qrp)", "qrp"));

        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".qrp")) path += ".qrp";

            ProjetData data = new ProjetData(
                    view.getInputText(),
                    model.getImagePath(),
                    view.getImageWidth(),
                    view.getImageHeight(),
                    view.getImageAlignment()
            );

            try {
                ProjetService.sauvegarderObjet(path, data);
                view.afficherInformation("Projet", "Le projet a été sauvegardé avec succès !");
            } catch (IOException ex) {
                view.afficherErreur("Erreur Sauvegarde", "Impossible de sauvegarder le projet : " + ex.getMessage());
            }
        }
    }

    private void chargerProjet() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouvrir un Projet");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers Projet (*.qrp)", "qrp"));

        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                ProjetData data = ProjetService.chargerObjet(path);

                view.setInputText(data.getTexteOuLien());
                model.setImagePath(data.getImagePath());
                view.setImagePathText(data.getImagePath() != null ? new File(data.getImagePath()).getName() : "Aucune image");
                view.setImageWidth(data.getImageWidth());
                view.setImageHeight(data.getImageHeight());
                view.setImageAlignment(data.getImageAlignment());

                if (!data.getTexteOuLien().isEmpty()) {
                    genererQRCode();
                }

                view.afficherInformation("Projet", "Projet chargé avec succès !");
            } catch (Exception ex) {
                view.afficherErreur("Erreur Chargement", "Impossible de charger le fichier projet : " + ex.getMessage());
            }
        }
    }


    private void sauvegarderProfil() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sauvegarder le Profil de Style");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers Profil (*.prf)", "prf"));

        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".prf")) path += ".prf";

            try {
                ProjetService.sauvegarderObjet(path, model.getProfil());
                view.afficherInformation("Profil", "Le profil de style a été sauvegardé avec succès !");
            } catch (IOException ex) {
                view.afficherErreur("Erreur Sauvegarde", "Impossible de sauvegarder le profil : " + ex.getMessage());
            }
        }
    }

    private void chargerProfil() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Charger un Profil de Style");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers Profil (*.prf)", "prf"));

        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                ProfilData profil = ProjetService.chargerObjet(path);
                model.setProfil(profil);
                view.afficherInformation("Profil", "Le profil de style a été appliqué avec succès !");
            } catch (Exception ex) {
                view.afficherErreur("Erreur Chargement", "Impossible de charger le profil : " + ex.getMessage());
            }
        }
    }

    private void exporterPDF() {
        if (model.getQrCodeImage() == null) {
            view.setStatusMessage("Export impossible.");
            view.afficherErreur("Export PDF", "Veuillez générer un QR Code avant de tenter un export PDF.");
            return;
        }

        model.setImageWidth(view.getImageWidth());
        model.setImageHeight(view.getImageHeight());
        model.setImageAlignment(view.getImageAlignment());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le fichier PDF");
        int userSelection = fileChooser.showSaveDialog(view);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String chemin = fileChooser.getSelectedFile().getAbsolutePath();
            if (!chemin.toLowerCase().endsWith(".pdf")) {
                chemin += ".pdf";
            }

            try {
                PDFService.exporterPDF(
                        chemin,
                        model.getTexteOuLien(),
                        model.getQrCodeImage(),
                        model.getImagePath(),
                        model.getImageWidth(),
                        model.getImageHeight(),
                        model.getImageAlignment(),
                        model.getProfil()
                );
                view.setStatusMessage("PDF exporté.");
                view.afficherInformation("Succès", "Le fichier PDF a été généré avec succès !");
            } catch (FileNotFoundException ex) {
                view.setStatusMessage("Erreur d'accès au fichier.");
                view.afficherErreur("Fichier verrouillé", "Vérifiez que le fichier n'est pas déjà ouvert.");
            } catch (DocumentException | IOException ex) {
                view.setStatusMessage("Erreur lors de la création du PDF.");
                view.afficherErreur("Erreur PDF", "Une erreur est survenue lors de l'écriture du PDF.");
            }
        }
    }

    public static void main(String[] args) {
        QRCodeModel model = new QRCodeModel();
        QRcodesaisi view = new QRcodesaisi();
        new QRCodeController(model, view);
        view.setVisible(true);
    }
}