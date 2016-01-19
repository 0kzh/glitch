package Entity;

import TileMap.*;
import java.util.ArrayList;
import Audio.JukeBox;
import GameState.Level0State;
import Main.GamePanel;
import javax.imageio.ImageIO;
import Entity.Particle;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject{

	//player stuff
	private int health;
	private int maxHealth;
	public boolean dead;
	private int spawnX;
	private int spawnY;
	
	public boolean dboxFinish;
	private boolean hasJumped;
	private boolean doubleJump;
	public boolean alreadyDoubleJump;
	private double doubleJumpStart;
	public static boolean teleported;
	private ArrayList<Particle> energyParticles;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
			6, 6, 1, 1, 6, 6
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int DEAD = 4;
	private static final int TELEPORTING = 5;
	
	
	public Player(TileMap tm){
		
		super(tm);
		
		width = 16;
		height = 16;
		cwidth = 8;
		cheight = 14;
		
		moveSpeed = 0.3;
		maxSpeed = 2.0;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4;
		stopJumpSpeed = 0.2;
		doubleJumpStart = -3.8;
		
		facingRight = true;
		
		health = maxHealth = 5;
		
		// load sprites
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.png"));
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 6; i ++) {
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
		energyParticles = new ArrayList<Particle>();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		JukeBox.load("/SFX/jump.mp3", "jump");
		JukeBox.load("/SFX/dead.mp3", "dead");
		
	}
	
	public void init(
			ArrayList<Particle> energyParticles) {
			this.energyParticles = energyParticles;
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	
	public void setSpawnPoint(int i, int j){
		spawnX = i;
		spawnY = j;
		
	}
	
	public void setJumping(boolean b) {
		if(!dboxFinish){
			if(b && !alreadyDoubleJump && hasJumped) {
				doubleJump = true;
			} 
			if(!(health < 0)) jumping = b;
		}
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
		
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;
			hasJumped = true;
			
			JukeBox.play("jump");
		}
		
		if(jumping && falling && !hasJumped){
			dy = jumpStart;
			falling = true;
			hasJumped = true;
			
			JukeBox.play("jump");
		}
		//double jump
		if(doubleJump) {
			dy = doubleJumpStart;
			alreadyDoubleJump = true;
			doubleJump = false;
			JukeBox.play("jump");
		}
		
		if(falling){
			dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}else{
			alreadyDoubleJump = false;
			hasJumped = false;
		}
		
	}
	
	@SuppressWarnings("static-access")
	public void update() {
		
		for(int i = 0; i < energyParticles.size(); i++) {
			energyParticles.get(i).update();
			if(energyParticles.get(i).shouldRemove()) {
				energyParticles.remove(i);
				i--;
			}
		}
		
		if(x > 0 && y > 0 && x < tileMap.getWidth() && y < tileMap.getHeight()){
			if(tileMap.getType((int) y / tileSize, (int) (x) / tileSize) == Tile.DAMAGING){
				health = 0;
			}
		}
		
		if(y > tileMap.getHeight()){
			health = 0;
		}
		
		if(bl == Tile.DAMAGING || br == Tile.DAMAGING || tl == Tile.DAMAGING || tr == Tile.DAMAGING){
			health = 0;	
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
		if(currentAction != TELEPORTING){
			getNextPosition();
			checkTileMapCollision();
			setPosition(xtemp, ytemp);
		}
		
		if(health <= 0){
			if(currentAction != DEAD){
				currentAction = DEAD;
				tileMap.fs = new FillScreen(Color.BLACK);
				for(int i = 0; i < 6; i++) {
					energyParticles.add(
						new Particle(
							tileMap,
							x,
							y + cheight / 4,
							Particle.UP));
				}
				animation.setFrames(sprites.get(DEAD));
				animation.setDelay(50);
				width = 16;
				JukeBox.play("dead");
				
			}
			if(animation.getFrame() >= 5){
				Level0State.playedOnce = true;
				tileMap.setShaking(false, 0);
				respawn();
				if(tileMap.fs != null){
					if(!tileMap.fs.shouldRemove()) tileMap.fs.setRemove(true);
				}
			}else{
				dx = 0;
				dy = 0;
			}
		}else if(x > 0 && y > 0 && x < tileMap.getWidth() && y < tileMap.getHeight() && 
				tileMap.getType((int) (y - cheight / 2) / tileSize, (int) (x - cwidth / 2) / tileSize) == Tile.TERMINAL){
			if(currentAction != TELEPORTING && (tl == Tile.TERMINAL || tr == Tile.TERMINAL || bl == Tile.TERMINAL || br == Tile.TERMINAL)){
				JukeBox.play("next");
				currentAction = TELEPORTING;
				animation.setFrames(sprites.get(TELEPORTING));
				animation.setDelay(200);
				width = 16;
			}
			
			if(animation.hasPlayedOnce()){
				teleported = true;
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
		tileMap.setPosition(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);
		health = 5;
	}

	public void draw(Graphics2D g){
		
		setMapPosition();
		for(int i = 0; i < energyParticles.size(); i++) {
			energyParticles.get(i).draw(g);
		}
		super.draw(g);
		
	}
}
