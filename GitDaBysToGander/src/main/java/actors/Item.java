
package actors;

import game.Stage;

/**
 *
 * @author ROB.SEABRIGHT
 */
public class Item extends Actor{
    
    private int damage;
    
    public Item(Stage stage) {
        super(stage);
    }

    public int getDamage() {
        return this.damage;
    }
    
    
}
