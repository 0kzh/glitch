package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class MovingPlatform extends MapObject{
	private BufferedImage[] sprites;
	
	public MovingPlatform(TileMap tm){
		
		super(tm);
		
		moveSpeed = 0.7;
		maxSpeed = 0.7;
		
		width = 64;
		height = 16;
		cwidth = 64;
		cheight = 16;
		
		// load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Other/platform.png"));
			
			sprites = new BufferedImage[1];
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
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
