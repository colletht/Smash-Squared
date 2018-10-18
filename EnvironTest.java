import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EnvironTest extends JApplet {
	
	int WIDTH = 700, HEIGHT = 200;
	int GRAVITY = 4;
	int DELAY = 20;
	int [] types = {-1,-1}; 	//TestEnvironment t
	KingOfTheHill t;
	
	public void init(){
		//t = new TestEnvironment(DELAY, 4, GRAVITY);
		t = new KingOfTheHill(DELAY,types,GRAVITY);
		getContentPane().add(t);
		t.requestFocusInWindow();
		setSize(WIDTH, HEIGHT);
		t.startGame();
		
	}
	
}
