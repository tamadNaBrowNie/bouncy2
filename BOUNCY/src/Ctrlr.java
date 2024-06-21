
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.shape.Circle;
public class Ctrlr implements EventHandler<ActionEvent>() {
    double dx,dy,x,y;
    Circle circle;
    Ctrlr(double dx,double dy,Circle circle) {

this.circle= circle;
this.x= 
this.circle.getLayoutX();
this.y= this.circle.getLayoutY ();

this.dx= dx;
this.dy= dy;
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