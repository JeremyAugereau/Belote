package fr.insa.IA;

import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Deck;
import fr.insa.IA.Game.Game;
import fr.insa.IA.Game.Round;
import fr.insa.IA.Player.Player;


public class App {
    

    public static void main( String[] args )
    {
        // Player p1 = new Player(); 
        // Player p2 = new Player();
        
        // List<Player> players = new ArrayList<>();
        // players.add(p1);
        // players.add(p2);

        // Deck deck = new Deck();
        // deck.deal(players);

        // System.out.println("P1");
        // Carte.printCards(p1.getHand());
        // Carte.printCards(p1.getTable());
        // Carte.printCards(p1.getSecret());

        // System.out.println("P2");
        // Carte.printCards(p2.getHand());
        // Carte.printCards(p2.getTable());
        // Carte.printCards(p2.getSecret());

        // Game game = new Game();
        // Round r1 = new Round(players, p1);
        // r1.roundProceed(game);

        // System.out.println("P1 score : " + p1.getScore());
        // System.out.println("P2 score : " + p2.getScore());

        Game game  = new Game(2);
        game.proceedGame();

    }
}
