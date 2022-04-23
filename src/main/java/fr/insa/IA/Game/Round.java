package fr.insa.IA.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Couleur;
import fr.insa.IA.Player.Player;

/**
 * A Round of our simplified Belote. In a round, each player plays 1 card, modelized by a
 * "coup".
 * 
 * @see Coup
 * 
 * @author AUGEREAU Jeremy
 * @author GRAC Guilhem
 */
public class Round implements Serializable {


    /**
     * A Coup represent a card played by a player within a Round.
     * 
     * @see Carte
     * @see Player
     */
    public class Coup {
        /**
         * Player who played the "coup".
         * 
         * @see Player
         */
        private Player player;
        /**
         * Card played.
         * 
         * @see Carte
         */
        private Carte carte;
        /**
         * To know from where the {@link #carte} comes from.
         * <p>
         * If -1 : the card comes from the player's hand.
         * </p>
         * <p>
         * If >=0 : index of the card when it was in the player's table.
         * </p>
         * 
         * @see Player
         */
        private int estDeLaMain;

        /**
         * Constructor of Coup. Creates a new Coup.
         * 
         * @param p           ({@link Player}) player who played this "coup"
         * @param c           ({@link Carte}) card played
         * @param estDeLaMain (int) from where the card comes from (>= -1)
         */
        public Coup(Player p, Carte c, int estDeLaMain) {
            player = p;
            carte = c;
            this.estDeLaMain = estDeLaMain;
        }

        /**
         * Getter of {@link #carte}, the card played in this "coup"
         * 
         * @return ({@link Carte}) card played
         * @see Carte
         */
        public Carte getCarte() {
            return carte;
        }

        /**
         * Setter of {@link #carte}, the card played in this "coup".
         * 
         * @param carte played
         */
        public void setCarte(Carte carte) {
            this.carte = carte;
        }

        /**
         * Getter of {@link #player}, the player of this "coup".
         * 
         * @return ({@link Player}) player who played
         * @see Player
         */
        public Player getPlayer() {
            return player;
        }

        /**
         * Setter of {@link #player}, the player of this "coup".
         * 
         * @param player who played
         */
        public void setPlayer(Player player) {
            this.player = player;
        }

        /**
         * Getter of {@link #estDeLaMain}.
         * 
         * @return (int) value superior or equal to -1
         */
        public int getEstDeLaMain() {
            return estDeLaMain;
        }

        /**
         * Setter of {@link #estDeLaMain}.
         * 
         * @param estDeLaMain (int) superior or equal to -1.
         */
        public void setEstDeLaMain(int estDeLaMain) {
            this.estDeLaMain = estDeLaMain;
        }

    }

    private Player firstPlayer;
    private List<Player> players;
    /**
     * List of Coup composing the round. There can be 0, 1, or 2 "coups" within.
     * 
     * @see Coup
     */
    private List<Coup> pli;
    /**
     * Parent game.
     * 
     * @see Game
     */
    private Game game;

    public Round(List<Player> plist, Player player, Game game) {
        players = plist;
        this.game = game;
        firstPlayer = player;
        pli = new ArrayList<>();
    }


    public void roundProceed() {
        while (pli.size() < 2) {

            if (game.getCurrentPlayer().getId() == 1) {
                game.printGameState();
                actionProceed();
            } else {
                botActionProceed();
            }
            game.setCurrentPlayer(nextCurrentPlayer());
        }
    }

    private void botActionProceed() {
        Player player = game.getCurrentPlayer();
        Carte cartePlayed = game.getBot().chooseAction(game);
        if (player.getHand().contains(cartePlayed)) {
            pli.add(new Coup(player, cartePlayed, -1));
        } else {
            pli.add(new Coup(player, cartePlayed, player.getTable().indexOf(cartePlayed)));
        }
        Carte.printCards(cartePlayed);
        player.removeCarte(cartePlayed);

    }

    public void actionProceed() {
        Player player = game.getCurrentPlayer();
        System.out.println("Pli en cours :");
        List<Carte> carteToPrint = new ArrayList<>();
        if (pli.size() != 0) {

            for (Coup coup : pli) {
                carteToPrint.add(coup.carte);
            }
            Carte.printCards(carteToPrint);

        }
        System.out.println("Tu peux jouer :");
        Carte.printCards(getPlayableCard(player));
        System.out.println("Entre le numero de ta carte : ");
        Scanner input = new Scanner(System.in);
        int nb = Integer.parseInt(input.nextLine());
        Carte cartePlayed = getPlayableCard(player).get(nb);
        if (player.getHand().contains(cartePlayed)) {
            pli.add(new Coup(player, cartePlayed, -1));
        } else {
            pli.add(new Coup(player, cartePlayed, player.getTable().indexOf(cartePlayed)));
        }
        Carte.printCards(cartePlayed);
        player.removeCarte(cartePlayed);
    }

    public void actionProceed(Player player, Carte carte) {
        if (!(getPlayableCard(player).contains(carte))) {
            throw new IllegalArgumentException();
        }
        if (player.getHand().contains(carte)) {
            pli.add(new Coup(player, carte, -1));
        } else {
            pli.add(new Coup(player, carte, player.getTable().indexOf(carte)));
        }
        player.removeCarte(carte);
        game.setCurrentPlayer(nextCurrentPlayer());

    }

    public Player nextCurrentPlayer() {
        if (pli.size() == players.size()) {
            return getRoundWinner();
        } else {
            for (Player p : players) {
                if (game.getCurrentPlayer().getId() != p.getId()) {
                    return p;
                }
            }
            return game.getPlayerById((game.getCurrentPlayer().getId() % players.size()) + 1);
        }
    }

    public List<Carte> getPlayableCard(Player player) {
        List<Carte> playableCartes = new ArrayList<>();
        playableCartes = player.getKnownCards();

        if (pli.isEmpty() || pli.size() == players.size()) {
            return playableCartes;
        }

        if (pli.size() > players.size())
            throw new IllegalArgumentException(); // juste au cas où

        for (Coup c : pli) {
            if (c.player == player) {
                throw new IllegalArgumentException();
            }
            Couleur mustCoul = pli.get(0).carte.getCouleur();
            playableCartes.removeIf(crt -> crt.getCouleur() != mustCoul);

        }
        if (playableCartes.isEmpty()) {
            playableCartes = player.getKnownCards();
        }
        return playableCartes;

    }

    public Player getRoundWinner() {
        Couleur currentCouleur = pli.get(0).carte.getCouleur();
        Carte currentCarteWinner = pli.get(0).carte;
        Player roundWinner = pli.get(0).player;
        for (Coup coup : pli) {
            if (coup.carte.getCouleur() == currentCouleur && coup.carte.compareTo(currentCarteWinner) > 0) {
                currentCarteWinner = coup.carte;
                roundWinner = coup.player;
            }
        }
        return roundWinner;
    }

    public int getRoundValue() {
        int score = 0;
        for (Coup coup : pli) {
            score += coup.carte.getHauteur().toScore();
        }
        return score;
    }

    public List<Coup> getPli() {
        return pli;
    }

    public void removeCoup(Coup c) {
        pli.remove(c);
    }

}
