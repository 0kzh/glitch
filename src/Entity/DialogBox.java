package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import Main.GamePanel;
import Audio.JukeBox;

public class DialogBox{
	
	private BufferedImage image;
	private Font font;
	private char[] toPrint;
	private String myString = "";
	private int i = -1;
	public boolean remove;
	private boolean done = false;
	
	
	public static final int PLAYER = 0;
	public static final int TEXT_PLAYER = 1;
	public static final int CONSOLE = 2;
	
	public DialogBox(String s, int character){
		
		toPrint = s.toCharArray();
		remove = false;
		try{
			
			if(character == 0){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/avatar.png"));
			}else if(character == 1){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/textavatar.png"));
			}else if(character == 2){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/cavatar.png"));
			}else if(character == 3){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/cavatar2.png"));
			}else if(character == 4){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/cavatar3.png"));
			}
			
			font = new Font("Arial", Font.PLAIN, 11);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
public DialogBox(String s, int character, boolean bold){
		
		toPrint = s.toCharArray();
		remove = false;
		try{
			
			if(character == 0){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/avatar.png"));
			}else if(character == 1){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/textavatar.png"));
			}else if(character == 2){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/cavatar.png"));
			}else if(character == 3){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/cavatar2.png"));
			}else if(character == 4){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/cavatar3.png"));
			}
			
			if(bold) font = new Font("Arial", Font.BOLD, 11);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public boolean shouldRemove() { return remove; }
	
	public boolean isDone() { return done; }
	
	public void setRemove(boolean b) { JukeBox.play("press"); remove = b; }
	
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
			done = true;
		}else{
			i++;
			myString += Character.toString(toPrint[i]);
		}
		
		
		try {
			Thread.sleep(30);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
