package modele;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class QRCodeModel {
    private String texteOuLien;
    private BufferedImage qrCodeImage;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public String getTexteOuLien() {
        return texteOuLien;
    }

    public BufferedImage getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeData(String texte, BufferedImage image) {
        String oldTexte = this.texteOuLien;
        this.texteOuLien = texte;
        this.qrCodeImage = image;
        pcs.firePropertyChange("qrCodeData", oldTexte, texte);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}