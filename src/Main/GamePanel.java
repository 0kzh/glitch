package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.apple.eawt.Application;

import Handlers.Keys;

import GameState.GameStateManager;
import GameState.OptionsState;

public class GamePanel extends JPanel implements Runnable, KeyListener{

	private static final long serialVersionUID = 1L;
	
	// dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS; // get target in millis
	
	
	//image
	private BufferedImage image;
	private Graphics2D g;
	
	//game state manager
	private GameStateManager gsm;
	
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	public void keyPressed(KeyEvent key){
		Keys.keySet(key.getKeyCode(), true);
		if(OptionsState.reading){
			OptionsState.keyPress = key.getKeyCode();
			OptionsState.reading = false;
			OptionsState.select();
		}
	}

	public void keyReleased(KeyEvent key){
		Keys.keySet(key.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent key){
		
	}

	public void run(){
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		while(running){
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
		
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			
			try{
				if(wait > 0){
					Thread.sleep(wait);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	private void drawToScreen(){
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}

	private void draw(){
		gsm.draw(g);
	}

	private void update(){
		gsm.update();
	}

	private void init(){
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		running = true;
		gsm = new GameStateManager();
	}
	
}
