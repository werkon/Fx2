package de.esnecca;

public class XSheep extends XObject {

    static int[] dx = {-1, -1, -1,  0, 0,  1, 1, 1};
    static int[] dy = {-1,  0,  1, -1, 1, -1, 0, 1};

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    int food;
    XMachine xMachine;

    XSheep(int x, int y, XMachine xMachine) {
        super(x, y);
        this.xMachine = xMachine;
        food = 255 / 2;
    }

    @Override
    public boolean iterate() {
        super.iterate();

        food -= 5;

        if(food <= 0){
            xMachine.createNewGrass(x, y);
            return true;
        }

        XObject xObjects[] = new XObject[8];

        int random = (int)(Math.random() * 8);
        for(int i = 0; i < 8; i++){
            int dr = (i + random) % 8;
            int nx = (getX() + dx[dr] + xMachine.getWidth()) % xMachine.getWidth();
            int ny = (getY() + dy[dr] + xMachine.getHeight()) % xMachine.getHeight();

            XObject xObject = xMachine.getAndLock(nx, ny);
            if(xObject == null){
                for(int j = 0; j < 8; j++){
                    if(xObjects[j] != null){
                        xObjects[j].getLock().unlock();
                    }
                }
                return false;
            }

            xObjects[i] = xObject;
        }

        int idx = -1;
        int age = 0;
        for(int i = 0; i < 8; i++){
            if(xObjects[i] instanceof XGrass){
                XGrass xGrass = (XGrass) xObjects[i];
                if(xGrass.getAge() > age){
                    idx = i;
                    age = xGrass.getAge();
                }
            }
        }
        if( idx >= 0){
            food += age / 10;
            if(food > 255){
                food = 255 / 2;
                XSheep xSheep = new XSheep(getX(), getY(), xMachine);
                xMachine.setAndDone(getX(), getY(), xSheep);
            }else{
                XGrass xGrass = new XGrass(getX(), getY());
                xMachine.setAndDone(getX(), getY(), xGrass);
            }

            x = xObjects[idx].getX();
            y = xObjects[idx].getY();

            xObjects[idx].kill();

            xMachine.set(x, y, this);
        }
        for(int i = 0; i < 8; i++){
            xObjects[i].getLock().unlock();
        }

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
