package fr.insa.IA.Player;

import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Carte;

public class Player {
    public final static int HAND_SIZE = 2;
    public final static int TABLE_SIZE = 1;
    public final static int SECRET_SIZE = 1;
    private static int nbPlayer = 1;
    private List<Carte> hand;
    private List<Carte> table;
    private List<Carte> secret;
    private int score;
    private int id;

    public Player() {
        score = 0;
        hand = new ArrayList<Carte>();
        table = new ArrayList<Carte>();
        secret = new ArrayList<Carte>();
        id = nbPlayer;
        nbPlayer++;
    }

    
    /** 
     * Retourne l'id du joueur
     * @return int
     */
    public int getId() {
        return id;
    }

    
    /** 
     * retourne le nombre de joueur
     * @return int
     */
    public int getNbPlayer() {
        return nbPlayer;
    }

    
    /** 
     * Supprime une carte du jeu d'un joueur et la retourne
     * @param c Carte à supprimer
     * @return Carte supprimée
     */
    public Carte removeCarte(Carte c) {
        if (hand.contains(c)) {
            hand.remove(c);
        } else if (table.contains(c)) {
            int index = table.indexOf(c);
            table.set(index, secret.get(index));
            secret.set(index, null);
        } else {
            throw new IllegalArgumentException();
        }
        return c;
    }

    /**
     * Retourne les cartes dans la main du joueur
     * @return List<Carte> return the hand
     */
    public List<Carte> getHand() {
        return hand;
    }

    /**
     * Ajoute une carte à la main du joueur
     * @param hand the hand to set
     */
    public void addHand (Carte c) {
        hand.add(c);
    }

    /**
     * Retourne les cartes face visible du joueur
     * @return List<Carte> return the table
     */
    public List<Carte> getTable() {
        return table;
    }

    /**
     * Ajoute une carte face visible au joueur
     * @param table the table to set
     */
    public void addTable (Carte c) {
        table.add(c);
    }

    /**
     * Retourne les cartes face cachée du joueur
     * @return List<Carte> return the secret
     */
    public List<Carte> getSecret() {
        return secret;
    }

    /**
     * Ajoute une carte face cachée au joueur
     * @param secret the secret to set
     */
    public void addSecret (Carte c) {
        secret.add(c);
    }

    /**
     * @return int retourne le score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score set le score du joueur
     */
    public void setScore(int score) {
        this.score = score;
    }
}
