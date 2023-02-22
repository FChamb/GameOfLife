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
    private JPanel gameMenu;
    private Board board;
    private ArrayList<Cell[][]> previousBoards = new ArrayList<>();
    private int count = 0;
    private JButton[][] buttons;
    private boolean running = false;
    private int tickSpeed = 500;

    private void createGameSettings() {
        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));
        JLabel xLabel = new JLabel("Game Rule X:");
        JLabel yzLabel = new JLabel("Game Rule Y & Z:");
        JLabel xAxis = new JLabel("X-Axis:");
        JLabel yAxis = new JLabel("Y-Axis:");
        JButton saveSettingsButton = new JButton("Update Settings");
        Integer[] comboBoxChoices = {0,1,2,3,4,5,6,7,8,9,10};
        Integer[] newComboBoxChoices = new Integer[46];
        for (int i = 0; i < 46; i++) {
            newComboBoxChoices[i] = i + 5;
        }
        JComboBox<Integer> xComboBox = new JComboBox<>(comboBoxChoices);
        settings.add(xLabel);
        settings.add(xComboBox);
        JComboBox<Integer> yzComboBox = new JComboBox<>(comboBoxChoices);
        settings.add(yzLabel);
        settings.add(yzComboBox);
        JComboBox<Integer> xAxisBox = new JComboBox<>(newComboBoxChoices);
        settings.add(xAxis);
        settings.add(xAxisBox);
        JComboBox<Integer> yAxisBox = new JComboBox<>(newComboBoxChoices);
        settings.add(yAxis);
        settings.add(yAxisBox);
        saveSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int xRule = Integer.parseInt(xComboBox.getSelectedItem().toString());
                int yzRule = Integer.parseInt(yzComboBox.getSelectedItem().toString());
                int xAx = Integer.parseInt(xAxisBox.getSelectedItem().toString());
                int yAx = Integer.parseInt(yAxisBox.getSelectedItem().toString());
                saveSettingsButton(xRule, yzRule, xAx, yAx);
            }
        });
        settings.add(saveSettingsButton);
        this.gameFrame.add(settings, BorderLayout.EAST);
    }

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
        createGameSettings();
    }

    public void run() {
        generateGrid();
        this.gameMenu = new JPanel();
        this.gameMenu.setLayout(new FlowLayout());
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
                backGenButton();
            }
        });
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButton(play, backGen, nextGen, reset);
            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetButton();
            }
        });
        nextGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextGenButton();
            }
        });
        speed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                tickSpeed = speed.getValue();
            }
        });
        this.gameMenu.add(backGen);
        this.gameMenu.add(play);
        this.gameMenu.add(nextGen);
        this.gameMenu.add(reset);
        this.gameMenu.add(speed);
        this.gameFrame.add(gameMenu, BorderLayout.SOUTH);
        this.gameFrame.setVisible(true);
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

    public void saveSettingsButton(int xRule, int yzRule, int xAx, int yAx) {
        board = new Board(yAx, xAx, xRule, yzRule);
        gameFrame.remove(grid);
        grid.removeAll();
        grid.setLayout(new GridLayout(board.getWidth(), board.getHeight()));
        buttons = new JButton[board.getWidth()][board.getHeight()];
        generateGrid();
        gameFrame.repaint();
        gameFrame.revalidate();
    }

    public void backGenButton() {
        if (!running) {
            if (count > 0) {
                board.setBoard(previousBoards.get(count - 1));
                if (count > 0) {
                    previousBoards.remove(count);
                    count--;
                }
                updateBoard();
            }
        }
    }

    public void playButton(JButton play, JButton backGen, JButton nextGen, JButton reset) {
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

    public void resetButton() {
        if (!running) {
            board.setBoard(previousBoards.get(0));
            updateBoard();
            Cell[][] first = previousBoards.get(0);
            previousBoards.clear();
            previousBoards.add(first);
            count = 0;
        }
    }

    public void nextGenButton() {
        if (!running) {
            previousBoards.add(board.getBoard());
            board.nextGeneration();
            updateBoard();
            count++;
        }
    }
}
