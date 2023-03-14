package gui;

import classes.Grid;
import main.Game;
import main.Mouse;
import main.Keyboard;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GUI {

    private String asset_path;

    private BufferedImage case_img;
    private Button[] buttons;
    private Message[] messages;
    private Button.Type mouse_pressed = null;


    /**
     * Default constructor for GUI object. Takes one parameter, a string path to the assets directory which
     * contains all overlays for the canvas. The assets_path is set to the given path and the case
     * object is set to the case.png in assets. Both construct buttons and messages is called which places
     * the onscreen buttons and messages on the panel.
     * @param asset_path
     */
    public GUI(String asset_path) {
        this.asset_path = asset_path;

        try {
            case_img = ImageIO.read(new File(asset_path, "case.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        constructButtons();
        constructMessages();
    }

    /**
     * Construct buttons takes the private array of buttons and initializes it to a length of 7.
     * Then each button is set with a new Button object that corresponds to the correct function.
     */
    private void constructButtons() {
        buttons = new Button[7];

        try {
            buttons[0] = new Button(asset_path, Button.Type.REWIND, 50, 675, 2);
            buttons[1] = new Button(asset_path, Button.Type.STOP, 150, 675, 2);
            buttons[2] = new Button(asset_path, Button.Type.PLAY, 250, 675, 2);
            buttons[3] = new Button(asset_path, Button.Type.STEP, 350, 675, 2);
            buttons[4] = new Button(asset_path, Button.Type.FAST_FORWARD, 450, 675, 2);
            buttons[5] = new Button(asset_path, Button.Type.EJECT, 600, 675, 2);
            buttons[6] = new Button(asset_path, Button.Type.ADMIT, 712, 675, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pushButton(Button.Type.STOP);
    }

    /**
     * Construct messages takes the private array of messages and initializes it to a length of 7.
     * Then each message is set with a new message object that corresponds to the correct function.
     */
    private void constructMessages() {
        messages = new Message[7];

        try {
            messages[0] = new Message(asset_path, Message.Type.REWIND, 31, 579, 2);
            messages[1] = new Message(asset_path, Message.Type.STOP, 131, 579, 2);
            messages[2] = new Message(asset_path, Message.Type.PLAY, 231, 579, 2);
            messages[3] = new Message(asset_path, Message.Type.STEP, 331, 579, 2);
            messages[4] = new Message(asset_path, Message.Type.FAST_FORWARD, 422, 579, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void pushButton(Button.Type type) {
        for(Button b : buttons)
            if(b != null && b.getType() == type)
                b.setFrame(1);
    }
    public void releaseButton(Button.Type type) {
        // System.out.println(mouse_pressed);
        if(mouse_pressed == type) return;

        for(Button b : buttons)
            if(b != null && b.getType() == type)
                b.setFrame(0);
    }

    private void mousePushButton(Button.Type type) {
        // System.out.println("pressed");
        mouse_pressed = type;
        pushButton(type);
    }

    public void commitAction(Game game, Button.Type type) {
        pushButton(type);
        switch (type) {
            case REWIND:
                game.getGrid().getPrevious();
                break;
            case STOP:
                game.setActive(false);
                buttons[2].setFrame(0);
                break;
            case PLAY:
                game.setActive(true);
                buttons[1].setFrame(0);
                break;
            case STEP:
                game.getGrid().update();
                break;
            case FAST_FORWARD:
                game.getGrid().updateMany();
                break;
            case EJECT:
                game.saveGame();
                break;
            case ADMIT:
                game.loadGame();
                break;
            default:
                return;
        }
    }

    public void interact(Game game, Mouse mouse, Keyboard keyboard) {
        Button button; Button.Type type; Atlas area; int bx, by; double bs;
        Point loc = mouse.getLocation();
        int x, y; boolean gotone;
        if(mouse.onScreen() && loc != null) {
            x = loc.x; y = loc.y;
            gotone = false;
            for(int b = 0; b < buttons.length; b++) {
                if(buttons[b] == null) continue;

                button = buttons[b]; bx = button.getX(); by = button.getY(); bs = button.getScale();
                type = button.getType();
                area = button.getCurrentFrameSize();
                if(x >= bx && y >= by && x < bx+area.w*bs && y < by+area.h*bs) {
                    if(messages[b] != null) messages[b].show = true;            // show message
                    if(mouse.isPressed(MouseEvent.BUTTON1)) {
                        gotone = true;
                        if(mouse.isClicked(MouseEvent.BUTTON1)) {
                            mousePushButton(type);
                            commitAction(game, type);
                        }
                    } else if(!type.sticky)
                        releaseButton(type);
                } else {
                    if(messages[b] != null) messages[b].show = false;           // hide message
                    if(!type.sticky)
                        releaseButton(type);
                }
            } if(!gotone) mouse_pressed = null;
        }
    }


    public void draw(Graphics graphics) {
        graphics.drawImage(case_img, 0, 0, null);

        for(Button  b :  buttons) if(b != null) b.draw(graphics);
        for(Message m : messages) if(m != null) m.draw(graphics);
    }
}