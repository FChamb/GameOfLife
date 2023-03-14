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
    public boolean isFlutter(int key) {
        boolean state = flutter[key];
        if(state) flutter[key] = false;
        return state;
    }

    /**
     *
     * @return
     */
    public boolean ctrl() {
        return pressed[KeyEvent.VK_CONTROL];
    }
    public boolean shift() {
        return pressed[KeyEvent.VK_SHIFT];
    }
    public boolean alt() {
        return pressed[KeyEvent.VK_ALT];
    }
    public boolean altgr() {
        return pressed[KeyEvent.VK_ALT_GRAPH];
    }
    public boolean alts() {
        return alt() | altgr();
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() >= pressed.length) return;
        if(!pressed[e.getKeyCode()]) clicked[e.getKeyCode()] = true;        // suppress the flutter when holding down a key
        pressed[e.getKeyCode()] = true;
        flutter[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() >= pressed.length) return;
        pressed[e.getKeyCode()] = false;
        clicked[e.getKeyCode()] = false;
        flutter[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyCode() >= pressed.length) return;
        pressed[e.getKeyCode()] = true;
        clicked[e.getKeyCode()] = true;
        flutter[e.getKeyCode()] = true;
    }
}