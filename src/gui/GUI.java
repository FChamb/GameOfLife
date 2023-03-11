package gui;

import classes.Grid;
import main.Game;
import main.Mouse;
import main.Keyboard;

import java.awt.Graphics;
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

        buttons[0] = new Button(asset_path, Button.Type.REWIND      , 100, 750);
        buttons[1] = new Button(asset_path, Button.Type.STOP        , 150, 750);
        buttons[2] = new Button(asset_path, Button.Type.PLAY        , 200, 750);
        buttons[3] = new Button(asset_path, Button.Type.STEP        , 250, 750);
        buttons[4] = new Button(asset_path, Button.Type.FAST_FORWARD, 300, 750);
        buttons[5] = new Button(asset_path, Button.Type.EJECT       , 500, 750);
        buttons[6] = new Button(asset_path, Button.Type.ADMIT       , 556, 750);
    }
    
    public void setState(int x, int y, Game game) {
        System.out.println(x + " " + y);
        Button choice = null;
        for (Button button : buttons) {
            int bX = button.getX();
            int bY = button.getY();
            if (x >= bX && x <= bX + 46 && y >= bY && y <= bY + 103) {
                choice = button;
                break;
            }
        }
        if (choice != null) {
            Button.Type type = choice.getType();
            System.out.println(type);
            switch (type) {
                case REWIND:
                    System.out.println("Rewind");
                    break;
                case STOP:
                    game.setActive(false);
                    break;
                case PLAY:
                    game.setActive(true);
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
            }
        }
    }


    public void update(Mouse mouse, Keyboard keyboard) {
        Point loc = mouse.getLocation();
        int x = loc.x, y = loc.y;

        if()
    }


    public void draw(Graphics graphics) {
        graphics.drawImage(case_img, 0, 0, null);

        for(Button b : buttons) if(b != null) b.draw(graphics);
    }
}