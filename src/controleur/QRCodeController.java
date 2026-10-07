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
                this.view.setStatusMessage("QR Code généré avec succès.", false);
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
            view.setStatusMessage("Image sélectionnée : " + selectedFile.getName(), false);
        }
    }

    private void genererQRCode() {
        String texte = view.getInputText();
        if (texte.isEmpty()) {
            view.setStatusMessage("Saisie invalide : le champ est vide.", true);
            view.afficherErreur("Saisie invalide", "Veuillez saisir un texte ou une URL avant de générer.");
            return;
        }

        view.demarrerChargement("Génération du QR Code en cours...");

        SwingWorker<BufferedImage, Void> worker = new SwingWorker<>() {
            @Override
            protected BufferedImage doInBackground() throws Exception {
                return QRCodeService.genererQRCode(texte, 250, 250);
            }

            @Override
            protected void done() {
                try {
                    BufferedImage image = get();
                    model.setQrCodeData(texte, image);
                } catch (Exception ex) {
                    view.setStatusMessage("Erreur lors de la génération.", true);
                    view.afficherErreur("Erreur de Génération", "Impossible de créer le QR Code : " + ex.getCause().getMessage());
                } finally {
                    view.arreterChargement();
                }
            }
        };
        worker.execute();
    }

    private void exporterPDF() {
        if (model.getQrCodeImage() == null) {
            view.setStatusMessage("Export impossible : aucun QR Code.", true);
            view.afficherErreur("Export PDF", "Veuillez d'abord générer un QR Code.");
            return;
        }

        model.setImageWidth(view.getImageWidth());
        model.setImageHeight(view.getImageHeight());
        model.setImageAlignment(view.getImageAlignment());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le fichier PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Document PDF (*.pdf)", "pdf"));

        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".pdf")) {
                path += ".pdf";
            }

            final String finalPath = path;
            view.demarrerChargement("Création du fichier PDF en cours...");

            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    PDFService.exporterPDF(
                            finalPath,
                            model.getTexteOuLien(),
                            model.getQrCodeImage(),
                            model.getImagePath(),
                            model.getImageWidth(),
                            model.getImageHeight(),
                            model.getImageAlignment(),
                            model.getProfil()
                    );
                    return true;
                }

                @Override
                protected void done() {
                    try {
                        get();
                        view.setStatusMessage("Document PDF exporté avec succès.", false);
                        view.afficherInformation("Succès", "Le fichier PDF a été sauvegardé dans :\n" + finalPath);
                    } catch (Exception ex) {
                        view.setStatusMessage("Échec de l'exportation PDF.", true);
                        if (ex.getCause() instanceof FileNotFoundException) {
                            view.afficherErreur("Fichier verrouillé", "Impossible d'écrire dans ce fichier. Vérifiez qu'il n'est pas déjà ouvert.");
                        } else {
                            view.afficherErreur("Erreur PDF", "Erreur lors de la génération : " + ex.getCause().getMessage());
                        }
                    } finally {
                        view.arreterChargement();
                    }
                }
            };
            worker.execute();
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
                view.setStatusMessage("Projet sauvegardé.", false);
                view.afficherInformation("Projet", "Le projet a été sauvegardé avec succès !");
            } catch (IOException ex) {
                view.setStatusMessage("Erreur de sauvegarde.", true);
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

                view.setStatusMessage("Projet chargé.", false);
                view.afficherInformation("Projet", "Projet chargé avec succès !");
            } catch (Exception ex) {
                view.setStatusMessage("Erreur de chargement.", true);
                view.afficherErreur("Erreur Chargement", "Impossible de charger le projet : " + ex.getMessage());
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
                view.setStatusMessage("Profil sauvegardé.", false);
                view.afficherInformation("Profil", "Le profil de style a été sauvegardé !");
            } catch (IOException ex) {
                view.setStatusMessage("Erreur de sauvegarde du profil.", true);
                view.afficherErreur("Erreur", "Impossible de sauvegarder le profil : " + ex.getMessage());
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
                view.setStatusMessage("Profil de style appliqué.", false);
                view.afficherInformation("Profil", "Profil appliqué avec succès !");
            } catch (Exception ex) {
                view.setStatusMessage("Erreur de chargement du profil.", true);
                view.afficherErreur("Erreur", "Impossible de charger le profil : " + ex.getMessage());
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