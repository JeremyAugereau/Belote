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

    public int getId() {
        return id;
    }

    public int getNbPlayer() {
        return nbPlayer;
    }

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
     * @return List<Carte> return the hand
     */
    public List<Carte> getHand() {
        return hand;
    }

    /**
     * @param hand the hand to set
     */
    public void addHand (Carte c) {
        hand.add(c);
    }

    /**
     * @return List<Carte> return the table
     */
    public List<Carte> getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void addTable (Carte c) {
        table.add(c);
    }

    /**
     * @return List<Carte> return the secret
     */
    public List<Carte> getSecret() {
        return secret;
    }

    /**
     * @param secret the secret to set
     */
    public void addSecret (Carte c) {
        secret.add(c);
    }

    /**
     * @return int return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }
}
