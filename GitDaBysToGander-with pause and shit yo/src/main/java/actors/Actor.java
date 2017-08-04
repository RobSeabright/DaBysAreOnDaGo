package actors;

import game.ResourceLoader;
import game.Stage;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Actor {

	private static final int POINT_VALUE = 0;
	protected float vx; 
	protected float vy;
	protected float posX;
	protected float posY;
	protected int height;
	protected int width;
	protected int frame;
	protected int frameSpeed;
	protected int actorSpeed;
	protected int time;
	private boolean markedForRemoval = false;
	protected String[] sprites = null; 
	protected Stage stage = null;

	public Actor(Stage stage) {
		this.stage = stage;
		frame = 0;
		frameSpeed = 1;
		actorSpeed = 10;
		time = 0;
	}
	
	public void update(int gameTimer) {
		updateSpriteAnimation();
	}
	
	private void updateSpriteAnimation() {
		time++;
		if (time % frameSpeed == 0) {
			time = 0;
			frame = (frame + 1) % sprites.length;
		}
	}
	
	public void playSound(final String name) {
		new Thread(new Runnable() {
			public void run() {
				ResourceLoader.getInstance().getSound(name).play();
			}
		}).start();
	}

			
	public void paint(Graphics g) {		
		g.drawImage(ResourceLoader.getInstance().getSprite(sprites[frame]), (int)posX, (int)posY, stage);
	}
	
	public void setX(int posX) {
		this.posX = posX;
	}
	
	public void setY(int posY) {
		this.posY = posY;
	}
	
	public float getX() {
		return posX;
	}
	
	public float getY() {
		return posY;
	}
	
	protected void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	protected void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}
	
	public void setVx(int vx) {
		this.vx = vx;
	}

	public double getVx() {
		return vx;
	}
	
	public void setVy(int vy) {
		this.vy = vy;
	}

	public double getVy() {
		return vy;
	}

	public Rectangle getBounds() {
		return new Rectangle((int)posX,(int)posY,width, height);
	}
	
	public void collision(Actor a) {		
	}
	
	public void setMarkedForRemoval(boolean markedForRemoval) {
		this.markedForRemoval = markedForRemoval;
	}

	public boolean isMarkedForRemoval() {
		return markedForRemoval;
	}
	
	public int getPointValue() {
		return Actor.POINT_VALUE;
	}
}
