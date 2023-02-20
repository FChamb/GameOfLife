public class GameOfLife {
    public static void main(String[] args) {
        new Thread(new Game(850,850)).start();
    }
}