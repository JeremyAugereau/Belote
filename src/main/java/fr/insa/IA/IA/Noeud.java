package fr.insa.IA.IA;

import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Game.Game;
import fr.insa.IA.Game.Round.Coup;
import fr.insa.IA.Player.Player;

public class Noeud {

    private List<Double> sumRegret;
    private List<Double> strategy;
    private List<Double> sumStrategy;
    private Game game;
    private InfoSet infoSet;
    private final int NUM_ACTION;

    

    public Noeud(Game g) {
        infoSet = new InfoSet();
        game = g;
        NUM_ACTION = game.getDeck().getDeckSize();
        sumRegret = new ArrayList<>();
        strategy = new ArrayList<>();
        sumStrategy = new ArrayList<>();
    }

    // public void undo(){
    // List<Carte> cartes = new ArrayList<>();
    // for(Coup c : game.getRounds().get(game.getRounds().size()-1).getPli()){
    // cartes.add()
    // }
    // }

    // public List<Double> getStrategy(double weight) {
    //     List<Carte> actions = game.getCurrentRound().getPlayableCard(game.getCurrentRound().getCurrentPlayer());
    //     int numActions = actions.size();
    //     int normalizationSum = 0;

    //     for (Carte c : actions) {
    //         int i = actions.indexOf(c);
    //         if (sumRegret.get(i) > 0) {
    //             strategy.set(i, sumRegret.get(i));
    //         } else {
    //             strategy.set(i, 0.0);
    //         }
    //         normalizationSum += strategy.get(i);
    //     }

    //     for (Carte c : actions) {
    //         int i = actions.indexOf(c);
    //         if (normalizationSum > 0) {
    //             strategy.set(i, strategy.get(i) / normalizationSum);
    //         } else {
    //             strategy.set(i, 1.0 / numActions);
    //         }
    //         sumStrategy.set(i, weight * strategy.get(i) + sumStrategy.get(i));
    //     }
    //     return strategy;
    // }

    // public List<Double> getAverageStrategy() {
    //     List<Double> avgStrategy = new ArrayList<>();
    //     int normalizationSum = 0;

    //     for (int i = 0; i < NUM_ACTION; i++) {
    //         normalizationSum += strategy.get(i);
    //     }
    //     for (int i = 0; i < NUM_ACTION; i++) {
    //         if (normalizationSum > 0) {
    //             avgStrategy.set(i, sumStrategy.get(i) / normalizationSum);
    //         } else {
    //             avgStrategy.set(i, 1.0 / NUM_ACTION);
    //         }
    //     }
    //     return avgStrategy;
    // }

    
    public List<Double> getSumRegret() {
        return sumRegret;
    }
    // @Override
    // public String toString() {
    //     return infoSet + " : " + getAverageStrategy();
    // }
}
