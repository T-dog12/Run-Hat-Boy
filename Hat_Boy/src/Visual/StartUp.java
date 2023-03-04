package Visual;

import java.awt.Color;

import javax.swing.JFrame;


public class StartUp extends JFrame{
	
	Panel panel;
	
	StartUp(){
		panel = new Panel();
		this.add(panel);
		this.setTitle("Run Hat Boy!");
		this.setResizable(false);
		this.setBackground(Color.blue);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		StartUp start = new StartUp();
	}
}
