package com.NautS.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import com.NautS.Audio.JukeBox;
import com.NautS.Entity.PlayerSave;
import com.NautS.Main.GamePanel;
import com.NautS.TileMap.Background;

public class PlayerSelectMenuState extends GameState {
	
	public Background bg;
	
	private String P1 = "Player 1";
	private String P2 = "Player 2";
	
	public static int currentChoice = 0;
	private int numChars = 2;
	private String[] options = {
			P1,
			P2,
			"Back"
	};
	

	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	public PlayerSelectMenuState(GameStateManager gsm) {
		
		super(gsm);
		
		JukeBox.load("/SFX/menu.mp3", "select");
		JukeBox.load("/SFX/menu2.mp3", "menu");
		
		try {
			
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color (128, 0, 0);
			titleFont = new Font("Serif", Font.PLAIN, 80);
			
			font = new Font("Goudy Old Style", Font.PLAIN, 48);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setCurrentChoice(int c) {
		int currentChoise = c;
		currentChoice = currentChoise;
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		
		//draw bg
		bg.draw(g);
		
		//draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		String title = "Select Player";
		int width = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (GamePanel.WIDTH / 2) - (width / 2), 150);
		
		//draw menu options
		g.setFont(font);
		for(int i = 0; i <options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.BLACK);
			}
			else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], GamePanel.WIDTH/20, (GamePanel.HEIGHT / 2) + i * (GamePanel.HEIGHT / 6));
		}
		
	}
	
	private void select() {
		for(int j = 0; j < options.length; j++) {
			if(currentChoice == numChars) {
				gsm.setState(GameStateManager.MENUSTATE);
			}
			else if(currentChoice == j) {
				PlayerSave.setCharacter(j);
				gsm.setState(GameStateManager.LEVEL1ASTATE);
			}
		}
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			JukeBox.play("select");
			select();
		}
		if(k == KeyEvent.VK_W || k == KeyEvent.VK_UP){
			currentChoice--;
			JukeBox.play("menu");
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN){
			currentChoice++;
			JukeBox.play("menu");
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}
	
}
