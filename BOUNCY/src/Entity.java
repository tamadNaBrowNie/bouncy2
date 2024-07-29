
public class Entity {
private double x,y;
private Type type;
	Entity(double x,double y, Type type){
		this.x=x;
		this.y = y;
		this.type = type;
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
}
