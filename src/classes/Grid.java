package classes;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Grid {

    public static final Color GRID_COLOUR = Color.DARK_GRAY;

    private CellStates cell_states;

    private int x, y;
    private int width, height;
    private int cell_width, cell_height;

    private byte[][] cells;

    public boolean draw_grid;


    public Grid(int x, int y, int width, int height, int cell_width, int cell_height) {
        // ----- INITIALISE VARIABLES -----
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.cell_width = cell_width; this.cell_height = cell_height;

        cell_states = new CellStates();

        cells = new byte[height][width];
        draw_grid = true;
    }


    private int fixX(int x) {
        // ----- MAP X-COORDANITE -----
        x -= this.x;
        x /= cell_width;

        return x;
    }
    private int fixY(int y) {
        // ----- MAP Y-COORDANITE -----
        y -= this.y;
        y /= cell_height;

        return y;
    }

    public void setState(int x, int y, byte state) {
        x = fixX(x);
        y = fixY(y);
        if(x < 0 || x >= width || y < 0 || y >= height) return;

        cells[y][x] = state;
    }
    public void toggleState(int x, int y) {
        x = fixX(x);
        y = fixY(y);
        if(x < 0 || x >= width || y < 0 || y >= height) return;

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
        int off_x = x, off_y = y;
        int x,y;

        // ----- DRAW CELLS -----
        byte cell;
        for(int j = 0; j < height; j++) {
            for(int i = 0; i < width; i++) {
                cell = cells[j][i];
                x = off_x + i*cell_width; y = off_y + j*cell_height;

                graphics.setColor(cell_states.colours[cell]);
                graphics.fillRect(x, y, cell_width, cell_height);
            }
        }
        

        if(!draw_grid) return;
        // ----- DRAW GRID -----
        graphics.setColor(GRID_COLOUR);
        for(int j = 0; j < height; j++) {
            y = off_y + j*cell_height;
            graphics.drawLine(off_x, y, off_x + width*cell_width, y);
        }
        graphics.drawLine(off_x, off_y + height*cell_height, off_x + width*cell_width, off_y + height*cell_height);

        for(int i = 0; i < width; i++) {
            x = off_x + i*cell_width;
            graphics.drawLine(x, off_y, x, off_y + height*cell_height);
        }
        graphics.drawLine(off_x + width*cell_width, off_y, off_x + width*cell_width, off_y + height*cell_height);
    }


    public void load(String filename) {
        Scanner reader = null;
        String line; int x,y;
        try {
            reader = new Scanner(new File("./savefiles",filename));
            y = 0;
            while(reader.hasNextLine()) {
                line = reader.nextLine();
                x = 0;
                for(char c : line.toCharArray()) {
                    cells[y][x] = (c == 'o' ? (byte)1 : (byte)0);
                    x++;
                }
                y++;
            }

            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public int[] findLoadWH(String filename) {
        String file = "savefiles/" + filename;
        int rows = 0;
        int cols = 0;
        try {
            Scanner fileRead = new Scanner(new File(file));
            while (fileRead.hasNext()) {
                String line = fileRead.nextLine();
                if (line.startsWith("#:")) {
                    String comments = line;
                } else if (line.startsWith(".") || line.startsWith("o")){
                    cols = line.length();
                    rows++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("That game state does not exist: " + e.getMessage());
        }
        return new int[]{rows, cols};
    }

    public void save(String filename) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File("./savefiles",filename));
            for(byte[] row : cells) {
                for(byte state : row) {
                    writer.write(state == 0 ? "." : "o");
                }
                writer.write("\n");
            }

            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public CellStates getCell_states() {
        return this.cell_states;
    }
}