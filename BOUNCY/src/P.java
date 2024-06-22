
import javafx.concurrent.Task;

import javafx.scene.canvas.GraphicsContext;
class P extends Task<Void>{
    Particle particle;
    GraphicsContext gc;
    p(Particle ball,GraphicsContext gc){
        this.particle=ball;
        this.gc =gc;
    }
    protected Void call() throws Exception{ 
        gc.setFill(particle.getBall().getFill());
    gc.fillOval(particle.getBall().getCenterX() - particle.getBall().getRadius(),
                particle.getBall().getCenterY() - particle.getBall().getRadius(),
                particle.getBall().getRadius() * 2, particle.getBall().getRadius() * 2);
    return null;}