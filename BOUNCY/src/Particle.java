import java.awt.Graphics;
import java.util.concurrent.Callable;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
public class Particle  implements Callable<double[]> {

	
	Graphics g;
	Circle circle;
	private double x,y, dx,dy, velocity, theta, ppu;

	private Timeline tl;

	Particle(double x,double  y,
			double theta, double v){ 
		this.ppu = v*0.0166666666667;
		this.theta = theta;
		this.velocity = v;
		this.x = x;
		this.y = y;
		this.circle = new Circle(10,Color.RED);
//		this.circle.setCenterX ( this.x);
//		this.circle.setCenterY ( this.y);
		this.circle.setLayoutX(this.x);
		this.circle.setLayoutY ( this.y);
//		EventHandler<ActionEvent> foo = new Ctrlr(ppu*Math.cos(theta),-ppu* Math.sin(theta),this.circle);
		double ppu = v*0.0166666666667;

		//this.tl = new Timeline(new KeyFrame(Duration.millis(17)),foo);
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), 
                new EventHandler<ActionEvent>() {

//        	double dx = 7; //Step on x or velocity
//        	double dy = 3; //Step on y

			double dx = ppu*Math.cos(theta);
			double dy = -ppu* Math.sin(theta);
        	
            @Override
            public void handle(ActionEvent t) {
            	//move the ball
            	circle.setLayoutX(circle.getLayoutX() + dx);
            	circle.setLayoutY(circle.getLayoutY() + dy);

//                Bounds bounds = canvas.getBoundsInLocal();
                
                //If the ball reaches the left or right border make the step negative
                if(circle.getLayoutX() <= (0 + (circle.getRadius()*2)) || 
                		circle.getLayoutX() >= (1280 - (circle.getRadius()*2)) ){

                	dx = -dx;
                	

                }

                //If the ball reaches the bottom or top border make the step negative
                if((circle.getLayoutY() >= (720 - (circle.getRadius()*2))) || 
                        (circle.getLayoutY() <= (0 + (circle.getRadius()*2)))){

                	dy = -dy;

                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
		
	}

	public Circle getBall(){
		return this.circle;
	}
	
	@Override
	public double[] call() throws Exception {
		double [] res = {this.circle.getLayoutX(),this.circle.getLayoutY()};

		return res;
	}
}