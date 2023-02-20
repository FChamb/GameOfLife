import java.awt.Color;
import java.awt.Graphics;

public class Grid {

    public static final Color GRID_COLOUR = new Color(63,63,63);

    private int width, height;
    private int cell_width, cell_height;


    public Grid(int width, int height, int cell_width, int cell_height) {
        this.width = width; this.height = height;
        this.cell_width = cell_width; this.cell_height = cell_height;
    }


    public void clicked(int x, int y) {
        x /= cell_width;
        y /= cell_height;

        System.out.println(x + " : " + y);
    }


    public void draw(Graphics graphics) {
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