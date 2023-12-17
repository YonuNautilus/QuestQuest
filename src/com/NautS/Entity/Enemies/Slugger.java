package com.NautS.Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
//import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.NautS.Entity.*;
import com.NautS.TileMap.TileMap;

public class Slugger extends Enemy {
	
	private BufferedImage[] sprites;
	
//	private ArrayList<BloodParticle> blood;
	
	public Slugger(TileMap tm) {
		
		super(tm);
		
		smallHUD = true;
		
		moveSpeed = 1;
		maxSpeed = 1;
		fallSpeed = 1;
		maxFallSpeed = 10;
		
		width = 80;
		height = 80;
		cwidth = 54;
		cheight = 67;
		
		health = maxHealth = 10;
		damage = 1;
		
		//load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/Enemies/slugger.gif"));
			
			sprites = new BufferedImage[4];
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
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
		
//		blood = new ArrayList<BloodParticle>();
		
	}
	
	private void getNextPosition() {
		if(left) dx = -moveSpeed;
		else if(right) dx = moveSpeed;
		else dx = 0;
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void update() {
		
		//check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		checkEnemyTileMapCollision();
		
		animation.update();
		
	}
	

	public void draw(Graphics2D g) {
//		setMapPosition();
		super.drawHUD(g);
		super.draw(g);
	}
	
}