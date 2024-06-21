import java.awt.Color; //ball coloring wala lang para colorful
import java.awt.Graphics;
import java.util.concurrent.Callable;

import javafx.scene.shape.Circle;

public class Particle implements Callable {
	
	Thread th;
	Graphics g;
	Circle circle;
	private double x,y;
	private double dx,dy;
	Particle(float x,float  y,
			float theta, float v){ 
		float ppu = v/60;
		double rad = Math.toRadians(theta);
		this.dx = ppu*Math.cos(rad);
		this.dy = -ppu* Math.sin(rad);
				this.x = x;
				this.y = y;
//				this.circle = new Circle(10.f,Color.RED);
				this.circle = new Circle(10.f); //
				this.circle.setCenterX ( this.x);
				this.circle.setCenterY ( this.y);
	}
public Circle getBall(){
	return this.circle;
}
private float collide(int max, double delta, double pos){
	double future = delta+pos;
	if (future<0||future>max)
	return -1;
	return 1;
}
	@Override
	public Object call() throws Exception {
		this.circle.setTranslateX(dx);
		this.circle.setTranslateY(dy);
		this.x+= this.dx;
		this.y+= this.dy;
		
		this.dx *= this.collide(1280,dx,x);
		this.dy *= this.collide(720,dy,y);
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'call'");
	}
}