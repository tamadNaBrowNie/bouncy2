import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import javafx.scene.shape.Circle;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    private List<Particle> ball_buf = new ArrayList<Particle>();
    // private Canvas canvas= new Canvas(1280, 720);;
    private Label fpsLabel = new Label("FPS: 0");;
    private long lastFPSTime = System.nanoTime();;
    private int frameCount = 0;
    private double fps = 0;
    public static ExecutorService es;

    private Pane paneContainer = new Pane();

    private Pane ballPane = new Pane();

    private Pane paneControl = new Pane();

    private GridPane gridPane = new GridPane();

    private Label labelStartX = new Label("Start X:");
    private TextField inputStartX = new TextField();
    private Label labelStartY = new Label("Start Y:");
    private TextField inputStartY = new TextField();
    private Label labelEndX = new Label("End X:");
    private TextField inputEndX = new TextField();
    private Label labelEndY = new Label("End Y:");
    private TextField inputEndY = new TextField();
    private Label labelStartAngle = new Label("Start Angle:");
    private TextField inputStartAngle = new TextField();
    private Label labelEndAngle = new Label("End Angle:");
    private TextField inputEndAngle = new TextField();
    private Label labelStartVelocity = new Label("Start Velocity:");
    private TextField inputStartVelocity = new TextField();
    private Label labelEndVelocity = new Label("End Velocity:");
    private TextField inputEndVelocity = new TextField();
    private Label labelVelocity = new Label("Velocity (px/s):");
    private TextField inputVelocity = new TextField();
    private Label labelAngle = new Label("Angle (degrees):");
    private TextField inputAngle = new TextField();
    private Label labelCount = new Label("Number of Particles:");
    private TextField inputCount = new TextField();

    private Button btnAddByDistance = new Button("Add by Distance");
    private Button btnAddByAngle = new Button("Add by Angle");
    private Button btnAddByVelocity = new Button("Add by Velocity");

    private GridPane gpDistance = new GridPane();
    private GridPane gpVelocity = new GridPane();
    private GridPane gpAngle = new GridPane();
    
    private GridPane gpStartXY = new GridPane();
    private GridPane gpEndXY = new GridPane();
    private GridPane gpStartEndVelocity = new GridPane();
    private GridPane gpStartEndAngle = new GridPane();

    private TabPane paneTab = new TabPane();
    private Tab tabDistance = new Tab("Add by Distance");
    private Tab tabAngle = new Tab("Angle");
    private Tab tabVelocity = new Tab("Velocity");
    
    private TextArea tester = new TextArea("(Test) Balls rn:\n");

    private GridPane gpControl = new GridPane();
    private Separator separator1 = new Separator();
    private Separator separator2 = new Separator();
    private Separator separator3 = new Separator();
    // need diff separators lagi, no reusing
    private Separator separatorV = new Separator();

    
    private Label notif = new Label("");
    private Label labelStartXY = new Label("Starting Points (X,Y):");
    private Label labelEndXY = new Label("End Points (X,Y):");
    private Label labelStartEndVelocity = new Label("Starting and Ending Velocity:");
    private Label labelStartEndAngle = new Label("Starting and Ending Angle:");
    private Label labelConstXY = new Label("Spawn Point (X,Y):");

    
    // paneContainer.getChildren().addAll(paneControl, canvas, fpsLabel);

    private GridPane gpContainer = new GridPane();

    public static void main(String[] args) {
        es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 3);
        // es = ForkJoinPool.commonPool();
        // es = Executors.newFixedThreadPool(2);
        // es = Executors.newCachedThreadPool();
        launch(args);
        es.shutdown();
    }

    public void start(Stage primaryStage) {

        // Particle part1 = new Particle(20,90,70,400);
        //
        // ballPane.getChildren().add(part1.getBall());
        // addParticlesByDistance(n, startX, startY, endX, endY, velocity, angle,
        // ballPane);

        ballPane.setLayoutX(270);
        ballPane.setMinHeight(720);
        ballPane.setMinWidth(1280);
        
        paneControl.setMaxWidth(250);
        
        fpsLabel.setLayoutX(1450);
        fpsLabel.setLayoutY(10);
       
        notif.setText(":)");

        gridPane.setAlignment(Pos.BASELINE_CENTER);

        tester.setMaxSize(250, 720);
        // gpContainer.addRow(0, paneControl, separatorV, canvas);

        // particles.forEach(p->ballPane.getChildren().add(p.getBall()));

        // paneContainer.getChildren().addAll(paneControl, paneBall, fpsLabel);

        // paneContainer.getChildren().addAll(paneControl, ballPane, fpsLabel);

        // Scene scene = new Scene(paneContainer, 1530, 720);

        separatorV.setOrientation(Orientation.VERTICAL);

        // Platform.runLater(new Runnable() {public void run(){
//        gridPane.setAlignment(Pos.BASELINE_CENTER);
//        gridPane.addRow(0, labelStartX, inputStartX);
//
//        gridPane.addRow(1, labelStartY, inputStartY);
//
//        gridPane.addRow(2, labelEndX, inputEndX);
//        gridPane.addRow(3, labelEndY, inputEndY);
//        gridPane.addRow(4, separator1);
//        gridPane.addRow(5, labelStartAngle, inputStartAngle);
//        gridPane.addRow(6, labelEndAngle, inputEndAngle);
//        gridPane.addRow(7, separator2);
//        gridPane.addRow(8, labelStartVelocity, inputStartVelocity);
//        gridPane.addRow(9, labelEndVelocity, inputEndVelocity);
//        gridPane.addRow(10, separator3);
//        gridPane.addRow(11, labelVelocity, inputVelocity);
//        gridPane.addRow(12, labelAngle, inputAngle);
//        gridPane.addRow(13, labelCount, inputCount);

        gridPane.setMaxWidth(250);

//        gpControl.addRow(0, gridPane);
//        gpControl.addRow(1, btnAddByDistance);
//        gpControl.addRow(2, btnAddByAngle);
//        gpControl.addRow(3, btnAddByVelocity);
//        gpControl.addRow(4, tester);


        btnAddByDistance.setPrefWidth(250);
        btnAddByVelocity.setPrefWidth(250);
        btnAddByAngle.setPrefWidth(250);

        paneTab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        paneTab.getTabs().addAll(tabDistance,tabAngle,tabVelocity);

        //DISTANCE tab open by default
        initTabDist();
        
        
        
        gpStartXY.addRow(0, inputStartX, inputStartY);
        gpStartEndVelocity.addRow(0, inputStartVelocity, inputEndVelocity);
        gpEndXY.addRow(0, inputEndX, inputEndY);
        gpStartEndAngle.addRow(0, inputStartAngle, inputEndAngle);

        
        gpStartEndVelocity.setMaxWidth(250);
        gpStartEndAngle.setMaxWidth(250);
        gpStartXY.setMaxWidth(250);
        gpEndXY.setMaxWidth(250);
        gpVelocity.setMaxWidth(250);
        gpDistance.setMaxWidth(250);
        gpAngle.setMaxWidth(250);
        tabVelocity.setContent(gpVelocity);
        tabDistance.setContent(gpDistance);
        tabAngle.setContent(gpAngle);

        paneControl.getChildren().add(paneTab);
        
        gpContainer.addRow(0, paneControl, separatorV, ballPane);
        paneContainer.getChildren().addAll(gpContainer, fpsLabel);
        
        tabVelocity.setOnSelectionChanged(e -> {
        	tabDistance.setText("Distance");
        	tabAngle.setText("Angle");
        	tabVelocity.setText("Add by Velocity");
        	
        	gpDistance.getChildren().clear();
        	gpAngle.getChildren().clear();
        	gpVelocity.getChildren().clear();
            initTabVelocity();
            gpVelocity.setMaxWidth(250);
            
        });
        
        tabDistance.setOnSelectionChanged(e -> {
        	tabDistance.setText("Add by Distance");
        	tabAngle.setText("Angle");
        	tabVelocity.setText("Velocity");

        	gpDistance.getChildren().clear();
        	gpAngle.getChildren().clear();
        	gpVelocity.getChildren().clear();
        	
        	initTabDist();
        });
        
        tabAngle.setOnSelectionChanged(e -> {
        	tabDistance.setText("Distance");
        	tabAngle.setText("Add by Angle");
        	tabVelocity.setText("Velocity");
        	
        	initTabAngle();
        });
        
        

        Scene scene = new Scene(paneContainer);

        primaryStage.setTitle("Particle Physics Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        btnAddByDistance.setOnAction(event -> {
            double startX = Double.parseDouble(inputStartX.getText());
            double startY = Double.parseDouble(inputStartY.getText());
            double endX = Double.parseDouble(inputEndX.getText());
            double endY = Double.parseDouble(inputEndY.getText());
            double velocity = Double.parseDouble(inputVelocity.getText());
            double angle = Double.parseDouble(inputAngle.getText());
            int n = Integer.parseInt(inputCount.getText());
            //it errrors if may null input
            try {
                if (n > 0)
                    addParticlesByDistance(n, startX, startY, endX, endY, velocity, angle, ballPane);
            } catch (NumberFormatException e) {
                notif.setText("Invalid input\n");
            };
        });

        btnAddByAngle.setOnAction(event -> {
            final double startX = Double.parseDouble(inputStartX.getText());
            final double startY = Double.parseDouble(inputStartY.getText());
            final double startAngle = Double.parseDouble(inputStartAngle.getText());
            final double endAngle = Double.parseDouble(inputEndAngle.getText());
            final double velocity = Double.parseDouble(inputVelocity.getText());

            final int n = Integer.parseInt(inputCount.getText());
            try {
                if (n > 0)
                    addParticlesByAngle(n, startX, startY, startAngle, endAngle, velocity, ballPane);
                // Platform.runLater(
                // ()->addParticlesByAngle( n, startX, startY, startAngle, endAngle, velocity,
                // ballPane));
            } catch (NumberFormatException e) {
                notif.setText("Invalid input\n");
            }
        });

        btnAddByVelocity.setOnAction(event -> {
            final double startX = Double.parseDouble(inputStartX.getText());
            final double startY = Double.parseDouble(inputStartY.getText());
            final double startVelocity = Double.parseDouble(inputStartVelocity.getText());
            final double endVelocity = Double.parseDouble(inputEndVelocity.getText());
            final double angle = Double.parseDouble(inputAngle.getText());

            final int n = Integer.parseInt(inputCount.getText());
            try {
                if (n > 0)
                    addParticlesByVelocity(n, startX, startY, startVelocity, endVelocity, angle, ballPane);
                // Platform.runLater(()->addParticlesByVelocity(n,startX,startY,startVelocity,endVelocity,angle,ballPane));
            } catch (NumberFormatException e) {
                notif.setText("Invalid input\n");
            }
        });

        System.nanoTime();
        lastFPSTime = System.nanoTime();
        new AnimationTimer() {
            @Override
            public void handle(long now) {

                frameCount++;
                try {
                    // double curr = now - lastFPSTime ;
                    update(now);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }.start();
    }

    private synchronized void addBall(Particle p) {
        ball_buf.add(p);
    }

    private synchronized void clrBall() {
        ball_buf.clear();
    }

    private synchronized List<Circle> balls() {
        try {
        	List<Circle> circles = es.invokeAll(ball_buf).parallelStream().map(t -> {
                try {
                    return t.get();
                } catch (InterruptedException | ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
        	ball_buf.forEach(Particle::play);
            return circles;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    private synchronized List<Circle> sballs() {
        return ball_buf.parallelStream().map(t -> {
            try {
                return t.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

    }
    
    private void initTabVelocity() {
    	gpDistance.getChildren().clear();
		gpAngle.getChildren().clear();
		gpVelocity.getChildren().clear();
		
        gpVelocity.addRow(0, labelConstXY);
        gpVelocity.addRow(1, gpStartXY);
    	gpVelocity.addRow(2, labelStartEndVelocity);
        gpVelocity.addRow(3, gpStartEndVelocity);
        gpVelocity.addRow(4, labelAngle);
        gpVelocity.addRow(5, inputAngle);
        gpVelocity.addRow(6, separator1);
        gpVelocity.addRow(7, labelCount);
        gpVelocity.addRow(8, inputCount);
        gpVelocity.addRow(9, btnAddByVelocity);
        gpVelocity.setMaxWidth(250);
        gpVelocity.addRow(10, notif);
    }
    private void initTabDist() {
    	gpDistance.getChildren().clear();
		gpAngle.getChildren().clear();
		gpVelocity.getChildren().clear();
		
	    gpDistance.addRow(0, labelStartXY);
	    gpDistance.addRow(1, gpStartXY);
	    gpDistance.addRow(2, labelEndXY);
	    gpDistance.addRow(3, gpEndXY);
	    gpDistance.addRow(4, labelVelocity);
	    gpDistance.addRow(5, inputVelocity);
	    gpDistance.addRow(6, labelAngle);
	    gpDistance.addRow(7, inputAngle);
	    gpDistance.addRow(8, separator1);
	    gpDistance.addRow(9, labelCount);
	    gpDistance.addRow(10, inputCount);
	    gpDistance.addRow(11, btnAddByDistance);
	    gpDistance.setMaxWidth(250);
        gpDistance.addRow(12, notif);

    }
    private void initTabAngle()
    {
    	gpDistance.getChildren().clear();
		gpAngle.getChildren().clear();
		gpVelocity.getChildren().clear();
		
		gpAngle.addRow(0, labelConstXY);
	    gpAngle.addRow(1, gpStartXY);
	    gpAngle.addRow(2, labelStartEndAngle);
		gpAngle.addRow(3, gpStartEndAngle);
	    gpAngle.addRow(4, labelVelocity);
	    gpAngle.addRow(5, inputVelocity);
	    gpAngle.addRow(6, separator1);
	    gpAngle.addRow(7, labelCount);
	    gpAngle.addRow(8, inputCount);
	    gpAngle.addRow(9, btnAddByAngle);
	    gpAngle.setMaxWidth(250);
	    gpAngle.addRow(10, notif);

    }
    
    private void addParticlesByDistance(int n, double startX, double startY, double endX, double endY, double velocity,
            double angle, Pane ballPane) {
        double dx = (endX - startX) / (n);
        double dy = (endY - startY) / (n);
        double x = startX;
        double y = startY; // Assuming 720 is the height of your pane and you want to invert the
                           // y-coordinate

        for (int i = 0; i < n; i++) {
            double xin = x, yin = y;
            
            addBall(new Particle(xin, yin, Math.toRadians(angle), velocity));
            x += dx;
            y += dy;
        }

        drawBalls(ballPane);

        // ballPane.getChildren().add(p.getBall());

        // ballPane.getChildren().addAll(tasks.stream().map(task -> {
        // try {
        // return task.get().getBall();
        // } catch (InterruptedException | ExecutionException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // return null;
        // }).collect(Collectors.toList()));

        // tester.appendText("Ball " + i + ": " + x + "," + y + ", v,a:" + velocity +
        // "," + angle + "\n");
    }

    private void drawBalls(Pane ballPane) {
        try {
            // ballPane.getChildren().addAll(sballs());
            ballPane.getChildren().addAll(balls());
            clrBall();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void addParticlesByAngle(int n, double startX, double startY, double startAngle, double endAngle,
            double velocity, Pane paneBall) {
        double angleDiff = (endAngle - startAngle) / (n);
        double angle = startAngle;
        for (int i = 0; i < n; i++) {
            addBall(new Particle(startX, startY, Math.toRadians(angle), velocity));
            angle += angleDiff;
            // negative sin because Y increases downwards\
            // // es.execute(new Task<Void>(){
            //
            // protected Void call(){
            //
            // Platform.runLater(()-> paneBall.getChildren().add(new Particle(startX,
            // startY, Math.toRadians(angle), velocity).getBall()));
            // return null;
            // }
            //
            // });
            // }

        }
        drawBalls(paneBall);
    }

    private void addParticlesByVelocity(int n, double startX, double startY, double startVelocity, double endVelocity,
            double angle, Pane ballPane) {
        double velocityDiff = (endVelocity - startVelocity) / (n);
        double v = startVelocity;
        for (int i = 0; i < n; i++) {

            double velocity = v;

            addBall(new Particle(startX, startY, Math.toRadians(angle), velocity));
            // Platform.runLater(()->
            // ballPane.getChildren().add(new Particle(startX, startY,
            // Math.toRadians(angle), velocity).getBall());
            // );
            v += velocityDiff;

        }
        drawBalls(ballPane);
    }

    private void update(long now) {

        double curr = now - lastFPSTime;

        if (curr < 500_000_000)
            return;
        fps = frameCount * 1_000_000_000.0 / curr;
        frameCount = 0;
        lastFPSTime = now;
        fpsLabel.setText(String.format("FPS: %.2f", fps));

    }

}