import java.awt.Color; //ball coloring wala lang para colorful
import java.awt.Graphics;
import java.util.concurrent.Callable;

import javax.swing.JComponent;	//swing tho not java fx
import javax.swing.JPanel; //gui



//eto ung threaded ball
//bale there will be many instances of this ball and calculations ng bounces
//paramihan ng balls na masspawn (in the thousands) without sacrificing FPS
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
				this.circle = new Circle(10.f,Color.RED);
				this.circle.setCenterX ( this.x);
				this.circle.setCenterY ( this.y);
	}
//	
//	public void draw(Graphics g)
//	{
//		g.setColor(Color.red);
//		g.fillOval(x1, y1, 50, 50);
//		
//	}
//}

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
		this.x += this.dx;
		this.y+= this.dy;
		
		this.dx *= this.collide(1280,dx,x);
		this.dy *= this.collide(720,dy,y);
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'call'");
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
	
