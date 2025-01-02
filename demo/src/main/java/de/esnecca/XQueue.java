package de.esnecca;

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

}
