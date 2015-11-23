package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import TileMap.TileMap;
import javax.imageio.ImageIO;

public class Bullet extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] iSprites;
	private BufferedImage[] hitSprites;

	public Bullet(TileMap tm, boolean right, boolean up, boolean down){
		
		super(tm);
		
		facingRight = right;
		facingUp = up;
		facingDown = down;
		
		moveSpeed = 6;
		
		if(up){ 
			dy = -moveSpeed;
		}else if(down) {
			dy = moveSpeed;
		}else{
			if(right){
				dx = moveSpeed;
			}else{
				dx = -moveSpeed;
			}
		}

		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		
		//load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/bullet.png"));
			
			//num of sprite frames
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
			iSprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++){
				iSprites[i] = spritesheet.getSubimage(i * width, height, width, height);
			}
			
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++){
				hitSprites[i] = spritesheet.getSubimage(i * width, height * 2, width, height);
			}
			
			animation = new Animation();
			if(!facingUp && !facingDown){
				animation.setFrames(sprites);
			}else{
				animation.setFrames(iSprites);
			}
			animation.setDelay(100);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setHit(){
		
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
		dy = 0;
	
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update(){
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(!hit){
			if((!facingUp && !facingDown)){
				if(dx == 0){
					setHit();
				}	
			}else{
				if(dy == 0){
					setHit();
				}
			}
		}
		
		
		
		if(!animation.hasPlayedOnce()){
			animation.update();
		}else{
			animation.setFrame(3);
		}
		
		if(hit && animation.hasPlayedOnce()){
			remove = true;
		}
		
	}
	
	public void draw(Graphics2D g){
		
		setMapPosition();
		super.draw(g);
		
		
	}
	
}
