package gui;

public enum Atlas {
    DEFAULT(46,0,50,50),
    
    PLAY_UP(0,0,45,106);



    public final int x,y,w,h;

    private Atlas(int x, int y, int w, int h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
    }
}