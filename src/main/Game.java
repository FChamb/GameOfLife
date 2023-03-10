package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import classes.Grid;
import gui.GUI;
import gui.Button;

public class Game implements Runnable {

    private int width, height;
    private Mouse mouse;
    private Keyboard keyboard;
    private Display display;

    private double fps;

    private int grid_width, grid_height;
    private int grid_x, grid_y, grid_w, grid_h; 
    private int cell_w, cell_h;
    private Grid grid;

    private byte sellectedState;
    private boolean active;

    private String asset_path;
    private GUI gui;

    private int tick;
    private int ups;


    /**
     * The Game constructor takes two integer parameters and begins setting all the local private variables
     * to the appropriate settings. The grid size is defaulted to a 50 by 50 square and the cell dimension
     * sizes are found by dividing the grid's total width and height by number of columns and rows. The state of
     * play, active, is set to false. Finally, the program takes the assets path and attempts to create a new GUI
     * object with the graphical overlay. At the very end, tick speed and fps are set to default of 60 frames/sec.
     * @param width - An integer variable containing the desired starting width of the game panel.
     * @param height - An integer variable containing the desired starting height of the game panel.
     */
    public Game(int width, int height) {
        // ----- INITIALISE VARIABLES -----
        this.width = width; this.height = height;

        mouse = new Mouse(); keyboard = new Keyboard();

        display = new Display(width, height, this, mouse, keyboard);
        fps = 60;


        grid_width = 500; grid_height = 500;
        grid_x = 100; grid_y = 100; grid_w = 50; grid_h = 50;
        cell_w = grid_width/grid_w; cell_h = grid_height/grid_h;
        grid = new Grid(grid_x, grid_y, grid_w, grid_h, cell_w, cell_h);

        sellectedState = (byte)1;
        active = false;

        // ----- LOAD GUI -----
        asset_path = "./assets";
        try {
            gui = new GUI(asset_path);
        } catch(IOException e) {
            System.out.println("Fatal Error: Failed to load asset files");
            if(!new File(asset_path).isDirectory()) System.out.println("The directory \'"+asset_path+"\' does not exist");
            else                                    System.out.println(e);
            System.exit(0);
        }

        tick = 0; ups = 10;
    }

    /**
     * A setter method which changes the state of the private attribute, active.
     * @param active - A boolean values to decide if the game is in play or paused.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

    /**
     * A getter method which returns the local private grid object i.e. the current state of the game.
     * @return - A Grid object variable with the current state of play.
     */
    public Grid getGrid() {
        return this.grid;
    }

    /**
     * A getter method which returns the local private gui object i.e. the graphical interface of the game.
     * @return - A GUI object variable with the current graphical interface.
     */
    public GUI getGui() {
        return this.gui;
    }

    /**
     * updateGrid takes a new set of values, one for the new desired grid width and one for the
     * new desired grid height. Then the appropriate dimensions for the board are calculated and
     * the physical pixel size of the individual cells are found. The private variables grid is set
     * to a new grid, matching the desired shape and size.
     * @param grid_w - An integer value of the number of cells wide for a new grid.
     * @param grid_h -  An integer value of the number of cell tall for the new grid.
     */
    public void updateGrid(int grid_w, int grid_h) {
        grid_width = 500; grid_height = 500;
        grid_x = 100; grid_y = 100; this.grid_w = grid_w; this.grid_h = grid_h;
        cell_w = grid_width/this.grid_w; cell_h = grid_height/this.grid_h;
        grid = new Grid(grid_x, grid_y, this.grid_w, this.grid_h, cell_w, cell_h);
    }


    /**
     * This method contains a multitude of conditional statements, checking whether the user
     * has clicked or pressed a key button/key. The appropriate action is called when a given
     * input has been registered.
     */
    public void update() {
        // ----- KEYBOARD AND MOUSE REGISTRY -----
        // Keyboard: esc key clicked ends game
        if(keyboard.isPressed(KeyEvent.VK_ESCAPE)) System.exit(0);

        tick++;
        if(tick % fps == 0) tick = 0;


        // Keyboard: space key clicked plays/pauses game
        if(keyboard.isClicked(KeyEvent.VK_SPACE)) {
            // active = !active;
            if(active) {
                gui.commitAction(this, Button.Type.STOP);
            } else {
                gui.commitAction(this, Button.Type.PLAY);
            }
            // grid.update();
        }
        // Keyboard: up arrow clicked increases play speed
        if(keyboard.isClicked(KeyEvent.VK_UP)) {
            ups++; if(ups > fps) ups = (int)fps;
        }
        // Keyboard: down arrow clicked decreases play speed
        if(keyboard.isClicked(KeyEvent.VK_DOWN)) {
            ups--; if(ups < 1) ups = 1;
        }
        // Keyboard: right arrow clicked performs a single generation step
        if(keyboard.isClicked(KeyEvent.VK_RIGHT)) {
            // grid.update();
            gui.commitAction(this, Button.Type.STEP);
        }
        if(keyboard.isClicked(KeyEvent.VK_LEFT)) {
            grid.getPrevious();
        }
        // Keyboard: r key clicked randomises the alive and dead cell on board
        if(keyboard.isClicked(KeyEvent.VK_R)) {
            grid.randomise();
        }
        // Keyboard: c key clicked clears the board
        if(keyboard.isClicked(KeyEvent.VK_C)) {
            grid.clear();
        }
        // Keyboard: g key clicked toggles grid lines
        if(keyboard.isClicked(KeyEvent.VK_G)) {
            grid.draw_grid = !grid.draw_grid;
        }
        // Keyboard: ctrl and s key clicked together open save game menu
        if(keyboard.ctrl() && keyboard.isClicked(KeyEvent.VK_S)) {
            saveGame();
        }
        // Keyboard: ctrl and o key clicked together open load game menu
        if(keyboard.ctrl() && keyboard.isClicked(KeyEvent.VK_O)) {
            loadGame();
        }
        // Keyboard: ctrl and h key clicked together open change game rule menu
        if (keyboard.ctrl() && keyboard.isClicked(KeyEvent.VK_H)) {
            updateGame();
        }
        // Keyboard: ctrl and u key clicked together open change board size menu
        if (keyboard.ctrl() && keyboard.isClicked(KeyEvent.VK_U)) {
            changeGrid();
        }


        // Mouse: left click (plus the option to drag) while mouse is on panel turns dead cells alive
        else
        if(mouse.isPressed(MouseEvent.BUTTON1) && mouse.onScreen()) {
            Point location = mouse.getLocation();
            grid.setState(location.x, location.y, sellectedState);
        }
        // Mouse: middle click (plus option to drag) while mouse is on panel changes alive to dead and vice-versa
        else
        if(mouse.isPressed(MouseEvent.BUTTON3) && mouse.onScreen()) {
            Point location = mouse.getLocation();
            grid.setState(location.x, location.y, (byte)0);
        }
        // Mouse: right click (plus option to drag) while mouse is on panel turns alive cells dead
        else
        if(mouse.isPressed(MouseEvent.BUTTON2) && mouse.onScreen()) {
            Point location = mouse.getLocation();
            grid.toggleState(location.x, location.y);
        }

        // Interact method which passes mouse and keyboard to GUI to enable clicking onscreen buttons
        gui.interact(this, mouse, keyboard);

        // Continuously calls this update method while game is in play.
        if(active && tick % (int)(fps/ups) == 0) grid.update();
    }

    /**
     * The draw method takes a parameter graphic and draws the gui/grid to the main panel.
     * @param graphics - An object which contains the assets and graphics for the GUI overlay.
     */
    public void draw(Graphics graphics) {

        gui.draw(graphics);

        grid.draw(graphics);

    }

    /**
     * Creates a new popup menu which provides the user with a text box to name their current board state
     * as well as write any comments they might choose to add. A save button is created with an action listener
     * that gets the text of filename and the comment and passes them to grid.save. Once the action is complete
     * the menu disappears.
     */
    public void saveGame() {
        final String[] fileName = {"default.gol"};
        JFrame saveGamePopUP= new JFrame("Save Game");
        saveGamePopUP.setLayout(new FlowLayout());
        JLabel prompt = new JLabel("Enter File Name:");
        JLabel prompt2 = new JLabel("Enter Comments:");
        saveGamePopUP.setSize(new Dimension(250, 210));
        saveGamePopUP.setLocationRelativeTo(null);
        JTextField userInput = new JTextField("default.gol");
        JTextArea comments = new JTextArea(5, 18);
        JScrollPane scroll = new JScrollPane(comments, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JButton save = new JButton("Enter");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[0] = userInput.getText();
                String comment = comments.getText();
                if (fileName[0].contains(".gol")) {
                    grid.save(fileName[0], comment);
                } else {
                    grid.save(fileName[0] + ".gol", comment);
                }
                saveGamePopUP.dispose();
            }
        });
        saveGamePopUP.add(prompt);
        saveGamePopUP.add(userInput);
        saveGamePopUP.add(prompt2);
        saveGamePopUP.add(scroll);
        saveGamePopUP.add(save);
        saveGamePopUP.setVisible(true);
    }

    /**
     * Creates a new popup menu which provides the user with a combo box of different filenames. Each
     * filename represents a different save state in the game. A save button is created with an action listener
     * that finds the value selected by the user. Then grid.load is called. Finally, after all the actions have been
     * performed, the menu disappears.
     */
    public void loadGame() {
        File files = new File("savefiles");
        final String[] fileNames = files.list();
        JFrame saveGamePopUP= new JFrame("Load Save File");
        saveGamePopUP.setLayout(new FlowLayout());
        JLabel prompt = new JLabel("Pick Save File:");
        saveGamePopUP.setSize(new Dimension(250, 125));
        saveGamePopUP.setLocationRelativeTo(null);
        JComboBox<String> saves = new JComboBox<>(fileNames);
        JButton save = new JButton("Load Game");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] vals = grid.findLoadWH(saves.getSelectedItem().toString());
                updateGrid(vals[1], vals[0]);
                grid.load(saves.getSelectedItem().toString());
                saveGamePopUP.dispose();
                showComments(saves.getSelectedItem().toString());
            }
        });
        saveGamePopUP.add(prompt);
        saveGamePopUP.add(saves);
        saveGamePopUP.add(save);
        saveGamePopUP.setVisible(true);
    }

    public void showComments(String filename) {
        JFrame comments = new JFrame("Save Comments");
        comments.setLayout(new FlowLayout());
        String lineInput = this.grid.findComments(filename);
        if (lineInput == null || lineInput.length() == 0) {
            return;
        }
        String[] comment = lineInput.split(" ");
        String finalComment = comment[0];
        for (int i = 1; i < comment.length; i++) {
            if (i % 5 == 0) {
                finalComment += "\n" + comment[i];
            } else {
                finalComment += " " + comment[i];
            }
        }
        JTextArea prompt = new JTextArea(finalComment, 5, 18);
        prompt.setEditable(false);
        JScrollPane scroll = new JScrollPane(prompt, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        comments.setSize(new Dimension(250, 150));
        comments.setLocationRelativeTo(null);
        JButton close = new JButton("Exit");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comments.dispose();
            }
        });
        comments.add(scroll);
        comments.add(close);
        comments.setVisible(true);
    }

    /**
     * Creates a new popup menu which provides the user with three combo boxes. Each box is labels as a
     * different game of life game rule: x, y, and z. A save button is created with an action listener
     * that gets the chosen value from the given boxes. grid.getCell_states.updateRules is called which
     * changes the game rules for the current board.
     */
    public void updateGame() {
        JFrame saveGamePopUP= new JFrame("Change Game Rules");
        saveGamePopUP.setLayout(new FlowLayout());
        saveGamePopUP.setSize(new Dimension(150, 250));
        saveGamePopUP.setLocationRelativeTo(null);
        JLabel xLabel = new JLabel("Game Rule X:");
        JLabel yLabel = new JLabel("Game Rule Y:");
        JLabel zLabel = new JLabel("Game Rule Z:");
        Integer[] comboBoxChoices = {0,1,2,3,4,5,6,7,8};
        JComboBox<Integer> xComboBox = new JComboBox<>(comboBoxChoices);
        JComboBox<Integer> yComboBox = new JComboBox<>(comboBoxChoices);
        JComboBox<Integer> zComboBox = new JComboBox<>(comboBoxChoices);
        JButton save = new JButton("Save Rules");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = xComboBox.getSelectedIndex();
                int y = yComboBox.getSelectedIndex();
                int z = zComboBox.getSelectedIndex();
                grid.getCell_states().updateRules(x, y, z);
                saveGamePopUP.dispose();
            }
        });
        saveGamePopUP.add(xLabel);
        saveGamePopUP.add(xComboBox);
        saveGamePopUP.add(yLabel);
        saveGamePopUP.add(yComboBox);
        saveGamePopUP.add(zLabel);
        saveGamePopUP.add(zComboBox);
        saveGamePopUP.add(save);
        saveGamePopUP.setVisible(true);
    }

    /**
     * Resets the game rules back to original Game of Life format.
     */
    public void resetGameRules() {
        this.grid.getCell_states().updateRules(2, 3, 3);
    }

    /**
     * Creates a new popup menu which provides the user with two text box options. The first box takes a user input
     * of a grid width. The second box takes a user input of a grid height. A save button is created with an action
     * listener that gets the two texts and calls updateGrid. After the button has been pressed and actions confirmed
     * the menu disappears.
     */
    public void changeGrid() {
        JFrame saveGamePopUP= new JFrame("Update Board Size");
        saveGamePopUP.setLayout(new FlowLayout());
        JLabel xPrompt = new JLabel("Enter Width:");
        JLabel yPrompt = new JLabel("Enter Height:");
        saveGamePopUP.setSize(new Dimension(150, 150));
        saveGamePopUP.setLocationRelativeTo(null);
        JTextField xUserInput = new JTextField(String.valueOf(this.grid_w));
        JTextField yUserInput = new JTextField(String.valueOf(this.grid_h));
        JButton save = new JButton("Enter");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGrid(Integer.parseInt(xUserInput.getText()), Integer.parseInt(yUserInput.getText()));
                saveGamePopUP.dispose();
            }
        });
        saveGamePopUP.add(xPrompt);
        saveGamePopUP.add(xUserInput);
        saveGamePopUP.add(yPrompt);
        saveGamePopUP.add(yUserInput);
        saveGamePopUP.add(save);
        saveGamePopUP.setVisible(true);
    }


    /**
     * Run is the starting method in Game which uses the chosen fps and tick speed to
     * continuously check for user input on the screen while the current thread
     * is in play. This method also acts as a catalyst for the play/pause functionality of
     * game of life to work.
     */
    @Override
    public void run() {
        // ----- CAPTURE FRAME AND UPDATE RATE -----
        double spf = 1/fps;
        double time_moment = System.currentTimeMillis()/1000.0, time_block = 0;
        while(!Thread.interrupted()) {
            time_block += System.currentTimeMillis()/1000.0 - time_moment;
            time_moment = System.currentTimeMillis()/1000.0;

            if(time_block >= spf) {
                while(time_block >= spf) {
                    time_block -= spf;
                    update();
                }
                render();
            }
        }
    }

    /**
     * Render calls generateGraphics in the display class which uses the assets and stage of the current
     * game to display the proper output on the canvas.
     */
    private void render() {
        display.generateGraphics(this);
    }
}