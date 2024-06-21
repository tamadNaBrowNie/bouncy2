//ball coloring wala lang para colorful
import java.awt.Graphics;
import java.util.concurrent.Callable;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class Particle extends Region implements Callable<double[]> {
	
	Thread th;
	Graphics g;
	Circle circle;
	private double x,y;
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
			this.tra.setX(-(this.tra.getX()));
		}
		if (this.y<=0||this.y>=720){
			this.tra.setY(-(this.tra.getY()));
		}
		double dx = this.tra.getX(), dy = this.tra.getY();
	
		
		this.relocate(dx, dy);
		this.x += dx;
		this.y+= dy;
		this.circle.getTransforms().addAll(this.tra);
		double [] res = {this.x,this.y};
		// TODO Auto-generated method stub

		return res;
	}
}
	
	//probs dont need these getters if dito din naman irrender mga particles
//	public int getX(){
//		return this.x;
//	}
//	public int getY(){
//		return this.y;
//	}
//	public int getDeg(){
//		return this.deg;
//	}
//	public int getVel(){
//		return this.vel;
//	}
//	
//	@Override
//	public void paint(Graphics g) {
//		super.paint(g);
//		g.setColor(Color.black);
//		g.fillOval(0,0,25,25); //<- TODO: 0,0 yung x and y starting pos ng bola
//		
	
