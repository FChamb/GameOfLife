import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;

public class Mouse implements MouseListener, MouseMotionListener {

    private Point location;
    private boolean on_screen;
    private boolean[] pressed, clicked;


    public Mouse() {
        pressed = new boolean[4];
        clicked = new boolean[4];
    }


    public Point getLocation() {
        return location;
    }

    public boolean onScreen() {
        return on_screen;
    }

    public boolean isPressed(int index) {
        return pressed[index];
    }
    public boolean isClicked(int index) {
        boolean state = clicked[index];
        if(state) clicked[index] = false;
        return state;
    }


    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        location = e.getPoint();
        on_screen = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        location = e.getPoint();
        on_screen = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        location = e.getPoint();
        clicked[e.getButton()] = true;
        pressed[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        location = e.getPoint();
        clicked[e.getButton()] = false;
        pressed[e.getButton()] = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        location = e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        location = e.getPoint();
    }
}