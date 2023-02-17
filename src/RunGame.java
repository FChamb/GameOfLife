public class RunGame {
    public static void main(String[] args) {
        Board gameBoard = new Board("test.gol");
        gameBoard.nextGeneration();
        //BoardGUI gameVisual = new BoardGUI(gameBoard);
        /**
        int i = 0;
        while (i < 100) {
            gameBoard.nextGeneration();
            i++;
        }
         */
    }
}
