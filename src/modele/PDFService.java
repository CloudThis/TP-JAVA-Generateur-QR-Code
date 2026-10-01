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

    private static final BaseColor COLOR_PRIMARY = new BaseColor(30, 58, 138);   // Bleu nuit (#1E3A8A)
    private static final BaseColor COLOR_ACCENT  = new BaseColor(59, 130, 246);  // Bleu vif (#3B82F6)
    private static final BaseColor COLOR_TEXT    = new BaseColor(31, 41, 55);    // Gris foncé (#1F2937)
    private static final BaseColor COLOR_BG_CARD = new BaseColor(243, 244, 246); // Gris clair (#F3F4F6)

    public static void exporterPDF(String cheminFichier, String contenu, BufferedImage qrImage)
            throws FileNotFoundException, DocumentException, IOException {

        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        FileOutputStream fos = new FileOutputStream(cheminFichier);
        PdfWriter.getInstance(document, fos);

        document.open();

        Font fontTitre     = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, COLOR_PRIMARY);
        Font fontSousTitre = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.ITALIC, COLOR_ACCENT);
        Font fontLabel     = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, COLOR_PRIMARY);
        Font fontValeur    = FontFactory.getFont(FontFactory.HELVETICA, 11, COLOR_TEXT);

        Paragraph titre = new Paragraph("Rapport de Génération QR Code", fontTitre);
        titre.setAlignment(Element.ALIGN_CENTER);
        document.add(titre);

        Paragraph sousTitre = new Paragraph("Document généré automatiquement via l'application MVC", fontSousTitre);
        sousTitre.setAlignment(Element.ALIGN_CENTER);
        sousTitre.setSpacingAfter(15);
        document.add(sousTitre);

        LineSeparator line = new LineSeparator(2f, 100, COLOR_ACCENT, Element.ALIGN_CENTER, -5);
        document.add(line);
        document.add(new Paragraph("\n"));

        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(85);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_BG_CARD);
        cell.setBorderColor(COLOR_ACCENT);
        cell.setBorderWidth(1.5f);
        cell.setPadding(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Paragraph pLabel = new Paragraph("Contenu encodé :", fontLabel);
        pLabel.setSpacingAfter(5);
        cell.addElement(pLabel);

        Paragraph pValeur = new Paragraph(contenu, fontValeur);
        pValeur.setSpacingAfter(20);
        cell.addElement(pValeur);

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