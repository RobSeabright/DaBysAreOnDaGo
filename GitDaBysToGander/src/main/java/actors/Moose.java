
package actors;

import game.GameSpeedHandler;
import game.Stage;


public class Moose extends Item {

    private int damage = 2;
    
    public Moose(Stage stage) {
        super(stage);

        sprites = new String[]{"moose.png"};

        frame = 0;
        frameSpeed = 50;
        actorSpeed = 10;
        width = 90;
        height = 101;
        
    }
   
    public int getDamage(){
        return this.damage;
    }
    
    public void update(int gameTimer) {
            super.update(gameTimer);
            updateYSpeed(gameTimer);
            updateXSpeed();
    }

    private void updateXSpeed() {
            posX += getVx();
            if (posX > stage.getWidth()) setMarkedForRemoval(true);		
    }
    
    private void updateYSpeed(int gameTimer) {
            posY += GameSpeedHandler.getInstance().CalcSpeed(gameTimer);
            if (posY > stage.getHeight()) setMarkedForRemoval(true);		
    }
    
    public void collision(Actor a) {
        playSound("wrench.wav");
        setMarkedForRemoval(true);
    }

}
