package controleur;

import modele.QRCodeModel;
import vue.QRcodesaisi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QRCodeController {
    private final QRCodeModel model;
    private final QRcodesaisi view;

    public QRCodeController(QRCodeModel model, QRcodesaisi view) {
        this.model = model;
        this.view = view;

        this.view.addGenerateListener(new GenererAction());

        this.model.addPropertyChangeListener(evt -> {
            if ("texteOuLien".equals(evt.getPropertyName())) {
                String texte = (String) evt.getNewValue();
                this.view.setStatusMessage("Donnée validée : " + texte);
            }
        });
    }

    private class GenererAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String saisie = view.getInputText();
            if (saisie.isEmpty()) {
                view.setStatusMessage("Erreur : Veuillez saisir un texte ou un lien.");
            } else {
                model.setTexteOuLien(saisie);
            }
        }
    }

    public static void main(String[] args) {
        QRCodeModel model = new QRCodeModel();
        QRcodesaisi view = new QRcodesaisi();
        new QRCodeController(model, view);

        view.setVisible(true);
    }
}
