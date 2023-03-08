package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import classes.Grid;
import gui.GUI;

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

    private GUI gui;

    private int tick;
    private int ups;


    public Game(int width, int height) {
        this.width = width; this.height = height;

        mouse = new Mouse(); keyboard = new Keyboard();

        display = new Display(width, height, mouse, keyboard);
        fps = 60;


        // grid_width = 500; grid_height = 500;
        // grid_x = 100; grid_y = 100; grid_w = 50; grid_h = 50;
        grid_width = 500; grid_height = 500;
        grid_x = 100; grid_y = 100; grid_w = 50; grid_h = 50;
        cell_w = grid_width/grid_w; cell_h = grid_height/grid_h;
        grid = new Grid(grid_x, grid_y, grid_w, grid_h, cell_w, cell_h);

        sellectedState = (byte)1;
        active = false;

        try {
            gui = new GUI("./assets");
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        tick = 0; ups = 10;
        // fpu = (int)(fps / ups);
    }


    public void update() {
        if(keyboard.isPressed(KeyEvent.VK_ESCAPE)) System.exit(0);

        tick++;
        if(tick % fps == 0) tick = 0;



        if(keyboard.isClicked(KeyEvent.VK_SPACE)) {
            active = !active;
            // grid.update();
        }
        if(keyboard.isClicked(KeyEvent.VK_UP)) {
            ups++; if(ups > fps) ups = (int)fps;
        }
        if(keyboard.isClicked(KeyEvent.VK_DOWN)) {
            ups--; if(ups < 1) ups = 1;
        }
        if(keyboard.isClicked(KeyEvent.VK_RIGHT)) {
            grid.update();
        }

        if(keyboard.isClicked(KeyEvent.VK_R)) {
            grid.randomise();
        }
        if(keyboard.isClicked(KeyEvent.VK_C)) {
            grid.clear();
        }
        if(keyboard.isClicked(KeyEvent.VK_G)) {
            grid.draw_grid = !grid.draw_grid;
        }
        if(keyboard.ctrl() && keyboard.isClicked(KeyEvent.VK_S)) {
            saveGame();
        }
        if(keyboard.ctrl() && keyboard.isClicked(KeyEvent.VK_O)) {
            loadGame();
        }
        if (keyboard.ctrl() && keyboard.isClicked(KeyEvent.VK_H)) {
            updateGame();
        }

        if(mouse.isPressed(MouseEvent.BUTTON1) && mouse.onScreen()) {
            Point location = mouse.getLocation();
            grid.setState(location.x, location.y, sellectedState);
        }
        else
        if(mouse.isPressed(MouseEvent.BUTTON3) && mouse.onScreen()) {
            Point location = mouse.getLocation();
            grid.setState(location.x, location.y, (byte)0);
        }
        else
        if(mouse.isPressed(MouseEvent.BUTTON2) && mouse.onScreen()) {
            Point location = mouse.getLocation();
            grid.toggleState(location.x, location.y);
        }


        if(active && tick % (int)(fps/ups) == 0) grid.update();
    }

    public void draw(Graphics graphics) {

        gui.draw(graphics);

        grid.draw(graphics);

    }

    public void saveGame() {
        final String[] fileName = {"default.gol"};
        JFrame saveGamePopUP= new JFrame("Save Game");
        saveGamePopUP.setLayout(new FlowLayout());
        JLabel prompt = new JLabel("Enter File Name:");
        saveGamePopUP.setSize(new Dimension(150, 150));
        JTextField userInput = new JTextField("default.gol");
        JButton save = new JButton("Enter");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[0] = userInput.getText();
                if (fileName[0].contains(".gol")) {
                    grid.save(fileName[0]);
                } else {
                    grid.save(fileName[0] + ".gol");
                }
                saveGamePopUP.dispose();
            }
        });
        saveGamePopUP.add(prompt);
        saveGamePopUP.add(userInput);
        saveGamePopUP.add(save);
        saveGamePopUP.setVisible(true);
    }

    public void loadGame() {
        final String[] fileName = {"default.gol"};
        JFrame saveGamePopUP= new JFrame("Load Save File");
        saveGamePopUP.setLayout(new FlowLayout());
        JLabel prompt = new JLabel("Enter File Name:");
        saveGamePopUP.setSize(new Dimension(150, 150));
        JTextField userInput = new JTextField("default.gol");
        JButton save = new JButton("Load Game");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[0] = userInput.getText();
                if (fileName[0].contains(".gol")) {
                    grid.load(fileName[0]);
                } else {
                    grid.load(fileName[0] + ".gol");
                }
                saveGamePopUP.dispose();
            }
        });
        saveGamePopUP.add(prompt);
        saveGamePopUP.add(userInput);
        saveGamePopUP.add(save);
        saveGamePopUP.setVisible(true);
    }

    public void updateGame() {
        JFrame saveGamePopUP= new JFrame("Change Game Rules");
        saveGamePopUP.setLayout(new FlowLayout());
        saveGamePopUP.setSize(new Dimension(250, 200));
        JLabel xLabel = new JLabel("Game Rule X:");
        JLabel yLabel = new JLabel("Game Rule Y:");
        JLabel zLabel = new JLabel("Game Rule Z:");
        Integer[] comboBoxChoices = {0,1,2,3,4,5,6,7,8,9,10};
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






    @Override
    public void run() {
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

    private void render() {
        display.generateGraphics(this);
    }
}