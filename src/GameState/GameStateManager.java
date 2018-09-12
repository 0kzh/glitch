package GameState;

import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import Main.GamePanel;

import GameState.PauseState;
import Handlers.Keys;
import Audio.JukeBox;

public class GameStateManager {

	private GameState[] gameStates;
	public static int currentState;
	
	private PauseState pauseState;
	public boolean paused;
	
	public static final int NUMGAMESTATES = 12;
	public static final int MENUSTATE = 0;
	public static final int OPTIONSSTATE = 1;
	public static final int INTROSTATE = 2;
	public static final int LEVEL1STATE = 3;
	public static final int LEVEL2STATE = 4;
	public static final int LEVEL3STATE = 5;
	public static final int LEVEL4STATE = 6;
	public static final int LEVEL5STATE = 7;
	public static final int LEVEL6STATE = 8;
	public static final int LEVEL7STATE = 9;
	public static final int LEVEL8STATE = 10;
	public static final int CREDITSSTATE = 11;
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];
		JukeBox.init();
		JukeBox.load("/SFX/pause.mp3", "pause");
		currentState = MENUSTATE;
		
		pauseState = new PauseState(this);
		paused = false;
		
		loadState(currentState);
		
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE){
			gameStates[state] = new MenuState(this);
		}
		
		if(state == OPTIONSSTATE){
			gameStates[state] = new OptionsState(this);
		}
		
		if(state == INTROSTATE){
			gameStates[state] = new Level0State(this);
		}
		
		if(state == LEVEL1STATE){
			gameStates[state] = new Level1State(this);
		}
		
		if(state == LEVEL2STATE){
			gameStates[state] = new Level2State(this);
		}
		
		if(state == LEVEL3STATE){
			gameStates[state] = new Level3State(this);
		}
		
		if(state == LEVEL4STATE){
			gameStates[state] = new Level4State(this);
		}
		
		if(state == LEVEL5STATE){
			gameStates[state] = new Level5State(this);
		}
		
		if(state == LEVEL6STATE){
			gameStates[state] = new Level6State(this);
		}
		
		if(state == LEVEL7STATE){
			gameStates[state] = new Level7State(this);
		}
		
		if(state == LEVEL8STATE){
			gameStates[state] = new Level8State(this);
		}
		
		if(state == CREDITSSTATE){
			gameStates[state] = new CreditsState(this);
		}
		
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state){
		
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		//gameStates[currentState].init();
		
	}
	
	public void save() throws IOException{
		Writer wr = new BufferedWriter(new FileWriter("save.txt"));
		wr.append("" + currentState);
		wr.close();
	}
	
	public void setPaused(boolean b) { 
		paused = b; 
		pauseState.pauseButton = true; 
		JukeBox.play("pause");
		
		if(currentState == LEVEL1STATE){
			if(b){
				JukeBox.stop("level1");
			}else{
				JukeBox.resume("level1", true);
			}
		}else if(currentState == LEVEL7STATE){
			if(b){
				JukeBox.stop("boss1");
			}else{
				JukeBox.resume("boss1", true);
			}
		}else{
			if(b){
				JukeBox.stop("bg");
			}else{
				JukeBox.resume("bg", true);
			}
		}
	}
	
	public void update() {
		if(paused) {
			pauseState.update();
			return;
		}
		if(gameStates[currentState] != null) gameStates[currentState].update();
	}
	
	public void draw(Graphics2D g) {
		if(paused) {
			pauseState.draw(g);
			return;
		}
		if(gameStates[currentState] != null) gameStates[currentState].draw(g);
		else {
			g.setColor(java.awt.Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	}
	
}
