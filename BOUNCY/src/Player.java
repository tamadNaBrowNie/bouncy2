import javafx.scene.Node;

public class Player {
	Player(int i, long t,Node node){
		index = i;
		time = t;
		this.setNode(node);
	}
private int index;
private long time;
private Node node;
public int getIndex() {
	return index;
}
public void setIndex(int index) {
	this.index = index;
}
public long getTime() {
	return time;
}
public void setTime(long time) {
	this.time = time;
}
public Node getNode() {
	return node;
}
public void setNode(Node node) {
	this.node = node;
}

}
