import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

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
        JPanel frame = new JPanel();
        frame.setLayout(new GridLayout(this.board.getWidth(), this.board.getHeight()));
        JButton button;
        for (int i = 0; i < this.board.getWidth(); i++) {
            for (int j = 0; j < this.board.getHeight(); j++) {
                button = new JButton();
                button.setBackground(Color.BLACK);
                frame.add(button);
            }
        }
        frame.setBorder(new EmptyBorder(100, 100, 100, 100));
        gameFrame.add(frame);
        gameFrame.setVisible(true);
    }
}
