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
    private CFR bot;

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
        bot = new CFR();
       }

    public void proceedGame() {
        while(!isOver()){
            System.out.println("*********New Round***********");
            addRound(new Round(players, currentPlayer, this));
            getCurrentRound().roundProceed();
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
        if(winner.getId()==1){
            System.out.println("Vous avec gagné avec "+winner.getScore()+" points contre "+looser.getScore());
        }else{
            System.out.println("Vous avec perdu avec "+looser.getScore()+" points contre "+winner.getScore());
        }
        
    }

    private void proccedScore(){
        for(Round round : rounds){
            round.getRoundWinner().setScore(round.getRoundWinner().getScore()+round.getRoundValue());
        }
    }

    public void undo() {
        Round lastRound = rounds.get(rounds.size() - 1);

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
            Round nextRound = new Round(players, currentPlayer, this);
            addRound(nextRound);
            nextRound.actionProceed(currentPlayer, carte);
        } else if (round.getPli().size() == 1) {
            round.actionProceed(currentPlayer, carte);
        } else if (round.getPli().size() == 0) { // ne sert qu'au tout début
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

    public void setBot(CFR bot) {
        this.bot = bot;
    }
    public CFR getBot() {
        return bot;
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

    public void printGameState(){
        List<Carte> cartes;
        for(int i=0;i<players.size();i++){
            cartes = new ArrayList<>();
            String name = "(toi)";
            if(i==1)name = "(IA)";
            System.out.println("------------PLAYER "+players.get(i).getId()+name+"------------");
            System.out.println("Hand                      Table     Secret");
            for(int j=0;j<Player.HAND_SIZE;j++){
                if(players.get(i).getHand().size()>j){
                    cartes.add(players.get(i).getHand().get(j));
                }else{
                    cartes.add(null);
                }
            }
            for(int j=0;j<Player.TABLE_SIZE;j++){
                if(players.get(i).getTable().size()>j){
                    cartes.add(players.get(i).getTable().get(j));
                }else{
                    cartes.add(null);
                }
            }
            for(int j=0;j<Player.TABLE_SIZE;j++){
                if(players.get(i).getSecret().size()>j){
                    cartes.add(players.get(i).getSecret().get(j));
                }else{
                    cartes.add(null);
                }
            }
            if(players.get(i).getId()==1){
                Carte.printCards(cartes);
            }else{
                Carte.printEnemyCards(cartes);
            }
        }
        

    }

    

}
