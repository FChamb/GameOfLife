package main;

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


    /**
     * The default constructor takes two integer parameters as well as a mouse and a keyboard input.
     * The width and height are used to determine the size of the grid. As this class extends JFrame,
     * it enables default JFrame methods to be called. The display sets the configurations in order to
     * properly render our GUI.
     * @param width - An integer with the size of the width of the current canvas.
     * @param height - An integer with the size of the height of the current canvas.
     * @param mouse - A mouse object which tracks the players mouse placement.
     * @param keyboard - A keyboard object which tracks the players key inputs.
     */
    public Display(int width, int height, Mouse mouse, Keyboard keyboard) {
        // ----- INITIALISE VARIABLES -----
        this.width = width; this.height = height;

        this.mouse = mouse; this.keyboard = keyboard;
        
        // ----- CONFIGURE JFRAME -----
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


    /**
     * generateGraphics takes a game object and draws the overlay. A toolkit is synced to the program
     * to enable fluidity on macOS as well as Linux.
     * @param game - A game object in which the graphics are drawn to.
     */
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