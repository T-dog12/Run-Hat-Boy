package Visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException
;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;

import Entities.BackDrop;
import Entities.Ground;
import Entities.Obstacles;
import Entities.Player;

public class Panel extends JPanel implements Runnable,ActionListener{
	
	private final static int GAME_WIDTH = 1000;
	private final static int GAME_HEIGHT = 500;
	private final static Dimension SCREEN_SIZE = new Dimension (GAME_WIDTH,GAME_HEIGHT);
	
	private int ns = 1000000000/60;
	private static long lastFPSCheck = 0;
	private static long currentFPS = 0;
	private static long totalFrames =0;
	
	int layerDiff = 120;
	int groundY;
	
	int prev = 0;
	int change =0;
	int repeat= 0;
	int highScore =  0;
	int score;
	int speed;
	int count;
	
	boolean started = false;
	boolean twice;
	boolean move;
	boolean low;
	boolean dead;
	
	Ground[] terrain = new Ground[25];
	BackDrop[] lBack = new BackDrop[3];
	
	ArrayList<Obstacles> obsticales;
	
	// runs the game
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random ran;
	
	// death animation
	Timer death;
	Timer scoreTimer;
	
	// images you see
	Player player;
	
	Ground ground;
	Ground cBlock;
	Ground nBlock;
	
	BackDrop back;
	Obstacles obst;
	StartScreen startScreen;
	DeathScreen deathScreen;
	
	
	File data;
	Scanner scan;
	BufferedWriter writer;
	
	
	// the sound effect
	Clip soundEffect;
	Clip theme;
	
	Panel(){			
		
		try {
			
			data = new File(".//src//High_Score.txt");
			scan = new Scanner(data);
			scan.nextLine();
			highScore = Integer.parseInt(scan.nextLine());
			
			scan.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// creates initial entities
		death = new Timer(70, this);
		scoreTimer = new Timer(500,this);
		
		newDeathScreen();
		newTitle();
		setUp();
		// creates the screen size and add the Action listener
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.addMouseListener(new ML());
		this.setPreferredSize(SCREEN_SIZE);
		
		playTheme();
		
		// starts game thread
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	void setUp(){
		
		obsticales = new ArrayList<Obstacles>();
		
		twice = false;
		move = true;
		low = true;
		dead = false;
		started = true;
		
		count=0;
		speed= 0;
		score =0;
		groundY = 450;
		
		ran = new Random();
		scoreTimer.restart();
		
		newPlayer();
		
		for(int p= 0; p<25;p++) {
			newGround(p,p*100);
			
		}
		for(int p= 0; p<3;p++) {
			newBack(p,p);
		}
	}
	
	// the methods to create the entities
	// player
	public void newPlayer() {
		player = new Player(100,390,38,64);
		
	}
	public void newTitle() {
		startScreen = new StartScreen();
		
	}
	public void newDeathScreen() {
		deathScreen = new DeathScreen();
		
	}
	
	// the floor
	public void newGround(int p, int point){
		
		count++;
		
		try {
			// y =450
			if(ran.nextInt(20) == 1 && count >= 50 && !startScreen.onTitle) {
				// makes ground lower
				ground = new Ground(100,50,point,groundY-layerDiff);
				
				if (low) {
					twice= true;
					low = false;
					groundY-= layerDiff;
					count = 20;
				
				}else {
					low = true;
					groundY+= layerDiff;
					count = 20;
				}
				
			}
			
			ground = new Ground(100,50,point,groundY);
			
			if(count >= 20 && !startScreen.onTitle) {
				
				// randomly creates the obstacles
				if(ran.nextInt(4) == 1 && !twice) {
					if(low) {
						
						newObstacles(20+ ground.x,428);
					}else {
						
						newObstacles(20+ ground.x,428-layerDiff);
					}
					
					twice = true;
				}else {
					twice = false;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		terrain[p] = ground;
	
	}
	// the obstacles
	public void newObstacles(int x,int y) {
		try {
			
			if(ran.nextInt(2) == 0) {
				obst = new Obstacles(2,60, 26, x, y+4);
				obsticales.add(obst);
			}else {
				obst = new Obstacles(1,60, 26, x, y);
				obsticales.add(obst);
				
				if(ran.nextInt(2) == 0) {
					obst = new Obstacles(2,60, 26, x, y-21);
					obsticales.add(obst);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	// the background 
	public void newBack(int p,int num) {
		
		try {
			back= new BackDrop(1000,450,1000*num,prev);
		} catch(Exception e){
			e.printStackTrace();
		}
		prev = back.frame;
		
		lBack[p] = back;
		
	}
	
	
	public Clip audio(String address) {
		Clip clip = null;
		try {
			
			File file = new File(address);
			AudioInputStream sound =AudioSystem.getAudioInputStream(file);
			
			clip = AudioSystem.getClip();
			clip.open(sound);
			
		}catch(Exception e) {
			
		}
		return clip;
	}
	
	public void playTheme() {
		theme = audio(".//src//Sounds//Jumps_Theme.wav");
		theme.setFramePosition(0);
		theme.loop(Clip.LOOP_CONTINUOUSLY);
		
	}
	
	
	public void fall() {
		soundEffect = audio(".//src//Sounds//Fall.wav");
		soundEffect.setFramePosition(0);
		soundEffect.start();
	}
	
	
	public void paint(Graphics g) {
		// puts everything on the screen all at once
		image = createImage(getWidth(),getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);
	}
	
	public void draw(Graphics g) {
		// draws all of the images
		for (int p = 0;p < lBack.length;p++) {
			lBack[p].draw(g);
		}
		for (int p = 0;p < terrain.length;p++) {
			terrain[p].draw(g);
			
		}
		if(obsticales.size() > 0) {
			for(int p=0; p< obsticales.size(); p++) {
				obsticales.get(p).draw(g);;
				
			}
		}
		startScreen.draw(g);
		
		if(dead) {
			deathScreen.draw(g);
		}
		
		g.setColor(new Color(255,44,125));
		g.setFont(new Font("Comic Sans MS", Font.PLAIN,40));
		g.drawString("Score:  "+ score, 10, 489);
		g.drawString("High Score:  "+highScore, 400, 489);
		
		player.draw(g);
	}
	
	public void move() {
		// moves everything
		if(move) {
			player.move();
			
			for (int p = 0;p < terrain.length;p++) {
				terrain[p].move();
				
			}
			for (int p = 0;p < lBack.length;p++) {
				lBack[p].move();
			}
			for(int p=0; p< obsticales.size(); p++) {
				obsticales.get(p).move();
				
			}
		}else {
			player.y += speed;
		}
		
		if(startScreen.titleX < 500) {
			startScreen.move();
		
		}
		
		if(deathScreen.titleX < 500) {
			deathScreen.move();
			
		}
	}
	
	public void hitBoxes() {
		// Checks whether player is on the ground
		
		for(int p = 0; p< terrain.length;p++) {
			
			if(terrain[p].x <= player.x && terrain[p].x + terrain[p].width>= player.x) {
				cBlock = terrain[p];
				
				if(terrain.length == p+1) {
					nBlock = terrain[0];
				}else {
					nBlock = terrain[p+1];
				}
				break;
			}
			
			
		}
		if ((player.y + player.height) < cBlock.y
				&& !player.jumping && move) {
			
			player.onGround = false;
			player.y+=player.speed;
			player.round();
			
			if(player.speed != 4) {
				player.speed ++;
				
			}
			
		}
		
		
		// starts the running animation
		else if (!player.jumping || player.onGround){
			player.onGround = true;
			player.animatTimer.start();
			
		}
		if((player.y > (nBlock.y+4) || (player.y + player.height) > (nBlock.y+4))
				&& (player.x + player.width) > nBlock.x && move) {
			
			repeat = 0;
			
			move = false;
			death.start();
			fall();
		}
		// removes the terrain and creates a new one behind the last
		for(int p = 0; p< terrain.length; p++) {
			if(terrain[p].x < -100) {
				newGround(p,ground.x+100);
				
			}
		}
		// creates a new background
		for (int p = 0;p < lBack.length;p++) {
			if(lBack[p].X < -999) {
				newBack(p,2);
			}
		}
		// Checks if the there are any obstacles present
		if(obsticales.size() !=0) {
			if(obsticales.get(0).x < -100) {
				obsticales.remove(0);
				
			}
			
			for (int p =0; p < obsticales.size();p++) {
				Obstacles item = obsticales.get(p);
				
				if(item.intersects(player)&& move) {
					repeat = 0;
					move = false;
					player.animatTimer.stop();
					death.start();
					
					fall();
					
				}
			}
		}
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			
			
			//FPS
			totalFrames ++;
			
			
			if (System.nanoTime() > lastFPSCheck + ns&& started) {
				lastFPSCheck = System.nanoTime();
				currentFPS = totalFrames;
				totalFrames = 0;
				
				// updates the game's panel and movements
				repaint();
				hitBoxes();
				move();
				
				if(deathScreen.reset) {
					setUp();
					
					deathScreen.reset = false;
				}
			
			}
			
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == death) {
			scoreTimer.stop();
			if(repeat < 4) {
				speed -=1;
			}
			else {
				speed +=1;
			}
			repeat++;
			
			if(player.y > GAME_HEIGHT) {
				soundEffect= audio(".//src//Sounds//Thud.wav");
				soundEffect.setFramePosition(0);
				soundEffect.start();
				dead = true;
				
				death.stop();
			}
		}
		if(e.getSource() == scoreTimer) {
			if(!startScreen.onTitle) {
				score+=1;
				
				if (score > highScore) {
					highScore = score;
					
					try {
						data.delete();
						
						FileWriter wData = new FileWriter(".//src//High_Score.txt",false);
						wData.write("High Score:\n"+ score);
						wData.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	public class AL extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			player.KeyPressed(e);
		}
		
	}
	public class ML extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			
			startScreen.mousePressed(e);
			
			if(dead) {
				deathScreen.mousePressed(e);
			}
		}
	}
}
