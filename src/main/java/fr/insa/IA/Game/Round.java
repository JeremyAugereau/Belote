package fr.insa.IA.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Couleur;
import fr.insa.IA.Player.Player;

/**
 * Un Round c'est un pli
 */
public class Round implements Serializable {

    public class Coup {
        private Player player;
        private Carte carte;
        private int estDeLaMain;

        public Coup(Player p, Carte c, int estDeLaMain) {
            player = p;
            carte = c;
            this.estDeLaMain = estDeLaMain;
        }

        public Carte getCarte() {
            return carte;
        }

        public Player getPlayer() {
            return player;
        }

        public int getEstDeLaMain() {
            return estDeLaMain;
        }

        public void setCarte(Carte carte) {
            this.carte = carte;
        }

        public void setEstDeLaMain(int estDeLaMain) {
            this.estDeLaMain = estDeLaMain;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }
    }

    private Player firstPlayer;
    private List<Player> players;
    private List<Coup> pli;
    private Game game;

    public Round(List<Player> plist, Player player, Game game) {
        players = plist;
        this.game = game;
        firstPlayer = player;
        pli = new ArrayList<>();
    }

    public Round(Round round) {
        this.firstPlayer = round.firstPlayer;
        this.game = round.game;
        this.players = round.players;
        this.pli = round.pli;
    }

    // public Player roundProceed() {
    // Player roundWinner;
    // for (int i = players.indexOf(firstPlayer); i < players.indexOf(firstPlayer) +
    // players.size(); i++) {
    // game.setCurrentPlayer(players.get(i % players.size()));
    // actionProceed(game.getCurrentPlayer());
    // }
    // roundWinner = getRoundWinner();
    // roundWinner.setScore(roundWinner.getScore() + getRoundValue());
    // game.addRound(this);
    // return roundWinner;
    // }

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
            // System.out.println("------------------------------------------");
            // System.out.println(player);
            // Carte.printCards(carte);
            // Carte.printCards(getPlayableCard(player));
            // System.out.println("------------------------------------------");
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

        // System.out.println("------------------------------------------");
        // for (Coup c : pli) {
        // Carte.printCards(c.carte);
        // }
        // System.out.println("------------------------------------------");

        for (Coup c : pli) {
            if (c.player == player) {
                throw new IllegalArgumentException();
            }
            // if (pli.get(0).carte == null)
            // System.out.println("OUILLE");
            Couleur mustCoul = pli.get(0).carte.getCouleur();
            playableCartes.removeIf(crt -> {
                // if (crt == null) {
                // System.out.println("AIE");
                // return true;
                // }
                return crt.getCouleur() != mustCoul;
            });
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
