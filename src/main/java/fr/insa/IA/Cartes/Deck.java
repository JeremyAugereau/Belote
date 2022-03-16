package fr.insa.IA.Cartes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.insa.IA.Player.Player;

public class Deck {

    private List<Carte> deck;

    public Deck() {
        deck = new ArrayList<>();
        for (Couleur C : Couleur.values()) {
            for (Hauteur H : Hauteur.values()) {
                deck.add(new Carte(H, C));
            }
        }

        this.shuffleDeck();
    }

    /**
     * Mélange le deck
     */
    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    /**
     * @return le deck avec une ArrayList<Carte>
     */
    public List<Carte> getDeck() {
        return deck;
    }

    
    /** 
     * retourne la première carte du deck et la supprime du deck
     * @return une Carte
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
     * Distribue à chaque joueur le bon nombre de carte (défini dans les statics de la classe Player)
     * @param players la liste des joueurs
     */
    public void deal(List<Player> players) {
        for (Player p : players) {
            for (int i = 0; i < Player.HAND_SIZE; i++) {
                p.addHand(this.pop());
            }
            for (int i = 0; i < Player.TABLE_SIZE; i++) {
                p.addTable(this.pop());
            }
            for (int i = 0; i < Player.SECRET_SIZE; i++) {
                p.addSecret(this.pop());
            }
        }
    }

}
