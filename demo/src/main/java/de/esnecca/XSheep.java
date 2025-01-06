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
    public boolean iterate() {
        super.iterate();

        food -= 10;

        if (food <= 0) {
            xMachine.createNewGrass(x, y, 0);
            return true;
        }

        XObject xObjects[] = reserve();
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
            food += age / 10;
            if (food > 255) {
                food = 255 / 2;
                XSheep xSheep = new XSheep(getX(), getY(), xMachine);
                xMachine.setAndDone(getX(), getY(), xSheep);
            } else {
                XGrass xGrass = new XGrass(getX(), getY());
                xMachine.setAndDone(getX(), getY(), xGrass);
            }

            x = xObjects[idx].getX();
            y = xObjects[idx].getY();

            xObjects[idx].kill();

            xMachine.set(x, y, this);
        }

        free(xObjects);

        return true;
    }

    @Override
    public int getG() {
        return food;
    }

    @Override
    public int getR() {
        return food;
    }

}
