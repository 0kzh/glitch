package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Main.GamePanel;

public class Console{
	
	private BufferedImage image;
	private Font font;
	private boolean remove;
	private int stage;
	
	public Console(int s){
		
		stage = s;
		remove = false;
		try{
			
			image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Other/terminal.png"));
			font = new Font("Lucida Console", Font.PLAIN, 9);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void setRemove(boolean b){
		remove = b;
	}
	
	public boolean shouldRemove(){
		return remove;
	}
	
	public void draw(Graphics2D g){
		
		g.drawImage(image, (GamePanel.WIDTH - image.getWidth()) / 2, (GamePanel.HEIGHT - image.getHeight()) / 2, null);
		
	}
	
}
