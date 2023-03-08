package gui;

public enum Atlas {
    PLAY_UP(0,0,45,106);



    public final int x,y,w,h;

    private Atlas(int x, int y, int w, int h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
    }
}