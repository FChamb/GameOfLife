import main.Game;

public class GameOfLife {
    public static void main(String[] args) {
        new Thread(new Game(900,900)).start();
    }
}