package modele;

import java.io.Serializable;

public class ProjetData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String texteOuLien;
    private String imagePath;
    private int imageWidth;
    private int imageHeight;
    private int imageAlignment;

    public ProjetData(String texteOuLien, String imagePath, int imageWidth, int imageHeight, int imageAlignment) {
        this.texteOuLien = texteOuLien;
        this.imagePath = imagePath;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageAlignment = imageAlignment;
    }

    public String getTexteOuLien() { return texteOuLien; }
    public String getImagePath() { return imagePath; }
    public int getImageWidth() { return imageWidth; }
    public int getImageHeight() { return imageHeight; }
    public int getImageAlignment() { return imageAlignment; }
}