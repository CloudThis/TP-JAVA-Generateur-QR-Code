package modele;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

public class QRCodeService {

    public static BufferedImage genererQRCode(String texte, int largeur, int hauteur) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(texte, BarcodeFormat.QR_CODE, largeur, hauteur);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}