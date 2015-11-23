package Entity;

import TileMap.*;

import java.util.ArrayList;
import Audio.JukeBox;
import Handlers.Keys;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject{

	//player stuff
	private int health;
	private int maxHealth;
	private int ammo;
	private int maxAmmo;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	private boolean wallJump;
	public boolean alreadyWallJump;
	private double wallJumpStart;
	
	//attacks
	private boolean firing;
	private int ammoCost;
	private int bulletDamage;
	private long fireDelay;
	private long timeElapsed;
	
	private ArrayList<Bullet> bullets;
	
	// gliding
	private boolean gliding;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
			2, 7, 1, 1, 1, 1, 1
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int SHOOTING = 4;
	private static final int SHOOTING_UP = 5;
	private static final int SHOOTING_DOWN = 6;
	
	
	public Player(TileMap tm){
		
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.3;
		maxSpeed = 2.0;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.2;
		wallJumpStart = -4.7;
		
		facingRight = true;
		facingUp = false;
		facingDown = false;
		
		health = maxHealth = 5;
		ammo = maxAmmo = 100;
		
		ammoCost = 1;
		bulletDamage = 5;
		bullets = new ArrayList<Bullet>();
		
		// load sprites
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.png"));
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i ++) {
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
		
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getAmmo() { return ammo;}
	public int getMaxAmmo() { return maxAmmo; }
	
	public void setJumping(boolean b) {
		if(b && !jumping && falling && !alreadyWallJump && ((bottomLeft && topLeft) || (bottomRight && topRight))) {
			wallJump = true;
		}
		jumping = b;
		
		
	}
	
	public void setFiring() {
		firing = true;
	}
	
	public void setGliding(boolean b){
		gliding = b;
	}
	
	public void checkAttack(ArrayList<Enemy> enemies){
		
		//loop through enemies
		for(int i = 0; i < enemies.size(); i++){
			Enemy e = enemies.get(i);
			
			/*
			if(scratching){
				if(facingRight){
					if(e.getx() > x && 
							   e.getx() < x + meleeRange &&
							   e.gety() > y - height /2 &&
							   e.gety() < y + height / 2){
								
								e.hit(meleeDamage);
					}
				}else{
					if(e.getx() < x &&
							   e.getx() > x - meleeRange &&
							   e.gety() > y - height / 2 &&
							   e.gety() < y + height / 2){
								e.hit(meleeDamage);
							}
				}
			}
			*/
			
			//bullets
			for(int j = 0; j < bullets.size(); j++){
				if(bullets.get(j).intersects(e)){
					e.hit(bulletDamage);
					
					bullets.get(j).setHit();
					break;
				}
			}
			
			//check enemy collision
			if(intersects(e)){
				hit(e.getDamage());
			}
		}
		
		
	}
	
	public void hit(int damage){
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public boolean isShooting(){
		if(currentAction == SHOOTING_UP || currentAction == SHOOTING_DOWN || currentAction == SHOOTING) return true;
		return false;
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
		
		
		//cannot attack while moving
		if((isShooting()) && !(jumping || falling)){
			dx = 0;
		}
		
		//jumping
		if(jumping && !falling){
			JukeBox.play("jump");
			dy = jumpStart;
			falling = true;
		}
		
		// wall jumping
		if(wallJump) {
			dy = wallJumpStart;
			if(facingRight){
				dx = -3;
			}else{
				dx = 3;
			}
			alreadyWallJump = true;
			wallJump = false;
			JukeBox.play("jump");
		}
		
		if(falling){
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		
		if(!falling) {alreadyWallJump = false;}
		
		
		
	}
	
	public void update() {
		timeElapsed = (System.nanoTime() - fireDelay) / 1000000;
		
		if(x >= tileMap.getWidth()){
			
		}
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(isShooting()){
			if(animation.hasPlayedOnce()) firing = false;
		}
		
		//shoot bullet
		if(ammo > maxAmmo) ammo = maxAmmo;
		
		//update bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update();
			if(bullets.get(i).shouldRemove()){
				bullets.remove(i);
				i--;
			}
		}
		
		//check if done flinching
		if(flinching){
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000){
				flinching = false;
			}
		}
		
		if(firing){
			if(!isShooting()){
				if(Keys.isPressed(Keys.UP)){
					currentAction = SHOOTING_UP;
					animation.setFrames(sprites.get(SHOOTING_UP));
					facingUp = true;
					facingDown = false;
				}else if(Keys.isPressed(Keys.DOWN)){
					currentAction = SHOOTING_DOWN;
					animation.setFrames(sprites.get(SHOOTING_DOWN));
					facingDown = true;
					facingUp = false;
				}else{
					currentAction = SHOOTING;
					animation.setFrames(sprites.get(SHOOTING));
					facingDown = false;
					facingUp = false;
				}
				
				animation.setDelay(100);
				width = 30;
				
				if(ammo > ammoCost){			
					
					fireDelay = System.nanoTime();
					//ammo -= ammoCost;
					Bullet b = new Bullet(tileMap, facingRight, facingUp, facingDown);
					b.setPosition(x, y);
					bullets.add(b);
				}
			}
		}else if(dy > 0){
			if(currentAction != FALLING){
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}else if(dy < 0){
			if(currentAction != JUMPING){
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}else if(left || right){
			if(currentAction != WALKING){
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(120);
				width = 30;
			}
		}else{
			if(currentAction != IDLE){
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		animation.update();
		
		//set direction
		if(!isShooting()){
			if(right) facingRight = true;
			if(left) facingRight = false;
			
		}
	}
	
	public void draw(Graphics2D g){
		
		setMapPosition();
		
		//draw bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw(g);
		}
		
		//draw player
		if(flinching){
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) return;
		}
		
		super.draw(g);
	}
}
