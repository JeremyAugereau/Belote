package fr.insa.IA.IA;

import java.lang.ProcessHandle.Info;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Couleur;
import fr.insa.IA.Cartes.Hauteur;
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
            // System.out.print(".......................................................................");
            Player.resetNbPlayer();
            Game game = new Game(2);

            // game.getPlayerById(1).setHand(0, new Carte(Hauteur.VALET, Couleur.PIQUE));
            // game.getPlayerById(1).setHand(1, new Carte(Hauteur.DAME, Couleur.CARREAU));
            // game.getPlayerById(1).setTable(0, new Carte(Hauteur.AS, Couleur.PIQUE));
            // game.getPlayerById(1).setSecret(0, new Carte(Hauteur.ROI, Couleur.PIQUE));

            // game.getPlayerById(2).setHand(0, new Carte(Hauteur.AS, Couleur.CARREAU));
            // game.getPlayerById(2).setHand(1, new Carte(Hauteur.ROI, Couleur.CARREAU));
            // game.getPlayerById(2).setTable(0, new Carte(Hauteur.DAME, Couleur.PIQUE));
            // game.getPlayerById(2).setSecret(0, new Carte(Hauteur.VALET, Couleur.CARREAU));

            // for (Player p : game.getPlayers()) {
            //     System.out.println(p);
            //     Carte.printCards(p.getHand());
            //     Carte.printCards(p.getTable());
            //     Carte.printCards(p.getSecret());
            // }
            value += cfr(game, 1, 1.0, 1.0);
        }
        
        for(Map.Entry<InfoSet, Noeud> entry : hashMap.entrySet()){
            if(entry.getKey().getHistory().size()==0){
                System.out.println(entry.getKey().toString() + entry.getValue().toString());
            }
        }
        System.out.println("Taille de la Map : "+hashMap.size());
        return value / n;
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
            // System.out.println(game.payoff(game.getPlayerById(idPlayer)));
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
            noeud = new Noeud(game);
            hashMap.put(game.getGameInfoSet(), noeud);
        }
        List<Double> strategy = getStrategy(noeud.getSumRegret());
        List<Double> utils = new ArrayList<>(Collections.nCopies(numAction, 0.0));
        double node_util = 0.0;
        // System.out.println("------------------------------------------");
        // Carte.printCards(actions);
        // System.out.println("------------------------------------------");

        for (Carte carte : actions) {

            int a = actions.indexOf(carte);
            game.next(carte, round);
            // System.out.println("------------------------------------------");
            // System.out.println(a);
            // System.out.println(actions.contains(carte));
            // System.out.println("------------------------------------------");
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

            // if (idPlayer == 1) {
            // double res = utils.get(a) - cfr(game, 2, pi * strategy.get(a), po, cpRound);
            // utils.set(a, res);
            // } else {
            // double res = utils.get(a) - cfr(game, 1, pi, strategy.get(a) * po, cpRound);
            // utils.set(a, res);
            // }

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
}
