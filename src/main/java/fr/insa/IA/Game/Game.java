package fr.insa.IA.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.insa.IA.Cartes.Carte;
import fr.insa.IA.Cartes.Deck;
import fr.insa.IA.IA.CFR;
import fr.insa.IA.IA.InfoSet;
import fr.insa.IA.Player.Player;

/**
 * A Game of our simplified Belote.
 * 
 * @author AUGEREAU Jeremy
 * @author GRAC Guilhem
 */
public class Game implements Serializable {
    /**
     * List of players in the game.
     * 
     * @see Player
     */
    private List<Player> players;
    /**
     * Deck used in the game.
     * 
     * @see Deck
     */
    private Deck deck;
    /**
     * List of rounds. This list grows throughtout the game.
     * Watch out ! The last round is the current round.
     * 
     * @see Round
     */
    private List<Round> rounds;
    private Round currentRound;
    /**
     * Player Who is about to play.
     * 
     * @see Player
     */
    private Player currentPlayer;

    /**
     * Constructor of Game.
     * @param nbPlayer (int) number of players playing the game (AI and human).
     */
    public Game(int nbPlayer) {
        players = new ArrayList<>();
        for (int i = 0; i < nbPlayer; i++) {
            players.add(new Player());
        }
        rounds = new ArrayList<>();
        deck = new Deck();
        deck.deal(players);
        currentPlayer = players.get(0);
        currentRound = new Round(players, currentPlayer, this);
    }

    /**
     * Proceed the entire game looping and creating new rounds.
     * Used when playing human vs human
     */
    public void proceedGame() {
        while (!isOver()) {
            currentRound = new Round(players, currentPlayer, this);
            currentPlayer = currentRound.roundProceed();
        }
        Player winner = players.get(0);
        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }
        System.out.println("Player " + winner.getId() + " a gagné !!!!!!");
    }

    /**
     * Proceed the entire game.
     * Human vs AI
     * 
     * @param bot bot to play against
     * @param i   player's id the human wants to play
     */
    public void proceedGame(CFR bot, int i) {
        while (!isOver()) {
            addRound(new Round(players, currentPlayer, this));
            currentPlayer = getCurrentRound().roundProceed();
        }
        proccedScore();
        Player winner = players.get(0);
        Player looser = players.get(1);
        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }
        for (Player p : players) {
            if (p.getScore() < looser.getScore()) {
                looser = p;
            }
        }
        if (winner.getId() == i) {
            System.out.println("Vous avec gagné avec " + winner.getScore() + " points contre " + looser.getScore());
        } else {
            System.out.println("Vous avec perdu avec " + looser.getScore() + " points contre " + winner.getScore());
        }

    }

    /**
     * Compute the score of score of each player at the end of the game by looking
     * at each round.
     */
    private void proccedScore() {
        for (Round round : rounds) {
            round.getRoundWinner().setScore(round.getRoundWinner().getScore() + round.getRoundValue());
        }
    }

    /**
     * Undo the last action / card played (Coup) played, and update consequently the
     * players.
     * Used by the AI.
     */
    public void undo() {
        // System.out.println(rounds.size());
        Round lastRound = rounds.get(rounds.size() - 1);
        // System.out.println(lastRound.getPli().size());

        Round.Coup c;
        if (lastRound.getPli().size() == 2) {
            c = lastRound.getPli().remove(1);
        } else if (lastRound.getPli().size() == 1) {
            if (rounds.size() > 1) { // IL NE FAUT JAMAIS QUE rounds SOIT VIDE
                rounds.remove(lastRound);
            }
            c = lastRound.getPli().remove(0);
        } else if (lastRound.getPli().size() == 0) {
            rounds.remove(rounds.size() - 1);
            lastRound = rounds.get(rounds.size() - 1);
            c = lastRound.getPli().remove(1);
        } else {
            throw new IllegalArgumentException();
        }

        Carte cartePlayed = c.getCarte();
        Player player = c.getPlayer();
        int estDeLaMain = c.getEstDeLaMain();
        if (estDeLaMain == -1) {
            player.addHand(cartePlayed);
        } else {
            player.setSecret(estDeLaMain, player.getTable().get(estDeLaMain));
            player.setTable(estDeLaMain, cartePlayed);
        }
        currentPlayer = player;
    }

    /**
     * The current player plays the next action / card.
     * Used by the AI.
     * 
     * @param carte card to be played
     * @param round current round where the play will occur
     */
    public void next(Carte carte, Round round) {
        if (round.getPli().size() == 2) {
            // System.out.println("*********************************************************");
            Round nextRound = new Round(players, currentPlayer, this);
            addRound(nextRound);
            // System.out.println(currentPlayer);
            // Carte.printCards(carte);
            nextRound.actionProceed(currentPlayer, carte);
        } else if (round.getPli().size() == 1) {
            // System.out.println(currentPlayer);
            // Carte.printCards(carte);
            round.actionProceed(currentPlayer, carte);
        } else if (round.getPli().size() == 0) { // ne sert qu'au tout début
            // System.out.println("#####################################");

            // System.out.println("------------------------------------------");
            // Carte.printCards(currentPlayer.getKnownCards());
            // System.out.println("------------------------------------------");
            // System.out.println(currentPlayer);
            // Carte.printCards(carte);
            round.actionProceed(currentPlayer, carte);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if the game is over by checking the hand and table size of the
     * players.
     * 
     * @return true if none of the players have cards to play.
     */
    public boolean isOver() {
        boolean res = true;
        for (Player player : players) {
            if (player.getKnownCards().size() != 0) {
                res = false;
            }
        }
        return res;
    }

    /**
     * Getter of {@link #players}.
     * 
     * @return players
     * @see Player
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Add a new round in the {@link #rounds} list.
     * 
     * @see Round
     * @param round ({@link Round}) to be added
     */
    public void addRound(Round round) {
        rounds.add(round);
    }

    /**
     * Getter of the current round.
     * The current round is the last element in {@link #rounds}. The
     * current round can
     * either have 0, 1, or 2 cards played.
     * 
     * @see Round
     * @return the current round
     */
    public Round getCurrentRound() {
        return rounds.get(rounds.size() - 1);
    }

    /**
     * Getter of the {@link #currentPlayer}.
     * 
     * @return player who is about to play
     * @see Player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Setter of the {@link #currentPlayer}.
     * 
     * @param currentPlayer ({@link Player}) : player who is about to play
     * @see Player
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Getter of the {@link #deck} used for this game.
     * 
     * @return deck used in the game
     * @see Deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Getter of the {@link #rounds} played. Watch out, the last one is the current
     * round.
     * 
     * @return List of rounds played
     * @see Round
     */
    public List<Round> getRounds() {
        return rounds;
    }

    /**
     * Look over the {@link #players} list to find the player with the specified id.
     * 
     * @param id (int) of the player
     * @return player
     * @throws IllegalArgumentException if the player is not found
     * @see Player
     */
    public Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * WTF
     * 
     * @param player ({@link Player})
     * @return (double)
     */
    public double payoff(Player player) {
        double score = 0;
        for (Round round : rounds) {
            if (round.getRoundWinner().equals(player)) {
                for (Round.Coup coup : round.getPli()) {
                    score += coup.getCarte().getHauteur().toScore();
                }
            } else {
                for (Round.Coup coup : round.getPli()) {
                    score -= coup.getCarte().getHauteur().toScore();
                }
            }
        }
        return score;
    }

    /**
     * Getter of the infoset at his current point in the game. An infoset contains
     * all the known information : already played cards, cards in hand, opponent
     * card in his table, ...
     * 
     * @return ({@link InfoSet})
     * @see InfoSet
     */
    public InfoSet getGameInfoSet() {
        InfoSet infoset = new InfoSet();
        if (getCurrentRound().getPli().size() == 1) {
            infoset.setCurrentCarte(getCurrentRound().getPli().get(0).getCarte());
        } else {
            infoset.setCurrentCarte(null);
        }
        for (Player player : players) {
            if (currentPlayer.getId() != player.getId()) {
                infoset.setEnemyTable(player.getTable());
            }
        }
        infoset.setHand(currentPlayer.getHand());
        List<Carte> h = new ArrayList<>();
        for (Round r : rounds) {
            for (Round.Coup c : r.getPli()) {
                h.add(c.getCarte());
            }
        }

        infoset.setHistory(h);
        infoset.setTable(currentPlayer.getTable());
        return infoset;
    }

}
