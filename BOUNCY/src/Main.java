import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private int n_balls = 0;
    private List<Particle> particles = new ArrayList<>();
    private Canvas canvas;
    private Label fpsLabel;
    private long lastUpdateTime;
    private int frameCount;
    private double fps;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        Pane paneContainer = new Pane();
        canvas = new Canvas(1280, 720);
        fpsLabel = new Label("FPS: 0");
        fpsLabel.setLayoutX(1300);
        fpsLabel.setLayoutY(10);

        Pane paneControl = new Pane();
        paneControl.setMaxWidth(250);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.BASELINE_CENTER);

        Label labelX = new Label("Pos X:");
        TextField inputX = new TextField();
        Label labelY = new Label("Pos Y:");
        TextField inputY = new TextField();
        Label labelDeg = new Label("Degrees:");
        TextField inputDeg = new TextField();
        Label labelVel = new Label("Velocity (px/s):");
        TextField inputVel = new TextField();

        Button btnSubmit = new Button("ADD BALL");

        gridPane.setAlignment(Pos.BASELINE_CENTER);
        gridPane.addRow(0, labelX, inputX);
        gridPane.addRow(1, labelY, inputY);
        gridPane.addRow(2, labelDeg, inputDeg);
        gridPane.addRow(3, labelVel, inputVel);

        TextArea tester = new TextArea("(Test) Balls rn:\n");
        tester.setMaxSize(250, 720);

        GridPane gpControl = new GridPane();
        gpControl.addRow(0, gridPane);
        gpControl.addRow(1, btnSubmit);
        gpControl.addRow(2, tester);

        paneControl.getChildren().add(gpControl);
        paneContainer.getChildren().addAll(paneControl, canvas, fpsLabel);
        Scene scene = new Scene(paneContainer, 1530, 720);

        primaryStage.setTitle("BOUNCING BALLS - grp i forgot num");
        primaryStage.setScene(scene);
        primaryStage.show();

        btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    double x = Double.parseDouble(inputX.getText());
                    double y = Double.parseDouble(inputY.getText());
                    double velocity = Double.parseDouble(inputVel.getText());
                    double degrees = Double.parseDouble(inputDeg.getText());
                    addParticle(x, y, velocity, degrees);
                    tester.appendText(n_balls + " at pos (" + x + "," + y + ") " + degrees + "deg + " + velocity + "px/s\n");
                    n_balls++;
                } catch (NumberFormatException e) {
                    tester.appendText("Invalid input\n");
                }
            }
        });

        lastUpdateTime = System.nanoTime();
        frameCount = 0;
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(now);
                draw();
            }
        }.start();
    }

    private void addParticle(double x, double y, double velocity, double angle) {
        particles.add(new Particle(x, y, velocity, Math.toRadians(angle)));
    }

    private void update(long now) {
        double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
        lastUpdateTime = now;
        for (Particle particle : particles) {
            particle.update(deltaTime, canvas.getWidth(), canvas.getHeight());
        }

        frameCount++;
        if (frameCount % 30 == 0) { // Update FPS every 30 frames
            fps = 30.0 / deltaTime;
            fpsLabel.setText(String.format("FPS: %.2f", fps));
        }
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Particle particle : particles) {
            particle.draw(gc);
        }
    }
}