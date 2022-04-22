package fr.insa.IA.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Deck;
import fr.insa.IA.IA.CFR;
import fr.insa.IA.IA.InfoSet;
import fr.insa.IA.Player.Player;

public class Game implements Serializable{
    private List<Player> players;
    private Deck deck;
    private List<Round> rounds;
    private Round currentRound;
    private Player currentPlayer;

    public Game(int nbPlayer) {
        players = new ArrayList<>();
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

    public void proceedGame(CFR bot,int i) {
        while(!isOver()){
            addRound(new Round(players, currentPlayer, this));
            currentPlayer = getCurrentRound().roundProceed();
        }
        proccedScore();
        Player winner = players.get(0);
        Player looser = players.get(1);
        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }
        for (Player p : players) {
            if (p.getScore() < looser.getScore()) {
                looser = p;
            }
        }
        if(winner.getId()==i){
            System.out.println("Vous avec gagné avec "+winner.getScore()+" points contre "+looser.getScore());
        }else{
            System.out.println("Vous avec perdu avec "+looser.getScore()+" points contre "+winner.getScore());
        }
        
    }

    // public void undo(){
    // Round lastRound = rounds.remove(rounds.size()-1);
    // lastRound.getRoundWinner().setScore(lastRound.getRoundWinner().getScore()-lastRound.getRoundValue());
    // for(Round.Coup c:lastRound.getPli()){
    // Carte cartePlayed =c.getCarte();
    // Player player =c.getPlayer();
    // int estDeLaMain= c.getEstDeLaMain();

    // if(estDeLaMain==-1){
    // player.addHand(cartePlayed);
    // }else{
    // player.setSecret(estDeLaMain, player.getTable().get(estDeLaMain));
    // player.setTable(estDeLaMain, cartePlayed);
    // }

    // }
    // }
    private void proccedScore(){
        for(Round round : rounds){
            round.getRoundWinner().setScore(round.getRoundWinner().getScore()+round.getRoundValue());
        }
    }

    public void undo() {
        // System.out.println(rounds.size());
        Round lastRound = rounds.get(rounds.size() - 1);
        // System.out.println(lastRound.getPli().size());

        Round.Coup c;
        if (lastRound.getPli().size() == 2) {
            c = lastRound.getPli().remove(1);
        } else if (lastRound.getPli().size() == 1) {
            if (rounds.size() > 1) { // IL NE FAUT JAMAIS QUE rounds SOIT VIDE
                rounds.remove(lastRound);
            }
            c = lastRound.getPli().remove(0);
        } else if (lastRound.getPli().size() == 0) {
            rounds.remove(rounds.size() - 1);
            lastRound = rounds.get(rounds.size() - 1);
            c = lastRound.getPli().remove(1);
        } else {
            throw new IllegalArgumentException();
        }

        Carte cartePlayed = c.getCarte();
        Player player = c.getPlayer();
        int estDeLaMain = c.getEstDeLaMain();
        if (estDeLaMain == -1) {
            player.addHand(cartePlayed);
        } else {
            player.setSecret(estDeLaMain, player.getTable().get(estDeLaMain));
            player.setTable(estDeLaMain, cartePlayed);
        }
        currentPlayer = player;
    }

    public void next(Carte carte, Round round) {
        if (round.getPli().size() == 2) {
            // System.out.println("*********************************************************");
            Round nextRound = new Round(players, currentPlayer, this);
            addRound(nextRound);
            // System.out.println(currentPlayer);
            // Carte.printCards(carte);
            nextRound.actionProceed(currentPlayer, carte);
        } else if (round.getPli().size() == 1) {
            // System.out.println(currentPlayer);
            // Carte.printCards(carte);
            round.actionProceed(currentPlayer, carte);
        } else if (round.getPli().size() == 0) { // ne sert qu'au tout début
            //System.out.println("#####################################");

            // System.out.println("------------------------------------------");
            // Carte.printCards(currentPlayer.getKnownCards());
            // System.out.println("------------------------------------------");
            // System.out.println(currentPlayer);
            // Carte.printCards(carte);
            round.actionProceed(currentPlayer, carte);
        } else {
            throw new IllegalArgumentException();
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


    public double payoff(Player player) {
        double score =0;
        for(Round round : rounds){
            if(round.getRoundWinner().equals(player)){
                for(Round.Coup coup : round.getPli()){
                    score +=coup.getCarte().getHauteur().toScore();
                }   
            }else{
                for(Round.Coup coup : round.getPli()){
                    score -=coup.getCarte().getHauteur().toScore();
                } 
            }    
        }
        return score;
    }

    public InfoSet getGameInfoSet() {
        InfoSet infoset = new InfoSet();
        if (getCurrentRound().getPli().size() == 1) {
            infoset.setCurrentCarte(getCurrentRound().getPli().get(0).getCarte());
        } else {
            infoset.setCurrentCarte(null);
        }
        for (Player player : players) {
            if (currentPlayer.getId() != player.getId()) {
                infoset.setEnemyTable(player.getTable());
            }
        }
        infoset.setHand(currentPlayer.getHand());
        List<Carte> h = new ArrayList<>();
        for(Round r : rounds){
            for(Round.Coup c : r.getPli()){
                h.add(c.getCarte());
            }
        }
        
        infoset.setHistory(h);
        infoset.setTable(currentPlayer.getTable());
        return infoset;
    }

    

}
