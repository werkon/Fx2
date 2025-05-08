package de.esnecca;

import java.util.concurrent.ThreadLocalRandom;

public class XLock extends XObject{

	private final int[] dx = {-1,  0,  1,  1,  1,  0, -1, -1};
    private final int[] dy = {-1, -1, -1,  0,  1,  1,  1,  0};

	private XMachine xMachine;

    public XLock(int x, int y, XMachine xMachine) {
		super(x, y);
		this.xMachine = xMachine;
	}

	public XObject[] reserve(int random	) {

		if(random < 0 ){
        	random = ThreadLocalRandom.current().nextInt(8);
		}

		XObject xObjects[] = new XObject[8];
        for (int i = 0; i < 8; i++) {
            int dr = (i + random) % 8;
            int nx = (getX() + dx[dr] + xMachine.getWidth()) % xMachine.getWidth();
            int ny = (getY() + dy[dr] + xMachine.getHeight()) % xMachine.getHeight();

            XObject xObject = xMachine.getAndLock(nx, ny);
            if (xObject == null) {
				free(xObjects);
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

	public XMachine getxMachine() {
		return xMachine;
	}

}

