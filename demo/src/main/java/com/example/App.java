package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static final int IMAGE_WIDTH = 800;
    private static final int IMAGE_HEIGHT = 600;
    private byte imageData[] = new byte[IMAGE_WIDTH * IMAGE_HEIGHT * 3];

    @Override
    public void start(Stage stage) throws IOException {

        HBox topMenu = new HBox();
        Button size = new Button("Size");
        size.setOnAction(e -> stage.sizeToScene());
        Button start = new Button("Start");
        Button stop = new Button("Stop");

        topMenu.getChildren().addAll(size, start, stop);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);

        Canvas canvas = new Canvas(IMAGE_WIDTH, IMAGE_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int x = 0; x < IMAGE_WIDTH; x++) {
            for (int y = 0; y < IMAGE_HEIGHT; y++) {
                imageData[(x + y * IMAGE_WIDTH) * 3] = (byte) 255;
                imageData[(x + y * IMAGE_WIDTH) * 3 + 1] = (byte) 0;
                imageData[(x + y * IMAGE_WIDTH) * 3 + 2] = (byte) 0;
            }
        }

        PixelWriter pixelWriter = gc.getPixelWriter();
        PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
        pixelWriter.setPixels(0, 0, IMAGE_WIDTH,
                IMAGE_HEIGHT, pixelFormat, imageData,
                0, IMAGE_WIDTH * 3);

        borderPane.setCenter(canvas);

        scene = new Scene(borderPane);
        stage.setScene(scene);

        stage.setTitle("test");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}