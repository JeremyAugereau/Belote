package fr.insa.IA.Game;

import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Deck;
import fr.insa.IA.Game.Round.Coup;
import fr.insa.IA.Player.Player;

public class Game {
    private List<Player> players;
    private Deck deck;
    private List<List<Coup>> plis;

    public Game(int nbPlayer){
        players = new ArrayList<>();
        for (int i = 0 ; i< nbPlayer ; i++) {
            players.add(new Player());
        }
        plis = new ArrayList<>();
        deck = new Deck();
        deck.deal(players);
    }

    public void proceedGame(){
        Player currentPlayer = players.get(0);
        while(players.get(0).nbCartes() !=0) {
            Round round = new Round(players, currentPlayer);
            currentPlayer = round.roundProceed(this);
        }
        Player winner = players.get(0);
        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }
        System.out.println("Player " + winner.getId() + " a gagné !!!!!!");
    }

    public void addPli(List<Coup> pli){
        plis.add(pli);
    }
}
