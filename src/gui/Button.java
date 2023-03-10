package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Button {
    
    public enum Type {
                PLAY(Atlas.PLAY_UP        ),
                STOP(Atlas.STOP_UP        ),
        FAST_FORWARD(Atlas.FAST_FORWARD_UP),
              REWIND(Atlas.REWIND_UP      ),
                STEP(Atlas.STEP_UP        ),
               EJECT(Atlas.EJECT_UP       ),
               ADMIT(Atlas.ADMIT_UP       );



        public Atlas[] atlantes;
        public int maps;


        private Type(Atlas... atlantes) {
            this.atlantes = atlantes;
            maps = atlantes.length;
        }
    }


    private String asset_path;

    private Type type;

    private BufferedImage[] sprites;
    private int pointer;

    private int x, y;
    private double scale;

    private boolean pressed;


    public Button(String asset_path, Type type, int x, int y              ) throws IOException { this(asset_path, type, x, y, 1); }
    public Button(String asset_path, Type type, int x, int y, double scale) throws IOException {
        this.asset_path = asset_path;
        this.type = type;
        this.x = x; this.y = y;
        this.scale = scale;

        pointer = 0;

        extractSprites();
    }

    private void extractSprites() throws IOException {
        BufferedImage atlas = ImageIO.read(new File(asset_path,"atlas.png"));

        int maps = type.maps;

        sprites = new BufferedImage[maps];
        Atlas a; BufferedImage img; Graphics g;
        int sw, sh;
        for(int i = 0; i < maps; i++) {
            a = type.atlantes[i];

            if(scale == 1) {
                sprites[i] = atlas.getSubimage(a.x, a.y, a.w, a.h);
                continue;
            }

            sw = (int)(a.w * scale); sh = (int)(a.h * scale);
            sprites[i] = new BufferedImage(sw, sh, atlas.getType());
            g = sprites[i].createGraphics();

            img = atlas.getSubimage(a.x, a.y, a.w, a.h);
            g.drawImage(img, 0, 0, sw, sh, 0, 0, a.w, a.h, null);

            g.dispose();
        }
    }


    public void draw(Graphics graphics) {
        graphics.drawImage(sprites[pointer], x, y, null);
    }
}