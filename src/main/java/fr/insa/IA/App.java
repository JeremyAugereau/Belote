package fr.insa.IA;

import java.io.IOException;
import fr.insa.IA.Game.Game;
import fr.insa.IA.IA.CFR;

/**
 * App is our main.
 *
 * @author AUGEREAU Jeremy
 * @author GRAC Guilhem
 */
public class App {

    public static void main(String[] args) {
        // Pour lancer le programme les paramètres sont " nbIteration JoueurSouhaité(p1
        // ou p2)"

        Game game = new Game(2);
        CFR bot = new CFR();
        game.setBot(bot);
        if (args.length != 2) {
            System.err.println(
                    "Veuillez lancer le programme avec 2 paramètres : le nombre d'iteration d'entrainement du bot, et les joueurs que vous voulez jouer (p1 ou p2)");
            System.exit(1);
        } else {
            int nbIteration = Integer.parseInt(args[0]);
            try {
                bot.importMap("modele_serialized_" + nbIteration + ".ser");
            } catch (IOException e) {
                System.out.println("Fichier introuvale, training du bot ...");
                bot.training(nbIteration);
            }

            if (args[1].equals("p2")) {
                game.setCurrentPlayer(game.getPlayerById(2));
            }
            game.proceedGame();
        }
    }
}
