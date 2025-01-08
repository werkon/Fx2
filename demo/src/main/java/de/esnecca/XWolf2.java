package de.esnecca;

public class XWolf2 extends XLock {

    private int sfood;
    private int oldAge;

    public XWolf2(int x, int y, int oldAge, XMachine xMachine) {
        super(x, y, xMachine);
        this.oldAge = oldAge;
        sfood = 255 / 2;
    }

    @Override
    public boolean iterate() {
        super.iterate();

        sfood -= 1;

        if (sfood <= 0) {
            getxMachine().createNewGrass(getX(), getY(), 0);
            return true;
        }

        XObject xObjects[] = reserve();
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
            free(xObjects);
            return true;

        }

        idx = -1;
        int age = 0;
        for (int i = 0; i < 8; i++) {
            if (xObjects[i] instanceof XGrass) {
                XGrass xGrass = (XGrass) xObjects[i];
                if (xGrass.getAge() > age) {
                    idx = i;
                    age = xGrass.getAge();
                }
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
            free(xObjects);
            return true;

        }

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
