package com.NautS.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.NautS.TileMap.TileMap;

public class AmmoBox extends MapObject{
	
	private BufferedImage[] sprites;
	
	public int ammo;
	public boolean pickUp;
	
	double d = Math.random();
	
	public boolean pickedUp() { return pickUp; }
	
	public int getAmount() { return ammo; }
	
	public AmmoBox(TileMap tm) {
		
		super(tm);
		
		width = 80;
		height = 80;
		cwidth = 32;
		cheight = 23;
		
		moveSpeed = 0;
		maxSpeed = 0;
		fallSpeed = .15;
		maxFallSpeed = 7;
		
		if (d < 0.5) {
			 // 50% chance of being here
			ammo = 10;
		}
		   
		else if (d < 0.85) {
		    // 35% chance of being here	
			ammo = 15;
		}
		else {
		    // 15% chance of being here	
			ammo = 30;
		}
		
		//load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/ammobox.gif"));
			
			sprites = new BufferedImage[2];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width, 0, width, height);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(500);
		
		right = true;
		facingRight = true;
		pickUp = false;
	}
	
	private void getNextPosition() {
		//falling
		if(falling) {
			dy += fallSpeed;
		}
	}
	
	
	public void update() {
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
		
	}
	
}
