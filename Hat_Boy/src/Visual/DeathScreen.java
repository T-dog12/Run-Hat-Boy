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

public class DeathScreen implements ActionListener {
	
	BufferedImage restartButton;
	BufferedImage title;
	BufferedImage spriteS;
	Timer timer;
	
	int RESTART_WIDTH = 200;
	int RESTART_HEIGHT= 100;
	int restartX = 400;
	int restartY = 325;
	
	int TITLE_WIDTH = 600;
	int TITLE_HEIGHT = 300;
	public int titleX = 200;
	int titleY= 10;
	
	
	
	boolean move = false;
	boolean reset = false;
	
	
	DeathScreen(){
		timer = new Timer(100,this);
		
		restartButton = sprite(RESTART_WIDTH,RESTART_HEIGHT,1,1,"/Restart_Button.png");
		title = sprite(TITLE_WIDTH,TITLE_HEIGHT,1,1,"/Death_Title.png");
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
			restartY += 10;
			
		}
	}
	

	public void draw(Graphics g) {
		g.drawImage(restartButton, restartX, restartY, null);
		g.drawImage(title, titleX, titleY, null);
	}
	
	public void mousePressed(MouseEvent e) {
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			if((e.getX() > restartX && e.getX() < (restartX+ RESTART_WIDTH))
					&& (e.getY() > restartY && e.getY() < (restartY+RESTART_HEIGHT))) {
				restartButton = sprite(RESTART_WIDTH,RESTART_HEIGHT,2,1,"/Restart_Button.png");
				timer.start();
			}
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == timer) {
			restartButton = sprite(RESTART_WIDTH,RESTART_HEIGHT,1,1,"/Restart_Button.png");
			timer.stop();
			
			move = true;
			reset= true;
			
		}
	}
}
