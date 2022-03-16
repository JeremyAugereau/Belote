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
                return "R";
            case DAME:
                return "D";
            case VALET:
                return "V";
            default:
                throw new IllegalArgumentException();
        }
    }
}
