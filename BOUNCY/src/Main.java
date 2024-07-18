import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.sun.corba.se.impl.orbutil.graph.Node;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private List<Particle> ball_buf = new ArrayList<Particle>();
    // private Canvas canvas= new Canvas(X_MAX, Y_MAX);;
    private Label fpsLabel = new Label("FPS: 0");;
    private long lastFPSTime = System.nanoTime();;
    private int frameCount = 0;
    private double fps = 0;
    public static ExecutorService es;
    private boolean hasExplorer = false;
    private boolean isDebug = true; //show control panel initially

    private final double X_MAX =1280, Y_MAX=720;
    private Pane paneContainer = new Pane();

    private Pane ballPane = new Pane();
    
    private Pane paneRight = new Pane();
    
    private Pane paneControl = new Pane();

    private GridPane gridPane = new GridPane();

    private TextField inputStartX = new TextField();
    private TextField inputStartY = new TextField();
    private TextField inputXexp = new TextField();
    private TextField inputYexp = new TextField();
    private TextField inputEndX = new TextField();
    private TextField inputEndY = new TextField();
    private TextField inputStartAngle = new TextField();
    private TextField inputEndAngle = new TextField();

    private TextField inputStartVelocity = new TextField();
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
    private Button btnAddExplorer = new Button("Add Explorer");

    private Button btnDebug = new Button("Debug Mode: ON");

    private GridPane gpDistance = new GridPane();
    private GridPane gpVelocity = new GridPane();
    private GridPane gpAngle = new GridPane();

    private GridPane gpStartXY = new GridPane();
    private GridPane gpEndXY = new GridPane();
    private GridPane gpStartEndVelocity = new GridPane();
    private GridPane gpStartEndAngle = new GridPane();

    private GridPane gpExplorer = new GridPane();

    private TabPane paneTab = new TabPane();
    private Tab tabDistance = new Tab("Add by Distance");
    private Tab tabAngle = new Tab("Angle");
    private Tab tabVelocity = new Tab("Velocity");

    private TextArea tester = new TextArea("(Test) Balls rn:\n");

    private Separator separator1 = new Separator();
    private Separator separatorV = new Separator();

    private Label notif = new Label("");
    private Label textTest = new Label("");

    private Label labelStartXY = new Label("Starting Points (X,Y):");
    private Label labelXYexp = new Label("Spawn Explorer on (X,Y):");
    private Label labelEndXY = new Label("End Points (X,Y):");
    private Label labelStartEndVelocity = new Label("Starting and Ending Velocity:");
    private Label labelStartEndAngle = new Label("Starting and Ending Angle:");
    private Label labelConstXY = new Label("Spawn Point (X,Y):");
    
	private Pane paneExp = new Pane();
//	private Explorer explorer;
    private StackPane spExplorer = new StackPane();
//    private Pane camera = new StackPane();//making this stack pane is a bag idea
    String bgFront = new File(System.getProperty("user.dir")+"\\src\\amongus.png").toURI().toString();
    Image bgImage = new Image(bgFront);
    String bgFlipped = new File(System.getProperty("user.dir")+"\\src\\amongusflipped.png").toURI().toString();
    Image bgImageFlipped = new Image(bgFlipped);
    String mapImgFile = new File(System.getProperty("user.dir")+"\\src\\masp.jpg").toURI().toString();
    Image mapImg = new Image(mapImgFile);
    Pane pSprite = new Pane();
    String bigSpriteFile = new File(System.getProperty("user.dir")+"\\src\\amongus.png").toURI().toString();
    Image bigSprite = new Image(bigSpriteFile);

    private GridPane gpContainer = new GridPane();
    private GridPane paneLeft = new GridPane();
    
	private BooleanProperty s_key = new SimpleBooleanProperty();
	private BooleanProperty a_key = new SimpleBooleanProperty();
	private BooleanProperty w_key = new SimpleBooleanProperty();
	private BooleanProperty d_key = new SimpleBooleanProperty();
//	private BooleanBinding keyPressed = s_key.or(a_key).or(w_key).or(d_key);

	private StackPane spMiniMap = new StackPane();
	private GridPane gpDebug = new GridPane();

    public static void main(String[] args) {
        es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 3);
        // es = ForkJoinPool.commonPool();
        // es = Executors.newFixedThreadPool(2);
        // es = Executors.newCachedThreadPool();
        launch(args);
        es.shutdown();
    }

    public void start(Stage primaryStage) {

    	paneRight.setLayoutX(270);
    	paneRight.setPrefHeight(Y_MAX);
    	paneRight.setMinWidth(1280);
        paneControl.setMaxWidth(250);
        anim();
        ballPane.setPrefHeight(Y_MAX);
        ballPane.setMinWidth(X_MAX);
        ballPane.setLayoutX(0);
        ballPane.setLayoutY(0);   
//        ballPane.setClip(new Rectangle(X_MAX,Y_MAX));
//        fpsLabel.setLayoutX(260);
//        fpsLabel.setLayoutY(0);
//        
//        btnDebug.setLayoutX(260);
//        btnDebug.setLayoutY(10);
        
//        textTest.setLayoutX(260);
//        textTest.setLayoutY(40);

        gpDebug.addRow(0, fpsLabel);
        gpDebug.addRow(1, btnDebug);
        gpDebug.addRow(2, textTest);
        gpDebug.addRow(3, notif);
        
        gpDebug.setLayoutX(260);
        gpDebug.setLayoutY(0);
//        notif.setText(":)");

        gridPane.setAlignment(Pos.BASELINE_CENTER);

        tester.setMaxSize(250, Y_MAX);

        separatorV.setOrientation(Orientation.VERTICAL);

        gridPane.setMaxWidth(250);

        btnAddByDistance.setPrefWidth(250);
        btnAddByVelocity.setPrefWidth(250);
        btnAddByAngle.setPrefWidth(250);

        paneTab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        paneTab.getTabs().addAll(tabDistance, tabAngle, tabVelocity);

        // DISTANCE tab open by default
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


//        paneControl.getChildren().add(gpControls);
        
        gpExplorer.addRow(0,labelXYexp);
        gpExplorer.addRow(1,inputXexp);
        gpExplorer.addRow(1,inputYexp);
        gpExplorer.addRow(2,btnAddExplorer);
        gpExplorer.setMaxWidth(250);
        
        paneLeft.addRow(0,paneTab);
        paneLeft.addRow(1,gpExplorer);
        ballPane.setStyle(
//        		"-fx-background-color: white;"+
                "-fx-border-color: blue;" + // Border color
                "-fx-border-width: 1px;" // Border width
        );
        
        ballPane.setBackground(new Background(new BackgroundImage (
        		mapImg,
				BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null, new BackgroundSize(
                		BackgroundSize.AUTO,BackgroundSize.AUTO,
                        false,false,true,false
                )))
		);
        
        //test minimap = try to store the elements of the balls and display somewhere.
        //this is going to show lang kunware na the balls that exist in the bounds of the
        // spExplorer is being stored perfectly. Then their layoutXY can be shown in place like, x10 kunware
        //so we can actually see it zoomed in
        spMiniMap.setLayoutX(0);
        spMiniMap.setPrefHeight(19*10);//The dimensions of the periphery are 19rows by 33 columns, (x10 para we can see what's actually happening)
        spMiniMap.setPrefWidth(33*10);
        spMiniMap.setLayoutY(paneRight.getHeight()-spMiniMap.getHeight());
        paneRight.getChildren().add(spMiniMap);
//        pSprite = new Pane();
//        pSprite.setBackground(new Background(new BackgroundImage (
//        		bigSprite,
//				BackgroundRepeat.NO_REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                null, new BackgroundSize(
//                		BackgroundSize.AUTO,BackgroundSize.AUTO,
//                        false,false,true,false
//                )))
//		);
//        spMiniMap.getChildren().add(pSprite);
        pSprite.setMaxSize(30, 30);
        
//        -------------------
        
        paneRight.setStyle(
                "-fx-border-color: grey;" + // Border color
                "-fx-border-width: 5px;" // Border width
        );
        
//        camera.getChildren().addAll(ballPane, spExplorer);
//        gpContainer.addRow(0, paneControl, separatorV, ballPane);
        gpContainer.addRow(0, paneLeft, separatorV, paneRight);
//        paneContainer.getChildren().addAll(gpContainer, fpsLabel, textTest, btnDebug);
        paneContainer.getChildren().addAll(gpContainer, gpDebug);

        Scene scene = new Scene(paneContainer);
        primaryStage.setTitle("Particle Physics Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        btnDebug.setOnAction(event ->{
            //hide panel Control when debug is off,
        	//change position of button
        	//change text of button to Debug Mode: OFF
        	if (isDebug) {
            	btnDebug.setText("Developer Mode: OFF");
            	gpContainer.getChildren().clear();
                gpContainer.addRow(0, paneRight);
                isDebug=false;
//                btnDebug.setLayoutX(10);
//                fpsLabel.setLayoutX(10); //10 px from the width of the control panel
//                fpsLabel.setLayoutY(0);
                gpDebug.setLayoutX(10);
                textTest.setText("Debug: "+isDebug);
                primaryStage.setWidth(X_MAX);
                
                if(hasExplorer) {
                	//zooms in the middle always,
                	//so have to adjust position of both ballPane
                	// - spExplorer's position will be adjusted to middle
                	// - then paneRight.getWidth()/2 (midpoint) will be added to ballPane instead for it to move to midpoint-position

                	paneRight.setScaleX(4.f);
                	paneRight.setScaleY(4.f);
                	
                    paneExp.setMaxSize(3,3);

                    //TODO: within the box of the stackpane spExplorer,
                    //get the id / or element num of those and render them to a new box, scaling their x and y to the screen.

                    notif.setText("Elements inside the box are: ");
                    //for loop to check which are inside
                    notif.setText(notif.getText()+"ball %d at [%d,%d]");
	            }
                
                //zoom in testing
//                ballPane.setScaleX(2f);
//            	ballPane.setScaleY(2f);
            	//follow in animation timer
                //try again: ball pane will be the one scaling and moving since i put it inside another container
//                if(hasExplorer) {
//                    ballPane.setScaleX(4f);
//                	ballPane.setScaleY(4f);	
//                }
        	}
        	else {
            	btnDebug.setText("Developer Mode: ON");
            	gpContainer.getChildren().clear();
                gpContainer.addRow(0, paneLeft, separatorV, paneRight);
//                fpsLabel.setLayoutX(260);
//                btnDebug.setLayoutX(250+10); //250 is size of control panel
                gpDebug.setLayoutX(260);

                isDebug=true;
                textTest.setText("Debug: "+isDebug);
                primaryStage.setWidth(X_MAX+250); //full screen size: X_MAX+250
                
                //zoom testing
                
                if(hasExplorer)
                {
                	paneRight.setScaleX(1.f);
                	paneRight.setScaleY(1.f);
	            	spExplorer.setMaxSize(320, 180);
	                paneExp.setMaxSize(30,30);
                }
        	}
        	
        });
        

		paneExp = new Pane();
		paneExp.setBackground(new Background(new BackgroundImage (
				bgImage,
				BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null, new BackgroundSize(
                		BackgroundSize.AUTO,BackgroundSize.AUTO,
                        false,false,true,false
                )))
		);
        spExplorer.setLayoutX(0);
        spExplorer.setLayoutY(0);
        spExplorer.setStyle(
                "-fx-border-color: red;" + // Border color
                "-fx-border-width: 1px;" // Border width
        );

        spExplorer.setPrefSize(X_MAX, Y_MAX); 
        spExplorer.getChildren().add(paneExp);
        paneRight.getChildren().add(spExplorer);
        spExplorer.setVisible(hasExplorer);
        paneRight.getChildren().add(ballPane);
        paneExp.setMaxSize(50,50);
        btnAddExplorer.setOnAction(event -> {
        	try {
        		double expX = Double.parseDouble(inputXexp.getText());
                double expY = Double.parseDouble(inputYexp.getText());
        		hasExplorer=!hasExplorer;
        		spExplorer.setVisible(hasExplorer);
        		if (hasExplorer){
//                    ballPane.setLayoutX(expX);
//                    ballPane.setLayoutX(720-expY);
                    ballPane.setScaleX(38);
                    ballPane.setScaleY(38);
                    
        			ballPane.relocate((640-expX)*ballPane.getScaleX(), (expY-360)*ballPane.getScaleY() );
    	            notif.setText("Explorer spawned.");
//                    spExplorer.setBorder(10,10,10,10);
                  //                    spExplorer.setPrefSize(320, 180); //div 4 zoom
//                    The dimensions of the periphery are 19 rows by 33 columns,
//                    with the sprite rendered in the very center. 
                    //wow super small
//                    spExplorer.setLayoutX(expX);
//                    spExplorer.setLayoutY(expY);

//                    spExplorer.setLayoutX((paneRight.getWidth()/2)-(spExplorer.getWidth()/2));	//center it para zoomed again, 12
//                    spExplorer.setLayoutY((paneRight.getHeight()/2)-spExplorer.getHeight()/2);
                    
                    //idk why but the middle is 435,240 (not same coordinates as balls yet TODO)
                    
//                    ballPane.setLayoutX((paneRight.getWidth()/2)+(spExplorer.getWidth()/2));
//                    ballPane.setLayoutY((paneRight.getHeight()/2)+(spExplorer.getHeight()/2));
//                    
                    

//            		ballPane.getChildren().add(explorer.getPane()); // THIS MAKES IT VISIBLE ON THE RIGHT PANE
//            		ballPane.getChildren().add(paneExp); // THIS MAKES IT VISIBLE ON THE RIGHT PANE

//            		btnAddExplorer.setDisable(true);
            	
//            		textTest.setText(System.getProperty("user.dir")+"\\src\\amongus.png");
    	            //	CORRECT PATH!
                    //file:///D:/Users/ghael/Documents/GitHub/Sites/MP/bouncy/BOUNCY/src/amongus.png
    	            
    	            
            	}else {
            		ballPane.relocate(0,0);
            		ballPane.setScaleX(1);
                    ballPane.setScaleY(1);
            	}
        		
	        } catch (NumberFormatException e) {
	            notif.setText("Invalid Explorer coordinates.\n");
	        }

        	
        	
        });
        
        scene.setOnKeyPressed(e ->{
        	//if (!isDebug && hasExplorer) //NOTE: uncomment when done testing

        	if (hasExplorer)

        	{//DONE-could be better, make instead another animationtimer that checks if keys are being pressed or not

                double moveY = ballPane.getLayoutY(),moveX = ballPane.getLayoutX();
                double dx =ballPane.getScaleX(),dy = ballPane.getScaleY();
        		switch(e.getCode())
        		{
        			case W:
        				moveY+=dy;

//        				w_key.set(true);
//    	                textTest.setText("W is pressed.");

    	                break;
        			case S:moveY-=dy;

//        				s_key.set(true);
//    	                textTest.setText("S is pressed.");
    	                break;
        			case A:moveX+=dx;

//        				a_key.set(true);
    	                
    	                paneExp.setBackground(new Background(new BackgroundImage (
                				bgImageFlipped,
                				BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                null, new BackgroundSize(
                                		BackgroundSize.AUTO,BackgroundSize.AUTO,
                                        false,false,true,false
                                )))
                		);
    	                break;
        			case D:
        				moveX-=dx;

//        				d_key.set(true);
    	                paneExp.setBackground(new Background(new BackgroundImage (
                				bgImage,
                				BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                null, new BackgroundSize(
                                		BackgroundSize.AUTO,BackgroundSize.AUTO,
                                        false,false,true,false
                                )))
                		);
    	                break;
        		}

                double halfSizX = ballPane.getWidth()*ballPane.getScaleX()*0.5,
                		halfSizY = ballPane.getHeight()*ballPane.getScaleY()*0.5;
                
                if(moveX>halfSizX )
                	moveX = halfSizX-38 ;
                if(moveX<-halfSizX )
                	moveX = 38-halfSizX ;
                if(moveY>halfSizY)
                	moveY = halfSizY-38 ;
                if(moveY<-halfSizY )
                	moveY = 38-halfSizY;
                ballPane.setLayoutX(moveX);
                ballPane.setLayoutY(moveY);

                textTest.setText(
                		"\nYou are at (in px):\n"
                		+ "X: ["+(640-moveX)+"]\n"
                		+ "Y: ["+(360-moveY)+"]");
        	}
		});
        scene.setOnKeyReleased(e ->{
//        	if (!isDebug && hasExplorer)  //NOTE: uncomment when done testing
        	if (hasExplorer)
        	{
        		switch(e.getCode())
        		{
        			case W:
        				w_key.set(false);
    	                textTest.setText("W is pressed.");
    	                break;
        			case S:
        				s_key.set(false);
    	                textTest.setText("S is pressed.");
    	                break;
        			case A:
        				a_key.set(false);
    	                textTest.setText("A is pressed.");
    	                break;
        			case D:
        				d_key.set(false);
    	                textTest.setText("D is pressed.");
    	                break;
        		}
        	}
        });
        
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
       

        btnAddByDistance.setOnAction(event -> {
            try {
                double startX = Double.parseDouble(inputStartX.getText());
                double startY = Double.parseDouble(inputStartY.getText());
                double endX = Double.parseDouble(inputEndX.getText());
                double endY = Double.parseDouble(inputEndY.getText());
                double velocity = Double.parseDouble(inputVelocity.getText());
                double angle = Double.parseDouble(inputAngle.getText());
                int n = Integer.parseInt(inputCount.getText());

                if (n > 0)
                    addParticlesByDistance(n, startX, startY, endX, endY, velocity, angle, ballPane);
            } catch (NumberFormatException e) {
                notif.setText("Invalid input\n");
            }
            ;
        });

        btnAddByAngle.setOnAction(event -> {
            try {
                final double startX = Double.parseDouble(inputStartX.getText());
                final double startY = Double.parseDouble(inputStartY.getText());
                final double startAngle = Double.parseDouble(inputStartAngle.getText());
                final double endAngle = Double.parseDouble(inputEndAngle.getText());
                final double velocity = Double.parseDouble(inputVelocity.getText());

                final int n = Integer.parseInt(inputCount.getText());

                if (n >= 0)
                    addParticlesByAngle(n, startX, startY, startAngle, endAngle, velocity, ballPane);

            } catch (NumberFormatException e) {
                notif.setText("Invalid input\n");
            }
        });

        btnAddByVelocity.setOnAction(event -> {
            try {
                final double startX = Double.parseDouble(inputStartX.getText());
                final double startY = Double.parseDouble(inputStartY.getText());
                final double startVelocity = Double.parseDouble(inputStartVelocity.getText());
                final double endVelocity = Double.parseDouble(inputEndVelocity.getText());
                final double angle = Double.parseDouble(inputAngle.getText());

                final int n = Integer.parseInt(inputCount.getText());

                if (n > 0)
                    addParticlesByVelocity(n, startX, startY, startVelocity, endVelocity, angle, ballPane);
            } catch (NumberFormatException e) {
                notif.setText("Invalid input\n");
            }
        });

        System.nanoTime();
        lastFPSTime = System.nanoTime();
        new AnimationTimer() {
            @Override
            public void handle(long now) {

                fps++;
                try {
                    update(now);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                

            }

        }.start();
    }

    private void addBall(Particle p) {
        ball_buf.add(p);
    }

    private void clrBall() {
        ball_buf.clear();
    }

    private synchronized List<Circle> balls() {
        
            List<Circle> circles = ball_buf.parallelStream().map(Particle::draw).collect(Collectors.toList());

            return circles;
        
    }

    private static Circle getBall(Future<Circle> t) {
        try {
            return t.get();
        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();
        }
        return null;
    }

private void anim() {
	Timeline tl = new Timeline();
	
	tl.getKeyFrames()
			.add(new KeyFrame(Duration.millis(16.6666666667),
					new EventHandler<ActionEvent>() {
				List<javafx.scene.Node> balls = ballPane.getChildren().filtered(node->(node instanceof Circle));
						@Override
						public void handle(ActionEvent t) {
							// move the ball
							balls.forEach(circle->{
								double x = circle.getLayoutX(), y = circle.getLayoutY();
								if(x < 0 ||x > X_MAX)
									circle.setTranslateX(-circle.getTranslateX());
								if (y > Y_MAX ||y < 0) 
									circle.setTranslateY(-circle.getTranslateY());
//								if(circle.isVisible()) {
									circle.setLayoutX(x + circle.getTranslateX());
									circle.setLayoutY(y + circle.getTranslateY());
//								}
//								circle.relocate(x + circle.getTranslateX(),y + circle.getTranslateY())
							});
							
						}
					}));
	// this.tl = new Timeline();

	tl.setCycleCount(Timeline.INDEFINITE);
	tl.play();

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

    private void initTabAngle() {
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
        double y = startY;

        for (int i = 0; i < n; i++) {
            double xin = x, yin = y;

            addBall(new Particle(xin, yin, Math.toRadians(angle), velocity));
            x += dx;
            y += dy;
        }

        drawBalls(ballPane);

    }

    private void drawBalls(Pane ballPane) {
        try {
            // ballPane.getChildren().addAll(sballs());
            ballPane.getChildren().addAll(balls());
            clrBall();
        } catch (Exception e) {
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

            v += velocityDiff;

        }
        drawBalls(ballPane);
    }

    private void update(long now) {

        double curr = now - lastFPSTime;

        if (curr < 500_000_000)
            return;
        fps *= 1_000_000_000.0 / curr;

        fpsLabel.setText(String.format("FPS: %.2f", fps));
        lastFPSTime = now;
        fps = 0;
        
        

    }

}
