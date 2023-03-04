package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import SpriteSheet.Movement.BufferImageLoader;
import SpriteSheet.Movement.SpriteSheet;

public class Ground extends Rectangle{

	boolean present= true; 
	
	BufferedImage spriteS;
	BufferedImage ground;
	BufferedImage groundBack;
	Random ran;
	
	
	public Ground(int width,int height, int x, int y) throws IOException{
		super(x,y,width,height);
		
		ran = new Random();
		groundBack = ImageIO.read(getClass().getResource("/ground back.png"));

		sprite(1+ ran.nextInt(15),1,"/ground.PNG");
	}
	
	void sprite(int col, int row, String varation) {
		BufferImageLoader loader = new BufferImageLoader();
	
		try {
			spriteS = loader.loadImage(varation);
		}catch(IOException e) {
			
		}
		
		SpriteSheet ss = new SpriteSheet(spriteS);
		ground = ss.grabImage(col, row, 1, width, height, 64);
	}
	
	public void move() {
		x-=3;
	}
	
	public void draw(Graphics g) {
		g.drawImage(groundBack,x, 450, null);
		
		g.drawImage(ground, x, y, null);
		g.drawRect(x, y, width, height);
		g.drawRect(x, 450, width, height);
	}
}
