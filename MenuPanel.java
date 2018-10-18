import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MenuPanel extends JPanel 
{
    private final int WIDTH = 1300, HEIGHT = 650;
    private final int LEFT_BORDER = 340;
    private static final Color c1 = new Color(255,255,255);      //white
    private static final Color c2 = new Color(255,255,51);       //yellow
    private static final Font f1 = new Font("Arial Black", Font.BOLD, 100);
    private static final Font f2 = new Font("Arial Black", Font.BOLD, 50);
    private static final Font f3 = new Font("Arial Black", Font.BOLD, 25);
    private static final Font f4 = new Font("Arial Black", Font.BOLD, 40);
    private static final String s1 = "ULTIMATE", s2 = "FIGHTER", s3 = "Mode: ";
    private static final String [] S = {"Play", "Gamemode", "Controls", "Close"}; //strings for main menu page
    private static final String [] SO = {"Stock","Minimize"};					  //strings for gamemode page
    private static final String back = "<==";
    private static final String[] I = {"In Boo Bombardment, the goal is to survive as long as possible.",
                                       "However, this gets harder and harder as more Boos",
                                       "spawn in periodicaly. To avoid the Boos use the left and right",
                                       "arrow keys to move, the spacebar to jump, and the shift button", 
                                       "to sprint.      Have Fun!"};
    private static final String SELECT = "Press Your Buttons to Join";
    //add an array with points to print strings and booleans
    private boolean backSel = false;
    private boolean backPre = false;
    private boolean backCli = false;
    private boolean[] selected = {false, false, false, false};	//tells whether a button has been selected on the main screen
    private boolean[] selectedO = {false, false};				//tells whether button has been selected on gamemode screen
    private boolean[] clicked = {false,false,false,false};		//tells if a button has been clicked
    private boolean[] clickedO = {false,false,false};
    private boolean[] pSelect = {false,false,false,false};	//tells whether a player is in or not
    private Point[] points = {new Point((int)(WIDTH/8),(HEIGHT/4)-f3.getSize()),new Point((int) (WIDTH*0.625),(HEIGHT/4)-f3.getSize()), new Point(WIDTH/8,(int) ((HEIGHT*0.75)-f3.getSize())), new Point((int) (WIDTH*0.625),(int) ((HEIGHT*0.75)-f3.getSize()))};
    private int[] fighters = new int[4];
    private AbstractFighter[] fighters2 = {new Brawler(WIDTH/8,WIDTH/8,HEIGHT/10,0,4,WIDTH/8,(int) (HEIGHT*0.4),WIDTH/8),new Brawler(WIDTH/8,(int) (WIDTH*0.625),HEIGHT/10,1,4,WIDTH/8,(int) (HEIGHT*0.4),(int) (WIDTH*0.625)),new Brawler(WIDTH/8,WIDTH/8,(int)(HEIGHT*0.6),2,4,WIDTH/8,(int) (HEIGHT*0.9),WIDTH/8), new Brawler(WIDTH/8,(int) (WIDTH*0.625),(int) (HEIGHT*0.6),3,4,WIDTH/8,(int) (HEIGHT*0.9),(int) (WIDTH*0.625))};
    private int page = 0;
    private final Rectangle[] R = {new Rectangle(LEFT_BORDER,310,150,50), new Rectangle(LEFT_BORDER,375,330,50), new Rectangle(LEFT_BORDER,440,300,50), new Rectangle(LEFT_BORDER,505,300,50)};
    private final Rectangle[] RO = {new Rectangle(LEFT_BORDER,310,300,50), new Rectangle(LEFT_BORDER,375,300,50), new Rectangle(LEFT_BORDER,440,300,50)};
    private final Rectangle backB = new Rectangle(25,535,300,50);
    private int count = 0;
    private Timer timer;
    private Point p;
    private int difficulty = 1;
    private boolean playtime = false;
    private boolean completlyDone = false;
    private Color winnerC = Color.red;
    
    public MenuPanel(int delay)
    {
        timer = new Timer(delay,null);
        timer.addActionListener(new MenuListener());
        MenuMoListener myListener = new MenuMoListener();
        addMouseListener(myListener);
        addMouseMotionListener(myListener);
        addKeyListener(new MenuKListener());
        setFocusable(true);
        timer.start();
        setBackground(Color.gray);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(page != 3){
        	g.setColor(c1);
        	g.setFont(f1);
        	g.drawString(s1,LEFT_BORDER,120);
        	g.drawString(s2,LEFT_BORDER,260);
        }
        g.setFont(f2);
        
        if(page == 1)
        {
            drawOptions(g);
        }else if(page == 2){
            drawControls(g);
        }else if(page == 3){
        	drawSelect(g);
        }else{
            drawMain(g);
        }
    }
    public boolean getPlayTime(){
        return playtime;
    }
    public void setPlayTime(boolean p){
        playtime = p;
    }
    public int getDiff(){
        return difficulty;
    }
    public int[] getFighterType(){
    	return fighters;
    }
    public void reset(){
        setFocusable(true);
        playtime = false;
        for(int i = 0; i < clickedO.length; i++)
        {
            clickedO[i] = false;
        }
        timer.start();
    }
    public boolean getComplete(){
        return completlyDone;
    }
    private class MenuListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            
            repaint();
        }
    }
    private class MenuKListener implements KeyListener{
    	
    	private final int[] jumpKeys = {38,87,84,73}; //Up, W, T, I
	    private final int[] leftKeys = {37,65,70,74};	//Left, A, F, J
	    private final int[] rightKeys = {39,68,72,76};	//Right, D, H, L
	    private final int[] downKeys = {40,83,71,75};	//Down, S, G, K
	    private final int[] attackKeys = {17, 16, 67, 78};	//Right Control, Left Shift, C, N
    	
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(page == 3){
				if(e.getKeyCode() == 32){
					
					setPlayTime(true);
				}
				for(int i = 0; i < 4; i++){
					if(e.getKeyCode() == rightKeys[i] || e.getKeyCode() == leftKeys[i] || e.getKeyCode() == downKeys[i] || e.getKeyCode() == jumpKeys[i] || e.getKeyCode() == attackKeys[i]){
						if(!pSelect[i]){
							pSelect[i] = true;
							fighters[i]+=1;
						}else{
							fighters[i]*=-1;
						}
					}
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
    	
    }
    private class MenuMoListener extends MouseInputAdapter{
    	
        public void mousePressed(MouseEvent e) {
            p = e.getPoint();
            if(page == 1){
                for(int i = 0; i < clickedO.length; i++){
                    if(RO[i].contains(p.x,p.y)){
                        clickedO[i] = true;
                        difficulty = i;
                        page = 0;
                    }else{
                        clickedO[i] = false;
                    }
                }
                if(backB.contains(p.x,p.y)){
                    page = 0;
                }
            }else if(page == 2){
                if(backB.contains(p.x,p.y)){
                    page = 0;
                }
            }else if(page == 0){//main page
                for(int i = 0; i < clicked.length; i++){
                    if(R[i].contains(p.x,p.y)){
                        clicked[i] = true;
                        if(i == 0){
                        	page = 3;//set page to select page
                        }
                        if(i == 1){
                            page = 1;
                        }
                        if(i == 2){
                            page = 2;
                        }
                    }else{
                        clicked[i] = false;
                    }
                }
            }
        }
        public void mouseMoved(MouseEvent e){
            p = e.getPoint();
            if(page == 1){
                if(backB.contains(p.x,p.y)){
                    backSel = true;
                }else{
                    for(int i = 0; i < selectedO.length; i++){
                        if(R[i].contains(p.x,p.y)){
                            selectedO[i] = true;
                        }else{
                            selectedO[i] = false;
                        }
                    }
                    backSel = false;
                }
            }else if(page == 2){
                if(backB.contains(p.x,p.y))
                {
                    backSel = true;
                }else{
                    backSel = false;
                }
            }else{
                for(int i = 0; i < selected.length; i++){
                    if(R[i].contains(p.x,p.y)){
                        selected[i] = true;
                    }else{
                        selected[i] = false;
                    }
                }
                backSel = false;
            }
        }
    }
    public void drawMain(Graphics g){
        for(int i = 0; i < S.length; i++){
            if(selected[i]){
                g.setColor(c2);  
            }else{
                g.setColor(c1);
            }
            g.drawString(S[i], LEFT_BORDER, 350 + (i*65));
        }
        g.setColor(c1);
        g.drawString(s3+SO[difficulty], LEFT_BORDER+350, 350);
    }
    public void drawOptions(Graphics g){
    	for(int i = 0; i < SO.length; i++){
            if(selectedO[i]){
                g.setColor(c2);
            }else{
                g.setColor(c1);
            }
            g.drawString(SO[i], LEFT_BORDER, 350 + (i*65));
        }
        if(backSel){
            g.setColor(c2);  
        }else{
            g.setColor(c1);
        }
        g.drawString(back, 25, 575);
        g.setColor(c1);
    }
    public void drawControls(Graphics g){
        g.setFont(f3);
        for(int i = 0; i < I.length;i++){
            g.drawString(I[i],LEFT_BORDER,350 + (i*40));
        }
        if(backSel){
            g.setColor(c2);  
        }else{
            g.setColor(c1);
        }
        g.setFont(f2);
        g.drawString(back, 25, 575);
        g.setColor(c1);
    }
    /*
     * draws the player select screen, allows players to select character before playing
     */
    public void drawSelect(Graphics g){
    	g.setColor(Color.black);
    	g.drawLine((int)(WIDTH/2), 0, (int)(WIDTH/2), HEIGHT);
    	g.drawLine(0, (int)(HEIGHT/2), WIDTH, (int)(HEIGHT/2));
    	g.setColor(Color.gray);
    	g.fillRect(WIDTH/2 - WIDTH/4 , HEIGHT/2 - f2.getSize()/2, (int) (WIDTH/2), (int) (f2.getSize()*1.5));
    	g.setColor(Color.black);
    	g.drawString("Press Space to Play", WIDTH/2 - WIDTH/4, HEIGHT/2+f2.getSize()/2);
    	g.setFont(f3);
    	for(int i = 0; i < pSelect.length; i++){
    		if(!pSelect[i]){
    			g.setColor(Color.black);
    			g.drawString(SELECT, (int)(points[i].getX()), (int)points[i].getY());
    		}else{
    			fighters2[i].move();
    			fighters2[i].draw(g);
    			if(fighters[i] > 0){
    				g.drawString("Shooter", fighters2[i].getX()+WIDTH/7, fighters2[i].getY()-WIDTH/16 - f3.getSize());
    			}else{
    				g.drawString("Brawler", fighters2[i].getX()+WIDTH/7, fighters2[i].getY()-WIDTH/16 - f3.getSize());
    			}
    		}
    	}
    }
}