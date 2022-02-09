package de.tuberlin.sese.swtpp.gameserver.control;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.HaskellBot;
import de.tuberlin.sese.swtpp.gameserver.model.User;
import de.tuberlin.sese.swtpp.gameserver.model.xiangqi.XiangqiGame;

public class GameFactory {

    //TODO: change path to bot executable if desired
    public static final String XIANGQI_BOT_PATH = "";
    public static final String XIANGQI_BOT_COMMAND = "Bot";

    private GameFactory() {
    }

    public static Game createGame(String gameType) throws Exception {
        try {
            switch (gameType) {
                case "xiangqi":
                    return new XiangqiGame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("Illegal game type encountered");
    }

    public static User createBot(String type, Game game) {
        if (type.equals("haskell")) {
            switch (game.getClass().getName().substring(game.getClass().getName().lastIndexOf(".") + 1)) {
                case "XiangqiGame":
                    return new HaskellBot(game, XIANGQI_BOT_PATH, XIANGQI_BOT_COMMAND);
                default:
                    return null;
            }
        }
        return null;
    }

}
