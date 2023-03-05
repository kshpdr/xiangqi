package de.tuberlin.sese.swtpp.gameserver.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Bot implementation that launches Haskell program to retrieve single next
 * move.
 */
public class HaskellBot extends Bot implements Runnable {

    /**
     *
     */
    private static final long serialVersionUID = 1371646871809169057L;

    protected String path; // path of bot executable
    protected final String bot; // bot executable
    protected final Game game; // the game this bot plays

    public HaskellBot(Game game, String path, String bot) {
        super("HaskellBot");
        this.game = game;
        this.path = path;
        this.bot = bot;

        // start a bot poll thread
        new Thread(this).start();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getName() {
        return "HaskellBot";
    }

    @SuppressWarnings("all")
    @Override
    public void run() {
        // ToDo: switch to observer pattern

        // run until game is finished
        while (!game.isFinished()) {
            try {
                // check every second for changes
                Thread.sleep(1000);

                // do move when it's my turn
                if (game.isUsersTurn(this)) {
                    executeMove();
                }

            } catch (InterruptedException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    protected void executeMove() throws IOException, InterruptedException {

        // Execute command
        final String command = path + bot + " " + game.getBoard() + " " + game.nextPlayerString();
        System.out.println("bot command:" + command);

        final Process child = Runtime.getRuntime().exec(command, null, new File(path));

        // get command line response (wait for bot to finish)
        final BufferedReader bri = new BufferedReader(new InputStreamReader(child.getInputStream()));

        child.waitFor();

        // get result into single string
        StringBuilder result = new StringBuilder();
        while (bri.ready())
            result.append(bri.readLine());

        System.out.println("bot answer: " + result + ".");
        // give up when bot didn't find a move (but should have)
        if (result.toString().equals(""))
            game.giveUp(game.getPlayer(this));
        else {
            if (!game.tryMove(result.toString(), game.getPlayer(this))) {
                // give up when move was illegal
                game.giveUp(game.getPlayer(this));
            }
        }
    }

}
