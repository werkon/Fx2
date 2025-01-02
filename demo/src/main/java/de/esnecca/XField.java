package de.esnecca;

public class XField {

    private XObject field[][];

    public XField(int width, int height) {
        field = new XObject[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = new XObject(i, j);
            }
        }
    }

    public synchronized XObject getAndLock(int x, int y) {
        XObject xObject = field[x][y];
        boolean locked = xObject.getLock().tryLock();
        if (locked) {
            return xObject;
        } else {
            return null;
        }
    }

    public synchronized void set(int x, int y, XObject xObject) {
        field[x][y] = xObject;
    }
}
