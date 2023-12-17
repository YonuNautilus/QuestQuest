package com.NautS.GameState;

import java.awt.Graphics2D;

import com.NautS.Audio.JukeBox;

public abstract class GameState {
	
	protected GameStateManager gsm;
	
	public JukeBox bgMusic;
	
	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	
}
