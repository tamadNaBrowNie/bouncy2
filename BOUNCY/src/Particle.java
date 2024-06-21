import java.awt.Color; //ball coloring wala lang para colorful
import java.awt.Graphics;

import javax.swing.JComponent;	//swing tho not java fx
import javax.swing.JPanel; //gui

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;



//eto ung threaded ball
//bale there will be many instances of this ball and calculations ng bounces
//paramihan ng balls na masspawn (in the thousands) without sacrificing FPS
public class Particle extends Thread {
	
	Thread th;
//	Graphics g;
	private int x1,x2,y1,y2,type;
	private double a1,a2,v1,v2;
    private GraphicsContext gc;
    Circle ball;
    
	Particle(int startX, int endX, int startY, int endY,
			double startAngle, double endAngle, double startVelocity, double endVelocity,
			int type, GraphicsContext gc){
		this.x1=startX;
		this.x2=endX;
		this.y1=startY;
		this.y2=endY;
		this.a1=startAngle;
		this.a2=endAngle;
		this.v1=startVelocity;
		this.v2=endVelocity;
		this.type=type;
		this.gc = gc;
		
		ball = new Circle(10);	//size 10 radius, so 20px ball.
		ball.relocate((1280/2)+x1,(720/2)+y1);
//        ball.relocate((1280/2)-x1, x2);
        //(1280/2)+x1 : midpoint ng panel where 0,0 exists + startX
//        canvasTest.getChildren().add(ball);
        
        
//		start();	//thread start
	}
	    @Override
	    public void run() {
	        while (true) {
	            move();
	            Platform.runLater(() -> draw(gc));
	            try {
	                Thread.sleep(16); // Approximately 60 FPS
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	
	
	
//		System.out.println(this.x1+", "+this.y1+"    "+this.x2+", "+this.y2);//starting points
//		sta
//		th = new Thread(new Thread()) {
//			public void run() {
//				while(true) {
        private void move() {
			int maxWidth = 1280;
			int maxHeight = 720;
	        double rad1 = Math.toRadians(a1);
	        double rad2 = Math.toRadians(a2);
			
			//directions
			//left if x2 < x1
			//right if x2 > x1
			//paakyat if y2 > y1
			//pababa if y2 < y1
	        
	        if (type==1) {
	        	/*
	        	 Provide an integer n indicating the number of particles to add.
				 -dito sa loob ng if: Keep the velocity and angle constant.
				 -DONE GUI - Provide a start point and end point.
				 -DONE SA MAIN.Java: Particles are added with a uniform distance between the given start and end points.?
				 */
	        	//WAIT A SEC, PAG MAY POINT1 AND POINT2 NA BAKET MAY ANGLE PA??
	        	//-ans:A user should be able to add particles to the environment
	        	 //with an initial position (x,y), an initial angle Θ (0 degrees is
	        		//	 east, and degrees increase in an anticlockwise manner, e.g.,
	        			// 90 degrees is north), and a velocity V (in pixel per second).
	        	 //- so mag iincrease yung angle ig, slope + angle
	        	//- arctan of slope m = radians
	        	//slope = y2-y1/x2-x1
	        	double slope = Math.atan((y2-y1)/(x2-x1));
	        	
	        	
	        	 x1 += v1 * Math.cos(rad1+slope);	//constant so doesnt matter if start or end vel/angle
	             y1 -= v1 * Math.sin(rad1+slope);

	             //x1 is start point so it's supposed to be updated till end point x2.
	             if (x1 <= 0 || x1 >= maxWidth - rad1+slope) {
	                 a1 = 180 - a1;	//update angle
	             }
	             if (y1 <= 0 || y1 >= 720 - 50) { //50 size of bilog
	                 a1= 360 - a1+Math.toDegrees(slope);
	             }

	             if (a1 < 0) {
	                 a1 += 360;
	             } else if (a1 >= 360) {
	                 a1 -= 360;
	             }
	             
	             
	        }
        }   
        
	public void draw(GraphicsContext gc) {
//        gc.setFill(Color.RED);
		
        gc.fillOval(x1, y1, 2 * 50, 2 * 50);
    }
	
//	
//	public void draw(Graphics g)
//	{
//		g.setColor(Color.red);
//		g.fillOval(x1, y1, 50, 50);
//		
//	}
//}
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
	
