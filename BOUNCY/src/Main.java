import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
public class Main extends Application {
    private static final String prompt_n = "Number of Particles:";
    private static final String V_PX_S = "Velocity (px/s):";
    private static final String ADD_BY_ANGLE = "Add by Angle";
    private static final String ADD_BY_VELOCITY = "Add by Velocity";
    private static final String ADD_BY_DISTANCE = "Add by Distance";
    private List<Particle> ball_buf = new ArrayList<Particle>();
    // private Canvas canvas= new Canvas(X_MAX, Y_MAX);;
    private Label fpsLabel = new Label("FPS: 0");;
    private long lastFPSTime = System.nanoTime();;
    // private int frameCount = 0;
    private double fps = 0;
    // public static ExecutorService es;
    private boolean hasExplorer = false;
    private boolean isDebug = true; // show control panel initially

    private final double X_MAX = 1280, Y_MAX = 720;
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
    private Label labelVelocity = new Label(V_PX_S);
    private TextField inputVelocity = new TextField();
    private Label labelAngle = new Label("Angle (degrees):");
    private TextField inputAngle = new TextField();
    private Label labelCount = new Label(prompt_n);
    private TextField inputCount = new TextField();
    private final String MODE_BTN_TXT = "Switch mode";
    private Button btnAddByDistance = new Button(ADD_BY_DISTANCE);
    private Button btnAddByAngle = new Button(ADD_BY_ANGLE);
    private Button btnAddByVelocity = new Button(ADD_BY_VELOCITY);
    private Button btnAddExplorer = new Button(MODE_BTN_TXT);

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
    private Tab tabDistance = new Tab(ADD_BY_DISTANCE);
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
    // private Explorer explorer;
    private StackPane spExplorer = new StackPane();
    // private Pane camera = new StackPane();//making this stack pane is a bag idea
    String bgFront = ".\\amongus.png";
    Image bgImage = new Image(bgFront);
    String bgFlipped = ".\\amongusflipped.png";
    Image bgImageFlipped = new Image(bgFlipped);
    String mapImgFile = ".\\map.jpg";
    Image mapImg = new Image(mapImgFile);
    Pane pSprite = new Pane();

    private final float EX_BOUND = 200f;
    private final double EX_LIM = ballPane.getScaleX() * 2 + EX_BOUND;
    private GridPane gpContainer = new GridPane();
    private GridPane paneLeft = new GridPane();

    // private BooleanBinding keyPressed = s_key.or(a_key).or(w_key).or(d_key);

    // private StackPane spMiniMap = new StackPane();
    private GridPane gpDebug = new GridPane();
    private static ExecutorService es;
    public static void main(String[] args) {
    	es = Executors.newFixedThreadPool(3);
        launch(args);
        try {
			es.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void start(Stage primaryStage) {
//    	inputYexp.managedProperty().bind(inputYexp.visibleProperty());
//    	inputXexp.managedProperty().bind(inputXexp.visibleProperty());
        paneRight.setLayoutX(270);
        paneRight.setPrefHeight(Y_MAX);
        paneRight.setMinWidth(1280);
        paneControl.setMaxWidth(250);
        Rectangle clip = new Rectangle(X_MAX, Y_MAX);
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        paneRight.setClip(clip);
        anim();
        ballPane.setPrefHeight(Y_MAX);
        ballPane.setMinWidth(X_MAX);
        ballPane.setLayoutX(0);
        ballPane.setLayoutY(0);

        gpDebug.addRow(0, fpsLabel);
        gpDebug.addRow(1, textTest);
        gpDebug.addRow(2, notif);

        gpDebug.setLayoutX(260);
        gpDebug.setLayoutY(0);

        gridPane.setAlignment(Pos.BASELINE_CENTER);

        tester.setMaxSize(250, Y_MAX);

        separatorV.setOrientation(Orientation.VERTICAL);

        gridPane.setMaxWidth(250);

        btnAddByDistance.setPrefWidth(250);
        btnAddByVelocity.setPrefWidth(250);
        btnAddByAngle.setPrefWidth(250);

        paneTab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        paneTab.getTabs().addAll(tabDistance, tabAngle, tabVelocity);
        paneTab.managedProperty().bind(paneTab.visibleProperty());
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

        // paneControl.getChildren().add(gpControls);

        gpExplorer.addRow(0, labelXYexp);
        gpExplorer.addRow(1, inputXexp);
        gpExplorer.addRow(1, inputYexp);
        gpExplorer.addRow(2, btnAddExplorer);
        gpExplorer.setMaxWidth(250);

        paneLeft.addRow(0, paneTab);
        paneLeft.addRow(1, gpExplorer);
        ballPane.setStyle(
                // "-fx-background-color: white;"+
                "-fx-border-color: blue;" + // Border color
                        "-fx-border-width: 1px;" // Border width
        );

        ballPane.setBackground(new Background(new BackgroundImage(
                mapImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null, new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO,
                        false, false, true, false))));

        pSprite.setMaxSize(30, 30);

        // -------------------

        paneRight.setStyle(
                "-fx-border-color: grey;" + // Border color
                        "-fx-border-width: 5px;" // Border width
        );

        gpContainer.addRow(0, paneLeft, separatorV, paneRight);
        paneContainer.getChildren().addAll(gpContainer, gpDebug);

        Scene scene = new Scene(paneContainer);
        primaryStage.setTitle("Particle Physics Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        btnDebug.setOnAction(event -> {
            if (isDebug) {
                btnDebug.setText("Developer Mode: OFF");
                gpContainer.getChildren().clear();
                gpContainer.addRow(0, paneRight);
                isDebug = false;
                gpDebug.setLayoutX(10);
                textTest.setText("Debug: " + isDebug);
                primaryStage.setWidth(X_MAX);

                if (hasExplorer) {
                    // zooms in the middle always,
                    // so have to adjust position of both ballPane
                    // - spExplorer's position will be adjusted to middle
                    // - then paneRight.getWidth()/2 (midpoint) will be added to ballPane instead
                    // for it to move to midpoint-position

                    paneRight.setScaleX(4.f);
                    paneRight.setScaleY(4.f);

                    paneExp.setMaxSize(3, 3);

                    // TODO: within the box of the stackpane spExplorer,
                    // get the id / or element num of those and render them to a new box, scaling
                    // their x and y to the screen.

                    notif.setText("Elements inside the box are: ");
                    // for loop to check which are inside
                    notif.setText(notif.getText() + "ball %d at [%d,%d]");
                }

            } else {
                btnDebug.setText("Developer Mode: ON");
                gpContainer.getChildren().clear();
                gpContainer.addRow(0, paneLeft, separatorV, paneRight);
                gpDebug.setLayoutX(260);

                isDebug = true;
                textTest.setText("Debug: " + isDebug);
                primaryStage.setWidth(X_MAX + 250); // full screen size: X_MAX+250

            }

        });
        btnDebug.setOnAction(null);

        paneExp = new Pane();
        paneExp.setBackground(new Background(new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null, new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO,
                        false, false, true, false))));
        spExplorer.setLayoutX(58);
        spExplorer.setLayoutY(0);
        

        spExplorer.setPrefSize(X_MAX, Y_MAX);
        paneRight.getChildren().add(ballPane);
        spExplorer.getChildren().add(paneExp);
        paneRight.getChildren().add(spExplorer);
        spExplorer.setVisible(hasExplorer);
        paneExp.setMaxSize(200, 200);
        
        btnAddExplorer.setOnAction(event -> {

            hasExplorer = !hasExplorer;
            if (hasExplorer) {
	            try {
	               

                    double expX = Double.parseDouble(inputXexp.getText());
                    double expY = Double.parseDouble(inputYexp.getText());
                    if (expX > X_MAX || expX < 0 || expY > Y_MAX || expY < 0)
                        throw new NumberFormatException();

                    double off_x = (expX >= X_MAX) ? EX_BOUND : (expX <= 0) ? -EX_BOUND : 0f,
                            off_y = (expY >= Y_MAX) ? -EX_BOUND : (expY <= 0) ? EX_BOUND : 0f;
                    ballPane.setScaleX(38.34);
                    ballPane.setScaleY(38.34);
//
//                    ballPane.setScaleX(1);
//                    ballPane.setScaleY(1);
                    expX = (640 - expX) * ballPane.getScaleX() + off_x;
                    expY = (expY - 360) * ballPane.getScaleY() + off_y;
                    ballPane.setLayoutX(expX);
                    ballPane.setLayoutY(expY);	
	
	            } catch (NumberFormatException e) {
	                notif.setText("Invalid Explorer coordinates.\n");
	                hasExplorer = false;
	
	            } 
            }else {
                ballPane.setScaleX(1);
                ballPane.setScaleY(1);
                ballPane.relocate(0, 0);

            }
            inputXexp.setVisible(!hasExplorer);
            inputYexp.setVisible(!hasExplorer);
            paneTab.setVisible(!hasExplorer);
            spExplorer.setVisible(hasExplorer);

        });

        scene.setOnKeyPressed(e -> {
            // if (!isDebug && hasExplorer) //NOTE: uncomment when done testing
            if (e.getCode() == KeyCode.E) {

                btnAddExplorer.fire();
            }
            if (!hasExplorer)
            	return;

            double moveY = ballPane.getLayoutY(), moveX = ballPane.getLayoutX();
            double dx = ballPane.getScaleX(), dy = ballPane.getScaleY();
            switch (e.getCode()) {
                case W:
                    moveY += dy;

                    // w_key.set(true);
                    // textTest.setText("W is pressed.");

                    break;
                case S:
                    moveY -= dy;

                    // s_key.set(true);
                    // textTest.setText("S is pressed.");
                    break;
                case A:
                    moveX += dx;

                    // a_key.set(true);

                    paneExp.setBackground(new Background(new BackgroundImage(
                            bgImageFlipped,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            null, new BackgroundSize(
                                    BackgroundSize.AUTO, BackgroundSize.AUTO,
                                    false, false, true, false))));
                    break;
                case D:
                    moveX -= dx;

                    // d_key.set(true);
                    paneExp.setBackground(new Background(new BackgroundImage(
                            bgImage,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            null, new BackgroundSize(
                                    BackgroundSize.AUTO, BackgroundSize.AUTO,
                                    false, false, true, false))));
                    break;
                default:
                    break;
            }

            double halfSizX = ballPane.getWidth() * ballPane.getScaleX() * 0.5,
                    halfSizY = ballPane.getHeight() * ballPane.getScaleY() * 0.5;
            
            if (moveX >= halfSizX)
                moveX = halfSizX - EX_LIM;
            if (moveX < -halfSizX)
                moveX = EX_LIM - halfSizX;
            if (moveY > halfSizY)
                moveY = halfSizY - EX_LIM;
            if (moveY < -halfSizY)
                moveY = EX_LIM - halfSizY;
            ballPane.setLayoutX(moveX);
            ballPane.setLayoutY(moveY);

            textTest.setText(
                    "\nYou are at (in px):\n"
                            + "X: [" + (ballPane.getWidth()*0.5-(ballPane.getLayoutX()/ ballPane.getScaleX())) + "]\n"
                            + "Y: [" + (ballPane.getHeight()*0.5-(ballPane.getLayoutY()/ ballPane.getScaleY()) ) + "]");
            
        });
        
        tabVelocity.setOnSelectionChanged(e -> {
            tabDistance.setText("Distance");
            tabAngle.setText("Angle");
            tabVelocity.setText(ADD_BY_VELOCITY);

            gpDistance.getChildren().clear();
            gpAngle.getChildren().clear();
            gpVelocity.getChildren().clear();
            initTabVelocity();
            gpVelocity.setMaxWidth(250);

        });

        tabDistance.setOnSelectionChanged(e -> {
            tabDistance.setText(ADD_BY_DISTANCE);
            tabAngle.setText("Angle");
            tabVelocity.setText("Velocity");

            gpDistance.getChildren().clear();
            gpAngle.getChildren().clear();
            gpVelocity.getChildren().clear();

            initTabDist();
        });

        tabAngle.setOnSelectionChanged(e -> {
            tabDistance.setText("Distance");
            tabAngle.setText(ADD_BY_ANGLE);
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

    private void anim() {
        Timeline tl = new Timeline();

        tl.getKeyFrames()
                .add(new KeyFrame(Duration.millis(16.6666666667),
                        new EventHandler<ActionEvent>() {
                		Bounds peri = paneRight.getBoundsInLocal();
                		
//                			double centerX= ballPane.getLayoutX()/ ballPane.getScaleX()+ballPane.getWidth()*0.5,
//                					centerY=(ballPane.getLayoutY()/ ballPane.getScaleY())+ballPane.getHeight()*0.5,
//                					boundsY= ballPane.getHeight()/ballPane.getScaleY(),boundsX= 16,
//                					top = (centerY-boundsY)*ballPane.getScaleX(),
//                					bottom =(centerY+boundsY)*ballPane.getScaleX(),
//                					left =(centerX+boundsX)*ballPane.getScaleX(),
//                					right = (centerX-boundsX)*ballPane.getScaleX();  
                					
                            List<javafx.scene.Node> balls = ballPane.getChildren()
                                    .filtered(node -> (node instanceof Circle));

                            @Override
                            public void handle(ActionEvent t) {
                                // move the ballv
//                            	top *=0.5;
//                            	bottom*=ballPane.getScaleY()/2;
//                            	left*= 0.5;
//                            	right *= 0.5;
                                balls.forEach(circle -> {
                                	
//                                    Platform.runLater(()->{
                                    	double x = circle.getLayoutX(), y = circle.getLayoutY();
                                        boolean isSeen = (hasExplorer)?peri.intersects(ballPane.localToParent(circle.getBoundsInParent())):true;
                                    	circle.setVisible(isSeen);
//                                    	ballPane.localToParent(circle.getBoundsInParent())
                                    if (x < 0 || x > X_MAX) 
                                        circle.setTranslateX(-circle.getTranslateX());
                                    
                                    if (y > Y_MAX || y < 0)
                                        circle.setTranslateY(-circle.getTranslateY());
                                    // if(circle.isVisible()) 
    
                                    circle.setLayoutX(x + circle.getTranslateX());
                                    circle.setLayoutY(y + circle.getTranslateY());
});
                                    // }
                                    // circle.relocate(x + circle.getTranslateX(),y + circle.getTranslateY())
//                                });

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

            ball_buf.add(new Particle(xin, yin, Math.toRadians(angle), velocity));
            x += dx;
            y += dy;
        }

        drawBalls(ballPane);

    }

    private void drawBalls(Pane ballPane) {
        try {
            List<Circle> circles = ball_buf.parallelStream().map(Particle::draw).collect(Collectors.toList());
            ballPane.getChildren().addAll(circles);
            ball_buf.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addParticlesByAngle(int n, double startX, double startY, double startAngle, double endAngle,
            double velocity, Pane paneBall) {
        double angleDiff = (endAngle - startAngle) / (n);
        double angle = startAngle;
        for (int i = 0; i < n; i++) {
            ball_buf.add(new Particle(startX, startY, Math.toRadians(angle), velocity));
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

            ball_buf.add(new Particle(startX, startY, Math.toRadians(angle), velocity));

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
