import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

public class BoardGUI {
    private JFrame gameFrame;
    private JPanel grid;
    private Board board;
    private ArrayList<Cell[][]> previousBoards = new ArrayList<>();
    private int count = 0;
    private JButton[][] buttons;
    private boolean running = false;
    private int tickSpeed = 500;

    public BoardGUI(Board board) {
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.gameFrame = new JFrame();
        this.board = board;
        this.previousBoards.add(board.getBoard());
        this.gameFrame.setTitle("Game of Life");
        this.gameFrame.setSize(new Dimension((int) (screen.width * 0.7), (int) (screen.height * 0.7)));
        this.gameFrame.setResizable(false);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setLayout(new BorderLayout());
        this.grid = new JPanel();
        this.grid.setLayout(new GridLayout(this.board.getWidth(), this.board.getHeight()));
        this.buttons = new JButton[this.board.getWidth()][this.board.getHeight()];
    }

    public void run() {
        generateGrid();
        JPanel gameMenu = new JPanel();
        gameMenu.setLayout(new FlowLayout());
        JButton backGen = new JButton("Last");
        backGen.setBackground(Color.CYAN);
        JButton play = new JButton("Play");
        play.setBackground(Color.GREEN);
        JButton nextGen = new JButton("Next");
        nextGen.setBackground(Color.CYAN);
        JButton reset = new JButton("Reset");
        reset.setBackground(Color.PINK);
        JSlider speed = new JSlider(JSlider.HORIZONTAL, 5, 1500, 750);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(1500, new JLabel("Slow"));
        labelTable.put(750, new JLabel("Medium"));
        labelTable.put(5, new JLabel("Fast"));
        speed.setLabelTable(labelTable);
        speed.setPaintLabels(true);
        backGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    if (count >= 0) {
                        board.setBoard(previousBoards.get(count--));
                        previousBoards.remove(count);
                        count--;
                        updateBoard();
                    }
                }
            }
        });
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (play.getText().equals("Play")) {
                    play.setText("Pause");
                    changeRun();
                    backGen.setBackground(Color.DARK_GRAY);
                    play.setBackground(Color.RED);
                    nextGen.setBackground(Color.DARK_GRAY);
                    reset.setBackground(Color.DARK_GRAY);
                } else {
                    play.setText("Play");
                    changeRun();
                    backGen.setBackground(Color.CYAN);
                    play.setBackground(Color.GREEN);
                    nextGen.setBackground(Color.CYAN);
                    reset.setBackground(Color.PINK);
                }
            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    board.setBoard(previousBoards.get(0));
                    updateBoard();
                }
            }
        });
        nextGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    previousBoards.add(board.getBoard());
                    board.nextGeneration();
                    updateBoard();
                    count++;
                }
            }
        });
        speed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                tickSpeed = speed.getValue();
            }
        });
        gameMenu.add(backGen);
        gameMenu.add(play);
        gameMenu.add(nextGen);
        gameMenu.add(reset);
        gameMenu.add(speed);
        gameFrame.add(gameMenu, BorderLayout.SOUTH);
        gameFrame.setVisible(true);
        beginGame();
    }

    public void changeRun() {
        this.running = !this.running;
    }

    public void beginGame() {
        while (true) {
            System.out.print("");
            while (running) {
                previousBoards.add(board.getBoard());
                this.board.nextGeneration();
                updateBoard();
                try {
                    Thread.sleep(this.tickSpeed);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.count++;
            }
        }
    }

    public void generateGrid() {
        for (int i = 0; i < this.board.getWidth(); i++) {
            for (int j = 0; j < this.board.getHeight(); j++) {
                final int row = i;
                final int col = j;
                this.buttons[i][j] = new JButton();
                if (this.board.getBoard()[i][j].isAlive()) {
                    this.buttons[i][j].setBackground(Color.WHITE);
                } else {
                    this.buttons[i][j].setBackground(Color.BLACK);
                }
                this.buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!running) {
                            board.changeCell(row, col);
                            changeButton(row, col);
                        }
                    }
                });
                this.grid.add(buttons[i][j]);
            }
        }
        this.gameFrame.add(grid, BorderLayout.CENTER);
    }

    public void updateBoard() {
        for (int i = 0; i < this.board.getWidth(); i++) {
            for (int j = 0; j < this.board.getHeight(); j++) {
                if (this.board.getBoard()[i][j].isAlive()) {
                    this.buttons[i][j].setBackground(Color.WHITE);
                } else {
                    this.buttons[i][j].setBackground(Color.BLACK);
                }
            }
        }
    }

    public void changeButton(int row, int col) {
        boolean isAlive = this.board.getBoard()[row][col].isAlive();
        if (isAlive) {
            this.buttons[row][col].setBackground(Color.WHITE);
        } else {
            this.buttons[row][col].setBackground(Color.BLACK);
        }
    }
}
