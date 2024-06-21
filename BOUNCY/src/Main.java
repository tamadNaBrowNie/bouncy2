import javafx.scene.layout.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Panel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
//import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javafx.scene.layout.BackgroundFill;

import java.lang.Math; //foor squareroot
import java.util.ArrayList;
import java.util.List;

//Ditey yung main gui bale
public class Main extends Application{
	
    public static void main(String[] args) {
    	//call new gui
//    	new Main().setVisible(true); //old jframe ver
    	launch(args);
    }

    private int current_n_particles = 0;   //tester
	private int fps = 0;//frameount
	Graphics g;
	Particle[] p = new Particle[100000];
    private List<Particle> particles = new ArrayList<>();

    ArrayList<Circle> balls;
    
	int points_x[];
	int points_y[];
    
	//when launch, call here
	public void start(Stage primaryStage) throws Exception{
		balls= new ArrayList<Circle>();
	//		JFrame root = new JFrame();
	//		root.setSize(1530,720); //in px-> 1280+250
	//		root.setDefaultCloseOperation(EXIT_ON_CLOSE);
	//		root.setLocationRelativeTo(null);
			
			//panelContainer looks like this BallPanel(1280,720) | ControlPanel(100,720)
			Pane paneContainer = new Pane();
	//		panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.X_AXIS));
			
			Pane paneBall = new Pane();
	//		panelBall.setBackground(Color.red);
	//		panelBall.setPreferredSize(new Dimension(1280, 720));
	
			Pane paneControl = new Pane();
	//		panelControl.setPreferredSize(new Dimension(250, 720));
	//		panelBall.setBackground(Color.white);
			
			//add to container
	//		panelContainer.add(panelBall);
	//		panelContainer.add(panelControl);		
			
			/*
			Particles can be added in batches. This is in three forms:
				 • Provide an integer n indicating the number of particles to
				 add. Keep the velocity and angle constant. Provide a start
				 point and end point. Particles are added with a uniform
				 distance between the given start and end points.
				 
				 • Provide an integer n indicating the number of particles to
				 add. Keep the start point and velocity constant. Provide a
				 start Θ and end Θ. Particles are added with uniform distance
				 between the given start Θ and end Θ.
				 
				 • Provide an integer n indicating the number of particles to
				 add. Keep the start point and angle constant. Provide a start
				 velocity and end velocity. Particles are added with a uniform
				 difference between the given start and end velocities.
			 */
				 
			//int n = num of particles to add
			//constant velocity,angle / startpoint,velocity / startpoint,angle
			//CHECK IF EMPTY:
			// start point, end point -> if not empty then type=1
			// start angle, end angle -> then type 2
			// start velocity, end velocity -> then type 2 
			
			
			
			int type = 0;
			
			Label labelHeader = new Label("Please leave textfield blank if inapplicable.");
			Label labelNParticles = new Label("Number of Particles");
			
			Label labelStartEndPos = new Label("Start and End Points [x,y]: ");
			Label labelStart = new Label("Start: ");
			Label labelEnd = new Label("End: ");

			Label labelStartEndAngle = new Label("Start and End Angles: ");
			Label labelStartEndVelocity = new Label("Start and End Velocity: ");
			
			Label labelDesignation = new Label("");
			
			TextField inputNParticles= new TextField();
			
			//Type 1 Particles are added with a uniform distance between the given start and end points, if provided inputs
			TextField inputStartX= new TextField();
			inputStartX.setMaxWidth(100);
			TextField inputStartY= new TextField();
			inputStartY.setMaxWidth(100);
			TextField inputEndX= new TextField();
			inputEndX.setMaxWidth(100);
			TextField inputEndY= new TextField();
			inputEndY.setMaxWidth(100);
			
			//Type 2 Particles are added with uniform distance between the given start Θ and end Θ.
			TextField inputStartAngle= new TextField();
			TextField inputEndAngle= new TextField();
			
			//Type 3 if velocities are provided: Particles are added with a uniform difference between the given start and end velocities.
			TextField inputStartVelocity= new TextField();
			TextField inputEndVelocity= new TextField();
			
			GridPane gpInputs = new GridPane();
			gpInputs.setMaxWidth(250);
			gpInputs.addRow(0, labelHeader);
			
			//table form para neat
//			gpInputRow1.addRow(0, labelNParticles, inputNParticles);
			gpInputs.addRow(1, labelNParticles);
			gpInputs.addRow(2, inputNParticles);
		
			gpInputs.addRow(3, labelStartEndPos);
			
			GridPane gpInputStartXY = new GridPane();
			GridPane gpInputEndXY = new GridPane();
			GridPane gpInputVelocities = new GridPane();
			GridPane gpInputAngles = new GridPane();

			gpInputStartXY.addRow(0, labelStart, inputStartX, inputStartY);
			gpInputs.addRow(4, gpInputStartXY);
			gpInputEndXY.addRow(0, labelEnd, inputEndX, inputEndY);
			gpInputs.addRow(5, gpInputEndXY);
			
//			gpInputs.addRow(3, gpInputRow1);
			

			gpInputAngles.addRow(0, inputStartAngle, inputEndAngle);
			gpInputs.addRow(6, labelStartEndAngle);
			gpInputs.addRow(7, gpInputAngles);

			gpInputVelocities.addRow(0, inputStartVelocity, inputEndVelocity);
			gpInputs.addRow(8, labelStartEndVelocity);
			gpInputs.addRow(9, gpInputVelocities);
			
	        Button btnSubmit = new Button("ADD PARTICLE BATCH");
			gpInputs.addRow(10, btnSubmit);
			
	        GridPane gridPane = new GridPane();
	        GridPane gpControl = new GridPane();
	        gpControl.setAlignment(Pos.BASELINE_CENTER);
	        
	//        VBox vboxControl = new VBox();
	//        TextArea txtSolution = new TextArea("");
	
	        Label labelDeg = new Label("Degrees:");
	        TextField inputDeg = new TextField();
	
	        Label labelVel = new Label("Velocity (px/s):");
	        TextField inputVel = new TextField();
	
	        paneBall.setMinWidth(1280);
//	        Background bgVal = new Background();
//	        BackgroundFill bgBal = new BackgroundFill(Color.RED, null, null);
//	        Pane  pane  = new Pane();

//	        BackgroundFill backgroundFill =
//	                new BackgroundFill(
//	                        new Paint(true, Color.valueOf("#FFFFFF")),
//	                        new CornerRadii(10),
//	                        new Insets(0,0,0,0)
//	                        );

//	        Background background =
//	                new Background(backgroundFill);
//
//	        pane.setBackground(background);
	        
	        
	        Pane canvasTest = new Pane();
//	        Panel panel = new Panel();
//	        panel.setForeground(Color.GREEN);
	        
	        
	        //ADDING A BALL IS LIKE THIS
//	        canvasTest.setBackground(new Background(BackgroundFill(Paint(Color.DARKRED), CornerRadii radii, Insets insets)));
	        //this ball now can be seen
//	        
	        Circle ball = new Circle(10); //makes it black withoutsacrificing awt.Circle
//	        ball.setStyle(value);
//	    	Circle ball = new Circle(10, Color.RED);
	        ball.relocate(1200, 500);
	        
	        canvasTest.getChildren().addAll(balls);
	        
	        
	        
//	        canvasTest.add(panel);


            paneBall.getChildren().add(canvasTest);
            
            
//	        stage.setTitle("Moving Ball");
//	        stage.setScene(scene);
//	        stage.show();
	        
	        
	        
	        
//	        Bounds bounds = canvas.getBoundsInLocal();
//	        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), 
//	                new KeyValue(ball.layoutXProperty(), bounds.getMaxX()-ball.getRadius())));
//	        timeline.setCycleCount(2);
//	        timeline.play();
	        
	        
//	        
//	        paneBall.setBackground(new Background(
//	        		new BackgroundFill(
//	                        new Paint(true, Color.valueOf("#FFFFFF")),
//	                        new CornerRadii(10),
//	                        new Insets(0,0,0,0)
//	                        );
	        
	        Label testLabel = new Label("TEST SHOULD APPEAR ON THE PANEL FOR THE BALL");
            paneBall.getChildren().add(testLabel);
	        
	        
	        paneControl.setMaxWidth(250);
	        TextArea tester = new TextArea("(Test) Balls rn:\n");
	        tester.setMaxSize(250, 720);
//	        
//	        gpControl.addRow(0,gridPane);
//	        gpControl.addRow(1,btnSubmit);
//	        gpControl.addRow(2,tester);
//	        
	        
	        gpInputs.addRow(11, tester);
	        
	        GridPane gpContainer = new GridPane();
	        gpContainer.addRow(0, gpInputs);
	        
	        
	        //DIDNT WORK
	        Canvas canvas = new Canvas(1280, 720);
	        GraphicsContext gc = canvas.getGraphicsContext2D();
//	        canvas.setStyle("-fx-background-color: black;");
//	        canvas.setBackground(Color.red);
//	        gc.setFill(PaintContext p); <---- causes issues
	        
	        //TEST IF MAG SHOW USING IBA
//	        Panel panel = new Panel();
//	        panel.setBackground(Color.green);
//	        paneBall.getChildren().add(canvas);
//	        paneBall.getChildren().addanel);/
	        gpContainer.addRow(0, paneBall);
	        
	        
	        
	        Scene scene = new Scene(gpContainer);

	        primaryStage.setTitle("BOUNCING BALLS - grp i forgot num");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        primaryStage.setHeight(720);
//	        primaryStage.setWidth(1530);
	             
	        
	        btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                //clear first
	                String temp = "";
	                
	                /*
//	                inputStartX.getText();
//	    			inputStartY.getText();
//	    			inputEndX.getText();
//	    			inputEndY.getText();
	    			
	    			//Type 2 Particles are added with uniform distance between the given start Θ and end Θ.
	    			inputStartAngle.getText();
	    			inputEndAngle.getText();
	    			
	    			//Type 3 if velocities are provided: Particles are added with a uniform difference between the given start and end velocities.
	    			inputStartVelocity.getText();
	    			inputEndVelocity.getText();
	                */
	                
	                //THIS IS CONSIDERING NA WALANG NEGATIVES
	                //when clicked, see which type oof spawning particles u want by checking which ones are constant/same start and ends
	                
	                
	                //TODO: make it so that it also gets if negative number dito
	                int x1= Integer.parseInt(inputStartX.getText());
	                int x2 = Integer.parseInt(inputEndX.getText());
	                int y1= Integer.parseInt(inputStartY.getText()); 
	                int y2= Integer.parseInt(inputEndY.getText());
	                
	                double v1 = Double.parseDouble(inputStartVelocity.getText());
	                double v2 =Double.parseDouble(inputEndVelocity.getText());
	                
	                double a1 = Double.parseDouble(inputStartAngle.getText());
	                double a2 =  Double.parseDouble(inputEndAngle.getText());
	                
	                int formType = 0; //pag 0 invalid
	                
	                
	                if(inputNParticles.getText().isEmpty())
	                		{
                				testLabel.setText("PARTICLES COUNT MUST NOT BE LEFT BLANK");
	                		}
	                else {
	                	int n_particles = Integer.parseInt(inputNParticles.getText(),10);
	                	
		                if ((x1 == x2) && (y1==y2))
		                {
		                	//starting and ending points are constants, so two options: 2 or 3rd type
		                	if (v1 == v2)
		                	{
		                		if (a1==a2) {
		                			//all are constants, does not fit spawn criteria, so pass
			                		testLabel.setText("All are CONSTANTS, does not fit spawn criteria, must have 2 constant factors ONLY.");
		                		}
		                		else {
	
			                		testLabel.setText("START/END POS, AND VELOCITIES ARE CONSTANT! (Type2)");
			                		formType=2;
		                		}
		                	}
		                	else {
		                		testLabel.setText("START/END POS, AND ANGLES ARE CONSTANT! (Type3)");
		                		formType=3;
		                	}
		                	
		                }
		                else {
		                	if (v1==v2 && a1==a2)	
		                	{
		                		testLabel.setText("VELOCITIES AND ANGLES ARE CONSTANT! (Type1)");
		                		formType=1;
		                		//if type 1 pasok dito, Particles are added with a uniform distance between the given start and end points.
		                		//TODO: calculate distances of points
		                		tester.appendText("DISTANCE FORMULA, straight line between pt 1 and pt 2 div n_partickes\n\n");
		                		//d=√((x2 – x1)² + (y2 – y1)²)
		                		double distanceSqrX = (x2-x1) * (x2-x1);
		                		double distanceSqrY = (y2-y1) * (y2-y1);
		                		double distanceFinal = Math.sqrt(distanceSqrX+distanceSqrY);
		                		tester.appendText("Distance: "+ distanceFinal + "\nPoints in between number of px (estimate, will not work for 1 particle): "+ Math.round(distanceFinal/n_particles)+"\n");
		                		
		                		
		                		//line formula -> y=mx+b, where m is slope
		                		// m = y2-y1 / x2-x1
		                		//^ to find the points  in between
		                		//
		                		
		                		//experimental:
		                		int distanceOfX = Math.abs(x2-x1);
		                		int distanceOfY = Math.abs(y2-y1);
		                		
		                		//what if find increments of points between x
		                		//and find increments of n_particle points between y
		                		//tapos yun ung plot na lol, simplistic pero tanga ako sa math so it fits it sits
		                		
		                		//from starting point to end point, find all points
		                		
		                		//how many partitions= n_particles-1 (di kasama starting point)
		                		int inc_x=Math.round(distanceOfX/n_particles);
		                		int inc_y=Math.round(distanceOfY/n_particles);
		                		int smaller_x=0;
		                		int smaller_y=0;
		                		//find which is less
		                		boolean goingRight = false;
		                		boolean goingUp = false;
		                		
		                		if (x1<x2) {
			                		smaller_x=x1;
			                		goingRight=true;
		                		}
		                		else {
		                			smaller_x=x2;
		                		}
		                		if (y1<y2) {
		                			smaller_y=y1;
		                			goingUp = true;
		                		}
		                		else {
		                			smaller_y=y2;
		                		}
		                		
		                		for(int i=0; i<n_particles; i++)
		                		{
		                			if (goingUp){
		                				if (goingRight)
		                				{
		                					p[i] = new Particle(smaller_x,x2,smaller_y,y2,a1,a2,v1,v2,formType,gc);
		                				}
		                				else {
		                					p[i] = new Particle(x1,smaller_x,smaller_y,y2,a1,a2,v1,v2,formType,gc);
		                				}
		                			}
		                			else {
		                				if (goingRight)
		                				{
		                					p[i] = new Particle(smaller_x,x2,y1,smaller_y,a1,a2,v1,v2,formType,gc);
		                				}
		                				else {
		                					p[i] = new Particle(x1,smaller_x,y1,smaller_y,a1,a2,v1,v2,formType,gc);
		                				}
		                			}
		                			
		                			tester.appendText("["+smaller_x+",");
		                			smaller_x+=inc_x;
		                			
		                			tester.appendText(""+smaller_y+"]\n");
		                			smaller_y+=inc_y;
				                	
		                		}
		                		
		                		
			                		//draw graphics and update the movement/bounce of he particle should be at Particle.java
//			                	}
		                	}
		                	else {
		                		testLabel.setText("No constants, does not fit spawn criteria, must have 2 combination of constant start/end.");
		                	}
		                	
		                	
		               current_n_particles++;
	                }}
	            }});
	        
	        
	        
	        //FPS THINGY
	        //TODO
	        
		}
//
//
//    private void update(long now) {
//        double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
//        lastUpdateTime = now;
//        for (Particle particle : particles) {
//            particle.update(deltaTime, canvas.getWidth(), canvas.getHeight());
//        }
//
//        frameCount++;
//        if (frameCount % 30 == 0) { // Update FPS every 30 frames
//            fps = 30.0 / deltaTime;
//            fpsLabel.setText(String.format("FPS: %.2f", fps));
//        }
//    }
//
//    private void draw() {
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//        for (Particle particle : particles) {
//            particle.draw(gc);
//        }
//    }
}
	
	
	
	
	
	