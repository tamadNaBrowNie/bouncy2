import java.awt.Color; //ball coloring wala lang para colorful
import java.awt.Graphics;

import javax.swing.JPanel; //gui

public class Bouncing extends JPanel{
	
	Bouncing(int x, int y, int velocity){
		//TODO: how to know direction/pass speed
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.fillOval(0,0,25,25); //<- TODO: 0,0 yung x and y starting pos ng bola
		
	}
}