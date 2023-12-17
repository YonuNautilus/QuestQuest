package com.NautS.Content;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

//this class loads resources on boot.
//spritesheets are taken from here.

public class Content {
	
	public static BufferedImage[][] Blood = load("/Sprites/blood.gif", 4, 4);
	
	public static BufferedImage[][] Player = load("/Sprites/Player/playersprites.gif", 80, 80);
	public static BufferedImage[][] Bullet = load("/Sprites/Player/bullet.gif", 80, 80);
	public static BufferedImage[][] GunSmoke = load("/Sprites/Player/gunsmoke.gif", 20, 20);
	
	public static BufferedImage[][] AmmoBox = load("/Sprites/ammobox.gif", 80, 80);
	public static BufferedImage[][] Medkit = load("/Sprites/medkit.gif", 80, 80);
	
	public static BufferedImage[][] Slugger = load("/Sprites/Enemies/slugger.gif", 80, 80);
	public static BufferedImage[][] DeathExplosion = load("/Sprites/Enemies/explosion.gif", 80, 80);
	
	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
}
