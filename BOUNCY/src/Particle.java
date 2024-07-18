import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Particle implements Callable<Circle> {

	private double x, y, v, theta,d;
	private Timeline tl;
	private Circle circle;
	private final double X_MAX=1280, Y_MAX=720;
	private ExecutorService es;
	Particle(double x, double y,
			double theta, double v,ExecutorService es) {
		int r = 1;
		this.x = x;
		this.y = Y_MAX - y;
		this.v = v;
		this.theta = theta;
		this.d = r*2;
		this.es=es;

		this.circle = new Circle(r, Color.RED);
		if (this.x > X_MAX) 
			this.x = X_MAX - d;
		else if (this.x < 0)
			this.x = d;
		
		if (this.y < 0)
			this.y = d;
		else if (this.y > Y_MAX) 
			this.x = Y_MAX - d;
		circle.setLayoutX(this.x);
		circle.setLayoutY(this.y);
		this.tl = new Timeline();
	}

	public void play() {
		tl.play();
	}

	@Override
	public Circle call() throws Exception {
		final double T= 0.0166666666667;
		double ppu = v * T;
		

		tl.getKeyFrames()
				.add(new KeyFrame(Duration.seconds(T),
						new EventHandler<ActionEvent>() {
							
							double dx = -ppu * Math.cos(theta);
							double dy = -ppu * Math.sin(theta);
							@Override
							public void handle(ActionEvent t) {
								// move the ball

								double x = circle.getLayoutX(), y = circle.getLayoutY();
								if(x < 0 ||x > X_MAX)
									dx=-dx;
								if (y > Y_MAX ||y < 0) 
									dy=-dy;
								if(circle.isVisible()) {
									circle.setLayoutX(x + dx);
									circle.setLayoutY(y + dy);
								}
							}
						}));
		// this.tl = new Timeline();

		tl.setCycleCount(Timeline.INDEFINITE);

		return circle;
	}
}