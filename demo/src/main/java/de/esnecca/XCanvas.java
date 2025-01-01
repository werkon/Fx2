package de.esnecca;

import javafx.event.EventHandler;
import java.nio.ByteBuffer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;

public class XCanvas {

    int width;
    int height;
    Canvas canvas;
    GraphicsContext gc;
    byte imageData[];
    PixelWriter pixelWriter;
    PixelFormat<ByteBuffer> pixelFormat;

    XCanvas(int width, int height) {
        this.width = width;
        this.height = height;

        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        imageData = new byte[width * height * 3];

        pixelWriter = gc.getPixelWriter();
        pixelFormat = PixelFormat.getByteRgbInstance();

        clear();
        paint();
    }


    public void setEventListener(EventHandler<javafx.scene.input.MouseEvent> eventHandler) {
        canvas.setOnMouseClicked(e -> {
            eventHandler.handle(e);
        });
    }

    public void set(int x, int y, int r, int g, int b) {
        int off = (x + y * width) * 3;
        imageData[off] = (byte) r;
        imageData[off + 1] = (byte) g;
        imageData[off + 2] = (byte) b;
    }

    public void clear() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                set(x, y, (byte) 0, (byte) 0, (byte) 0);
            }
        }
    }

    public void paint() {
        pixelWriter.setPixels(0, 0, width, height, pixelFormat, imageData, 0, width * 3);
    }

    public Canvas getCanvas() {
        return canvas;
    }

}
