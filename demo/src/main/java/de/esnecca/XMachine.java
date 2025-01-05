package de.esnecca;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class XMachine extends Thread implements EventHandler<javafx.scene.input.MouseEvent> {

    private int width;
    private int height;
    private XCanvas xCanvas;

    private XField field;
    private XQueue todo;
    private XQueue done;
    private XThread xThreads[];
    private XMainThread xMainThread;

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

        for(int i = 0; i < 1000; i++){
            int x = (int)(Math.random() * width);
            int y = (int)(Math.random() * height);
            createNewSheep(x, y);
        }

        xThreads = new XThread[32];
        for (int i = 0; i < xThreads.length; i++) {
            xThreads[i] = new XThread(this);
            xThreads[i].start();
        }

        xMainThread = new XMainThread(this);

        xCanvas.setEventListener(this);

    }

    @Override
    public void start() {
        if (xMainThread != null) {
            stopIt();
        }
        xMainThread = new XMainThread(this);
        xMainThread.start();
    }

    public void stopIt() {
        xMainThread.stopIt();
        while (xMainThread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    public void stopItAll() {
        stopIt();
        for (int i = 0; i < xThreads.length; i++) {
            xThreads[i].stopIt();
            while (xThreads[i].isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void iterate() {
        doneToDo();
        while (!todo.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        paintField();
    }

    private void paintField() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                XObject xObject = field.getAndLock(i, j);
                xCanvas.set(i, j, xObject.getR(), xObject.getG(), xObject.getB());
                xObject.getLock().unlock();
            }
        }

        xCanvas.paint();
    }

    public void step() {
        XObject xObject = getTodoAndLock();
        if (xObject != null) {
            boolean ok = xObject.iterate();
            if(ok){
                if(xObject.isAlive()){
                    done.add(xObject);
                }
            }else{
                todo.add(xObject);
            }
            xObject.getLock().unlock();	
        }
    }

    public void createNewGrass(int i, int j) {
        XGrass xGrass = new XGrass(i, j);
        setNew(i, j, xGrass);
    }

    public void createNewSheep(int i, int j) {
        XSheep xSheep = new XSheep(i, j, this);
        setNew(i, j, xSheep);
    }


    public void setNew(int i, int j, XObject xObject) {
        XObject old = field.getAndLock(i, j);
        if( old != null){
            old.kill();
            setAndDone(i, j, xObject);
            old.getLock().unlock();
        }
    }

    public void set(int i, int j, XObject xObject) {
        field.set(i, j, xObject);
    }

    public void setAndDone(int i, int j, XObject xObject) {
        field.set(i, j, xObject);
        done.add(xObject);
    }


    @Override
    public void handle(MouseEvent e) {
        System.out.println("Mouse clicked at " + e.getX() + ", " + e.getY());

        XObject xSheep = new XSheep((int) e.getX(), (int) e.getY(), this);
        setNew((int)e.getX(), (int)e.getY(), xSheep);
    }

    public XObject getTodoAndLock() {
        while (!todo.isEmpty()) {
            XObject xObject = todo.getAndLock();
            if (xObject != null) {
                if (xObject.isAlive()) {
                    return xObject;
                } else {
                    xObject.getLock().unlock();
//                    System.out.println("Dead object found");
                }
            }
        }
        return null;
    }

    public synchronized void doneToDo() {
        System.out.print("doneToDo: " + done.size() + " ");
        done.ana();


        todo = done;
        done = new XQueue();
    }

    public XObject getAndLock(int i, int j) {
        return field.getAndLock(i, j);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
