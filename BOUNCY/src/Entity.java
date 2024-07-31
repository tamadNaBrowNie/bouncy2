
public class Entity {
private double x,y;
private Type type;
private boolean isRight;
	Entity(double x,double y, Type type,boolean facing){
		this.x=x;
		this.y = y;
		this.type = type;
		this.isRight = facing;
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
	
}
