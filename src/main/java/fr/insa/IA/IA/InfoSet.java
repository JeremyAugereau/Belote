package fr.insa.IA.IA;

import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Carte;

public class InfoSet {
    private List<Carte> history;
    private List<Carte> hand;
    private List<Carte> table;
    private List<Carte> enemyTable;
    private Carte currentCarte;

    public InfoSet() {
        history = new ArrayList<>();
        hand = new ArrayList<>();
        table = new ArrayList<>();
        enemyTable = new ArrayList<>();
        currentCarte = null;
    }

    public InfoSet(List<Carte> table, List<Carte> enemyTable, List<Carte> history, List<Carte> hand,
            Carte currentCarte) {
        this.history = history;
        this.hand = hand;
        this.table = table;
        this.enemyTable = enemyTable;
        this.currentCarte = currentCarte;
    }
    @Override
    public String toString() {
        String res="hand:(";
        for(Carte carte : hand){
            res += carte.getHauteur().toString()+carte.getCouleur().toString()+",";
        }
        res+="),table";
        for(Carte carte : table){
            res += carte.getHauteur().toString()+carte.getCouleur().toString()+",";
        }
        res+="),enemyT";
        for(Carte carte : enemyTable){
            res += carte.getHauteur().toString()+carte.getCouleur().toString()+",";
        }
        res+="),currentC:"+currentCarte.getHauteur().toString()+currentCarte.getCouleur().toString()+",hist(";
        for(Carte carte : history){
            res += carte.getHauteur().toString()+carte.getCouleur().toString()+",";
        }
        return res;
    }

    public List<Carte> getHand() {
        return hand;
    }

    public List<Carte> getEnemyTable() {
        return enemyTable;
    }

    public List<Carte> getHistory() {
        return history;
    }

    public List<Carte> getTable() {
        return table;
    }

    public Carte getCurrentCarte() {
        return currentCarte;
    }

    public void setCurrentCarte(Carte currentCarte) {
        this.currentCarte = currentCarte;
    }

    public void setEnemyTable(List<Carte> enemyTable) {
        this.enemyTable = enemyTable;
    }

    

    public void setHand(List<Carte> hand) {
        this.hand = hand;
    }

    public void setHistory(List<Carte> history) {
        this.history = history;
    }

    public void setTable(List<Carte> table) {
        this.table = table;
    }

}
