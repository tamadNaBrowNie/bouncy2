import java.awt.Graphics;
import java.util.concurrent.Callable;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
public class Particle  implements Callable<double[]> {

	
	Graphics g;
	Circle circle;
	private double x,y, dx,dy;
	private Timeline tl;

	Particle(double x,double  y,
			double theta, double v){ 
		double ppu = v*0.0166666666667;

		this.x = x;
		this.y = y;
		this.circle = new Circle(10,Color.RED);
		this.circle.setCenterX ( this.x);
		this.circle.setCenterY ( this.y);
		this.circle.setLayoutX(this.x);
		this.circle.setLayoutY ( this.y);
		EventHandler<ActionEvent> foo = new Ctrlr(ppu*Math.cos(theta),-ppu* Math.sin(theta),this.circle);

		//this.tl = new Timeline(new KeyFrame(Duration.millis(17)),foo);

	}

	public Circle getBall(){
		return this.circle;
	}
	
	@Override
	public double[] call() throws Exception {
		if (this.x<=0||1280<=this.x){
			this.dx *=-1;
		}
		if (this.y<=0||this.y>=720){
			this.dy *=-1;
		}
		this.x += dx;
		this.y+= dy;
		this.circle.setCenterX ( this.x);
		this.circle.setCenterY ( this.y);
		this.circle.setLayoutX(this.x);
		this.circle.setLayoutY ( this.y);
		
		//  tl.setCycleCount(Timeline.INDEFINITE);
	    //     tl.play();
		double [] res = {this.x,this.y};
		return res;
	}
}