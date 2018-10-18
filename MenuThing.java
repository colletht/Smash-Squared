import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuThing extends JApplet {
	
	int WIDTH = 1320, HEIGHT = 650;
	int GRAVITY = 4;
	int DELAY = 20;
	int [] types = {0,1,0,1};
	//TestEnvironment t;
	MenuPanel t;
	
	public void init(){
		//t = new TestEnvironment(DELAY, 4, GRAVITY);
		t = new MenuPanel(DELAY);
		getContentPane().add(t);
		t.requestFocusInWindow();
		setSize(WIDTH, HEIGHT);
		
	}
	
}