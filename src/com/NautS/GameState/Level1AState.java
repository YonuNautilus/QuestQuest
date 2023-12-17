package com.NautS.GameState;

import java.awt.Point;
import java.util.Arrays;

public class Level1AState extends Level {
	
	public Level1AState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	public void init() {
		
		nextLevel = 2;
		
		spawn = new Point(200, 440);
		music = "takeOnMe";
		musicDir = "/Music/TakeOnMe.mp3";
		testWin = false;
		
		tileSet = "/Tilesets/tileset1.gif";
		map = "/Maps/level1.map";
		tween = .07;
		backGround = "/Backgrounds/grassbg1.gif";
		moveScale = 0.1;
		
		super.init();
		
		//may be necessary for enemy/player attacks and collisions
//		player.init(enemies);
		
	}
	
	protected void populateEntities() {
		
		sluggerPoints.addAll(Arrays.asList(
			new Point(1400, 300),
			new Point(1700, 300),
			new Point(1900, 300),
			new Point(300, 280)
		));
		
		ammoBoxPoints.add(new Point(340, 280));
		ammoBoxPoints.add(new Point(400, 280));
		
		medkitPoints.add(new Point(280, 280));
		
		super.populateEntities();
	}
	
}