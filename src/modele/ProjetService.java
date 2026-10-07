package modele;

import java.io.*;

public class ProjetService {

    @SuppressWarnings("unchecked")
    public static <T> void sauvegarderObjet(String chemin, T objet) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chemin))) {
            oos.writeObject(objet);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T chargerObjet(String chemin) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chemin))) {
            return (T) ois.readObject();
        }
    }
}