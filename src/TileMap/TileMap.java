package TileMap;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

import Audio.JukeBox;
import Entity.DialogBox;
import Entity.FillScreen;
import Main.GamePanel;

public class TileMap {
	
	// position
	private double x;
	private double y;
	private double xtemp;
	private double ytemp;
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	private boolean loaded;
	
	private double tween;
	
	// map
	private String loadedMap;
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	private boolean shaking;
	private boolean invisible;
	private int intensity;
	public static FillScreen fs;
	private BufferedImage[] sides;
	
	//timekeeping
	private long elapsed;
	private long timePassed;
	
	public TileMap(int tileSize){
		this.tileSize = tileSize;
		sides = new BufferedImage[6];
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
		loaded = false;
	}
	
	public void loadTiles(String s){
		try{
			
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++){
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				if(s.equals("/Tilesets/texttileset.png") || s.equals("/Tilesets/tileset.png")){
					if(col == 2){
						tiles[0][col] = new Tile(subimage, Tile.BLOCKED);
					}else if(col == 4){
						tiles[0][col] = new Tile(subimage, Tile.TERMINAL);
					}else if(col == 3){
						tiles[0][col] = new Tile(subimage, Tile.DAMAGING);
					}else{
						tiles[0][col] = new Tile(subimage, Tile.NORMAL);
					}
				}else{
					tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				}
				subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				if(s.equals("/Tilesets/texttileset.png") || s.equals("/Tilesets/tileset.png")){
					if(col == 15){
						tiles[1][col] = new Tile(subimage, Tile.DAMAGING);
					}else{
						tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
					}
				}else{
					tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
				}
			}
			for(int col = 0; col < 6; col++){
				sides[col] = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s){
		loadedMap = s;
		try{
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++){
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++){
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			loaded = true;
			timePassed = 0;
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public int getTileSize() { return tileSize; }
	public double getx() { return x; }
	public double gety() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	public boolean isLoaded() { return loaded; }
	public boolean isShaking() { return shaking; }
	public boolean isInvisible() { return invisible; }
	public void resetCounter() { timePassed = 0; }
	
	public int getType(int row, int col){
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		
		return tiles[r][c].getType();
	}
	
	public void setType(int row, int col, int type){
		map[row][col] = type;
	}
	
	public void setTween(double t){
		this.tween = t;
	}
	
	public void setShaking(boolean b, int i) { 
		shaking = b;
		intensity = i; 
	}
	
	public void setInvisible(boolean b){
		invisible = b;
	}
	
	public void setPosition(double x, double y){
		
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();
		
		colOffset = (int) - this.x / tileSize;
		rowOffset = (int) - this.y / tileSize;
		
	}
	
	public void fixBounds(){
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void update(){
		if(!loadedMap.contains("intro.map") && !loadedMap.contains("level6.map") && !loadedMap.contains("level8.map")){
			if(timePassed == 0) timePassed = System.nanoTime();
			long elapsed = (System.nanoTime() - timePassed) / 1000000;
			if(elapsed > 2200){
				setShaking(false, 0);
				setInvisible(true);
				if(loadedMap.contains("level7.map")){
					if(elapsed > 4000){
						setShaking(true, 2);
					}
					
					if(elapsed > 4200){
						setShaking(false, 0);
						setInvisible(false);
						timePassed = System.nanoTime();
						x = xtemp;
						y = ytemp;
					}
				}
			}else if(elapsed > 2000 && !invisible){
				setShaking(true, 2);
			}
		}
	}
	
	public void draw(Graphics2D g){
		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++){
			
			if(row >= numRows) break;
			for(int col = colOffset; col < colOffset + numColsToDraw; col++){
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
//				if(row == 0 || col == 0 || row == numRows-1 || col == numCols-1 || ){
				xtemp = x;
				ytemp = y;
				if(shaking) {
//					x += Math.random() * intensity - intensity / 2;
					y += Math.random() * intensity - intensity / 2;
				}
				if(invisible){
					if(tiles[r][c].getType() == Tile.DAMAGING || tiles[r][c].getType() == Tile.TERMINAL){
						g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);
					}else if(row == 0 || col == 0 || row == numRows-1 || col == numCols-1){
						BufferedImage img;
						
						if(row == 0 && col == 0) img = sides[4];
						else if(row == 0 && col == numCols-1) img = sides[5];
						else if(row == numRows-1 && col == 0) img = sides[2];
						else if(row == numRows-1 && col == numCols-1) img = sides[3];
						else if(row == 0 || row == numRows-1) img = sides[0];
						else img = sides[1];
						
						g.drawImage(img, (int)x + col * tileSize, (int)y + row * tileSize, null);
					}
				}else{
					g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);
				}
			}
		}
		if(fs != null){
			if(!fs.shouldRemove()) fs.draw(g);
		}
	}
}
