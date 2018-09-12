package GameState;

import java.awt.Graphics2D;

import GameState.GameStateManager;

public abstract class GameState {

	protected static GameStateManager gsm;
	
	@SuppressWarnings("static-access")
	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void handleInput();
	
}
