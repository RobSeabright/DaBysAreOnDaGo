package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import actors.Actor;
import actors.Vehicle;
import actors.Moose;
import actors.Pothole;
import actors.Wrench;
import static game.Stage.HEIGHT;
import static game.Stage.WIDTH;
import java.io.IOException;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
public class MooseGame extends Stage implements KeyListener, Runnable {

    private Vehicle vehicle;
    private int score;
    private BufferStrategy strategy;	 //double buffering strategy
    private InputHandler keyPressedHandler;
    private InputHandler keyReleasedHandler;
    private int gameTimer = 0;
    private int gameSpeed = 1;
    private int spawnTimer = 0;
    private long usedTime;

    private BufferedImage background, backgroundTile;
    private int backgroundY;

    Random rng = new Random();
    
    public MooseGame() {
        //init the UI

        setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
        setBackground(Color.green);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(Stage.WIDTH, Stage.HEIGHT));
        panel.setLayout(null);

        panel.add(this);

        JFrame frame = new JFrame("Invaders");
        frame.add(panel);

        frame.setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);

        //cleanup resources on exit
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ResourceLoader.getInstance().cleanup();
                System.exit(0);
            }
        });

        addKeyListener(this);
        //add a player
        vehicle = new Vehicle(this);

        keyPressedHandler = new InputHandler(this, vehicle);
        keyPressedHandler.action = InputHandler.Action.PRESS;
        keyReleasedHandler = new InputHandler(this, vehicle);
        keyReleasedHandler.action = InputHandler.Action.RELSEASE;
        //create a double buffer
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        requestFocus();
        initWorld();
    }

    public void initWorld() {
        actors = new ArrayList<Actor>();
        gameOver = false;
        vehicle.resetVehicle();
        score = 0;    
        gameTimer =0;
        
        backgroundTile = ResourceLoader.getInstance().getSprite("stage1.png");
        background = ResourceLoader.createCompatible(
                WIDTH, HEIGHT + backgroundTile.getHeight(),
                Transparency.OPAQUE);
        Graphics2D g = (Graphics2D) background.getGraphics();
        g.setPaint(new TexturePaint(backgroundTile,
                new Rectangle(0, 0, backgroundTile.getWidth(), backgroundTile.getHeight())));
        g.fillRect(0, 0, background.getWidth(), background.getHeight());
        backgroundY = backgroundTile.getHeight();
    }

    public void run() {
        loopSound("gameMusic.wav");

        usedTime = 0;
        int spawnRate = 1;

        while (isVisible()) {
            long startTime = System.currentTimeMillis();
            gameTimer++;
            spawnTimer++;
           
            if (spawnTimer > 200){
                spawnRate++;
                spawnTimer = 0;
            }
            
            backgroundY -= GameSpeedHandler.getInstance().CalcSpeed(gameTimer);
            if (backgroundY < 0) {
                backgroundY = backgroundTile.getHeight();
            }

            if (super.gameOver) {
                paintGameOver();
                //break;
            }
            else {
                
                score++;
                
                if (gameTimer > 2500 && gameTimer % 25 == 0){
                    System.out.println("3");
                    spawnActor();
                }
                else if (gameTimer > 1250 && gameTimer % 50 == 0){
                    System.out.println("2");
                    spawnActor();
                }
                else if (gameTimer > 10 && gameTimer % 110 == 0){
                    System.out.println("1");
                    spawnActor();
                }
                                
                updateWorld();
                paintWorld();
                checkCollision(vehicle);
            }

            usedTime = System.currentTimeMillis() - startTime;

            //calculate sleep time
            if (usedTime == 0) {
                usedTime = 1;
            }
            int timeDiff = (int) ((1000 / usedTime) - DESIRED_FPS);
            if (timeDiff > 0) {
                try {
                    Thread.sleep(timeDiff / 75);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void loopSound(final String name) {
        new Thread(new Runnable() {
            public void run() {
                ResourceLoader.getInstance().getSound(name).loop();
            }
        }).start();
    }
    
    public void spawnActor(){
        
        int spawnType = rng.nextInt(100) % 8;
        
        switch(spawnType) {
            case 0: case 1: case 2: case 3: 
                Actor moose = new Moose(this);
                moose.setX(rng.nextInt(800) + 200);
                moose.setY(0);
                moose.setVy(gameSpeed);
                //moose.setVx(gameSpeed);
                actors.add(moose);
                break;
            case 4: case 5: case 6:  
                Actor pothole = new Pothole(this);
                pothole.setX(rng.nextInt(800) + 200);
                pothole.setY(0);
                pothole.setVy(gameSpeed);
                actors.add(pothole);
                break;
            case 7:
                Actor wrench = new Wrench(this);
                wrench.setX(rng.nextInt(800) + 200);
                wrench.setY(0);
                wrench.setVy(gameSpeed);
                actors.add(wrench);
                break;
        }
    }

    public void updateWorld() {
        //get the graphics from the buffer
        Graphics g = strategy.getDrawGraphics();

        vehicle.update(gameTimer);
        paintHealth(g, vehicle.getHealth());

        int i = 0;

        while (i < actors.size()) {
            Actor actor = actors.get(i);

            if (actor.isMarkedForRemoval()) {
                actors.remove(i);
            } else {
                actor.update(gameTimer);
                i++;
            }
        }

        if (vehicle.getHealth() < 1) {
            this.endGame();
        }
    }

    private void checkCollision(Vehicle vehicle) {

        Rectangle actorBounds = vehicle.getBounds();
        for (int i = 0; i < actors.size(); i++) {
            Actor otherActor = actors.get(i);
            if (null == otherActor || vehicle.equals(otherActor)) {
                continue;
            }
            if (actorBounds.intersects(otherActor.getBounds())) {
                vehicle.collision(otherActor);
                otherActor.collision(vehicle);

                if (otherActor instanceof Moose) {
                    Moose moose = (Moose) otherActor;
                    vehicle.setHealth(vehicle.getHealth() - moose.getDamage());
                } else if (otherActor instanceof Pothole) {
                    Pothole pothole = (Pothole) otherActor;
                    vehicle.setHealth(vehicle.getHealth() - pothole.getDamage());
                } else if (otherActor instanceof Wrench) {
                    Wrench wrench = (Wrench) otherActor;
                    if (vehicle.getHealth() < 4) {
                        vehicle.setHealth(vehicle.getHealth() - wrench.getDamage());
                    }
                }
            }
        }
    }

    public void paintWorld() {

        //get the graphics from the buffer
        Graphics g = strategy.getDrawGraphics();
        //init image to background
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        //load subimage from the background
        g.drawImage(background, 0, 0, Stage.WIDTH, Stage.HEIGHT, 0, backgroundY, Stage.WIDTH, backgroundY + Stage.HEIGHT, this);

        //paint the actors        
        for (int i = 0; i < actors.size(); i++) {
            Actor actor = actors.get(i);
            actor.paint(g);
        }

        vehicle.paint(g);
        paintScore(g);
        paintHealth(g, vehicle.getHealth());

        paintFPS(g);
        //swap buffer
        strategy.show();
    }

    public void paintGameOver() {
        Graphics g = strategy.getDrawGraphics();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        paintScore(g);

        try {
            ScoreHandler.getInstance().writeScore(score);
        } catch (IOException ex) {
            Logger.getLogger(MooseGame.class.getName()).log(Level.SEVERE, null, ex);
        }

        g.drawString("High Score: ", 10, 60);
        g.drawString("" + ScoreHandler.getInstance().readScore(), 190, 60);

        //about 310 pixels wide
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.setColor(Color.RED);
        int xPos = getWidth() / 2 - 155;
        g.drawString("GAME OVER", (xPos < 0 ? 0 : xPos), getHeight() / 2);

        xPos += 30;
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("ENTER: try again", (xPos < 0 ? 0 : xPos), getHeight() / 2 + 50);

        strategy.show();
    }

    public void paintScore(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.black);
        g.drawString("Score: ", 10, 30);
        g.setColor(Color.black);
        g.drawString("" + score, 110, 30);
    }

    public void paintFPS(Graphics g) {
        g.setColor(Color.RED);
        if (usedTime > 0) {
            g.drawString(String.valueOf(1000 / usedTime) + " fps", 0, Stage.HEIGHT - 50);
        } else {
            g.drawString("--- fps", 0, Stage.HEIGHT - 50);
        }
    }

    public void paintHealth(Graphics g, int health) {
        for (int i = 1; i <= health; i++) {
            g.drawImage(ResourceLoader.getInstance().getSprite("heart.png"), ((50 * i) - 45), 40, this);
        }
    }

    public void keyPressed(KeyEvent e) {
        keyPressedHandler.handleInput(e);
    }

    public void keyReleased(KeyEvent e) {
        keyReleasedHandler.handleInput(e);
    }

    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        MooseGame game = new MooseGame();
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(game);
    }
}
