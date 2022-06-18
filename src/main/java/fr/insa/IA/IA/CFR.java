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

/**
 * CFR AI for our simplified Belote.
 * 
 * @author AUGEREAU Jeremy
 * @author GRAC Guilhem
 */
public class CFR implements Serializable {
    private HashMap<InfoSet, Noeud> hashMap;

    /**
     * Constructor of CFR. Initialize {@link #hashMap}.
     */
    public CFR() {
        hashMap = new HashMap<>();
    }

    /**
     * Train the AI by playing multiple games, and computing statistics to find the
     * best card to play.
     * 
     * @param n (int) number of iterations / games played
     * @return (double) the result of the training
     */
    public double trainingCFR(int n) {
        double value = 0.0;
        int pourcent = 0;
        String msg = "";
        for (int i = 1; i <= n; i++) {
            Player.resetNbPlayer();
            Game game = new Game(2);
            if (i * 100 / n > pourcent) {
                msg = "|" + "=".repeat(pourcent / 2 + 1) + " ".repeat((100 - pourcent) / 2 - 1) + "|\r";
                pourcent = i * 100 / n + 1;
                System.out.print(msg);
            }
            value += cfr(game, 1, 1.0, 1.0);
        }
        System.out.println("");
        System.out.println("Taille de la Map : " + hashMap.size());
        saveMap("CFR", n);
        System.out.println("HashMap sauvegardée");
        return value / n;
    }

    public double trainingMCCFR(int n) {
        double value = 0.0;
        int pourcent = 0;
        String msg = "";
        for (int i = 1; i <= n; i++) {
            for (int idPlayer = 1; idPlayer < 2; idPlayer++) {
                Player.resetNbPlayer();
                Game game = new Game(2);
                if (i * 100 / n > pourcent) {
                    msg = "|" + "=".repeat(pourcent / 2 + 1) + " ".repeat((100 - pourcent) / 2 - 1) + "|\r";
                    pourcent = i * 100 / n + 1;
                    System.out.print(msg);
                }
                double v = mccfr(game, idPlayer);
                if (idPlayer == 0)
                    value += v;
            }
        }
        System.out.println("");
        System.out.println("Taille de la Map : " + hashMap.size());
        saveMap("MCCFR", n);
        System.out.println("HashMap sauvegardée");
        return value / (double) n;
    }

    /**
     * Save the result of the training ({@link #hashMap}) inside a file.
     * 
     * @param name (String) name of the file
     */
    private void saveMap(String algo, int nbIteration) {
        try {
            FileOutputStream file = new FileOutputStream("modele_serialized_" + algo + nbIteration + ".ser");

            ObjectOutputStream output = new ObjectOutputStream(file);

            output.writeObject(hashMap);
            output.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Import the {@link #hashMap} saved precedently to make the AI play.
     * 
     * @param path (String) path where the file is located
     * @throws IOException if the file is not correctly opened
     * @see #saveMap(String)
     */
    public void importMap(String path) throws IOException {
        try {
            System.out.println("Chargement de la HashMap ...");
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream input = new ObjectInputStream(file);

            hashMap = (HashMap) input.readObject();

            input.close();
            file.close();
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("HashMap chargée");
    }

    /**
     * Save the results of the training into a unencrypted file. Presently not used,
     * but can be useful to see what's inside the {@link #hashMap}.
     * 
     * @param name (String) name of the file
     * @see #saveMap(String)
     */
    private void unencryptedSaveMap(String name) {
        String outputFilePath = "modele_" + name + ".txt";
        File file = new File(outputFilePath);
        BufferedWriter bf = null;

        try {
            bf = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<InfoSet, Noeud> entry : hashMap.entrySet()) {
                bf.write(entry.getKey() + ":" + entry.getValue());
                bf.newLine();
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bf.close();
            } catch (Exception e) {
            }
        }

    }

    /**
     * Return a strategy regarding the List in parameter, by normalizing it or
     * defining it as a uniform tab
     * 
     * @param array (List<Double>) that will be normalized
     * @return (List<Double>) the normalized strategy
     */

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

    /**
     * Make the average between the current node strategy and the strategy in
     * parameter.
     * 
     * @param noeud    ({@link Noeud}) the node corresponding to the current game
     *                 state
     * @param strategy (List<Double>) the strategy to be added to this node
     * @param p        (double) the weight of the strategy corresponding to this
     *                 node
     */

    public void updateStrategySum(Noeud noeud, List<Double> strategy, double p) {

        int n = noeud.getSumStrategy().size();
        List<Double> strategySum = noeud.getSumStrategy();
        for (int i = 0; i < n; i++) {
            double tmp = strategy.get(i) * p + strategySum.get(i);
            noeud.setSumStrategy(i, tmp);
        }

    }

    /**
     * CFR algorithm.
     * 
     * @param game     ({@link Game}) game where the AI is playing
     * @param idPlayer (int) id of the player who is about to play
     * @param pi       (double)
     * @param po       (double)
     * @return (double) the result of the game
     */
    public double cfr(Game game, int idPlayer, double pi, double po) {

        if (game.isOver()) {
            return game.payoff(game.getPlayerById(idPlayer));
        }

        if (game.getRounds().size() == 0) { // IL NE FAUT JAMAIS QUE rounds SOIT VIDE
            game.addRound(new Round(game));
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

    public int distribution(List<Double> strategy) {
        int n = strategy.size();
        double r = Math.random();
        return find(n, r, strategy, 0, strategy.get(0));
    }

    public int find(final int n, final double r, final List<Double> strategy, int i, double sum) {
        if (i == n - 1)
            return i;
        if (r < sum)
            return i;
        return find(n, r, strategy, i + 1, sum + strategy.get(i));
    }

    public double mccfr(Game game, int idPlayer) {
        if (game.isOver()) {
            return game.payoff((game.getPlayerById(idPlayer)));
        }

        if (game.getRounds().size() == 0) { // IL NE FAUT JAMAIS QUE rounds SOIT VIDE
            game.addRound(new Round(game));
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

        if (idPlayer != game.getCurrentPlayer().getId()) {
            int a = distribution(strategy);
            Carte carte = actions.get(a);
            game.next(carte, round);

            double util = mccfr(game, idPlayer);
            game.undo();
            updateStrategySum(noeud, strategy, 1);
            return util;
        } else {
            List<Double> utils = new ArrayList<>(Collections.nCopies(numAction, 0.0));
            double node_util = 0.0;

            for (Carte carte : actions) {
                int a = actions.indexOf(carte);
                game.next(carte, round);
                int nextId = game.getCurrentPlayer().getId(); // deja update ds next
                utils.set(a, mccfr(game, nextId));
                game.undo();
                node_util += strategy.get(a) * utils.get(a);
            }
            for (Carte carte : actions) {
                int a = actions.indexOf(carte);
                double regret = utils.get(a) - node_util;
                noeud.setSumregret(a, noeud.getSumRegret().get(a) + regret);
            }
            return node_util;
        }
    }

    /**
     * Allow the bot to choose a card to play by taking in account the game current
     * state.
     * 
     * @param game ({@link Game}) game where the AI is playing
     * @return (Carte) the card choosen to be played
     */
    public Carte chooseAction(Game game) {
        InfoSet info = game.getGameInfoSet();
        if (hashMap.containsKey(info)) {
            double sum = 0.0;
            List<Double> list = hashMap.get(info).getSumStrategy();
            for (Double d : list) {
                sum += d;
            }
            double random = Math.random() * sum;
            double sommeCumul = 0.0;
            for (int i = 0; i < list.size(); i++) {
                sommeCumul += list.get(i);
                if (random <= sommeCumul)
                    return game.getCurrentRound().getPlayableCard(game.getCurrentPlayer()).get(i);
            }
            return game.getCurrentRound().getPlayableCard(game.getCurrentPlayer()).get(list.size() - 1); // ça devrait
                                                                                                         // jamais
                                                                                                         // arriver mais
                                                                                                         // au cas ou;
        } else {
            int random = (int) Math.random() * game.getCurrentRound().getPlayableCard(game.getCurrentPlayer()).size();
            return game.getCurrentRound().getPlayableCard(game.getCurrentPlayer()).get(random);
        }
    }
}
