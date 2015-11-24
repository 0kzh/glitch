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
	private Player player;
	public boolean talking;
	public boolean z_pressed;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions; 
	
	public Level1State(GameStateManager gsm){
		super(gsm);
		init();
	}
	
	public void init() {
		//initialize tile map
		tileMap = new TileMap(24);
		tileMap.loadTiles("/Tilesets/cavetileset.png");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/level1bg.gif", 0.1);
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
		populateEnemies();
		playCutscene();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		JukeBox.load("/Music/level1-1.mp3", "level1");
		JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);
	}
	
	private synchronized void playCutscene() {
		fs = new FillScreen(Color.BLACK);
		DialogBox[] dialog = {new DialogBox("Where... Where am I?"), new DialogBox("..."), new DialogBox("...")}; 
		
		talking = true;
		for(int i = 0; i < dialog.length; i++){
			dbox = dialog[i];
			try {
				dbox.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void populateEnemies(){
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Point[] points = new Point[] {
			//new Point(1000, 200),
			//new Point(1050, 200),
			//new Point(1560, 230)
		};
		
		for(int i = 0; i < points.length; i++){
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
	}

	public void update() {
		
		// check keys
		handleInput();
		
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		
		
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// attack enemies
		player.checkAttack(enemies);
		
		//update enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()){
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getx(), e.gety()));
			}
		}
		
		//update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()){
				explosions.remove(i);
				i--;
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
		
		//draw enemies
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).draw(g);
		}
		
		//draw explosions
		for(int i = 0; i < explosions.size(); i++){
			explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
			
		}
		
		
		//draw hud
		hud.draw(g);
		
		fs.draw(g);
		
		if(talking) dbox.draw(g);
	}
	
	public void handleInput() {
		//if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(true);
		if(player.getHealth() == 0) return;
		player.setUp(Keys.keyState[Keys.UP]);
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.BUTTON1]);
		if(Keys.isPressed(Keys.BUTTON2)) player.setFiring();
	}

	
	
}
