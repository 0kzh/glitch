package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.*;
import TileMap.TileMap;

public class Bat extends Enemy{
	
	private BufferedImage[] sprites;
	
	public Bat(TileMap tm){
		
		super(tm);
		
		moveSpeed = 1.1;
		maxSpeed = 1.1;
		
		width = 16;
		height = 16;
		cwidth = 13;
		cheight = 13;
		
		health = maxHealth = 2;
		damage = 1;
		
		// load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/bat.png"));
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(140);
		
		right = true;
		facingRight = true;
		
	}
	
	private void getNextPosition() {
		
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
		}
	}
	
	public void update(){
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//if it hits wall, turn
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		
		else if(left && dx == 0){
			right = true;
			left = false;
			facingRight = true;
		}
		
		
		//update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
		
	}

}
