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
	private DialogBox dbox2;
	private DialogBox dbox1;
	private TextPlayer player;
	
	private DialogBox[] dialog = {
			new DialogBox("JUST KIDDING.", 2, true), 
			new DialogBox("THAT CAT JUST KILLED YOU.", 2), 
			new DialogBox("hahaha!", 2),
			new DialogBox("HaHaHa!!", 2),
			new DialogBox("HAHAHAHA!11!1!", 2)}; 
	private int index;
	private int index2 = 0;
	private boolean keyPressed;
	private boolean talking;
	private boolean pauseKeyPressed;
	private boolean introduced = false;
	private long timePassed;
	
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
		tileMap.loadMap("/Maps/level1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/level1bg.png", 0.1);
		player = new TextPlayer(tileMap);
		//player.setSpawnPoint(489, 55);
		//player.setPosition(489, 55);
		player.setSpawnPoint(41, 212);
		player.setPosition(41, 212);
		//hud = new HUD(player);
		
		JukeBox.stopAll();
		JukeBox.load("/Music/level1-1.mp3", "level1");
		JukeBox.load("/SFX/press.mp3", "press");
		JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);
		
	}
	private void printDialogue() {
		if(index < dialog.length){
			fs = new FillScreen(Color.BLACK);
			dbox = dialog[index];
			JukeBox.stop("level1");
			talking = true;
		}else{
			if(!fs.shouldRemove()){
				try {
					Thread.sleep(1000);
					dbox = new DialogBox("...", 1);
					talking = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				fs.setRemove(true);
			}
			if(dbox2 == null && dbox.shouldRemove()) talking = false;
		}
		
	}

	public void update() {
		// check keys
		
		printDialogue();
		
		long elapsed = (System.nanoTime() - timePassed) / 1000000;
		if(elapsed > 500) player.dboxFinish = false;
		
		if(!talking && JukeBox.isPlaying("level1")){
			if(console == null){
				handleInput();
				player.update();
			}
			
			if(player.getx() > 145 && player.gety() > 172){
				if(dbox2 == null){
					dbox2 = new DialogBox("Where am I?", 1);
					talking = true;
				}
			}
			
			if(player.tl == Tile.TERMINAL || player.tr == Tile.TERMINAL || player.bl == Tile.TERMINAL || player.br == Tile.TERMINAL){
				gsm.setState(GameStateManager.LEVEL2STATE);
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
		
		if(Keys.isPressed(Keys.BUTTON1)){
			if(dbox2 != null && !introduced){
				if(dbox2.isDone()){
					dbox2.setRemove(true);
					introduced = true;
					talking = false;
				}
			}
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
		
		if(dbox2 != null && !dbox2.shouldRemove()){
			dbox2.draw(g);
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
