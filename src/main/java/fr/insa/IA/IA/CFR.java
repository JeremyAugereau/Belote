package fr.insa.IA.IA;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Game.Game;
import fr.insa.IA.Game.Round;
import fr.insa.IA.Player.Player;

public class CFR implements Serializable{
    private HashMap<InfoSet, Noeud> hashMap;

    public CFR() {
        hashMap = new HashMap<>();
    }

    public double training(int n) {
        double value = 0.0;
        int pourcent=0;
        for (int i = 1; i <= n; i++) {
            
            Player.resetNbPlayer();
            Game game = new Game(2);


            if(i*100/n > pourcent){
                System.out.println(pourcent+1+"%");
                pourcent = i*100/n;
            }
            value += cfr(game, 1, 1.0, 1.0);
        }
       
        System.out.println("Taille de la Map : "+hashMap.size());
        sauvegarde(String.valueOf(n));
        System.out.println("HashMap sauvegardée");
        return value / n;
    }

    private void sauvegarde(String name) {
        try {
            FileOutputStream file= new FileOutputStream("modele_serialized_"+name+".ser");
  
            ObjectOutputStream output= new ObjectOutputStream(file);
  
            output.writeObject(hashMap);
            output.close();
            file.close();
          } catch (IOException e) {
            e.printStackTrace();
          }

    }

    public void importMap(String path) throws IOException{
        try {
            System.out.println("Chargement de la HashMap ...");
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream input= new ObjectInputStream(file);
  
            hashMap = (HashMap)input.readObject();
  
            input.close();
            file.close();
        }
  
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
  
        System.out.println("HashMap chargée");
    }

    // non utilisée mais peut etre pratique pour observer ce qu'il y a dans la map
    private void sauvegardeEnClair(String name) {
        String outputFilePath= "modele_"+name+".txt";
        File file = new File(outputFilePath);
        BufferedWriter bf = null;
  
        try {
            bf = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<InfoSet, Noeud> entry :hashMap.entrySet()) {
                bf.write(entry.getKey() + ":"+ entry.getValue());
                bf.newLine();
            }
            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                bf.close();
            }
            catch (Exception e) {
            }
        }
    
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

    public void updateStrategySum(Noeud noeud, List<Double> strategy, double p) {

        int n = noeud.getSumStrategy().size();
        List<Double> strategySum = noeud.getSumStrategy();
        for (int i = 0; i < n; i++) {
            double tmp = strategy.get(i) * p + strategySum.get(i);
            noeud.setSumStrategy(i, tmp);
        }

    }

    public double cfr(Game game, int idPlayer, double pi, double po) {

        if (game.isOver()){
            return game.payoff(game.getPlayerById(idPlayer));
        }
            
        if (game.getRounds().size() == 0) { // IL NE FAUT JAMAIS QUE rounds SOIT VIDE
            game.addRound(new Round(game.getPlayers(), game.getCurrentPlayer(), game));
        }
        Round round = game.getCurrentRound();
        List<Carte> actions = round.getPlayableCard(game.getPlayerById(idPlayer));
        int numAction = actions.size();
        Noeud noeud;
        if (hashMap.containsKey(game.getGameInfoSet())) {
            noeud = hashMap.get(game.getGameInfoSet());
        } else {
            noeud = new Noeud(numAction);
            hashMap.put(game.getGameInfoSet(), noeud);
        }
        List<Double> strategy = getStrategy(noeud.getSumRegret());
        List<Double> utils = new ArrayList<>(Collections.nCopies(numAction, 0.0));
        double node_util = 0.0;

        for (Carte carte : actions) {

            int a = actions.indexOf(carte);
            game.next(carte, round);
            double res;
            int nextId = game.getCurrentPlayer().getId(); // deja update ds next
            if (idPlayer == nextId) {
                if (idPlayer == 1) {
                    res = utils.get(a) + cfr(game, nextId, pi * strategy.get(a), po);
                } else {
                    res = utils.get(a) + cfr(game, nextId, pi, strategy.get(a) * po);
                }
            } else {
                if (idPlayer == 1) {
                    res = utils.get(a) - cfr(game, nextId, pi * strategy.get(a), po);
                } else {
                    res = utils.get(a) - cfr(game, nextId, pi, strategy.get(a) * po);
                }
            }
            utils.set(a, res);

            game.undo();
            node_util += strategy.get(a) * utils.get(a);
        }
        for (Carte carte : actions) {
            int a = actions.indexOf(carte);
            double regret = utils.get(a) - node_util;
            if (idPlayer == 1) {
                noeud.setSumregret(a, noeud.getSumRegret().get(a) + po * regret);
            } else {
                noeud.setSumregret(a, noeud.getSumRegret().get(a) + pi * regret);
            }
        }
        if (idPlayer == 1) {
            updateStrategySum(noeud, strategy, pi);
        } else {
            updateStrategySum(noeud, strategy, po);
        }
        return node_util;
    }

    public Carte chooseAction(Game game) {
        InfoSet info = game.getGameInfoSet();
        if(hashMap.containsKey(info)){
            double sum = 0.0;
            List<Double> list =hashMap.get(info).getSumStrategy();
            for(Double d : list){
                sum += d;
            }
            double random = Math.random() * sum;
            double sommeCumul =0.0;
            for(int i =0;i<list.size();i++){
                sommeCumul += list.get(i);
                if(random<=sommeCumul) return game.getCurrentRound().getPlayableCard(game.getCurrentPlayer()).get(i);
            }
            return game.getCurrentRound().getPlayableCard(game.getCurrentPlayer()).get(list.size()-1);                           //ça devrait jamais arriver mais au cas ou;
        }else{
            int random = (int) Math.random() * game.getCurrentRound().getPlayableCard(game.getCurrentPlayer()).size(); 
            return game.getCurrentRound().getPlayableCard(game.getCurrentPlayer()).get(random);
        }
    }
}
