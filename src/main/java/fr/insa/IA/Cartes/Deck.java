package fr.insa.IA.Cartes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.insa.IA.Player.Player;

/**
 * A Deck of our simplified Belote. At it uses a 8-cards deck : 2 colors, from Jack to As.
 * 
 * @author AUGEREAU Jeremy
 * @author GRAC Guilhem
 */
public class Deck implements Serializable {

    /**
     * List of cards compsing the deck.
     * 
     * @see Carte
     */
    private List<Carte> deck;
    /**
     * Original size of the deck (before deal).
     */
    private int deckSize;

    /**
     * Constructor of Deck. Fill {@link #deck} up with all the cards, and shuffles
     * it.
     * 
     * @see Carte
     */
    public Deck() {
        deckSize = 0;
        deck = new ArrayList<>();
        for (Couleur C : Couleur.values()) {
            for (Hauteur H : Hauteur.values()) {
                deck.add(new Carte(H, C));
                deckSize++;
            }
        }
        this.shuffleDeck();
    }

    /**
     * Shuffles the deck.
     */
    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    /**
     * Getter of the {@link #deck}.
     * 
     * @return List of cards composing the deck
     * @see Carte
     */
    public List<Carte> getDeck() {
        return deck;
    }

    /**
     * Removes the first card of the deck.
     * 
     * @return the card removed
     * @see Carte
     * @throws IndexOutOfBoundsException if the deck is empty
     */
    public Carte pop() {
        if (deck.isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        Carte c = deck.get(0);
        deck.remove(0);
        return c;
    }

    /**
     * Deals the cards of the deck among players.
     * 
     * @param players (List of {@link Player}) the players to receive the cards
     * @see #pop()
     * @see Player
     */
    public void deal(List<Player> players) {
        for (Player p : players) {
            for (int i = 0; i < Player.HAND_SIZE; i++) {
                p.addHand(this.pop());
            }
            for (int i = 0; i < Player.TABLE_SIZE; i++) {
                p.addTable(this.pop());
            }
            for (int i = 0; i < Player.TABLE_SIZE; i++) {
                p.addSecret(this.pop());
            }
        }
    }

    /**
     * Getter of {@link #deckSize}, the original size of the deck (before deal).
     * 
     * @return (int)
     */
    public int getDeckSize() {
        return deckSize;
    }

}
