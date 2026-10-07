package modele;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class QRCodeModel {
    private String texteOuLien;
    private BufferedImage qrCodeImage;

    private String imagePath;
    private int imageWidth = 150;
    private int imageHeight = 150;
    private int imageAlignment = 1;

    private ProfilData profil = new ProfilData();

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public String getTexteOuLien() { return texteOuLien; }

    public BufferedImage getQrCodeImage() { return qrCodeImage; }

    public void setQrCodeData(String texte, BufferedImage image) {
        String oldTexte = this.texteOuLien;
        this.texteOuLien = texte;
        this.qrCodeImage = image;
        pcs.firePropertyChange("qrCodeData", oldTexte, texte);
    }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public int getImageWidth() { return imageWidth; }
    public void setImageWidth(int imageWidth) { this.imageWidth = imageWidth; }

    public int getImageHeight() { return imageHeight; }
    public void setImageHeight(int imageHeight) { this.imageHeight = imageHeight; }

    public int getImageAlignment() { return imageAlignment; }
    public void setImageAlignment(int imageAlignment) { this.imageAlignment = imageAlignment; }

    public ProfilData getProfil() { return profil; }
    public void setProfil(ProfilData profil) {
        ProfilData oldProfil = this.profil;
        this.profil = profil;
        pcs.firePropertyChange("profil", oldProfil, profil);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}