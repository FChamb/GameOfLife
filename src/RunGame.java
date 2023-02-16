public class RunGame {
    public static void main(String[] args) {
        Board gameBoard = new Board("test.gol");
        boardGUI gameVisual = new boardGUI(gameBoard);
    }
}
