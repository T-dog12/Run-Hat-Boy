package Entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

import SpriteSheet.Movement.BufferImageLoader;
import SpriteSheet.Movement.SpriteSheet;

public class Player extends Rectangle implements ActionListener{
	
	private int frame=1;
	public int speed = 10;
	int count=0;
	
	public boolean jumping = false;
	public boolean onGround = true;
	
	Timer jumpTimer;
	public Timer animatTimer;
	BufferedImage spriteS;
	BufferedImage player;
	Clip clip;
	Random ran;
	
	String[] sound = {".//src//Sounds//Jump one.wav", ".//src//Sounds//Jump two.wav"};
	
	public Player(int x, int y,int width,int height) {
		super(x,y,width,height);
		
		
		jumpTimer = new Timer(70, this);
		animatTimer = new Timer(80,this);
		animatTimer.start();
		sprite(frame,1,"/Player.PNG");
		
	}
	
	void sprite(int col, int row, String varation) {
		BufferImageLoader loader = new BufferImageLoader();
	
		try {
			spriteS = loader.loadImage(varation);
		}catch(IOException e) {
			
		}
		
		SpriteSheet ss = new SpriteSheet(spriteS);
		player = ss.grabImage(col, row, 64, 64, 64, 64);
	}

	public void audio() {
		try {
			ran = new Random();
			int z =ran.nextInt(2);
			
			File file = new File(sound[z]);
			AudioInputStream sound =AudioSystem.getAudioInputStream(file);
			
			clip = AudioSystem.getClip();
			clip.open(sound);
			
		}catch(Exception e) {
			
		}
	}
	
	public void round() {
		String temp = String.valueOf(y);
		int num = Integer.parseInt(temp.substring(temp.length()-1));
		
		if(jumping) {
			y=200;
		}
		if(num > 4) {
			y += 10-num;
		}
	}
	
	
	public void KeyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			// 390
			if(onGround) {
				audio();
				
				clip.setFramePosition(0);
				clip.start();
				
				
				speed = 10;
				jumping =true;
				onGround = false;
				jumpTimer.start();
				animatTimer.stop();
				
			}
		}
	}
	
	public void move() {
		if(jumping) {
			y-=speed;
		}
	}
	
	public void draw(Graphics g){
		
		g.drawImage(player, 88, y,null);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jumpTimer) {
			speed -= 1;
			count++;
			
			sprite(5,1,"/Player.PNG");
			
			if (count == 6) {
				jumping =false;
				jumpTimer.stop();
				count=0;
				speed = 0;
				
				round();
				
			}
			
		}
		if(e.getSource() == animatTimer) {
			frame++;
			
			if (frame > 4) {
				frame = 1;
			}
			sprite(frame,1,"/Player.PNG");
		}
		
	}
}
