package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.*;
import TileMap.TileMap;

public class VertBat extends Enemy{
	
	private BufferedImage[] sprites;
	
	public VertBat(TileMap tm){
		
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
		
		up = true;
		
	}
	
	private void getNextPosition() {
		
		if(up){
			dy -= moveSpeed;
			if(dy < -maxSpeed){
				dy = -maxSpeed;
			}
		}else if(down){
			dy += moveSpeed * 2;
			if(dy > maxSpeed * 2){
				dy = maxSpeed * 2;
			}
		}
	}
	
	public void update(){
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//if it hits wall, turn
		if(up && dy == 0) {
			up = false;
			down = true;
		}
		
		else if(down && dx == 0){
			up = true;
			down = false;
		}
		
		
		//update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
		
	}

}
