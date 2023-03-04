package Visual;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.Timer;

import SpriteSheet.Movement.BufferImageLoader;
import SpriteSheet.Movement.SpriteSheet;

public class StartScreen implements ActionListener{
	
	BufferedImage startButton;
	BufferedImage title;
	BufferedImage spriteS;
	
	Timer timer;
	
	int START_WIDTH = 200;
	int START_HEIGHT= 100;
	int startX = 400;
	int startY = 325;
	
	int TITLE_WIDTH = 600;
	int TITLE_HEIGHT = 300;
	public int titleX = 200;
	int titleY= 10;
	
	boolean move = false;
	public boolean onTitle = true;
	
	StartScreen(){
		timer = new Timer(100,this);
		
		startButton = sprite(START_WIDTH,START_HEIGHT,1,1,"/Start Button.png");
		title = sprite(TITLE_WIDTH,TITLE_HEIGHT,1,1,"/Title.png");
	}
	
	BufferedImage sprite(int width,int height,int col, int row, String varation) {
		BufferImageLoader loader = new BufferImageLoader();
	
		try {
			spriteS = loader.loadImage(varation);
		}catch(IOException e) {
			
		}
		
		SpriteSheet ss = new SpriteSheet(spriteS);
		return ss.grabImage(col, row, 1, width, height, width);
	}
	
	public void move() {
		if(move) {
			titleY += 10;
			startY += 10;
			
		}
	}
	
	public void mousePressed(MouseEvent e) {
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			if((e.getX() > startX && e.getX() < (startX+ START_WIDTH))
					&& (e.getY() > startY && e.getY() < (startY+START_HEIGHT))) {
				startButton = sprite(START_WIDTH,START_HEIGHT,2,1,"/Start Button.png");
				onTitle = false;
				timer.start();
			}
		}
		
	}
	
	public void draw(Graphics g) {
		g.drawImage(startButton, startX, startY, null);
		g.drawImage(title, titleX, titleY, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == timer) {
			startButton = sprite(START_WIDTH,START_HEIGHT,1,1,"/Start Button.png");
			timer.stop();
			move = true;
		}
	}
	
}
