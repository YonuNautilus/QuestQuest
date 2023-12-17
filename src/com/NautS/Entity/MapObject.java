package com.NautS.Entity;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import com.NautS.Main.GamePanel;
import com.NautS.TileMap.Tile;
import com.NautS.TileMap.TileMap;

public abstract class MapObject {
	
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	//position/vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	//dimensions
	protected int width;
	protected int height;
	
	//collision box
	protected int cwidth;
	protected int cheight;
	
	//collision stuff
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	protected boolean thtopLeft;
	protected boolean thtopRight;
	protected boolean thbottomLeft;
	protected boolean thbottomRight;
	
	protected boolean bhtopLeft;
	protected boolean bhtopRight;
	protected boolean bhbottomLeft;
	protected boolean bhbottomRight;
	
	private boolean bbl;
	private boolean bbr;
	
	//animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	//movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	//movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	//shared stats
	protected int health;
	protected int maxHealth;
	protected boolean flinching;
	protected long flinchTimer;
	
	//constructor
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
		animation = new Animation();
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRekt();
		Rectangle r2 = o.getRekt();
		return r1.intersects(r2);
	}
	
	public Rectangle getRekt() {
		return new Rectangle(
			(int)x - cwidth/2,
			(int)y - cheight/2,
			cwidth,
			cheight
		);
	}
	
	public void calculateCorners(double x, double y) {
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
        if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
                leftTile < 0 || rightTile >= tileMap.getNumCols()) {
        		topLeft = topRight = bottomLeft = bottomRight = false;
        		return;
        }
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);
        
        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
        
        int thtopTile = (int)(y - cheight / 4) / tileSize;
        int bhbottomTile = (int)(y + cheight / 4 - 1) / tileSize;
        
        int thtl = tileMap.getType(thtopTile, leftTile);
        int thtr = tileMap.getType(thtopTile, rightTile);
        int thbl = tileMap.getType(bottomTile, leftTile);
        int thbr = tileMap.getType(bottomTile, rightTile);
        
        thtopLeft = thtl == Tile.TOPHALF;
        thtopRight = thtr == Tile.TOPHALF;
        thbottomLeft = thbl == Tile.TOPHALF;
        thbottomRight = thbr == Tile.TOPHALF;
        
        int bhtl = tileMap.getType(topTile, leftTile);
        int bhtr = tileMap.getType(topTile, rightTile);
        int bhbl = tileMap.getType(bhbottomTile, leftTile);
        int bhbr = tileMap.getType(bhbottomTile, rightTile);
        
        bhtopLeft = bhtl == Tile.BOTTOMHALF;
        bhtopRight = bhtr == Tile.BOTTOMHALF;
        bhbottomLeft = bhbl == Tile.BOTTOMHALF;
        bhbottomRight = bhbr == Tile.BOTTOMHALF;
	}
	
	public void checkTileMapCollision() {
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft || topRight || bhtopLeft || bhtopRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			//move into space bellow top-half tiles
			if(thtopLeft || thtopRight) {
				dy = 0;
				ytemp =currRow * tileSize + cheight / 6;
			}
			else { ytemp += dy; }
		}
		if(dy > 0) {
			if (bottomLeft || bottomRight || thbottomLeft || thbottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else { ytemp += dy; }
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft || thbottomLeft || bhtopLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else { xtemp += dx; }
		}
		if(dx > 0) {
			if(topRight || bottomRight || thbottomRight || bhtopRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else { xtemp += dx; }
		}
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(bottomLeft || thbottomLeft) {
				bbl = true;
			} else { bbl = false; }
			if(bottomRight || thbottomRight) {
				bbr = true;
			} else { bbr = false; }
			if(!bbl && !bbr) {
				falling = true;
			}
		}
	}
	
	public void checkEnemyTileMapCollision() {
		calculateCorners(x, ydest + 1);
		if(!bottomLeft && !thbottomLeft) {
			left = false;
			right = facingRight = true;
		}
		if(!bottomRight && !thbottomRight) {
			left = true;
			right = facingRight = false;
		}
		
		setPosition(xtemp,ytemp);
		
		//if hits a wall, turn around
		if(dx == 0) {
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
		
		
	}
	
	public void checkBounceCollision() {
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if(dy < 0) { //if going up...
			if(topLeft || topRight || bhtopLeft || bhtopRight) {
				dy = -.75*dy;
			}
			//move into space below top-half tiles
			if(thtopLeft || thtopRight) {
				dy = -.75*dy;
				ytemp = currRow * tileSize + cheight / 6;
			}
			else { ytemp += dy; }
		}
		if(dy > 0) { // if going down...
			if (bottomLeft || bottomRight || thbottomLeft || thbottomRight) {
				dy = -.75*dy;
			}
			//move into space above bottom-half tiles
			if(bhbottomLeft || bhbottomRight) {
				dy = -.75*dy;
				ytemp = currRow * tileSize - cheight / 6;
			}
			else { ytemp += dy; }
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) { // if going left...
			if(topLeft || bottomLeft || thbottomLeft || bhtopLeft) {
				dx = -dx;
			}
			else { xtemp += dx; }
		}
		if(dx > 0) { // if going right..
			if(topRight || bottomRight || thbottomRight || bhtopRight) {
				dx = -dx;
			}
			else { xtemp += dx; }
		}
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(bottomLeft || thbottomLeft) {
				bbl = true;
			} else { bbl = false; }
			if(bottomRight || thbottomRight) {
				bbr = true;
			} else { bbr = false; }
			if(!bbl && !bbr) {
				falling = true;
			}
		}
	}
	
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getxmap() { return (int)xmap; }
	public int getymap() { return (int)ymap; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	public boolean getRight() { return facingRight; }
	
	
	public void setPosition (double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GamePanel.HEIGHT;
	}
	
//	public static String toString(int i)
	
	public void draw(java.awt.Graphics2D g) {
		setMapPosition();
		if(facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
		}
		else {
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2),
						-width, height, null);
		}
		//Debug stuff
		if(PlayerSave.debugG()){
			g.setColor(new Color(0, 0, 0, 90));
			g.fillRect((int)(x+xmap-(width/2)), (int)(y+ymap-(height/2)), width, height);
			g.setColor(new Color(250, 250, 250));
			g.fillRect((int)(x + xmap-3), (int)(y + ymap-3), 6, 6);
			g.setColor(Color.RED);
			g.drawRect((int)(x+xmap-(width/2)), (int)(y+ymap-(height/2)), width, height);
			g.setColor(Color.GREEN);
			g.drawLine((int)(x+xmap-(width/2)), (int)(y+ymap), (int)(x+xmap+(width/2)), (int)(y+ymap));
			g.drawLine((int)(x+xmap), (int)(y+ymap-(height/2)), (int)(x+xmap), (int)(y+ymap)+(height/2));
			g.setColor(Color.CYAN);
			g.draw(new Rectangle2D.Double((int)(x+xmap) - (cwidth/2),(int)(y+ymap) - (cheight/2), cwidth, cheight));
			g.draw(new Rectangle2D.Double((int)(x+xmap) - cwidth/2, (int)(y+ymap) - cheight/2, cwidth, cheight));
		}
	}
}