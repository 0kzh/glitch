package Entity.Enemies;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.*;
import TileMap.TileMap;

public class MiniBoss extends Enemy{
	
	private BufferedImage[] sprites;
	private Graphics2D gg;
	private float alpha;
	
	public MiniBoss(TileMap tm){
		
		super(tm);
		
		moveSpeed = 0.8;
		maxSpeed = 0.8;
		
		width = 99;
		height = 240;
		cwidth = 99;
		cheight = 240;
		
		health = maxHealth = 2;
		damage = 1;
		alpha = 1.0f;
		
		// load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/miniboss.png"));
			
			sprites = new BufferedImage[6];
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(140);
		
	}
	
	private void getNextPosition() {
		x += moveSpeed;
	}
	
	public float getOpacity(){ return alpha; }
	public void setOpacity(float a){ alpha = a; }
	
	public void update(){
		
		if(x > 1000){
			//die
			health = 0;
			dead = true;
			animation.setFrame(0);
		}
		
		if(!dead){
			//update position
			getNextPosition();
			setPosition(x, 120);
			
			//update animation
			animation.update();
		}
		
	}
	
	public void draw(Graphics2D g) {
		if(gg == null) gg = (Graphics2D) g.create();
		gg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
//		setMapPosition();
		super.draw(gg);
	}

}
