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

    private JButton selectImageButton;
    private JLabel imagePathLabel;
    private JSpinner widthSpinner;
    private JSpinner heightSpinner;
    private JComboBox<String> alignComboBox;

    public QRcodesaisi() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ignored) {}

        setTitle("Générateur de QR Code");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initCustomComponents();

        setContentPane(mainPanel);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        setMinimumSize(new Dimension(550, 450));
        pack();
        setLocationRelativeTo(null);

        Contenu.setText("Texte / URL : ");
        generateButton.setText("Générer");
    }

    private void initCustomComponents() {
        selectImageButton = new JButton("Parcourir image...");
        imagePathLabel = new JLabel("Aucune image sélectionnée");

        widthSpinner = new JSpinner(new SpinnerNumberModel(150, 20, 500, 10));
        heightSpinner = new JSpinner(new SpinnerNumberModel(150, 20, 500, 10));

        alignComboBox = new JComboBox<>(new String[]{"Gauche", "Centre", "Droite"});
        alignComboBox.setSelectedIndex(1); // Centre par défaut

        JPanel imageOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imageOptionPanel.setBorder(BorderFactory.createTitledBorder("Image additionnelle PDF"));

        imageOptionPanel.add(selectImageButton);
        imageOptionPanel.add(imagePathLabel);
        imageOptionPanel.add(new JLabel("L :"));
        imageOptionPanel.add(widthSpinner);
        imageOptionPanel.add(new JLabel("H :"));
        imageOptionPanel.add(heightSpinner);
        imageOptionPanel.add(new JLabel("Alignement :"));
        imageOptionPanel.add(alignComboBox);

        if (mainPanel == null) {
            mainPanel = new JPanel(new BorderLayout());
        }
        mainPanel.add(imageOptionPanel, BorderLayout.SOUTH);
    }

    public String getInputText() {
        return inputTextField.getText().trim();
    }

    public void addGenerateListener(ActionListener listener) {
        generateButton.addActionListener(listener);
    }

    public void addExportListener(ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public void addSelectImageListener(ActionListener listener) {
        selectImageButton.addActionListener(listener);
    }

    public void setImagePathText(String path) {
        imagePathLabel.setText(path);
    }

    public int getImageWidth() {
        return (int) widthSpinner.getValue();
    }

    public int getImageHeight() {
        return (int) heightSpinner.getValue();
    }

    public int getImageAlignment() {
        return alignComboBox.getSelectedIndex(); // 0: Gauche, 1: Centre, 2: Droite
    }

    public void setStatusMessage(String message) {
        qrCodeDisplayLabel.setIcon(null);
        qrCodeDisplayLabel.setText(message);
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