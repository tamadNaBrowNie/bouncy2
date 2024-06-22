import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    private int n_balls = 0;
//    private List<Particle> particles = new ArrayList<>();
    private Canvas canvas;
    private Label fpsLabel;
    private long lastUpdateTime=System.nanoTime();
    private long lastFPSTime= System.nanoTime();;
    private int frameCount = 0;
    private double fps = 0;
    public static ExecutorService es;
    private final List<Particle> particles = Collections.synchronizedList(new ArrayList<>());
    
    public static void main(String[] args) {
    	es = Executors.newCachedThreadPool();
        launch(args);
        es.shutdown();
    }

    public void start(Stage primaryStage) {
        Pane paneContainer = new Pane();
        canvas = new Canvas(1280, 720);
        Pane ballPane = new Pane();
        ballPane.setLayoutX(270);
        ballPane.setMinHeight(720);
        ballPane.setMinWidth(1280);

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


        TextArea tester = new TextArea("(Test) Balls rn:\n");
        tester.setMaxSize(250, 720);

        GridPane gpControl = new GridPane();
        Separator separator1 = new Separator();
        Separator separator2 = new Separator();
        Separator separator3 = new Separator();
        //need diff separators lagi, no reusing
        Separator separatorV = new Separator();
        separatorV.setOrientation(Orientation.VERTICAL);
        
        //Platform.runLater(new Runnable() {public void run(){
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
        

        gridPane.setMaxWidth(250);

        gpControl.addRow(0, gridPane);
        gpControl.addRow(1, btnAddByDistance);
        gpControl.addRow(2, btnAddByAngle);
        gpControl.addRow(3, btnAddByVelocity);
        gpControl.addRow(4, tester);
    // }}
// );
        
        paneControl.getChildren().add(gpControl);
//        paneContainer.getChildren().addAll(paneControl, canvas, fpsLabel);
        
        GridPane gpContainer = new GridPane();
        
//        Particle part1 = new Particle(20,90,70,400);
//        
//        ballPane.getChildren().add(part1.getBall());
//    	addParticlesByDistance(n, startX, startY, endX, endY, velocity, angle, ballPane);

        
        
        
        
        
//        gpContainer.addRow(0, paneControl, separatorV, canvas);
        		
        gpContainer.addRow(0, paneControl, separatorV, ballPane);
        paneContainer.getChildren().addAll(gpContainer, fpsLabel);
        
//        paneContainer.getChildren().addAll(paneControl, paneBall, fpsLabel);

//      paneContainer.getChildren().addAll(paneControl, ballPane, fpsLabel);

//        Scene scene = new Scene(paneContainer, 1530, 720);

        particles.forEach(p->ballPane.getChildren().add(p.getBall()));
      Scene scene = new Scene(paneContainer);

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
				int n = Integer.parseInt(inputCount.getText());
				
				double x = startX;
	            double y = 720 - startY;
				double dx = (endX - startX) / (n);
				double dy = (endY - startY) / (n);
				
				for (int i = 0; i < n; i++) {
	                Particle particle = new Particle(x, y, Math.toRadians(angle), velocity);
	                particles.add(particle);
	                ballPane.getChildren().add(particle.getBall());
	                x += dx;
	                y -= dy;
	                tester.appendText("Ball " + (i+particles.size())+ ": " + x + "," + y + ", v,a:" + velocity + "," + angle + "\n");
				}
				n = particles.size();
				tester.appendText(n + " particles added with constant angle and velocity\n");
            }
        	catch (NumberFormatException e) {
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
	   			int n = Integer.parseInt(inputCount.getText()); 			

                double x = startX ;
                double y = 720-startY ; // negative sin because Y increases downwards\
    	    	double angleDiff = (endAngle - startAngle) / (n);
    			double angle;
    	    	
    	    	for (int i = 0; i < n; i++) {
        			angle = startAngle + i * angleDiff;

    	            Particle p = new Particle(x, y, Math.toRadians(angle), velocity);
    	            particles.add(p);
                    ballPane.getChildren().add(p.getBall());
                    tester.appendText("Ball " + (i+particles.size())+ ": " + x + "," + y + ", v,a:" + velocity + "," + angle + "\n");
    	        }
    			n = particles.size();
	            n_balls = particles.size();
	            tester.appendText(n + " particles added with constant start point and velocity\n");
            }
        	catch (NumberFormatException e) {
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
                int n = Integer.parseInt(inputCount.getText());

            	double velocityDiff = (endVelocity - startVelocity) / (n);
    			double velocity,x,y;
    			
    			for (int i = 0; i < n; i++) {
    				velocity = startVelocity + i * velocityDiff;
    	            x = startX ;
    	            y = 720-startY ; // negative sin because Y increases downwards     
                    Particle particle = new Particle(x, y, Math.toRadians(angle), velocity);
                    particles.add(particle);
                    ballPane.getChildren().add(particle.getBall());
                    tester.appendText("Ball " + (i+particles.size())+ ": " + x + "," + y + ", v,a:" + velocity + "," + angle + "\n");
    			}
    			n = particles.size();
    			tester.appendText(n + " particles added with constant start point and velocity\n");
                n_balls = particles.size();
            } catch (NumberFormatException e) {
                tester.appendText("Invalid input\n");
            }
        });

        
    
         //es.invokeAll(particles);
		 particles.forEach(p->{
			try {
				p.call();	//hangs
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		 
		 
        lastUpdateTime=System.nanoTime();
        lastFPSTime= System.nanoTime();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
            	frameCount++;
                try {
                	//double curr = now - lastFPSTime ;
                   update(now);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

      
            }
       
    }.start();
}

    private void update(long now) {

        double curr = now - lastFPSTime ;
        
        if (curr>=500_000_000) 
        {
                fps = frameCount*1_000_000_000.0 / curr ;
                frameCount = 0;
                lastFPSTime = now;
                fpsLabel.setText(String.format("FPS: %.2f", fps));
        }

        if(now - lastUpdateTime <=16666666.666666667)
            return;
       lastUpdateTime = now;
       // particles.forEach((p)->p.call());
        try {
            es.invokeAll(particles);
            particles.forEach((p)->{
				try {
					p.call();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;


                
    }
        // Platform.runLater(new Runnable() {
        //     public void run() {
        //         fpsLabel.setText(String.format("FPS: %.2f", fps));}
        //     });
			
}