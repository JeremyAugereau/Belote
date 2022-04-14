package fr.insa.IA.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Couleur;
import fr.insa.IA.Player.Player;

/**
 * Un Round c'est un pli
 */
public class Round {

    public class Coup {
        private Player player;
        private Carte carte;
        private int estDeLaMain;

        public Coup (Player p, Carte c,int estDeLaMain) {
            player = p;
            carte = c;
            this.estDeLaMain = estDeLaMain;
        }
        public Carte getCarte() {
            return carte;
        }
        public Player getPlayer() {
            return player;
        }
        public int getEstDeLaMain(){
            return estDeLaMain;
        }
        public void setCarte(Carte carte) {
            this.carte = carte;
        }
        public void setEstDeLaMain(int estDeLaMain) {
            this.estDeLaMain = estDeLaMain;
        }
        public void setPlayer(Player player) {
            this.player = player;
        }
    }

    private Player firstPlayer;
    private Player currentPlayer;
    private List<Player> players;
    private List<Coup> pli;
    private Game game;

    public Round(List<Player> plist,Player player, Game game) {
        players = plist;
        this.game = game;
        firstPlayer = player;
        currentPlayer = firstPlayer;
        pli = new ArrayList<>();
    }

    public Player roundProceed(){
        Player roundWinner;
        for(int i = players.indexOf(firstPlayer); i<players.indexOf(firstPlayer)+players.size();i++){
            currentPlayer = players.get(i%players.size());
            actionProceed(currentPlayer);
        }
        roundWinner = getRoundWinner();
        roundWinner.setScore(roundWinner.getScore()+getRoundValue());
        game.addRound(this); 
        return roundWinner;
    }

    public void actionProceed(Player player){
        System.out.println("Pli en cours :");
        List<Carte> carteToPrint = new ArrayList<>();
        for(Coup coup : pli){
            carteToPrint.add(coup.carte);
        }
        Carte.printCards(carteToPrint);

        System.out.println("Tu peux jouer :");
        Carte.printCards(getPlayableCard(player));
        System.out.println("Entre le numero de ta carte : ");
        Scanner input = new Scanner(System.in);
        int nb = Integer.parseInt(input.nextLine());
        Carte cartePlayed = getPlayableCard(player).get(nb);
        if(player.getHand().contains(cartePlayed)){
            pli.add(new Coup(player, cartePlayed,-1));
        }else{
            pli.add(new Coup(player, cartePlayed,player.getTable().indexOf(cartePlayed)));
        }
        
        player.removeCarte(cartePlayed);
        game.addHistory(cartePlayed);
        
    }

    public void actionProceed(Player player,Carte carte){
        if(!(getPlayableCard(player).contains(carte))){
            throw new IllegalArgumentException();
        }
        if(player.getHand().contains(carte)){
            pli.add(new Coup(player, carte,-1));
        }else{
            pli.add(new Coup(player, carte,player.getTable().indexOf(carte)));
        }
        player.removeCarte(carte);
        game.addHistory(carte);

    }

    public List<Carte> getPlayableCard(Player player){
        List<Carte> playableCartes = new ArrayList<>();
        playableCartes.addAll(player.getHand());
        playableCartes.addAll(player.getTable());

        if(pli.isEmpty()){
            return playableCartes;
        }

        for(Coup c : pli){
            if (c.player == player) throw new IllegalArgumentException();
            Couleur mustCoul =  pli.get(0).carte.getCouleur();
            playableCartes.removeIf(crt -> crt.getCouleur() != mustCoul);
        }
        if(playableCartes.isEmpty()){
            playableCartes.addAll(player.getHand());
            playableCartes.addAll(player.getTable());
        }
        return playableCartes;

    }

    public Player getRoundWinner(){
        Couleur currentCouleur = pli.get(0).carte.getCouleur();
        Carte currentCarteWinner = pli.get(0).carte;
        Player roundWinner = pli.get(0).player;
        for(Coup coup : pli){
            if(coup.carte.getCouleur()==currentCouleur && coup.carte.compareTo(currentCarteWinner) > 0){
                currentCarteWinner = coup.carte;
                roundWinner = coup.player;
            }
        }
        return roundWinner;
    }

    public int getRoundValue(){
        int score = 0;
        for(Coup coup : pli){
            score += coup.carte.getHauteur().toScore();
        }
        return score;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public List<Coup> getPli() {
        return pli;
    }
    public void removeCoup(Coup c){
        pli.remove(c);
    }

}
