public class RunGame {
    public static void main(String[] args) throws InterruptedException {
        Board gameBoard = new Board();
        BoardGUI gameVisual = new BoardGUI(gameBoard);
        gameVisual.run();
    }
}
