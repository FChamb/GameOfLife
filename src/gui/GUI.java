package gui;

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
        buttons = new Button[3];

        buttons[0] = new Button(asset_path, Button.Type.STOP        ,  100, 750);
        buttons[1] = new Button(asset_path, Button.Type.PLAY        , 150, 750);
        buttons[2] = new Button(asset_path, Button.Type.FAST_FORWARD, 200, 750);
    }


    public void draw(Graphics graphics) {
        graphics.drawImage(case_img, 0, 0, null);

        for(Button b : buttons) b.draw(graphics);
    }
}