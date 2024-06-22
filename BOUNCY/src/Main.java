import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    private int n_balls = 0;
    private List<Particle> particles = new ArrayList<>();
    private Canvas canvas;
    private Label fpsLabel;
    private long lastUpdateTime;
    private long lastFPSTime;
    private int frameCount;
    private double fps;
    public static ExecutorService es;
    private Pane paneBall;
    
    public static void main(String[] args) {
    	es = Executors.newCachedThreadPool();
        launch(args);
        es.shutdown();
    }

    public void start(Stage primaryStage) {
        paneBall = new Pane();

        Pane paneContainer = new Pane();
        canvas = new Canvas(1280, 720);
//        Pane paneBall = new Pane();
        paneBall.setLayoutX(270);
        paneBall.setMinHeight(720);
        paneBall.setMinWidth(1280);

        fpsLabel = new Label("FPS: 0");
        fpsLabel.setLayoutX(50);
        fpsLabel.setLayoutY(600);

        Pane paneControl = new Pane();
        paneControl.setMaxWidth(250);

        GridPane gridPane = new GridPane(); 
        gridPane.setAlignment(Pos.BASELINE_CENTER);

        Label labelStartX = new Label("Start X:");
        TextField inputStartX = new TextField();
        Label labelStartY = new Label("Start Y:");
        TextField inputStartY = new TextField();
        Label labelEndX = new Label("End X:");
        TextField inputEndX = new TextField();
        Label labelEndY = new Label("End Y:");
        TextField inputEndY = new TextField();
        Label labelStartAngle = new Label("Start Angle:");
        TextField inputStartAngle = new TextField();
        Label labelEndAngle = new Label("End Angle:");
        TextField inputEndAngle = new TextField();
        Label labelStartVelocity = new Label("Start Velocity:");
        TextField inputStartVelocity = new TextField();
        Label labelEndVelocity = new Label("End Velocity:");
        TextField inputEndVelocity = new TextField();
        Label labelVelocity = new Label("Velocity (px/s):");
        TextField inputVelocity = new TextField();
        Label labelAngle = new Label("Angle (degrees):");
        TextField inputAngle = new TextField();
        Label labelCount = new Label("Number of Particles:");
        TextField inputCount = new TextField();

        Button btnAddByDistance = new Button("Add by Distance");
        Button btnAddByAngle = new Button("Add by Angle");
        Button btnAddByVelocity = new Button("Add by Velocity");


        Separator separator1 = new Separator();
        Separator separator2 = new Separator();
        Separator separator3 = new Separator();
        Separator separatorV = new Separator();
        separatorV.setOrientation(Orientation.VERTICAL);
        
        gridPane.setAlignment(Pos.BASELINE_CENTER);
        gridPane.addRow(0, labelStartX, inputStartX);
        gridPane.addRow(1, labelEndX, inputEndX);
        gridPane.addRow(2, labelStartY, inputStartY);
        gridPane.addRow(3, labelEndY, inputEndY);
        gridPane.addRow(4, separator1);
        gridPane.addRow(5, labelStartAngle, inputStartAngle);
        gridPane.addRow(6, labelEndAngle, inputEndAngle);
        gridPane.addRow(7, separator2);
        gridPane.addRow(8, labelStartVelocity, inputStartVelocity);
        gridPane.addRow(9, labelEndVelocity, inputEndVelocity);
        gridPane.addRow(10, separator3);
        gridPane.addRow(11, labelVelocity, inputVelocity);
        gridPane.addRow(12, labelAngle, inputAngle);
        gridPane.addRow(13, labelCount, inputCount);

        TextArea tester = new TextArea("(Test) Balls rn:\n");
        tester.setMaxSize(250, 720);

        GridPane gpControl = new GridPane();
        gpControl.addRow(0, gridPane);
        gpControl.addRow(1, btnAddByDistance);
        gpControl.addRow(2, btnAddByAngle);
        gpControl.addRow(3, btnAddByVelocity);
        gpControl.addRow(4, tester);

        paneControl.getChildren().add(gpControl);
//        paneContainer.getChildren().addAll(paneControl, canvas, fpsLabel);
        
        GridPane gpContainer = new GridPane();
//        gpContainer.addRow(0, paneControl, separatorV, canvas);
        
        
        
        
        
        
        
        
        
        
        //TESTING START EHREE
//        int, double, double, double, double, double, double, Pane) in the type 
//        addParticlesByDistance(5, 5, 3, 2, 30, 2, 4, paneBall);
//        paneBall.getChildren().add(new Particle())
        
        
//        double x,double  y,	double theta, double v, Pane canvas
//        test particle
        Particle testP = new Particle(100,200,3,4,paneBall);
        particles.add(testP);
        Particle testP2 = new Particle(0,50,6,74, paneBall);
        particles.add(testP2);
        
        labelStartX.setText(particles.size()+" particle size:");
        paneBall.getChildren().add(particles.get(0).getBall()); //add the CIRCLE to the pane
        paneBall.getChildren().add(particles.get(1).getBall()); //add the CIRCLE to the pane

        gpContainer.addRow(0, paneControl, separatorV, paneBall);
        
        paneContainer.getChildren().addAll(gpContainer, fpsLabel);
        
        
        
        
        
        
        
        
//        paneContainer.getChildren().addAll(paneControl, paneBall, fpsLabel);

//      paneContainer.getChildren().addAll(paneControl, paneBall, fpsLabel);
//        Scene scene = new Scene(paneContainer, 1530, 720);
        Scene scene = new Scene(paneContainer,1280, 720, Color.ALICEBLUE);

        primaryStage.setTitle("Particle Physics Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        btnAddByDistance.setOnAction(event -> {
            try {
                double startX = Double.parseDouble(inputStartX.getText());
                double startY = Double.parseDouble(inputStartY.getText());
                double endX = Double.parseDouble(inputEndX.getText());
                double endY = Double.parseDouble(inputEndY.getText());
                double velocity = Double.parseDouble(inputVelocity.getText());
                double angle = Double.parseDouble(inputAngle.getText());

                int n = Integer.parseInt(inputCount.getText()); // ayaw umayos, n laging 1
                tester.appendText(n + " particles added with constant velocity and angle\n");

                
                Platform.runLater(
                    new Runnable(){
                        public void run(){
                	addParticlesByDistance(n, startX, startY, endX, endY, velocity, angle, paneBall);
                        	}});
                n_balls = particles.size();
                
            
                //clear particles for reuse

                
            } catch (NumberFormatException e) {
                tester.appendText("Invalid input\n");
            }
        });

        btnAddByAngle.setOnAction(event -> {
            try {
                double startX = Double.parseDouble(inputStartX.getText());
                double startY = Double.parseDouble(inputStartY.getText());
                double startAngle = Double.parseDouble(inputStartAngle.getText());
                double endAngle = Double.parseDouble(inputEndAngle.getText());
                double velocity = Double.parseDouble(inputVelocity.getText());

                final int n = Integer.parseInt(inputCount.getText());
                Platform.runLater(
                    new Runnable(){
                        public void run(){
                            addParticlesByAngle(n, startX, startY, startAngle, endAngle, velocity,paneBall);
                        }
                    }
                );
                n_balls = particles.size();
                tester.appendText(n + " particles added with constant start point and velocity\n");
            } catch (NumberFormatException e) {
                tester.appendText("Invalid input\n");
            }
        });

        btnAddByVelocity.setOnAction(event -> {
            try {
                double startX = Double.parseDouble(inputStartX.getText());
                double startY = Double.parseDouble(inputStartY.getText());
                double startVelocity = Double.parseDouble(inputStartVelocity.getText());
                double endVelocity = Double.parseDouble(inputEndVelocity.getText());
                double angle = Double.parseDouble(inputAngle.getText());

                final int n = Integer.parseInt(inputCount.getText());
                Platform.runLater(
                    new Runnable(){
                        public void run(){
                addParticlesByVelocity(n, startX, startY, startVelocity, endVelocity, angle,paneBall);}});

                n_balls = particles.size();
                tester.appendText(n + " particles added with constant start point and angle\n");
            } catch (NumberFormatException e) {
                tester.appendText("Invalid input\n");
            }
        });

        lastUpdateTime = System.nanoTime();
        lastFPSTime = System.nanoTime();
        frameCount = 0;
        
        try {
			es.invokeAll(particles);
            //particles.forEach(p->p.call());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
					update(now);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                draw();
            }
        }.start();
    }

    private void addParticlesByDistance(int n, double startX, double startY, double endX, double endY, double velocity, double angle, Pane paneBall) {
      //  double totalDistance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        double dx = (endX - startX) / (n - 1);
        double dy = (endY - startY) / (n - 1);
        
        for (int i = 0; i < n; i++) {
            double x = startX + i * dx;
            double y = 720-(startY + i * dy);
            Particle p = new Particle(x, y, velocity, Math.toRadians(angle), paneBall);
            particles.add(p);
            paneBall.getChildren().add(p.getBall());
            x+=dx;
            y+=dy;
            
        }
    }

    private void addParticlesByAngle(int n, double startX, double startY, double startAngle, double endAngle, double velocity, Pane paneBall) {
        double angleDiff = (endAngle - startAngle) / (n - 1);

        for (int i = 0; i < n; i++) {
            double angle = startAngle + i * angleDiff;
            double x = startX ;
            double y = 720-startY ; // negative sin because Y increases downwards\
            Particle p = new Particle(x, y, velocity, Math.toRadians(angle), paneBall);
            particles.add(p);
            paneBall.getChildren().add(p.getBall());
        }
    }

    private void addParticlesByVelocity(int n, double startX, double startY, double startVelocity, double endVelocity, double angle, Pane paneBall) {
        double velocityDiff = (endVelocity - startVelocity) / (n - 1);

        for (int i = 0; i < n; i++) {
            double velocity = startVelocity + i * velocityDiff;
            double x = startX ;
            double y = 720-startY ; // negative sin because Y increases downwards     
            Particle p = new Particle(x, y, velocity, Math.toRadians(angle), paneBall);
            particles.add(p);
            paneBall.getChildren().add(p.getBall());
        }
    }
    
    private void update(long now) throws Exception {
        double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
        lastUpdateTime = now;
        frameCount++;
        particles.forEach((p)->{
			try {
				p.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
        // Update FPS display every 0.5 seconds
        if (now - lastFPSTime >= 500_000_000) {
            fps = frameCount / ((now - lastFPSTime) / 1_000_000_000.0);
            fpsLabel.setText(String.format("FPS: %.2f", fps));
            frameCount = 0;
            lastFPSTime = now;
        }
    }
    class p implements Runnable{
    	Particle particle;
    	GraphicsContext gc;
		p(Particle ball,GraphicsContext gc){
			this.particle=ball;
			this.gc =gc;
		}
		public void run(){ 
			gc.setFill(particle.getBall().getFill());
        gc.fillOval(particle.getBall().getCenterX() - particle.getBall().getRadius(),
                    particle.getBall().getCenterY() - particle.getBall().getRadius(),
                    particle.getBall().getRadius() * 2, particle.getBall().getRadius() * 2);}
}
	
    private void draw() {
    	CountDownLatch latch = new CountDownLatch(particles.size());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        for (Particle particle : particles) {
        	
        	es.execute(new p(particle,gc));
        }
        try {
        	  latch.await();
        	} catch (InterruptedException E) {
        	   // handle
        	}
    }
//
//    private void draw() {
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//        for (Particle particle : particles) {
//         //   particle.draw(gc);
//        }
//    }
}