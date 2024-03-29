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

    private  Button[] buttons;
    private   Wheel[] wheels;
    private Message[] messages;

    private final static int BUTTON_INDEX = 0,
                              WHEEL_INDEX = 11;
    private final static int WHEEL_OFFSET = 35;


    private Button.Type mouse_pressed = null;


    /**
     * Default constructor for GUI object. Takes one parameter, a string path to the assets directory which
     * contains all overlays for the canvas. The assets_path is set to the given path and the case
     * object is set to the case.png in assets. Both construct buttons and messages is called which places
     * the onscreen buttons and messages on the panel.
     * @param asset_path
     */
    public GUI(String asset_path) throws IOException {
        this.asset_path = asset_path;
        
        case_img = ImageIO.read(new File(asset_path, "case.png"));
        constructComponents();
    }

    /**
     * Construct buttons takes the private array of buttons and initializes it to a length of 7.
     * Then each button is set with a new Button object that corresponds to the correct function.
     */
    private void constructComponents() throws IOException {
        buttons = new Button[11];

        buttons[0]  = new Button(asset_path, Button.Type.REWIND      ,  50, 675, 2);
        buttons[1]  = new Button(asset_path, Button.Type.STOP        , 150, 675, 2);
        buttons[2]  = new Button(asset_path, Button.Type.PLAY        , 250, 675, 2);
        buttons[3]  = new Button(asset_path, Button.Type.STEP        , 350, 675, 2);
        buttons[4]  = new Button(asset_path, Button.Type.FAST_FORWARD, 450, 675, 2);
        buttons[5]  = new Button(asset_path, Button.Type.EJECT       , 600, 675, 2);
        buttons[6]  = new Button(asset_path, Button.Type.ADMIT       , 712, 675, 2);

        buttons[7]  = new Button(asset_path, Button.Type.GRID_VISIBLE     , 656, 255, 2);
        buttons[8]  = new Button(asset_path, Button.Type.COLOUR_SWITCH    , 756, 255, 2);
        buttons[9]  = new Button(asset_path, Button.Type.CLEAR            , 656, 390, 3);
        buttons[10] = new Button(asset_path, Button.Type.RANDOM           , 756, 390, 3);

        pushButton(Button.Type.STOP);


        wheels = new Wheel[3];

        wheels[0] = new Wheel(asset_path, 654, 100, 2);
        wheels[1] = new Wheel(asset_path, 654, 150, 2);
        wheels[2] = new Wheel(asset_path, 654, 500, 2);


        messages = new Message[14];

        messages[0] = new Message(asset_path, Message.Type.REWIND      ,  31, 579, 2);
        messages[1] = new Message(asset_path, Message.Type.STOP        , 131, 579, 2);
        messages[2] = new Message(asset_path, Message.Type.PLAY        , 231, 579, 2);
        messages[3] = new Message(asset_path, Message.Type.STEP        , 331, 579, 2);
        messages[4] = new Message(asset_path, Message.Type.FAST_FORWARD, 367, 599, 2);
        messages[5] = new Message(asset_path, Message.Type.EJECT       , 587, 579, 2);
        messages[6] = new Message(asset_path, Message.Type.ADMIT       , 699, 579, 2);
        messages[7] = new Message(asset_path, Message.Type.GRID_LINE   , 600, 164, 2);
        messages[8] = new Message(asset_path, Message.Type.SWAP_COLOUR , 700, 164, 2);
        messages[9] = null;
        messages[10] = null;

        messages[11] = null;
        messages[12] = null;
        messages[13] = new Message(asset_path, Message.Type.UPDATE_RATE , 599, 424, 2);
    }


    public void pushButton(Button.Type type) {
        for(Button b : buttons)
            if(b != null && b.getType() == type) {
                if(type.special == Button.TOGGLE)
                    b.setFrame(b.pointer == 1 ? 0 : 1);
                else
                    b.setFrame(1);
            }
    }
    public void releaseButton(Button.Type type) {
        if(mouse_pressed == type) return;

        for(Button b : buttons)
            if(b != null && b.getType() == type) {
                b.setFrame(0);
            }
    }

    private void mousePushButton(Button.Type type) {
        mouse_pressed = type;
        pushButton(type);
    }

    public Button[] getButtons() {
        return this.buttons;
    }

    public void commitAction(Game game, Button.Type type                                  ) { commitAction(game, type, false); }
    public void commitAction(Game game, Button.Type type, boolean ignore_animation_trigger) {
        if(!ignore_animation_trigger) pushButton(type);

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
            case GRID_VISIBLE:
                game.gridLines();
                break;
            case COLOUR_SWITCH:
                game.changeColors();
            case CLEAR:
                game.getGrid().clear();
                break;
            case RANDOM:
                game.getGrid().randomise();
                break;
            default:
                return;
        }
    }

    public void commitWheelAction(Game game, int wheel, int steps) {
        switch(wheel) {
            case 0:
                game.changeHeight(steps);
                break;
            
            case 1:
                game.changeWidth(steps);
                break;
            
            case 2:
                game.changeUpdateRate(steps);
                break;
            
            default:
                return;
        }
    }

    public void interact(Game game, Mouse mouse, Keyboard keyboard) {
        Button button; Button.Type type; int bx, by; double bs;
        Wheel wheel; int wx, wy; double ws;
        Atlas area;

        Point loc = mouse.getLocation();
        int x, y; boolean gotone;
        if(mouse.onScreen() && loc != null) {
            x = loc.x; y = loc.y;

            // check buttons
            gotone = false;
            for(int b = 0; b < buttons.length; b++) {
                if(buttons[b] == null) continue;

                button = buttons[b]; bx = button.getX(); by = button.getY(); bs = button.getScale();
                type = button.getType();
                area = button.getCurrentFrameSize();
                if(x >= bx && y >= by && x < bx+area.w*bs && y < by+area.h*bs) {
                    if(messages[BUTTON_INDEX+b] != null) messages[BUTTON_INDEX+b].show = true;              // show message
                    if(mouse.isPressed(MouseEvent.BUTTON1)) {
                        gotone = true;
                        if(mouse.isClicked(MouseEvent.BUTTON1)) {
                            mousePushButton(type);
                            commitAction(game, type, true);
                        }
                    } else if(type.special == Button.NORMAL)
                        releaseButton(type);
                } else {
                    if(messages[BUTTON_INDEX+b] != null) messages[BUTTON_INDEX+b].show = false;             // hide message
                    if(type.special == Button.NORMAL)
                        releaseButton(type);
                }
            } if(!gotone) mouse_pressed = null;

            // check wheels
            for(int w = 0; w < wheels.length; w++) {
                if(wheels[w] == null) continue;

                wheel = wheels[w]; wx = wheel.getX(); wy = wheel.getY(); ws = wheel.getScale();
                area = wheel.getCurrentFrameSize();
                if(y >= wy && y < wy+area.h*ws) {

                    if(x >= wx              && x < wx+area.w*ws                                                    ) {       // if the mouse clicks on the wheel
                        if(messages[WHEEL_INDEX+w] != null) messages[WHEEL_INDEX+w].show = true;                // show message
                        if(mouse.isPressed(MouseEvent.BUTTON1)) {
                            if(x < wx + (area.w*ws)/2) {
                                commitWheelAction(game, w, -1);
                            } else commitWheelAction(game, w, 1);
                        }
                    }

                    else
                    if(x >= wx-WHEEL_OFFSET && x < wx+area.w*ws+WHEEL_OFFSET && mouse.isClicked(MouseEvent.BUTTON1)) {      // if the mouse clicks next to wheel
                        if(x < wx + (area.w*ws)/2) {
                            commitWheelAction(game, w, -1);
                        } else commitWheelAction(game, w, 1);
                    }

                    else {
                        if(messages[WHEEL_INDEX+w] != null) messages[WHEEL_INDEX+w].show = false;               // hide message
                    }
                } else {
                    if(messages[WHEEL_INDEX+w] != null) messages[WHEEL_INDEX+w].show = false;               // hide message
                }
            }
        }
    }

    public void spinWheel(int wheel           ) { spinWheel(wheel, 1); }
    public void spinWheel(int wheel, int times) {
        wheels[wheel].shiftFrame(times);
    }


    public void draw(Graphics graphics) {
        graphics.drawImage(case_img, 0, 0, null);

        for(Button  b :  buttons) if(b != null) b.draw(graphics);
        for(Wheel   w :   wheels) if(w != null) w.draw(graphics);
        for(Message m : messages) if(m != null) m.draw(graphics);
    }
}