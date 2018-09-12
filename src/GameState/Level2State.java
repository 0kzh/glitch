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
	private DialogBox dbox1;
	private Player player;
	private ArrayList<Enemy> enemies;
	private int index2 = 0;
	private boolean talking;
	private boolean pauseKeyPressed;
	private boolean jumped = false;
	private long timePassed;
	
	public Level2State(GameStateManager gsm){
		super(gsm);
		init();
		try{
			gsm.save();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void init() {
		//initialize tile map
		
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/texttileset.png");
		tileMap.loadMap("/Maps/level2.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/level1bg.png", 0.1);
		player = new Player(tileMap);
		player.setSpawnPoint(37, 160);
		player.setPosition(37, 160);
		
		enemies = new ArrayList<Enemy>();
//		Enemy bat = new Bat(tileMap);
//		bat.setPosition(169, 135);
//		enemies.add(bat);
//		
//		Enemy worm = new Slugger(tileMap);
//		worm.setPosition(233, 187);
//		enemies.add(worm);
		
		JukeBox.load("/Music/bg.mp3", "bg");
		JukeBox.load("/SFX/press.mp3", "press");
		JukeBox.load("/SFX/level.mp3", "next");
		if(!JukeBox.isPlaying("bg")) JukeBox.loop("bg", 600, JukeBox.getFrames("bg") - 2200);
		
	}

	@SuppressWarnings("static-access")
	public void update() {
		
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).update();
		}
		
		long elapsed = (System.nanoTime() - timePassed) / 1000000;
		if(elapsed > 500) player.dboxFinish = false;
		
		if(!talking && JukeBox.isPlaying("bg")){
			if(console == null){
				handleInput();
				player.update();
				player.checkAttack(enemies);
			}
			
			if(player.tl == Tile.TERMINAL || player.tr == Tile.TERMINAL || player.bl == Tile.TERMINAL || player.br == Tile.TERMINAL){
				if(player.teleported){
					gsm.setState(GameStateManager.LEVEL3STATE);
					player.teleported = false;
				}
			}
			
			tileMap.setPosition(
					GamePanel.WIDTH / 2 - player.getx(),
					GamePanel.HEIGHT / 2 - player.gety()
				);
			tileMap.update();
			tileMap.fixBounds();
			
			bg.setPosition(tileMap.getx(), tileMap.gety());
		}else{
			player.dboxFinish = true;
			timePassed = System.nanoTime();
		}
	}

	
	public void draw(Graphics2D g) {
		
		//draw bg
		bg.draw(g);
		
		//draw tilemap
		tileMap.draw(g);
		
		//draw messages
		if(!(player.getHealth() <= 0) && !enemies.isEmpty()){
			for(int i = 0; i < enemies.size(); i++){
				enemies.get(i).draw(g);
			}
		}
		
		//draw player
		player.draw(g);
		
		if(console != null) console.draw(g);
		//draw hud
		//hud.draw(g);
		
		if(dbox1 != null && !dbox1.shouldRemove()){
			dbox1.draw(g);
		}
		
		try{
			if(!fs.shouldRemove()){
				fs.draw(g);
			}else{
				if(index2 < 30){
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2 - (index2 * GamePanel.HEIGHT / 60));
					g.fillRect(0, GamePanel.HEIGHT / 2 + (index2 * GamePanel.HEIGHT / 60), GamePanel.WIDTH, GamePanel.HEIGHT / 2);
					Thread.sleep(10);
					index2++;
				
				}else{
					JukeBox.resume("bg", true);
				}
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
		if(Keys.isPressed(Keys.BUTTON1)){
			if(!jumped){
				player.setJumping(true);
				jumped = true;
			}
		}else{
			jumped = false;
		}
	}
	
}
