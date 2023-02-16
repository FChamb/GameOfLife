import javax.swing.*;
import java.awt.*;

public class boardGUI {
    private JFrame gameFrame;
    private JMenu gameMenu;
    public boardGUI(Board board) {
        gameFrame = new JFrame();
        gameFrame.setTitle("Game of Life");
        gameFrame.setSize(new Dimension(1000, 1000));
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
                button = new JButton(i + "," + j);
                button.setPreferredSize(new Dimension(50, 50));
                frame.add(button);
            }
        }
    }
}
