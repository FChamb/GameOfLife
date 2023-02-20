import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    private Cell[][] board;
    private int width = 50;
    private int height = 50;
    private int x = 2;
    private int y = 3;
    private boolean running = false;
    private final BoardGUI gui;

    public Board() {
        this.board = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.board[i][j] = new Cell();
            }
        }
        this.gui = new BoardGUI(this);
    }

    public Board(int rows, int cols, int x, int y) {
        this.width = rows;
        this.height = cols;
        this.board = new Cell[rows][cols];
        this.x = x;
        this.y = y;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.board[i][j] = new Cell();
            }
        }
        this.gui = new BoardGUI(this);
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
        this.width = rows;
        this.height = cols;
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
        this.gui = new BoardGUI(this);
    }

    public Cell[][] getBoard() {
        return this.board;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public BoardGUI getGui() {
        return this.gui;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void changeCell(int row, int col) {
        if (!this.board[row][col].isAlive()) {
            this.board[row][col] = new Cell(true);
        } else {
            this.board[row][col] = new Cell();
        }
    }

    public void saveGameState(String filePath) {
        try {
            FileWriter write = new FileWriter(filePath);
            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j < this.width; j++) {
                    if (this.board[i][j].isAlive()) {
                        write.write("o");
                    } else {
                        write.write(".");
                    }
                }
                write.write("\n");
            }
            write.write("#: " + this.x + " " + this.y);
            write.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public boolean checkRules(Cell check, int neighbors) {
        if (neighbors == this.y || (check.isAlive() && neighbors == this.x)) {
            return true;
        } else {
            return false;
        }
    }

    public void nextGeneration() {
        Cell[][] tempBoard = new Cell[this.board.length][this.board[0].length];
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                Cell check = this.board[i][j];
                boolean isAlive = checkRules(check, countNeighbors(i, j));
                tempBoard[i][j] = new Cell(isAlive);
                this.gui.changeCell(i, j);
            }
        }
        this.board = tempBoard;
    }

    public void play() throws InterruptedException {
        while (running) {
            nextGeneration();
            Thread.sleep(1500);
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
        test.saveGameState("test_nextGen.gol");
    }
}
