package GameState;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import Audio.JukeBox;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Handlers.Keys;

import TileMap.Background;

public class OptionsState extends GameState {
	
	private Background bg;
	
	private static int currentChoice = 0;
	private String[] keys = {"Jump: ", "Left: ", "Right: ", "<- Back"};
	private boolean buttonPressed = false;
	private static boolean enterPressed = false;
	
	public static boolean reading;
	public static int keyPress;
	
	BufferedImage logo;
	File f = new File("pointer.png");
	Image icon;
	
	private Font font;
	

	public OptionsState(GameStateManager gsm){
		
		super(gsm);
		currentChoice = 0;
		try{
			Thread.sleep(500);
		}catch(InterruptedException e1){
			e1.printStackTrace();
		}
		
		try{
			
			bg = new Background("/Backgrounds/menubg.gif", 1);
			//bg.setVector(-0.1, 0);
			
			font = new Font("Arial", Font.PLAIN, 12);
			logo = ImageIO.read(new File("Resources/Backgrounds/logo.png"));
			icon = new ImageIcon(f.toURI().toURL()).getImage();
			JukeBox.load("/SFX/option.mp3", "option");
			JukeBox.load("/SFX/select.wav", "select");
			JukeBox.load("/Music/menu.mp3", "menu");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private static boolean checkConflicting(int key){
		if(key == Keys.keyZ || key == Keys.keyUp || key == Keys.keyDown ||key == Keys.keyLeft || key == Keys.keyRight){
			return true;
		}
		return false;
	}
	
	public void init(){
		
	}

	public void update(){
		handleInput();
		bg.update();
	}

	public void draw(Graphics2D g){
		bg.draw(g);
		
		g.drawImage(logo, 70, 30, null);
		
		g.setFont(font);
		for (int i = 0; i < keys.length; i++){
			if( i == currentChoice){
				g.setColor(Color.GREEN);
			}else{
				g.setColor(Color.WHITE);
			}
			if(currentChoice == 0){
				g.drawImage(icon, 110, 108, null);
			}else if(currentChoice == 1){
				g.drawImage(icon, 110, 123, null);
			}else if(currentChoice == 2){
				g.drawImage(icon, 110, 138, null);
			}
			if(i != 3) g.drawString(keys[i], 135, 120 + i * 15);
			if(i == 0){
				if(currentChoice == 0 && reading){
					g.drawString("Press a key...", 180, 120 + i * 15);
				}else{
					g.drawString(KeyEvent.getKeyText(Keys.keyZ), 180, 120 + i * 15);
				}
			}else if(i == 1){
				if(currentChoice == 2 && reading){
					g.drawString("Press a key...", 180, 120 + i * 15);
				}else{
					g.drawString(KeyEvent.getKeyText(Keys.keyLeft), 180, 120 + i * 15);
				}
			}else if(i == 2){
				if(currentChoice == 3 && reading){
					g.drawString("Press a key...", 180, 120 + i * 15);
				}else{
					g.drawString(KeyEvent.getKeyText(Keys.keyRight), 180, 120 + i * 15);
				}
			}else if(i == 3){
				g.drawString(keys[i], 135, 220);
			}
		}
	}

	public static void select(){
		if(currentChoice == 0){
			//key z
			if(!reading && keyPress == 0){
				reading = true;
			}
			
			if(keyPress != 0){
				if(keyPress != KeyEvent.VK_ENTER){
					if(!checkConflicting(keyPress)) Keys.keyZ = keyPress;
				}
				keyPress = 0;
				reading = false;
			}
			
		}
		else if(currentChoice == 1){
			// left
			if(!reading && keyPress == 0){
				reading = true;
			}
			
			if(keyPress != 0){
				if(keyPress != KeyEvent.VK_ENTER){
					if(!checkConflicting(keyPress)) Keys.keyLeft = keyPress;
				}
				keyPress = 0;
				reading = false;
			}
			
		}
		
		else if(currentChoice == 2){
			// right
			if(!reading && keyPress == 0){
				reading = true;
			}
			
			if(keyPress != 0){
				if(keyPress != KeyEvent.VK_ENTER){
					if(!checkConflicting(keyPress)) Keys.keyRight = keyPress;
				}
				keyPress = 0;
				reading = false;
			}
		}
		
		else if (currentChoice == 3){
			// back
			gsm.setState(GameStateManager.MENUSTATE);
			for(int i = 0; i < Keys.keyState.length; i++){
				Keys.keyState[i] = false;
			}
		}
	}

	public void handleInput() {
		
		if(Keys.isPressed(Keys.ENTER)){
			if(!enterPressed){
				JukeBox.play("select");
				select();
				enterPressed = true;
			}
		}else{
			enterPressed = false;
		}
		
		if(Keys.isPressed(Keys.UP)){
			
			if(!buttonPressed){
				currentChoice--;
				if(currentChoice == -1){
					currentChoice = keys.length - 1;
				}
				buttonPressed = true;
				JukeBox.play("option");
			}
		}
		else if(Keys.isPressed(Keys.DOWN)){
			
			if(!buttonPressed){
				currentChoice++;
				if(currentChoice == keys.length){
					currentChoice = 0;
				}
				buttonPressed = true;
				JukeBox.play("option");
			}
		}else{
			buttonPressed = false;
		}
		
	}

}
