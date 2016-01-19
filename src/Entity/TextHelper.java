package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class TextHelper{
	
	private Font font;
	private String myString = "";
	private Color color;
	private int x;
	private int y;
	
	public TextHelper(String s, int myX, int myY, Color c){
		
		myString = s;
		x = myX;
		y = myY;
		color = c;
		font = new Font("Arial", Font.PLAIN, 9);
		
	}
	
	public void draw(Graphics2D g){
		g.setFont(font);
		g.setColor(color);
		g.drawString(myString, x, y);
	}
	
}
