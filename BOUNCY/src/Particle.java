import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Particle {

	private double x, y, v, theta;
	private Circle circle;
	private final double X_MAX = 1280, Y_MAX = 720;

	Particle(double x, double y,
			double theta, double v) {

		this.x = x;
		this.y = Y_MAX - y;
		this.v = v;
		this.theta = theta;

	}

	public Circle draw() {
		int r = 12;
		
		this.circle = new Circle(r, Color.RED);
		final double T = 0.0166666666667;
		double ppu = v * T;

		if (this.x > X_MAX)
			this.x = X_MAX;
		if (this.x < 0)
			this.x = 0;

		if (this.y < 0)
			this.y = 0;
		if (this.y > Y_MAX)
			this.x = Y_MAX;
		circle.setLayoutX(this.x);
		circle.setLayoutY(this.y);
		circle.setTranslateX(-ppu * Math.cos(theta));
		circle.setTranslateY(-ppu * Math.sin(theta));
		circle.managedProperty().bind(circle.visibleProperty());
		return circle;
		
	}



}