package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.GamePanel;
import TileMap.Tile;

import TileMap.TileMap;

public abstract class Platform extends MapObject{
	
	// constructor
	public Platform(TileMap tm){
		super(tm);
		
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	
	public void checkTileMapCollision() {
			
		currCol = (int) x / tileSize;
		currRow = (int) y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		
		
		calculateCorners(x, ydest);
		if(dy < 0){
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize;
				
			}else{
				ytemp += dy;
			}
		}
		
		if(dy > 0){
			if(bottomLeft || bottomRight){
				dy = 0;
				ytemp = (currRow + 1) * tileSize;
			}else{
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0){
			if(topLeft || bottomLeft){
				dx = 0;
				xtemp = currCol * tileSize;
				
			}else{
				xtemp += dx;
			}
		}
		
		if(dx > 0){
			if(topRight || bottomRight){
				dx = 0;
				xtemp = currCol * tileSize;
				
			}else{
				xtemp += dx;
			}
		}
	}
	
	public void draw(Graphics2D g){
		g.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int)(y + ymap - height / 2), null);
	}
}
