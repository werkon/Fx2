package de.esnecca;

import java.util.HashMap;
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
        while (!ll.isEmpty()) {
            XObject xObject = ll.removeFirst();
            boolean locked = xObject.getLock().tryLock();
            if (locked) {
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

    public void analyze() {

        int alive = 0;
        Iterator<XObject> it = ll.iterator();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        while (it.hasNext()) {
            XObject xObject = it.next();
            if (xObject.isAlive()) {
                ++alive;
            }
            String className = xObject.getClass().getName();
            if (map.containsKey(className)) {
                map.put(className, map.get(className) + 1);
            } else {
                map.put(className, 1);
            }
        }
        System.out.print("Alive: " + alive);
        for (String key : map.keySet()) {
            System.out.print(", " + key.substring(key.lastIndexOf('.') + 1) + ": " + map.get(key));
        }
        System.out.println();

    }

}
