package de.esnecca;

public class XWolf extends XLock {

    private int sfood;
    private int oldAge;

    public XWolf(int x, int y, int oldAge, XMachine xMachine) {
        super(x, y, xMachine);
        this.oldAge = oldAge;
        sfood = 255 / 2;
    }

    @Override
    public boolean iterate(XCanvas xCanvas) {
        super.iterate(xCanvas);

        sfood -= 1;

        if (sfood <= 0) {
            getxMachine().createNewGrass(getX(), getY(), 0);
            return true;
        }

        XObject xObjects[] = reserve(-1);
        if (xObjects == null) {
            return false;
        }

        int idx = -1;
        int food = 0;
        for (int i = 0; i < 8; i++) {
            if (xObjects[i] instanceof XSheep xSheep) {
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
                XWolf xWolf = new XWolf(getX(), getY(), oldAge, getxMachine());
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
    public int getR() {
        if(sfood < 128) {
            return 128;
        }
        return sfood;
    }

}
