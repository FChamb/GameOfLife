package gui;

public enum Atlas {
            DEFAULT(  0,103,50, 50),

            PLAY_UP(  0,  0,45,103),

            STOP_UP( 45,  0,45,103),

    FAST_FORWARD_UP( 90,  0,45,103),

          REWIND_UP(135,  0,45,103),

            STEP_UP(180,  0,45,103)
    ;



    public final int x,y,w,h;

    private Atlas(int x, int y, int w, int h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
    }
}