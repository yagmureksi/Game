package Test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
	
public class GamePanel  extends JPanel implements ActionListener{

	static final int width=600;
	static final int height=600;
	static final int object_size=25; //object size
	static final int game_objects=(width*height)/object_size;
	static final int delay=75;

	
	final int x[]=new int[game_objects];
	final int y[]=new int[game_objects];
	int bodyParts=6;
	int foodsEaten;
	int foodX;
	int foodY;
	
	char direction='R';
	boolean running=false;
	Timer timer;
	Random random;
	
	
	GamePanel(){
		random=new Random();
		this.setPreferredSize(new Dimension(width,height));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newFood();
		running=true;
		timer=new Timer(delay,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if(running) {
			for(int i=0;i<height/object_size;i++) {
			g.drawLine(i*object_size,0,i*object_size,height);
			g.drawLine(0,i*object_size,width,i*object_size);
			g.setColor(getBackground());
			}
			g.setColor(Color.red);
			g.fillOval(foodX,foodY,object_size,object_size);
		
			for(int i=0;i<bodyParts;i++) {
				if(i==0) {
					g.setColor(Color.green);
					g.fillRect(x[i],y[i],object_size,object_size);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i],y[i],object_size,object_size);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Int Free",Font.BOLD,40));
			FontMetrics metrics=getFontMetrics(g.getFont());
			g.drawString("Score: "+foodsEaten,(width-metrics.stringWidth("Score: "+foodsEaten))/2,g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}
	public void newFood() {
		foodX=random.nextInt((int)(width/object_size))*object_size;
		foodY=random.nextInt((int)(height/object_size))*object_size;
	}
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			 x[i]=x[i-1];
			 y[i]=y[i-1];
		} 
		switch(direction) {
		case 'U':
			y[0]=y[0]-object_size;
			break;
		case 'D':
			y[0]=y[0]+object_size;
			break;
		case 'L':
			x[0]=x[0]-object_size;
			break;
		case 'R':
			x[0]=x[0]+object_size;
			break;
		}
	}
	public void checkFood() {
		if((x[0]==foodX)&&(y[0]==foodY)) {
			bodyParts++;
			foodsEaten++;
			newFood();
		}
		
	}
	public void checkCollisions() {
		//checks if head collides with body
		for(int i=bodyParts;i>0;i--) {
			if((x[0]==x[i])&&(y[0]==y[i])) {
				running=false;
			}
		}
		//check if head touches left border
		if(x[0]<0) {
			running=false;
		}
		//check if head touches right border
		if(x[0]>width) {
			running=false;
		}
		//check if head touches top border
		if(y[0]<0) {
			running=false;
		}
		//check if head touches bottom border
		if(y[0]>height) {
			running=false;
		}
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("Int Free",Font.BOLD,40));
		FontMetrics metrics1=getFontMetrics(g.getFont());
		g.drawString("Score: "+foodsEaten,(width-metrics1.stringWidth("Score: "+foodsEaten))/2,g.getFont().getSize());
		//Game Over
		g.setColor(Color.red);
		g.setFont(new Font("Int Free",Font.BOLD,75));
		FontMetrics metrics2=getFontMetrics(g.getFont());
		g.drawString("GAME OVER",(width-metrics2.stringWidth("GAME OVER"))/2,height/2);
		
		
	}
	
	@Override 
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkFood();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			
			case KeyEvent.VK_LEFT:
				if(direction !='R') {
					direction='L';
					
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction !='L') {
					direction='R';
					
				}
				break;
			case KeyEvent.VK_UP:
				if(direction !='D') {
					direction='U';
					
				}	
				break;
			case KeyEvent.VK_DOWN:
				if(direction !='U') {
					direction='D';
					
				}
				break;
			}
		}
	}

}
