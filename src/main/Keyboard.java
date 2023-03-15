package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    private boolean[] pressed, clicked, flutter;

    /**
     * Default constructor for a Keyboard object which implements KeyListener.
     * This constructor sets two boolean array lists which 256 values which enables
     * key support for most key types needed in this practical.
     */
    public Keyboard() {
        pressed = new boolean[256];
        clicked = new boolean[256];
        flutter = new boolean[256];
    }

    /**
     * A simple getter method which takes a specific key and returns whether it has
     * been pressed or not.
     * @param key - A given key on the keyboard.
     * @return - A boolean value denoting whether key has been pressed.
     */
    public boolean isPressed(int key) {
        return pressed[key];
    }

    /**
     * A simple getter method which takes a specific key and returns whether it has
     * been clicked or not.
     * @param key - A given key on the keyboard.
     * @return - A boolean value denoting whether the key has been pressed.
     */
    public boolean isClicked(int key) {
        boolean state = clicked[key];
        if(state) clicked[key] = false;
        return state;
    }

    /**
     * A simple getter method which takes a specific key and returns whether the key
     * object flutters or not.
     * @param key - A given key on the Keyboard.
     * @return - A boolean value denoting whether the key flutters.
     */
    public boolean isFlutter(int key) {
        boolean state = flutter[key];
        if(state) flutter[key] = false;
        return state;
    }

    /**
     * ctrl() is a specific getter method which returns the state of if the
     * ctrl key has been pressed.
     * @return - A boolean value denoting whether the ctrl key has been pressed.
     */
    public boolean ctrl() {
        return pressed[KeyEvent.VK_CONTROL];
    }

    /**
     * shift() is a specific getter method which returns the state of if the
     * shift key has been pressed.
     * @return - A boolean value denoting whether the shift key has been pressed.
     */
    public boolean shift() {
        return pressed[KeyEvent.VK_SHIFT];
    }

    /**
     * alt() is a specific getter method which returns the state of if the
     * alt key has been pressed.
     * @return - A boolean value denoting whether the alt key has been pressed.
     */
    public boolean alt() {
        return pressed[KeyEvent.VK_ALT];
    }
    public boolean altgr() {
        return pressed[KeyEvent.VK_ALT_GRAPH];
    }
    public boolean alts() {
        return alt() | altgr();
    }


    /**
     * An override of KeyListener's keyPressed method. This method changes
     * the boolean state of whether a key has been pressed or clicked depending
     * on which key actually was altered.
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() >= pressed.length) return;
        if(!pressed[e.getKeyCode()]) clicked[e.getKeyCode()] = true;        // suppress the flutter when holding down a key
        pressed[e.getKeyCode()] = true;
        flutter[e.getKeyCode()] = true;
    }

    /**
     * An override of KeyListener's keyReleased method. This method changes
     * the boolean state of whether a key has been released once it has been clicked
     * or pressed.
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() >= pressed.length) return;
        pressed[e.getKeyCode()] = false;
        clicked[e.getKeyCode()] = false;
        flutter[e.getKeyCode()] = false;
    }

    /**
     * An override of KeyListener's keyReleased method. This method changes
     * the boolean state of whether a key has been typed i.e. continuously pressed
     * down.
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyCode() >= pressed.length) return;
        pressed[e.getKeyCode()] = true;
        clicked[e.getKeyCode()] = true;
        flutter[e.getKeyCode()] = true;
    }
}