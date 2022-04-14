package fr.insa.IA.IA;

import java.util.ArrayList;
import java.util.Collections;
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
        sumRegret = new ArrayList<>(Collections.nCopies(NUM_ACTION, 0.0));
        strategy = new ArrayList<>(Collections.nCopies(NUM_ACTION, 0.0));
        sumStrategy = new ArrayList<>(Collections.nCopies(NUM_ACTION, 0.0));
    }

    
    
    public List<Double> getSumRegret() {
        return sumRegret;
    }

    public void setSumregret(int i,double d){
        sumRegret.set(i,d);
    }
    public List<Double> getSumStrategy() {
        return sumStrategy;
    }
    public void setSumStrategy(int i,double d){
        sumStrategy.set(i,d);
    }
    public List<Double> getStrategy() {
        return strategy;
    }

}
