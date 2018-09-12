package GameState;

import java.awt.*;
import GameState.GameStateManager;
import Entity.*;
import Main.GamePanel;
import TileMap.*;
import Audio.JukeBox;

public class CreditsState extends GameState{

	private FillScreen fs;
	private Background bg;
	private boolean fade = true;
	private int index = 255;
	
	public CreditsState(GameStateManager gsm){
		super(gsm);
		init();
	}
	
	public void init() {
		fs = new FillScreen(Color.BLACK);
		bg = new Background("/Backgrounds/credits.jpg", 0.1);
		JukeBox.load("/Music/bg.mp3", "menu");
		if(JukeBox.isPlaying("bg")) JukeBox.stop("bg");
		JukeBox.load("/Music/menu.mp3", "menu");
		JukeBox.load("/SFX/press.mp3", "press");
		JukeBox.loop("menu", 600, JukeBox.getFrames("menu") - 2200);
	}
	public void update() {
		
		bg.update();
		if(bg.y < -670){
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}
		

	
	public void draw(Graphics2D g) {
		
		fs.draw(g);
		bg.draw(g);
		if(fade){
			g.setColor(new Color(0, 0, 0, index));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			if(index > 0) index -= 5;
			else{
				bg.setVector(0.0, -0.4);
			}
		}
	}
	
	public void handleInput(){
		
	}
	
}
