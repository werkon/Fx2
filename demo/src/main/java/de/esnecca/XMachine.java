package de.esnecca;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

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
    private int rt;

    private Date date;

    XMachine(int width, int height, XCanvas xCanvas) {
        this.width = width;
        this.height = height;
        this.xCanvas = xCanvas;

        rt = 0;
        date = new Date();

        field = new XField(width, height);
        todo = new XQueue(width*height*2);
        done = new XQueue(width*height*2);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                while (!createNewGrass(i, j, 255 / 2))
                    ;
            }
        }

        for (int i = 0; i < 20; i++) {
            int x = ThreadLocalRandom.current().nextInt(width);
            int y = ThreadLocalRandom.current().nextInt(height);
            while (!createNewSheep(x, y))
                ;
        }

        XQueue xQueue = new XQueue(width*height*2);
        while (!done.isEmpty()) {
            if (ThreadLocalRandom.current().nextInt(2)==0) {
                XObject xObject = done.get();
                xQueue.addFirst(xObject);
            } else {
                XObject xObject = done.get();
                xQueue.add(xObject);
                
            }
        }
        done = xQueue;

        xThreads = new XThread[32];
        for (int i = 0; i < xThreads.length; i++) {
            xThreads[i] = new XThread(this);
            xThreads[i].start();
        }

        xMainThread = new XMainThread(this);

        xCanvas.setEventListener(this);

    }

    public synchronized void incRt() {
        ++rt;
    }

    public synchronized void decRt() {
        --rt;
    }

    public synchronized int getRt() {
        return rt;
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
        while (!todo.isEmpty() || getRt() > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        // try {
        //     Thread.sleep(10);
        // } catch (InterruptedException e) {
        // }
        // while (!todo.isEmpty() || getRt() > 0) {
        //     try {
        //         Thread.sleep(10);
        //     } catch (InterruptedException e) {
        //     }
        // }
        paintField();
    }

    private void paintField() {
        // for (int i = 0; i < width; i++) {
        // for (int j = 0; j < height; j++) {
        // // XObject xObject = field.getAndLock(i, j);
        // XObject xObject = field.get(i, j);
        // xCanvas.set(i, j, xObject.getR(), xObject.getG(), xObject.getB());
        // // xObject.getLock().unlock();
        // }
        // }

        xCanvas.paint();
    }

    // public void step() {
    // XObject xObject = getTodoAndLock();
    // if (xObject != null) {
    // boolean ok = xObject.iterate(xCanvas);
    // if (ok) {
    // if (xObject.isAlive()) {
    // done.add(xObject);
    // }
    // } else {
    // done.add(xObject);
    // }
    // xObject.getLock().unlock();
    // }
    // }

    public void step() {
        XObject xObjects[] = todo.getMany();
        if (xObjects.length > 0) {
            incRt();
            XQueue ldone = new XQueue(xObjects.length);
            for (int i = 0; i < xObjects.length; i++) {
                XObject xObject = xObjects[i];
                if (xObject.isAlive()) {
                    if (xObject.getLock().tryLock()) {
                        boolean ok = xObject.iterate(xCanvas);
                        if (ok) {
                            if (xObject.isAlive()) {
                                ldone.add(xObject);
                            }
                        } else {
                            ldone.add(xObject);
                        }
                        xObject.getLock().unlock();
                    } else {
                        ldone.add(xObject);
                    }
                }
            }
            done.addMany(ldone);
            decRt();
        }
    }

    public boolean createNewGrass(int i, int j, int age) {
        XGrass xGrass = new XGrass(i, j);
        xGrass.setAge(age);
        return setNew(i, j, xGrass);
    }

    public boolean createNewSheep(int i, int j) {
        XSheep xSheep = new XSheep(i, j, this);
        return setNew(i, j, xSheep);
    }

    public boolean setNew(int i, int j, XObject xObject) {
        XObject old = field.getAndLock(i, j);
        if (old == null) {
            return false;
        } else {
            old.kill();
            setAndDone(i, j, xObject);
            old.getLock().unlock();
            return true;
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
    public synchronized void handle(MouseEvent e) {
        System.out.println(e.getButton().name());
        System.out.println("Mouse clicked at " + e.getX() + ", " + e.getY());

        XObject old = field.getAndLock((int) e.getX(), (int) e.getY());
        if (old != null) {
            int age = 0;
            if (old instanceof XGrass) {
                age = ((XGrass) old).getAge();
            }
            old.getLock().unlock();
            if (e.getButton().name().equals("PRIMARY")) {
                XObject xObject = new XWolf((int) e.getX(), (int) e.getY(), age, this);
                setNew((int) e.getX(), (int) e.getY(), xObject);
            } else {
                if (e.getButton().name().equals("SECONDARY")) {
                    XObject xObject = new XWolf3((int) e.getX(), (int) e.getY(), age, this);
                    setNew((int) e.getX(), (int) e.getY(), xObject);
                } else {
                    XObject xObject = new XSheep((int) e.getX(), (int) e.getY(), this);
                    setNew((int) e.getX(), (int) e.getY(), xObject);
                }
            }

        }
    }

    public XObject getTodoAndLock() {
        XObject xObject = todo.get();
        if (xObject != null) {
            if (xObject.isAlive()) {
                if (xObject.getLock().tryLock()) {
                    return xObject;
                } else {
                    done.add(xObject);
                }
            }
        }
        return null;
    }

    public synchronized void doneToDo() {
        // done.analyze();
        //System.out.println("Done: " + done.size() + ", Todo: " + todo.size());
        
        Date date = new Date();
        long diff = date.getTime() - this.date.getTime();
        System.out.println("Done: " + done.size() + ", Todo: " + todo.size() + ", Time: " + diff);
        this.date = date;

        XQueue tmp = todo;
        todo = done;
        done = tmp; // new XQueue(width*height*2);
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
