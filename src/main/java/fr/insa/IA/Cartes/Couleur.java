package fr.insa.IA.Cartes;

public enum Couleur {
    CARREAU,
    PIQUE;

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
