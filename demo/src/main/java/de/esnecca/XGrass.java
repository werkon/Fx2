package de.esnecca;

public class XGrass extends XObject {

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    int age;

    XGrass(int x, int y) {
        super(x, y);
        age = 0;
    }

    @Override
    public boolean iterate() {
        super.iterate();
        if(++age > 255){
            age = 255;
        }
        return true;
    }

    @Override
    public int getG() {
        return age;
    }

    

    
}
