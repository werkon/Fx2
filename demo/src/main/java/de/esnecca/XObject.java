package de.esnecca;

import java.util.concurrent.locks.ReentrantLock;

public class XObject {
    private int x, y;
    private boolean isAlive;
    private ReentrantLock lock;

    XObject(int x, int y) {
        this.x = x;
        this.y = y;
        isAlive = true;
        lock = new ReentrantLock();
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill() {
        isAlive = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isStatic() {
        return true;
    }
    
    public boolean iterate() {
        return true;
    }

    public int getR() {
        return 0;
    }

    public int getG() {
        return 0;
    }

    public int getB() {
        return 0;
    }
}
