
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Particle{

	private double x, y, v, theta, d;

	
	private final double X_MAX = 1280, Y_MAX = 720;


	Particle(double x, double y,
			double theta, double v) {
		this.x = x;
		this.y = Y_MAX - y;
		this.v = v;
		this.theta = theta;
		
	}


	
	public Circle draw() {
		try {
			return call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Circle call() throws Exception {
		final double T = 0.0166666666667;
		final int r = 1;

		this.d = r <<1;
		Circle circle = new Circle(r, Color.RED);
		
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
		double ppu = v * T;

		circle.setTranslateX(-ppu * Math.cos(theta));
		circle.setTranslateY(-ppu * Math.sin(theta));


		return circle;
	}
}