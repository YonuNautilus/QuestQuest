package com.NautS.Entity;

import java.awt.image.BufferedImage;
import java.awt.*;
import javax.imageio.ImageIO;

import com.NautS.Audio.JukeBox;
import com.NautS.TileMap.TileMap;

public class Bullet extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;

	
	public Bullet(TileMap tm, boolean right) {
		
		super(tm);
		
		moveSpeed = 15;
		if(right) {
			dx = moveSpeed;
			facingRight = true;
		}
		else {
			dx = -moveSpeed;
			facingRight = false;
		}
		
		width = 80;
		height = 80;
		cwidth = 40;
		cheight = 1;
		
		JukeBox.load("/SFX/buzz.mp3", "bullethit");
		
		//load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Player/bullet.gif"));
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spritesheet.getSubimage(i * width, height, width, height);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(50);
		dx = 0;
		if(notOnScreen()) {}
		else {JukeBox.play("bullethit");}
		
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(dx == 0 && !hit) {
			setHit();
		}
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}	
	}
	
	public void draw(Graphics2D g) {	
		setMapPosition();
		super.draw(g);
	}
}