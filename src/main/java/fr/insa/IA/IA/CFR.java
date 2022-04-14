package fr.insa.IA.IA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Game.Game;
import fr.insa.IA.Player.Player;

public class CFR {
    private HashMap<InfoSet, Noeud> hashMap;

    public CFR() {
        hashMap = new HashMap<>();
    }

    public List<Double> training(int n) {
        double value = 0.0;
        for (int i = 1; i <= n; i++) {
            Game game = new Game(2);
            value += cfr(game, 0, 1.0, 1.0);
        }
        value /= n;
    }

    public List<Double> getStrategy(List<Double> array) {
        int n = array.size();
        List<Double> strategy = new ArrayList<>(Collections.nCopies(n, 0.0));
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += Math.max(array.get(i), 0.0);
        }
        for (int i = 0; i < n; i++) {
            if (array.get(i) > 0.0) {
                strategy.set(i, array.get(i));
            } else {
                strategy.set(i, 0.0);
            }
            if (sum == 0.0) {
                strategy.set(i, (double) 1 / n);
            } else {
                strategy.set(i, strategy.get(i) / sum);
            }
        }
        return strategy;
    }

    public double cfr(Game game,int idPlayer, double pi,double po){
        if(game.isOver())return game.payoff(game.getPlayerById(idPlayer));
        int numAction = game.getCurrentRound().getPlayableCard(game.getPlayerById(idPlayer)).size();
        Noeud noeud;
        if(hashMap.containsKey(game.getGameInfoSet())){
            noeud = hashMap.get(game.getGameInfoSet());
        }else{
            noeud = new Noeud(game);
            hashMap.put(game.getGameInfoSet(),noeud);
        }
        List<Double> strategy = getStrategy(noeud.getSumRegret());
        List<Double> utils = new ArrayList<>();
        double node_util =0;
        List<Carte> actions = game.getCurrentRound().getPlayableCard(game.getCurrentRound().getCurrentPlayer());
        int numActions = actions.size();
        int normalizationSum = 0;
        for(Carte carte : actions){
            
        }
    }
}
