/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import game.Stage;

/**
 *
 * @author eric.stock
 */
public class Pothole extends Item {

    private int damage = 1;
    
    public Pothole(Stage stage) {
        super(stage);

        sprites = new String[]{"pothole.png"};

        frame = 0;
        frameSpeed = 50;
        actorSpeed = 10;
        width = 60; 
        height = 42;
        
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
        playSound("pothole.wav");
        setMarkedForRemoval(true);
    }

}
