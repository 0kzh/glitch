package Handlers;

import java.awt.event.KeyEvent;

// this class contains a boolean array of current and previous key states
// for the 10 keys that are used for this game.
// a key k is down when keyState[k] is true.

public class Keys{
	
	public static final int NUM_KEYS = 16;
	
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];
	
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	public static int BUTTON1 = 4;
	public static int ENTER = 6;
	public static int ESCAPE = 7;
	
	public static int keyUp = KeyEvent.VK_UP;
	public static int keyDown = KeyEvent.VK_DOWN;
	public static int keyLeft = KeyEvent.VK_LEFT;
	public static int keyRight = KeyEvent.VK_RIGHT;
	public static int keyZ = KeyEvent.VK_Z;
	
	
	public static void keySet(int i, boolean b) {
		if(i == keyUp) keyState[UP] = b;
		else if(i == keyLeft) keyState[LEFT] = b;
		else if(i == keyDown) keyState[DOWN] = b;
		else if(i == keyRight) keyState[RIGHT] = b;
		else if(i == keyZ) keyState[BUTTON1] = b;
		else if(i == KeyEvent.VK_ENTER) keyState[ENTER] = b;
		else if(i == KeyEvent.VK_ESCAPE) keyState[ESCAPE] = b;
	}
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}
	
	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}
	
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(keyState[i]) return true;
		}
		return false;
	}
}
