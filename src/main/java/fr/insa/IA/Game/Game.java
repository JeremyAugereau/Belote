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
        currentRound = new Round(players, currentPlayer, this);
    }

    public void proceedGame() {
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

    // public void undo(){
    // Round lastRound = rounds.remove(rounds.size()-1);
    // lastRound.getRoundWinner().setScore(lastRound.getRoundWinner().getScore()-lastRound.getRoundValue());
    // for(Round.Coup c:lastRound.getPli()){
    // Carte cartePlayed =c.getCarte();
    // Player player =c.getPlayer();
    // int estDeLaMain= c.getEstDeLaMain();
    // history.remove(cartePlayed);

    // if(estDeLaMain==-1){
    // player.addHand(cartePlayed);
    // }else{
    // player.setSecret(estDeLaMain, player.getTable().get(estDeLaMain));
    // player.setTable(estDeLaMain, cartePlayed);
    // }

    // }
    // }

    public void undo() {
        System.out.println(rounds.size());
        Round lastRound = currentRound;
        // System.out.println(lastRound.getPli().size());
        if (lastRound.getPli().size() == 1) {
            Round.Coup c = lastRound.getPli().get(0);
            Carte cartePlayed = c.getCarte();
            Player player = c.getPlayer();
            int estDeLaMain = c.getEstDeLaMain();
            history.remove(cartePlayed);

            if (estDeLaMain == -1) {
                player.addHand(cartePlayed);
            } else {
                player.setSecret(estDeLaMain, player.getTable().get(estDeLaMain));
                player.setTable(estDeLaMain, cartePlayed);
            }
            currentRound = rounds.remove(rounds.size() - 1);
            currentPlayer = rounds.get(rounds.size() - 1).getRoundWinner();
        } else if (lastRound.getPli().size() == 0) {
            rounds.remove(rounds.size() - 1);
            lastRound = rounds.get(rounds.size() - 1); // throws IndexOutOfBoundException si on remonte trop loin
            Round.Coup c = lastRound.getPli().get(1);
            Carte cartePlayed = c.getCarte();
            Player player = c.getPlayer();
            int estDeLaMain = c.getEstDeLaMain();
            history.remove(cartePlayed);
            if (estDeLaMain == -1) {
                player.addHand(cartePlayed);
            } else {
                player.setSecret(estDeLaMain, player.getTable().get(estDeLaMain));
                player.setTable(estDeLaMain, cartePlayed);
            }
            lastRound.removeCoup(c);
            currentPlayer = lastRound.getPli().get(0).getPlayer();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Round next(Carte carte, Round round) {
        if (round == null) {
            System.out.println("*********************************************************");
            System.out.println(currentPlayer);
            Carte.printCards(carte);
            round = new Round(players, currentPlayer, this);
            addRound(round);
            round.actionProceed(currentPlayer, carte);
            history.add(carte);
            return round;
        } else {
            System.out.println(currentPlayer);
            Carte.printCards(carte);
            round.actionProceed(currentPlayer, carte);
            history.add(carte);
            return null;
        }

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
        if (rounds.size() < 1)
            return new Round(players, currentPlayer, this);
        return rounds.get(rounds.size() - 1);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
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

    public void addHistory(Carte c) {
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
        for (Player player : players) {
            if (currentPlayer.getId() != player.getId()) {
                infoset.setEnemyTable(player.getTable());
            }
        }
        infoset.setHand(currentPlayer.getHand());
        infoset.setHistory(history);
        infoset.setTable(currentPlayer.getTable());
        return infoset;
    }

}
