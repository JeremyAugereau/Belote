package fr.insa.IA.Game;

import java.util.List;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Player.Player;

/**
 * Un Round c'est un pli
 */
public class Round {

    private class Coup {
        Player player;
        Carte carte;

        public Coup (Player p, Carte c) {
            player = p;
            carte = c;
        }
    }

    private List<Coup> pli;

    public Round() {
        
    }
}
