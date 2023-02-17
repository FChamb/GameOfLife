import javax.swing.*;
import java.awt.*;

public class BoardGUI {
    private JFrame gameFrame;
    private JMenu gameMenu;
    private Board board;

    public BoardGUI(Board board) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        gameFrame = new JFrame();
        this.board = board;
        gameFrame.setTitle("Game of Life");
        gameFrame.setSize(screen);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(new GridLayout(this.board.getWidth(), this.board.getHeight()));
        //JPanel frame = new JPanel();
        //gameFrame.add(frame, BorderLayout.SOUTH);
        addButtons(gameFrame);
        gameFrame.setVisible(true);
    }

    public void addButtons(JFrame grid) {
        JButton button;
        for (int i = 0; i < this.board.getWidth(); i++) {
            for (int j = 0; j < this.board.getHeight(); j++) {
                button = new JButton();
                button.setSize(new Dimension(10, 10));
                button.setOpaque(true);
                //button.setBorderPainted(false);
                if (this.board.getBoard()[i][j].isAlive()) {
                    button.setBackground(Color.WHITE);
                } else {
                    button.setBackground(Color.BLACK);
                }
                grid.add(button);
            }
        }
    }
}
