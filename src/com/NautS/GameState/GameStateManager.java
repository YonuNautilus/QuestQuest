package com.NautS.GameState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;

import com.NautS.Audio.JukeBox;
import com.NautS.Main.GamePanel;

public class GameStateManager {
	
	private Font font2;
	private Font font;

	private GameState[] gameStates;
	private int currentState;
	
	private PauseState pauseState;
	private boolean paused;
	private Composite opaque;
	private Composite alpha;
	
//	public static boolean player1;
//	public static boolean player2;
	
	public static final int NUMGAMESTATES = 5;
	public static final int MENUSTATE = 0;
	public static final int HELPSTATE = 1;
	public static final int PLAYERSELECTSTATE = 2;
	public static final int LEVEL1ASTATE = 3;
	public static final int LEVEL1BSTATE = 4;
	
	public GameStateManager () {
		
		JukeBox.init();
		
		gameStates = new GameState[NUMGAMESTATES];
		
		pauseState = new PauseState(this);
		paused = false;
		
		currentState = MENUSTATE;
		loadState(currentState);
		
		font = new Font("Century Gothic", Font.PLAIN, 28);
		font2 = new Font("Times New Roman", Font.PLAIN, 10);
		alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f);
		opaque = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
		
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == HELPSTATE)
			gameStates[state] = new HelpState(this);
		if(state == PLAYERSELECTSTATE)
			gameStates[state] = new PlayerSelectMenuState(this);
		if(state == LEVEL1ASTATE)
			gameStates[state] = new Level1AState(this);
		if(state == LEVEL1BSTATE)
			gameStates[state] = new Level1BState(this);
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public void setPaused(boolean b) { paused = b; }
	
	public boolean isPaused() { return paused; }
	
	public void update() {
		if(paused) {
			pauseState.update();
			return;
		}
		if(gameStates[currentState] != null) gameStates[currentState].update();
	}
	
	public void draw(java.awt.Graphics2D g) {
		g.setComposite(opaque);
		
		if(paused) {
			pauseState.draw(g);
			return;
		}
		if(gameStates[currentState] != null) gameStates[currentState].draw(g);
		else {
			g.setColor(java.awt.Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			g.setColor(Color.WHITE);
			g.setFont(font);
			String loading = "Loading";
			int width = g.getFontMetrics().stringWidth(loading)/2;
			g.drawString(loading, (GamePanel.WIDTH/2) - width, GamePanel.HEIGHT/2);
		}
		g.setFont(font2);
		g.setColor(Color.BLACK);
		g.setComposite(alpha);
		String version = "Alpha 0.1.2";
		int wide = g.getFontMetrics().stringWidth(version) + 5;
		int rect = wide + 5;
		g.fillRect(GamePanel.WIDTH - rect, 0, rect, 13);
		g.setComposite(opaque);
		g.setColor(Color.white);
		g.drawString(version, GamePanel.WIDTH - wide, 10);
	}
	
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
}
