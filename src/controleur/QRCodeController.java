package controleur;

import modele.PDFService;
import modele.QRCodeModel;
import vue.QRcodesaisi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QRCodeController {
    private final QRCodeModel model;
    private final QRcodesaisi view;

    public QRCodeController(QRCodeModel model, QRcodesaisi view) {
        this.model = model;
        this.view = view;

        this.view.addGenerateListener(e -> {
            String saisie = view.getInputText();
            if (saisie.isEmpty()) {
                view.setStatusMessage("Erreur : Veuillez saisir un texte.");
            } else {
                model.setTexteOuLien(saisie);
            }
        });

        this.view.addExportListener(e -> exporterPDF());

        this.model.addPropertyChangeListener(evt -> {
            if ("texteOuLien".equals(evt.getPropertyName())) {
                view.setStatusMessage("Donnée enregistrée : " + evt.getNewValue());
            }
        });
    }

    private void exporterPDF() {
        if (model.getTexteOuLien() == null || model.getTexteOuLien().isEmpty()) {
            view.setStatusMessage("Erreur : Aucun contenu à exporter.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le fichier PDF");
        int userSelection = fileChooser.showSaveDialog(view);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String chemin = fileChooser.getSelectedFile().getAbsolutePath();
            if (!chemin.endsWith(".pdf")) chemin += ".pdf";

            try {
                PDFService.exporterPDF(chemin, model.getTexteOuLien());
                view.setStatusMessage("PDF généré avec succès !");
            } catch (Exception ex) {
                view.setStatusMessage("Erreur lors de la génération du PDF.");
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