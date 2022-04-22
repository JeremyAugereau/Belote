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

    

    public Noeud(Game g,int numAction) {
        game = g;
        sumRegret = new ArrayList<>(Collections.nCopies(numAction, 0.0));
        strategy = new ArrayList<>(Collections.nCopies(numAction, 0.0));
        sumStrategy = new ArrayList<>(Collections.nCopies(numAction, 0.0));
    }

    
    @Override
    public String toString() {
        return sumStrategy.toString();
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
