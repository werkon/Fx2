package de.esnecca;

public class XSheep extends XObject {

    public int getFood() {
        return food;
    }

    public void setFood(int age) {
        this.food = age;
    }

    int food;
    XMachine xMachine;

    XSheep(int x, int y, XMachine xMachine) {
        super(x, y);
        this.xMachine = xMachine;
        food = 255 / 2;
    }

    @Override
    public void iterate() {
        super.iterate();
        if(--food <= 0){
            xMachine.createNewGrass(x, y);
        }

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
