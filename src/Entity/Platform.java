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
	
	public void calculateCorners(double x, double y) {
		
		int leftTile = (int) (x - cwidth / 2) / tileSize;
		int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
		int topTile = (int) (y - cheight / 2) / tileSize;
		int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
		
		
		if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
                leftTile < 0 || rightTile >= tileMap.getNumCols()) {
                topLeft = topRight = bottomLeft = bottomRight = false;
                return;
        }
		
		tl = tileMap.getType(topTile, leftTile);
		tr = tileMap.getType(topTile, rightTile);
		bl = tileMap.getType(bottomTile, leftTile);
		br = tileMap.getType(bottomTile, rightTile);
		
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
		
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
