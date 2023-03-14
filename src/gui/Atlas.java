package gui;

public enum Atlas {
              DEFAULT(  0,206,50, 50),


              PLAY_UP(  0,  0,45,103),
            PLAY_DOWN(  0,103,45,103),

              STOP_UP( 45,  0,45,103),
            STOP_DOWN( 45,103,45,103),

      FAST_FORWARD_UP( 90,  0,45,103),
    FAST_FORWARD_DOWN( 90,103,45,103),

            REWIND_UP(135,  0,45,103),
          REWIND_DOWN(135,103,45,103),

              STEP_UP(180,  0,45,103),
            STEP_DOWN(180,103,45,103),


             EJECT_UP(225,  0,51,103),
           EJECT_DOWN(225,103,51,103),

             ADMIT_UP(276,  0,51,103),
           ADMIT_DOWN(276,103,51,103),


         PLAY_MESSAGE(327,  0,64, 44)
    ;



    public final int x,y,w,h;

    private Atlas(int x, int y, int w, int h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
    }
}