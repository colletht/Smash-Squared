import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Shooter extends AbstractFighter {

	private ArrayList<Bullet> hitboxes = new ArrayList<Bullet>();
	private static final int MAX_DISTANCE = 400;
	private static final int BULLET_VELOCITY = 10;
	private Rectangle hitbox = new Rectangle();
	private boolean fireable = true; //tells whether a new bullet can be fired or not
	
	public Shooter(int size, int x, int y, int color, int gravity, int width, int height,int width_offset) {
		super(size, x, y, color, gravity, width, height,width_offset);
	}
	
	public void draw(Graphics g){
		super.draw(g);
		if(hitboxes.size() >= 0 || hitbox != new Rectangle()){
			drawAttack(g);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractFighter#drawAttack(java.awt.Graphics)
	 */
	public void drawAttack(Graphics g) {
		for(Bullet b: hitboxes){
			b.draw(g);
		}
		if(attacking){
			g.fillRect((int)hitbox.getX(), (int)hitbox.getY(), (int)hitbox.getWidth(), (int)(hitbox.getHeight()));
		}
	}
	
	public void move(){ //moves the player and the attacks
		super.move();
		moveAttack();
		updateHitting();
	}
	
	/*
	 * moves the attack before it is drawn
	 */
	public void moveAttack(){
		setFireable();
		if(faceDown && !grounded && attacking){ // if loop that decides what to do, down attack? or generate new bullets
			hitbox.setBounds(x+size/4, y, size/2, (int)(size/2));
		}else if(!faceDown && fireable){
			if(faceRight){					
				hitboxes.add(new Bullet(x+size,y-size,size/4,size/4,BULLET_VELOCITY));
			}else{
				hitboxes.add(new Bullet(x-size/4,y-(size),size/4,size/4,-BULLET_VELOCITY));
			}
		}
		//for loop updates previously existing bullets' positions and/or kills them if necessary
		for(int i = 0; i < hitboxes.size(); i++){
			Bullet b = hitboxes.get(i);
			if(!b.isDead()){
				b.move();
			}else{
				hitboxes.remove(b);
				i--;
			}
		}
	}
	/*
	 * checks to make sure all conditions are met in order to fire a new bullet
	 */
	public void setFireable(){ 
		if(attacking){
			if(!hitboxes.isEmpty()){
				Bullet b = hitboxes.get(hitboxes.size()-1);
				if(Math.abs(b.getInitialX()-b.getBullet().getX()) >= /*b.getBullet().getWidth()*2*/60 && hitboxes.size() < 3){
					fireable = true;
				}else{
					fireable = false;
				}
			}else{
				fireable = true;
			}
		}else{
			fireable= false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractFighter#getHit(java.awt.Rectangle)
	 */
	public boolean getHit(Rectangle r) {
		for(int i = 0; i < hitboxes.size(); i++){	//checks to see if the bullets hit the specified rectangle WORKS
			Bullet b = hitboxes.get(i);
			if(b.getBullet().intersects(r)){
				hitbox = b.getBullet();
				hitboxes.remove(b);
				i--;
				return true;
			}
		}
		if(hitbox.intersects(r)){	//checks to see if down attack hits the specified rectangle DOESNT WORK
			return true;
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractFighter#updateHitting()
	 */
	public void updateHitting(){
		if(hitbox != new Rectangle()){
			hitting = true;
		}else{
			hitting = false;
		}
	}
	
	public void clearHitbox(){
		hitbox = new Rectangle();
	}
	
	public Rectangle getHitbox(){
		return hitbox;
	}
	
	/*
	 * bullet class that contains data for each of the bullets and controls their movement and state
	 */
	private class Bullet{
		
		private Rectangle bullet;
		private int xVelocity;
		private int initialX;
		private boolean dead = false;
		
		public Bullet(int x, int y, int width, int height, int xVelocity){
			this.bullet = new Rectangle(x,y,width,height);
			this.xVelocity = xVelocity;
			this.initialX = x;
		}
		
		public void move(){
			int x = (int) bullet.getX();
			bullet.setLocation(x+=xVelocity, (int)bullet.getY());
			if(Math.abs(bullet.getX()-initialX) >= MAX_DISTANCE){
				dead = true;
			}
		}
		
		public void draw(Graphics g){
			g.fillRect((int)bullet.getX(), (int)bullet.getY(), (int)bullet.getWidth(), (int)bullet.getHeight());
		}
		
		public boolean isDead(){
			return dead;
		}
		
		public Rectangle getBullet(){
			return bullet;
		}
		
		public int getInitialX(){
			return initialX;
		}
		
	}

}
