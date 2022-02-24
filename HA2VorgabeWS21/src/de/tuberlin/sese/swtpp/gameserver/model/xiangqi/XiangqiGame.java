package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import de.tuberlin.sese.swtpp.gameserver.model.*;
import java.util.*;
//TODO: more imports from JVM allowed here

import java.io.Serializable;
import java.util.ArrayList;


public class XiangqiGame extends Game implements Serializable{

	
	private static final long serialVersionUID = 5424778147226994452L;

	// just for better comprehensibility of the code: assign red and black player
	private Player blackPlayer;
	private Player redPlayer;
	private Board board;

	/************************
	 * constructors
	 ***********************/

	public XiangqiGame() {
		
		// initialization of game:
		super();
		
		// initialization of board:
		this.board = new Board("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR");	
	}

	public String getType() {
		return "xiangqi";
	}

	/*******************************************
	 * Game class functions already implemented
	 ******************************************/

	@Override
	public boolean addPlayer(Player player) {
		if (!started) {
			players.add(player);

			// game starts with two players
			if (players.size() == 2) {
				started = true;
				this.redPlayer = players.get(0);
				this.blackPlayer= players.get(1);
				nextPlayer = redPlayer;
			}
			return true;
		}

		return false;
	}

	@Override
	public String getStatus() {
		if (error)
			return "Error";
		if (!started)
			return "Wait";
		if (!finished)
			return "Started";
		if (surrendered)
			return "Surrendered";
		if (draw)
			return "Draw";

		return "Finished";
	}

	@Override
	public String gameInfo() {
		String gameInfo = "";

		if (started) {
			if (blackGaveUp())
				gameInfo = "black gave up";
			else if (redGaveUp())
				gameInfo = "red gave up";
			else if (didRedDraw() && !didBlackDraw())
				gameInfo = "red called draw";
			else if (!didRedDraw() && didBlackDraw())
				gameInfo = "black called draw";
			else if (draw)
				gameInfo = "draw game";
			else if (finished)
				gameInfo = blackPlayer.isWinner() ? "black won" : "red won";
		}

		return gameInfo;
	}

	@Override
	public String nextPlayerString() {
		return isRedNext() ? "r" : "b";
	}

	@Override
	public int getMinPlayers() {
		return 2;
	}

	@Override
	public int getMaxPlayers() {
		return 2;
	}

	@Override
	public boolean callDraw(Player player) {

		// save to status: player wants to call draw
		if (this.started && !this.finished) {
			player.requestDraw();
		} else {
			return false;
		}

		// if both agreed on draw:
		// game is over
		if (players.stream().allMatch(Player::requestedDraw)) {
			this.draw = true;
			finish();
		}
		return true;
	}

	@Override
	public boolean giveUp(Player player) {
		if (started && !finished) {
			if (this.redPlayer == player) {
				redPlayer.surrender();
				blackPlayer.setWinner();
			}
			if (this.blackPlayer == player) {
				blackPlayer.surrender();
				redPlayer.setWinner();
			}
			surrendered = true;
			finish();

			return true;
		}

		return false;
	}

	/* ******************************************
	 * Helpful stuff
	 ***************************************** */

	/**
	 *
	 * @return True if it's red player's turn
	 */
	public boolean isRedNext() {
		return nextPlayer == redPlayer;
	}

	/**
	 * Ends game after regular move (save winner, finish up game state,
	 * histories...)
	 *
	 * @param winner player who won the game
	 * @return true if game was indeed finished
	 */
	public boolean regularGameEnd(Player winner) {
		// public for tests
		if (finish()) {
			winner.setWinner();
			winner.getUser().updateStatistics();
			return true;
		}
		return false;
	}

	public boolean didRedDraw() {
		return redPlayer.requestedDraw();
	}

	public boolean didBlackDraw() {
		return blackPlayer.requestedDraw();
	}

	public boolean redGaveUp() {
		return redPlayer.surrendered();
	}

	public boolean blackGaveUp() {
		return blackPlayer.surrendered();
	}

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 ******************************************/

	@Override
	public void setBoard(String state) {
		// This method is for automatic testing, a regular game would not start at some artificial state:
		this.board = new Board(state);
	}

	@Override
	public String getBoard() {
		return this.board.getBoardState();
	}
	
	
	
	// checks format of moveString (eg. "c5-a5"):
	public boolean moveStringFormatCheck(String moveString) {
		
		if(moveString.length() != 5) {
			return false;
		}
		if("abcdefghi".indexOf(moveString.charAt(0)) == -1 || "abcdefghi".indexOf(moveString.charAt(3)) == -1) {
			return false;
		}
		if("9876543210".indexOf(moveString.charAt(1)) == -1 || "9876543210".indexOf(moveString.charAt(4)) == -1) {
			return false;
		}
		if(moveString.charAt(2) != '-') {
			return false;
		}	
		return true;
	}
	
	// extends cheatCheck:
	public boolean cheatCheckExtended(String moveString, Player player) {
		int row = "9876543210".indexOf(moveString.charAt(1));
		int col = "abcdefghi".indexOf(moveString.charAt(0));
		Position pos = new Position(row,col);
		
		
		// --> checks whether game not finished yet:
		if(this.finished) {
			return false;
		}
		// --> checks whether player tries to move playing-piece of other player:
		if(player == this.redPlayer && !pos.isRed(board)) {
			return false;
		}
		if(player != this.redPlayer && pos.isRed(board)) {
			return false;
		}	
		return true;
	}
	
	// checks whether player is trying to cheat:
	public boolean cheatCheck(String moveString, Player player) {
		
		// start Position of move:
		int row = "9876543210".indexOf(moveString.charAt(1));
		int col = "abcdefghi".indexOf(moveString.charAt(0));
		
		// --> checks format of moveString:
		if(!moveStringFormatCheck(moveString)) {
			return false;
		}	
		// --> checks whether startPosition and targetPosition of moveSting differ:
		if(("9876543210".indexOf(moveString.charAt(4)) == row) && ("abcdefghi".indexOf(moveString.charAt(3)) == col)) {
			return false;
		}
		// --> checks whether it's player's turn:
		if(!this.isPlayersTurn(player)) {
			return false;
		}
		if(!cheatCheckExtended(moveString, player)) {
			return false;
		}
		
		return true;
	}
	
	
	// adds new move to history:
	public void updateHistory(Move move) {

		List<Move> updateHistory = this.getHistory();
		updateHistory.add(move);
		this.setHistory(updateHistory);
	}
	
	
	
	// executes move, updates boardMatrix, boardStateString and history: 
	public void doMove(Move move, Figur figur) {
		
		// start:
		int row1 = "9876543210".indexOf(move.getMove().charAt(1));
		int col1 = "abcdefghi".indexOf(move.getMove().charAt(0));
		// target:
		int row2 = "9876543210".indexOf(move.getMove().charAt(4));
		int col2 = "abcdefghi".indexOf(move.getMove().charAt(3));
		
		// moves playing-piece by updating boardMatrix:
		this.board.getBoardMatrix()[row2][col2] = this.board.getBoardMatrix()[row1][col1];
		this.board.getBoardMatrix()[row1][col1] = '0';
		
		// updates boardStateString:
		String stringCheck = this.board.boardMatrixToBoardString();
		this.setBoard(this.board.boardMatrixToBoardString());
		
		// updates position of playing-piece:
		figur.setPosition(new Position(row2,col2));
		
		// updates history:
		updateHistory(move);
	}
	
	
	
	// --> returns figures of enemy:
	ArrayList<Figur> enemyFigures(Board board, Player player) {
		
		if(player == this.redPlayer) {
			return board.blackFigures;
		}
		return board.redFigures;
	}
	
	
	
	// --> returns general of enemy:
	public General getEnemyGeneral(Player player) {
		
		if(player == this.redPlayer) {
			return this.board.getBlackGeneral();	
		} 
		return this.board.getRedGeneral();	
	}
	
	
	
	// --> returns true, if enemy can not make any more moves:
	public boolean isWonByPatt(Board board, Player player) {
		
		for(Figur figure : enemyFigures(board, player)) {
			if(!figure.getPossibleMoves(board, player).isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	
	// --> returns true, if enemy is in check-mate:
	public boolean isWonByCheckMate(Board board, Player player) {
		
		General enemyGeneral = getEnemyGeneral(player);
		
		Player enemyPlayer = redPlayer;
		
		if (isRedNext()) {
			enemyPlayer = blackPlayer;
		}
		
		for(Figur figure : enemyFigures(board, player)) {
			
			ArrayList<Move> moves = figure.getPossibleMoves(board, enemyPlayer);
			
			for(Move move: moves) {
				if(!enemyGeneral.isCheck(board, move) && !enemyGeneral.isThreatened(board, move)) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	// --> sets other player as nextPlayer:
	public void nextTurn(Player player) {
				
		if(player.getUser().getId().equals(this.redPlayer.getUser().getId())) {
			this.setNextPlayer(this.blackPlayer);
		}
		else{
			this.setNextPlayer(this.redPlayer);
		}
	}

	
	
	// --> checks, if possibleMoves contains move:
	public boolean containsMove(ArrayList<Move> possibleMoves, Move move) {
		
		for(Move m : possibleMoves) {
			
			if(m.getMove().equals(move.getMove())) {
				return true;
			}
		}	
		return false;
	}

	
	
	@Override
	public boolean tryMove(String moveString, Player player) {
		
		if(cheatCheck(moveString, player)) {
			
			Move move = new Move(moveString, this.board.getBoardState(), player);
			
			// calculates position:
			Position position = new Position("9876543210".indexOf(moveString.charAt(1)),"abcdefghi".indexOf(moveString.charAt(0))); 
			
			// gets playing-piece on position:
			Figur figur = this.board.getFigurFromBoard(position);
			
			// gets possible moves for playing-piece:
			ArrayList<Move> possibleMoves = figur.getPossibleMoves(board, player);
			General general = board.getFriendGeneral(position);
			
			// checks whether move is possible:
			if(containsMove(possibleMoves, move)) {
				// checks whether general is in-check:
				if(!general.isCheck(board, move) && !general.isThreatened(board, move)) {
					
					// executes move, updates boardMatrix, boardString and history:
					doMove(move, figur);	
					// finishes and sets winner if game is won:
					if(isWonByCheckMate(board,player) || isWonByPatt(board,player)) {
						regularGameEnd(player);
					}
					// sets other player as nextPlayer:
					nextTurn(player);
					
					return true;		
				}
			}
			
		}			
		return false;
	}

	
	public static void main(String[] args) {

		
		XiangqiGame game = new XiangqiGame();
		
		game.setBoard("3ege3/9/3Rs4/9/9/9/9/9/9/4G4");
		Player player1 = new Player(new User("Denis", "1"), game);
		Player player2 = new Player(new User("Daniil", "2"), game);
		game.addPlayer(player1);
		game.addPlayer(player2);
		
		
		game.tryMove("d7-e7", player1);
		game.tryMove("f9-e8", player2); 
		game.tryMove("g6-g5", player2);
		
	}
}
