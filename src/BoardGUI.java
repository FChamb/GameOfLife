import javax.swing.*;
import java.awt.*;

public class BoardGUI {
    private JFrame gameFrame;
    private JMenu gameMenu;
    private Board board;

    public BoardGUI(Board board) {
        gameFrame = new JFrame();
        this.board = board;
        gameFrame.setTitle("Game of Life");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(new GridLayout(board.getBoard().length, board.getBoard()[0].length));
        addButtons(gameFrame, board.getBoard().length);
        gameFrame.pack();
        gameFrame.setVisible(true);
    }

    public void addButtons(JFrame frame, int length) {
        JButton button;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                button = new JButton();
                button.setPreferredSize(new Dimension(20, 20));
                button.setOpaque(true);
                button.setBorderPainted(false);
                if (this.board.getBoard()[i][j].isAlive()) {
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.WHITE);
                } else {
                    button.setBackground(Color.BLACK);
                    button.setForeground(Color.BLACK);
                }
                frame.add(button);
            }
        }
    }
}
