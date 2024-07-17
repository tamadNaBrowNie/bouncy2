import java.util.concurrent.Callable;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Particle implements Callable<Circle> {

	private double x, y, v, theta;
	private Timeline tl;
	
	Particle(double x, double y,
			double theta, double v) {

		this.x = x;
		this.y = 720 - y;
		this.v = v;
		this.theta = theta;
		this.tl = new Timeline();
	}

	public void play() {
		tl.play();
	}

	@Override
	public Circle call() throws Exception {
		Circle circle;
		final int rad = 1;
		circle = new Circle(rad, Color.RED);
		double ppu = v * 0.0166666666667;

		double d = rad << 2;
		if (this.x >= 1280) {
			this.x = 1280 - d;
		}else if(0>=this.x){
			this.x =d;	
		}
		if (this.y <= 0)
			this.y = d;
		else if (this.y>= 720)
			this.y = 720-d;
		circle.setLayoutX(this.x);
		circle.setLayoutY(this.y);

		tl.getKeyFrames()
				.add(new KeyFrame(Duration.millis(16.666667),
						new EventHandler<ActionEvent>() {

							double dx = -ppu * Math.cos(theta);
							double dy = -ppu * Math.sin(theta);

							@Override
							public void handle(ActionEvent t) {
								// move the ball
								boolean is_seen = true;
								double x = circle.getLayoutX(), y = circle.getLayoutY();
								// TODO: USE CONCURRENCY
								// If the ball reaches the left or right border make the step negative
								if (x < d ||
										x > 1280 - d) {
									dx = -dx;

								}

								// If the ball reaches the bottom or top border make the step negative
								if (y > 720 - d ||
										y < d) {
									dy = -dy;

								}
								x+=dx;
								y+=dy;
								// TODO only set layout if visible
								if(is_seen){
								circle.setLayoutX(x);
								circle.setLayoutY(y);
								}
							}
						}));
		
		tl.setCycleCount(Timeline.INDEFINITE);

		return circle;
	}
}
