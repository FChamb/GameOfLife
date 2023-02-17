public class RunGame {
    public static void main(String[] args) {
        Board gameBoard = new Board("test.gol");
        BoardGUI gameVisual = new BoardGUI(gameBoard);
    }
}
