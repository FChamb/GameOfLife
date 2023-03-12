package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class Display extends JFrame{

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
        JMenuItem openSaveItem = new JMenuItem("Open Save");
        JMenuItem createNewSaveItem = new JMenuItem("Save As");
        fileMenu.add(openSaveItem);
        fileMenu.add(createNewSaveItem);
        myMenuBar.add(fileMenu);

        // Create Game Menu
        JMenu gameMenu = new JMenu("Game");
        JMenuItem playItem = new JMenuItem("Play");
        JMenuItem pauseItem = new JMenuItem("Pause");
        JMenuItem stepItem = new JMenuItem("Step");
        JMenuItem fastForwardItem = new JMenuItem("Fast Forward");
        JMenuItem rewindItem = new JMenuItem("Rewind");
        JMenuItem clearGridItem = new JMenuItem("Clear Grid");
        gameMenu.add(playItem);
        gameMenu.add(pauseItem);
        gameMenu.add(stepItem);
        gameMenu.add(fastForwardItem);
        gameMenu.add(rewindItem);
        gameMenu.add(clearGridItem);
        myMenuBar.add(gameMenu);

        // Create Cell Rule SubMenu
        JMenu cellRuleSubMenu = new JMenu("Edit Cell Rules");
        JMenuItem definexyzItem = new JMenuItem("Show x, y, z definitions");
        JMenuItem editxItem = new JMenuItem("Edit x value...");
        JMenuItem edityItem = new JMenuItem("Edit y value...");
        JMenuItem editzItem = new JMenuItem("Edit z value...");
        JMenuItem resetxyzItem = new JMenuItem("Reset x, y, z");
        cellRuleSubMenu.add(definexyzItem);
        cellRuleSubMenu.add(editxItem);
        cellRuleSubMenu.add(edityItem);
        cellRuleSubMenu.add(editzItem);
        cellRuleSubMenu.add(resetxyzItem);

        // Create Edit Rule Menu
        JMenu editRulesMenu = new JMenu("Edit Rules");
        JMenuItem editStepSizeItem = new JMenuItem("Edit Step Size");
        JMenuItem editCellRulesItem = new JMenuItem("Edit Cell Rules");
        JMenuItem editBoardItem = new JMenuItem("Edit Board Size");
        editRulesMenu.add(editStepSizeItem);
        editRulesMenu.add(cellRuleSubMenu);
        editRulesMenu.add(editBoardItem);
        myMenuBar.add(editRulesMenu);

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

    // Implements the functionality for the Menu Bar Items
    class MenuBarActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            switch(event.getActionCommand()){
                case "openSaveItem":
                    // TODO: Add Button Functionality
                    break;
                case "overwriteSaveItem":
                    // TODO: Add Button Functionality
                    break;
                case "createNewSaveItem":
                    // TODO: Add Button Functionality
                    break;
                case "playItem":
                    // TODO: Add Button Functionality
                    break;
                case "pauseItem":
                    // TODO: Add Button Functionality
                    break;
                case "stepItem":
                    // TODO: Add Button Functionality
                    break;
                case "fastForwardItem":
                    // TODO: Add Button Functionality
                    break;
                case "rewindItem":
                    // TODO: Add Button Functionality
                    break;
                case "clearGridItem":
                    // TODO: Add Button Functionality
                    break;
                case "definexyzItem":
                    // TODO: Add Button Functionality
                    break;
                case "editxItem":
                    // TODO: Add Button Functionality
                    break;
                case "edityItem":
                    // TODO: Add Button Functionality
                    break;
                case "editzItem":
                    // TODO: Add Button Functionality
                    break;
                case "resetxyzItem":
                    // TODO: Add Button Functionality
                    break;
                case "editStepSizeItem":
                    // TODO: Add Button Functionality
                    break;
                case "editBoardItem":
                    // TODO: Add Button Functionality
                    break;

            }
        }
    }

    
}