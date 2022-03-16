package fr.insa.IA.Cartes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Carte implements Comparable<Carte> {
    
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
     * @return retourne un entier <0 si l'objet courant est inférieur à arg0 (objet en paramètre) et >0 si c'est l'inverse
     */
    @Override
    public int compareTo(Carte arg0) {
        // Pas d'atout pour l'instant
        return this.hauteur.ordinal() - arg0.hauteur.ordinal();
    }

    /**
     * C'est plutot un print qu'un toString
     * @return retourne null quoi qu'il arrive
     */
    public String toString() {
        Set<Carte> ens = new HashSet<>();
        ens.add(this);
        Carte.printCards(ens);
        return null;
    }

    /**
     * Afficher un ensemble de cartes sur une ligne.
     * cf RlCards
     * @param cartes Collection de Cartes
     */
    public static void printCards(Collection<Carte> cartes) {
        List<String> lignes = new ArrayList<>(); 
        for (int i = 0 ; i<9 ; i++) lignes.add("");
        for (Carte carte : cartes) {
            if (carte == null) continue;
            lignes.set(0,lignes.get(0) + "┌─────────┐");
            lignes.set(1,lignes.get(1) + "│"+carte.hauteur.toString()+"        │");
            lignes.set(2,lignes.get(2) + "│         │");
            lignes.set(3,lignes.get(3) + "│         │");
            lignes.set(4,lignes.get(4) + "│    "+carte.couleur.toString()+"    │");
            lignes.set(5,lignes.get(5) + "│         │");
            lignes.set(6,lignes.get(6) + "│         │");
            lignes.set(7,lignes.get(7) + "│        "+carte.hauteur.toString()+"│");
            lignes.set(8,lignes.get(8) + "└─────────┘");
        }
        for (String s : lignes) System.out.println(s);
    }


    /*
    public static void main(String[] args) {
        Carte c1 = new Carte(Hauteur.A, Couleur.Pique);
        Carte c2 = new Carte(Hauteur.V, Couleur.Pique);
        Carte c3 = new Carte(Hauteur.R, Couleur.Carreau);
    
        List<Carte> cartes = new ArrayList<>();
        cartes.add(c1);
        cartes.add(c2);
        cartes.add(c3);
        Carte.printCards(cartes);
    }
    */

}
