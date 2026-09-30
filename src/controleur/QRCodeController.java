package controleur;

import modele.PDFService;
import modele.QRCodeModel;
import modele.QRCodeService;
import vue.QRcodesaisi;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class QRCodeController {
    private final QRCodeModel model;
    private final QRcodesaisi view;

    public QRCodeController(QRCodeModel model, QRcodesaisi view) {
        this.model = model;
        this.view = view;

        this.view.addGenerateListener(e -> genererQRCode());

        this.view.addExportListener(e -> exporterPDF());

        this.model.addPropertyChangeListener(evt -> {
            if ("qrCodeData".equals(evt.getPropertyName())) {
                this.view.setQRCodeImage(this.model.getQrCodeImage());
            }
        });
    }

    private void genererQRCode() {
        String texte = view.getInputText();
        if (texte.isEmpty()) {
            view.setStatusMessage("Erreur : Veuillez saisir un texte ou un lien.");
            return;
        }

        try {
            BufferedImage image = QRCodeService.genererQRCode(texte, 200, 200);
            model.setQrCodeData(texte, image);
        } catch (Exception ex) {
            view.setStatusMessage("Erreur lors de la génération du QR Code.");
        }
    }

    private void exporterPDF() {
        if (model.getQrCodeImage() == null) {
            view.setStatusMessage("Erreur : Veuillez générer un QR Code avant d'exporter.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le fichier PDF");
        int userSelection = fileChooser.showSaveDialog(view);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String chemin = fileChooser.getSelectedFile().getAbsolutePath();
            if (!chemin.endsWith(".pdf")) chemin += ".pdf";

            try {
                PDFService.exporterPDF(chemin, model.getTexteOuLien(), model.getQrCodeImage());
                view.setStatusMessage("PDF généré avec succès !");
            } catch (Exception ex) {
                view.setStatusMessage("Erreur lors de l'exportation du PDF.");
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