package fr.insa.IA;

import java.io.IOException;
import fr.insa.IA.Game.Game;
import fr.insa.IA.IA.CFR;

public class App {

    public static void main(String[] args) {
        // Pour lancer le programme les paramètres sont " nbIteration JoueurSouhaité(p1
        // ou p2)"

        Game game = new Game(2);
        CFR bot = new CFR();
        game.setBot(bot);
        if (args.length != 3) {
            System.err.println(
                    "Veuillez lancer le programme avec 3 paramètres : l'algorithme d'entrainement (cfr ou mccfr)," +
                            "le nombre d'iteration d'entrainement du bot, et les joueurs que vous voulez jouer (p1 ou p2)");
            System.exit(1);
        } else {
            String algo = args[0].toUpperCase();
            int nbIteration = Integer.parseInt(args[1]);
            try {
                bot.importMap("modele_serialized_" + algo + "_" + nbIteration + ".ser");
            } catch (IOException e) {
                System.out.println("Fichier introuvale, training du bot ...");
                switch (algo) {
                    case "CFR":
                        bot.trainingCFR(nbIteration);
                        break;
                    case "MCCFR":
                        bot.trainingMCCFR(nbIteration);
                        break;
                    default:
                        System.err.println("Veuillez lancer le programme avec un algorithme correct : cfr ou mccfr");
                        System.exit(1);
                }

            }

            if (args[1].equals("p2")) {
                game.setCurrentPlayer(game.getPlayerById(2));
            }
            game.proceedGame();
        }
    }
}
