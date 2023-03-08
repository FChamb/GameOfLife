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
        FAST_FORWARD(Atlas.FAST_FORWARD_UP);



        public Atlas[] atlantes;
        public int scale, maps;


        private Type(           Atlas... atlantes) { this(1, atlantes); }
        private Type(int scale, Atlas... atlantes) {
            this.scale = scale;
            this.atlantes = atlantes;
            maps = atlantes.length;
        }
    }


    private String asset_path;

    private Type type;

    private BufferedImage[] sprites;
    private int pointer;

    private int x, y;

    private boolean pressed;


    public Button(String asset_path, Type type, int x, int y) throws IOException {
        this.asset_path = asset_path;
        this.type = type;
        this.x = x; this.y = y;

        pointer = 0;

        extractSprites();
    }

    private void extractSprites() throws IOException {
        BufferedImage atlas = ImageIO.read(new File(asset_path,"atlas.png"));

        int maps = type.maps;

        sprites = new BufferedImage[maps];
        Atlas a;
        for(int i = 0; i < maps; i++) {
            a = type.atlantes[i];

            sprites[i] = atlas.getSubimage(a.x, a.y, a.w, a.h);
        }
    }


    public void draw(Graphics graphics) {
        graphics.drawImage(sprites[pointer], x, y, null);
    }
}