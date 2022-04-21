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
            for (Player p : game.getPlayers()) {
                System.out.println(p);
                Carte.printCards(p.getHand());
                Carte.printCards(p.getTable());
                Carte.printCards(p.getSecret());
            }
            value += cfr(game, 1, 1.0, 1.0, null);
        }
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

    public double cfr(Game game, int idPlayer, double pi, double po, Round round) {

        if (game.isOver())
            return game.payoff(game.getPlayerById(idPlayer));
        int numAction = game.getCurrentRound().getPlayableCard(game.getPlayerById(idPlayer)).size();
        Noeud noeud;
        if (hashMap.containsKey(game.getGameInfoSet())) {
            noeud = hashMap.get(game.getGameInfoSet());
        } else {
            noeud = new Noeud(game);
            hashMap.put(game.getGameInfoSet(), noeud);
        }
        List<Double> strategy = getStrategy(noeud.getSumRegret());
        List<Double> utils = new ArrayList<>(Collections.nCopies(numAction, 0.0));
        double node_util = 0;
        Round cpRound = null;
        List<Carte> actions = game.getPlayerById(idPlayer).getKnownCards();
        if (round != null) {
            cpRound = new Round(round);
            actions = cpRound.getPlayableCard(game.getPlayerById(idPlayer));
        }
        // System.out.println("------------------------------------------");
        // Carte.printCards(actions);
        // System.out.println("------------------------------------------");

        for (Carte carte : actions) {

            int a = actions.indexOf(carte);
            cpRound = game.next(carte, cpRound);
            // System.out.println("------------------------------------------");
            // System.out.println(a);
            // System.out.println(actions.contains(carte));
            // System.out.println("------------------------------------------");
            double res;
            int nextId = game.getCurrentPlayer().getId(); // deja update ds next
            if (idPlayer == nextId) {
                if (idPlayer == 1) {
                    res = utils.get(a) + cfr(game, nextId, pi * strategy.get(a), po, cpRound);
                } else {
                    res = utils.get(a) + cfr(game, nextId, pi, strategy.get(a) * po, cpRound);
                }
            } else { // ATTENTION c quoi currentRound par rapport à cpRound ?
                if (idPlayer == 1) {
                    res = utils.get(a) - cfr(game, nextId, pi * strategy.get(a), po, cpRound);
                } else {
                    res = utils.get(a) - cfr(game, nextId, pi, strategy.get(a) * po, cpRound);
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
            try {
                game.undo();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Fin du Undo");
            }
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
