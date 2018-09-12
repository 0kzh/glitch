package GameState;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import GameState.GameStateManager;
import Handlers.Keys;
import Entity.*;
import Main.GamePanel;
import TileMap.*;
import Audio.JukeBox;

public class Level6State extends GameState{

	private TileMap tileMap;
	private Background bg;
	private Console console;
	
	private DialogBox dbox;
	private Player player;
	private ArrayList<Enemy> enemies;
	private int[] checkpoints = {136, 272, 408, 544, 680, 816, 952};
	private DialogBox[] dialog = {
			new DialogBox("...", 3), 
			new DialogBox("You're persistent, aren't you?", 3), 
			new DialogBox("Fine then. I'll deal with you myself.", 3),
			new DialogBox("You know, it's never too late to give up.", 3),
			new DialogBox("You can still quit this game at any time.", 3),
			new DialogBox("But if you really want to commit suicide...", 3),
			new DialogBox("Be my guest.", 3)};
	private int index;
	private boolean talking;
	private boolean keyPressed;
	private boolean pauseKeyPressed;
	private boolean jumped = false;
	private long timePassed;
	
	public static boolean dialogue = true;
	
	public Level6State(GameStateManager gsm){
		super(gsm);
		init();
		try{
			gsm.save();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void init(){
		//initialize tile map
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/texttileset.png");
		tileMap.loadMap("/Maps/level6.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/preboss.png", 0.1);
		player = new Player(tileMap);
		//player.setSpawnPoint(489, 55);
		//player.setPosition(489, 55);
		player.setSpawnPoint(73, 168);
		player.setPosition(73, 168);
		//hud = new HUD(player);
		
		enemies = new ArrayList<Enemy>();
		if(JukeBox.isPlaying("level1")) JukeBox.stop("level3");
		
		JukeBox.load("/Music/bg.mp3", "bg");
		JukeBox.load("/SFX/press.mp3", "press");
		JukeBox.load("/SFX/level.mp3", "next");
		JukeBox.stop("bg");
//		if(!JukeBox.isPlaying("bg")) JukeBox.loop("bg", 600, JukeBox.getFrames("bg") - 2200);
		
	}
	
	private void printDialogue(){
		if(index < dialog.length){
			if(player.getx() > checkpoints[index]){
				talking = true;
				dbox = dialog[index];
				dbox.setRemove(false);
				index++;
			}
		}else{
			if(dbox.shouldRemove()) talking = false;
		}
	}

	@SuppressWarnings("static-access")
	public void update(){
		
		if(dialogue) printDialogue();
			
		long elapsed = (System.nanoTime() - timePassed) / 1000000;
		if(elapsed > 500) player.dboxFinish = false;
		
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).update();
		}
		
		if(!talking && dialogue){
			if(console == null){
				handleInput();
				player.update();
				player.checkAttack(enemies);
			}
			
			if(player.tl == Tile.TERMINAL || player.tr == Tile.TERMINAL || player.bl == Tile.TERMINAL || player.br == Tile.TERMINAL){
				if(player.teleported){
					gsm.setState(GameStateManager.LEVEL7STATE);
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

	
	public void draw(Graphics2D g){
		
		//draw bg
		bg.draw(g);
		
		//draw tilemap
		tileMap.draw(g);
		
		if(!(player.getHealth() <= 0)){
			for(int i = 0; i < enemies.size(); i++){
				enemies.get(i).draw(g);
			}
		}
		
		//draw player
		player.draw(g);
		
		if(console != null) console.draw(g);
		//draw hud
		//hud.draw(g);
		
		try{
			if(!dbox.shouldRemove()){
				dbox.draw(g);
				if(Keys.isPressed(Keys.BUTTON1) && !keyPressed && dbox.isDone()){
					dbox.setRemove(true);
					
					keyPressed = true;
				}else if(!Keys.isPressed(Keys.BUTTON1)){
					keyPressed = false;
				}
			}else{
				talking = false;
			}
			
		}catch(Exception e){
		}
	}
	
	public void handleInput(){
		
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
