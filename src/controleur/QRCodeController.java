package controleur;

import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import modele.PDFService;
import modele.QRCodeModel;
import modele.QRCodeService;
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

        int choice = chooser.showOpenDialog(view);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            model.setImagePath(selectedFile.getAbsolutePath());
            view.setImagePathText(selectedFile.getName());
        }
    }

    private void genererQRCode() {
        String texte = view.getInputText();

        if (texte.isEmpty()) {
            view.setStatusMessage("Erreur : Champ vide.");
            view.afficherErreur("Saisie invalide", "Veuillez saisir un texte ou une URL avant de générer.");
            return;
        }

        try {
            BufferedImage image = QRCodeService.genererQRCode(texte, 200, 200);
            model.setQrCodeData(texte, image);
        } catch (WriterException ex) {
            view.setStatusMessage("Erreur de génération.");
            view.afficherErreur("Erreur QR Code", "Impossible de générer le QR Code à partir du texte fourni.");
        } catch (Exception ex) {
            view.setStatusMessage("Erreur inattendue.");
            view.afficherErreur("Erreur système", "Une erreur inattendue est survenue : " + ex.getMessage());
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
                        model.getImageAlignment()
                );
                view.setStatusMessage("PDF exporté.");
                view.afficherInformation("Succès", "Le fichier PDF a été généré avec succès !");
            } catch (FileNotFoundException ex) {
                view.setStatusMessage("Erreur d'accès au fichier.");
                view.afficherErreur("Fichier verrouillé",
                        "Impossible d'écrire dans ce fichier.\nVérifiez qu'il n'est pas déjà ouvert dans un autre programme.");
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