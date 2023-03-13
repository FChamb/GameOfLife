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


    public GUI(String asset_path) throws IOException {
        this.asset_path = asset_path;

        case_img = ImageIO.read(new File(asset_path, "case.png"));
        constructButtons();
    }

    private void constructButtons() throws IOException {
        buttons = new Button[7];

        buttons[0] = new Button(asset_path, Button.Type.REWIND      ,  50, 675, 2);
        buttons[1] = new Button(asset_path, Button.Type.STOP        , 150, 675, 2);
        buttons[2] = new Button(asset_path, Button.Type.PLAY        , 250, 675, 2);
        buttons[3] = new Button(asset_path, Button.Type.STEP        , 350, 675, 2);
        buttons[4] = new Button(asset_path, Button.Type.FAST_FORWARD, 450, 675, 2);
        buttons[5] = new Button(asset_path, Button.Type.EJECT       , 600, 675, 2);
        buttons[6] = new Button(asset_path, Button.Type.ADMIT       , 712, 675, 2);

        buttons[1].setFrame(1);
    }


    public void commitAction(Game game, Button.Type type) {
        for(Button b : buttons)
            if(b != null && b.getType() == type)
                b.setFrame(1);
        
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
                game.getGrid().update50();
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
        int x, y;
        if(mouse.onScreen() && loc != null) {
            x = loc.x; y = loc.y;
            for(int b = 0; b < buttons.length; b++) {
                if(buttons[b] == null) continue;

                button = buttons[b]; bx = button.getX(); by = button.getY(); bs = button.getScale();
                type = button.getType();
                area = button.getCurrentFrameSize();
                if(x >= bx && y >= by && x < bx+area.w*bs && y < by+area.h*bs) {
                    if(mouse.isPressed(MouseEvent.BUTTON1)) {
                        if(mouse.isClicked(MouseEvent.BUTTON1))
                            commitAction(game, type);
                    } else if(!type.sticky) {
                        button.setFrame(0);
                    }
                } else {
                    if(type.sticky) continue;
                    button.setFrame(0);
                }
            }
        }
    }


    public void draw(Graphics graphics) {
        graphics.drawImage(case_img, 0, 0, null);

        for(Button b : buttons) if(b != null) b.draw(graphics);
    }
}