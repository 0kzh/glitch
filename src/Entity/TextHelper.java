package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import TileMap.TileMap;

public class TextHelper extends MapObject{
	
	private Font font;
	private String myString = "";
	private Color color;
	
	public TextHelper(TileMap tm, String s, int x, int y, Color c){
		super(tm);
		myString = s;
		this.x = x;
		this.y = y;
		color = c;
		font = new Font("Arial", Font.PLAIN, 9);
		
	}
	
	public void draw(Graphics2D g){
		setMapPosition();
		g.setFont(font);
		g.setColor(color);
		g.drawString(myString, (int) x, (int) y);
	}
	
}
