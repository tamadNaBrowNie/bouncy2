import java.awt.Graphics;
import java.util.concurrent.Callable;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class Particle extends Region implements Callable<double[]> {
	
	Graphics g;
	Circle circle;
	private double x,y, dx,dy;
	private Translate tra;
	Particle(double x,double  y,
			double theta, double v){ 
		double ppu = v/60;
		double rad = Math.toRadians(theta);
		this.tra = new Translate (ppu*Math.cos(rad),-ppu* Math.sin(rad));
				this.x = x;
				this.y = y;
				this.circle = new Circle(10,Color.RED);
				this.circle.setCenterX ( this.x);
				this.circle.setCenterY ( this.y);
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
		double [] res = {this.x,this.y};
		return res;
	}
}