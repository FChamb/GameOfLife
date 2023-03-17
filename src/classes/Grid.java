package classes;

import java.awt.Color;
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
    private int area_width, area_height;
    private int cell_width, cell_height;

    private byte[][] cells;

    private ArrayList<byte[][]> previousGrids = new ArrayList<>();

    private int many = 50;

    public boolean draw_grid;


    /**
     * Constructor for Grid object. The width and height of the grid are used
     * to determine how big each cell can be, by dividing the total length by number.
     * A new CellStates attribute is called and set to cell_states. Finally, the 2D
     * array is created and grid lines are turned on.
     * @param x - number of cells wide
     * @param y - number of cells tall
     * @param width - width of grid
     * @param height - height of grid
     * @param cell_width - cell width
     * @param cell_height - cell height
     */
    public Grid(int x, int y, int width, int height, int area_width, int area_height) {
        // ----- INITIALISE VARIABLES -----
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.area_width = area_width; this.area_height = area_height;
        this.cell_width = area_width/width; this.cell_height = area_height/height;

        cell_states = new CellStates();

        cells = new byte[height][width];
        draw_grid = true;
    }


    private int fixX(int x) {
        // ----- MAP X-COORDANITE -----
        x -= this.x;
        if(x < 0) return -1;

        x /= cell_width;

        return x;
    }
    private int fixY(int y) {
        // ----- MAP Y-COORDANITE -----
        y -= this.y;
        if(y < 0) return -1;

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

    /**
     * Randomise checks every cell in the board and gives it a 50/50
     * change of being alive or dead.
     */
    public void randomise() {
        cells = new byte[height][width];
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
                cells[y][x] = (byte)(Math.random()*2);
    }

    /**
     * Clear sets the private cells attribute to a new 2D array of
     * bytes, therein clearing the board.
     */
    public void clear() {
        cells = new byte[height][width];
        previousGrids.clear();
    }


    /**
     * Count neighbors takes an x and y coordinate for which cell to look at.
     * Using that information all eight cells surrounding the chosen one are
     * found. For each alive cell the count goes up. Finally, count is returned.
     * @param x - x coord of cell
     * @param y - y coord of cell
     * @return int[] an array with the first value containing the number of
     * alive cells around a specific one
     */
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

    /**
     * update() determines the state of every cell by individually counting its
     * neighbors and applying game rules. Then this grid object is set to the new
     * state.
     */
    public void update() {
        byte[][] buffer = new byte[height][width];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                buffer[y][x] = cell_states.newState(cells[y][x], countNeighbours(x,y));
            }
        }

        cells = buffer;
        previousGrids.add(cells);
    }

    /**
     * Functionality for the fast-forward key. This class looks at the private
     * many object and runs update() that many times.
     */
    public void updateMany() {
        for (int i = 0; i < this.many; i++) {
            update();
        }
    }

    /**
     * getPrevious updates the 2D array of bytes to be equal to the last save in
     * this.previousGrids only if the size of the arraylist is greater than 1.
     * Lastly the arraylist removes the updated save.
     */
    public void getPrevious() {
        if (previousGrids.size() > 1) {
            byte[][] buffer = previousGrids.get(previousGrids.size() - 2);
            previousGrids.remove(previousGrids.size() - 1);
            cells = buffer;
        }
    }


    /**
     * Draw takes a graphic object and draws the individual cells and grid lines on
     * the grid. This method is called at the initialization stage of a game when all
     * the assets are drawn to the canvas.
     * @param graphics - Graphics containing all the assets for the game of life
     */
    public void draw(Graphics graphics) {
        int off_x = x + (area_width - cell_width*width)/2, off_y = y + (area_height - cell_height*height)/2;
        // int off_x = x, off_y = y;
        int x,y;

        // ----- DRAW CELLS -----
        byte cell;
        for(int j = 0; j < height; j++) {
            for(int i = 0; i < width; i++) {
                cell = cells[j][i];
                x = off_x + i*cell_width; y = off_y + j*cell_height;

                graphics.setColor(cell_states.colors[cell]);
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


    /**
     * Loads a game state by using a reader object and finding all instances of . or o.
     * @param filename - String value containing the name of the save
     */
    public void load(String filename) {
        previousGrids.clear();
        Scanner reader = null;
        String line; int x,y;
        try {
            reader = new Scanner(new File("./savefiles",filename));
            y = 0;
            while(reader.hasNextLine()) {
                line = reader.nextLine();
                if (!line.startsWith(".") && !line.startsWith("o")) {
                    break;
                }
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
        byte[][] buffer = cells;
        previousGrids.add(buffer);
    }

    /**
     * findLoadWH takes a filename and returns an array of integers which contain
     * the width and the height of a save state. The enables loading a file by
     * updating the grid size beforehand.
     * @param filename - A string value containing the name of the save
     * @return int[] - an array of integers with the width and height of a save state
     */
    public int[] findLoadWH(String filename) {
        String file = "savefiles/" + filename;
        int rows = 0;
        int cols = 0;
        try {
            Scanner fileRead = new Scanner(new File(file));
            while (fileRead.hasNext()) {
                String line = fileRead.nextLine();
                if (line.startsWith(".") || line.startsWith("o")){
                    cols = line.length();
                    rows++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("That game state does not exist: " + e.getMessage());
        }
        return new int[]{rows, cols};
    }

    /**
     * findComments reads a save file and attempts to find any comments at the bottom of the
     * save. Comments are labeled with #:. If there are no comments null will be returned
     * rather than the string of comments.
     * @param filename - A string value containing the name of the save state
     * @return String - a string value with the comments of a save state
     */
    public String findComments(String filename) {
        String file = "savefiles/" + filename;
        String comments = null;
        try {
            Scanner fileRead = new Scanner(new File(file));
            while (fileRead.hasNext()) {
                String line = fileRead.nextLine();
                if (line.startsWith("#:")) {
                    comments = line.substring(line.indexOf("#:") + 2);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("That game state does not exist: " + e.getMessage());
        }
        return comments;
    }

    /**
     * save takes both a filename and comments and save the current state of the
     * board game. Using a writer object the file has . and o written to represent
     * dead and alive cells.
     * @param filename - a string value containing the name of the file
     * @param comments - a string value containing any save comments
     */
    public void save(String filename, String comments) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File("./savefiles",filename));
            for(byte[] row : cells) {
                for(byte state : row) {
                    writer.write(state == 0 ? "." : "o");
                }
                writer.write("\n");
            }
            writer.write("#:" + comments);

            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public CellStates getCell_states() {
        return this.cell_states;
    }

    public void setMany(int many) {
        this.many = many;
    }

    public int getMany() {
        return this.many;
    }
}