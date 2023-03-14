package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;

public class Mouse implements MouseListener, MouseMotionListener {

    private Point location;
    private boolean on_screen;
    private boolean[] pressed, clicked;


    /**
     * Default constructor for a mouse object. This class implements the classes MouseListener and MouseMotionListener.
     * When a new mouse object is created, a boolean array is created for the four basic buttons on a mouse.
     */
    public Mouse() {
        pressed = new boolean[4];
        clicked = new boolean[4];
    }


    /**
     * A simple getter method which returns a Point object which the given
     * location of the mouse while it is on the panel.
     * @return - A Point object which the x,y coords of the mouse.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * A simple getter method which return a boolean value depending on if the mouse
     * is on screen.
     * @return - A boolean value. True for on screen, false otherwise.
     */
    public boolean onScreen() {
        return on_screen;
    }

    /**
     * isPressed is a getter method which returns if a given button is pressed in boolean
     * form.
     * @param button - A given button on the mouse.
     * @return -  A boolean value. True for pressed, false otherwise.
     */
    public boolean isPressed(int button) {
        return pressed[button];
    }

    /**
     * isCLicked is a getter method which returns if a given button is clicked
     * in boolean form.
     * @param button - A given button on the mouse.
     * @return - A boolean value. True for clicked, false otherwise.
     */
    public boolean isClicked(int button) {
        boolean state = clicked[button];
        if(state) clicked[button] = false;
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