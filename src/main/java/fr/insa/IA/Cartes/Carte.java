package fr.insa.IA.Cartes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Player.Player;

public class Carte implements Comparable<Carte>, Serializable {

    private Hauteur hauteur;
    private Couleur couleur;

    /**
     * @param h hauteur de la carte (voir enum HAUTEUR)
     * @param c couleur de la carte (voir enum COULEUR)
     */
    public Carte(Hauteur h, Couleur c) {
        couleur = c;
        hauteur = h;
    }

    /**
     * @param arg0 est la carte à comparer à l'objet courant
     * @return retourne un entier <0 si l'objet courant est inférieur à arg0 (objet
     *         en paramètre) et >0 si c'est l'inverse
     */
    @Override
    public int compareTo(Carte arg0) {
        // Pas d'atout pour l'instant
        return this.hauteur.toScore() - arg0.hauteur.toScore();
    }

    /**
     * C'est plutot un print qu'un toString
     * 
     * @return retourne null quoi qu'il arrive
     */
    public String toString() {
        List<Carte> ens = new ArrayList<>();
        ens.add(this);
        Carte.printCards(ens);
        return null;
    }

    /**
     * Trie une List de {@link Carte} selon la couleur et la hauteur. Nécessaire
     * pour l'IA d'avoir toujours une main triée. Tri en place.
     * 
     * @param cartes Liste à trier
     */
    public static void sortCartes(List<Carte> cartes) {
        cartes.sort((Carte c1, Carte c2) -> {
            if (c1.getCouleur() == c2.getCouleur())
                return c1.compareTo(c2);
            if (c1.getCouleur() == Couleur.CARREAU)
                return 1;
            if (c1.getCouleur() == Couleur.PIQUE)
                return -1;
            else
                throw new RuntimeException("Couleur non supportée.");
        });
    }

    @Override
    public int hashCode() {
        return (hauteur.ordinal() * 13 + couleur.ordinal()) * 53;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Carte o = (Carte) obj;
        return (o.couleur == couleur && o.hauteur == hauteur);
    }

    /**
     * Afficher un ensemble de cartes sur une ligne.
     * cf RlCards
     * 
     * @param cartes Collection de Cartes
     */
    public static void printCards(List<Carte> cartes) {
        List<String> lignes = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            lignes.add("");
        for (Carte carte : cartes) {
            if (carte == null) {
                lignes.set(0, lignes.get(0) + "           ");
                lignes.set(1, lignes.get(1) + "           ");
                lignes.set(2, lignes.get(2) + "           ");
                lignes.set(3, lignes.get(3) + "           ");
                lignes.set(4, lignes.get(4) + "           ");
                lignes.set(5, lignes.get(5) + "           ");
                lignes.set(6, lignes.get(6) + "           ");
                lignes.set(7, lignes.get(7) + "           ");
                lignes.set(8, lignes.get(8) + "           ");
            } else if (cartes.indexOf(carte) < Player.HAND_SIZE + Player.TABLE_SIZE) {
                lignes.set(0, lignes.get(0) + "┌─────────┐");
                lignes.set(1, lignes.get(1) + "│" + carte.hauteur.toString() + "        │");
                lignes.set(2, lignes.get(2) + "│         │");
                lignes.set(3, lignes.get(3) + "│         │");
                lignes.set(4, lignes.get(4) + "│    " + carte.couleur.toString() + "    │");
                lignes.set(5, lignes.get(5) + "│         │");
                lignes.set(6, lignes.get(6) + "│         │");
                lignes.set(7, lignes.get(7) + "│        " + carte.hauteur.toString() + "│");
                lignes.set(8, lignes.get(8) + "└─────────┘");
            } else {
                lignes.set(0, lignes.get(0) + "┌─────────┐");
                lignes.set(1, lignes.get(1) + "│░░░░░░░░░│");
                lignes.set(2, lignes.get(2) + "│░░░░░░░░░│");
                lignes.set(3, lignes.get(3) + "│░░░░░░░░░│");
                lignes.set(4, lignes.get(4) + "│░░░░░░░░░│");
                lignes.set(5, lignes.get(5) + "│░░░░░░░░░│");
                lignes.set(6, lignes.get(6) + "│░░░░░░░░░│");
                lignes.set(7, lignes.get(7) + "│░░░░░░░░░│");
                lignes.set(8, lignes.get(8) + "└─────────┘");
            }
        }
        for (String s : lignes)
            System.out.println(s);
    }

    public static void printEnemyCards(List<Carte> cartes) {
        List<String> lignes = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            lignes.add("");
        for (Carte carte : cartes) {
            if (carte == null) {
                lignes.set(0, lignes.get(0) + "           ");
                lignes.set(1, lignes.get(1) + "           ");
                lignes.set(2, lignes.get(2) + "           ");
                lignes.set(3, lignes.get(3) + "           ");
                lignes.set(4, lignes.get(4) + "           ");
                lignes.set(5, lignes.get(5) + "           ");
                lignes.set(6, lignes.get(6) + "           ");
                lignes.set(7, lignes.get(7) + "           ");
                lignes.set(8, lignes.get(8) + "           ");
            } else if (cartes.indexOf(carte) >= Player.HAND_SIZE && cartes.indexOf(carte) < Player.HAND_SIZE + Player.TABLE_SIZE) {
                lignes.set(0, lignes.get(0) + "┌─────────┐");
                lignes.set(1, lignes.get(1) + "│" + carte.hauteur.toString() + "        │");
                lignes.set(2, lignes.get(2) + "│         │");
                lignes.set(3, lignes.get(3) + "│         │");
                lignes.set(4, lignes.get(4) + "│    " + carte.couleur.toString() + "    │");
                lignes.set(5, lignes.get(5) + "│         │");
                lignes.set(6, lignes.get(6) + "│         │");
                lignes.set(7, lignes.get(7) + "│        " + carte.hauteur.toString() + "│");
                lignes.set(8, lignes.get(8) + "└─────────┘");
            } else {
                lignes.set(0, lignes.get(0) + "┌─────────┐");
                lignes.set(1, lignes.get(1) + "│░░░░░░░░░│");
                lignes.set(2, lignes.get(2) + "│░░░░░░░░░│");
                lignes.set(3, lignes.get(3) + "│░░░░░░░░░│");
                lignes.set(4, lignes.get(4) + "│░░░░░░░░░│");
                lignes.set(5, lignes.get(5) + "│░░░░░░░░░│");
                lignes.set(6, lignes.get(6) + "│░░░░░░░░░│");
                lignes.set(7, lignes.get(7) + "│░░░░░░░░░│");
                lignes.set(8, lignes.get(8) + "└─────────┘");
            }
        }
        for (String s : lignes)
            System.out.println(s);
    }

    public static void printCards(Carte carte) {
        List<Carte> clist = new ArrayList<>();
        clist.add(carte);
        printCards(clist);
    }

    public Hauteur getHauteur() {
        return this.hauteur;
    }

    public Couleur getCouleur() {
        return this.couleur;
    }
}
