package de.esnecca;

public class XThread extends Thread {

    XMachine xMachine;
    boolean toStop;

    XThread(XMachine xMachine) {
        super();
        this.xMachine = xMachine;
        toStop = false;
    }

    @Override
    public void run() {
        super.run();

        while(!toStop){
            xMachine.step();
        }
    }

    public void stopIt() {
        toStop = true;
    }

}
