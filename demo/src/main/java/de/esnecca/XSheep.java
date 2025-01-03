package de.esnecca;

public class XSheep extends XObject {

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    int age;

    XSheep(int x, int y) {
        super(x, y);
        age = 255 / 2;
    }

    @Override
    public void iterate() {
        super.iterate();
        if(++age > 255){
            age = 255;
        }

    }

    @Override
    public int getG() {
        return age;
    }

    @Override
    public int getR() {
        return age;
    }

}
