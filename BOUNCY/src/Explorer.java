import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.control.Label;


public class Explorer{

	//wasd movements
	private BooleanProperty s_key = new SimpleBooleanProperty();
	private BooleanProperty a_key = new SimpleBooleanProperty();
	private BooleanProperty w_key = new SimpleBooleanProperty();
	private BooleanProperty d_key = new SimpleBooleanProperty();
	//if something is being pressed
	private BooleanBinding keyPressed = s_key.or(a_key).or(w_key).or(d_key);
	private Timeline tl;
	private double x,y;
	private Pane paneExp = new Pane();
	AnimationTimer timer;
	
	Explorer(double expX, double expY, Label textTest) {
		this.x = expX;
		this.y = 720 - expY; //idk if reverse paden?
		

		paneExp.requestFocus();//for wasd movements
		//this is the explorer (it is now a pane, so there is a bg)
		String backgroundImageURL = new File(System.getProperty("user.dir")+"\\src\\amongus.png").toURI().toString();
        Image bgImage = new Image(backgroundImageURL);
		
        paneExp.setBackground(new Background(new BackgroundImage (
				bgImage,
				BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null, new BackgroundSize(
                		BackgroundSize.AUTO,BackgroundSize.AUTO,
                        false,false,true,false
                )))
		);

        paneExp.setLayoutX(expX);
        paneExp.setLayoutY(expY);
        paneExp.setPrefSize(30,30);
        
        
        timer = new AnimationTimer() {

			@Override
			public void handle(long timestamp) {
				// TODO Auto-generated method stub
				if (w_key.get())	//move up, x=0,y=1
				{
					paneExp.setLayoutX(paneExp.getLayoutY()-1); // 1 px/nanosecond ang movement spd
	                textTest.setText("W is being pressed.");
				}
				if (s_key.get())
				{
	                textTest.setText("S is being pressed.");
				}
				if (a_key.get())
				{
	                textTest.setText("A is being pressed.");
				}
				if (d_key.get())
				{
	                textTest.setText("D is being pressed.");
				}
			}
        	
        };
	}
	
	public Pane getPane() {
//        paneExp.setLayoutX(x);
//        paneExp.setLayoutY(y);
//        paneExp.setPrefSize(30,30);
		return paneExp;
	}
	public AnimationTimer getTimer() {
//      paneExp.setLayoutX(x);
//      paneExp.setLayoutY(y);
//      paneExp.setPrefSize(30,30);
		return timer;
	}
//
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		// TODO Auto-generated method stub
//		scene.setOnKeyPressed(e ->){
//			if(e.getCode()==KeyCode.W) {
//				w_key.set(true);
//			}
//		}

	}

	
	