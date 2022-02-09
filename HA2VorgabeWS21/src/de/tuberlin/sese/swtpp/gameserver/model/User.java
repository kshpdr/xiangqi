package de.tuberlin.sese.swtpp.gameserver.model;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * This class represents all properties associated with a user in the system.
 * Games are linked via player objects, which hold information of a user
 * regarding a specific game.
 *
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8218752033321831785L;

	/**********************************
	 * Member
	 **********************************/

	// attributes
	protected String displayName;
	protected String name;
	private String pwdhash; // password is saved as a hash value together with the salt
	private byte[] salt; // used to create the password hash

	// associations
	private final LinkedList<Player> activeParticipations = new LinkedList<>();
	private final LinkedList<Player> history = new LinkedList<>();
	private final Statistics stats = new Statistics();

	/**********************************
	 * constructors
	 **********************************/

	public User(String name, String id) {
		this.displayName = name;
		this.name = id;
	}

	/**********************************
	 * Getter/Setter/Helper
	 **********************************/

	public Statistics getStats() {
		return stats;
	}

	public void setName(String name) {
		this.displayName = name;
	}

	public void addParticipation(Player player) {
		activeParticipations.add(player);
	}

	public void finishGame(Player player) {
		activeParticipations.removeFirstOccurrence(player);
		history.add(player);
		// statistics have to be updated every time a game is finished(unfinished games
		// are not considered)
		updateStatistics();
	}

	public LinkedList<Player> getActiveParticipations() {
		return activeParticipations;
	}

	public LinkedList<Player> getHistory() {
		return history;
	}

	public long getNBGamesPlayed() {
		return history.stream().filter(p -> !p.isGameInvalid()).count();
	}

	public String getName() {
		return displayName;
	}

	public String getId() {
		return name;
	}

	public void setId(String id) {
		this.name = id;
	}

	public String getPwdhash() {
		return pwdhash;
	}

	public void setPwdhash(String pwdhash) {
		this.pwdhash = pwdhash;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	public void updateStatistics() {
		int nbTotal=0, nbWon=0, nbLost=0,nbDraw = 0;
		int moves = 0;

		for (Player p : history) {
			if (p.isGameInvalid() || !p.getGame().isFinished())
				continue;

			final Game g = p.getGame();
			nbTotal++;
			moves += g.getMoveCount();
			if (g.isDraw()) {
				nbDraw++;
			} else if (p.isWinner()) {
				nbWon++;
			} else {
				nbLost++;
			}
		}

		stats.numWon = nbWon;
		stats.numLost = nbLost;
		stats.numDraw = nbDraw;
		stats.avgMoves = nbTotal > 0 ? (float) moves / (float) nbTotal : 0.0;
		stats.percentWon = nbTotal > 0 ? (float) nbWon / (float) nbTotal * 100 : 0;
		stats.percentDraw = nbTotal > 0 ? (float) nbDraw / (float) nbTotal * 100 : 0;
		stats.percentLost = nbTotal > 0 ? (float) nbLost / (float) nbTotal * 100 : 0;

	}

}
