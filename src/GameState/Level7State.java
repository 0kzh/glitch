package GameState;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import GameState.GameStateManager;

import Handlers.Keys;

import Entity.*;
import Entity.Enemies.MiniBoss;
import Main.GamePanel;
import TileMap.*;
import Audio.JukeBox;

public class Level7State extends GameState{

	private TileMap tileMap;
	private Background bg;
	private Console console;
	private FillScreen fs;
	private boolean fade;
	private DialogBox dbox;
	private DialogBox dbox2;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private DialogBox[] dialog = {
			new DialogBox("I TRICKED YOU AGAIN!!", 3, true),
			new DialogBox("You humans are foolish creatures.", 3), 
			new DialogBox("Prepare to face... my greatest creation!", 3), 
			new DialogBox("THE GATES-O-TRON 5000!", 3), 
			new DialogBox("*grumble*", 8)};
	private DialogBox[] dialog1 = {
			new DialogBox("NO! MY PRECIOUS CREATION!", 4, true),
			new DialogBox("OKAY OKAY! YOU WIN!", 4), 
			new DialogBox("I'LL DO ANYTHING! JUST LEAVE -- PLEASE!", 4), 
			new DialogBox("Only if you get me out of this place.", 1), 
			new DialogBox("FINE.", 4)};
	private TextHelper[] messages = {new TextHelper("Drop, Jump, Jump!", 771, 99, Color.WHITE)};
	private int index;
	private int index2 = 0;
	private int index3 = 0;
	private int index4 = 0;
	private boolean talking;
	private boolean keyPressed;
	private boolean pauseKeyPressed;
	private boolean jumped = false;
	private long timePassed;
	private boolean eventBossDead;
	private boolean eventFinish;
	private int eventCount;
	private Random r;
	
	public static boolean dialogue = true;
	
	public Level7State(GameStateManager gsm){
		super(gsm);
		init();
		try {
			gsm.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		fs = new FillScreen(Color.BLACK);
		
		//initialize tile map
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/texttileset.png");
		tileMap.loadMap("/Maps/level7.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		//set background
		bg = new Background("/Backgrounds/level1bg.png", 0.1);
		
		//initialize player and set position/spawn
		player = new Player(tileMap);
		player.setSpawnPoint(119, 181);
		player.setPosition(119, 181);
				
		//initialize enemies/explosions (explosions when dead)
		explosions = new ArrayList<Explosion>();
		enemies = new ArrayList<Enemy>();
		
		//add boss to enemy
		Enemy boss = new MiniBoss(tileMap);
		boss.setPosition(0, 120);
		enemies.add(boss);
		
		
		//music stuff
		if(JukeBox.isPlaying("bg")) JukeBox.stop("bg");
		
		JukeBox.load("/Music/boss.mp3", "boss1");
		JukeBox.load("/SFX/press.mp3", "press");
		JukeBox.load("/SFX/level.mp3", "next");
		JukeBox.load("/SFX/explode.mp3", "explode");
		JukeBox.loop("boss1", 600, JukeBox.getFrames("boss1") - 2200);
		
		//initialize variables
		eventCount = 0;
		eventBossDead = false;
		eventFinish = false;

		r = new Random();
		
	}
	
	private void printDialogue() {
		if(index < dialog.length){
			dbox = dialog[index];
			JukeBox.stop("boss1");
			talking = true;
		}else{
			if(!eventBossDead && eventFinish){
				if(index3 < dialog1.length){
					dbox2 = dialog1[index3];
					talking = true;
				}else{
					dbox2.setRemove(true);
					talking = false;
					fade = true;
				}
			}
			if(dbox2 == null && dbox.shouldRemove()){
				talking = false;
				fs.setRemove(true);
			}
		}
		
	}

	public void update() {
		//for each tick (60 times per second)
		
		if(dialogue) printDialogue();
			
		long elapsed = (System.nanoTime() - timePassed) / 1000000;
		if(elapsed > 500) player.dboxFinish = false;
		
		
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		if(enemies.size() > 0){
			if(!eventFinish && enemies.get(0).isDead()) {
				eventBossDead = true;
			}
		}
		
		if(eventBossDead) eventBossDead();
		
		if(!talking && JukeBox.isPlaying("boss1")){
			
			for(int i = 0; i < enemies.size(); i++){
				enemies.get(i).update();
			}
			
			if(console == null){
				if(!eventBossDead){
					handleInput();
				}
				player.update();
				player.checkAttack(enemies);
			}
			
			tileMap.setPosition(
					tileMap.getx() - 0.8,
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
		
		if(!(player.getHealth() <= 0)){
			for(int i = 0; i < enemies.size(); i++){
				enemies.get(i).draw(g);
			}
			for(int i = 0; i < messages.length; i++){
				messages[i].draw(g);
			}
		}else{
			enemies.get(0).setPosition(0, 120);
		}
		
		//draw player
		player.draw(g);
		
		//draw explosions
		for(Explosion e : explosions) {
			e.draw(g);
		}
		
		if(console != null) console.draw(g);
		
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
					if(!eventBossDead && !eventFinish) JukeBox.resume("boss1", true);
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
			
			if(!dbox2.shouldRemove()){
				dbox2.draw(g);
				if(Keys.isPressed(Keys.BUTTON1) && !keyPressed && dbox2.isDone()){
					dbox2.setRemove(true);
					
					keyPressed = true;
				}else if(!Keys.isPressed(Keys.BUTTON1)){
					keyPressed = false;
				}
			}
			else if(index3 < dialog1.length){
				index3++;
			}
			
		}catch(Exception e){
		}
		
		if(fade){
			g.setColor(new Color(0, 0, 0, index4));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			if(index4 < 255) index4 += 15;
			else{
				gsm.setState(GameStateManager.LEVEL8STATE);
			}
		}
	}
	
	public void eventBossDead() {
		eventCount++;
		if(eventCount == 1) {
			JukeBox.stop("boss1");
		}
		if(eventCount <= 120 && eventCount % 30 == 0) {
			tileMap.setShaking(true, 10);
			explosions.add(new Explosion(tileMap, enemies.get(0).getx(), enemies.get(0).gety()));
			explosions.add(new Explosion(tileMap, (enemies.get(0).getx() + r.nextInt(40)), (enemies.get(0).gety()) - r.nextInt(120)));
			explosions.add(new Explosion(tileMap, (enemies.get(0).getx() - r.nextInt(40)), (enemies.get(0).gety()) + r.nextInt(1200)));
			explosions.add(new Explosion(tileMap, (enemies.get(0).getx() + r.nextInt(40)), (enemies.get(0).gety()) - r.nextInt(120)));
			explosions.add(new Explosion(tileMap, (enemies.get(0).getx() - r.nextInt(40)), (enemies.get(0).gety()) + r.nextInt(120)));
			JukeBox.play("explode");
		}
		
		if(eventCount == 250) {
			tileMap.setShaking(false, 0);
			eventBossDead = false;
			eventCount = 0;
			eventFinish = true;
			//next level
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
