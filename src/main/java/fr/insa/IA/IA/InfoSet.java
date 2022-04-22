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
        if(currentCarte != null){
            res+="),currentC:"+currentCarte.getHauteur().toString()+currentCarte.getCouleur().toString()+",hist(";
        }
        res+="),currentC:() ,hist(";
        for(Carte carte : history){
            res += carte.getHauteur().toString()+carte.getCouleur().toString()+",";
        }
        return res;
    }

    @Override
    public int hashCode() {
        int hash = 37;
        for(Carte c : history){
            hash += c.hashCode();
        }
        hash = hash *11;
        for(Carte c : hand){
            hash += c.hashCode();
        }
        hash = hash *17;
        for(Carte c : table){
            if(c!=null){
                hash += c.hashCode();
            }
        }
        hash = hash *19;
        for(Carte c : enemyTable){
            if(c!=null){
                hash += c.hashCode();
            }
        }
        hash = hash *23;
        if(currentCarte != null)
            hash += currentCarte.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if((obj == null || getClass() != obj.getClass()))return false;
        InfoSet o =(InfoSet) obj;
        if(this.history.size() != o.history.size() || this.table.size() != o.table.size() || this.hand.size() != o.hand.size() || this.enemyTable.size() != o.enemyTable.size()) return false;
        for(Carte c : history){
            if(!o.history.contains(c)) return false;
        }
        for(Carte c : table){
            if(!o.table.contains(c)) return false;
        }
        for(Carte c : enemyTable){
            if(!o.enemyTable.contains(c)) return false;
        }
        for(Carte c : hand){
            if(!o.hand.contains(c)) return false;
        }
        if(currentCarte == null && o.currentCarte == null) return true;
        if(currentCarte == null || o.currentCarte == null || !currentCarte.equals(o.currentCarte))return false;
        return true;
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
