import java.awt.Color;
import java.awt.Graphics;

public class Game implements Runnable {

    private int width, height;
    private Display display;

    private double fps;


    public Game(int width, int height) {
        this.width = width; this.height = height;

        display = new Display(width, height);
        fps = 60;
    }


    public void update() {

    }

    public void draw(Graphics g) {
        g.setColor(new Color(85,85,85));
        g.fillRect(10,10,50,50);
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