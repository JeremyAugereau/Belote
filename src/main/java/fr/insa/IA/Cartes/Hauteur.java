package fr.insa.IA.Cartes;

public enum Hauteur {
    AS,
    ROI,
    DAME,
    VALET;

    /**
     * @return retourne le caractère associé à la couleur pour plus d'esthetisme
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
