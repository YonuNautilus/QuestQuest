package com.NautS.Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.NautS.Main.GamePanel;

public class HUD {
	
	private Player player;
	private BufferedImage lifeBar;
	private BufferedImage deathBar;
	private BufferedImage healthBar;
	private BufferedImage clipBar;
	private BufferedImage ammoBar;
	private BufferedImage hotbar;
	private BufferedImage select;
	private BufferedImage w1_1;
	private Font font;
	private Color color;
	private Font font2;
	private Color color2;
	
	public HUD(Player p) {
		player = p;
		try {
			lifeBar = ImageIO.read(getClass().getResourceAsStream("/HUD/lifebar.gif"));
			
			deathBar = ImageIO.read(getClass().getResourceAsStream("/HUD/death.gif"));
			healthBar = ImageIO.read(getClass().getResourceAsStream("/HUD/healthbar.gif"));
		
			ammoBar = ImageIO.read(getClass().getResourceAsStream("/HUD/ammobar.gif"));
			
			hotbar = ImageIO.read(getClass().getResourceAsStream("/HUD/hotbar.gif"));
			select = ImageIO.read(getClass().getResourceAsStream("/HUD/select.gif"));
			
			w1_1 = ImageIO.read(getClass().getResourceAsStream("/Weapons/twinRevolver.gif"));
			
			font2 = new Font("Arial", Font.BOLD, 14);
			color2 = new Color(255, 0, 0);
			
			font = new Font("Arial", Font.PLAIN, 14);
			color = new Color(255, 255, 0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		
		try {
			clipBar = ImageIO.read(getClass().getResourceAsStream("/HUD/clipbar" + player.getMag() + ".gif"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		g.setFont(font);
		g.setColor(color);
		
		String lives = Integer.toString(player.getLives());
		int livesWidth = g.getFontMetrics().stringWidth(lives);
		
		g.drawImage(lifeBar, 0, 10, null);
		g.drawString("Lives:", 88, 24);
		
		g.drawImage(deathBar, 0, 10, null);
		g.drawImage(healthBar,
				0,
				10,
				74 * player.getHealth()/player.getMaxHealth(),
				30,
				0,
				0,
				74 * player.getHealth()/player.getMaxHealth(),
				20,
				null);
		g.drawImage(clipBar, 0, 10, null);
		g.drawImage(ammoBar, 0, 10, null);
		
		String health = player.getHealth() + "/" + player.getMaxHealth();
		int width = g.getFontMetrics().stringWidth(health);
		g.drawString(health, (74/2) - width/2, 25);
		
		String mag = player.getMag() + "/" + player.getMagSize();
		int width1 = g.getFontMetrics().stringWidth(mag);	
		g.drawString(mag, 74/2 - width1/2, 46);
		
		String ammo = player.getAmmo() + "/" + player.getMaxAmmo();
		int width2 = g.getFontMetrics().stringWidth(ammo);
		g.drawString(ammo, 74/2 - width2/2, 67);
		
		g.setFont(font2);
		g.setColor(color2);
		
		g.drawString(lives, 152 - livesWidth, 24);
		
		int hbheight = hotbar.getHeight();
		int hbwidth = hotbar.getWidth();
		
		g.drawImage(hotbar, (GamePanel.WIDTH - hbwidth) / 2, GamePanel.HEIGHT - (hbheight), null);
		g.drawImage(select, ((GamePanel.WIDTH - hbwidth) / 2) + (((hbwidth/4)-1)*(player.getCurrentSlot()-1)),
					GamePanel.HEIGHT - (hbheight), null);
		g.drawImage(w1_1, ((GamePanel.WIDTH - hbwidth) / 2)+5,  GamePanel.HEIGHT - (hbheight)+5, null);
		
	}
	
}
