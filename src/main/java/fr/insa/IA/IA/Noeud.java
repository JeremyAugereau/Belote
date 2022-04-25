package fr.insa.IA.IA;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Node for our simplified Belote.
 * 
 * @see CFR
 * @author AUGEREAU Jeremy
 * @author GRAC Guilhem
 */
public class Noeud implements Serializable{

    private List<Double> sumRegret;
    private List<Double> strategy;
    private List<Double> sumStrategy;

    public Noeud(int numAction) {
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
