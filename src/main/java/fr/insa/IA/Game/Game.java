package fr.insa.IA.Game;

import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Deck;
import fr.insa.IA.IA.InfoSet;
import fr.insa.IA.Player.Player;

public class Game {
    private List<Player> players;
    private Deck deck;
    private List<Round> rounds;
    private Round currentRound;
    private Player currentPlayer;
    private List<Carte> history; 

    public Game(int nbPlayer) {
        players = new ArrayList<>();
        history = new ArrayList<>();
        for (int i = 0; i < nbPlayer; i++) {
            players.add(new Player());
        }
        rounds = new ArrayList<>();
        deck = new Deck();
        deck.deal(players);
        currentPlayer = players.get(0);
    }

    public void proceedGame() {
        currentPlayer = players.get(0);
        while (!isOver()) {
            currentRound = new Round(players, currentPlayer, this);
            currentPlayer = currentRound.roundProceed();
        }
        Player winner = players.get(0);
        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }
        System.out.println("Player " + winner.getId() + " a gagné !!!!!!");
    }

    public void undo(){
        Round lastRound = rounds.remove(rounds.size()-1);
        lastRound.getRoundWinner().setScore(lastRound.getRoundWinner().getScore()-lastRound.getRoundValue());
        for(Round.Coup c:lastRound.getPli()){
            Carte cartePlayed =c.getCarte();
            Player player =c.getPlayer();
            int estDeLaMain= c.getEstDeLaMain();
            history.remove(cartePlayed);

            if(estDeLaMain==-1){
                player.addHand(cartePlayed);
            }else{
                player.setSecret(estDeLaMain, player.getTable().get(estDeLaMain));
                player.setTable(estDeLaMain, cartePlayed);
            }
            
        }
    }

    public void next(Carte carte){
        Round round = new Round(players,currentPlayer,this);
        history.add(carte);

    }

    public boolean isOver() {
        boolean res = true;
        for (Player player : players) {
            if (player.nbCartes() != 0) {
                res = false;
            }
        }
        return res;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addRound(Round round) {
        rounds.add(round);
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public Deck getDeck() {
        return deck;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        throw new IllegalArgumentException();
    }

    public void addHistory(Carte c){
        history.add(c);
    }

    public int payoff(Player player) {
        for (Player p : getPlayers()) {
            if (player.getId() != p.getId()) {
                return player.getScore() - p.getScore();
            }
        }
        throw new IllegalArgumentException();
    }

    public InfoSet getGameInfoSet() {
        InfoSet infoset = new InfoSet();
        if (!currentRound.getPli().isEmpty()) {
            infoset.setCurrentCarte(currentRound.getPli().get(0).getCarte());
        } else {
            infoset.setCurrentCarte(null);
        }
        for(Player player: players){
            if(currentPlayer.getId()!=player.getId()){
                infoset.setEnemyTable(player.getTable());
            }
        }
        infoset.setHand(currentPlayer.getHand());
        infoset.setHistory(history);
        infoset.setTable(currentPlayer.getTable());
        return infoset;
    }
}
