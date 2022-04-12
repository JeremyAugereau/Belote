package fr.insa.IA.Game;

import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Deck;
import fr.insa.IA.Player.Player;

public class Game {
    private List<Player> players;
    private Deck deck;
    private List<Round> rounds;
    private Round currentRound;

    public Game(int nbPlayer){
        players = new ArrayList<>();
        for (int i = 0 ; i< nbPlayer ; i++) {
            players.add(new Player());
        }
        rounds = new ArrayList<>();
        deck = new Deck();
        deck.deal(players);
    }

    public void proceedGame(){
        Player currentPlayer = players.get(0);
        while(players.get(0).nbCartes() !=0) {
            currentRound = new Round(players, currentPlayer);
            currentPlayer = currentRound.roundProceed(this);
        }
        Player winner = players.get(0);
        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }
        System.out.println("Player " + winner.getId() + " a gagné !!!!!!");
    }

    public void addRound(Round round){
        rounds.add(round);
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public Deck getDeck() {
        return deck;
    }
}
