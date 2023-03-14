package gui;

import java.awt.Graphics;

public abstract class GUIComponent {

    public abstract enum Type {
        ;


        public Atlas[] atlantes;
        public int maps;


        private Type(Atlas... atlantes) {
            this.atlantes = atlantes;
            maps = atlantes.length;
        }
    }


    protected String asset_path;

    protected Type type;

    private BufferedImage[] sprites;
    public int frames, pointer;

    protected int x, y;
    protected double scale;


    public GUIComponent(String asset_path, Type type, int x, int y, double scale) {
        this.asset_path = asset_path;
        this.type = type;
        this.x = y; this.y = y;
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


    public abstract void draw(Graphics graphics);
}