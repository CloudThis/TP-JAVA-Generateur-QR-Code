package vue;

import modele.ProfilData;

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

    private JMenuItem itemSauvegarderProjet;
    private JMenuItem itemChargerProjet;
    private JMenuItem itemSauvegarderProfil;
    private JMenuItem itemChargerProfil;

    public QRcodesaisi() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ignored) {}

        setTitle("Générateur de QR Code - Projets & Profils");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        creerMenu();
        initCustomComponents();

        setContentPane(mainPanel);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        setMinimumSize(new Dimension(600, 480));
        pack();
        setLocationRelativeTo(null);

        Contenu.setText("Texte / URL : ");
        generateButton.setText("Générer");
    }

    private void creerMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFichier = new JMenu("Projet");
        itemSauvegarderProjet = new JMenuItem("Sauvegarder le Projet");
        itemChargerProjet = new JMenuItem("Ouvrir un Projet");
        menuFichier.add(itemSauvegarderProjet);
        menuFichier.add(itemChargerProjet);

        JMenu menuProfil = new JMenu("Profil / Style");
        itemSauvegarderProfil = new JMenuItem("Sauvegarder le Profil");
        itemChargerProfil = new JMenuItem("Charger un Profil");
        menuProfil.add(itemSauvegarderProfil);
        menuProfil.add(itemChargerProfil);

        menuBar.add(menuFichier);
        menuBar.add(menuProfil);
        setJMenuBar(menuBar);
    }

    private void initCustomComponents() {
        selectImageButton = new JButton("Parcourir image...");
        imagePathLabel = new JLabel("Aucune image");

        widthSpinner = new JSpinner(new SpinnerNumberModel(150, 20, 500, 10));
        heightSpinner = new JSpinner(new SpinnerNumberModel(150, 20, 500, 10));

        alignComboBox = new JComboBox<>(new String[]{"Gauche", "Centre", "Droite"});
        alignComboBox.setSelectedIndex(1);

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

    public String getInputText() { return inputTextField.getText().trim(); }
    public void setInputText(String text) { inputTextField.setText(text); }

    public void setImagePathText(String path) { imagePathLabel.setText(path); }
    public int getImageWidth() { return (int) widthSpinner.getValue(); }
    public void setImageWidth(int w) { widthSpinner.setValue(w); }

    public int getImageHeight() { return (int) heightSpinner.getValue(); }
    public void setImageHeight(int h) { heightSpinner.setValue(h); }

    public int getImageAlignment() { return alignComboBox.getSelectedIndex(); }
    public void setImageAlignment(int idx) { alignComboBox.setSelectedIndex(idx); }

    public void addGenerateListener(ActionListener l) { generateButton.addActionListener(l); }
    public void addExportListener(ActionListener l) { exportButton.addActionListener(l); }
    public void addSelectImageListener(ActionListener l) { selectImageButton.addActionListener(l); }

    public void addSauvegarderProjetListener(ActionListener l) { itemSauvegarderProjet.addActionListener(l); }
    public void addChargerProjetListener(ActionListener l) { itemChargerProjet.addActionListener(l); }
    public void addSauvegarderProfilListener(ActionListener l) { itemSauvegarderProfil.addActionListener(l); }
    public void addChargerProfilListener(ActionListener l) { itemChargerProfil.addActionListener(l); }

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