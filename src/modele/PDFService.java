package modele;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class PDFService {

    public static void exporterPDF(String cheminFichier, String contenu) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(cheminFichier));

        document.open();

        Paragraph titre = new Paragraph("Rapport de génération de QR Code");
        titre.setAlignment(Element.ALIGN_CENTER);
        document.add(titre);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Contenu encodé : " + contenu));


        document.close();
    }
}