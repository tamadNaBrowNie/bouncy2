import java.util.concurrent.Callable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Particle implements Callable<Circle> {

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
		final double T = 0.0166666666667;
		double ppu = v * T;
		int r = 3;
		// this.d = r<<1;

		this.circle = new Circle(r, Color.RED);
		if (this.x > X_MAX)
			this.x = X_MAX;
		else if (this.x < 0)
			this.x = 0;

		if (this.y < 0)
			this.y = 0;
		else if (this.y > Y_MAX)
			this.x = Y_MAX;
		circle.setLayoutX(this.x);
		circle.setLayoutY(this.y);
		circle.setTranslateX(-ppu * Math.cos(theta));
		circle.setTranslateY(-ppu * Math.sin(theta));
		circle.managedProperty().bind(circle.visibleProperty());
		return circle;
	}

	// public boolean isVisible(double minX, double maxX, double minY, double maxY)
	// {
	// double r = circle.getRadius();
	// return (x - r >= minX) && (x + r <= maxX) && (y - r >= minY) && (y + r <=
	// maxY);
	// }

	// public double getX() {
	// return x;
	// }

	// public double getY() {
	// return y;
	// }

	// public double getRadius() {
	// return circle.getRadius();
	// }
}