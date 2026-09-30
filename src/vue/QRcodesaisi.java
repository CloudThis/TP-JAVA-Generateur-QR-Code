package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class QRcodesaisi extends JFrame {
    private JPanel mainPanel;
    private JLabel Contenu;
    private JTextField inputTextField;
    private JButton generateButton;
    private JLabel qrCodeDisplayLabel;
    private JButton exportButton;

    public QRcodesaisi() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ignored) {}

        setTitle("Générateur de QR Code");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        setMinimumSize(new Dimension(450, 200));
        pack();
        setLocationRelativeTo(null);

        Contenu.setText("Texte / URL : ");
        generateButton.setText("Générer");
    }

    public String getInputText() {
        return inputTextField.getText().trim();
    }

    public void addGenerateListener(ActionListener listener) {
        generateButton.addActionListener(listener);
    }

    public void setStatusMessage(String message) {
        qrCodeDisplayLabel.setIcon(null);
        qrCodeDisplayLabel.setText(message);
    }

    public void addExportListener(ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public void setQRCodeImage(BufferedImage image) {
        qrCodeDisplayLabel.setText("");
        qrCodeDisplayLabel.setIcon(new ImageIcon(image));
    }

    public void afficherErreur(String titre, String message) {
        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.ERROR_MESSAGE);
    }

    public void afficherInformation(String titre, String message) {
        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.INFORMATION_MESSAGE);
    }
}