package de.tuberlin.sese.swtpp.gameserver.web;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.control.UserController;
import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.Statistics;
import de.tuberlin.sese.swtpp.gameserver.model.User;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet implementation class GameServerServlet
 */
@WebServlet("/GameServerServlet")
public class GameServerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // TODO: change location of data if you like
    // this is just hardcoded to "somewhere"
    private static final String DB_PATH = "test.db";

/////////////////////////////////////////////////////////////////////
//        GLOBAL DATA                                              //
/////////////////////////////////////////////////////////////////////														   

    // controller classes
    public static UserController userController;
    public static GameController gameController;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GameServerServlet() {
        super();

        userController = UserController.getInstance();
        gameController = GameController.getInstance();

        readState();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        String usecase = request.getParameter("usecase");

        if (usecase == null || usecase.equals("") || usecase.equals("checkuser")) {
            ServletContext sc = getServletContext();
            RequestDispatcher rd;

            rd = sc.getRequestDispatcher("/main.html");
            rd.forward(request, response);
        } else if (usecase.equals("login")) {
            login(request, out);
        } else if (usecase.equals("logout")) {
            // log of just disconnects the current user and goes back to start
            request.getSession().removeAttribute("currentUser");
            // go to index page
        } else if (usecase.equals("register")) {
            register(request, out);
        } else {
            User u = (User) request.getSession().getAttribute("currentUser");

            // answer with empty string when user is not logged in
            if (u != null) {
                executeGameUseCase(request, out, usecase, u);
            }
        }
    }

    private void executeGameUseCase(HttpServletRequest request, PrintWriter out, String usecase, User u) {
        // game-related and data-related use cases.
        // servlet's job here is only to extract the correct parameters and to pass them
        // on to the use case methods (see below)
        // the result is passed back to javascript as a string (if complex: JSON)
        try {
            switch (usecase) {
                case "startgame": out.write(startGame(u, request.getParameter("bots"), request.getParameter("type"))); break;
                case "joingame": out.write(joinGame(u, request.getParameter("type"))); break;
                case "getuserdata": out.write(getUserDataJSON(u)); break;
                case "getstatistics": out.write(getStatisticsJSON(u)); break;
                default : {
                    int gameID = Integer.parseInt(request.getParameter("gameID"));
                    switch (usecase) {
                        case "giveUp": out.write(giveUp(u, gameID)); break;
                        case "callDraw": out.write(callDraw(u, gameID)); break;
                        case "getgamedata": out.write(getGameData(u, gameID)); break;
                        case "trymove": {
                            String move = request.getParameter("move");
                            out.write(tryMove(u, gameID, move));
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Request could not be processed.");
            e.printStackTrace();
        }

        // might be smarter to save only if there was a change
        // and also bot moves are not saved....
        saveState();
    }

    private void register(HttpServletRequest request, PrintWriter out) {
        String name = request.getParameter("name");
        Pattern p = Pattern.compile("[a-zA-Z\\s,]+");
        Matcher m = p.matcher(name);

        if (!m.matches()) {
            out.write("badinput");
            return;
        }

        if (userController.checkUserExists(request.getParameter("id"))) {
            out.write("exists");
            return;
        }

        // register a new user with the selected data. the createUser method returns
        // error messages when the data given was not correct
        User u = userController.register(request.getParameter("id"), request.getParameter("name"),
                request.getParameter("password"));

        if (u != null) {
            request.getSession().setAttribute("currentUser", u);
            saveState();
        } else {
            out.write("badinput");
        }
    }

    private void login(HttpServletRequest request, PrintWriter out) {
        // normal log in request : check user ID and password
        // if ok remember user and forward to main page, else try again
        String id = request.getParameter("id");
        String password = request.getParameter("password");

        if (userController.checkUserPwd(id, password)) {
            request.getSession().setAttribute("currentUser", userController.findUserByID(request.getParameter("id")));
        } else {
            out.write("User/Pwd unbekannt.");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // forward post requests to get method
        doGet(request, response);
    }

/////////////////////////////////////////////////////////////
///      WEB INTERFACE: USE CASE FUNCTIONS                 //
/////////////////////////////////////////////////////////////

    public String startGame(User u, String bots, String type) throws Exception {
        int ID = gameController.startGame(u, bots, type);
        return createGameJSON(u, ID);
    }

    public String joinGame(User u, String type) {
        int ID = gameController.joinGame(u, type);

        if (ID >= 0) {
            return createGameJSON(u, ID);
        } else {
            return "nogame";
        }
    }

    public String tryMove(User u, int gameID, String move) {

        gameController.tryMove(u, gameID, move);

        return createGameJSON(u, gameID);

    }

    public String giveUp(User u, int gameID) {

        gameController.giveUp(u, gameID);

        return createGameJSON(u, gameID);

    }

    public String callDraw(User u, int gameID) {

        gameController.callDraw(u, gameID);

        return createGameJSON(u, gameID);

    }

    public String getGameData(User u, int gameID) {

        return createGameJSON(u, gameID);
    }

    public String getGameData(Player p, int gameID) {
        return createGameJSON(p, gameID);
    }

    public String createGameJSON(Player pl, int gameID) {
        Game g = gameController.getGame(gameID);
        
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        
        if(g != null) { 
	        JsonArrayBuilder players = factory.createArrayBuilder();
	
	        for (Player p : g.getPlayers()) {
	            players.add(p.getName());
	        }
	
	        JsonObject value = factory.createObjectBuilder().add("gameID", gameID).add("gameType", g.getType())
	                .add("players", players).add("status", g.getStatus()).add("info", g.gameInfo()).add("requestedby", pl.getUser().getName())
	                .add("yourturn", g.isPlayersTurn(pl)).add("board", g.getBoard()).build();
	        return value.toString();
        } else {
        	return factory.createObjectBuilder().build().toString();
        }
	        
        

    }

    public String createGameJSON(User u, int gameID) {
        Player p = gameController.getGame(gameID).getPlayer(u);
        return createGameJSON(p, gameID);
    }

    public String getUserDataJSON(User u) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonArrayBuilder games = factory.createArrayBuilder();

        for (Player p : u.getActiveParticipations()) {
        	JsonObjectBuilder game = factory.createObjectBuilder()
						        		.add("id", p.getGame().getGameID())
						        		.add("type", p.getGame().getType());
            if (p.getGame().isStarted()) {
                game.add("players", 
                    		p.getGame().getPlayers().stream()
                    			.map(Player::getName)
                    			.reduce((x, y) -> x + " vs " + y).get());
            } else {
                game.add("players", "Waiting for Player.");
            }
            
            games.add(game);
        }

        JsonObject value = factory.createObjectBuilder().add("userID", u.getId()).add("userFirstName", u.getName())
                .add("games", games).build();

        return value.toString();
    }

    public String getStatisticsJSON(User u) {
        Statistics s = userController.getStatistics(u);

        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonObject value = factory.createObjectBuilder().add("userFirstName", u.getName()).add("nbWon", s.numWon)
                .add("nbLost", s.numLost).add("nbDraw", s.numDraw).add("avgMoves", s.avgMoves)
                .add("fracWon", s.percentWon).add("fracDraw", s.percentDraw).add("fracLost", s.percentLost).build();

        // System.out.println(value.toString());
        return value.toString();
    }

    public void saveState() {
        try {
            FileOutputStream fos = new FileOutputStream(DB_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(UserController.getInstance().getUsers());
            oos.writeObject(GameController.getInstance().getGames());
            oos.writeInt(Game.getLastID());

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings(value = {"unchecked"})
    public void readState() {
        FileInputStream fis;
        try {
            fis = new FileInputStream(DB_PATH);
            ObjectInputStream ois = new ObjectInputStream(fis);

            UserController.getInstance().setUsers((HashMap<String, User>) ois.readObject());
            GameController.getInstance().setGames((LinkedList<Game>) ois.readObject());
            Game.setLastID(ois.readInt());

            ois.close();
            fis.close();

        } catch (FileNotFoundException e) {
            System.out.println("No database found. Starting new...");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
