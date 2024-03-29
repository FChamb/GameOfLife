package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Button {

    public static final byte NORMAL = 0x00;
    public static final byte STICKY = 0x01;
    public static final byte TOGGLE = 0x02;

    
    public enum Type {
                PLAY(STICKY, Atlas.PLAY_UP        , Atlas.PLAY_DOWN        ),
                STOP(STICKY, Atlas.STOP_UP        , Atlas.STOP_DOWN        ),
        FAST_FORWARD(        Atlas.FAST_FORWARD_UP, Atlas.FAST_FORWARD_DOWN),
              REWIND(        Atlas.REWIND_UP      , Atlas.REWIND_DOWN      ),
                STEP(        Atlas.STEP_UP        , Atlas.STEP_DOWN        ),
               EJECT(        Atlas.EJECT_UP       , Atlas.EJECT_DOWN       ),
               ADMIT(        Atlas.ADMIT_UP       , Atlas.ADMIT_DOWN       ),
                CLEAR(       Atlas.PUSH_BUTTON_UP , Atlas.PUSH_BUTTON_DOWN ),
               RANDOM(       Atlas.PUSH_BUTTON_UP , Atlas.PUSH_BUTTON_DOWN ),
        
         GRID_VISIBLE(TOGGLE, Atlas.TOGGLE_SWITCH_LEFT, Atlas.TOGGLE_SWITCH_RIGHT),
        COLOUR_SWITCH(TOGGLE, Atlas.TOGGLE_SWITCH_LEFT, Atlas.TOGGLE_SWITCH_RIGHT)
        ;



        public Atlas[] atlantes;
        public int maps;
        public byte special;


        private Type(              Atlas... atlantes) { this(NORMAL, atlantes); }
        private Type(byte special, Atlas... atlantes) {
            this.atlantes = atlantes;
            maps = atlantes.length;
            this.special = special;
        }
    }


    private String asset_path;

    private Type type;

    private BufferedImage[] sprites;
    public int frames, pointer;

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

        frames = type.maps;

        sprites = new BufferedImage[frames];
        Atlas a; BufferedImage img; Graphics g;
        int sw, sh;
        for(int i = 0; i < frames; i++) {
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

    public Atlas getCurrentFrameSize() {
        return type.atlantes[pointer];
    }


    public void shiftFrame(         ) { shiftFrame(1); }
    public void shiftFrame(int steps) { setFrame(pointer + steps); }
    public void   setFrame(int frame) {
        pointer = frame;
        while(pointer >= frames) pointer -= frames;
        while(pointer <       0) pointer += frames;
        // System.out.println(pointer);
    }


    public void draw(Graphics graphics) {
        graphics.drawImage(sprites[pointer], x, y, null);
    }
}