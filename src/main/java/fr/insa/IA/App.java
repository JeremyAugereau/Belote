package fr.insa.IA;

import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Deck;
import fr.insa.IA.Game.Game;
import fr.insa.IA.Game.Round;
import fr.insa.IA.IA.CFR;
import fr.insa.IA.Player.Player;


public class App {
    

    public static void main( String[] args )
    {

        // Game game  = new Game(2);
        // game.proceedGame();

        CFR bot = new CFR();
        System.out.println(bot.training(1));

    }
}
