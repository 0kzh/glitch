package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.Tile;
import TileMap.TileMap;

public class MovingPlatform extends Platform{
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
				sprites[i] = spritesheet.getSubimage(0, 0, 64, 16);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		
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
		
		//set blocks to blocked tiles?
		if(tileMap.isLoaded()){
			for(int i = 0; i < 4; i++){
				//System.out.println((int) (xtemp - cwidth / 2) / tileSize + 1);
				//System.out.println(tileMap.getType((int) (xtemp - cwidth / 2) / tileSize + 1, (int) (ytemp - cheight / 2) / tileSize));
				int row = (int) (ytemp - cheight / 2) / tileSize;
				int col = (int) (xtemp - cwidth / 2) / tileSize + i;
				tileMap.setType(row, col, 9);
				if(tileMap.getType(row, col - 4) != Tile.BLOCKED) tileMap.setType(row, col - 4, 0);
				if(tileMap.getType(row, col + 1) != Tile.BLOCKED) tileMap.setType(row, col + 1, 0);
			}
		}
		
		//if it hits wall, turn
		if(right && dx == 0.0) {
			right = false;
			left = true;
		}
		
		else if(left && dx == 0.0){
			right = true;
			left = false;
		}
		
		getNextPosition();
		setPosition(xtemp, ytemp);
		
		//update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		checkTileMapCollision();
		super.draw(g);
		
	}
	
}
