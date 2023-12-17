package com.NautS.Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.NautS.GameState.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {
	
	//size of the screen
//	static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	static Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
	static Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

	
	//available size of the screen 
//	Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
//	int taskBarSize = scnMax.bottom;
	
	static int taskBarHeight = scrnSize.height - winSize.height;
	
	
	// dimensions
	public static int WIDTH = (int)(scrnSize.getWidth() / 2);
	public static int HEIGHT = (int)((scrnSize.getHeight()/2));
//	public static final int WIDTH = 920;
//	public static final int HEIGHT = 480;
	public int SCALE = 2;
	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
	public GamePanel() {
		super();
		if (WIDTH*SCALE < 920*2) {
			WIDTH = 920;
			HEIGHT = 480;
			SCALE = 1;
		}
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();

	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void init() {
		//for the Game's scale drawn onto the Game Panel
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		gsm = new GameStateManager();
	}

	public void changeScale() {
		if (SCALE == 2) {
			SCALE = 1;
		} if (SCALE == 1) {
			SCALE = 2;
		}
		run();
	}
	
	public void run() {
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		
		// game loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;
			
			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void update() {
		gsm.update();
	}
	private void draw() {
		gsm.draw(g);
	}
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	
	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());
	}
	
	public void keyTyped(KeyEvent key) {}
	
//	public void keyTyped(KeyEvent key) {}
//	public void keyPressed(KeyEvent key) {
//		if(key.isControlDown()) {
//			if(key.getKeyCode() == KeyEvent.VK_R) {
//				//recording = !recording;
//				return;
//			}
//			if(key.getKeyCode() == KeyEvent.VK_S) {
			//	screenshot = true;
//				return;
//			}
//		}
//		Keys.keySet(key.getKeyCode(), true);
//	}
//	public void keyReleased(KeyEvent key) {
//		Keys.keySet(key.getKeyCode(), false);
//	}
}
