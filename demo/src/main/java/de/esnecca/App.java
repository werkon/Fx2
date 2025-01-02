package de.esnecca;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static final int IMAGE_WIDTH = 2400;
    private static final int IMAGE_HEIGHT = 1200;

    private static XCanvas xCanvas;
    private static XMachine xMachine;

    @Override
    public void start(Stage stage) throws IOException {

        HBox topMenu = new HBox();
        Button size = new Button("Size");
        size.setOnAction(e -> stage.sizeToScene());
        Button start = new Button("Start");
        start.setOnAction(e->{
            xMachine.iterate();
        });
        Button stop = new Button("Stop");

        topMenu.getChildren().addAll(size, start, stop);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);

        xCanvas = new XCanvas(IMAGE_WIDTH, IMAGE_HEIGHT);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(xCanvas.getCanvas());

        borderPane.setCenter(scrollPane);

        xMachine = new XMachine(IMAGE_WIDTH, IMAGE_HEIGHT, xCanvas);

        scene = new Scene(borderPane);
        stage.setScene(scene);

        stage.setTitle("FX");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}