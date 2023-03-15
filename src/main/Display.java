package main;

import gui.Button;

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

    private Game game;
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
     
    public Display(int width, int height, Game game, Mouse mouse, Keyboard keyboard) {
        // ----- INITIALISE VARIABLES -----
        this.width = width; this.height = height;
        this.game = game;
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
        openSaveItem.addActionListener(new MenuBarActionListener());
        createNewSaveItem.addActionListener(new MenuBarActionListener());
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
        JMenuItem randomizeItem = new JMenuItem("Randomize");
        JMenuItem clearGridItem = new JMenuItem("Clear Grid");
        playItem.addActionListener(new MenuBarActionListener());
        pauseItem.addActionListener(new MenuBarActionListener());
        stepItem.addActionListener(new MenuBarActionListener());
        fastForwardItem.addActionListener(new MenuBarActionListener());
        rewindItem.addActionListener(new MenuBarActionListener());
        randomizeItem.addActionListener(new MenuBarActionListener());
        clearGridItem.addActionListener(new MenuBarActionListener());
        gameMenu.add(playItem);
        gameMenu.add(pauseItem);
        gameMenu.add(stepItem);
        gameMenu.add(fastForwardItem);
        gameMenu.add(rewindItem);
        gameMenu.add(randomizeItem);
        gameMenu.add(clearGridItem);
        myMenuBar.add(gameMenu);

        // Create Edit Rule Menu
        JMenu editRulesMenu = new JMenu("Edit Rules");
        JMenuItem editStepSizeItem = new JMenuItem("Edit Fast Forward Size");
        JMenuItem changexyzItem = new JMenuItem("Change x, y, z rules");
        JMenuItem editBoardItem = new JMenuItem("Edit Board Size");
        editStepSizeItem.addActionListener(new MenuBarActionListener());
        changexyzItem.addActionListener(new MenuBarActionListener());
        editBoardItem.addActionListener(new MenuBarActionListener());
        editRulesMenu.add(editStepSizeItem);
        editRulesMenu.add(changexyzItem);
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
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        // if(bufferStrategy == null) canvas.createBufferStrategy(3);
        // bufferStrategy = canvas.getBufferStrategy();

        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0,0,width,height);

        game.draw(graphics);

        Toolkit.getDefaultToolkit().sync();
        graphics.dispose();
        bufferStrategy.show();
    }

    /**
     * This class is a different version of an action listener which overrides actionPerformed.
     * The method takes an ActionEvent and uses a switch case to decide which menu item has been
     * selected. The appropriate action is then activated. Some of the options also pause
     * the game to make the board state static, particularly for when saving/opening files
     */
    class MenuBarActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            switch(event.getActionCommand()){
                case "Open Save":
                    game.getGui().commitAction(game, Button.Type.STOP);
                    game.getGui().commitAction(game, Button.Type.ADMIT);
                    break;
                case "Save As":
                    game.getGui().commitAction(game, Button.Type.STOP);
                    game.getGui().commitAction(game, Button.Type.EJECT);
                    break;
                case "Play":
                    game.getGui().commitAction(game, Button.Type.PLAY);
                    break;
                case "Pause":
                    game.getGui().commitAction(game, Button.Type.STOP);
                    break;
                case "Step":
                    game.getGui().commitAction(game, Button.Type.STEP);
                    break;
                case "Fast Forward":
                    game.getGui().commitAction(game, Button.Type.FAST_FORWARD);
                    break;
                case "Rewind":
                    game.getGui().commitAction(game, Button.Type.STOP);
                    game.getGui().commitAction(game, Button.Type.REWIND);
                    break;
                case "Randomize":
                    game.getGrid().randomise();
                    break;
                case "Clear Grid":
                    game.getGrid().clear();
                    break;
                case "Change x, y, z rules":
                    game.getGui().commitAction(game, Button.Type.STOP);
                    game.updateGame();
                    break;
                case "Edit Fast Forward Size":
                    game.updateMany();
                    break;
                case "Edit Board Size":
                    game.changeGrid();
                    break;

            }
        }
    }    
}