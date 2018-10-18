import java.awt.Graphics;
import java.awt.Rectangle;

public class Brawler extends AbstractFighter {

	private Rectangle hitbox = new Rectangle();
	
	public Brawler(int size, int x, int y, int color, int gravity, int width, int height,int width_offset) {
		super(size, x, y, color, gravity, width, height,width_offset);
	}
	
	public Brawler(){
		super();
	}
	
	public void draw(Graphics g){
		super.draw(g);
		if(attackCount > 0){
			drawAttack(g);
		}
	}
	
	public void drawAttack(Graphics g) {
		g.fillRect((int)hitbox.getX(), (int)hitbox.getY(), (int)hitbox.getWidth(), (int)hitbox.getHeight());
	}
	
	public void move(){
		super.move();
		//handles attack count
		if(attacking){
			attackCount++;
		}else if(attackCount > 0 && !attacking){
			attackCount--;
		}
		if(attackCount > 0){
			moveAttack();
		}
		updateHitting();
		
	}
	
	public void moveAttack(){
		int width = (int)((size/5)*(attackCount*0.8));
		if(faceDown && grounded){
			if(faceRight){
				hitbox.setBounds(x+size, y-(int)(size/2), width, (int)(size/2));
			}else{
				hitbox.setBounds(x-width, y-(int)(size/2), width, (int)(size/2));
			}
		}else if(faceDown && !grounded){
				hitbox.setBounds(x+size/4, y, size/2, (int)(size/2));
		}else{
			if(faceRight){
				hitbox.setBounds(x+size, y-(int)(size/2)-(int)(size/4), width, (int)(size/2));
			}else{
				hitbox.setBounds(x-width, y-(int)(size/2)-(int)(size/4), width, (int)(size/2));
			}
		}
		if(attackCount > 5){
			setAttacking(false);
		}
		if(hitbox.getWidth() == 0){
			hitbox = new Rectangle();
		}
	}
	
	public void updateHitting(){
		if(hitbox != new Rectangle()){
			hitting = true;
		}else{
			hitting = false;
		}
	}

	public boolean getHit(Rectangle r) {
		return hitbox.intersects(r);
	}
	
	public void clearHitbox(){
		hitbox = new Rectangle();
	}
	
	public Rectangle getHitbox(){
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}
	
	
}
