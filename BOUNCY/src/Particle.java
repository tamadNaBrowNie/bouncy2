import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Particle {
    private double x, y;
    private double velocity;
    private double angle;
    private static final double RADIUS = 10;

    public Particle(double x, double y, double velocity, double angle) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.angle = angle;
    }

    public void update(double deltaTime, double canvasWidth, double canvasHeight) {
        x += velocity * Math.cos(angle) * deltaTime;
        y += velocity * Math.sin(angle) * deltaTime;

        if (x - RADIUS < 0 || x + RADIUS > canvasWidth) {
            angle = Math.PI - angle;
            x = Math.max(RADIUS, Math.min(x, canvasWidth - RADIUS));
        }
        if (y - RADIUS < 0 || y + RADIUS > canvasHeight) {
            angle = -angle;
            y = Math.max(RADIUS, Math.min(y, canvasHeight - RADIUS));
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
    }
}