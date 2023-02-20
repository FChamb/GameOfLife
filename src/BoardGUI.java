import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardGUI {
    private JFrame gameFrame;
    private JPanel gridPanel;
    private JPanel optionsPanel;
    private Board board;
    private JButton[][] buttons;

    public BoardGUI(Board board) {
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }

        // Create the Window
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.gameFrame = new JFrame();
        this.board = board;
        this.gridPanel = new JPanel();
        this.optionsPanel = new JPanel();
        this.gameFrame.setTitle("Game of Life");
        this.gameFrame.setSize(new Dimension((int) (screen.width * 0.7), (int) (screen.height * 0.7)));
        this.gameFrame.setResizable(false);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setLayout(new BorderLayout());
        this.gridPanel.setLayout(new GridLayout(this.board.getWidth(), this.board.getHeight()));
        this.buttons = new JButton[this.board.getWidth()][this.board.getHeight()];
        this.gameFrame.setVisible(true);

        createGUI();
    }

    public void run() {
        createGUI();
        JPanel gameMenu = new JPanel();
        gameMenu.setLayout(new FlowLayout());
        JButton play = new JButton("Play");
        JButton pause = new JButton("Pause");
        JButton nextGen = new JButton("Next");
        /**
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (play.getText().equals("Play")) {
                    play.setText("Pause");
                    board.setRunning(true);
                    try {
                        board.play();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    play.setText("Play");
                    board.setRunning(false);
                }
            }
        });
         */
        nextGen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.nextGeneration();
            }
        });
        gameMenu.add(play);
        gameMenu.add(pause);
        gameMenu.add(nextGen);
        gameFrame.add(gameMenu, BorderLayout.SOUTH);
        gameFrame.setVisible(true);
        try {
            this.board.setRunning(true);
            this.board.play();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void createGUI() {
        // Create the grid
        generateGrid();

        // Initialise Panes and Screen Elements
        JSplitPane mySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gridPanel, optionsPanel);
        JLabel xLabel = new JLabel("x:");
        JLabel yLabel = new JLabel("y:");
        JLabel zLabel = new JLabel("z:");
        JButton saveSettingsButton = new JButton("Update Settings");

        // Initialise the Combo Box
        Integer[] comboBoxChoices = {0,1,2,3,4,5,6,7,8};
        JComboBox<Integer> xComboBox = new JComboBox<>(comboBoxChoices);
        JComboBox<Integer> yComboBox = new JComboBox<>(comboBoxChoices);
        JComboBox<Integer> zComboBox = new JComboBox<>(comboBoxChoices);
    }

    public void generateGrid() {
        for (int widthIndex = 0; widthIndex < this.board.getWidth(); widthIndex++) {
            for (int heightIndex = 0; heightIndex < this.board.getHeight(); heightIndex++) {
                final int row = widthIndex;
                final int col = heightIndex;
                this.buttons[widthIndex][heightIndex] = new JButton();
                if (this.board.getBoard()[widthIndex][heightIndex].isAlive()) {
                    this.buttons[widthIndex][heightIndex].setBackground(Color.WHITE);
                } else {
                    this.buttons[widthIndex][heightIndex].setBackground(Color.BLACK);
                }
                this.buttons[widthIndex][heightIndex].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        board.changeCell(row, col);
                        changeCell(row, col);
                    }
                });
                this.gridPanel.add(buttons[widthIndex][heightIndex]);
            }
        }
        this.gameFrame.add(gridPanel, BorderLayout.CENTER);
    }

    public void changeCell(int row, int col) {
        boolean isAlive = this.board.getBoard()[row][col].isAlive();
        if (!isAlive) {
            this.buttons[row][col].setBackground(Color.BLACK);
        } else {
            this.buttons[row][col].setBackground(Color.WHITE);
        }
    }
}
