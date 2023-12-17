package com.NautS.GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.NautS.GameState.GameStateManager;
import com.NautS.Main.GamePanel;

public class PauseState extends GameState {
	
	private Font font;
	private Font font2;
	
	public PauseState(GameStateManager gsm) {
		
		super(gsm);
		
		//fonts
		font = new Font("Century Gothic", Font.PLAIN, 40);
		font2 = new Font("Century Gothic", Font.PLAIN, 15);
	}
	
	public void init() {}
	
	public void update() {}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		String paused = "Game Paused";
		int width = g.getFontMetrics().stringWidth(paused)/2;
		g.drawString(paused, (GamePanel.WIDTH / 2) - width, GamePanel.HEIGHT / 2);
		
		g.setFont(font2);
		String paused2 = "Return to game - ESC";
		int width2 = g.getFontMetrics().stringWidth(paused2)/2;
		g.drawString(paused2, (GamePanel.WIDTH / 2) - width2, (GamePanel.HEIGHT / 2) + 20);
		
		String paused3 = "Return to Menu - ENTER";
		int width3 = g.getFontMetrics().stringWidth(paused3)/2;
		g.drawString(paused3, (GamePanel.WIDTH / 2) - width3, (GamePanel.HEIGHT / 2) + 40);

	}
	
	public void keyPressed(int k) {}
	
	public void keyReleased(int k) {}
}
