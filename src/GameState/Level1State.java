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

public class Level1State extends GameState{

	private TileMap tileMap;
	private Background bg;
	private Console console;
	private FillScreen fs;
	private DialogBox dbox;
	private DialogBox dbox1;
	private TextPlayer player;
	
	
	private ArrayList<Enemy> enemies;
	//private ArrayList<Explosion> explosions; 
	private DialogBox[] dialog = {new DialogBox("...", 1), new DialogBox("Where am I?", 1), new DialogBox("GREETINGS. I SEE YOU HAVE AWOKEN.", 2), new DialogBox("THIS IS THE MATRIX, WHERE NO ONE LEAVES", 2), new DialogBox("GOOD LUCK ESCAPING, NOONE EVER HAS!", 2), new DialogBox("hahaha!!", 2), new DialogBox("HAHahA!!", 2), new DialogBox("HAHAHA!!11!!1", 2)}; 
	private int index;
	private int index2 = 0;
	private boolean keyPressed;
	private boolean talking;
	
	public Level1State(GameStateManager gsm){
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
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/level1bg.png", 0.1);
		populateEnemies();
		player = new TextPlayer(tileMap);
		//player.setSpawnPoint(489, 55);
		//player.setPosition(489, 55);
		player.setSpawnPoint(32, 374);
		player.setPosition(32, 374);
		//hud = new HUD(player);
		
		
		JukeBox.load("/Music/level1-1.mp3", "level1");
		JukeBox.load("/SFX/press.mp3", "press");
		JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);
		
	}
	
private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Point[] points = new Point[] {
			new Point(315, 116),
			new Point(421, 116)
		};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
	}
	
	private void printDialogue() {
		if(index < dialog.length){
			fs = new FillScreen(Color.BLACK);
			dbox = dialog[index];
			JukeBox.stop("level1");
			talking = true;
		}else{
			fs.setRemove(true);
			talking = false;
		}
		
	}

	public void update() {
		// check keys
		
		printDialogue();
		if(!talking){
			if(console == null){
				handleInput();
				player.update();
			}
			if(Keys.isPressed(Keys.BUTTON1)){
				if(player.tl == Tile.TERMINAL || player.tr == Tile.TERMINAL || player.bl == Tile.TERMINAL || player.br == Tile.TERMINAL){
					if(console == null){
						console = new Console(1);
					}else{
						if(dbox1 == null){
							dbox1 = new DialogBox("I'm... a glitch?", 1);
						}
						if(dbox1.isDone()){
							dbox1.setRemove(true);
							gsm.setState(GameStateManager.LEVEL2STATE);
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
			
			// attack enemies
			player.checkAttack(enemies);
			//player.checkPlatformCollision(platforms);
		}
		
		
	}

	
	public void draw(Graphics2D g) {
		
		//draw bg
		bg.draw(g);
		
		//draw tilemap
		tileMap.draw(g);
		
		//draw player
		player.draw(g);
		
		if(console != null) console.draw(g);
		//draw hud
		//hud.draw(g);
		
		if(dbox1 != null && !dbox1.shouldRemove()){
			dbox1.draw(g);
		}
		
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
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
