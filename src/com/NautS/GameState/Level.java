package com.NautS.GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.NautS.Audio.JukeBox;
import com.NautS.Entity.AmmoBox;
import com.NautS.Entity.DeathExplosion;
import com.NautS.Entity.Enemy;
import com.NautS.Entity.HUD;
import com.NautS.Entity.Medkit;
import com.NautS.Entity.Player;
import com.NautS.Entity.PlayerSave;
import com.NautS.Entity.Enemies.Slugger;
import com.NautS.Main.GamePanel;
import com.NautS.TileMap.Background;
import com.NautS.TileMap.TileMap;

public class Level extends GameState{
	
	protected TileMap tileMap;
	protected Background bg;
	
	protected Player player;
	
	protected Slugger s;
	protected ArrayList<Point> sluggerPoints;
	protected AmmoBox ammoBox;
	protected ArrayList<Point> ammoBoxPoints;
	protected Medkit medkit;
	protected ArrayList<Point> medkitPoints;
	
	protected ArrayList<Enemy> enemies;
	protected ArrayList<DeathExplosion> explosions;
	protected ArrayList<AmmoBox> ammoBoxes;
	protected ArrayList<Medkit> medkits;
	
	protected HUD hud;
	
	protected Point spawn;
	
	// events
	protected boolean blockInput = false;
	protected int eventCount = 0;
	protected boolean eventStart;
	protected ArrayList<Rectangle> tb;
	protected boolean eventFinish;
	protected boolean eventDead;
	
	protected String tileSet;
	protected String map;
	protected String backGround;
	protected String musicDir;
	protected String music;
	
	protected double moveScale;
	protected double tween;
	
	protected boolean testWin = false;
	protected int nextLevel;

	public Level(GameStateManager gsm) {
		super(gsm);
	}

	public void init() {
		tileMap = new TileMap(80);
		tileMap.loadTiles(tileSet);
		tileMap.loadMap(map);
		tileMap.setPosition(0, 0);
		tileMap.setTween(tween);
		
		bg = new Background(backGround, moveScale);
		
		player = new Player(tileMap);
		player.setPosition(spawn.getX(), spawn.getY());
		player.setHealth(PlayerSave.getHealth());
		player.setLives(PlayerSave.getLives());
		player.setTime(PlayerSave.getTime());
		
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<DeathExplosion>();
		ammoBoxes = new ArrayList<AmmoBox>();
		medkits = new ArrayList<Medkit>();
		
		//hud
		hud = new HUD(player);
		
		sluggerPoints = new ArrayList<Point>();
		s = new Slugger(tileMap);
		
		ammoBoxPoints = new ArrayList<Point>();
		ammoBox = new AmmoBox(tileMap);
		
		medkitPoints = new ArrayList<Point>();
		medkit = new Medkit(tileMap);
		
		populateEntities();
		
		JukeBox.load(musicDir, music);
		JukeBox.loop(music, 600, JukeBox.getFrames(music) - 2200);
		
		JukeBox.load("/SFX/wail3.mp3", "explosion");
		JukeBox.load("/SFX/pickup.mp3", "pickup");
		
		//may be necessary for enemy/player attacks and collisions
//		player.init(enemies);
		
		// start event
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
		
	}
	
	protected void populateEntities() {
				
		enemies.clear();
		ammoBoxes.clear();
		medkits.clear();
		
		//enemies
		for(int i = 0; i < sluggerPoints.size(); i++) {
			s = new Slugger(tileMap);
			s.setPosition(sluggerPoints.get(i).x, sluggerPoints.get(i).y);
			enemies.add(s);
		}
		
		//ammoboxes
		for(int i = 0; i < ammoBoxPoints.size(); i++) {
			ammoBox = new AmmoBox(tileMap);
			ammoBox.setPosition(ammoBoxPoints.get(i).x, ammoBoxPoints.get(i).y);
			ammoBoxes.add(ammoBox);
		}
		
		//medkits
		for(int i = 0; i < medkitPoints.size(); i++) {
			medkit = new Medkit(tileMap);
			medkit.setPosition(medkitPoints.get(i).x, medkitPoints.get(i).y);
			medkits.add(medkit);
		}
	}

	public void update() {
		
		//check if end of level event should start
		if(testWin || (int)player.getx() > tileMap.getWidth() - tileMap.getTileSize()) {
			eventFinish = blockInput = true;
		}
				
		// check if player dead event should start
		if(player.getHealth() == 0 || player.gety() >= tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		
		//update playa
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(),
							GamePanel.HEIGHT / 2 - player.gety());
		
		// check if player dead event should start
		if(player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		
		//paralax
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		//attack enemies
		player.checkAttack(enemies);
		player.checkPickUp(ammoBoxes);
		player.checkPickHeal(medkits);
		
		//update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(enemies.get(i).isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new DeathExplosion(e.getx(), e.gety()));
				JukeBox.play("explosion");
			}
		}
		
		//update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		//update ammo boxes
		for(int i = 0; i < ammoBoxes.size(); i++) {
			ammoBoxes.get(i).update();
			if(ammoBoxes.get(i).pickedUp()) {
				JukeBox.play("pickup");
				ammoBoxes.remove(i);
				i--;
			}
		}
		
		//update medkits
			for(int i = 0; i < medkits.size(); i++) {
				medkits.get(i).update();
				if(medkits.get(i).pickedUp()) {
					JukeBox.play("pickup");
					medkits.remove(i);
					i--;
				}
			}
			
		// play events
				if(eventStart) eventStart();
				if(eventDead) eventDead();
				if(eventFinish) eventFinish();		
	}

	public void draw(Graphics2D g) {
		//draw background
		bg.draw(g);
		
		//draw tilemap
		tileMap.draw(g);
		
		//draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		//draw player
		player.draw(g);
		
		//draw ammo boxes
		for(int i = 0; i < ammoBoxes.size(); i++) {
			ammoBoxes.get(i).draw(g);
		}
		
		//draw medkits
		for(int i = 0; i < medkits.size(); i++) {
			medkits.get(i).draw(g);
		}
		
		//draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
			explosions.get(i).setMapPosition(
				(int)tileMap.getx(),
				(int)tileMap.gety());
				JukeBox.stop("explode");
		}
		
		//draw HUD
		hud.draw(g);
		
		// draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for(int i = 0; i < tb.size(); i++) {
			g.fill(tb.get(i));
		}
		
	}

	public void keyPressed(int k) {
		if(blockInput || player.getHealth() == 0) return;
		if(k == KeyEvent.VK_ESCAPE && !gsm.isPaused()) gsm.setPaused(true);
		else if(k == KeyEvent.VK_ESCAPE && gsm.isPaused()) gsm.setPaused(false);
		if(k == KeyEvent.VK_ENTER && !gsm.isPaused()){
			gsm.setPaused(false);
			restart();
			}
		else if(k == KeyEvent.VK_ENTER && gsm.isPaused()){
			gsm.setPaused(false);
			JukeBox.stop(music);
			gsm.setState(GameStateManager.MENUSTATE);
			}
		if(k == KeyEvent.VK_A) player.setLeft(true);
		if(k == KeyEvent.VK_D) player.setRight(true);
		if(k == KeyEvent.VK_W) player.setGliding(true);
		if(k == KeyEvent.VK_S) player.setDown(true);
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setJumping(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_SPACE) player.setJumping(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_CONTROL) player.setPunching();
		if(k == KeyEvent.VK_F) player.setFiring();
		if(k == KeyEvent.VK_R && player.getCurrAction() != 5) player.reload();
		if(k == KeyEvent.VK_0) testWin = true;
		
		if(k == KeyEvent.VK_F3) PlayerSave.debugGOO();
		
		if(k == KeyEvent.VK_1) Player.setCurrentSlot(1);
		if(k == KeyEvent.VK_2) Player.setCurrentSlot(2);
		if(k == KeyEvent.VK_3) Player.setCurrentSlot(3);
		if(k == KeyEvent.VK_4) Player.setCurrentSlot(4);
//		if(k == KeyEvent.VK_ENTER) restart();

	}

	public void keyReleased(int k) {
		if(k == KeyEvent.VK_A) player.setLeft(false);
		if(k == KeyEvent.VK_D) player.setRight(false);
		if(k == KeyEvent.VK_W) player.setGliding(false);
		if(k == KeyEvent.VK_S) player.setDown(false);
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setJumping(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_SPACE) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
//		if(k == KeyEvent.VK_ESCAPE) gsm.setPaused(false);
	}
	
	protected void restart() {
		tileMap.setTween(1);
		tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT));
		player.restart();
		player.setPosition(spawn.getX(), spawn.getY());
		sluggerPoints.clear();
		ammoBoxPoints.clear();
		medkitPoints.clear();
		populateEntities();
		blockInput = true;
		eventCount = 0;
		tileMap.setTween(tween);
		eventStart = true;
		eventStart();
	}
	// level started
	protected void eventStart() {
		eventCount++;
		if(eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
		}
		if(eventCount > 1 && eventCount < 60) {
			tb.get(0).height -= GamePanel.HEIGHT/60;
			tb.get(1).width -= GamePanel.WIDTH/60;
			tb.get(2).y += GamePanel.HEIGHT/60;
			tb.get(3).x += GamePanel.WIDTH/60;
		}
		if(eventCount == 60) {
			eventStart = blockInput = false;
			eventCount = 0;
			tb.clear();
		}
	}
	// player has died
	protected void eventDead() {
		eventCount++;
		if(eventCount == 1) {
			player.setDead();
			player.stop();
		}
		if(eventCount == 60) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount > 60) {
			tb.get(0).x -= GamePanel.WIDTH / 60;
			tb.get(0).y -= GamePanel.HEIGHT / 60;
			tb.get(0).width += GamePanel.WIDTH / 30;
			tb.get(0).height += GamePanel.HEIGHT / 30;
		}
		if(eventCount >= 120) {
			if(player.getLives() == 0) {
				JukeBox.stop(music);
				gsm.setState(GameStateManager.MENUSTATE);
			} else {
				eventDead = blockInput = false;
				eventCount = 0;
				player.loseLife();
				restart();
			}
		}
	}
	
	// finished level
	protected void eventFinish() {
		eventCount++;
		if(eventCount == 1) {
			player.stop();
		}
		else if(eventCount == 120) {
			tb.clear();
			tb.add(new Rectangle(
				GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount > 120) {
			tb.get(0).x -= GamePanel.WIDTH / 60;
			tb.get(0).y -= GamePanel.HEIGHT / 60;
			tb.get(0).width += GamePanel.WIDTH / 30;
			tb.get(0).height += GamePanel.HEIGHT / 30;
		}
		if(eventCount == 180) {
			JukeBox.stop(music);
			PlayerSave.setHealth(player.getHealth());
			PlayerSave.setLives(player.getLives());
			PlayerSave.setTime(player.getTime());
			PlayerSave.setAmmo(player.getAmmo());
			PlayerSave.setClip(player.getMag());
			PlayerSave.setSlot(player.getCurrentSlot());
			gsm.setState(nextLevel + 2);
		}
	}
}
