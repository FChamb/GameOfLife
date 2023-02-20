import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Graphics;

public class Grid {

    public static final Color GRID_COLOUR = new Color(63,63,63);

    private CellStates cell_states;

    private int width, height;
    private int cell_width, cell_height;

    private byte[][] cells;

    public boolean draw_grid;


    public Grid(int width, int height, int cell_width, int cell_height) {
        this.width = width; this.height = height;
        this.cell_width = cell_width; this.cell_height = cell_height;

        cell_states = new CellStates();

        cells = new byte[height][width];
        draw_grid = true;
    }


    public void setState(int x, int y, byte state) {
        x /= cell_width;
        y /= cell_height;

        cells[y][x] = state;
    }
    public void toggleState(int x, int y) {
        x /= cell_width;
        y /= cell_height;

        if(cells[y][x] == 0) cells[y][x] = 1;
        else                 cells[y][x] = 0;
    }

    public void randomise() {
        cells = new byte[height][width];
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
                cells[y][x] = (byte)(Math.random()*2);
    }
    public void clear() {
        cells = new byte[height][width];
    }


    public int[] countNeighbours(int x, int y) {
        int i,j,k,l, neighbours[] = new int[256];
        for(j = -1; j < 2; j++) {
            for(i = -1; i < 2; i++) {
                if(i == 0 && j == 0) continue;
                
                k = (i+x) %  width; while(k < 0) k +=  width;
                l = (j+y) % height; while(l < 0) l += height;

                neighbours[cells[l][k]]++;
            }
        }

        return neighbours;
    }

    public void update() {
        byte[][] buffer = new byte[height][width];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                buffer[y][x] = cell_states.newState(cells[y][x], countNeighbours(x,y));
            }
        }

        cells = buffer;
    }


    public void draw(Graphics graphics) {
        // draw cells
        byte cell;
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                cell = cells[y][x];
                graphics.setColor(cell_states.colours[cell]);
                graphics.fillRect(x*cell_width, y*cell_height, width*cell_width, height*cell_height);
            }
        }

        if(!draw_grid) return;
        // draw grid
        graphics.setColor(GRID_COLOUR);
        for(int y = 0; y < height; y++) {
            graphics.drawLine(           0, y*cell_height, width*cell_width,      y*cell_height);
        }
        graphics.drawLine(0, height*cell_height - 1, width*cell_width, height*cell_height - 1);
        for(int x = 0; x < width; x++) {
            graphics.drawLine(x*cell_width,             0,     x*cell_width, height*cell_height);
        }
        graphics.drawLine(width*cell_width - 1, 0, width*cell_width-1, height*cell_height);
    }
}