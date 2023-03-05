package de.tuberlin.sese.swtpp.gameserver.control;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * This class holds game-related use case and additional functionality required
 * by these.
 */
public class GameController implements Serializable {

    /**
     * ID for Serialization.
     */
    private static final long serialVersionUID = 1L;

    // associations

    /**
     * Full list of games.
     */
    protected LinkedList<Game> games = new LinkedList<>();

    // singleton instance

    /**
     * Full list of games.
     */
    private static GameController gameController;

    protected GameController() {
    }

    /*******************************
     * Getters/Setters
     ******************************/

    public LinkedList<Game> getGames() {
        return games;
    }

    public void setGames(LinkedList<Game> games) {
        this.games = games;
    }

    /**
     * getInstance (Singleton)
     *
     * @return game controller singleton
     */
    public static GameController getInstance() {
        if (gameController == null)
            gameController = new GameController();

        return gameController;
    }

    /* ******************************
     * Use Case functionality
     ******************************/

    /***
     * Creates a new game (Type is determined by game factory). The starting user is
     * the first player. If bots are requested, they are created as well. The game
     * implementation itself determined whether there are actually enough players to
     * start the game.
     *
     * @param u uer
     * @param bots bot string delimited with ";"
     * @return ID of new game
     */
    public int startGame(final User u, final String bots, final String type) throws Exception {

        System.out.println("start game " + type + " with " + bots);

        Game newGame = GameFactory.createGame(type);

        System.out.println("game id " + newGame.getGameID() +
                " type " + newGame.getType() +
                " " + newGame.getClass().getName());

        Player p = new Player(u, newGame);
        u.addParticipation(p);

        newGame.addPlayer(p);

        if (!bots.equals("")) {
            String[] bTypes = bots.split(";");
            for (String bot : bTypes) {
                User botG = GameFactory.createBot(bot, newGame);
                Player botP = new Player(botG, newGame);
                newGame.addPlayer(botP);
            }

        }

        this.games.add(newGame);

        return newGame.getGameID();
    }

    /**
     * The supplied User joins an existing game (creating a Player object) that is
     * waiting for players.
     *
     * @param u user
     * @return -1 if there was no game waiting, otherwise: gameID
     */
    public int joinGame(User u, String type) {
        int ID;
        Game gameWaiting = findOldestGameWaitingforPlayers(u, type);

        if (gameWaiting == null) {
            return -1;
        } else {
            Player p = new Player(u, gameWaiting);
            u.addParticipation(p);

            gameWaiting.addPlayer(p);

            ID = gameWaiting.getGameID();

        }

        return ID;
    }

    /**
     * Move request of player is passed to the right game
     *
     * @param u      user
     * @param gameID with game to try a move in
     * @param move   move string representation (depends on game)
     * @return true if move was allowed and performed
     */
    public boolean tryMove(User u, int gameID, String move) {
        Game g = getGame(gameID);

        if (g != null) {

            // game running
            if (!g.isStarted() || g.isFinished()) {
                return false;
            } else {
                return g.tryMove(move, g.getPlayer(u));
            }
        }

        return false;
    }

    /**
     * GiveUp request of player is passed to the right game
     *
     * @param u      user
     * @param gameID which game to give up
     */
    public void giveUp(User u, int gameID) {
        Game g = getGame(gameID);

        if (g != null) {
            g.giveUp(g.getPlayer(u));
        }
    }

    /**
     * Call draw (Remis) request of player is passed to the right game
     *
     * @param u      user
     * @param gameID which game to call draw
     */
    public void callDraw(User u, int gameID) {
        Game g = getGame(gameID);

        if (g != null) {
            g.callDraw(g.getPlayer(u));
        }
    }

    /**
     * Retrieves the game state from the concrete game.
     *
     * @param gameID which game to get state from
     * @return game info
     */
    public String getGameState(int gameID) {
        Game g = getGame(gameID);

        if (g != null) {
            return g.getBoard();
        } else {
            return "";
        }
    }

    /**
     * Retrieves status text from the concrete game.
     *
     * @param gameID which game to get status for
     * @return game info
     */
    public String getGameStatus(int gameID) {
        Game g = getGame(gameID);

        if (g != null) {
            return g.getStatus();
        } else {
            return "";
        }
    }

    /**
     * Retrieves additional info text about the game state from the concrete game.
     *
     * @param gameID game id
     * @return game info
     */
    public String gameInfo(int gameID) {
        Game g = getGame(gameID);

        if (g != null) {
            return g.gameInfo();
        } else {
            return "";
        }
    }

    /*******************************
     * Helpers.
     ******************************/

    public Game getGame(int gameID) {

        for (Game g : games) {
            if (g.getGameID() == gameID)
                return g;
        }

        return null;
    }

    public Game findOldestGameWaitingforPlayers(User u, String type) {

        for (Game g : games) {
            if (!g.isStarted() && !g.isPlayer(u) && g.getType().equals(type))
                return g;
        }

        return null;
    }

    public void clear() {
        games = new LinkedList<>();
    }

}
