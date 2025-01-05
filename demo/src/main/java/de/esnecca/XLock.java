package de.esnecca;

public class XLock extends XObject{

	static int[] dx = {-1, -1, -1,  0, 0,  1, 1, 1};
    static int[] dy = {-1,  0,  1, -1, 1, -1, 0, 1};

    public XLock(int x, int y) {
		super(x, y);
	}
}

