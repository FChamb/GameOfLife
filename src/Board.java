import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    private Cell[][] board;
    private int x = 2;
    private int y = 3;

    public Board() {
        this.board = new Cell[50][50];
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                this.board[i][j] = new Cell();
            }
        }
    }

    public Board(int rows, int cols, int x, int y) {
        this.board = new Cell[rows][cols];
        this.x = x;
        this.y = y;
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
                if (line.startsWith("#:")) {
                    String[] comments = line.split(" ");
                    this.x = Integer.parseInt(comments[1]);
                    this.y = Integer.parseInt(comments[2]);
                } else if (line.startsWith(".") || line.startsWith("o")){
                    String[] rowVals = new String[line.length()];
                    cols = 0;
                    for (int i = 0; i < line.length(); i++) {
                        rowVals[i] = String.valueOf(line.charAt(i));
                        cols++;
                    }
                    boardTemp.add(rowVals);
                    rows++;
                }
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

    public int countNeighbors(int row, int col) {
        int up, down, left, right;
        if (row > 0) {
            up = row - 1;
        } else  {
            up = this.board.length - 1;
        }
        if (row < (this.board.length - 1)) {
            down = row + 1;
        } else {
            down = 0;
        }
        if (col > 0) {
            left = col - 1;
        } else {
            left = this.board[0].length - 1;
        }
        if (col < (this.board[0].length - 1)) {
            right = col + 1;
        } else {
            right = 0;
        }
        return checkNeighbors(row, col, up, down, left, right);
    }

    public int checkNeighbors(int row, int col, int up, int down, int left, int right) {
        int neighbors = 0;
        if (this.board[up][left].isAlive()) {
            neighbors++;
        }
        if (this.board[up][col].isAlive()) {
            neighbors++;
        }
        if (this.board[up][right].isAlive()) {
            neighbors++;
        }
        if (this.board[row][left].isAlive()) {
            neighbors++;
        }
        if (this.board[row][right].isAlive()) {
            neighbors++;
        }
        if (this.board[down][left].isAlive()) {
            neighbors++;
        }
        if (this.board[down][col].isAlive()) {
            neighbors++;
        }
        if (this.board[down][right].isAlive()) {
            neighbors++;
        }
        return neighbors;
    }

    public void nextGeneration() {
        Cell[][] tempBoard = new Cell[this.board.length][this.board[0].length];
        for (int i = 0; i < tempBoard.length; i++) {
            for (int j = 0; j < tempBoard[0].length; j++) {
                tempBoard[i][j] = new Cell();
            }
        }
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                Cell check = this.board[i][j];
                boolean isAlive = checkRules(check, countNeighbors(i, j));
                tempBoard[i][j] = new Cell(isAlive);
            }
        }
        this.board = tempBoard;
    }

    public boolean checkRules(Cell check, int neighbors) {
        if (neighbors == this.y || (check.isAlive() && neighbors == this.x)) {
            return true;
        } else {
            return false;
        }
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
    public static void main(String[] args) {
        Board test = new Board(args[0]);
        System.out.println(test);
        test.nextGeneration();
        System.out.println(test);
    }
}
