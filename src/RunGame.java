public class RunGame {
    public static void main(String[] args) throws InterruptedException {
        Board gameBoard = new Board("test.gol");
        BoardGUI gameVisual = new BoardGUI(gameBoard);
        gameVisual.run();
    }
}
