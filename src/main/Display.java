package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class Display extends JFrame {

    private int width, height;

    private Mouse mouse;
    private Keyboard keyboard;

    private Canvas canvas;

    public Display(int width, int height, Mouse mouse, Keyboard keyboard) {
        // ----- INITIALISE VARIABLES -----
        this.width = width; this.height = height;
        this.mouse = mouse; this.keyboard = keyboard;
        
        // ----- CONFIGURE JFRAME -----
        setTitle("Game of Life");
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create Canvas
        canvas = new Canvas();
        canvas.setSize(width, height);
        canvas.setFocusable(false);
        canvas.addMouseListener(mouse);
        canvas.addMouseMotionListener(mouse);
        canvas.addKeyListener(keyboard);

        // Create the Menu Bar
        JMenuBar myMenuBar = new JMenuBar();
        setJMenuBar(myMenuBar);

        // Create File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem openSaveItem = new JMenuItem("Open Save...");
        JMenuItem overwriteSaveItem = new JMenuItem("Save");
        JMenuItem createNewSaveItem = new JMenuItem("Save As...");
        fileMenu.add(openSaveItem);
        fileMenu.add(overwriteSaveItem);
        fileMenu.add(createNewSaveItem);
        myMenuBar.add(fileMenu);

        // Create Game Menu
        JMenu gameMenu = new JMenu("Game");
        JMenuItem playItem = new JMenuItem("Play");
        JMenuItem pauseItem = new JMenuItem("Pause");
        JMenuItem stepItem = new JMenuItem("Step...");
        JMenuItem fastForwardItem = new JMenuItem("Fast Forward");
        JMenuItem rewindItem = new JMenuItem("Rewind");
        gameMenu.add(playItem);
        gameMenu.add(pauseItem);
        gameMenu.add(stepItem);
        gameMenu.add(fastForwardItem);
        gameMenu.add(rewindItem);
        myMenuBar.add(gameMenu);

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