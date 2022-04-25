package fr.insa.IA.Cartes;

/**
 * Describe the rank of a card
 * 
 * @see Carte
 * @author AUGEREAU Jeremy
 * @author GRAC Guilhem
 */
public enum Hauteur {
    /**
     * Ace
     */
    AS,
    /**
     * King
     */
    ROI,
    /**
     * Queen
     */
    DAME,
    /**
     * Jack
     */
    VALET;

    /**
     * {@return Return the character or symbol associated to the rank of the card.}
     */
    @Override
    public String toString() {
        switch (this) {
            case AS:
                return "A";
            case ROI:
                return "K";
            case DAME:
                return "Q";
            case VALET:
                return "J";
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Each card as a value used to compute the score of a player, based on its rank.
     * @return (int) rank of the card in accordance with its rank
     */
    public int toScore() {
        switch (this) {
            case AS:
                return 11;
            case ROI:
                return 4;
            case DAME:
                return 3;
            case VALET:
                return 2;
            default:
                throw new IllegalArgumentException();
        }
    }
}
