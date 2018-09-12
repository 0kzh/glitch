package Entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import Main.GamePanel;

public class FillScreen{
	
	private Color color;
	private float alpha = 1.0f;
	private boolean remove = false;
	private Graphics2D gg;
	
	public FillScreen(Color c){
		color = c;
		remove = false;
	}
	
	public FillScreen(Color c, float a){
		color = c;
		remove = false;
		alpha = a;
	}
	
	public boolean shouldRemove() { return remove; }
	public void setRemove(boolean b){
		remove = b; 
		if(remove && gg != null) gg.dispose();
	}
	public float getOpacity() { return alpha; }
	public void setOpacity(float a) { alpha = a; }
	
	public void draw(Graphics2D g){
		if(gg == null) gg = (Graphics2D) g.create();
		gg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		gg.setColor(color);
		gg.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}
	
}
