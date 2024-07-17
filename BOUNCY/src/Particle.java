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
	private double check(double coord, double d, double max, double delta){
		return coord+(coord<d|| coord>max-d)?-max:max;
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

		double dx = -ppu * Math.cos(theta);
		double dy = -ppu * Math.sin(theta);
		tl.getKeyFrames()
				.add(new KeyFrame(Duration.millis(16.666667),
						new EventHandler<ActionEvent>() {


							@Override
							public void handle(ActionEvent t) {
								// move the ball
								double x , y;
								// TODO: USE CONCURRENCY
								
								x=check(circle.getLayoutX(),d,1280,dx);
								y=check(circle.getLayoutY(),d,720,dy);
								// TODO only set layout if visible
								//TODO find a way to do a cull check (hide if out of bounds
								// Wait i should do that in Main.java
								if(circle.isVisible()){
								circle.setLayoutX(x);
								circle.setLayoutY(y);
								}
							}
						}));
		
		tl.setCycleCount(Timeline.INDEFINITE);

		return circle;
	}
}
