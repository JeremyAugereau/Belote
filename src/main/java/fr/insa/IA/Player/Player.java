package fr.insa.IA.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Carte;

/**
 * A Player of our simplified Belote. A player can be a human or the AI.
 * 
 * @author AUGEREAU Jeremy
 * @author GRAC Guilhem
 */
public class Player implements Serializable {
    /**
     * Number of cards the player's hand can hold.
     */
    public final static int HAND_SIZE = 2;
    /**
     * Number of cards the player's table can hold.
     */
    public final static int TABLE_SIZE = 1;
    /**
     * Number of players in a game.
     * 
     * @see #id
     */
    private static int nbPlayer = 1;
    /**
     * List of {@link #HAND_SIZE} cards, composing the player's hand.
     * 
     * @see Carte
     */
    private List<Carte> hand;
    /**
     * List of {@link #TABLE_SIZE} cards, composing the player's table.
     * These cards can only be played by this player but can be seen by all the
     * players within the same game.
     * 
     * @see Carte
     */
    private List<Carte> table;
    /**
     * List of {@link #TABLE_SIZE} cards, composing the player's secret.
     * When a {@link #table} card is played, the correspond secret card (the one
     * with the same index) takes its place.
     * 
     * @see Carte
     */
    private List<Carte> secret;
    /**
     * Score of the player. The score is calculated with the value
     * ({@link Carte#getHauteur()}) of each card within a round this player has won.
     * It is computed at the end of the game.
     * 
     * @see Carte
     */
    private int score;
    /**
     * Id of the player. Starts at 1.
     * 
     * @see #nbPlayer
     */
    private int id;

    /**
     * Constructor of Player. Initialize {@link #hand}, {@link #table}, and
     * {@link #secret}, gives an id to the player, and increment {@link #nbPlayer}
     * by 1.
     */
    public Player() {
        score = 0;
        hand = new ArrayList<Carte>();
        table = new ArrayList<Carte>();
        secret = new ArrayList<Carte>();
        id = nbPlayer;
        nbPlayer++;
    }

    /**
     * Reset {@link #nbPlayer} to 1. Notably used when creating a new game.
     */
    public static void resetNbPlayer() {
        nbPlayer = 1;
    }

    /**
     * Getter of {@link #id}, the id of the player.
     * 
     * @return (int) the id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter of {@link #nbPlayer}, the number of players.
     * 
     * @return (int)
     */
    public static int getNbPlayer() {
        return nbPlayer;
    }

    /**
     * @return (List of cards) concatenation of his {@link #hand} and his
     *         {@link #table}.
     * @see Carte
     */
    public List<Carte> getKnownCards() {
        List<Carte> res = new ArrayList<>();
        for (Carte c : hand) {
            if (c != null)
                res.add(c);
        }
        for (Carte c : table) {
            if (c != null)
                res.add(c);
        }
        return res;
    }

    /**
     * Removes a card from the player's {@link #hand} or {@link #table}.
     * 
     * @param c Card to remove
     * @see Carte
     * @throws IllegalArgumentException if the player does not have this card
     */
    public void removeCarte(Carte c) {
        if (hand.contains(c)) {
            hand.remove(c);
        } else if (table.contains(c)) {
            int index = table.indexOf(c);
            table.set(index, secret.get(index));
            secret.set(index, null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Getter of the player's {@link #hand}.
     * 
     * @return List of cards, the player's hand
     * @see Carte
     */
    public List<Carte> getHand() {
        return hand;
    }

    @Override
    public String toString() {
        return "Player " + id;
    }

    /**
     * Add a cards to the player's {@link #hand}.
     * 
     * @param c ({@link Carte}) card to add
     * @see Carte
     */
    public void addHand(Carte c) {
        hand.add(c);
    }

    /**
     * Setter of the player's {@link #hand}.
     * 
     * @param i (int) index to where add the card in the hand.
     * @param c ({@link Carte}) card to add
     * @see Carte
     */
    public void setHand(int i, Carte c) {
        hand.set(i, c);
    }

    /**
     * Getter of {@link #table}.
     * 
     * @return List of cards, the player's table
     * @see Carte
     */
    public List<Carte> getTable() {
        return table;
    }

    /**
     * Add a cards to the player's {@link #table}.
     * 
     * @param c ({@link Carte}) card to add
     * @see Carte
     */
    public void addTable(Carte c) {
        table.add(c);
    }

    /**
     * Setter of the player's {@link #table}.
     * 
     * @param i (int) index to where add the card in the table.
     * @param c ({@link Carte}) card to add
     * @see Carte
     */
    public void setTable(int i, Carte c) {
        table.set(i, c);
    }

    /**
     * Getter of {@link #secret}.
     * 
     * @return List of cards, the player's secret
     * @see Carte
     */
    public List<Carte> getSecret() {
        return secret;
    }

    /**
     * Add a cards to the player's {@link #secret}.
     * 
     * @param c ({@link Carte}) card to add
     * @see Carte
     */
    public void addSecret(Carte c) {
        secret.add(c);
    }

    /**
     * Setter of the player's {@link #secret}.
     * 
     * @param i (int) index to where add the card in the secret.
     * @param c ({@link Carte}) card to add
     * @see Carte
     */
    public void setSecret(int i, Carte c) {
        secret.set(i, c);
    }

    /**
     * Score of the player. The score is calculated with the value
     * ({@link Carte#getHauteur()}) of each card within a round this player has won.
     * 
     * @return (int) {@link #score}
     */
    public int getScore() {
        return score;
    }

    /**
     * Setter of {@link #score}.
     * @param score (int) new score
     */
    public void setScore(int score) {
        this.score = score;
    }
}
