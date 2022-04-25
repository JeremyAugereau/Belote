package fr.insa.IA.Cartes;

/**
 * Describe the suit of a card
 * 
 * @see Carte
 * @author AUGEREAU Jeremy
 * @author GRAC Guilhem
 */
public enum Couleur {
    /**
     * Diamond
     */
    CARREAU,
    /**
     * Spade
     */
    PIQUE;

    /**
     * {@return Return the character or symbol associated to the suit of the card.}
     */
    @Override
    public String toString() {
        switch (this) {
            case CARREAU:
                return "\u2666";
            case PIQUE:
                return "\u2660";
            default:
                throw new IllegalArgumentException();
        }
    }
}
