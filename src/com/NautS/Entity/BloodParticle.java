package com.NautS.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.NautS.Content.Content;
import com.NautS.TileMap.TileMap;

public class BloodParticle extends MapObject {
	
	private int count;
	private boolean remove;
	
	private BufferedImage[] sprites;
	
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	public static int CENTER = 4;
	
	public BloodParticle(TileMap tm, int dir) {
		super(tm);
		
		width = 4;
		height = 4;
		
		fallSpeed = .2;
		maxFallSpeed = 7;
		
		double d1 = Math.random() * 2.5 - 1.25;
		double d2 = -Math.random() - 0.8; 
		if(dir == UP) {
			dx = d1;
			dy = d2;
		}
		else if(dir == LEFT) {
			dx = d2;
			dy = d1;
		}
		else if(dir == DOWN) {
			dx = d1;
			dy = -d2;
		}
		else if(dir == RIGHT) {
			dx = -d2;
			dy = d1;
		}
		else if(dir == CENTER) {
			dx = dy = d1;
		}
		
		count = 0;
		sprites = Content.Blood[0];
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	public void update() {
		dy += fallSpeed;
		checkBounceCollision();
		setPosition(xtemp, ytemp);
		animation.update();
		
		count++;
		if(count == 120) remove = true;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void draw(Graphics2D g) {
		super.draw(g);
		setMapPosition();
	}
	
}
