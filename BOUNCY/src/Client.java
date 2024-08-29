import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Client extends Application {
	private static final double DY = 38.34;
	private static final double DX = 38.34;
	private static final double SIZ_OTHER = 10;
	private static final int RAD = 12;
	private final Label fpsLabel = new Label("FPS: 0");
	private long lastFPSTime = System.nanoTime();
	private double fps = 0;
	private boolean hasExplorer = false;

	private final double X_MAX = 1280, Y_MAX = 720;
	private final Pane paneContainer = new Pane();

	private final Pane ballPane = new Pane();

	private final Pane paneRight = new Pane();

	private final GridPane gridPane = new GridPane();

	private Server_Interface server = null;
	private final String ENTRY_TXT = "BECOME AN EXPLORER";
    private final Button btnAddExplorer = new Button(ENTRY_TXT);

	private final GridPane gpDistance = new GridPane();
	private final GridPane gpVelocity = new GridPane();
	private final GridPane gpAngle = new GridPane();

	private final GridPane gpStartXY = new GridPane();
	private final GridPane gpEndXY = new GridPane();
	private final GridPane gpStartEndVelocity = new GridPane();
	private final GridPane gpStartEndAngle = new GridPane();

	private final Label labelXYexp = new Label("Spawn Explorer on (X,Y) Coordinates:");
	private final TextField inputXexp = new TextField();
	private final TextField inputYexp = new TextField();
	private final GridPane gpExplorerXY = new GridPane(); // this is for the XY textfields ONLY

	private final Label labelIP = new Label("Input IP:");
	private final TextField inputIP = new TextField();

	private final Label labelName = new Label("Input Client Name:");
	private final TextField inputName = new TextField();

	private final Label labelPort = new Label("Input Port Number:");
	private final TextField inputPort = new TextField();

	private final GridPane gpExplorer = new GridPane();

	private final TextArea tester = new TextArea("(Test) Balls rn:\n");

	private final Separator separatorV = new Separator();

	private final Label notif = new Label("ERROR GO HERE");
	private final Label textTest = new Label("");

	private final float EX_BOUND = 5.2164841f;

	private final GridPane gpContainer = new GridPane();
	private final GridPane paneLeft = new GridPane();
	private final Pane paneExp = new StackPane();
	private final GridPane gpDebug = new GridPane();
	private static final ExecutorService es = Executors.newCachedThreadPool();

	private static Registry registry;

	public static void main(String[] args) {
		launch(args);
	}

	private final ReentrantReadWriteLock serverLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock expLock = new ReentrantReadWriteLock();

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
		paneRight.setVisible(false);
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
		/*
		 *
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

		btnAddExplorer.setOnAction(this::ExploreEvent);
		super.init();

	}
	private void ExploreEvent(ActionEvent event) {
		ExploreEvent();
	}
	private void ExploreEvent() {
		try {

			btnAddExplorer.setDisable(true);
			changeMode();
		} catch (RemoteException e) {

			warnUnreachable(e);
			btnAddExplorer.setDisable(false);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(paneContainer);
		primaryStage.setTitle("Particle Physics Simulator Client");
		primaryStage.setScene(scene);
		primaryStage.show();

		scene.setOnKeyPressed(this::handleKB);
		System.nanoTime();
		lastFPSTime = System.nanoTime();

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16.66667), new EventHandler<ActionEvent>() {
			boolean clr = true;
			@Override
            public void handle(ActionEvent t) {
                clr = !clr;
                makeFrame(clr);
            }
        }));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		AnimationTimer ticer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				fps++;
				double curr = now - lastFPSTime;

				if (curr < 500_000_000) return;
				fps *= 1_000_000_000.0 / curr;

				fpsLabel.setText(format("FPS: %.2f", fps));
				lastFPSTime = now;
				fps = 0;

			}

		};
		ticer.start();

	}

	public void makeFrame(boolean clear) {
		Server_Interface server = getServer();
		if (server == null) {
			return;
		}
		if (clear) {
			ballPane.getChildren().clear();
			return;
		}
		Future<List<Node>> result = es.submit(() -> {

            try {
                return server.updateServer(my_X, my_Y, getuName(), paneExp.getScaleX() > 0).parallelStream()
                        .map(this::toNode).collect(Collectors.toList());

            } catch (RemoteException e) {
                warnUnreachable(e);

            }
            return null;
        });
		try {

			List<Node> nodes = result.get(1, TimeUnit.SECONDS);
			if (nodes != null && !nodes.isEmpty())
				drawBalls(nodes);

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			notif.setText("Server Timeout");
			ExploreEvent();
			result.cancel(true);
			e.printStackTrace();
		}
	}

	private void warnUnreachable(Exception e) {
		warnUnreachable(e, "Server unreachable");
	}

	private void warnUnreachable(Exception e, String msg) {
		setExploring(false);
		notif.setText(msg);
		setServer(null);
		changeControls(false);
		e.printStackTrace();
	}

    private void handleKB(KeyEvent e) {
		switch (e.getCode()) {
		case E:
			ExploreEvent();
			break;
		case ESCAPE:
			Platform.exit();
			break;
		default:
			break;
		}
		if (!isExploring()) {
			return;
		}
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

        String msg = "\nYou are at (in px): X: [ %.2f ], Y: [ %.2f ]";
        notif.setText(format(msg, my_X, my_Y));
		ballPane.setLayoutX((halfSizX - my_X) * DX);
		ballPane.setLayoutY((my_Y - halfSizY) * DY);
	}

	private Node toNode(Entity ent) {

		if (ent == null)
			return null;
		Node entity;
		switch (ent.type) {
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
			return null;

		}
		ent.setName("ENTITY BALL");
		entity.setId(ent.name);
		entity.setLayoutX(ent.x);
		entity.setLayoutY(ent.y);
		entity.setVisible(true);
		return entity;
	}

	@Override
	public void stop() throws Exception {
//		if (es != null) {
//			leaveGame(getuName());
//			super.stop();
//			return;
//		}
		es.execute(() -> leaveGame(getuName()));
		es.shutdown();

		super.stop();

	}

	private void leaveGame(String uName) {
		Server_Interface server = getServer();
		if (uName.isEmpty())
			return;
		try {
			if (server == null)
				throw new RemoteException();
			server.leaveGame(uName);

		} catch (RemoteException e) {
			warnUnreachable(e);
		} finally {
			setServer(null);
		}
	}

	private void changeMode() throws RemoteException, InterruptedException, ExecutionException {

		boolean exploring = isExploring();

		if (exploring) {
			notif.setText("");
			Future<?> result = es.submit(() -> leaveGame(getuName()));
			try {
				result.get(1, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				result.cancel(true);
				notif.setText("You have already left");
			}
			my_X = 0;
			my_Y = 0;
		} else if (inputName.getText().isEmpty()) {
			notif.setText("NAME EMPTY");
			setExploring(true);
		} else {
			notif.setText("Going to " + inputPort.getText());
			explore();
		}
		setExploring(!isExploring());
		changeControls(isExploring());

	}

	private void changeControls(boolean exp) {

		inputXexp.setVisible(!exp);
		inputYexp.setVisible(!exp);
		inputIP.setDisable(exp);
		inputPort.setDisable(exp);
		inputName.setDisable(exp);
		paneRight.setVisible(exp);
		btnAddExplorer.setDisable(false);
        String LEAVE_TXT = "LEAVE";
        btnAddExplorer.setText((exp) ? LEAVE_TXT : ENTRY_TXT);

	}

	private void explore() {
		try {

			notif.setText("Exploring/...");
			double ex_X = Double.parseDouble(inputXexp.getText()), ex_Y = Double.parseDouble(inputYexp.getText());
			String ip = inputIP.getText(), name = inputName.getText();
			int port = Integer.parseInt(inputPort.getText());
			ballPane.setScaleX(DX);
			ballPane.setScaleY(DY);

			if (ex_X > X_MAX || ex_X < 0 || ex_Y > Y_MAX || ex_Y < 0)
				throw new NumberFormatException();
			double off_x = (ex_X >= X_MAX) ? EX_BOUND : (ex_X <= 0) ? -EX_BOUND : 0f,
					off_y = (ex_Y >= Y_MAX) ? -EX_BOUND : (ex_Y <= 0) ? EX_BOUND : 0f;

			my_X = ex_X -= off_x;
			my_Y = ex_Y += off_y;
			ex_X = (ballPane.getWidth() * 0.5 - ex_X) * DX;
			ex_Y = (ex_Y - ballPane.getHeight() * 0.5) * DY;
			ballPane.relocate(ex_X, ex_Y);

			if (joinServer(ip, name, port))
				return;

			notif.setText("Can't join :(");

		} catch (NumberFormatException e) {
			notif.setText("Invalid inputs.\n");
		}
		setExploring(true);
	}

	public boolean joinServer(String ip, String name, int port) {

		Future<Boolean> result = es.submit(() -> {
            System.setProperty("java.rmi.server.hostname", ip);

            try {
                registry = LocateRegistry.getRegistry(ip, port);
                if (registry == null)
                    throw new NotBoundException("No registry");
                setServer((Server_Interface) registry.lookup("Server"));

                Server_Interface server = getServer();

                if (server == null) {
                    throw new NotBoundException("Server not found");
                }
                setuName(name);
                return server.joinGame(my_X, my_Y, name);
            } catch (NotBoundException e) {
                e.printStackTrace();
                String msg = e.getMessage();
                Platform.runLater(() -> warnUnreachable(e, msg));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
			return false;

        });
		try {
			return result.get(1, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			result.cancel(true);
			notif.setText("Connetion Timed out");
		}
		return false;
	}

	private void drawBalls(List<Node> nodes) {
		try {

			ballPane.getChildren().addAll(nodes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Server_Interface getServer() {


		serverLock.readLock().lock();
		Server_Interface server = this.server;
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

	private final ReentrantReadWriteLock nameLock = new ReentrantReadWriteLock();

	public String getuName() {

		nameLock.readLock().lock();
		String name = uName;
		nameLock.readLock().unlock();
		return name;
	}

	public void setuName(String uName) {

		nameLock.writeLock().lock();
		this.uName = uName;

		nameLock.writeLock().unlock();
	}

	public boolean isExploring() {

		expLock.readLock().lock();
		boolean hasExplorer = this.hasExplorer;
		expLock.readLock().unlock();
		return hasExplorer;
	}

}
