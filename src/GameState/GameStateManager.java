package GameState;

import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import Audio.JukeBox;

public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES = 5;
	public static final int MENUSTATE = 0;
	public static final int OPTIONSSTATE = 1;
	public static final int LEVEL1STATE = 2;
	public static final int LEVEL2STATE = 3;
	public static final int LEVEL3STATE = 4;
	
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];
		JukeBox.init();
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE){
			gameStates[state] = new MenuState(this);
		}
		
		if(state == OPTIONSSTATE){
			gameStates[state] = new OptionsState(this);
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
	
	public void update(){
		if(gameStates[currentState] != null){
			gameStates[currentState].update();
		}
	}
	
	public void draw(Graphics2D g){
		if(gameStates[currentState] != null){
			gameStates[currentState].draw(g);
		}
	}
	
}
