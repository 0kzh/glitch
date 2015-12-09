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
		g.setFont(font);
		g.setColor(Color.WHITE);
		if(stage == 1){
			g.drawString("view patchnotes", 182, 77);
			g.drawString("Version 1.0:", 35, 88);
			g.drawString("Hello everyone! I am the game developer.", 35, 99);
			g.drawString("As you may know, this is the official release of the game.", 35, 110);
			g.drawString("Hopefully, all bugs have been fixed.", 35, 121);
		}else if(stage == 2){
			g.drawString("view patchnotes", 182, 77);
			g.drawString("Version 1.1:", 35, 88);
			g.drawString("- Added background music", 35, 99);
			g.drawString("- Fixed some bugs", 35, 110);
			g.drawString("- Removed herobrine", 35, 121);
		}
		//g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
		//g.drawString(player.getAmmo() + "/" + player.getMaxAmmo(), 30, 45);
		
	}
	
}
