import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

public class BoardGUI {
    private JFrame gameFrame;
    private JPanel grid;
    private Board board;
    private JButton[][] buttons;

    public BoardGUI(Board board) {
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.gameFrame = new JFrame();
        this.board = board;
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
        JButton play = new JButton("Play");
        JButton nextGen = new JButton("Next");
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (play.getText().equals("Play")) {
                    play.setText("Pause");
                } else {
                    play.setText("Play");
                }
            }
        });
        nextGen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.nextGeneration();
            }
        });
        gameMenu.add(play);
        gameMenu.add(nextGen);
        gameFrame.add(gameMenu, BorderLayout.SOUTH);
        gameFrame.setVisible(true);
    }

    public void nextGen() {
        board.nextGeneration();
        grid.removeAll();
        grid.revalidate();
        grid.repaint();
        generateGrid();
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
                    public void actionPerformed(ActionEvent e) {
                        changeCell(row, col);
                        board.changeCell(row, col);
                    }
                });
                this.grid.add(buttons[i][j]);
            }
        }
        this.gameFrame.add(grid, BorderLayout.CENTER);
    }

    public void changeCell(int row, int col) {
        if (this.buttons[row][col].getBackground().equals(Color.BLACK)) {
            this.buttons[row][col].setBackground(Color.WHITE);
        } else {
            this.buttons[row][col].setBackground(Color.BLACK);
        }
    }
}
