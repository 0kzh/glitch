package Entity;

import java.awt.Color;
import java.awt.Graphics2D;

import Main.GamePanel;

public class FillScreen{
	
	private Color color;
	private boolean remove = false;
	
	public FillScreen(Color c){
		
		color = c;
		remove = false;
		
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void setRemove(boolean b) { remove = b; }
	
	public void draw(Graphics2D g){
		g.setColor(color);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
	}
	
}
