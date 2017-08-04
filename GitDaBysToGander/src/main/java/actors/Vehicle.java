package actors;

import actors.Actor;
import actors.KeyboardControllable;
import game.Stage;
import java.awt.event.KeyEvent;

/**
 *
 * @author ROB.SEABRIGHT
 */
public class Vehicle extends Actor implements KeyboardControllable {
    
    private boolean left,right;
    private int health = 4;
    
    public Vehicle(Stage stage) {
        super(stage);
        sprites = new String[]{"car.png"};
       resetVehicle();
    }
    
    public void resetVehicle() {
        frame = 0;
        frameSpeed = 35;
        actorSpeed = 10;
        width = 115;
        height = 207;
        posX = 800;
        posY = 550;
        setHealth(11);
    }
    
    public void setHealth(int health){
        this.health = health;
    }
    
    public int getHealth(){
        return this.health;
    }
    
    public void update(int gameTimer) {
        super.update(gameTimer);
        updateSpeed();
    }
	
    protected void updateSpeed() {
        vx = 0;
        
        if (left)
                vx = -actorSpeed;
        if (right)
                vx = actorSpeed;

        //don't allow scrolling off the edge of the screen		
        if (posX - width > 90 && vx < 0)
                posX += vx;
        else if (posX + width  + (width)< 1200 && vx > 0)
                posX += vx;
    }
    
            
    @Override
    public void triggerKeyRelease(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
                left = false;
                break;
        case KeyEvent.VK_RIGHT:
                right = false;
                break;
        }
    }

    @Override
    public void triggerKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
                left = true;
                break;
        case KeyEvent.VK_RIGHT:
                right = true;
                break;
        }
    }
    
    
}
