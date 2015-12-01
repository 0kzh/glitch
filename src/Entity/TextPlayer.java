package Entity;

import TileMap.*;

import java.util.ArrayList;
import Audio.JukeBox;
import Handlers.Keys;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TextPlayer extends MapObject{

	//player stuff
	private int health;
	private int maxHealth;
	private boolean dead;
	private int spawnX;
	private int spawnY;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
			6, 6, 1, 1, 6
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int DEAD = 4;
	
	
	public TextPlayer(TileMap tm){
		
		super(tm);
		
		width = 16;
		height = 16;
		cwidth = 12;
		cheight = 14;
		
		moveSpeed = 0.3;
		maxSpeed = 2.0;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.6;
		stopJumpSpeed = 0.2;
		
		facingRight = true;
		
		health = maxHealth = 5;
		
		// load sprites
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.png"));
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 5; i ++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++){
					
					bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					
					}
				
				sprites.add(bi);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		JukeBox.load("/SFX/jump.mp3", "jump");
		JukeBox.load("/SFX/dead.mp3", "dead");
		
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	
	public void setSpawnPoint(int i, int j){
		spawnX = i;
		spawnY = j;
		
	}
	
	public void setJumping(boolean b) {
		jumping = b;
	}
	
	public void checkAttack(ArrayList<Enemy> enemies){
		for(int i = 0; i < enemies.size(); i++){
			Enemy e = enemies.get(i);
			if(intersects(e)){
				health = 0;	
			}
		}
		
		
	}
	
	private void getNextPosition() {
		
		//movement
		if(left){
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx = -maxSpeed;
			}
		}else if(right){
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}else{
			if(dx > 0){
				dx -= stopSpeed;
				if(dx < 0){
					dx = 0;
				}
			}else if(dx < 0){
				dx += stopSpeed;
				if(dx > 0){
					dx = 0;
				}
			}
		}
		
		//jumping
		if(jumping && !falling){
			JukeBox.play("jump");
			dy = jumpStart;
			falling = true;
		}
		
		
		if(falling){
			dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		
	}
	
	public void update() {
		
		if(bl == Tile.DAMAGING || br == Tile.DAMAGING){
			health = 0;	
		}
		
		if(tl == Tile.TERMINAL || tr == Tile.TERMINAL){
			if(Keys.isPressed(Keys.BUTTON1)){
				//if z is pressed, show console
				
				
			}
		}
		
		if(x >= tileMap.getWidth() - tileSize/2){
			x = tileMap.getWidth()- tileSize/2;
			topRight = true;
		}
		
		if(x <= tileSize / 2){
			x = tileSize / 2;
			bottomLeft = true;
		}
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(health <= 0){
			if(currentAction != DEAD){
				currentAction = DEAD;
				animation.setFrames(sprites.get(DEAD));
				animation.setDelay(100);
				width = 16;
				JukeBox.play("dead");
			}
			if(animation.hasPlayedOnce()){
				respawn();
			}else{
				dx = 0;
				dy = 0;
			}
		}else if(dy > 0){
			if(currentAction != FALLING){
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 16;
			}
		}else if(dy < 0){
			if(currentAction != JUMPING){
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 16;
			}
		}else if(left || right){
			if(currentAction != WALKING){
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(120);
				width = 16;
			}
		}else{
			if(currentAction != IDLE){
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 16;
			}
		}
		
		animation.update();
		
		if(right) facingRight = true;
		if(left) facingRight = false;
	}
	
	private void respawn() {
		setPosition(spawnX, spawnY);
		health = 5;
	}

	public void draw(Graphics2D g){
		
		setMapPosition();
		super.draw(g);
	}
}
