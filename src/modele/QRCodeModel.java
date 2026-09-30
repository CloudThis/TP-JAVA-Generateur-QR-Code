package modele;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class QRCodeModel {
    private String texteOuLien;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public String getTexteOuLien() {
        return texteOuLien;
    }

    public void setTexteOuLien(String texteOuLien) {
        String oldVal = this.texteOuLien;
        this.texteOuLien = texteOuLien;
        pcs.firePropertyChange("texteOuLien", oldVal, texteOuLien);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}