package de.tuberlin.sese.swtpp.gameserver.control;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.Statistics;
import de.tuberlin.sese.swtpp.gameserver.model.User;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements all general user-related use cases.
 */
public class UserController {

    // singleton instance of controller
    private static UserController instance = null;

    // associations
    private HashMap<String, User> users;

    /**
     * Singleton
     */
    public static UserController getInstance() {
        if (instance == null)
            instance = new UserController();

        return instance;
    }

    /**
     * private constructor. creates empty map of users
     */
    private UserController() {
        this.users = new HashMap<>();
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    /**
     * Returns true if a user with the given ID exists in the list of users.
     *
     * @param id user id to look for
     * @return true if user with given id exists
     */
    public boolean checkUserExists(String id) {

        return users.containsKey(id);
    }

    /**
     * Checks user credentials.
     *
     * @param id  user id to check
     * @param pwd pwd to check
     * @return true if User/Pwd exist
     */
    public boolean checkUserPwd(String id, String pwd) {
        if (checkUserExists(id)) {
            User u = users.get(id);

            if (u.getId().toLowerCase().equals(id.toLowerCase()))
                return check(pwd, u);
        }

        return false;
    }

    /**
     * Returns user object with given ID or null if it does not exist.
     *
     * @param id user to search for
     * @return user object
     */
    public User findUserByID(String id) {
        if (checkUserExists(id)) {
            return users.get(id);
        }

        return null;
    }

    /**
     * Use Case create user. All data supplied must not be empty. The id is a unique
     * key. The user can not be created if the ID exists already. The created user
     * is returned.
     *
     * @param id   user id to register user with
     * @param name screen name of user
     * @param pwd  pwd to store
     * @return newly created user object
     */
    public synchronized User register(String id, String name, String pwd) {
        if (id == null || name == null || pwd == null || id.equals("") || name.equals("") || pwd.equals(""))
            return null;

        if (checkUserExists(id))
            return null;

        final User u = new User(name, id);
        createSaltedHash(pwd, u);

        users.put(id, u);

        return u;
    }

    /**
     * Returns statistics object for given user
     *
     * @param u Must not be null
     * @return current statistics object for user
     */
    public Statistics getStatistics(User u) {
        return u.getStats();
    }

    /**
     * Creates a list of unfinished games where the given user is associated with a
     * Player object.
     *
     * @param u user to get games for
     * @return list of unfinished games
     */
    public List<Game> getActiveGames(User u) {
        LinkedList<Game> games = new LinkedList<>();
        u.getActiveParticipations().stream().map(Player::getGame).forEach(games::add);
        return games;
    }

    /**
     * Computes a salted hash of given plaintext password and stores password hash
     * and salt to user object
     *
     * @param password plaintext password
     * @param u        user to save hash and salt for
     */
    public void createSaltedHash(String password, User u) {
        final int saltLen = 32;
        try {
            u.setSalt(SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen));
            u.setPwdhash(hash(password, u.getSalt()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether given plaintext password corresponds to a stored salted hash
     * of the password.
     */
    public boolean check(String password, User u) {
        String hashOfInput = hash(password, u.getSalt());
        return hashOfInput.equals(u.getPwdhash());
    }

    /**
     * Creates password hash with given plaintext and salt
     *
     * @param password pwd to hash
     * @param salt     password salt
     * @return salted pwd SHA1 hash
     */
    private static String hash(String password, byte[] salt) {
        final int iterations = 10000;
        final int desiredKeyLen = 128;

        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, desiredKeyLen));
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // should not happen
            e.printStackTrace();
        }
        return "";
    }

}
