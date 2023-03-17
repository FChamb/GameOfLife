package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Message {
    
    public enum Type {
                PLAY(Atlas.PLAY_MESSAGE        ),
                STOP(Atlas.STOP_MESSAGE        ),
        FAST_FORWARD(Atlas.FAST_FORWARD_MESSAGE),
              REWIND(Atlas.REWIND_MESSAGE      ),
                STEP(Atlas.STEP_MESSAGE        ),
               EJECT(Atlas.EJECT_MESSAGE       ),
               ADMIT(Atlas.ADMIT_MESSAGE       ),
         UPDATE_RATE(Atlas.UPDATE_RATE_MESSAGE ),
        GRID_LINE(Atlas.GRID_LINE_MESSAGE      ),
        SWAP_COLOUR(Atlas.SWAP_COLOUR_MESSAGE  )
        ;



        public Atlas atlas;


        private Type(Atlas atlas) {
            this.atlas = atlas;
        }
    }


    private String asset_path;

    private Type type;

    private BufferedImage image;

    private int x, y;
    private double scale;

    public boolean show;


    public Message(String asset_path, Type type, int x, int y              ) throws IOException { this(asset_path, type, x, y, 1); }
    public Message(String asset_path, Type type, int x, int y, double scale) throws IOException {
        this.asset_path = asset_path;
        this.type = type;
        this.x = x; this.y = y;
        this.scale = scale;

        show = false;

        extractImage();
    }

    private void extractImage() throws IOException {
        BufferedImage atlas = ImageIO.read(new File(asset_path,"atlas.png"));

        Atlas a = type.atlas;
        if(scale == 1) {
            image = atlas.getSubimage(a.x, a.y, a.w, a.h);
            return;
        }

        int sw = (int)(a.w * scale), sh = (int)(a.h * scale);
        image = new BufferedImage(sw, sh, atlas.getType());
        Graphics g = image.createGraphics();

        BufferedImage img = atlas.getSubimage(a.x, a.y, a.w, a.h);
        g.drawImage(img, 0, 0, sw, sh, 0, 0, a.w, a.h, null);

        g.dispose();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public double getScale() {
        return this.scale;
    }

    public Type getType() {
        return this.type;
    }


    public void draw(Graphics graphics) {
        if(show)
            graphics.drawImage(image, x, y, null);
    }
}