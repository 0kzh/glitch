package GameState;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import GameState.GameStateManager;

import Handlers.Keys;

import Entity.*;
import Entity.Enemies.*;
import Main.GamePanel;
import TileMap.*;
import Audio.JukeBox;

public class Level2State extends GameState{

	private TileMap tileMap;
	private Background bg;
	private Console console;
	private FillScreen fs;
	private DialogBox dbox;
	private DialogBox dbox1;
	private TextPlayer player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<MovingPlatform> platforms;
	//private ArrayList<Explosion> explosions; 
	private DialogBox[] dialog = {new DialogBox("...", 1), new DialogBox("Where am I?", 1), new DialogBox("Welcome to the Matrix.", 2), new DialogBox("... What?", 1), new DialogBox("It is where deleted objects go.", 2)}; 
	private int index;
	private int index2 = 0;
	private boolean keyPressed;
	private boolean pauseKeyPressed;
	
	public Level2State(GameStateManager gsm){
		super(gsm);
		init();
		try {
			gsm.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		//initialize tile map
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/texttileset.png");
		tileMap.loadMap("/Maps/level1-2.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/level1bg.png", 0.1);
		populateEnemies();
		player = new TextPlayer(tileMap);
		player.setSpawnPoint(19, 365);
		player.setPosition(19, 365);
		//player.setSpawnPoint(476, 40);
		//player.setPosition(476, 40);
		//hud = new HUD(player);
		
		
		JukeBox.load("/Music/level1-1.mp3", "level1");
		//JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);
		if(!JukeBox.isPlaying("level1")){
			JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);
		}
		
	}
	
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		platforms = new ArrayList<MovingPlatform>();
		
		Slugger s;
		Point[] points = new Point[] {
			new Point(163, 402),
			new Point(177, 131)
		};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
		MovingPlatform mp;
		Point[] points1 = new Point[] {
				new Point(311, 155)
			};
			for(int i = 0; i < points1.length; i++) {
				mp = new MovingPlatform(tileMap);
				mp.setPosition(points1[i].x, points1[i].y);
				platforms.add(mp);
			}
		
	}

	public void update() {
		// check keys
		
		//printDialogue();
		
		if(console == null){
			handleInput();
			player.update();
			
		}
		
		if(Keys.isPressed(Keys.BUTTON1)){
			if(player.tl == Tile.TERMINAL || player.tr == Tile.TERMINAL || player.bl == Tile.TERMINAL || player.br == Tile.TERMINAL){
				if(console == null){
					console = new Console(2);
				}else{
					if(dbox1 == null){
						
						dbox1 = new DialogBox("Wut lol", 1);
					}
					if(dbox1.isDone()){
						gsm.setState(GameStateManager.LEVEL3STATE);
					}
				}
			}
		}
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		
		
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
		}
		
		for(int i = 0; i < platforms.size(); i++) {
			MovingPlatform mp = platforms.get(i);
			mp.setPosition(311, 155);
			mp.update();
		}
		
		// attack enemies
		player.checkAttack(enemies);
		player.checkPlatformCollision(platforms);
		
		
	}

	
	public void draw(Graphics2D g) {
		
		//draw bg
		bg.draw(g);
		
		//draw tilemap
		tileMap.draw(g);
		
		//draw player
		player.draw(g);
		
		for(int i = 0; i < platforms.size(); i++) {
			platforms.get(i).draw(g);
		}
		
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		if(console != null) console.draw(g);
		//draw hud
		//hud.draw(g);
		try{
			if(!dbox1.shouldRemove() && dbox1 != null){
				dbox1.draw(g);
				if(Keys.isPressed(Keys.BUTTON1) && dbox1.isDone()){
					dbox1.setRemove(true);
				}else if(!Keys.isPressed(Keys.BUTTON1)){
				}
			}
			else if(index < dialog.length){
				index++;
			}
		}catch(Exception e){
		}
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)){
			if(!pauseKeyPressed){
				gsm.setPaused(true);
				pauseKeyPressed = true;
			}
		}else{
			pauseKeyPressed = false;
		}
		if(player.getHealth() == 0) return;
		player.setUp(Keys.keyState[Keys.UP]);
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.BUTTON1]);
	}

	
	
}
