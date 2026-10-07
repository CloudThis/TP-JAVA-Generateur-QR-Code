package modele;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFService {

    public static void exporterPDF(String cheminFichier, String contenu, BufferedImage qrImage,
                                   String cheminImageExtra, int largeurImg, int hauteurImg, int alignementImg,
                                   ProfilData profil)
            throws FileNotFoundException, DocumentException, IOException {

        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        FileOutputStream fos = new FileOutputStream(cheminFichier);
        PdfWriter.getInstance(document, fos);

        document.open();

        BaseColor colPrimary = new BaseColor(profil.getCouleurPrimaire().getRGB());
        BaseColor colAccent  = new BaseColor(profil.getCouleurAccent().getRGB());
        BaseColor colBgCard  = new BaseColor(profil.getCouleurFond().getRGB());

        Font fontTitre     = FontFactory.getFont(profil.getNomPolice(), profil.getTailleTitre(), Font.BOLD, colPrimary);
        Font fontSousTitre = FontFactory.getFont(profil.getNomPolice(), 11, Font.ITALIC, colAccent);

        Paragraph titre = new Paragraph("Rapport de Génération QR Code", fontTitre);
        titre.setAlignment(Element.ALIGN_CENTER);
        document.add(titre);

        Paragraph sousTitre = new Paragraph("Document généré automatiquement via l'application MVC", fontSousTitre);
        sousTitre.setAlignment(Element.ALIGN_CENTER);
        sousTitre.setSpacingAfter(15);
        document.add(sousTitre);

        LineSeparator line = new LineSeparator(2f, 100, colAccent, Element.ALIGN_CENTER, -5);
        document.add(line);
        document.add(new Paragraph("\n"));

        if (cheminImageExtra != null && !cheminImageExtra.trim().isEmpty()) {
            try {
                Image extraImg = Image.getInstance(cheminImageExtra);
                extraImg.scaleToFit(largeurImg, hauteurImg);

                switch (alignementImg) {
                    case 0:  extraImg.setAlignment(Element.ALIGN_LEFT); break;
                    case 2:  extraImg.setAlignment(Element.ALIGN_RIGHT); break;
                    case 1:
                    default: extraImg.setAlignment(Element.ALIGN_CENTER); break;
                }

                extraImg.setSpacingAfter(15);
                document.add(extraImg);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'insertion de l'image : " + e.getMessage());
            }
        }

        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(85);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(colBgCard);
        cell.setBorderColor(colAccent);
        cell.setBorderWidth(1.5f);
        cell.setPadding(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        if (qrImage != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            Image pdfImg = Image.getInstance(baos.toByteArray());
            pdfImg.setAlignment(Element.ALIGN_CENTER);
            pdfImg.scaleToFit(180, 180);
            cell.addElement(pdfImg);
        }

        card.addCell(cell);
        document.add(card);

        document.close();
    }
}