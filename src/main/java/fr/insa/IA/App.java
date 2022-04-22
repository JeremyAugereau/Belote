package fr.insa.IA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Couleur;
import fr.insa.IA.Cartes.Deck;
import fr.insa.IA.Cartes.Hauteur;
import fr.insa.IA.Game.Game;
import fr.insa.IA.Game.Round;
import fr.insa.IA.IA.CFR;
import fr.insa.IA.IA.InfoSet;
import fr.insa.IA.IA.Noeud;
import fr.insa.IA.Player.Player;


public class App {
    

    public static void main( String[] args )
    {

        // Game game  = new Game(2);
        // game.proceedGame();
        // Map<InfoSet,Noeud> map = new HashMap<>();

        // InfoSet i1 = new InfoSet();
        // InfoSet i2 = new InfoSet();

        // Game g = new Game(2);

        // Noeud n1 = new Noeud(g);
        // System.out.println(n1);
        // Noeud n2 = new Noeud(g);
        // System.out.println(n2);


        // map.put(i1,n1);
        // map.put(i2,n2);

        // System.out.println(map.get(i1));
        // System.out.println(i1.equals(i2));
        // System.out.println(i1.hashCode());
        // System.out.println(i2.hashCode());



        CFR bot = new CFR();
        System.out.println(bot.training(10000));

        
        


    }
}
