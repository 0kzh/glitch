package Entity;

import java.awt.Color;
import java.awt.Graphics2D;

import Main.GamePanel;

public class FillScreen{
	
	private Color color;
	
	public FillScreen(Color c){
		
		color = c;
		
	}
	
	public void draw(Graphics2D g){
		
		g.setColor(color);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
	}
	
}
