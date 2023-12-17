package com.NautS.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import com.NautS.Audio.JukeBox;
import com.NautS.Main.GamePanel;
import com.NautS.TileMap.Background;

public class HelpState extends GameState {
	
	public Background bg;
	
	private String[] controls = {
			"A or Left",
			"D or Right",
			"W or UP or Space Bar",
			"F",
			"E",
			"Ctrl",
			"R",
			"F3",
			"ESC"
	};
	
	private String[] actions = {
			"Move Left",
			"Move Right",
			"Jump (Hold for higher jump)",
			"Fire Gun",
			"Glide (hold)",
			"Punch",
			"Reload",
			"Toggle Debug Mode",
			"Pause"
	};
	
	private String option = "Back";

	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	private Font cFont;

	private Color cColor;
	
	public HelpState(GameStateManager gsm) {
		
		super(gsm);
		
		JukeBox.load("/SFX/menu.mp3", "select");
		
		try {
			
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color (128, 0, 0);
			cColor = new Color (150, 50, 0);
			titleFont = new Font("Serif", Font.PLAIN, 100);
			
			cFont = new Font("Goudy Old Style", Font.PLAIN, 20);
			font = new Font("Goudy Old Style", Font.PLAIN, 48);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {}
	public void update() {
		//handleInput();
		bg.update();
	}
	public void draw(Graphics2D g) {
		
		//draw bg
		bg.draw(g);
		
		//draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		String title = "Help";
		int width = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (GamePanel.WIDTH / 2) - (width / 2), 150);
		
		//draw controls
		g.setFont(cFont);
		for(int i = 0; i <controls.length; i++) {
			int swidth = g.getFontMetrics().stringWidth(controls[i]);
			g.setColor(cColor);
			g.drawString(controls[i], (GamePanel.WIDTH/2) - swidth - 5, 200 + i * (GamePanel.HEIGHT/16));
		}
		
		//draw actions
		for(int j = 0; j <actions.length; j++) {
			g.setColor(Color.red);
			g.drawString(actions[j], GamePanel.WIDTH / 2 + 5, 200 + j * (GamePanel.HEIGHT / 16));
		}
		
		//draw options
		g.setFont(font);
		g.setColor(Color.BLACK);
		int owidth = g.getFontMetrics().stringWidth(option);
		g.drawString(option, GamePanel.WIDTH/2 - owidth/2, GamePanel.HEIGHT - 10);
		
	}
	
	private void select() {
		JukeBox.play("select");
		gsm.setState(GameStateManager.MENUSTATE);
		MenuState.setCurrentChoice(1);
	}

	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER || k == KeyEvent.VK_ESCAPE){
			select();
		}
	}
	public void keyReleased(int k) {}
	
}
