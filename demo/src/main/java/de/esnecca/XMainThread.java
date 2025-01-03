package de.esnecca;

public class XMainThread extends Thread {

    private XMachine xMachine;
    private boolean toStop;

    XMainThread(XMachine xMachine) {
        super();
        this.xMachine = xMachine;
        toStop = false;
    }

    @Override
    public void run() {
        super.run();

        while(!toStop){
            xMachine.iterate();
        }
        toStop = false;
    }

    public void start() {
        if(!isAlive()){
            super.start();
        }
    }

    public void stopIt() {
        toStop = true;
    }

}
