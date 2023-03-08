package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    private boolean[] pressed, clicked;

    public Keyboard() {
        pressed = new boolean[256];
        clicked = new boolean[256];
    }


    public boolean isPressed(int key) {
        return pressed[key];
    }
    public boolean isClicked(int key) {
        boolean state = clicked[key];
        if(state) clicked[key] = false;
        return state;
    }

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
        pressed[e.getKeyCode()] = true;
        clicked[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() >= pressed.length) return;
        pressed[e.getKeyCode()] = false;
        clicked[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyCode() >= pressed.length) return;
        pressed[e.getKeyCode()] = true;
        clicked[e.getKeyCode()] = true;
    }
}