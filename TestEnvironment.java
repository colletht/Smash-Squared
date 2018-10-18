import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TestEnvironment extends JPanel {

	private final int WIDTH = 1300, HEIGHT = 650;
    private final int SIZE = 60;
    private final int SPEED_CONSTANT = 15;
    private final int JUMP_CONSTANT = 50;
	
	private Timer timer;
	private boolean tester = false;
	private ArrayList<AbstractFighter> fighters = new ArrayList<AbstractFighter>();
	
	public TestEnvironment(int delay, int playerNum, int gravity){
		Timer timer = new Timer(delay,null);				//setting up timer and listeners for movement and animation
		timer.addActionListener(new AnimationListener());
        MoveListener listener = new MoveListener();
        addKeyListener(listener);
        setFocusable(true);
        int playerHeight = HEIGHT-50;
        for(int i = 0; i < playerNum; i++){		//Sets up the fighter characters in the game
        	if(i%2 == 0){
        		fighters.add(new Brawler(SIZE, i*(int)(WIDTH/4)+(int)(WIDTH/8), playerHeight-SIZE*3,i,gravity, WIDTH, playerHeight,0));
        	}else{
        		fighters.add(new Shooter(SIZE, i*(int)(WIDTH/4)+(int)(WIDTH/8), playerHeight-SIZE*3,i,gravity, WIDTH, playerHeight,0));	
        	}
        }
        timer.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(AbstractFighter f: fighters){
			f.draw(g);
		}
		g.setColor(Color.black);
		g.fillRect(0,HEIGHT-50, WIDTH,50);
		for(int i = 0; i < fighters.size(); i++){
			fighters.get(i).drawPercent(g, i*(int)(WIDTH/4)+(int)(WIDTH/8), HEIGHT-10);
		}
		repaint();
	}
	
	private class AnimationListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			for(AbstractFighter f: fighters){
				f.updateHitting();
				if(f.getAttackCount() > 0 || f.isHitting()){	//if fighter has a hitbox then check to see if they hit something
					for(AbstractFighter g: fighters){
						if(!f.equals(g) && f.getHit(g.getLocation())){
							g.collide(f.getHitbox().intersection(g.getLocation()));
						}
					}
					f.clearHitbox();
				}
				f.move();
			}
			repaint();
		}
		
	}
	
	private class MoveListener implements KeyListener{

	    private final int[] jumpKeys = {38,87,84,73}; //Up, W, T, I
	    private final int[] leftKeys = {37,65,70,74};	//Left, A, F, J
	    private final int[] rightKeys = {39,68,72,76};	//Right, D, H, L
	    private final int[] downKeys = {40,83,71,75};	//Down, S, G, K
	    private final int[] attackKeys = {17, 16, 67, 78};	//Right Control, Left Shift, C, N
		
		public void keyPressed(KeyEvent e) {
			for(int i = 0; i < fighters.size(); i++){
				if(e.getKeyCode() == leftKeys[i]){
					fighters.get(i).setXVelocity(-SPEED_CONSTANT);
				}else if(e.getKeyCode() == rightKeys[i]){
					fighters.get(i).setXVelocity(SPEED_CONSTANT);
				}
				if(e.getKeyCode() == jumpKeys[i] && fighters.get(i).isGrounded()){ //only jumps if the jump key is pressed and the specific fighter is on the ground
					fighters.get(i).setYVelocity(-JUMP_CONSTANT);
					//fighters.get(i).setGrounded(false);
				}else if(e.getKeyCode() == downKeys[i]){
					fighters.get(i).setYVelocity((int)0.5*JUMP_CONSTANT);
					fighters.get(i).setFaceDown(true);
				}
				if(e.getKeyCode() == attackKeys[i] /*&& !fighters.get(i).isAttacking()*/ && fighters.get(i).getAttackCount() == 0){
					fighters.get(i).setAttacking(true);
				}
			}
		}

		
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			for(int i = 0; i < fighters.size(); i++){
				if(e.getKeyCode() == leftKeys[i]){
					fighters.get(i).setXVelocity(0);
				}else if(e.getKeyCode() == rightKeys[i]){
					fighters.get(i).setXVelocity(0);
				}
				if(e.getKeyCode() == downKeys[i]){
					fighters.get(i).setFaceDown(false);
				}
				if(e.getKeyCode() == attackKeys[i]){
					fighters.get(i).setAttacking(false);
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
/*
 * tidy up code, maybe add percents? if not start working on gamemodes and or maps
 */
