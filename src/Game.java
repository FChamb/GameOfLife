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

    private byte sellectedState;
    private boolean active;

    private int tick;
    private int ups;


    public Game(int width, int height) {
        this.width = width; this.height = height;

        mouse = new Mouse(); keyboard = new Keyboard();

        display = new Display(width, height, mouse, keyboard);
        fps = 60;


        grid_w = 50; grid_h = 50;
        cell_w = width/grid_w; cell_h = height/grid_h;
        grid = new Grid(grid_w, grid_h, cell_w, cell_h);

        sellectedState = (byte)1;
        active = false;

        tick = 0; ups = 10;
        // fpu = (int)(fps / ups);
    }


    public void update() {
        if(keyboard.isPressed(KeyEvent.VK_ESCAPE)) System.exit(0);

        tick++;
        if(tick % fps == 0) tick = 0;



        if(keyboard.isClicked(KeyEvent.VK_SPACE)) {
            active = !active;
            // grid.update();
        }
        if(keyboard.isClicked(KeyEvent.VK_UP)) {
            ups++; if(ups > fps) ups = (int)fps;
        }
        if(keyboard.isClicked(KeyEvent.VK_DOWN)) {
            ups--; if(ups < 1) ups = 1;
        }

        if(keyboard.isClicked(KeyEvent.VK_R)) {
            grid.randomise();
        }
        if(keyboard.isClicked(KeyEvent.VK_C)) {
            grid.clear();
        }
        if(keyboard.isClicked(KeyEvent.VK_G)) {
            grid.draw_grid = !grid.draw_grid;
        }

        if(mouse.isPressed(MouseEvent.BUTTON1) && mouse.onScreen()) {
            Point location = mouse.getLocation();
            grid.setState(location.x, location.y, sellectedState);
        }
        else
        if(mouse.isPressed(MouseEvent.BUTTON3) && mouse.onScreen()) {
            Point location = mouse.getLocation();
            grid.setState(location.x, location.y, (byte)0);
        }
        else
        if(mouse.isPressed(MouseEvent.BUTTON2) && mouse.onScreen()) {
            Point location = mouse.getLocation();
            grid.toggleState(location.x, location.y);
        }


        if(active && tick % (int)(fps/ups) == 0) grid.update();
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

            if(time_block >= spf) {
                while(time_block >= spf) {
                    time_block -= spf;
                    update();
                }
                render();
            }
        }
    }

    private void render() {
        display.generateGraphics(this);
    }
}