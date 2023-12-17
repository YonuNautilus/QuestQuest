package com.NautS.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.NautS.Content.Content;
import com.NautS.TileMap.TileMap;

public class GunSmoke extends MapObject {
	
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	
	
	public GunSmoke(TileMap tm, boolean right) {
		
		super(tm);
		
		if(right) {
			facingRight = true;
		}
		else {
			facingRight = false;
		}
		
		width = 20;
		height = 20;
		
		sprites = Content.GunSmoke[0];
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(100);
		
	}
	
	public boolean shouldRemove() { return remove; }

	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public void setMapPosition(int x, int y) {
		xmap = x;
		ymap = y;
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		
		if(facingRight) {
			g.drawImage(animation.getImage(),
					(int)(x + xmap + 35),
					(int)(y + ymap - height - 3),
					width,
					height,
					null);
		}
		else {
			g.drawImage(animation.getImage(),
					(int)(x + xmap - 35),
					(int)(y + ymap - height - 3),
					-width,
					height,
					null);
		}
	}
}