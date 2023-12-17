package com.NautS.Entity;

import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.NautS.Audio.JukeBox;
import com.NautS.TileMap.*;

public class Player extends MapObject{
	
	public static boolean player1;
	public static boolean player2;
	
	public static int currentSlot = 1;
	private int[] barSlot = {0, 1, 2, 3};
	
	// player stuff
	private int mag;
	private int ammo;
	private int maxAmmo;
	private int maxMagSize;
	private int lives;
	private boolean knockback;
	private boolean reloading;
	private long reloadTime;
	
	private long time;
	
	//references
	private ArrayList<Enemy> enemies;
	private ArrayList<BloodParticle> blood;
	
	//bullet
	private boolean shooting;
	private double ammoCost;
	private int bulletDamage;
	private ArrayList<Bullet> bullets;
	private ArrayList<GunSmoke> gunSmoke;
	
	//punching
	private boolean punching;
	private int punchDamage;
	private int punchRange;
	
	//gliding
	private boolean gliding;
	
	//animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		2, 6, 1, 3, 4, 6, 4, 1, 1
	};
	
	//animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int SHOOTING = 5;
	private static final int PUNCHING = 6;
	private static final int DEAD = 7;
	private static final int KNOCKBACK = 8;
	
	public Player(TileMap tm) {
		
		super(tm);
		
		if (PlayerSave.getCharacter() == 1) {
// CHARACTER 2
			width = 80;
			height = 80;
			cwidth = 36;
			cheight = 70;
			moveSpeed = .1;
			maxSpeed = 4;
			stopSpeed = .5;
			fallSpeed = .15;
			maxFallSpeed = 7;
			jumpStart = -6;
			stopJumpSpeed = .3;
			facingRight = true;
			health = maxHealth = 7;
			mag = maxMagSize = 6;
			ammo = 50;
			maxAmmo = 100;
			bulletDamage = 5;
			punchDamage = 5;
			punchRange = 100;
			//load sprites
			try {
				BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/Player/playersprites.gif")
				);
				sprites = new ArrayList<BufferedImage[]>();
				for(int i = 0; i < numFrames.length; i++) {
					BufferedImage[] bi = new BufferedImage[numFrames[i]];
					for(int j = 0; j < numFrames [i]; j++) {
						if(i != PUNCHING) {
							bi[j] = spritesheet.getSubimage(j * width,
									i * height, width, height);
						} else {
							bi[j] = spritesheet.getSubimage(j * (width + 20),
									i * height, width + 20, height);
						}
					}
					sprites.add(bi);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			animation = new Animation();
			currentAction = IDLE;
			animation.setFrames(sprites.get(IDLE));
			animation.setDelay(400);
		}
		else {
// CHARACTER 1
		width = 80;
		height = 80;
		cwidth = 36;
		cheight = 70;
		moveSpeed = .5;
		maxSpeed = 5;
		stopSpeed = .4;
		fallSpeed = .3;
		maxFallSpeed = 9;
		jumpStart = -8;
		stopJumpSpeed = .3;
		facingRight = true;
		health = maxHealth = 5;
		mag = maxMagSize = 6;
		ammo = 50;
		maxAmmo = 100;
		bulletDamage = 5;
		punchDamage = 8;
		punchRange = 100;
		//load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Player/playersprites.gif")
			);
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < numFrames.length; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames [i]; j++) {
					if(i != PUNCHING) {
						bi[j] = spritesheet.getSubimage(j * width,
								i * height, width, height);
					} else {
						bi[j] = spritesheet.getSubimage(j * (width + 20),
								i * height, width + 20, height);
					}
				}
				sprites.add(bi);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		}
		
		lives = 3;
		ammoCost = 1;
		
		JukeBox.load("/SFX/jump2.mp3", "playerjump");
		JukeBox.load("/SFX/pop.mp3", "playerfire");
		JukeBox.load("/SFX/poof.mp3", "playerpunch");
		JukeBox.load("/SFX/click.mp3", "click");
		JukeBox.load("/SFX/wail4.mp3", "playerhit");
		JukeBox.load("/SFX/reload1.mp3", "reload1");
		JukeBox.load("/SFX/reload2.mp3", "reload2");
		JukeBox.load("/SFX/reload3.mp3", "reload3");
		JukeBox.load("/SFX/reload4.mp3", "reload4");
		JukeBox.load("/SFX/reload5.mp3", "reload5");
		JukeBox.load("/SFX/reload6.mp3", "reload6");
		
		blood = new ArrayList<BloodParticle>();
		bullets = new ArrayList<Bullet>();
		gunSmoke = new ArrayList<GunSmoke>();
		
	}
	
	public void init(
		ArrayList<Enemy> enemies) {
			this.enemies = enemies;
	}
	
	public int getHealth() { return health;}
	public int getMaxHealth() { return maxHealth; }
	public int getMag() { return mag; }
	public int getMagSize() { return maxMagSize; }
	public int getAmmo() { return ammo; }
	public int getMaxAmmo() { return maxAmmo; }
	public int getCurrAction() { return currentAction; }
	public int getLives() { return lives; }
	public void setLives(int l) { lives = l; }
	public void gainLife() { lives++; }
	public void loseLife() { lives--; }
	public void setHealth(int h) { health = h; }
	public void setTime(long t) { time = t; }
	public long getTime() { return time; }
	public void setMag(int m) { mag = m; }
	public void setAmmo(int a) { ammo = a; }
	
	public void setFiring() {
		if(knockback) return;
		shooting = true;
	}
	public void setPunching() {
		if(knockback) return;
		punching = true;
	}
	public void setGliding (boolean b) {
		if(knockback) return;
		gliding = b;
	}
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		//loop through enemies
		for(int i = 0; i < enemies.size(); i ++) {
			
			Enemy e = enemies.get(i);
			
			//punch attack check
			if(punching) {
				if(facingRight) {
					if(
						e.getx() > x &&
						e.getx() < x + punchRange &&
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(punchDamage);
					}
				}
				else {
					if(
						e.getx() < x &&
						e.getx() > x - punchRange &&
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(punchDamage);
					}
				}
			}
			
			//bullet collision check 
			for(int j = 0; j < bullets.size(); j++) {
				if(bullets.get(j).intersects(e)) {
					e.hit(bulletDamage);
					bullets.get(j).setHit();
					break;
				}
			}
			
			//check enemy collision
			if(intersects(e)) {
					hit(e.getDamage(), e);
			}
		}
	}
	
	public void hit(int damage, Enemy e) {
		if(flinching) return;
		JukeBox.play("playerhit");
		health -= damage;
		if(health < 0) health = 0;
		flinching = true;
		flinchTimer = System.nanoTime();
		if ((int)(e.getx() + e.getxmap()) >= (int)(x + xmap)) {
			facingRight = true;
			dx = -2;
			for(int i = 0; i < 6; i++) {
				blood.add(new BloodParticle(
					tileMap,
					BloodParticle.RIGHT));
			}
		} else {
			facingRight = false;
			dx = 2;
			for(int i = 0; i < 6; i++) {
				blood.add(
					new BloodParticle(
						tileMap,
						BloodParticle.LEFT));
			}
		}
		for(int b = 0; b < blood.size(); b++) {
			blood.get(b).setPosition(x, y);
		}
		dy = -4;
		knockback = true;
		falling = true;
		jumping = false;
	}
	
	public void checkPickUp(ArrayList<AmmoBox> ammoBoxes) {
		
		//loop through enemies
		for(int i = 0; i < ammoBoxes.size(); i ++) {
			
			AmmoBox a = ammoBoxes.get(i);
			
			//check enemy collision
			if(intersects(a)) {
				pickUp(a.getAmount());
				a.pickUp = true;
			}
		}
	}
	
	public void checkPickHeal(ArrayList<Medkit> medkits) {
		
		//loop through enemies
		for(int i = 0; i < medkits.size(); i ++) {
			
			Medkit m = medkits.get(i);
			
			//check enemy collision
			if(intersects(m)) {
				pickHeal(m.getHealing());
				m.pickUp = true;
			}
		}
	}
	
	public void pickUp(int amount) {
		ammo += amount;
		if(ammo > maxAmmo) {
			ammo = maxAmmo;
		}
	}
	
	public void pickHeal(int heal) {
		health += heal;
		if(health > maxHealth) {
			health = maxHealth;
		}
	}
	
	private void getNextPosition() {
		
		if(knockback) {
			dy += fallSpeed * 2;
			if(!falling) knockback = false;
			return;
		}
		
		//movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		//cannot move while attack 'cept in air m8
		if(
			(currentAction == PUNCHING || currentAction == SHOOTING) &&
			!(jumping || falling)) {
				dx = 0;
		}
		
		//jumping
		if(jumping && !falling) {
			JukeBox.play("playerjump");
			dy = jumpStart;
			falling = true;
		}
		
		//falling
		if(falling) {
			
			if(dy > 0 && gliding){
				dy += fallSpeed * .1;
			}
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}
	
	public void reload() {
		double d = Math.random();
		if(mag > maxMagSize) {
			mag = maxMagSize;
			reloading = false;
		}
		if(ammo <= 0) {
			ammo = 0;
			reloading = false;
		}
		else if(ammo > 0 && mag < maxMagSize){
			mag += 1;
			ammo -= 1;
			reloadTime = System.nanoTime();
			reloading = true;
			if (d < 0.2) {
				 // 20% chance of being here
				 JukeBox.play("reload1");
			} else if (d < 0.4) {
			    // 20% chance of being here
				 JukeBox.play("reload2");
			} else if (d < .6) {
			    // 20% chance of being here
				 JukeBox.play("reload3");
			} else if (d < .8) {
			    // 20% chance of being here
				 JukeBox.play("reload4");
			} else {
			    // 20% chance of being here
				 JukeBox.play("reload5");
//			} else {
//			    // 19% chance of being here
//				 JukeBox.play("reload6");
			}
		}
	}

	public void update() {
		
		//update pos
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check attack has stopped
		if(currentAction == PUNCHING) {
			if(animation.hasPlayedOnce()) punching = false;
		}
		if(currentAction == SHOOTING) {
			if(animation.hasPlayedOnce()) shooting = false;
		}
		
		//gun attack
		if(shooting && currentAction != SHOOTING) {
			if(mag >= ammoCost) {
				JukeBox.play("playerfire");
				mag -= ammoCost;
				
				Bullet bt = new Bullet(tileMap, facingRight);
				bt.setPosition(x, y-14);
				bullets.add(bt);
	
				//BLOOD DEBUGGING
				
//				BloodParticle bp = new BloodParticle(tileMap, BloodParticle.UP);
//				bp.setPosition(x, y);
//				blood.add(bp);
				
				GunSmoke gs = new GunSmoke(tileMap, facingRight);
				gs.setPosition(x, y);
				gunSmoke.add(gs);
				
				reloadTime = System.nanoTime();
				reloading = true;
			}
			else {
				JukeBox.play("click");
			}
		}
		
		
		if(reloading) {
			long elapsed = (System.nanoTime() - reloadTime) / 1000000;
			if(elapsed > 2000) {
				reload();
			}
		}

		//update bullets/smoke
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
			if(bullets.get(i).shouldRemove()) {
				bullets.remove(i);
				i--;
			}
		}
		for(int i = 0; i < gunSmoke.size(); i++) {
			gunSmoke.get(i).update();
			if(gunSmoke.get(i).shouldRemove()) {
				gunSmoke.remove(i);
				i--;
			}
		}
		
		//check done flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 2000) {
				flinching = false;
			}
		}
		
		//blood particles
		for(int i = 0; i < blood.size(); i ++) {
			blood.get(i).update();
			if(blood.get(i).shouldRemove()) {
				blood.remove(i);
				i--;
			}
		}
		
		//set animation in order of priority
		if(knockback) {
			if(currentAction != KNOCKBACK) {
				animation.setFrames(sprites.get(KNOCKBACK));
			}
		}
		else if(health == 0) {
			if(currentAction != DEAD){
				animation.setFrames(sprites.get(DEAD));

			}
		}
		else if(punching) {
			if(currentAction != PUNCHING) {
				JukeBox.play("playerpunch");
				currentAction = PUNCHING;
				animation.setFrames(sprites.get(PUNCHING));
				animation.setDelay(50);
			}
		}
		else if (shooting) {
			if(currentAction != SHOOTING) {
				currentAction = SHOOTING;
				animation.setFrames(sprites.get(SHOOTING));
				animation.setDelay(75);
			}
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
				}
			}
			else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
			}
		}
		else if (dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(175);
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
			}
		}
		
		width = 80;
		
		if(punching && !facingRight) {
			width = 100;
		}
		else {
			width = 80;
		}
		
		animation.update();
		
		//set direction
		if(!punching && !shooting && !knockback) {
			if (right) facingRight = true;
			if (left) facingRight = false;
		}
	}
	
	public static void setCurrentSlot(int s) {
		currentSlot = s;
	}
	public int getCurrentSlot() { return currentSlot; }
	
	public void setDead() {
		health = 0;
		stop();
	}
	
	public void restart() {
		health = PlayerSave.getHealth();
		ammo = PlayerSave.getAmmo();
		mag = PlayerSave.getClip();
		facingRight = true;
		currentAction = -1;
		stop();
	}
	
	public void stop() {
		left = right = up = down = flinching = jumping = punching = shooting = gliding = false;
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		//draw bullets
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);
		}
		for(int i = 0; i < gunSmoke.size(); i++) {
			gunSmoke.get(i).draw(g);
		}
		
		//draw player
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 100000;
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}
		if (!punching){
			if(facingRight) {
				g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
			}
			else {
				g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2),
							(-1 * width), height, null);
			}
		}
		else if (punching) {
			if(facingRight) {
				g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
			}
			else {
				g.drawImage(animation.getImage(), (int)(x + xmap - width / 2) + 90, (int)(y + ymap - height / 2),
							(-1 * width), height, null);
			}
		}
		
		//draw blood
		for(int i = 0; i <blood.size(); i++) {
			blood.get(i).draw(g);
		}
		//Debug stuff
		if(PlayerSave.debugG()){
			g.setColor(new Color(0, 0, 0, 90));
			g.fillRect((int)(x+xmap-(width/2)), (int)(y+ymap-(height/2)), width, height);
			g.setColor(new Color(250, 250, 250));
			g.fillRect((int)(x + xmap-3), (int)(y + ymap-3), 6, 6);
			g.setColor(Color.RED);
			g.drawRect((int)(x+xmap-(width/2)), (int)(y+ymap-(height/2)), width, height);
			g.setColor(Color.GREEN);
			g.drawLine((int)(x+xmap-(width/2)), (int)(y+ymap), (int)(x+xmap+(width/2)), (int)(y+ymap));
			g.drawLine((int)(x+xmap), (int)(y+ymap-(height/2)), (int)(x+xmap), (int)(y+ymap)+(height/2));
			g.setColor(Color.CYAN);
			g.draw(new Rectangle2D.Double((int)(x+xmap) - (cwidth/2),(int)(y+ymap) - (cheight/2), cwidth, cheight));
			
			g.setFont(new Font("Times New Roman", Font.PLAIN, 10));
			g.setColor(Color.BLACK);
			String xco = String.valueOf((int)Math.round(x));
			String yco = String.valueOf((int)Math.round(y));
			String xcop = String.valueOf((int)Math.round(x)/80);
			String ycop = String.valueOf((int)Math.round(y)/80);
			String coords = xco + ", " + yco;
			String fCoords = xcop + ", " + ycop;
			int coordsWidth = g.getFontMetrics().stringWidth(coords)/2;
			int wide = 2*coordsWidth + 2;
			int rect = wide + 5;
			g.fillRect((int)(x+xmap) - wide/2 ,(int)(y+ymap)-(height/2)-15, rect, 13);
			g.setColor(Color.white);
			g.drawString(coords, (int)(x+xmap) - coordsWidth, (int)(y+ymap)-(height/2)-5);
			int fCoordsWidth = g.getFontMetrics().stringWidth(fCoords)/2;
			int fWide = 2*fCoordsWidth + 2;
			int fRect = fWide + 5;
			g.setColor(Color.BLACK);
			g.fillRect((int)(x+xmap) - fWide/2 ,(int)(y+ymap)-(height/2)-30, fRect, 13);
			g.setColor(Color.white);
			g.drawString(fCoords, (int)(x+xmap) - fCoordsWidth, (int)(y+ymap)-(height/2)-20);
		}
	}
}