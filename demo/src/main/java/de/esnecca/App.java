package de.esnecca;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application implements EventHandler<javafx.scene.input.MouseEvent> {

    private static Scene scene;
    private static final int IMAGE_WIDTH = 2400;
    private static final int IMAGE_HEIGHT = 1200;

    private static XCanvas xCanvas;

    @Override
    public void start(Stage stage) throws IOException {

        HBox topMenu = new HBox();
        Button size = new Button("Size");
        size.setOnAction(e -> stage.sizeToScene());
        Button start = new Button("Start");
        start.setOnAction(e->{
            xCanvas.set(10, 10, 255, 0, 0);
            xCanvas.paint();
        });
        Button stop = new Button("Stop");

        topMenu.getChildren().addAll(size, start, stop);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);

        xCanvas = new XCanvas(IMAGE_WIDTH, IMAGE_HEIGHT, this);
        borderPane.setCenter(xCanvas.getCanvas());

        scene = new Scene(borderPane);
        stage.setScene(scene);

        stage.setTitle("FX");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void handle(MouseEvent e) {
        System.out.println("Mouse clicked at " + e.getX() + ", " + e.getY());
        xCanvas.set((int) e.getX(), (int) e.getY(), 0, 0, 255);
        xCanvas.paint();
}

}