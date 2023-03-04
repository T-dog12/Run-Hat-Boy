package Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import SpriteSheet.Movement.BufferImageLoader;
import SpriteSheet.Movement.SpriteSheet;

public class BackDrop {
	
	public int X;
	public int Y;
	final int WIDTH;
	final int HEIGHT;
	
	public int frame = 0;
	
	BufferedImage spriteS;
	BufferedImage background;
	Random ran;
	
	
	public BackDrop(int width,int height, int x, int prev) {
		
		HEIGHT = height;
		WIDTH = width;
		
		X = x;
		Y = 0;
		
		ran = new Random();
		frame = 1+ ran.nextInt(4);
		
		while (frame == prev) {
			frame = 1+ ran.nextInt(4);
		}
		
		sprite(frame,1,"/Back drop.PNG");
	}
	
	void sprite(int col, int row, String varation) {
		BufferImageLoader loader = new BufferImageLoader();
	
		try {
			spriteS = loader.loadImage(varation);
		}catch(IOException e) {
			System.out.print(e);
		}
		
		SpriteSheet ss = new SpriteSheet(spriteS);
		background = ss.grabImage(col, row, 1000, WIDTH, HEIGHT, 1000);
	}
	
	public void move() {
		X-=1;
		
	}
	
	public void draw(Graphics g) {
		
		g.drawImage(background, X, Y,null);
	
	}
	
}
