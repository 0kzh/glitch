package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import GameState.GameStateManager;
import Handlers.Keys;
import Entity.*;
import Main.GamePanel;
import TileMap.*;
import Audio.JukeBox;

public class Level0State extends GameState{

	private TileMap tileMap;
	private Background bg;
	private Console console;
	private FillScreen fs;
	private DialogBox dbox;
	private DialogBox dbox1;
	private Player player;
	private TextHelper[] messages = {new TextHelper("Z", 195, 181, Color.WHITE), new TextHelper("Double Jump!", 230, 135, Color.WHITE)};
	//private ArrayList<Explosion> explosions; 
	private DialogBox[] dialog = {
			new DialogBox("Greetings Player! Welcome to GLITCH!", 2), 
			new DialogBox("I'm Bill, and I will be your guide today!", 2), 
			new DialogBox("And now... for the controls:", 2),
			new DialogBox("Use the arrow keys to move!", 2),
			new DialogBox("Press " + KeyEvent.getKeyText(Keys.keyZ) +" to jump!", 2)}; 
	private int index;
	private int index2 = 0;
	private boolean keyPressed;
	private boolean talking;
	private boolean pauseKeyPressed;
	private boolean introduced = false;
	private boolean jumped = false;
	private long timePassed;
	public static boolean playedOnce = false;
	
	public Level0State(GameStateManager gsm){
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
		fs = new FillScreen(Color.BLACK);
		
		if(!KeyEvent.getKeyText(Keys.keyLeft).equals("Left") || !KeyEvent.getKeyText(Keys.keyRight).equals("Right")){
			dialog[3] = new DialogBox("Use " + KeyEvent.getKeyText(Keys.keyLeft) + " and " + KeyEvent.getKeyText(Keys.keyRight) + " to move!", 2);
		}
		
		//load and initialize tilemap & background
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/tileset.png");
		tileMap.loadMap("/Maps/intro.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/gamebg.png", 0.1);
		player = new Player(tileMap);
		player.setSpawnPoint(35, 206);
		player.setPosition(35, 206);
		
		//load musics
		JukeBox.load("/Music/bg.mp3", "bg");
		JukeBox.load("/SFX/press.mp3", "press");
		JukeBox.loop("bg", 600, JukeBox.getFrames("bg") - 2200);
		
	}
	
	private void printDialogue() {
		if(index < dialog.length){
			dbox = dialog[index];
			JukeBox.stop("bg");
			talking = true;
		}else{
			fs.setRemove(true);
			if(dbox1 == null) talking = false;
		}
		
	}

	public void update() {
		
		printDialogue();
		
		long elapsed = (System.nanoTime() - timePassed) / 1000000;
		if(elapsed > 500) player.dboxFinish = false;
		
		if(!talking){
			handleInput();
			player.update();
			
			if(player.getx() < 205 && player.gety() < 75){
				if(dbox1 == null){
					dbox1 = new DialogBox("Pet that cat to advance to the next level!", 2);
					talking = true;
				}
			}
			
			if(player.tl == Tile.DAMAGING || player.tr == Tile.DAMAGING || player.bl == Tile.DAMAGING || player.br == Tile.DAMAGING){
				JukeBox.stop("bg");
			}
			
			if(playedOnce){
				gsm.setState(GameStateManager.LEVEL1STATE);
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
			if(dbox1 != null && !introduced){
				if(dbox1.isDone()){
					dbox1.setRemove(true);
					introduced = true;
					talking = false;
				}
			}
		}
		
		
	}

	
	@SuppressWarnings("static-access")
	public void draw(Graphics2D g) {
		
		//draw bg
		bg.draw(g);
		
		//draw tilemap
		tileMap.draw(g);
		
		//draw messages
		if(tileMap.fs == null){
			for(int i = 0; i < messages.length; i++){
				messages[i].draw(g);
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
