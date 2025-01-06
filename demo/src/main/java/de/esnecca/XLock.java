package de.esnecca;

public class XLock extends XObject{

	static int[] dx = {-1, -1, -1,  0, 0,  1, 1, 1};
    static int[] dy = {-1,  0,  1, -1, 1, -1, 0, 1};

	XMachine xMachine;

    public XLock(int x, int y, XMachine xMachine) {
		super(x, y);
		this.xMachine = xMachine;
	}

	public XObject[] reserve() {
		XObject xObjects[] = new XObject[8];

        int random = (int) (Math.random() * 8);
        for (int i = 0; i < 8; i++) {
            int dr = (i + random) % 8;
            int nx = (getX() + XLock.dx[dr] + xMachine.getWidth()) % xMachine.getWidth();
            int ny = (getY() + XLock.dy[dr] + xMachine.getHeight()) % xMachine.getHeight();

            XObject xObject = xMachine.getAndLock(nx, ny);
            if (xObject == null) {
                for (int j = 0; j < 8; j++) {
                    if (xObjects[j] != null) {
                        xObjects[j].getLock().unlock();
                    }
                }
                return null;
            }

            xObjects[i] = xObject;
        }	
		return xObjects;
	}

	public void free(XObject xObjects[]) {
		for (int i = 0; i < 8; i++) {
			if (xObjects[i] != null) {
				xObjects[i].getLock().unlock();
			}
		}
	}

}

