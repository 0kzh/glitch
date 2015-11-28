package GameState;

import java.awt.*;
import java.util.ArrayList;

import Handlers.Keys;

import Entity.*;
import Entity.Enemies.*;
import Main.GamePanel;
import TileMap.*;
import Audio.JukeBox;

public class Level1State extends GameState{

	private TileMap tileMap;
	private Background bg;
	private HUD hud;
	private FillScreen fs;
	private DialogBox dbox;
	private TextPlayer player;
	
	//private ArrayList<Enemy> enemies;
	//private ArrayList<Explosion> explosions; 
	private DialogBox[] dialog = {new DialogBox("Where... Where am I?"), new DialogBox("..."), new DialogBox("...")}; 
	private int index;
	private int index2 = 0;
	private boolean keyPressed;
	
	public Level1State(GameStateManager gsm){
		super(gsm);
		init();
	}
	
	public void init() {
		//initialize tile map
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/texttileset.png");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/level1bg.png", 0.1);
		
		player = new TextPlayer(tileMap);
		player.setSpawnPoint(51, 380);
		player.setPosition(51, 380);
		//hud = new HUD(player);
		
		
		JukeBox.load("/Music/level1-1.mp3", "level1");
		JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);
		
	}
	
	private void printDialogue() {
		if(index < dialog.length){
			fs = new FillScreen(Color.BLACK);
			dbox = dialog[index];
			JukeBox.stop("level1");
		}else{
			fs.setRemove(true);
		}
		
	}

	public void update() {
		// check keys
		
		printDialogue();
		handleInput();
		
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		
		
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// attack enemies
		//player.checkAttack(enemies);
		
		
		
	}

	
	public void draw(Graphics2D g) {
		
		//draw bg
		bg.draw(g);
		
		//draw tilemap
		tileMap.draw(g);
		
		//draw player
		player.draw(g);
		
		
		//draw hud
		//hud.draw(g);
		try{
			if(!fs.shouldRemove()){
				fs.draw(g);
			}else{
				if(index2 < 30){
					g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2 - (index2 * GamePanel.HEIGHT / 60));
					g.fillRect(0, GamePanel.HEIGHT / 2 + (index2 * GamePanel.HEIGHT / 60), GamePanel.WIDTH, GamePanel.HEIGHT / 2);
					Thread.sleep(10);
					index2++;
				}else{
					JukeBox.resume("level1", true);
				}
			}
			
			if(!dbox.shouldRemove()){
				dbox.draw(g);
				if(Keys.isPressed(Keys.BUTTON1) && !keyPressed && dbox.isDone()){
					dbox.setRemove(true);
					keyPressed = true;
				}else if(!Keys.isPressed(Keys.BUTTON1)){
					keyPressed = false;
				}
			}
			else if(index < dialog.length){
				index++;
			}
		}catch(Exception e){
		}
	}
	
	public void handleInput() {
		//if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(true);
		if(player.getHealth() == 0) return;
		player.setUp(Keys.keyState[Keys.UP]);
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.BUTTON1]);
	}

	
	
}
