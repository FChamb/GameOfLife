import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class Display extends JFrame {

    private int width, height;
    private Canvas canvas;


    public Display(int width, int height) {
        this.width = width; this.height = height;
        
        setTitle("Game of Life");
        setSize(width, height);

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        canvas = new Canvas();
        canvas.setSize(width, height);
        canvas.setFocusable(false);

        add(canvas);
        pack();

        canvas.createBufferStrategy(3);

        setVisible(true);
        setLocationRelativeTo(null);
    }


    public void generateGraphics(Game game) {
        BufferStrategy bs = canvas.getBufferStrategy();
        // if(bs == null) canvas.createBufferStrategy(3);
        // bs = canvas.getBufferStrategy();

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0,width,height);

        game.draw(g);

        g.dispose();
        bs.show();
    }
}