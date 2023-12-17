package com.NautS.Entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.NautS.Audio.JukeBox;
import com.NautS.TileMap.TileMap;

public class Enemy extends MapObject{
	
	protected boolean smallHUD;
	
	protected boolean dead;
	protected int damage;
	
	public Enemy(TileMap tm) {
		super(tm);
		
		JukeBox.load("/SFX/boom.mp3", "enemyHit");
	}
	
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	
	public int getHealth() { return health; }
	
	public int getMaxHealth() { return maxHealth; }
	
	public void hit(int damage) {
		if (dead || flinching) return;
		health -= damage;
		JukeBox.play("enemyHit");
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void drawHUD(Graphics2D g) {
		if(smallHUD) {
			Color red = new Color(178, 25, 0);
			g.setColor(red);
			g.fillRect((int)(x + xmap - width / 2), (int)(y + ymap - tileSize / 2), width, 6);
			
			Color dRed = new Color(120, 17, 0);
			g.setColor(dRed);
			g.fillRect((int)(x + xmap - width / 2), (int)(y + ymap - tileSize / 2) + 5, width, 1);
			
			Color lRed = new Color(99, 14, 0);
			g.setColor(lRed);
			g.fillRect((int)(x + xmap - width / 2), (int)(y + ymap - tileSize / 2), width * getHealth() / getMaxHealth(), 1);
			g.fillRect((int)(x + xmap + width / 2), (int)(y + ymap - tileSize / 2), 1, 6);
			
			Color green = new Color(52, 178, 0);
			g.setColor(green);
			g.fillRect((int)(x + xmap - width / 2), (int)(y + ymap - tileSize / 2), width * getHealth() / getMaxHealth(), 6);
			
			Color dGreen = new Color(34, 115, 0);
			g.setColor(dGreen);
			g.fillRect((int)(x + xmap - width / 2), (int)(y + ymap - tileSize / 2) + 5, width * getHealth() / getMaxHealth(), 1);
			g.fillRect((int)(x + xmap - width / 2), (int)(y + ymap - tileSize / 2), 1, 6);
			
			Color lGreen = new Color(60, 204, 0);
			g.setColor(lGreen);
			g.fillRect((int)(x + xmap - width / 2), (int)(y + ymap - tileSize / 2), width * getHealth() / getMaxHealth(), 1);
			if (getHealth()/getMaxHealth() == 1){
				g.fillRect((int)(x + xmap + width / 2), (int)(y + ymap - tileSize / 2), 1, 6);
			}
		}
	}
	
	public void update() {}

}
