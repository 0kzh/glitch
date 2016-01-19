package GameState;

import java.awt.*;
import GameState.GameStateManager;
import Handlers.Keys;
import Entity.*;
import Main.GamePanel;
import TileMap.*;
import Audio.JukeBox;

public class Level8State extends GameState{

	private TileMap tileMap;
	private Background bg;
	private Player player;
	private boolean pauseKeyPressed;
	private boolean jumped = false;
	public static boolean playedOnce = false;
	private int index = 0;
	private boolean fade = false;
	
	public Level8State(GameStateManager gsm){
		super(gsm);
		init();
	}
	
	public void init() {
		//initialize tile map
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/tileset.png");
		tileMap.loadMap("/Maps/level8.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/gamebg.png", 0.1);
		player = new Player(tileMap);
		//player.setSpawnPoint(489, 55);
		//player.setPosition(489, 51);
		player.setSpawnPoint(162, 206);
		player.setPosition(162, 51);
		//hud = new HUD(player);
		
		JukeBox.load("/Music/bg.mp3", "bg");
		JukeBox.load("/SFX/press.mp3", "press");
		JukeBox.load("/SFX/level.mp3", "level");
		JukeBox.loop("bg", 600, JukeBox.getFrames("bg") - 2200);
		
	}
	@SuppressWarnings("static-access")
	public void update() {
		
		handleInput();
		player.update();
		
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getx(),
				GamePanel.HEIGHT / 2 - player.gety()
			);
		tileMap.update();
		tileMap.fixBounds();
		
		if(player.tl == Tile.TERMINAL || player.tr == Tile.TERMINAL || player.bl == Tile.TERMINAL || player.br == Tile.TERMINAL){
			if(player.teleported){
				fade = true;
				player.teleported = false;
			}
		}
		
		bg.setPosition(tileMap.getx(), tileMap.gety());
	}

	
	public void draw(Graphics2D g) {
		
		//draw bg
		bg.draw(g);
		
		//draw tilemap
		tileMap.draw(g);
		
		//draw player
		player.draw(g);
		
		//draw fade out
		if(fade){
			g.setColor(new Color(0, 0, 0, index));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			if(index < 255) index += 5;
			else{
				gsm.setState(GameStateManager.CREDITSSTATE);
			}
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
