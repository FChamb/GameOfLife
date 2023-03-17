package classes;

import java.awt.Color;

public class CellStates {

    public Color[] colors = new Color[256];
    private int xRule = 2;
    private int yRule = 3;
    private int zRule = 3;


    public CellStates() {
        colors[0] = Color.WHITE;
        colors[1] = Color.BLACK;
    }

    public void swapColors() {
        if (colors[0] == Color.WHITE) {
            colors[0] = Color.BLACK;
            colors[1] = Color.WHITE;
        } else {
            colors[0] = Color.WHITE;
            colors[1] = Color.BLACK;
        }
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


    /**
     * newState defines the game rules for game of life. By using the number of
     * neigbors, this method can decide if a state should die or be brought to
     * life according to x,y, and z rules.
     * @param state - a specific cell state
     * @param neighbours - an array of the number of neighbors a cell has
     * @return byte - returns the new byte object of the next cell
     */
    public byte newState(byte state, int[] neighbours) {
        switch(state){
            case 0:
                if(neighbours[1] == zRule)
                    return 1;
            
            case 1:
                if(neighbours[1] < xRule || neighbours[1] > yRule)
                    return 0;
            
            
            default:
                return state;
        }
    }
}