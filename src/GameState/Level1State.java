package GameState;

import java.awt.*;
import java.io.IOException;
import GameState.GameStateManager;
import Handlers.Keys;
import Entity.*;
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
	private Player player;
	private DialogBox[] dialog = {
			new DialogBox("!!!", 2),
			new DialogBox("That was not supposed to happen!", 2),
			new DialogBox("Are you all right?", 2),
			new DialogBox("HAHAHA!!!", 3),
			new DialogBox("YOUR GAME SUCKS!", 3),
			new DialogBox("LET ME MAKE THINGS...", 3),
			new DialogBox("A BIT MORE INTERESTING.", 3)}; 
	private int index;
	private int index2 = 0;
	private boolean keyPressed;
	private boolean talking;
	private boolean pauseKeyPressed;
	private boolean introduced = false;
	private boolean jumped = false;
	private long timePassed;
	
	public Level1State(GameStateManager gsm){
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
		fs = new FillScreen(Color.BLACK);
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/texttileset.png");
		tileMap.loadMap("/Maps/level1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/level1bg.png", 0.1);
		player = new Player(tileMap);
		//player.setSpawnPoint(489, 55);
		//player.setPosition(489, 55);
		player.setSpawnPoint(41, 212);
		player.setPosition(41, 212);
		//hud = new HUD(player);
		
		JukeBox.load("/Music/bg.mp3", "bg");
		JukeBox.load("/SFX/press.mp3", "press");
		JukeBox.load("/SFX/level.mp3", "next");
		if(!JukeBox.isPlaying("bg")) JukeBox.loop("bg", 600, JukeBox.getFrames("bg") - 2200);
		
	}
	private void printDialogue() {
		if(index < dialog.length){
			dbox = dialog[index];
			JukeBox.stop("bg");
			talking = true;
		}else{
			if(!fs.shouldRemove()){
				fs.setRemove(true);
			}
			if(dbox2 == null && dbox.shouldRemove()) talking = false;
		}
		
	}

	@SuppressWarnings("static-access")
	public void update() {
		// check keys
		
		printDialogue();
		
		long elapsed = (System.nanoTime() - timePassed) / 1000000;
		if(elapsed > 500) player.dboxFinish = false;
		
		if(!talking && JukeBox.isPlaying("bg")){
			if(console == null){
				handleInput();
				player.update();
			}
			
			if(tileMap.isInvisible()){
				if(dbox2 == null){
					dbox2 = new DialogBox("Where did everything go!?", 1);
					talking = true;
				}
			}
			
			if(player.tl == Tile.TERMINAL || player.tr == Tile.TERMINAL || player.bl == Tile.TERMINAL || player.br == Tile.TERMINAL){
				if(player.teleported){
					gsm.setState(GameStateManager.LEVEL2STATE);
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
					JukeBox.resume("bg", true);
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
			} else if(index < dialog.length){
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
