import java.io.Serializable;

public class Entity implements Serializable{
private double x,y,size;
private Type type;
private boolean isRight;
	Entity(double x,double y, double size, Type type,boolean facing){
		this.x=x;
		this.y = y;
		this.type = type;
		this.isRight = facing;
		this.setSize(size);
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public Type getType() {
		return this.type;
	}
	public boolean isRight() {
		return isRight;
	}
	public double getSize() {
		return size;
	}
	private void setSize(double size) {
		this.size = size;
	}
	
}
