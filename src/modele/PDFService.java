package modele;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class PDFService {

    public static void exporterPDF(String cheminFichier, String contenu, BufferedImage qrImage) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(cheminFichier));

        document.open();

        Paragraph titre = new Paragraph("Rapport de génération de QR Code");
        titre.setAlignment(Element.ALIGN_CENTER);
        document.add(titre);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Contenu encodé : " + contenu));
        document.add(new Paragraph("\n"));

        if (qrImage != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            Image pdfImg = Image.getInstance(baos.toByteArray());
            pdfImg.setAlignment(Element.ALIGN_CENTER);
            pdfImg.scaleToFit(200, 200);
            document.add(pdfImg);
        }

        document.close();
    }
}