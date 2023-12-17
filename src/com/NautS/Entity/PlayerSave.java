package com.NautS.Entity;

import java.util.ArrayList;

import com.NautS.Items.Weapon;

public class PlayerSave {
	
//	private static Player player;
	
	private static int lives = 3;
	private static int health = 5;
	private static long time = 0;
	private static int ammo = 30;
	private static int clip = 6;
	private static int character = 0;
	private static int slot = 0;
	
	private static ArrayList<Weapon> slot2;
	
	private static boolean debugG = false;
	
	public static void init() {
		lives = 3;
		health = 5;
		time = 0;
	}
	
	public static int getCharacter() { return character; }
	public static void setCharacter(int c) { character = c; }
	
	public static int getLives() { return lives; }
	public static void setLives(int l) { lives = l; }
	
	public static int getHealth() { return health; }
	public static void setHealth(int h) { health = h; }
	
	public static long getTime() { return time; }
	public static void setTime(long t) { time = t; }
	
	public static int getAmmo() { return ammo; }
	public static void setAmmo(int a) { ammo = a; }
	
	public static int getClip() { return clip; }
	public static void setClip(int c) { clip = c; }
	
	public static int getSlot() { return slot; }
	public static void setSlot(int s) { slot = s; }
	
	public static boolean debugG() { return debugG; }
	public static void debugGOO() {
		if (debugG) {
			debugG = false;
		} else {
			debugG = true;
		}
	}

}
