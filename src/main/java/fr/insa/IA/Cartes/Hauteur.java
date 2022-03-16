package fr.insa.IA.Cartes;

public enum Hauteur {
    AS,
    ROI,
    DAME,
    VALET;

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
}
