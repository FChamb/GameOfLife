package classes;

import java.awt.Color;

public class CellStates {

    public Color[] colours = new Color[256];
    private int xRule = 2;
    private int yRule = 3;
    private int zRule = 3;

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
        colours[0] = Color.WHITE;
        colours[1] = Color.BLACK;
    }

    public void updateRules(int xRule, int yRule, int zRule) {
        this.xRule = xRule;
        this.yRule = yRule;
        this.zRule = zRule;
    }

    public int getxRule() {
        return this.xRule;
    }

    public int getyRule() {
        return this.yRule;
    }

    public int getzRule() {
        return this.zRule;
    }


    public byte newState(byte state, int[] neighbours) {
        switch(state){
            case 0:
                if(neighbours[1] == yRule)
                    return 1;
            
            case 1:
                if(neighbours[1] < xRule || neighbours[1] > zRule)
                    return 0;
            
            
            default:
                return state;
        }
    }
}