import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Display extends JFrame {

    private int width, height;

    private Mouse mouse;
    private Keyboard keyboard;

    private Canvas canvas;


    public Display(int width, int height, Mouse mouse, Keyboard keyboard) {
        this.width = width; this.height = height;

        this.mouse = mouse; this.keyboard = keyboard;
        
        setTitle("Game of Life");
        setSize(width, height);

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        canvas = new Canvas();
        canvas.setSize(width, height);
        canvas.setFocusable(false);
        canvas.addMouseListener(mouse);
        canvas.addMouseMotionListener(mouse);
        canvas.addKeyListener(keyboard);

        add(canvas);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addKeyListener(keyboard);
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

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
        bs.show();
    }
}