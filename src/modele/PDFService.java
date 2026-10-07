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
    private static final BaseColor COLOR_BG_CARD = new BaseColor(243, 244, 246); // Gris clair (#F3F4F6)

    public static void exporterPDF(String cheminFichier, String contenu, BufferedImage qrImage,
                                   String cheminImageExtra, int largeurImg, int hauteurImg, int alignementImg)
            throws FileNotFoundException, DocumentException, IOException {

        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        FileOutputStream fos = new FileOutputStream(cheminFichier);
        PdfWriter.getInstance(document, fos);

        document.open();

        Font fontTitre     = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, COLOR_PRIMARY);
        Font fontSousTitre = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.ITALIC, COLOR_ACCENT);

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

        if (cheminImageExtra != null && !cheminImageExtra.trim().isEmpty()) {
            try {
                Image extraImg = Image.getInstance(cheminImageExtra);
                extraImg.scaleToFit(largeurImg, hauteurImg);

                switch (alignementImg) {
                    case 0:
                        extraImg.setAlignment(Element.ALIGN_LEFT);
                        break;
                    case 2:
                        extraImg.setAlignment(Element.ALIGN_RIGHT);
                        break;
                    case 1:
                    default:
                        extraImg.setAlignment(Element.ALIGN_CENTER);
                        break;
                }

                extraImg.setSpacingAfter(15);
                document.add(extraImg);
            } catch (Exception e) {
                // En cas de problème de lecture de l'image
                System.err.println("Impossible d'insérer l'image additionnelle : " + e.getMessage());
            }
        }

        // 2. Insertion du QR Code dans sa carte
        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(85);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_BG_CARD);
        cell.setBorderColor(COLOR_ACCENT);
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