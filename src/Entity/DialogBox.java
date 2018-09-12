package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import Main.GamePanel;
import Audio.JukeBox;
import Handlers.Keys;

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
	public static String nextKey;
	
	public DialogBox(String s, int character){
		
		toPrint = s.toCharArray();
		remove = false;
		try{
			
			if(character == 0){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/sigh.png"));
			}else if(character == 1){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/neutral.png"));
			}else if(character == 2){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/surprised.png"));
			}else if(character == 3){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/vmad.png"));
			}else if(character == 4){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/defeat.png"));
			}else if(character == 5){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/mercy.png"));
			}else if(character == 6){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/dead.png"));
			}else if(character == 7){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/cavatar6.png"));
			}else if(character == 8){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/silhouette.png"));
			}
			
			font = new Font("Arial", Font.PLAIN, 11);
			nextKey = KeyEvent.getKeyText(Keys.keyZ);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
public DialogBox(String s, int character, boolean bold){
		
		toPrint = s.toCharArray();
		remove = false;
		try{
			
			if(character == 0){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/sigh.png"));
			}else if(character == 1){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/neutral.png"));
			}else if(character == 2){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/surprised.png"));
			}else if(character == 3){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/vmad.png"));
			}else if(character == 4){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/defeat.png"));
			}else if(character == 5){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/mercy.png"));
			}else if(character == 6){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/dead.png"));
			}else if(character == 7){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/cavatar6.png"));
			}else if(character == 8){
				image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/silhouette.png"));
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
		int y = GamePanel.HEIGHT - 35;
		String[] line = myString.split("\n");
		if(line.length == 1){
			 g.drawString(myString, 75, y);
		}else{
			g.drawString(line[0], 75, y - g.getFontMetrics().getHeight() / 2);
			g.drawString(line[1], 75, y + g.getFontMetrics().getHeight() / 2);
		}
		g.setFont(new Font("Arial", Font.BOLD, 9));
		g.drawString("[" + nextKey + "]", GamePanel.WIDTH - 40, GamePanel.HEIGHT - 25);
		if(i >= toPrint.length - 1){
			i = toPrint.length - 1;
			done = true;
		}else{
			i++;
			myString += Character.toString(toPrint[i]);
		}
		
		
		try{
			Thread.sleep(30);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	
}
