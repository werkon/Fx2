package de.esnecca;

import java.util.LinkedList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class XMachine implements EventHandler<javafx.scene.input.MouseEvent> {

    private int width;
    private int height;
    private XCanvas xCanvas;

    private XField field;
    private XQueue todo;
    private XQueue done;

    XMachine(int width, int height, XCanvas xCanvas) {
        this.width = width;
        this.height = height;
        this.xCanvas = xCanvas;

        field = new XField(width, height);
        todo = new XQueue();
        done = new XQueue();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                createNewGrass(i, j);
            }
        }

        xCanvas.setEventListener(this);

    }

    public void iterate() {
        
        xCanvas.paint();
    }

    private void createNewGrass(int i, int j) {
        XGrass xGrass = new XGrass(i, j);
        setNew(i, j, xGrass);
    }

    private void setNew(int i, int j, XObject xObject) {
        XObject old = field.getAndLock(i, j);
        old.kill();
        old.getLock().unlock();
        field.set(i, j, xObject);
        done.add(xObject);
    }

    @Override
    public void handle(MouseEvent e) {
        System.out.println("Mouse clicked at " + e.getX() + ", " + e.getY());
        xCanvas.set((int) e.getX(), (int) e.getY(), 0, 0, 255);
        xCanvas.paint();
    }

    public XObject pullTodo() {
        while (!todo.isEmpty()) {
            XObject xObject = todo.getAndLock();
            if (xObject != null ){
                if(xObject.isAlive()) {
                    return xObject;
                }else{
                    xObject.getLock().unlock();
                }
            }
        }
        return null;
    }

    public synchronized void doneToDo() {
        todo = done;
        done = new XQueue();
    }

}
