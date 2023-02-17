import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class BoardGUI {
    private JFrame gameFrame;
    private JPanel grid;
    private Board board;
    private JButton[][] buttons;

    public BoardGUI(Board board) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        gameFrame = new JFrame();
        this.board = board;
        gameFrame.setTitle("Game of Life");
        gameFrame.setSize(new Dimension(screen.width, screen.height - gameFrame.getInsets().top));
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        grid = new JPanel();
        grid.setLayout(new GridLayout(this.board.getWidth(), this.board.getHeight()));
        this.buttons = new JButton[this.board.getWidth()][this.board.getHeight()];
        for (int i = 0; i < this.board.getWidth(); i++) {
            for (int j = 0; j < this.board.getHeight(); j++) {
                final int row = i;
                final int col = j;
                buttons[i][j] = new JButton();
                if (this.board.getBoard()[i][j].isAlive()) {
                    buttons[i][j].setBackground(Color.WHITE);
                } else {
                    buttons[i][j].setBackground(Color.BLACK);
                }
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        changeCell(row, col);
                        board.changeCell(row, col);
                    }
                });
                grid.add(buttons[i][j]);
            }
        }
        grid.setBorder(new EmptyBorder(100, 100, 100, 100));
        gameFrame.add(grid);
        gameFrame.setVisible(true);
    }

    public void changeCell(int row, int col) {
        if (this.board.getBoard()[row][col].isAlive()) {
            this.buttons[row][col].setBackground(Color.WHITE);
        } else {
            this.buttons[row][col].setBackground(Color.BLACK);
        }
    }
}
