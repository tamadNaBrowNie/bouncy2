import java.io.Serializable;

public class Entity implements Serializable{
private double x,y,size;
private Type type;
public String name;
@Override
public String toString() {
	
	
	
	return String.format("X: %.2f, Y : %.2f", this.x,this.y) +" "+ isRight+" " + type+" "+name;
}
private boolean isRight;
	Entity(double x,double y, double size, Type type,boolean facing){
		this.x=x;
		this.y = y;
		this.type = type;
		this.isRight = facing;
		this.setSize(size);
		this.name = "f00";
	}
	Entity(double x,double y, double size, Type type,boolean facing, String name){
		this.x=x;
		this.y = y;
		this.type = type;
		this.isRight = facing;
		this.setSize(size);
		this.name = name;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public void setName(String newName) {
		this.name = newName;
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
