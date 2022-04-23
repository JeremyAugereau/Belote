package fr.insa.IA;

import java.io.IOException;
import fr.insa.IA.Game.Game;
import fr.insa.IA.IA.CFR;


public class App {
    

    public static void main( String[] args )
    {

        // Game game  = new Game(2);
        // game.proceedGame();
        // Map<InfoSet,Noeud> map = new HashMap<>();

        // InfoSet i1 = new InfoSet();
        // InfoSet i2 = new InfoSet();

        // Game g = new Game(2);

        // Noeud n1 = new Noeud(g);
        // System.out.println(n1);
        // Noeud n2 = new Noeud(g);
        // System.out.println(n2);


        // map.put(i1,n1);
        // map.put(i2,n2);

        // System.out.println(map.get(i1));
        // System.out.println(i1.equals(i2));
        // System.out.println(i1.hashCode());
        // System.out.println(i2.hashCode());


        Game game = new Game(2);
        CFR bot = new CFR();
        try{
            bot.importMap("modele_serialized_10000.ser");
        }catch(IOException e){
            bot.training(100);
        }
        game.proceedGame();
        


        //test d'import du modele 
        //bot.importMap("modele_serialized_10.ser");


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
        //             game.proceedGame(2);
        //         }else{
        //             game.proceedGame(1);
        //         }
        //     }
        // }else{
        //     try{
        //         bot.importMap("modele_serialized_100000.ser");
        //     }catch(IOException e){
        //         bot.training(10000);
        //     }
        //     game.proceedGame(bot,1);
        // }
    }
}
