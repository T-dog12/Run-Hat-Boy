package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import SpriteSheet.Movement.BufferImageLoader;
import SpriteSheet.Movement.SpriteSheet;

public class Obstacles extends Rectangle{
	
	BufferedImage spriteS;
	BufferedImage obstacles;
	int num;
	
	public Obstacles(int col,int width,int height,int x, int y) throws IOException{
		super(x,y,width,height);
		
		
		num= col;
		sprite(col,1,"/Blocks.png");
		
	}
	
	void sprite(int col, int row, String varation) {
		BufferImageLoader loader = new BufferImageLoader();
	
		try {
			spriteS = loader.loadImage(varation);
		}catch(IOException e) {
			System.out.print(e);
		}
		
		SpriteSheet ss = new SpriteSheet(spriteS);
		obstacles = ss.grabImage(col, row, 1000, width, height, 60);
		
		
	}
	
	public void move() {
		x-=3;
	}
	
	public void draw(Graphics g) {
		if(num == 2) {
			g.drawImage(obstacles, x, y-4, null);
		}else {
			g.drawImage(obstacles, x, y, null);
		}
		
	}
}
