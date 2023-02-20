import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Point;

public class Game implements Runnable {

    private int width, height;
    private Mouse mouse;
    private Keyboard keyboard;
    private Display display;

    private double fps;

    private int grid_w, grid_h; 
    private int cell_w, cell_h;
    private Grid grid;


    public Game(int width, int height) {
        this.width = width; this.height = height;

        mouse = new Mouse(); keyboard = new Keyboard();

        display = new Display(width, height, mouse, keyboard);
        fps = 60;


        grid_w = 25; grid_h = 50;
        cell_w = width/grid_w; cell_h = height/grid_h;
        grid = new Grid(grid_w, grid_h, cell_w, cell_h);
    }


    public void update() {
        if(keyboard.isPressed(KeyEvent.VK_ESCAPE)) System.exit(0);

        if(mouse.isClicked(MouseEvent.BUTTON1)) {
            Point location = mouse.getLocation();
            grid.clicked(location.x, location.y);
        }
    }

    public void draw(Graphics graphics) {
        grid.draw(graphics);
    }






    @Override
    public void run() {
        double spf = 1/fps;
        double time_moment = System.currentTimeMillis()/1000.0, time_block = 0;
        while(true) {
            time_block += System.currentTimeMillis()/1000.0 - time_moment;
            time_moment = System.currentTimeMillis()/1000.0;

            while(time_block > spf) {
                time_block -= spf;
                render();
            }
            update();
        }
    }

    private void render() {
        display.generateGraphics(this);
    }
}