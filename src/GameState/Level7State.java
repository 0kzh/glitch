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
	private FillScreen flash;
	private boolean fade;
	private DialogBox dbox;
	private DialogBox dbox2;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private DialogBox[] dialog = {
			new DialogBox("I must commend you for making it this far.", 3),
			new DialogBox("But unfortunately for you, this is the end.", 3), 
			new DialogBox("BEHOLD MY TRUE FORM !", 3, true)};
	private DialogBox[] dialog1 = {
			new DialogBox("NooooOooOOOooo!!!", 4, true),
			new DialogBox("I... I DO NOT BELIEVE IT.", 4),
			new DialogBox("PLEASE, I BEG YOU, LET ME LIVE!", 4),
			new DialogBox("I'LL DO ANYTHING!", 5), 
			new DialogBox("I CANNOT GO SO SOON!", 5), 
			new DialogBox("I STILL HAVE A FAMILY!", 5), 
			new DialogBox("I STILL WAnt tooo —", 5), 
			new DialogBox("...", 6), 
			new DialogBox("Good riddance.", 1), 
			new DialogBox("", 1), 
			new DialogBox("Player, I cannot tell you how grateful I am.", 1), 
			new DialogBox("That virus almost drove me mad.", 1), 
			new DialogBox("Thanks to you, he is now gone — forever.", 1)};
	private TextHelper[] messages = new TextHelper[1];
	private ArrayList<Integer> waitTimes;
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
	
	private Enemy boss;
	
	public static boolean dialogue = true;
	
	public Level7State(GameStateManager gsm){
		super(gsm);
		init();
		try{
			gsm.save();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void init() {
		fs = new FillScreen(Color.BLACK);
		flash = new FillScreen(new Color(255,224,178), 0.8f);
		flash.setRemove(true);
		
		//initialize tile map
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/texttileset.png");
		tileMap.loadMap("/Maps/level7.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		messages[0] = new TextHelper(tileMap, "Drop, Jump, Jump!", 825, 130, Color.WHITE);
		
		//set background
		bg = new Background("/Backgrounds/preboss.png", 0.1);
		
		//initialize player and set position/spawn
		player = new Player(tileMap);
		player.setSpawnPoint(119, 181);
		player.setPosition(119, 181);
				
		//initialize enemies/explosions (explosions when dead)
		explosions = new ArrayList<Explosion>();
		enemies = new ArrayList<Enemy>();
		
		//add boss to enemy
		boss = new MiniBoss(tileMap);
		boss.setPosition(30, 120);
		enemies.add(boss);
		
		
		//music stuff
		if(JukeBox.isPlaying("bg")) JukeBox.stop("bg");
		
		JukeBox.load("/Music/boss1.mp3", "boss1");
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
		
		if(eventBossDead){
			if(waitTimes == null || waitTimes.isEmpty()){
				waitTimes = new ArrayList<Integer>();
				waitTimes.add(30);
				waitTimes.add(60);
				waitTimes.add(90);
				waitTimes.add(120);
				
				waitTimes.add(140);
				waitTimes.add(160);
				waitTimes.add(180);
				waitTimes.add(200);
				
				waitTimes.add(210);
				waitTimes.add(220);
				waitTimes.add(230);
				waitTimes.add(240);
				
				waitTimes.add(245);
				waitTimes.add(250);
				waitTimes.add(255);
				waitTimes.add(260);
				waitTimes.add(265);
				waitTimes.add(270);
				waitTimes.add(275);
				waitTimes.add(280);
//				int start = 0;
//				for(int i = 0; i < 15; i++){
//					start += (int) (40 - Math.pow(2, 0.59 * (i-6)));
//					waitTimes.add(start);
//					System.out.println(start);
//				}
			}
			
			eventBossDead();
		}
		
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
		
		boolean drawEnemy = true;
		//fade out boss if index >= 7
		if(index3 >= 7){
			MiniBoss mBoss = (MiniBoss) boss;
			if(mBoss.getOpacity() - 0.05f >= 0){
				mBoss.setOpacity(mBoss.getOpacity()-0.05f);
			}else{
				drawEnemy = false;
			}
		}
		
		if(!(player.getHealth() <= 0)){
			for(int i = 0; i < enemies.size(); i++){
				if(drawEnemy) enemies.get(i).draw(g);
			}
			
			for(int i = 0; i < messages.length; i++){
				if(!tileMap.isInvisible()) messages[i].draw(g);
			}
		}else{
			if(drawEnemy) enemies.get(0).setPosition(30, 120);
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
			} else if(index < dialog.length){
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
			} else if(index3 < dialog1.length){
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
		
		if(!flash.shouldRemove()){
			flash.draw(g);
		}
	}
	
	public void eventBossDead() {
		eventCount++;
		if(eventCount == 1) {
			JukeBox.stop("boss1");
			
		}
		
//		int rem = (int) (Math.pow(2, 0.0576*eventCount));
//		System.out.println(eventCount);
//		System.out.println(rem);
		if(eventCount <= 280 && waitTimes.contains(eventCount)) {
//			explosions.add(new Explosion(tileMap, enemies.get(0).getx(), enemies.get(0).gety()));
			explosions.add(new Explosion(tileMap, (enemies.get(0).getx() + r.nextInt(40)), (enemies.get(0).gety()) - r.nextInt(120)));
			explosions.add(new Explosion(tileMap, (enemies.get(0).getx() - r.nextInt(40)), (enemies.get(0).gety()) + r.nextInt(120)));
			explosions.add(new Explosion(tileMap, (enemies.get(0).getx() + r.nextInt(40)), (enemies.get(0).gety()) - r.nextInt(120)));
			explosions.add(new Explosion(tileMap, (enemies.get(0).getx() - r.nextInt(40)), (enemies.get(0).gety()) + r.nextInt(120)));
			explosions.add(new Explosion(tileMap, (enemies.get(0).getx() - r.nextInt(40)), (enemies.get(0).gety()) - r.nextInt(120)));
			explosions.add(new Explosion(tileMap, (enemies.get(0).getx() + r.nextInt(40)), (enemies.get(0).gety()) + r.nextInt(120)));
			JukeBox.play("explode");
		}
		
		if(eventCount == 280){
			flash.setRemove(false);
		}
		
		if(flash.getOpacity() > 0 && !flash.shouldRemove()){
			if(flash.getOpacity() - 0.015f >= 0){
				flash.setOpacity(flash.getOpacity()-0.015f);
			}else{
				flash.setRemove(true);
			}
		}
		
		if(eventCount == 360) {
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
