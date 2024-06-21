
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.shape.Circle;
public class Ctrlr implements EventHandler<ActionEvent>() {
    double dx,dy,x,y;
    Circle circle;
    Ctrlr(double x,double y,double dx,double dy,Circle circle) {
this.x= x;
this.y= y;
this.dx= dx;
this.dy= dy;
this.circle= circle;
    }
@Override
    public void handle(ActionEvent t) {		
if (this.x<=0||1280<=this.x){
this.dx *=-1;
}
if (this.y<=0||this.y>=720){
this.dy *=-1;
}
this.x += dx;
this.y+= dy;
this.circle.setLayoutX(this.x);
this.circle.setLayoutY ( this.y);
}
};