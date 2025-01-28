package de.esnecca;

public class XWolf2 extends XLock {

    private int sfood;
    private int oldAge;
    private int direction;

    public XWolf2(int x, int y, int oldAge, XMachine xMachine) {
        super(x, y, xMachine);
        this.oldAge = oldAge;
        sfood = 255 / 2;
        direction = (int) (Math.random() * 8);
    }

    @Override
    public boolean iterate(XCanvas xCanvas) {
        super.iterate(xCanvas);

        sfood -= 3;

        if (sfood <= 0) {
            getxMachine().createNewGrass(getX(), getY(), 0);
            return true;
        }

        XObject xObjects[] = reserve(direction);
        if (xObjects == null) {
            return false;
        }

        int idx = -1;
        int food = 0;
        for (int i = 0; i < 8; i++) {
            if (xObjects[i] instanceof XSheep) {
                XSheep xSheep = (XSheep) xObjects[i];
                if (xSheep.getFood() > food) {
                    idx = i;
                    food = xSheep.getFood();
                }
            }
        }

        if (idx >= 0) {
            sfood += food;
            if (sfood > 255) {
                sfood = 255 / 2;
                XWolf2 xWolf = new XWolf2(getX(), getY(), oldAge, getxMachine());
                getxMachine().setAndDone(getX(), getY(), xWolf);
            } else {
                XGrass xGrass = new XGrass(getX(), getY());
                xGrass.setAge(oldAge);
                getxMachine().setAndDone(getX(), getY(), xGrass);
            }

            setX(xObjects[idx].getX());
            setY(xObjects[idx].getY());

            xObjects[idx].kill();

            getxMachine().set(getX(), getY(), this);
            xCanvas.set(getX(), getY(), getR(), getG(), getB());
            free(xObjects);
            return true;

        }

        idx = -1;
        for (int i = 0; i < 8; i++) {
            if (xObjects[i] instanceof XGrass) {
                idx = i;
                break;
            }
        }

        if (idx >= 0) {
            XGrass xGrass = new XGrass(getX(), getY());
            xGrass.setAge(oldAge);
            getxMachine().setAndDone(getX(), getY(), xGrass);

            setX(xObjects[idx].getX());
            setY(xObjects[idx].getY());
            oldAge = ((XGrass)xObjects[idx]).getAge();

            xObjects[idx].kill();

            getxMachine().set(getX(), getY(), this);
            xCanvas.set(getX(), getY(), getR(), getG(), getB());
            free(xObjects);
            return true;

        }

        xCanvas.set(getX(), getY(), getR(), getG(), getB());
        free(xObjects);
        return true;
    }

    @Override
    public int getB() {
        if(sfood < 128) {
            return 128;
        }
        return sfood;
    }

}
