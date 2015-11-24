package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Main.GamePanel;

public class DialogBox{
	
	private BufferedImage image;
	private Font font;
	private char[] toPrint;
	private String myString = "";
	private int i = -1;
	public boolean finished = false;
	
	public DialogBox(String s){
		
		toPrint = s.toCharArray();
		try{
			
			image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/avatar.png"));
			font = new Font("Arial", Font.PLAIN, 11);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public synchronized void setFinished(){
		finished = true;
		notifyAll();
	}
	
	public void draw(Graphics2D g){
		
		g.setColor(Color.BLACK);
		g.fillRect(19, GamePanel.HEIGHT - 59, GamePanel.WIDTH - 39, 41);
		g.setColor(new Color(52, 73, 94));
		g.fillRect(20, GamePanel.HEIGHT - 60, GamePanel.WIDTH - 40, 41);
		g.drawImage(image, 20, GamePanel.HEIGHT - 65, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(myString, 75, GamePanel.HEIGHT - 35);
		if(i >= toPrint.length - 1){
			i = toPrint.length - 1;
			setFinished();
		}else{
			i++;
			myString += Character.toString(toPrint[i]);
		}
		
		
		try {
			Thread.sleep(70);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//g.drawString(player.getAmmo() + "/" + player.getMaxAmmo(), 30, 45);
		
	}
	
}
