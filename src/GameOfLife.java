import main.Game;

/**
 * This class is the starting point for the game of life. Calling this method starts a new Thread which calls to
 * create a new Game object with starting dimensions of 900 by 900.
 */
public class GameOfLife {
    public static void main(String[] args) {
        new Thread(new Game(900,900)).start();
    }
}