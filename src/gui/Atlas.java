package gui;

public enum Atlas {
               DEFAULT(  0,206,50, 50),
 
 
               PLAY_UP(  0,   0,  45, 103),
             PLAY_DOWN(  0, 103,  45, 103),
 
               STOP_UP( 45,   0,  45, 103),
             STOP_DOWN( 45, 103,  45, 103),
 
       FAST_FORWARD_UP( 90,   0,  45, 103),
     FAST_FORWARD_DOWN( 90, 103,  45, 103),
 
             REWIND_UP(135,   0,  45, 103),
           REWIND_DOWN(135, 103,  45, 103),
 
               STEP_UP(180,   0,  45, 103),
             STEP_DOWN(180, 103,  45, 103),
 
 
              EJECT_UP(225,   0,  51, 103),
            EJECT_DOWN(225, 103,  51, 103),
 
              ADMIT_UP(276,   0,  51, 103),
            ADMIT_DOWN(276, 103,  51, 103),
 
 
          PLAY_MESSAGE(327,   0,  64,  44),
          STOP_MESSAGE(327,  44,  64,  44),
          STEP_MESSAGE(327,  88,  64,  44),
  FAST_FORWARD_MESSAGE(327, 132, 128,  36),
        REWIND_MESSAGE(391,   0,  64,  44),
         EJECT_MESSAGE(391,  44,  64,  44),
         ADMIT_MESSAGE(391,  88,  64,  44),


               WHEEL_1(327, 168,  73,  15),
               WHEEL_2(327, 183,  73,  15),
               WHEEL_3(327, 198,  73,  15),
               WHEEL_4(327, 213,  73,  15)
  ;



  public final int x,y,w,h;

  private Atlas(int x, int y, int w, int h) {
      this.x = x; this.y = y; this.w = w; this.h = h;
  }
}