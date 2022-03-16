package fr.insa.IA.Cartes;

public enum Couleur {
    Carreau,
    Pique;

    @Override
    public String toString() {
        switch (this) {
            case Carreau:
                return "\u2666";
            case Pique:
                return "\u2660";
            default:
                throw new IllegalArgumentException();
        }
    }
}
