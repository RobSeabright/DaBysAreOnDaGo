package game;

import java.awt.event.KeyEvent;

import actors.KeyboardControllable;
/**
 * creates a thread to process player input
 * @author ghast
 *
 */
public class InputHandler {
    public enum Action {
        PRESS,
        RELSEASE
    }
    private Stage stage = null;
    private KeyboardControllable player  = null;
    public Action action;

    public InputHandler(Stage stg, KeyboardControllable player) {
        this.stage = stg;
        this.player = player;
    }

    public void handleInput(KeyEvent event) {
        if (action == Action.PRESS) {
            if (KeyEvent.VK_ENTER == event.getKeyCode()) {
                if (stage.gameOver || stage.gameWon) {
                    stage.initWorld();
                    //stage.game();
                }
            } else if (KeyEvent.VK_ESCAPE == event.getKeyCode()){
                if (stage.gameOver || stage.gameWon){
                    Menu menu = new Menu();
                    menu.launchMenu();
                }
            }
            else
                player.triggerKeyPress(event);
        }
        else if (action == Action.RELSEASE)
            player.triggerKeyRelease(event);
    }
}