package GameState;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Handlers.Keys;

import TileMap.Background;

public class MenuState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {"Start", "Options", "Quit"};
	private String copyright = "2015 Kelvin Zhang";
	private String version = "Version 1.0";
	BufferedImage logo;
	File f = new File("pointer.gif");
	Image icon;
	
	private Font font;
	

	public MenuState(GameStateManager gsm){
		
		super(gsm);
		
		try{
			
			bg = new Background("/Backgrounds/menubg.gif", 1);
			//bg.setVector(-0.1, 0);
			
			font = new Font("Arial", Font.PLAIN, 12);
			logo = ImageIO.read(new File("Resources/Backgrounds/logo.png"));
			icon = new ImageIcon(f.toURI().toURL()).getImage();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void init() {

	}

	public void update() {
		handleInput();
		bg.update();
	}

	public void draw(Graphics2D g) {
		bg.draw(g);
		
		g.drawImage(logo, 70, 30, null);
		
		g.setFont(font);
		for (int i = 0; i < options.length; i++) {
			if( i == currentChoice){
				g.setColor(Color.LIGHT_GRAY);
			}else{
				g.setColor(Color.WHITE);
			}
			if(currentChoice == 0){
				g.drawImage(icon, 120, 108, null);
			}else if(currentChoice == 1){
				g.drawImage(icon, 120, 123, null);
			}else if(currentChoice == 2){
				g.drawImage(icon, 120, 138, null);
			}
			g.drawString(options[i], 145, 120 + i * 15);
		}
		
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		g.setColor(Color.WHITE);
		g.drawString(copyright, 113, 240-30);
		g.drawString(version, 133, 240-20);
	}

	private void select(){
		if(currentChoice == 0){
			//start
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		
		if(currentChoice == 1){
			// options
		}
		
		if(currentChoice == 2){
			//exit
			System.exit(0);
		}
	}

	public void handleInput() {
		
		if(Keys.isPressed(Keys.ENTER)){
			select();
		}
		if(Keys.isPressed(Keys.UP)){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentChoice--;
			if(currentChoice == -1){
				currentChoice = options.length - 1;
			}
		}
		if(Keys.isPressed(Keys.DOWN)){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentChoice++;
			if(currentChoice == options.length){
				currentChoice = 0;
			}
		}
		
	}

}
