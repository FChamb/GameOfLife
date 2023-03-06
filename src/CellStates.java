import java.awt.Color;

public class CellStates {

    public Color[] colours = new Color[256];

    // public static final byte[][][] RULES = new byte[][][] {
    //     {
    //         {
    //             ALIVE,              // turn into ALIVE if:
    //             (byte)3, ALIVE      // 3 ALIVE neighbours
    //         }
    //     },
    //     {
    //         {
    //             ALIVE,
    //             (byte)3, 
    //         }
    //     }
    // }


    public CellStates() {
        colours[0] = new Color(  0,  0,  0);
        colours[1] = new Color(255,255,255);
    }


    public byte newState(byte state, int[] neighbours) {
        switch(state){
            case 0:
                if(neighbours[1] == 3)
                    return 1;
            
            case 1:
                if(neighbours[1] < 2 || neighbours[1] > 3)
                    return 0;
            
            
            default:
                return state;
        }
    }
}