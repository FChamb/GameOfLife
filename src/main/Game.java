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
        } catch(Exception e) {
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

    /**
     * A getter method which returns the local private active object i.e. the boolean key which decides
     * if the game is in play.
     * @return - A boolean variable with a true for in play and false otherwise.
     */
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
     * @param new_grid_w - An integer value of the number of cells wide for a new grid.
     * @param new_grid_h -  An integer value of the number of cell tall for the new grid.
     */
    public void updateGrid(int new_grid_w, int new_grid_h) {
        grid_width = 500; grid_height = 500;

        // Resizes the new width and height to a maximum of 250
        if (new_grid_w > 250) {new_grid_w = 250;}
        if (new_grid_h > 250) {new_grid_h = 250;}

        grid_x = 100; grid_y = 100; this.grid_w = new_grid_w; this.grid_h = new_grid_h;
        cell_w = grid_width/this.grid_w; cell_h = grid_height/this.grid_h;
        grid = new Grid(grid_x, grid_y, this.grid_w, this.grid_h, cell_w, cell_h);

        // Automatically turns off the grid lines when above 100 cells in either direction to increase visibility
        if (new_grid_w > 100 || new_grid_h > 100) {
            grid.draw_grid = !grid.draw_grid;
        }
    }


    /**
     * This method contains a multitude of conditional statements, checking whether the user
     * has clicked or pressed a key button/key. The appropriate action is called when a given
     * input has been registered.
     */
    public void update() {
        // Keyboard: esc key clicked ends game
        if(keyboard.isPressed(KeyEvent.VK_ESCAPE)) System.exit(0);

        tick++;
        if(tick % fps == 0) tick = 0;


        // Interact method which passes mouse and keyboard to GUI to enable clicking onscreen buttons
        gui.interact(this, mouse, keyboard);

        // ----- KEYBOARD AND MOUSE REGISTRY -----

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
        if(keyboard.isFlutter(KeyEvent.VK_UP)) {
            ups++; if(ups > fps) ups = (int)fps;
            else gui.spinWheel(0);
        }
        // Keyboard: down arrow clicked decreases play speed
        if(keyboard.isFlutter(KeyEvent.VK_DOWN)) {
            ups--; if(ups < 1) ups = 1;
            else gui.spinWheel(0, -1);
        }

        // Keyboard: right arrow clicked performs a single generation step
        if(!keyboard.shift() && keyboard.isPressed(KeyEvent.VK_RIGHT)) {
            // grid.update();
            gui.pushButton(Button.Type.STEP);
            if(keyboard.isClicked(KeyEvent.VK_RIGHT))
                gui.commitAction(this, Button.Type.STEP);
        } else gui.releaseButton(Button.Type.STEP);
        // Keyboard: shift and right arrow clicked together preforms multiple generation steps
        if(keyboard.shift() && keyboard.isPressed(KeyEvent.VK_RIGHT)) {
            // grid.update();
            gui.pushButton(Button.Type.FAST_FORWARD);
            if(keyboard.isClicked(KeyEvent.VK_RIGHT))
                gui.commitAction(this, Button.Type.FAST_FORWARD);
        } else gui.releaseButton(Button.Type.FAST_FORWARD);

        // Keyboard: left arrow clicked recedes a single generation step
        if(keyboard.isPressed(KeyEvent.VK_LEFT)) {
            gui.pushButton(Button.Type.REWIND);
            if(keyboard.isFlutter(KeyEvent.VK_LEFT))
                grid.getPrevious();
        } else gui.releaseButton(Button.Type.REWIND);


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
        if(keyboard.ctrl() && keyboard.isPressed(KeyEvent.VK_S)) {
            gui.pushButton(Button.Type.EJECT);
            if(keyboard.isClicked(KeyEvent.VK_S))
                saveGame();
        } else gui.releaseButton(Button.Type.EJECT);

        // Keyboard: ctrl and o key clicked together open load game menu
        if(keyboard.ctrl() && keyboard.isPressed(KeyEvent.VK_O)) {
            gui.pushButton(Button.Type.ADMIT);
            if(keyboard.isClicked(KeyEvent.VK_O))
                loadGame();
        } else gui.releaseButton(Button.Type.EJECT);
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

        // Continuously calls this update method while game is in play.
        if(active && tick % (int)(fps/ups) == 0) grid.update();
    }

    /**
     * The draw method takes a parameter graphic and draws the gui/grid to the main panel.
     * @param graphics - An object which contains the assets and graphics for the GUI overlay.
     */
    public void draw(Graphics graphics) {

        grid.draw(graphics);

        gui.draw(graphics);

    }

    /**
     * Creates a new popup menu which provides the user with a text box to name their current board state
     * as well as write any comments they might choose to add. A save button is created with an action listener
     * that gets the text of filename and the comment and passes them to grid.save. Once the action is complete
     * the menu disappears.
     */
    public void saveGame() {
        final String[] fileName = {"default.gol"};
        JFrame saveGamePopUp= new JFrame("Save Game");
        saveGamePopUp.setLayout(new FlowLayout());
        JLabel prompt = new JLabel("Enter File Name:");
        JLabel prompt2 = new JLabel("Enter Comments:");
        saveGamePopUp.setSize(new Dimension(250, 210));
        saveGamePopUp.setLocationRelativeTo(null);
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
                saveGamePopUp.dispose();
            }
        });
        saveGamePopUp.add(prompt);
        saveGamePopUp.add(userInput);
        saveGamePopUp.add(prompt2);
        saveGamePopUp.add(scroll);
        saveGamePopUp.add(save);
        saveGamePopUp.setVisible(true);
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
        JFrame loadGamePopUp= new JFrame("Load Save File");
        loadGamePopUp.setLayout(new FlowLayout());
        JLabel prompt = new JLabel("Pick Save File:");
        loadGamePopUp.setSize(new Dimension(250, 125));
        loadGamePopUp.setLocationRelativeTo(null);
        JComboBox<String> saves = new JComboBox<>(fileNames);
        JButton save = new JButton("Load Game");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] vals = grid.findLoadWH(saves.getSelectedItem().toString());
                updateGrid(vals[1], vals[0]);
                grid.load(saves.getSelectedItem().toString());
                loadGamePopUp.dispose();
                showComments(saves.getSelectedItem().toString());
            }
        });
        loadGamePopUp.add(prompt);
        loadGamePopUp.add(saves);
        loadGamePopUp.add(save);
        loadGamePopUp.setVisible(true);
    }

    /**
     * Show comments takes a filename parameter and displays the comments for a given save file. This
     * method is called immediately after a save state is loaded. It first checks to see if the file
     * contains comments and if it does not the method ends. Otherwise, the comments are split up into
     * lines of five words per and added to a non-editable text field.
     * @param filename - A string value containing the name of the save file.
     */
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
     * updateMany displays a popup menu which a text box that enables the user to change
     * the number of steps the fast-forward button activates. A save button is created with
     * an action listener that gets the chosen value from the given text box. grid.setMany
     * is called which changes the step count for the current grid.
     */
    public void updateMany() {
        JFrame ff = new JFrame("Fast Forward Size");
        ff.setLayout(new FlowLayout());
        JLabel prompt = new JLabel("Fast Forward Size:");
        JTextField text = new JTextField(String.valueOf(grid.getMany()));
        ff.setSize(new Dimension(150, 100));
        ff.setLocationRelativeTo(null);
        JButton close = new JButton("Save");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grid.setMany(Integer.parseInt(text.getText()));
                ff.dispose();
            }
        });
        ff.add(prompt);
        ff.add(text);
        ff.add(close);
        ff.setVisible(true);
    }

    /**
     * Creates a new popup menu which provides the user with the a text area with definitions 
     * of each variable to edit the game rules. Also provides the user with 3 combo boxes, each
     * to edit a different rule: x, y, or z. A save button is created with an action listener
     * that gets the chosen value from the given boxes. grid.getCell_states.updateRules is called
     * which changes the game rules for the current board. Similarly, a reset button is created
     * to put the variables back to default values
     */
    public void updateGame() {
        JFrame changeRulesPopUp = new JFrame("Change Game Rules");
        changeRulesPopUp.setLayout(new FlowLayout());
        changeRulesPopUp.setSize(new Dimension(450, 250));
        changeRulesPopUp.setLocationRelativeTo(null);

        // Creates the text box to show defintions of x, y, z
        JTextArea definitionBox = new JTextArea("Any live cell with fewer than x live neighbours dies.\nAny live cell with x to y live neighbours lives on to the next generation.\nAny live cell with more than y live neighbours dies.\nAny dead cell with exactly z live neighbours becomes a live cell.");
        definitionBox.setEditable(false);

        // Creates the elements to allow user to edit x, y, z variables
        JLabel xLabel = new JLabel("Game Rule x:");
        JLabel yLabel = new JLabel("Game Rule y:");
        JLabel zLabel = new JLabel("Game Rule z:");
        Integer[] comboBoxChoices = {0,1,2,3,4,5,6,7,8};
        JComboBox<Integer> xComboBox = new JComboBox<>(comboBoxChoices);
        JComboBox<Integer> yComboBox = new JComboBox<>(comboBoxChoices);
        JComboBox<Integer> zComboBox = new JComboBox<>(comboBoxChoices);
        JButton save = new JButton("Save Rules");
        JButton reset = new JButton("Reset Rules");
        
        // Functionality for Save button
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = xComboBox.getSelectedIndex();
                int y = yComboBox.getSelectedIndex();
                int z = zComboBox.getSelectedIndex();
                grid.getCell_states().updateRules(x, y, z);
                changeRulesPopUp.dispose();
            }
        });

        // Functionality for Reset button
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = 2; int y = 3; int z = 3;
                xComboBox.setSelectedItem(x);
                yComboBox.setSelectedItem(y);
                zComboBox.setSelectedItem(z);
            }
        });

        // Sets the combo box values to automatically be the current values for x, y, z rules
        xComboBox.setSelectedItem(grid.getCell_states().getxRule());
        yComboBox.setSelectedItem(grid.getCell_states().getyRule());
        zComboBox.setSelectedItem(grid.getCell_states().getzRule());

        changeRulesPopUp.add(definitionBox);
        changeRulesPopUp.add(xLabel);
        changeRulesPopUp.add(xComboBox);
        changeRulesPopUp.add(yLabel);
        changeRulesPopUp.add(yComboBox);
        changeRulesPopUp.add(zLabel);
        changeRulesPopUp.add(zComboBox);
        changeRulesPopUp.add(save);
        changeRulesPopUp.add(reset);
        changeRulesPopUp.setVisible(true);
    }

    /**
     * Creates a new popup menu which provides the user with two text box options. The first box takes a user input
     * of a grid width. The second box takes a user input of a grid height. A save button is created with an action
     * listener that gets the two texts and calls updateGrid. After the button has been pressed and actions confirmed
     * the menu disappears.
     */
    public void changeGrid() {
        JFrame changeGridPopUp = new JFrame("Update Board Size");
        JLabel xPrompt = new JLabel("Enter Width:");
        JLabel yPrompt = new JLabel("Enter Height:");
        JLabel maxPromt = new JLabel("(Max size 250 by 250)");
        JTextField xUserInput = new JTextField(String.valueOf(this.grid_w));
        JTextField yUserInput = new JTextField(String.valueOf(this.grid_h));
        JButton save = new JButton("Enter");

        changeGridPopUp.setLayout(new FlowLayout());
        changeGridPopUp.setSize(new Dimension(150, 150));
        changeGridPopUp.setLocationRelativeTo(null);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int width = Integer.parseInt(xUserInput.getText());
                int height = Integer.parseInt(yUserInput.getText());

                updateGrid(width, height);
                changeGridPopUp.dispose();
            }
        });
        
        changeGridPopUp.add(maxPromt);
        changeGridPopUp.add(xPrompt);
        changeGridPopUp.add(xUserInput);
        changeGridPopUp.add(yPrompt);
        changeGridPopUp.add(yUserInput);
        changeGridPopUp.add(save);
        changeGridPopUp.setVisible(true);
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