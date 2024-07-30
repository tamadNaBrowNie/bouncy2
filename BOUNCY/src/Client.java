import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

public class Client extends Application {
//    private static final String prompt_n = "Number of Particles:";
//    private static final String V_PX_S = "Velocity (px/s):";
//    private static final String ADD_BY_ANGLE = "Add by Angle";
//    private static final String ADD_BY_VELOCITY = "Add by Velocity";
//    private static final String ADD_BY_DISTANCE = "Add by Distance";
	private List<Particle> ball_buf = new ArrayList<Particle>();
	// private Canvas canvas= new Canvas(X_MAX, Y_MAX);;
	private Label fpsLabel = new Label("FPS: 0");;
	private long lastFPSTime = System.nanoTime();;
	// private int frameCount = 0;
	private double fps = 0;
	// public static ExecutorService es;
	private boolean hasExplorer = false;

	private final double X_MAX = 1280, Y_MAX = 720;
	private Pane paneContainer = new Pane();

	private Pane ballPane = new Pane();

	private Pane paneRight = new Pane();

//    private Pane paneControl = new Pane();

	private GridPane gridPane = new GridPane();

//    private TextField inputStartX = new TextField();
//    private TextField inputStartY = new TextField();
//    
//    private TextField inputEndX = new TextField();
//    private TextField inputEndY = new TextField();
//    private TextField inputStartAngle = new TextField();
//    private TextField inputEndAngle = new TextField();
//
//    private TextField inputStartVelocity = new TextField();
//    private TextField inputEndVelocity = new TextField();
//    private Label labelVelocity = new Label(V_PX_S);
//    private TextField inputVelocity = new TextField();
//    private Label labelAngle = new Label("Angle (degrees):");
//    private TextField inputAngle = new TextField();
//    private Label labelCount = new Label(prompt_n);
//    private TextField inputCount = new TextField();
//
//    private Button btnAddByDistance = new Button(ADD_BY_DISTANCE);
//    private Button btnAddByAngle = new Button(ADD_BY_ANGLE);
//    private Button btnAddByVelocity = new Button(ADD_BY_VELOCITY);

	private final String ENTRY_TXT = "Join";
	private final String LEAVE_TXT = "LEAVE";
	private Button btnAddExplorer = new Button(ENTRY_TXT);

	private GridPane gpDistance = new GridPane();
	private GridPane gpVelocity = new GridPane();
	private GridPane gpAngle = new GridPane();

	private GridPane gpStartXY = new GridPane();
	private GridPane gpEndXY = new GridPane();
	private GridPane gpStartEndVelocity = new GridPane();
	private GridPane gpStartEndAngle = new GridPane();

//    Exp stuff?
	private Label labelXYexp = new Label("Spawn Explorer on (X,Y):");
	private TextField inputXexp = new TextField();
	private TextField inputYexp = new TextField();
	private GridPane gpExplorer = new GridPane();

	private TabPane paneTab = new TabPane();
//    private Tab tabDistance = new Tab(ADD_BY_DISTANCE);
	private Tab tabAngle = new Tab("Angle");
	private Tab tabVelocity = new Tab("Velocity");

	private TextArea tester = new TextArea("(Test) Balls rn:\n");

	private Separator separator1 = new Separator();
	private Separator separatorV = new Separator();

	private Label notif = new Label("");
	private Label textTest = new Label("");

	private Label labelStartXY = new Label("Starting Points (X,Y):");

	private Label labelEndXY = new Label("End Points (X,Y):");
	private Label labelStartEndVelocity = new Label("Starting and Ending Velocity:");
	private Label labelStartEndAngle = new Label("Starting and Ending Angle:");
	private Label labelConstXY = new Label("Spawn Point (X,Y):");

	private final float EX_BOUND = 5.2164841f;

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

//    client coords.
	private double my_X = 0, my_Y = 0;

	public void start(Stage primaryStage) {
//    	inputYexp.managedProperty().bind(inputYexp.visibleProperty());
//    	inputXexp.managedProperty().bind(inputXexp.visibleProperty());
		paneRight.setLayoutX(270);
		paneRight.setPrefHeight(Y_MAX);
		paneRight.setMinWidth(1280);
//        paneControl.setMaxWidth(250);
		Rectangle clip = new Rectangle(X_MAX, Y_MAX);
		clip.setLayoutX(0);
		clip.setLayoutY(0);
		paneRight.setClip(clip);
		paneRight.setVisible(hasExplorer);
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

//        btnAddByDistance.setPrefWidth(250);
//        btnAddByVelocity.setPrefWidth(250);
//        btnAddByAngle.setPrefWidth(250);

		paneTab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

//        paneTab.getTabs().addAll(tabDistance, tabAngle, tabVelocity);
		paneTab.managedProperty().bind(paneTab.visibleProperty());
		paneTab.setVisible(false);
		// DISTANCE tab open by default
//        initTabDist();

//        gpStartXY.addRow(0, inputStartX, inputStartY);
//        gpStartEndVelocity.addRow(0, inputStartVelocity, inputEndVelocity);
//        gpEndXY.addRow(0, inputEndX, inputEndY);
//        gpStartEndAngle.addRow(0, inputStartAngle, inputEndAngle);

		gpStartEndVelocity.setMaxWidth(250);
		gpStartEndAngle.setMaxWidth(250);
		gpStartXY.setMaxWidth(250);
		gpEndXY.setMaxWidth(250);
		gpVelocity.setMaxWidth(250);
		gpDistance.setMaxWidth(250);
		gpAngle.setMaxWidth(250);
		tabVelocity.setContent(gpVelocity);
//        tabDistance.setContent(gpDistance);
		tabAngle.setContent(gpAngle);

		// paneControl.getChildren().add(gpControls);

		gpExplorer.addRow(0, labelXYexp);
		gpExplorer.addRow(1, inputXexp);
		gpExplorer.addRow(1, inputYexp);
		gpExplorer.addRow(2, btnAddExplorer);
		gpExplorer.setMaxWidth(250);

//        paneLeft.addRow(0, paneTab);
//        paneLeft.addRow(1, gpExplorer);
		paneLeft.addRow(0, gpExplorer);
		ballPane.setStyle(
				// "-fx-background-color: white;"+
				"-fx-border-color: blue;" + // Border color
						"-fx-border-width: 1px;" // Border width
		);

		// private Pane camera = new StackPane();//making this stack pane is a bag idea
		String bgFront = ".\\amongus.png";
		Image bgImage = new Image(bgFront);
//		String bgFlipped = ".\\amongusflipped.png";
//		Image bgImageFlipped = new Image(bgFlipped);
		Pane pSprite = new Pane();
		ballPane.setStyle("-fx-border-color: blue;" + // Border color
				"-fx-border-width: 3px;" + "-fx-background-image:url('map.jpg');" + "-fx-background-repeat: no-repeat;"
				+ " -fx-background-size: cover;" + " -fx-background-position: center center;"); // Border width

		pSprite.setMaxSize(30, 30);

		// -------------------

		paneRight.setStyle("-fx-border-color: grey;" + // Border color
				"-fx-border-width: 5px;" // Border width
		);

		gpContainer.addRow(0, paneLeft, separatorV, paneRight);
		paneContainer.getChildren().addAll(gpContainer, gpDebug);

		Scene scene = new Scene(paneContainer);
		primaryStage.setTitle("Particle Physics Simulator");
		primaryStage.setScene(scene);
		primaryStage.show();

		// private Explorer explorer;

//        This is when we add the ballPane to screen
		paneRight.getChildren().add(ballPane);

//      Holds the sprite
		StackPane spExplorer = new StackPane();
//		BackgroundImage flipSprite = new BackgroundImage(bgImageFlipped, BackgroundRepeat.NO_REPEAT,
//				BackgroundRepeat.NO_REPEAT, null,
//				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)),
		BackgroundImage sprite = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null,
						new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
		Background bgSprite = new Background(sprite);
//				, bgFlip = new Background(flipSprite);
//      The sprite
		Pane paneExp = new Pane();
//Image of sprite
		paneExp.setBackground(bgSprite);
		spExplorer.getChildren().add(paneExp);

		spExplorer.setLayoutX(58);
		spExplorer.setLayoutY(0);

		spExplorer.setPrefSize(X_MAX, Y_MAX);
		paneExp.setMaxSize(200, 200);
		paneRight.getChildren().add(spExplorer);
		spExplorer.setVisible(true);
		gpDebug.setVisible(false);

		btnAddExplorer.setOnAction(event -> {
			changeMode();

		});
//        btnAddExplorer.setOnAction(null);

		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case E:
				btnAddExplorer.fire();
				break;
			case ESCAPE:
				primaryStage.close();
				break;
			default:
				break;
			}
			if (!hasExplorer)
				return;

			double moveY = ballPane.getLayoutY(), moveX = ballPane.getLayoutX(), dx = ballPane.getScaleX(),
					dy = ballPane.getScaleY(), facing = paneExp.getScaleX();
			switch (e.getCode()) {
			case W:
				moveY += dy;
				my_Y--;
				break;
			case S:
				moveY -= dy;
				my_Y++;
				break;
			case A:
				moveX += dx;
				my_X--;
//				paneExp.setBackground(bgFlip);
				if(facing > 0) paneExp.setScaleX(-facing);
				break;
			case D:
				moveX -= dx;
				my_X++;
				if(facing < 0) paneExp.setScaleX(-facing);
//				paneExp.setBackground(bgSprite);
				break;

			default:
				break;
			}
			final double EX_LIM = (2 + EX_BOUND);
			double halfSizX = X_MAX *dx* 0.5, halfSizY = ballPane.getHeight() *dy* 0.5;

			if (moveX >= halfSizX) {
				moveX = (halfSizX - EX_LIM*dx);
				my_X = EX_LIM;
			}
			if (moveX <= -halfSizX) {
				moveX = (EX_LIM*dx - halfSizX);
				my_X = X_MAX - EX_LIM;
			}
			if (moveY >= halfSizY) {
				moveY = (halfSizY - EX_LIM*dy);
				my_Y = EX_LIM;
			}
			if (moveY <= -halfSizY) {
				moveY = (EX_LIM - halfSizY*dy);
				my_Y = Y_MAX - EX_LIM;
			}

//            ballPane.setLayoutX(moveX);
//            ballPane.setLayoutY(moveY);
			ballPane.relocate(moveX, moveY);
		});
		System.nanoTime();
		lastFPSTime = System.nanoTime();
		AnimationTimer ticer = new AnimationTimer() {
			@Override
			public void handle(long now) {

				fps++;
				makeFrame();
				textTest.setText("\nYou are at (in px):\n" + "X: [" + my_X + "]\n" + "Y: [" + (720 - my_Y) + "]");
				try {
					update(now);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		};
		Thread anims = new Thread(() -> ticer.start());
		anims.start();
	}

	private void changeMode() {
		hasExplorer = !hasExplorer;

		inputXexp.setVisible(!hasExplorer);
		inputYexp.setVisible(!hasExplorer);
		paneTab.setVisible(!hasExplorer);
		paneRight.setVisible(hasExplorer);
		gpDebug.setVisible(hasExplorer);
		btnAddExplorer.setText((hasExplorer) ? LEAVE_TXT : ENTRY_TXT);
		if (!hasExplorer) {
			ballPane.setScaleX(1);
			ballPane.setScaleY(1);
			ballPane.relocate(0, 0);
			return;
		}
		try {
			double ex_X = Double.parseDouble(inputXexp.getText());
			double ex_Y = Double.parseDouble(inputYexp.getText());
			if (ex_X > X_MAX || ex_X < 0 || ex_Y > Y_MAX || ex_Y < 0)
				throw new NumberFormatException();
			double off_x = (ex_X >= X_MAX) ? EX_BOUND : (ex_X <= 0) ? -EX_BOUND : 0f,
					off_y = (ex_Y >= Y_MAX) ? -EX_BOUND : (ex_Y <= 0) ? EX_BOUND : 0f;
			ballPane.setScaleX(38.34);
			ballPane.setScaleY(38.34);
			my_X = ex_X -= off_x;
			my_Y = ex_Y += off_y;
			ex_X = (ballPane.getWidth() * 0.5 - my_X) * ballPane.getScaleX();
			ex_Y = (my_Y - ballPane.getHeight() * 0.5) * ballPane.getScaleY();
			ballPane.setLayoutX(ex_X);
			ballPane.setLayoutY(ex_Y);

		} catch (NumberFormatException e) {
			notif.setText("Invalid Explorer coordinates.\n");
			hasExplorer = false;
		}
	}

	private void makeFrame() {
		try {
			es.submit(() ->

			check_vis(ballPane.getChildren())).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void check_vis(List<Node> balls) {
		final double w, h, s_x, s_y, Y_off, X_off, cen_X, cen_Y;

		w = ballPane.getWidth() * 0.5;
		h = ballPane.getHeight() * 0.5;
		s_x = 1.0 / ballPane.getScaleX();
		s_y = 1.0 / ballPane.getScaleY();

		Y_off = h * s_y;
		X_off = w * s_x;

		cen_X = w - ballPane.getLayoutX() * s_x;
		cen_Y = h - ballPane.getLayoutY() * s_y;

		Platform.runLater(() -> balls.forEach(circle -> mov_ball(Y_off, X_off, cen_X, cen_Y, circle)));

//		mov_balls(balls, Y_off, X_off, cen_X,cen_Y);
	}

	private void mov_ball(double Y_off, double X_off, double cen_X, double cen_Y, Node circle) {
		double x = circle.getLayoutX(), y = circle.getLayoutY(),
//					dx = circle.getTranslateX(),
//					dy =  circle.getTranslateY(),
				top, bottom, left, right;
		top = cen_Y + Y_off;
		bottom = cen_Y - Y_off;
		left = cen_X - X_off;
		right = cen_X + X_off;

		boolean isSeen = left <= x && right >= x && top >= y && bottom <= y;
		circle.setVisible(hasExplorer && isSeen);
//			hit_check(circle, x, y, dx, dy);
	}

//	private void hit_check(Node circle, double x, double y, double dx, double dy) {
//		if (dx != 0f) {
//			if (x <= 0 || x >= X_MAX)
//				circle.setTranslateX(-dx);
//			circle.setLayoutX(x + circle.getTranslateX());
//		}
//		if (dy != 0f) {
//			if (y >= Y_MAX || y <= 0)
//				circle.setTranslateY(-dy);
//			circle.setLayoutY(y + circle.getTranslateY());
//		}
//	}

	private void drawBalls(List<Node> nodes,Pane ballPane) {
		try {
//			List<Circle> circles = ball_buf.parallelStream().map(Particle::draw).collect(Collectors.toList());
			ballPane.getChildren().clear();
			ballPane.getChildren().addAll(nodes);

//			ball_buf.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

//        tabVelocity.setOnSelectionChanged(e -> {
//            tabDistance.setText("Distance");
//            tabAngle.setText("Angle");
//            tabVelocity.setText(ADD_BY_VELOCITY);
//
//            gpDistance.getChildren().clear();
//            gpAngle.getChildren().clear();
//            gpVelocity.getChildren().clear();
//            initTabVelocity();
//            gpVelocity.setMaxWidth(250);
//
//        });
//
//        tabDistance.setOnSelectionChanged(e -> {
//            tabDistance.setText(ADD_BY_DISTANCE);
//            tabAngle.setText("Angle");
//            tabVelocity.setText("Velocity");
//
//            gpDistance.getChildren().clear();
//            gpAngle.getChildren().clear();
//            gpVelocity.getChildren().clear();
//
//            initTabDist();
//        });
//
//        tabAngle.setOnSelectionChanged(e -> {
//            tabDistance.setText("Distance");
//            tabAngle.setText(ADD_BY_ANGLE);
//            tabVelocity.setText("Velocity");
//
//            initTabAngle();
//        });
//
//        btnAddByDistance.setOnAction(event -> {
//            try {
//                double startX = Double.parseDouble(inputStartX.getText());
//                double startY = Double.parseDouble(inputStartY.getText());
//                double endX = Double.parseDouble(inputEndX.getText());
//                double endY = Double.parseDouble(inputEndY.getText());
//                double velocity = Double.parseDouble(inputVelocity.getText());
//                double angle = Double.parseDouble(inputAngle.getText());
//                int n = Integer.parseInt(inputCount.getText());
//
//                if (n > 0)
//                    addParticlesByDistance(n, startX, startY, endX, endY, velocity, angle, ballPane);
//            } catch (NumberFormatException e) {
//                notif.setText("Invalid input\n");
//            }
//            ;
//        });
//
//        btnAddByAngle.setOnAction(event -> {
//            try {
//                final double startX = Double.parseDouble(inputStartX.getText());
//                final double startY = Double.parseDouble(inputStartY.getText());
//                final double startAngle = Double.parseDouble(inputStartAngle.getText());
//                final double endAngle = Double.parseDouble(inputEndAngle.getText());
//                final double velocity = Double.parseDouble(inputVelocity.getText());
//
//                final int n = Integer.parseInt(inputCount.getText());
//
//                if (n >= 0)
//                    addParticlesByAngle(n, startX, startY, startAngle, endAngle, velocity, ballPane);
//
//            } catch (NumberFormatException e) {
//                notif.setText("Invalid input\n");
//            }
//        });
//
//        btnAddByVelocity.setOnAction(event -> {
//            try {
//                final double startX = Double.parseDouble(inputStartX.getText());
//                final double startY = Double.parseDouble(inputStartY.getText());
//                final double startVelocity = Double.parseDouble(inputStartVelocity.getText());
//                final double endVelocity = Double.parseDouble(inputEndVelocity.getText());
//                final double angle = Double.parseDouble(inputAngle.getText());
//
//                final int n = Integer.parseInt(inputCount.getText());
//
//                if (n > 0)
//                    addParticlesByVelocity(n, startX, startY, startVelocity, endVelocity, angle, ballPane);
//            } catch (NumberFormatException e) {
//                notif.setText("Invalid input\n");
//            }
//        });

//    private void initTabVelocity() {
//        gpDistance.getChildren().clear();
//        gpAngle.getChildren().clear();
//        gpVelocity.getChildren().clear();
//
//        gpVelocity.addRow(0, labelConstXY);
//        gpVelocity.addRow(1, gpStartXY);
//        gpVelocity.addRow(2, labelStartEndVelocity);
//        gpVelocity.addRow(3, gpStartEndVelocity);
//        gpVelocity.addRow(4, labelAngle);
//        gpVelocity.addRow(5, inputAngle);
//        gpVelocity.addRow(6, separator1);
//        gpVelocity.addRow(7, labelCount);
//        gpVelocity.addRow(8, inputCount);
//        gpVelocity.addRow(9, btnAddByVelocity);
//        gpVelocity.setMaxWidth(250);
//        gpVelocity.addRow(10, notif);
//    }
//
//    private void initTabDist() {
//        gpDistance.getChildren().clear();
//        gpAngle.getChildren().clear();
//        gpVelocity.getChildren().clear();
//
//        gpDistance.addRow(0, labelStartXY);
//        gpDistance.addRow(1, gpStartXY);
//        gpDistance.addRow(2, labelEndXY);
//        gpDistance.addRow(3, gpEndXY);
//        gpDistance.addRow(4, labelVelocity);
//        gpDistance.addRow(5, inputVelocity);
//        gpDistance.addRow(6, labelAngle);
//        gpDistance.addRow(7, inputAngle);
//        gpDistance.addRow(8, separator1);
//        gpDistance.addRow(9, labelCount);
//        gpDistance.addRow(10, inputCount);
//        gpDistance.addRow(11, btnAddByDistance);
//        gpDistance.setMaxWidth(250);
//        gpDistance.addRow(12, notif);
//    }
//
//    private void initTabAngle() {
//        gpDistance.getChildren().clear();
//        gpAngle.getChildren().clear();
//        gpVelocity.getChildren().clear();
//
//        gpAngle.addRow(0, labelConstXY);
//        gpAngle.addRow(1, gpStartXY);
//        gpAngle.addRow(2, labelStartEndAngle);
//        gpAngle.addRow(3, gpStartEndAngle);
//        gpAngle.addRow(4, labelVelocity);
//        gpAngle.addRow(5, inputVelocity);
//        gpAngle.addRow(6, separator1);
//        gpAngle.addRow(7, labelCount);
//        gpAngle.addRow(8, inputCount);
//        gpAngle.addRow(9, btnAddByAngle);
//        gpAngle.setMaxWidth(250);
//        gpAngle.addRow(10, notif);
//
//    }
//    private void addParticlesByAngle(int n, double startX, double startY, double startAngle, double endAngle,
//    double velocity, Pane paneBall) {
//    double angleDiff = (endAngle - startAngle) / (n);
//    double angle = startAngle;
//    for (int i = 0; i < n; i++) {
//        ball_buf.add(new Particle(startX, startY, Math.toRadians(angle), velocity));
//        angle += angleDiff;
//
//    }
//    drawBalls(paneBall);
//}
//
//private void addParticlesByVelocity(int n, double startX, double startY, double startVelocity, double endVelocity,
//        double angle, Pane ballPane) {
//    double velocityDiff = (endVelocity - startVelocity) / (n);
//    double v = startVelocity;
//    for (int i = 0; i < n; i++) {
//
//        double velocity = v;
//        
//        ball_buf.add(new Particle(startX, startY, Math.toRadians(angle), velocity));
//
//        v += velocityDiff;
//
//    }
//    drawBalls(ballPane);
//}

//    private void addParticlesByDistance(int n, double startX, double startY, double endX, double endY, double velocity,
//            double angle, Pane ballPane) {
//        double dx = (endX - startX) / (n);
//        double dy = (endY - startY) / (n);
//        double x = startX;
//        double y = startY;
//
//        for (int i = 0; i < n; i++) {
//            double xin = x, yin = y;
//
//            ball_buf.add(new Particle(xin, yin, Math.toRadians(angle), velocity));
//            x += dx;
//            y += dy;
//        }
//
//        drawBalls(ballPane);
//
//    }
//    private Object Lock = new Object();
