public class RunGame {
    public static void main(String[] args) throws InterruptedException {
        Board gameBoard = new Board();
        BoardGUI gameVisual = gameBoard.getGui();
        gameVisual.run();
    }
}
