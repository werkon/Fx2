package de.esnecca;

public class XSheep extends XLock {

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    int food;

    XSheep(int x, int y, XMachine xMachine) {
        super(x, y, xMachine);
        food = 255 / 2;
    }

    @Override
    public boolean iterate(XCanvas xCanvas) {
        super.iterate(xCanvas);

        food -= 12;

        if (food <= 0) {
            getxMachine().createNewGrass(getX(), getY(), 0);
            return true;
        }

        XObject xObjects[] = reserve(-1);
        if (xObjects == null) {
            return false;
        }

        int idx = -1;
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
            food += age / 5;
            if (food > 255) {
                food = 255 / 2;
                XSheep xSheep = new XSheep(getX(), getY(), getxMachine());
                getxMachine().setAndDone(getX(), getY(), xSheep);
            } else {
                XGrass xGrass = new XGrass(getX(), getY());
                getxMachine().setAndDone(getX(), getY(), xGrass);
            }

            setX(xObjects[idx].getX());
            setY(xObjects[idx].getY());

            xObjects[idx].kill();

            getxMachine().set(getX(), getY(), this);
        }

        xCanvas.set(getX(), getY(), getR(), getG(), getB());

        free(xObjects);

        return true;
    }

    @Override
    public int getG() {
        if(food < 128) {
            return 128;
        }
        return food;
    }

    @Override
    public int getR() {
        if(food < 128) {
            return 128;
        }
        return food;
    }

}
