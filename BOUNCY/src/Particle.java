import java.util.concurrent.Callable;

import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Particle implements Callable<Circle> {

	private double x, y, v, theta,d;
	private Timeline tl;
	private Circle circle;
	private final double X_MAX=1280, Y_MAX=720;
	
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

	@Override
	public Circle call() throws Exception {
		final double T= 0.0166666666667;
		double ppu = v * T;
		int r = 1;
		this.d = r*2;
		
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

		double dx = -ppu * Math.cos(theta);
		double dy = -ppu * Math.sin(theta);
		circle.setTranslateX(dx);
		circle.setTranslateY(dy);
		
		return circle;
	}
}