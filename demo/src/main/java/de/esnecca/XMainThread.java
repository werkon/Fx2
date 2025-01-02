package de.esnecca;

public class XMainThread extends Thread {

    private XMachine xMachine;
    private boolean running;
    private boolean toStop;

    XMainThread(XMachine xMachine) {
        super();
        this.xMachine = xMachine;
        running = false;
        toStop = false;
    }

    @Override
    public void run() {
        super.run();

        while(!toStop){
            xMachine.iterate();
        }
        running = false;
        toStop = false;
    }

    public void start() {
        if(!running){
            running = true;
            super.start();
        }
    }

    public void stopIt() {
        toStop = true;
    }

    public boolean isRunning() {
        return running;
    }

}
