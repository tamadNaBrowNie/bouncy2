import java.awt.Graphics;
import java.util.concurrent.Callable;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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

	}
	public void play()
	{
		tl.play();
	}	
	@Override
	public Circle call() throws Exception {
		double ppu = v * 0.0166666666667;

		Circle circle = new Circle(1, Color.RED);
		double d = circle.getRadius();
		this.x += circle.getRadius() * 2;
		this.y += d;
		if (this.x >= 1280) {
			this.x = 1280 - d;
		}
		if (this.y > 720)
			this.y = 720 - d;
		circle.setLayoutX(this.x);
		circle.setLayoutY(this.y);
		// EventHandler<ActionEvent> foo = new Ctrlr(ppu*Math.cos(theta),-ppu*
		// Math.sin(theta),this.circle);

		// this.tl = new Timeline(new KeyFrame(Duration.millis(17)),foo);

		this.tl = new Timeline(new KeyFrame(Duration.millis(17),
				new EventHandler<ActionEvent>() {

					// double dx = 7; //Step on x or velocity
					// double dy = 3; //Step on y

					double dx = -ppu * Math.cos(theta);
					double dy = -ppu * Math.sin(theta);

					@Override
					public void handle(ActionEvent t) {
						// move the ball

						double x = circle.getLayoutX(), y = circle.getLayoutY();
						// Bounds bounds = canvas.getBoundsInLocal();

						// If the ball reaches the left or right border make the step negative
						if (x < (d) ||
								x > (1280 - d)) {

							dx *= -1;

						}

						// If the ball reaches the bottom or top border make the step negative
						if ((y > (720 - d)) ||
								y < d) {

							dy *= -1;

						}
						// circle.relocate(x+dx, y+dy);
						circle.setLayoutX(x + dx);
						circle.setLayoutY(y + dy);
					}
				}));

		tl.setCycleCount(Timeline.INDEFINITE);
//		Platform.runLater(()->play());
		
		return circle;
	}
}