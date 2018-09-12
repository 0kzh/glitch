package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Particle extends MapObject {
	
	private int count;
	private boolean remove;
	
	private BufferedImage[] sprites;
	
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	private float opacity;
	private Random random = new Random();
	
	public Particle(TileMap tm, double x, double y, int dir){
		super(tm);
		this.x = x;
		this.y = y;
		double d1 = Math.random() * 2.5 - 1;
		double d2 = -Math.random() - 0.8; 
		opacity = (float) (random.nextFloat() * (1 - 0.6) + 0.6);
		
		if(dir == UP){
			dx = d1;
			dy = d2;
		}
		else if(dir == LEFT){
			dx = d2;
			dy = d1;
		}
		else if(dir == DOWN){
			dx = d1;
			dy = -d2;
		}
		else {
			dx = -d2;
			dy = d1;
		}
		
		count = 0;
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/particle.png"));
			
			sprites = new BufferedImage[1];
			sprites[0] = spritesheet.getSubimage(0, 0, 2, 2);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	public void update(){
		x += dx;
		y += dy;
		count++;
		if(count == 20) remove = true;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void draw(Graphics2D g){
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		super.draw(g);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
	
}
