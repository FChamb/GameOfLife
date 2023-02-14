import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    private Cell[][] board;

    public Board() {
        this.board = new Cell[50][50];
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                this.board[i][j] = new Cell();
            }
        }
    }

    public Board(int rows, int cols) {
        this.board = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.board[i][j] = new Cell();
            }
        }
    }

    public Board(String file) {
        ArrayList<String[]> boardTemp = new ArrayList<>();
        int rows = 0;
        int cols = 0;
        try {
            Scanner fileRead = new Scanner(new File(file));
            while (fileRead.hasNext()) {
                String line = fileRead.nextLine();
                String[] rowVals = new String[line.length()];
                cols = 0;
                for (int i = 0; i < line.length(); i++) {
                    rowVals[i] = String.valueOf(line.charAt(i));
                    cols++;
                }
                boardTemp.add(rowVals);
                rows++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("That game state does not exist: " + e.getMessage());
        }
        this.board = new Cell[rows][cols];
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j++) {
                if (boardTemp.get(i)[j].equals(".")) {
                    this.board[i][j] = new Cell();
                } else {
                    this.board[i][j] = new Cell(true);
                }
            }
        }
    }

    public Cell[][] getBoard() {
        return this.board;
    }

    public String toString() {
        String board = "";
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                board += this.board[i][j].toString();
            }
            if (i != this.board.length - 1) {
                board += "\n";
            }
        }
        return board;
    }

    /**
     * A testing method to see how the board works in terminal view.
     * @param args - the command line arguments, a game state
     */
    public static void main(String args[]) {
        Board test = new Board(args[0]);
        System.out.println(test);
    }
}
