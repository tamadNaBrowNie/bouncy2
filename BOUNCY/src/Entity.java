import java.io.Serializable;

public class Entity implements Serializable{
/**
	 *
	 */
	private static final long serialVersionUID = 4L;
public final double x, y;
final Type type;
public String name;
@Override
public String toString() {



	return String.format("\nX: %.2f, Y : %.2f ", this.x,this.y) + type+" "+name;
}

    Entity(double x, double y, Type type){
		this.x=x;
		this.y = y;
		this.type = type;
        this.name = "f00";
	}
	Entity(double x, double y, Type type,  String name){
		this(x,y, type);
		this.name = name;
	}
	public void setName(String newName) {
		this.name = newName;
	}



}
