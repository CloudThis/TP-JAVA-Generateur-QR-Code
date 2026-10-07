package modele;

import java.awt.Color;
import java.io.Serializable;

public class ProfilData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nomPolice = "HELVETICA";
    private int tailleTitre = 20;
    private Color couleurPrimaire = new Color(30, 58, 138);
    private Color couleurAccent  = new Color(59, 130, 246);
    private Color couleurFond    = new Color(243, 244, 246);

    public ProfilData() {}

    public ProfilData(String nomPolice, int tailleTitre, Color couleurPrimaire, Color couleurAccent, Color couleurFond) {
        this.nomPolice = nomPolice;
        this.tailleTitre = tailleTitre;
        this.couleurPrimaire = couleurPrimaire;
        this.couleurAccent = couleurAccent;
        this.couleurFond = couleurFond;
    }

    public String getNomPolice() { return nomPolice; }
    public void setNomPolice(String nomPolice) { this.nomPolice = nomPolice; }

    public int getTailleTitre() { return tailleTitre; }
    public void setTailleTitre(int tailleTitre) { this.tailleTitre = tailleTitre; }

    public Color getCouleurPrimaire() { return couleurPrimaire; }
    public void setCouleurPrimaire(Color couleurPrimaire) { this.couleurPrimaire = couleurPrimaire; }

    public Color getCouleurAccent() { return couleurAccent; }
    public void setCouleurAccent(Color couleurAccent) { this.couleurAccent = couleurAccent; }

    public Color getCouleurFond() { return couleurFond; }
    public void setCouleurFond(Color couleurFond) { this.couleurFond = couleurFond; }
}