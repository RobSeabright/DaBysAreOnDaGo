package game;

/**
 *
 * @author ROB.SEABRIGHT
 */
public class GameSpeedHandler {

    private static GameSpeedHandler instance = new GameSpeedHandler();
    
    public static GameSpeedHandler getInstance() {
        return instance;
    }
    
    private static float maxSpeed;
    private static float maxFrames;
    private static float currentSpeed;

    private GameSpeedHandler() {
        maxSpeed = 20f;
        maxFrames = 9000f;
        currentSpeed = 4f;
    }

    public float CalcSpeed(float gameTimer) {
        
        currentSpeed = 4.0f + (gameTimer / maxFrames) * (maxSpeed - 1.0f);
        currentSpeed = Math.min(currentSpeed, maxSpeed);

        return currentSpeed;
    }

}
