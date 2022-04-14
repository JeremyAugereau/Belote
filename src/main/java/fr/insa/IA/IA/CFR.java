package fr.insa.IA.IA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Game.Game;
import fr.insa.IA.Game.Round;
import fr.insa.IA.Player.Player;

public class CFR {
    private HashMap<InfoSet, Noeud> hashMap;

    public CFR() {
        hashMap = new HashMap<>();
    }

    public double training(int n) {
        double value = 0.0;
        for (int i = 1; i <= n; i++) {
            System.out.print(".");
            Game game = new Game(2);
            value += cfr(game, 1, 1.0, 1.0);
        }
        return value/n;
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

    public void updateStrategySum(Noeud noeud,List<Double> strategy,double p){
        
        int n = noeud.getSumStrategy().size();
        List<Double> strategySum = noeud.getSumStrategy();
        for (int i = 0; i < n; i++) {
            double tmp= strategy.get(i)*p+strategySum.get(i);
            noeud.setSumStrategy(i, tmp);
        }

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
        List<Double> utils = new ArrayList<>(Collections.nCopies(numAction, 0.0));
        double node_util =0;
        List<Carte> actions = game.getCurrentRound().getPlayableCard(game.getCurrentRound().getCurrentPlayer());
        Round round = null;
        for(Carte carte : actions){
            round = game.next(carte, round,game.getCurrentRound().getCurrentPlayer());
            int a =actions.indexOf(carte);
            if(idPlayer==1){
                double res = utils.get(a)-cfr(game,2,pi*strategy.get(a),po);
                utils.set(a, res);
            }else{
                double res = utils.get(a)-cfr(game,1,pi,strategy.get(a)*po);
                utils.set(a, res);
            }
            game.undo();
            node_util += strategy.get(a)*utils.get(a);
        }
        for(Carte carte : actions){
            int a =actions.indexOf(carte);
            double regret= utils.get(a)-node_util;
            if(idPlayer ==1){
                noeud.setSumregret(a, noeud.getSumRegret().get(a)+ po* regret);
            }else{
                noeud.setSumregret(a, noeud.getSumRegret().get(a)+ pi* regret); 
            }
        }
        if(idPlayer ==1){
            updateStrategySum(noeud, strategy, pi);
        }else{
            updateStrategySum(noeud, strategy, po);
        }
        return node_util;
    }
}
