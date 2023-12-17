package com.NautS.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import com.NautS.Audio.JukeBox;
import com.NautS.Main.GamePanel;
import com.NautS.TileMap.Background;

public class MenuState extends GameState {
	
	public GamePanel gp;
	public Background bg;
	private String title;
	
	public static int currentChoice = 0;
	private String[] options = {
			"Start",
			"Help",
			"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	private Font note;
	
	private Font font;
	private Font font2;
	private Composite composite;
	private Composite opaque;
	
	public MenuState(GameStateManager gsm) {
		
		super(gsm);
		
		double d = Math.random();
		if (d < .05) {
			title = "ProTip: Don't Die";
		} else {title = "QuestQuest";}
		
		JukeBox.load("/SFX/menu.mp3", "select");
		JukeBox.load("/SFX/menu2.mp3", "menu");
		
		try {
			
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color (128, 0, 0);
			titleFont = new Font("Serif", Font.PLAIN, 100);
			
			font = new Font("Goudy Old Style", Font.PLAIN, 48);
			font2 = new Font("Courier", Font.PLAIN, 15);
			note = new Font("Comic Sans MS", Font.BOLD + Font.ITALIC, 15);
			composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f);
			opaque = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
			
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
		g.setComposite(opaque);
		g.setColor(titleColor);
		g.setFont(titleFont);
		int width = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (GamePanel.WIDTH / 2) - (width / 2), 150);
		
		g.setFont(note);
		String note = "This game is incomplete";
		int width2 = g.getFontMetrics().stringWidth(note);
		g.drawString(note, (GamePanel.WIDTH / 2) - (width2 / 2), 15);
		String resize = "Press Q to change scale-- GET THIS TO ACTUALLY WORK";
		int width3 = g.getFontMetrics().stringWidth(resize);
		g.setColor(new Color(178, 124, 45));
		g.drawString(resize, (GamePanel.WIDTH / 2) - (width3 / 2), 50);
		
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
		//draw credit
		g.setComposite(composite);
		g.setFont(font2);
		g.setColor(Color.RED);
		String credit = "Engine based on engine by Mike S. (ForeignGuyMike)";
		g.drawString(credit, 5, GamePanel.HEIGHT - 10);
	}
	
	private void select() {
		if(currentChoice == 0) {
			// start
			gsm.setState(GameStateManager.PLAYERSELECTSTATE);
		}
		if(currentChoice == 1) {
			// help
			gsm.setState(GameStateManager.HELPSTATE);
		}
		if(currentChoice == 2) {
			System.exit(0);
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
		if(k == KeyEvent.VK_Q){
			gp.changeScale();
		}
	}

	public void keyReleased(int k) {}
	
}
