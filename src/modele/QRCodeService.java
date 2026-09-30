package modele;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

public class QRCodeService {

    public static BufferedImage genererQRCode(String texte, int largeur, int hauteur) throws WriterException {
        if (texte == null || texte.trim().isEmpty()) {
            throw new IllegalArgumentException("Le texte fourni ne peut pas être vide.");
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(texte, BarcodeFormat.QR_CODE, largeur, hauteur);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}