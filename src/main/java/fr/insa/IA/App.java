package fr.insa.IA;

import java.io.IOException;
import fr.insa.IA.Game.Game;
import fr.insa.IA.IA.CFR;


public class App {
    

    public static void main( String[] args )
    {

        Game game = new Game(2);
        CFR bot = new CFR();
        game.setBot(bot);
        try{
            bot.importMap("modele_serialized_1000.ser");
        }catch(IOException e){
            System.out.println("Fichier introuvale, training du bot ...");
            bot.training(1000);
        }
        game.proceedGame();
      
        // Pour lancer le programme les paramètres sont " nbIteration JoueurSouhaité(p1 ou p2)"
        // game.setBot(bot);
        // if(args.length !=0){
        //     int nbIteration = Integer.parseInt(args[0]);
        //     try{
        //         bot.importMap("modele_serialized_"+nbIteration+".ser");
        //     }catch(IOException e){
        //         bot.training(nbIteration);
        //     }
        //     if(args.length>1){
        //         if(args[1].equals("p2")){
        //             game.setCurrentPlayer(game.getPlayerById(2));
        //             game.proceedGame();
        //         }else{
        //             game.proceedGame();
        //         }
        //     }
        // }else{
        //     try{
        //         bot.importMap("modele_serialized_100000.ser");
        //     }catch(IOException e){
        //         bot.training(10000);
        //     }
        //     game.proceedGame();
        // }
    }
}
