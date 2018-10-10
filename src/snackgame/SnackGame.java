package snackgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class SnackGame {
	public static void main(String[] args) {
		JFrame w = new JFrame();
		
		SnackPanel sp = new SnackPanel();
		w.add(sp);
		
		new Thread(sp).start();
		
		w.addKeyListener(sp);
		sp.addKeyListener(sp);
		
		w.setBounds(100, 100, 410, 438);
//		w.setUndecorated(true);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setVisible(true);
	}
}

class SnackPanel extends JPanel implements Runnable, KeyListener {
	private Point[] body;
	private int length;
	private int head;
	
	private int speed;
	private int direction;
	private static final int UP = 0;
	private static final int DOWN = 1;
	private static final int LEFT = 2;
	private static final int RIGHT = 3;
	
	private Point food;
	
	private boolean isLive;
	
	public SnackPanel() {
		body = new Point[50];
		for (int i = 0; i < body.length; i++) {
			body[i] = new Point();
		}
		food = new Point((int)(Math.random()*20), (int)(Math.random()*20));
		body[0].x = (int)(Math.random()*5);
		body[0].y = (int)(Math.random()*20);
		body[1].x = body[0].x-1;
		body[1].y = body[0].y;
		body[2].x = body[1].x-1;
		body[2].y = body[1].y;
		
		head = 0;
		speed = 1;
		direction = 1;
		length = 3;
		isLive = true;
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(!isLive){
			g.drawString("gameover", 200, 200);
			return ;
		}
		g.setColor(Color.RED);
		g.fillOval(food.x*20, food.y*20, 20, 20);
		
		int temp = head;
		g.setColor(Color.BLUE);
		for (int i = 0; i < length; i++) {
			g.fillOval(body[temp].x*20, body[temp].y*20, 20, 20);
			temp = (temp+1)%body.length;
		}
		
		
	}


	@Override
	public void run() {
		while(true){
			if(collision()){
				isLive = false;
				repaint();
				return ;
			}
			eat();
			int newHead = (head-1+body.length)%body.length;
			body[newHead].x = body[head].x;
			body[newHead].y = body[head].y;
			head = newHead;
			switch (direction) {
			case UP:
				body[head].y -= speed;
				break;
			case DOWN:
				body[head].y += speed;
				break;
			case LEFT:
				body[head].x -= speed;
				break;
			case RIGHT:
				body[head].x += speed;
				break;
			}
			repaint();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void eat(){
		if(body[head].x==food.x && body[head].y==food.y){
			length++;
            food = new Point((int)(Math.random()*20), (int)(Math.random()*20));
		}
	}
	
	public boolean collision(){
		if(body[head].x<0 || 
				body[head].x>=20 || 
				body[head].y<0 || 
				body[head].y>=20){
			return true;
		}
		int temp = head;
		for (int i = 1; i < length; i++) {
			temp = (temp+1)%body.length;
			if(body[head].x==body[temp].x && body[head].y==body[temp].y){
				return true;
			}
		}
		return false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key==KeyEvent.VK_UP){
			if(direction!=DOWN){
				direction = UP;
			}
		}else if(key==KeyEvent.VK_DOWN){
			if(direction!=UP){
				direction = DOWN;
			}
		}else if(key==KeyEvent.VK_LEFT){
			if(direction!=RIGHT){
				direction = LEFT;
			}
		}else if(key==KeyEvent.VK_RIGHT){
			if(direction!=LEFT){
				direction = RIGHT;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
}