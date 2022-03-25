package fr.insa.IA.Game;

import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Deck;
import fr.insa.IA.Player.Player;

public class Game {
    private List<Player> players;
    private Deck deck;

    public Game(){
        players = new ArrayList<>();
        deck = new Deck();
        proceedGame();
    }

    public void proceedGame(){
        while()
    }

    public void addPlayer(Player player){
        players.add(player);
    }




}
