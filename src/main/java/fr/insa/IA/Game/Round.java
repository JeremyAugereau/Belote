package fr.insa.IA.Game;

import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Couleur;
import fr.insa.IA.Cartes.Hauteur;
import fr.insa.IA.Player.Player;

/**
 * Un Round c'est un pli
 */
public class Round {

    private class Coup {
        Player player;
        Carte carte;

        public Coup (Player p, Carte c) {
            player = p;
            carte = c;
        }
    }

    private Player currentPlayer;
    private List<Player> players;
    private List<Coup> pli;

    public Round(List<Player> plist,Player player) {
        players = plist;
        currentPlayer = player;
        pli = new ArrayList<>();
    }

    public Player roundProceed(){
        Player roundWinner;
        for(int i = players.indexOf(currentPlayer); i<players.indexOf(currentPlayer)+players.size();i++){
            actionProceed(players.get(i%players.size()));
        }
        roundWinner = getRoundWinner();
        roundWinner.setScore(roundWinner.getScore()+getRoundValue());
        return roundWinner;
    }

    public void actionProceed(Player player){
        
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
            score += coup.carte.getHauteur().ordinal();
        }
        return score;
    }
}
