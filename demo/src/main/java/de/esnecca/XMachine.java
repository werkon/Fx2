package de.esnecca;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class XMachine  implements EventHandler<javafx.scene.input.MouseEvent> {

    private int width;
    private int height;
    private XCanvas xCanvas;

    private XObject xObject[][];

    XMachine(int width, int height, XCanvas xCanvas) {
        this.width = width;
        this.height = height;
        this.xCanvas = xCanvas;

        xObject = new XObject[width][height];

        xCanvas.setEventListener(this);

    }

    @Override
    public void handle(MouseEvent e) {
        System.out.println("Mouse clicked at " + e.getX() + ", " + e.getY());
        xCanvas.set((int) e.getX(), (int) e.getY(), 0, 0, 255);
        xCanvas.paint();
}

}
