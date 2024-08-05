import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Client extends Application {
	private static final double DY = 38.34;
	private static final double DX = 38.34;
	private static final double SIZ_OTHER = 10;
	private static final int RAD = 12;
	private Label fpsLabel = new Label("FPS: 0");
	private long lastFPSTime = System.nanoTime();
	private double fps = 0;
	private boolean hasExplorer = false;

	private final double X_MAX = 1280, Y_MAX = 720;
	private Pane paneContainer = new Pane();

	private Pane ballPane = new Pane();

	private Pane paneRight = new Pane();

	private GridPane gridPane = new GridPane();

	private Server_Interface server = null;
	private final String ENTRY_TXT = "BECOME AN EXPLORER";
	private final String LEAVE_TXT = "LEAVE";
	private Button btnAddExplorer = new Button(ENTRY_TXT);

	private GridPane gpDistance = new GridPane();
	private GridPane gpVelocity = new GridPane();
	private GridPane gpAngle = new GridPane();

	private GridPane gpStartXY = new GridPane();
	private GridPane gpEndXY = new GridPane();
	private GridPane gpStartEndVelocity = new GridPane();
	private GridPane gpStartEndAngle = new GridPane();

	private Label labelXYexp = new Label("Spawn Explorer on (X,Y) Coordinates:");
	private TextField inputXexp = new TextField();
	private TextField inputYexp = new TextField();
	private GridPane gpExplorerXY = new GridPane(); // this is for the XY textfields ONLY

	private Label labelIP = new Label("Input IP:");
	private TextField inputIP = new TextField();

	private Label labelName = new Label("Input Client Name:");
	private TextField inputName = new TextField();

	private Label labelPort = new Label("Input Port Number:");
	private TextField inputPort = new TextField();

	private GridPane gpExplorer = new GridPane();

	private TextArea tester = new TextArea("(Test) Balls rn:\n");

	private Separator separatorV = new Separator();

	private Label notif = new Label("ERROR GO HERE");
	private Label textTest = new Label("");

	private final float EX_BOUND = 5.2164841f;

	private GridPane gpContainer = new GridPane();
	private GridPane paneLeft = new GridPane();
	private Pane paneExp = new StackPane();
	private GridPane gpDebug = new GridPane();
	private static ExecutorService es = Executors.newCachedThreadPool();

	private static Registry registry;

	public static void main(String[] args) {
		launch(args);
	}

	private ReentrantReadWriteLock serverLock = new ReentrantReadWriteLock(), expLock = new ReentrantReadWriteLock();

	private double my_X = 0, my_Y = 0;
	private String uName = "";

	@Override
	public void init() throws Exception {

		paneRight.setLayoutX(270);
		paneRight.setPrefHeight(Y_MAX);
		paneRight.setMinWidth(1280);
		Rectangle clip = new Rectangle(X_MAX, Y_MAX);
		clip.setLayoutX(0);
		clip.setLayoutY(0);
		paneRight.setClip(clip);
		paneRight.setVisible(hasExplorer);
		ballPane.setPrefHeight(Y_MAX);
		ballPane.setMinWidth(X_MAX);
		ballPane.setLayoutX(0);
		ballPane.setLayoutY(0);
		ballPane.setStyle("-fx-background-image:url('bg_grid.png');" + "-fx-border-color: blue;" + // Border color
				"-fx-border-width: 1px;" // Border width
		);
		gpDebug.addRow(0, fpsLabel);
		gpDebug.setLayoutX(260);
		gpDebug.setLayoutY(0);

		gridPane.setAlignment(Pos.BASELINE_CENTER);

		tester.setMaxSize(250, Y_MAX);

		separatorV.setOrientation(Orientation.VERTICAL);

		gridPane.setMaxWidth(250);

		gpStartEndVelocity.setMaxWidth(250);
		gpStartEndAngle.setMaxWidth(250);
		gpStartXY.setMaxWidth(250);
		gpEndXY.setMaxWidth(250);
		gpVelocity.setMaxWidth(250);
		gpDistance.setMaxWidth(250);
		gpAngle.setMaxWidth(250);

		// will look like
		/**
		 * Input Server IP Number:(?) need pa ba Input Port Number: [ ] Spawn Explorer
		 * on (X,Y) coordinates: [ ][ ] Join Server btn
		 */

		// All tha can be seen:
		// can remove all else na UI elems
		gpExplorer.addRow(0, labelName);
		gpExplorer.addRow(1, inputName);
		gpExplorer.addRow(2, labelIP);
		gpExplorer.addRow(3, inputIP);
		gpExplorer.addRow(4, labelPort);
		gpExplorer.addRow(5, inputPort);
		gpExplorer.addRow(6, labelXYexp);
		gpExplorerXY.addRow(1, inputXexp);
		gpExplorerXY.addRow(1, inputYexp);
		gpExplorer.addRow(7, gpExplorerXY);
		gpExplorer.addRow(8, btnAddExplorer);
		gpExplorer.addRow(9, notif);
		gpExplorer.addRow(10, textTest);

		gpExplorer.setMaxWidth(250);

		paneLeft.addRow(0, gpExplorer);
		paneRight.getChildren().add(ballPane);
		Pane pSprite = new Pane();
		pSprite.setMaxSize(30, 30);
		pSprite.setStyle("-fx-border-color: yellow;" + // Border color
				"-fx-border-width: 1px;" // Border width
		);

		gpContainer.addRow(0, paneLeft, separatorV, paneRight);
		paneContainer.getChildren().addAll(gpContainer, gpDebug);

		// Holds the sprite
		StackPane spExplorer = new StackPane();

		paneExp.setStyle("-fx-border-color: blue;" + // Border color
				"-fx-border-width: 1px;" // Border width
		);

		spExplorer.getChildren().add(paneExp);

		spExplorer.setLayoutX(58);
		spExplorer.setLayoutY(0);

		spExplorer.setPrefSize(X_MAX, Y_MAX);
		paneExp.setMaxSize(200, 200);
		paneRight.getChildren().add(spExplorer);
		spExplorer.setVisible(true);

		btnAddExplorer.setOnAction(event -> {
			try {
				notif.setText("Going to " + inputPort.getText());
				btnAddExplorer.setDisable(true);
				changeMode();
			} catch (RemoteException e) {

				warnUnreachable(e);
				btnAddExplorer.setDisable(false);
			}

		});
		super.init();

	}

	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(paneContainer);
		primaryStage.setTitle("Particle Physics Simulator Client");
		primaryStage.setScene(scene);
		primaryStage.show();

		scene.setOnKeyPressed(e -> handleKB(e));
		System.nanoTime();
		lastFPSTime = System.nanoTime();

		AnimationTimer ticer = new AnimationTimer() {
			private boolean clear = true;

			@Override

			public void handle(long now) {
				// fast negate
				this.clear ^= true;
				boolean clear = this.clear;
				fps++;
				expLock.readLock().lock();
				Server_Interface server = getServer();
				expLock.readLock().unlock();
				if (server == null) {
					return;
				}
				try {

					if (clear) {
						ballPane.getChildren().clear();
						return;
					}
					List<Node> nodes = es.submit(new Callable<List<Node>>() {

						@Override
						public List<Node> call() throws Exception {
							try {
								return server.updateServer(my_X, my_Y, uName, paneExp.getScaleX() > 0).parallelStream()
										.map(ent -> toNode(ent)).collect(Collectors.toList());

							} catch (RemoteException e) {

								warnUnreachable(e);
							}

							return null;
						}

					}).get();
					drawBalls(nodes);

				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

				double curr = now - lastFPSTime;

				if (curr < 500_000_000)
					return;
				fps *= 1_000_000_000.0 / curr;

				fpsLabel.setText(String.format("FPS: %.2f", fps));
				lastFPSTime = now;
				fps = 0;
			}

		};
		ticer.start();

	}

	private void warnUnreachable(RemoteException e) {
		setExploring(false);

		notif.setText("Server unreachable");
		setServer(null);
		changeControls(false);
		e.printStackTrace();
	}

	private final String msg = "\nYou are at (in px):\n" + "X: [ %.2f ]\n" + "Y: [ %.2f ]";

	private void handleKB(KeyEvent e) {
		switch (e.getCode()) {
			case E:
				btnAddExplorer.fire();
				break;
			case ESCAPE:
				Platform.exit();
				break;
			default:
				break;
		}
		expLock.readLock().lock();
		if (!hasExplorer) {
			expLock.readLock().unlock();
			return;
		}
		expLock.readLock().unlock();
		;
		switch (e.getCode()) {
			case W:
				my_Y++;
				break;
			case S:
				my_Y--;
				break;
			case A:
				my_X--;
				break;
			case D:
				my_X++;
				break;

			default:
				break;
		}
		final double EX_LIM = 2 + EX_BOUND;
		double halfSizX = X_MAX * 0.5, halfSizY = Y_MAX * 0.5;

		if (my_X >= X_MAX - EX_BOUND)
			my_X = X_MAX - EX_LIM;

		else if (my_X <= EX_BOUND)
			my_X = EX_LIM;

		if (my_Y >= Y_MAX - EX_BOUND)
			my_Y = Y_MAX - EX_LIM;

		else if (my_Y <= EX_BOUND)
			my_Y = EX_LIM;

		notif.setText(String.format(msg, my_X, my_Y));
		ballPane.setLayoutX((halfSizX - my_X) * DX);
		ballPane.setLayoutY((my_Y - halfSizY) * DY);
	}

	private Node toNode(Entity ent) {
		Node entity = null;
		if (ent == null)
			return null;
		switch (ent.getType()) {
			case BALL:
				entity = new Circle(RAD, Paint.valueOf("Red"));
				break;
			case EXP:
				Pane exp = new Pane();
				exp.setMaxSize(SIZ_OTHER, SIZ_OTHER);
				exp.setStyle("-fx-border-color: blue;" + // Border color
						"-fx-border-width: 1px;" // Border width
				);
				entity = exp;
				break;
			default:
				notif.setText("Nothing found");
				break;

		}
		if (entity != null) {
			ent.setName("ENTITY BALL");
			entity.setId(ent.name);
			entity.setLayoutX(ent.getX());
			entity.setLayoutY(ent.getY());
			entity.setVisible(true);

		}
		return entity;
	}

	@Override
	public void stop() throws Exception {

		leaveGame(uName);
		if (es != null)
			es.shutdown();
		super.stop();

	}

	private void leaveGame(String uName) {
		Server_Interface server = getServer();
		serverLock.readLock().lock();
		if (uName.isEmpty())
			return;
		try {
			if (server == null)
				throw new RemoteException();
			server.leaveGame(uName);

		} catch (RemoteException e) {
			warnUnreachable(e);
		} finally {
			serverLock.readLock().unlock();
			setServer(null);
		}
	}

	private void changeMode() throws RemoteException {

		setExploring(!hasExplorer);
		if (!hasExplorer) {
			leaveGame(uName);
			my_X = 0;
			my_Y = 0;

		} else if (inputName.getText().isEmpty()) {
			notif.setText("NAME EMPTY");

			setExploring(false);
		} else
			explore();
		expLock.readLock().lock();
		changeControls(hasExplorer);
		expLock.readLock().unlock();

	}

	private void changeControls(boolean exp) {

		inputXexp.setVisible(!exp);
		inputYexp.setVisible(!exp);
		inputIP.setDisable(exp);
		inputPort.setDisable(exp);
		inputName.setDisable(exp);
		paneRight.setVisible(exp);
		btnAddExplorer.setDisable(false);
		btnAddExplorer.setText((exp) ? LEAVE_TXT : ENTRY_TXT);

	}

	private void explore() throws AccessException, RemoteException {
		try {

			notif.setText("Exploring/...");
			double ex_X = Double.parseDouble(inputXexp.getText());
			double ex_Y = Double.parseDouble(inputYexp.getText());
			if (ex_X > X_MAX || ex_X < 0 || ex_Y > Y_MAX || ex_Y < 0)
				throw new NumberFormatException();
			double off_x = (ex_X >= X_MAX) ? EX_BOUND : (ex_X <= 0) ? -EX_BOUND : 0f,
					off_y = (ex_Y >= Y_MAX) ? -EX_BOUND : (ex_Y <= 0) ? EX_BOUND : 0f;
			ballPane.setScaleX(DX);
			ballPane.setScaleY(DY);
			my_X = ex_X -= off_x;
			my_Y = ex_Y += off_y;
			ex_X = (ballPane.getWidth() * 0.5 - my_X) * DX;
			ex_Y = (my_Y - ballPane.getHeight() * 0.5) * DY;
			ballPane.setLayoutX(ex_X);
			ballPane.setLayoutY(ex_Y);

			try {

				String ip = inputIP.getText();
				System.setProperty("java.rmi.server.hostname", ip);
				int PORT_NUM = Integer.parseInt(inputPort.getText());

				registry = LocateRegistry.getRegistry(ip, PORT_NUM);
				notif.setText(ip);
				if (registry == null)
					throw new NotBoundException("No registry");
				setServer((Server_Interface) registry.lookup("Server"));
				serverLock.readLock().lock();
				Server_Interface server = getServer();

				if (server == null) {
					serverLock.readLock().unlock();
					throw new NotBoundException("Server not found");
				}
				uName = inputName.getText();
				if (!server.joinGame(my_X, my_Y, uName)) {
					notif.setText("Can't join :(");
					setExploring(false);
				}
				serverLock.readLock().unlock();

			} catch (NotBoundException e) {
				e.printStackTrace();
				notif.setText(e.getMessage());
			}
		} catch (NumberFormatException e) {
			notif.setText("Invalid Explorer coordinates.\n");
			setExploring(false);
		}
	}

	private void drawBalls(List<Node> nodes) {
		try {

			ballPane.getChildren().addAll(nodes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Server_Interface getServer() {

		Server_Interface server = null;
		serverLock.readLock().lock();
		server = this.server;
		serverLock.readLock().unlock();
		return server;
	}

	private void setServer(Server_Interface server) {
		serverLock.writeLock().lock();
		this.server = server;
		serverLock.writeLock().unlock();
	}

	private void setExploring(boolean hasExplorer) {
		expLock.writeLock().lock();
		this.hasExplorer = hasExplorer;
		expLock.writeLock().unlock();
	}

}