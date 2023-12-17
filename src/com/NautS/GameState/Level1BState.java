package com.NautS.GameState;

import java.awt.Point;
import java.util.Arrays;

public class Level1BState extends Level {
	
	public Level1BState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	public void init() {
		
		nextLevel = 3;
		
		spawn = new Point(120, 200);
		music = "pushlimit";
		musicDir = "/Music/PushLimit.mp3";
		testWin = false;
		
		tileSet = "/Tilesets/tileset2.gif";
		map = "/Maps/level1a.map";
		tween = .07;
		backGround = "/Backgrounds/metalbg1.gif";
		moveScale = 0.8;
		
		super.init();
		
	}
	
	protected void populateEntities() {
		
		sluggerPoints.addAll(Arrays.asList(
			new Point(200, 100)
		));
		
		ammoBoxPoints.add(new Point(160, 80));
		
		medkitPoints.add(new Point(200, 80));
		
		super.populateEntities();
	}
	
}