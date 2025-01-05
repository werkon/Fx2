package de.esnecca;

import java.util.Iterator;
import java.util.LinkedList;

public class XQueue {
    private LinkedList<XObject> ll;

    public XQueue() {
        ll = new LinkedList<XObject>();
    }

    public synchronized void add(XObject xObject) {
        ll.add(xObject);
    }

    public synchronized XObject getAndLock() {
        while(!ll.isEmpty()){
            XObject xObject = ll.removeFirst();
            boolean locked = xObject.getLock().tryLock();
            if(locked){
                return xObject;
            } else {
                ll.add(xObject);
            }
        }
        return null;
    }

    public synchronized boolean isEmpty() {
        return ll.isEmpty();
    }

    public synchronized int size() {
        return ll.size();
    }

    public void ana(){
        int grass = 0;
        int sheep = 0;
        int alive = 0;

        Iterator<XObject> it = ll.iterator();
        while(it.hasNext()) {
            XObject xObject = it.next();
            if(xObject.isAlive()){
                ++alive;
            }
            if(xObject instanceof XGrass){
                ++grass;
            }
            if(xObject instanceof XSheep){
                ++sheep;
            }

          }
      System.out.println("Grass: " + grass + ", Sheep: " + sheep + ", Alive: " + alive);
    }

}
