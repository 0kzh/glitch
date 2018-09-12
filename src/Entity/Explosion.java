package Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Explosion extends MapObject {
	
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	private Point[] points;
	private int speed;
	private double diagSpeed;
	
	public Explosion(TileMap tm, int x, int y){
		
		super(tm);
		
		this.x = x;
		this.y = y;
		
		width = 45;
		height = 45;
		
		speed = 2;
		diagSpeed = 1.41;
		
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/explosion.png"));
			sprites = new BufferedImage[5];
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		
		animation.setFrames(sprites);
		animation.setDelay(12);
		
		points = new Point[8];
		for(int i = 0; i < points.length; i++){
			points[i] = new Point(x, y);
		}
		
	}
	
	public void update(){
		animation.update();
		if(animation.hasPlayedOnce()){
			remove = true;
		}
		points[0].x += speed;
		points[1].x += diagSpeed;
		points[1].y += diagSpeed;
		points[2].y += speed;
		points[3].x -= diagSpeed;
		points[3].y += diagSpeed;
		points[4].x -= speed;
		points[5].x -= diagSpeed;
		points[5].y -= diagSpeed;
		points[6].y -= speed;
		points[7].x += diagSpeed;
		points[7].y -= diagSpeed;
		
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void draw(Graphics2D g){
		setMapPosition();
		for(int i = 0; i < points.length; i++){
			g.drawImage(
				animation.getImage(),
				(int) (points[i].x + xmap - width / 2),
				(int) (points[i].y + ymap - height / 2),
				null
			);
		}
	}
	
}