
package actors;

import game.Stage;

/**
 *
 * @author ROB.SEABRIGHT
 */
public class Wrench extends Item {

    private int damage = -1;
    
    public Wrench(Stage stage) {
        super(stage);

        sprites = new String[]{"wrench.png"};

        frame = 0;
        frameSpeed = 50;
        actorSpeed = 10;
        width = 50;
        height = 66;
        
    }
   
    public int getDamage(){
        return this.damage;
    }
    
    public void update(int gameTimer) {
            super.update(gameTimer);
            updateYSpeed();
    }

    private void updateYSpeed() {
            posY += getVy();
            if (posY > stage.getHeight()) setMarkedForRemoval(true);		
    }

    public void collision(Actor a) {
        playSound("wrench.wav");
        setMarkedForRemoval(true);
    }

}

