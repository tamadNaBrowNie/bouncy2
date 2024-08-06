import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Server extends Application {
	private static final int l_width = 250;
// 1099;
	private static final String prompt_n = "Number of Particles:";
	private static final String V_PX_S = "Velocity (px/s):";
	private static final String ADD_BY_ANGLE = "Add by Angle";
	private static final String ADD_BY_VELOCITY = "Add by Velocity";
	private static final String ADD_BY_DISTANCE = "Add by Distance";
	private Label fpsLabel = new Label("FPS: 0");
	private long lastFPSTime = System.nanoTime();
	private double fps = 0;

	private final static double X_MAX = 1280;
	private final static double Y_MAX = 720;
	private Pane paneContainer = new Pane();

	private static Pane ballPane = new Pane();

	private Pane paneRight = new Pane();

	private GridPane gridPane = new GridPane();

	private TextField inputStartX = new TextField();
	private TextField inputStartY = new TextField();

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
	private Button btnAddByDistance = new Button(ADD_BY_DISTANCE);
	private Button btnAddByAngle = new Button(ADD_BY_ANGLE);
	private Button btnAddByVelocity = new Button(ADD_BY_VELOCITY);

	private GridPane gpDistance = new GridPane();
	private GridPane gpVelocity = new GridPane();
	private GridPane gpAngle = new GridPane();

	private GridPane gpStartXY = new GridPane();
	private GridPane gpEndXY = new GridPane();
	private GridPane gpStartEndVelocity = new GridPane();
	private GridPane gpStartEndAngle = new GridPane();

	private TabPane paneTab = new TabPane();
	private Tab tabDistance = new Tab(ADD_BY_DISTANCE);
	private Tab tabAngle = new Tab("Angle");
	private Tab tabVelocity = new Tab("Velocity");

	private TextArea tester = new TextArea("(Test) Balls rn:\n");

	private Separator separator1 = new Separator();
	private Separator separatorV = new Separator();

	private Label notif = new Label("");

	private Label labelStartXY = new Label("Starting Points (X,Y):");

	private Label labelEndXY = new Label("End Points (X,Y):");
	private Label labelStartEndVelocity = new Label("Starting and Ending Velocity:");
	private Label labelStartEndAngle = new Label("Starting and Ending Angle:");
	private Label labelConstXY = new Label("Spawn Point (X,Y):");

	private GridPane gpContainer = new GridPane();
	private GridPane paneLeft = new GridPane();
	private GridPane gpDebug = new GridPane();
	private static ExecutorService es;

	private static ConcurrentHashMap<String, Player> playerList = new ConcurrentHashMap<String, Player>(0);

	public static void main(String[] args) {
		es = Executors.newFixedThreadPool(3);
		Server_Interface worker = new Server_Interface() {
			private List<Entity> getBalls(double x, double y, int width, int height, String name)
					throws RemoteException, InterruptedException, ExecutionException {

				FutureTask<List<Entity>> t = new FutureTask<>(new Callable<List<Entity>>() {

					@Override
					public List<Entity> call() throws Exception {

						List<Entity> entities = new ArrayList<>();
						entities = ballPane.getChildren().filtered(ent -> {
							double w_h = width * 0.5, h_h = height * 0.5, y2 = 720 - y;
							double lX = ent.getLayoutX(), lY = ent.getLayoutY();
							boolean named = (ent.getId() == null) ? false : ent.getId().equals(name);
							return !named && (lX >= x - w_h && lX <= x + w_h && lY >= y2 - h_h && lY <= y2 + h_h);
						}).stream().map(ent -> toEntity(ent)).collect(Collectors.toList());
						return entities;
					}

					private Entity toEntity(Node ent) {
						return (ent instanceof Circle)
								? new Entity(ent.getLayoutX(), ent.getLayoutY(), ent.getBoundsInParent().getHeight(),
										Type.BALL, ent.getScaleX() > 0)
								: new Entity(ent.getLayoutX(), ent.getLayoutY(), ent.getBoundsInParent().getHeight(),
										Type.EXP, ent.getScaleX() > 0, ent.getId());
					}

				});
				es.submit(() -> Platform.runLater(t));
				return t.get();

			}

			@Override
			public boolean joinGame(double x, double y, String name) throws RemoteException {
				boolean success = false;
				if (name == null) {
					return false;
				} else if (name.isEmpty()) {
					return false;
				}

				FutureTask<Boolean> t = new FutureTask<>(new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						ObservableList<Node> children = ballPane.getChildren();
						if (playerList.contains(name)
								|| children.stream().anyMatch(node -> name.equals(node.getId()))) {
							return false;
						}
						System.out.println(name + " is JOINING\n");
						Pane paneExp = new Pane();

						paneExp.setLayoutX(x);
						paneExp.setLayoutY(720 - y);
						paneExp.setStyle("-fx-border-color: blue;" + // Border color
								"-fx-border-width: 1px;" // Border width
						);
						paneExp.setId(name);
						paneExp.setMaxSize(4.99, 4.99);
						paneExp.setScaleX(20);
						paneExp.setScaleY(20);
						boolean success = children.add(paneExp);
						if (!success)
							return false;
						int i = children.size() - 1;
						if (paneExp != children.get(i))
							i = children.indexOf(paneExp);

						return playerList.put(name, new Player(i, System.nanoTime(), paneExp)) == null;
					}
				});

				Platform.runLater(t);
				try {
					success = t.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				return success;

			}

			@Override
			public void leaveGame(String name) throws RemoteException {

				if (name == null) {
					return;
				} else if (name.isEmpty()) {
					return;
				}
				es.execute(() -> playerList.forEach((k, v) -> {
					if (System.nanoTime()- v.getTime() > 30_000_000_000l)
						playerList.remove(k);
				}));
				Platform.runLater(() -> ballPane.getChildren()
						.removeIf(node -> node instanceof Pane && name.equals(node.getId())));

			}

			public boolean updatePos(double x, double y, String name, boolean face_right)
					throws InterruptedException, ExecutionException {
				//
				if (name == null) {
					return false;
				}

				FutureTask<Boolean> task = new FutureTask<>(new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						List<Node> players = ballPane.getChildren();
						if (players.isEmpty())
							return false;
						Player player = playerList.get(name);
						if (player == null) {
							ballPane.getChildren().removeIf(node -> node instanceof Pane && name.equals(node.getId()));
							return false;
						}

						Node child = players.get(player.getIndex());
						if (player.getNode() != child) {
							ballPane.getChildren().removeIf(node -> node instanceof Pane && name.equals(node.getId()));
							return false;
						}
						if (child.getLayoutX() != x && child.getLayoutY() != y) {
							child.setLayoutX(x);
							child.setLayoutY(720 - y);
							player.setTime(System.nanoTime());
						}
						System.out.print("NAME: " + name + " XY:" + x + " " + y + '\n');

						return true;
					}
				});
				Platform.runLater(task);
				return task.get();
			}

			@Override
			public List<Entity> updateServer(double x, double y, String name, boolean face_right)
					throws RemoteException, InterruptedException, ExecutionException {

				if (!this.updatePos(x, y, name, face_right)) {
					throw new RemoteException();
				}
				return this.getBalls(x, y, 33, 19, name);

			}

		};
		try {

			final int PORT_IN = 8880;
			Registry registry = LocateRegistry.createRegistry(PORT_IN);
			Remote obj = UnicastRemoteObject.exportObject(worker, 0);
			registry.rebind("Server", obj);
			System.out.println("Server running at //" + obj.toString() + ":" + PORT_IN + '\n');
			launch(args);
			UnicastRemoteObject.unexportObject(worker, false);
			registry.unbind("Server");
			UnicastRemoteObject.unexportObject(registry, false);
		} catch (RemoteException e) {
			System.out.println("Server failed");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("Server never bound");
			e.printStackTrace();
		}

		try {
			es.shutdown();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		paneRight.setLayoutX(270);
		paneRight.setPrefHeight(Y_MAX);
		paneRight.setMinWidth(1280);
		Rectangle clip = new Rectangle(X_MAX, Y_MAX);
		clip.setLayoutX(0);
		clip.setLayoutY(0);
		paneRight.setClip(clip);

		ballPane.setPrefHeight(Y_MAX);
		ballPane.setMinWidth(X_MAX);
		ballPane.relocate(0, 0);
		ballPane.setStyle("-fx-background-image:url('bg_grid.png');" + "-fx-border-color: blue;" + // Border color
				"-fx-border-width: 1px;" // Border width
		);

		gpDebug.addRow(0, fpsLabel);
		gpDebug.relocate(260, 0);

		gridPane.setAlignment(Pos.BASELINE_CENTER);

		tester.setMaxSize(l_width, Y_MAX);

		separatorV.setOrientation(Orientation.VERTICAL);

		gridPane.setMaxWidth(l_width);

		btnAddByDistance.setPrefWidth(l_width);
		btnAddByVelocity.setPrefWidth(l_width);
		btnAddByAngle.setPrefWidth(l_width);

		paneTab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		paneTab.getTabs().addAll(tabDistance, tabAngle, tabVelocity);
		paneTab.managedProperty().bind(paneTab.visibleProperty());
		// DISTANCE tab open by default
		initTabDist();

		gpStartXY.addRow(0, inputStartX, inputStartY);
		gpStartEndVelocity.addRow(0, inputStartVelocity, inputEndVelocity);
		gpEndXY.addRow(0, inputEndX, inputEndY);
		gpStartEndAngle.addRow(0, inputStartAngle, inputEndAngle);

		gpStartEndVelocity.setMaxWidth(l_width);
		gpStartEndAngle.setMaxWidth(l_width);
		gpStartXY.setMaxWidth(l_width);
		gpEndXY.setMaxWidth(l_width);
		gpVelocity.setMaxWidth(l_width);
		gpDistance.setMaxWidth(l_width);
		gpAngle.setMaxWidth(l_width);
		tabVelocity.setContent(gpVelocity);
		tabDistance.setContent(gpDistance);
		tabAngle.setContent(gpAngle);
		paneLeft.addRow(0, paneTab);
		gpContainer.addRow(0, paneLeft, separatorV, paneRight);
		paneContainer.getChildren().addAll(gpContainer, gpDebug);

		Scene scene = new Scene(paneContainer);
		primaryStage.setTitle("Particle Physics Simulator Server");
		primaryStage.setScene(scene);
		primaryStage.show();

//        This is when we add the ballPane to screen
		paneRight.getChildren().add(ballPane);

		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				Platform.exit();
			}
		});

		tabVelocity.setOnSelectionChanged(e -> {
			tabDistance.setText("Distance");
			tabAngle.setText("Angle");
			tabVelocity.setText(ADD_BY_VELOCITY);

			gpDistance.getChildren().clear();
			gpAngle.getChildren().clear();
			gpVelocity.getChildren().clear();
			initTabVelocity();
			gpVelocity.setMaxWidth(l_width);

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

				if (n > 0) {
					addParticlesByDistance(n, startX, startY, endX, endY, velocity, angle, ballPane);
				}
			} catch (NumberFormatException e) {
				notif.setText("Invalid input\n");
			}
		});

		btnAddByAngle.setOnAction(event -> {
			try {
				final double startX = Double.parseDouble(inputStartX.getText());
				final double startY = Double.parseDouble(inputStartY.getText());
				final double startAngle = Double.parseDouble(inputStartAngle.getText());
				final double endAngle = Double.parseDouble(inputEndAngle.getText());
				final double velocity = Double.parseDouble(inputVelocity.getText());

				final int n = Integer.parseInt(inputCount.getText());

				if (n > 0) {
					addParticlesByAngle(n, startX, startY, startAngle, endAngle, velocity, ballPane);
				}

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

				if (n > 0) {
					addParticlesByVelocity(n, startX, startY, startVelocity, endVelocity, angle, ballPane);
				}
			} catch (NumberFormatException e) {
				notif.setText("Invalid input\n");
			}
		});

		System.nanoTime();
		lastFPSTime = System.nanoTime();
		AnimationTimer ticer = new AnimationTimer() {
			@Override
			public void handle(long now) {

				fps++;
				ballPane.getChildren().filtered(node -> (node instanceof Circle))
						.forEach(circle -> mov_ball((Circle) circle));
				es.execute(() -> playerList.forEach((k, v) -> {
					if (now - v.getTime() > 30_000_000_000l)
						playerList.remove(k);
				}));
				double curr = now - lastFPSTime;

				if (curr < 500_000_000) {
					return;
				}
				fps *= 1_000_000_000.0 / curr;

				fpsLabel.setText(String.format("FPS: %.2f", fps));
				lastFPSTime = now;
				fps = 0;

			}

		};
		ticer.start();
	}

	private void mov_ball(Circle circle) {
		boolean isSeen = true;
		double x_0 = circle.getLayoutX(), y_0 = circle.getLayoutY(), dx = circle.getTranslateX(),
				dy = circle.getTranslateY(), x_f = x_0 + dx, y_f = y_0 + dy, r = circle.getRadius();
		circle.setVisible(isSeen);

		if (x_f < r || x_f > X_MAX - r) {
			circle.setTranslateX(-dx);
		}

		circle.setLayoutX(x_0 + circle.getTranslateX());
		if (y_f > Y_MAX - r || y_f < r) {
			circle.setTranslateY(-dy);
		}
		circle.setLayoutY(y_0 + circle.getTranslateY());
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

	private void drawBalls(List<Circle> circles, Pane ballPane) {
		if (!circles.isEmpty()) {
			ballPane.getChildren().addAll(circles);
		}
	}

	private Circle draw(double x, double y, double theta, double v) {
		int r = 4;

		Circle circle = new Circle(r, Color.RED);
		double ppu = v * 0.0166666666667;

		if (x > X_MAX - r) {
			x = X_MAX - r;
		} else if (x < r) {
			x = r;
		}

		if (y < r) {
			y = r;
		} else if (y > Y_MAX - r) {
			y = Y_MAX - r;
		}
		circle.relocate(x, 720 - y);
		circle.setTranslateX(-ppu * Math.cos(theta));
		circle.setTranslateY(-ppu * Math.sin(theta));
		return circle;
	}

	private void addParticlesByDistance(int n, double startX, double startY, double endX, double endY, double velocity,
			double angle, Pane ballPane) {
		try {
			ArrayList<Circle> balls = es.submit(new Callable<ArrayList<Circle>>() {

				@Override
				public ArrayList<Circle> call() throws Exception {
					final double dx = (n > 1) ? (endX - startX) / (n - 1) : 0,
							dy = (n > 1) ? (endY - startY) / (n - 1) : 0;
					double x = startX;
					double y = startY;
					ArrayList<Circle> balls = new ArrayList<>();
					for (int i = 0; i < n; i++) {
						double xin = x, yin = y;

						balls.add(draw(xin, yin, Math.toRadians(angle), velocity));
						x += dx;
						y += dy;
					}
					return balls;
				}
			}).get();

			drawBalls(balls, ballPane);
		} catch (InterruptedException | ExecutionException e) {

			e.printStackTrace();
		}

	}

	private void addParticlesByAngle(int n, double startX, double startY, double startAngle, double endAngle,
			double velocity, Pane paneBall) {
		try {
			ArrayList<Circle> balls = es.submit(new Callable<ArrayList<Circle>>() {

				@Override
				public ArrayList<Circle> call() throws Exception {

					final double angleDiff = (n > 1) ? (endAngle - startAngle) / (n - 1) : 0;
					double angle = startAngle;
					ArrayList<Circle> balls = new ArrayList<>();
					for (int i = 0; i < n; i++) {
						balls.add(draw(startX, startY, Math.toRadians(angle), velocity));
						angle += angleDiff;

					}
					return balls;
				}
			}).get();

			drawBalls(balls, ballPane);
		} catch (InterruptedException | ExecutionException e) {

			e.printStackTrace();
		}

	}

	private void addParticlesByVelocity(int n, double startX, double startY, double startVelocity, double endVelocity,
			double angle, Pane ballPane) {
		try {
			ArrayList<Circle> balls = es.submit(new Callable<ArrayList<Circle>>() {

				@Override
				public ArrayList<Circle> call() throws Exception {

					final double velocityDiff = (n > 1) ? (endVelocity - startVelocity) / (n - 1) : 0;
					ArrayList<Circle> balls = new ArrayList<>();
					double v = startVelocity;
					for (int i = 0; i < n; i++) {
						double velocity = v;

						balls.add(draw(startX, startY, Math.toRadians(angle), velocity));

						v += velocityDiff;

					}
					return balls;
				}
			}).get();

			drawBalls(balls, ballPane);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}
}
