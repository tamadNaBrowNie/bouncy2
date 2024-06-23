import java.util.concurrent.Callable;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Particle implements Callable<Timeline> {

	private double x, y, v, theta;
	private Timeline tl;
	private Circle circle;

	Particle(double x, double y,
			double theta, double v) {

		this.x = x;
		this.y = 720 - y;
		this.v = v;
		this. circle = new Circle(5, Color.RED);
		double d = circle.getRadius()*2;
		this.x += d;
		this.y += d;
		if (this.x >= 1280) {
			this.x = 1280 - d;
		}
		if (this.y<0)
			this.y = d;
		circle.setLayoutX(this.x);
		circle.setLayoutY(this.y);
		
		
	}
	public Circle getCircle()
	{
		return circle;
	}	
	@Override
	public Timeline call() throws Exception {
		double ppu = v * 0.0166666666667;
		double d = circle.getRadius()*2;
		this.tl = new Timeline(new KeyFrame(Duration.millis(16.666667),
			new EventHandler<ActionEvent>() {

				
				double dx = -ppu * Math.cos(theta);
				double dy = -ppu * Math.sin(theta);

				@Override
				public void handle(ActionEvent t) {
					// move the ball

					double x = circle.getLayoutX(), y = circle.getLayoutY();
					// If the ball reaches the left or right border make the step negative
					if (x < d ||
							x > 1280 - d) {
						dx = -dx;

					}

					// If the ball reaches the bottom or top border make the step negative
					if (y > 720 - d||
							y < d) {
						dy = -dy;

					}
					 circle.setLayoutX(x + dx);
					 circle.setLayoutY(y + dy);
				}
			}));

		tl.setCycleCount(Timeline.INDEFINITE);
		
		
		return tl;
	}
}