package vue;

import javax.swing.*;
import java.awt.event.ActionListener;

public class QRcodesaisi extends JFrame {
    private JPanel mainPanel;
    private JLabel Contenu;
    private JTextField inputTextField;
    private JButton generateButton;
    private JLabel qrCodeDisplayLabel;

    public QRcodesaisi() {
        setTitle("Générateur de QR Code");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
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
}