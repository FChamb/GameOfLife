public class GameOfLife {
    public static void main(String[] args) {
        new Thread(new Game(500,500)).start();
    }
}