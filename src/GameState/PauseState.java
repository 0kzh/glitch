package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Audio.JukeBox;
import Handlers.Keys;
import Main.GamePanel;

public class PauseState extends GameState {
	
	private Font font;
	public boolean pauseButton;
	
	public PauseState(GameStateManager gsm) {
		
		super(gsm);
		
		// fonts
		font = new Font("Arial", Font.BOLD, 14);
		
	}
	
	public void init() {}
	
	public void update() {
		handleInput();
		
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Game Paused", 110, 110);
		g.setFont(new Font("Arial", Font.PLAIN, 11));
		g.drawString("[Esc] to resume; [Z] to quit", 95, 145);
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)){
			if(!pauseButton){
				gsm.setPaused(false);
				pauseButton = true;
			}
		}else{
			pauseButton = false;
		}
		if(Keys.isPressed(Keys.BUTTON1)) {
			System.exit(-1);
		}
	}

}
