package de.esnecca;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class XQueue {
    protected ArrayDeque<XObject> ll;

    public XQueue(int size) {
        ll = new ArrayDeque<XObject>(size);
    }

    public synchronized void add(XObject xObject) {
        ll.addLast(xObject);
    }

    public synchronized void addFirst(XObject xObject) {
        ll.addFirst(xObject);
    }

    public synchronized void addMany(XQueue xQueue) {
        try{
            while(true) {
                ll.addLast(xQueue.ll.removeFirst());
            }
        } catch (Exception e) {
        }
    }

    public synchronized XObject[] getMany() {
        int bound = ThreadLocalRandom.current().nextInt(100, 1000);
        return getMany(bound);
    }

    public synchronized XObject[] getMany(int bound) {

        int size = ll.size();
        size = size > bound ? bound : size;
        XObject[] xObjects = new XObject[size];
        for (int i = 0; i < size; i++) {
            xObjects[i] = ll.removeFirst();
        }
        return xObjects;

        // try{
        //     return ll.removeFirst();
        // } catch (Exception e) {
        //     return null;
        // }

        // while (!ll.isEmpty()) {
        //     XObject xObject = ll.removeFirst();
        //     boolean locked = xObject.getLock().tryLock();
        //     if (locked) {
        //         return xObject;
        //     } else {
        //         ll.add(xObject);
        //     }
        // }
        // return null;
    }


    public synchronized XObject get() {
        try{
            return ll.removeFirst();
        } catch (Exception e) {
            return null;
        }
        // while (!ll.isEmpty()) {
        //     XObject xObject = ll.removeFirst();
        //     boolean locked = xObject.getLock().tryLock();
        //     if (locked) {
        //         return xObject;
        //     } else {
        //         ll.add(xObject);
        //     }
        // }
        // return null;
    }

    public synchronized boolean isEmpty() {
        return ll.isEmpty();
    }

    public synchronized int size() {
        return ll.size();
    }

    public synchronized void analyze() {

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
