
import java.awt.*;
import java.util.ArrayList;

public abstract class AbstractFighter  {

	private static final Color[] COLORS = {Color.red, Color.blue, new Color(255,255,0), new Color(0,255,0)};			 
	/*										red			blue			yellow				green	*/
	//Now implement percents and/or health and make methods to display those values
	private static int gravity;
	protected int xDecell = 0;
	private int yDecell = 0;
	private int HEIGHT;
	private int WIDTH;
	private int WIDTH_OFFSET;
	
	protected int size;
	protected int x;
	protected int y;
	protected int xVelocity = 0;
	protected int yVelocity = 0;
	protected Rectangle location;
	private int AREA_CONSTANT = (int)(size/16);
	private int COLLISION_CONSTANT = 10;
	private int percent = 0;
	protected Color color;
	
	protected boolean attacking = false;
	protected int attackCount = 0;
	protected boolean inAir = false;
	protected boolean faceRight = false;
	protected boolean faceDown = false;
	protected boolean grounded = true;
	protected int jumpCount = 0;
	protected boolean hitting = false;
	protected boolean dead = false;

	public AbstractFighter(int size, int x, int y, int color, int gravity, int width, int height, int width_offset){
		this.size = size;
		this.x = x;
		this.y = y;
		this.color = COLORS[color];
		this.gravity = gravity;
		this.HEIGHT = height;
		this.WIDTH = width;
		this.WIDTH_OFFSET = width_offset;
		location = new Rectangle(x,y,size,size);
	}
	
	public AbstractFighter(){
		size = 0;
		x = 0;
		y=0;
		color = null;
		gravity = 0;
		this.HEIGHT = 0;
		this.WIDTH = 0;
		this.WIDTH_OFFSET = 0;
		location = null;
		dead = true;
	}
	
	public void draw(Graphics g){
		g.setColor(color);
		g.fillRect(x, y-size, size, size);
	}
	
	public void drawPercent(Graphics g, int x, int y){
		String s = percent+"%";
		g.setColor(color);
		g.setFont(new Font("Arial Black",Font.BOLD,(int)(size*0.66)));
		if(dead){
			g.drawString("-", x, y);
		}else{
			g.drawString(s, x, y);
		}
	}
	public abstract void drawAttack(Graphics g);
	public abstract void moveAttack();
	
	public void move(){
		x+=(xVelocity+xDecell);
		y+=(yVelocity+yDecell);
		
		if(y >= HEIGHT && (x+size>=WIDTH_OFFSET && x <= WIDTH+WIDTH_OFFSET)){
			grounded = true;
			jumpCount = 0;
			if(yDecell > 0){
				yDecell = 0;
			}
			y = HEIGHT;
			yVelocity = 0;
		}else{
			yVelocity+=(gravity);
		}
		if(xDecell != 0){
			xDecell = (int)(xDecell/1.1);
			//figure out how to handle x and y decelerations
		}
		if(yDecell != 0){
			yDecell = (int)(yDecell/1.1);
		}
		updateRect();
		
	}
	
	/*						-----------------
	 * 						|		|		|		if only one quad is touched it will return that number else it will return product of the two boxes touched the most
	 * 						| 	3	|	2	|
	 * 						|		|		|
	 * 						-----------------
	 * 						|		|		|
	 * 						|	4	|	5	|
	 * 						|		|		|
	 * 						-----------------
	 * 
	 */
	//add back severity soon
	public void collide(Rectangle intersection){ 
		double severity;
		if((int)(intersection.getWidth()*intersection.getHeight()) > AREA_CONSTANT){
			severity = 1.5; //hit is harder
			percent += 6;
		}else{
			severity = 1; //hit is less hard
			percent += 3;
		}
		
		ArrayList<Rectangle> quadList = quadCreate();
		int colPlace = 1;
		for(int i = 0; i < quadList.size(); i++){
			if(quadList.get(i).intersects(intersection) && colPlace < 20){
				colPlace*=(i+2);
			}
		}
		switch(colPlace){
			case 2: 	//quad 1 is only impacted
				yDecell += calcPercent(severity);
				xDecell += -calcPercent(severity);
				System.out.println("Quad 1 hit");
				break;
			case 3:		//quad 2 is only impacted
				yDecell += calcPercent(severity);
				xDecell += calcPercent(severity);
				System.out.println("Quad 2 hit");
				break;
			case 4:		//quad 3 is only impacted
				yDecell += -calcPercent(severity);
				xDecell += calcPercent(severity);
				System.out.println("Quad 3 hit");
				break;
			case 5:		//quad 4 is only impacted
				yDecell += -calcPercent(severity);
				xDecell += -calcPercent(severity);
				System.out.println("Quad 4 hit");
				break;
			case 6:		//quad 1 and 2 are impacted
				yDecell += calcPercent(severity);
				System.out.println("Quad 1 and 2 hit");
				break;
			case 12:	//quad 2 and 3 are impacted
				xDecell += calcPercent(severity);
				System.out.println("Quad 2 and 3 hit");
				break;
			case 20:	//quad 3 and 4 are impacted
				yDecell += -calcPercent(severity);
				System.out.println("Quad 3 and 4 hit");
				break; 
			case 10:	//quad 4 and 1 are impacted
				xDecell += -calcPercent(severity);
				System.out.println("Quad 1 and 4 hit");
				break;
		}
	}
	
	//possible lose life by making entire character smaller, decrementing size? GameModes?
	
	public void updateRect(){
		location.setBounds(x,y-size,size,size);
	}
	
	public ArrayList<Rectangle> quadCreate(){
		ArrayList<Rectangle> r = new ArrayList<Rectangle>();
		r.add(new Rectangle((int)x+size/2,y-size,(int)size/2,(int)size/2));
		r.add(new Rectangle(x,y-size,(int)size/2,(int)size/2));
		r.add(new Rectangle(x,(int)y-size/2,(int)size/2,size/2));
		r.add(new Rectangle(x+size/2,y-size/2,size/2,size/2));
		return r;
	}
	
	public int calcPercent(double severity){
		double knockback = (COLLISION_CONSTANT*(percent/15)*severity);
		if((int)knockback <= 1){
			return 2;
		}else{
			return (int)knockback;
		}
	}
	/*
	 * Setter and Getters
	 */
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getSize(){
		return this.size;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
		if(!attacking){
			//resetACount();
		}
	}

	public boolean isInAir() {
		return inAir;
	}

	public void setInAir(boolean inAir) {
		this.inAir = inAir;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void incrementACount(){
		this.attackCount++;
	}
	
	public void resetACount(){
		this.attackCount = 0;
	}
	
	public int getXVelocity() {
		return xVelocity;
	}

	public void setXVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
		if(xVelocity > 0){
			setFaceRight(true);
		}
		if(xVelocity < 0){
			setFaceRight(false);
		}
	}

	public int getYVelocity() {
		return yVelocity;
	}

	public void setYVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}
	public boolean isFaceRight() {
		return faceRight;
	}

	public void setFaceRight(boolean faceRight) {
		this.faceRight = faceRight;
	}

	public boolean isFaceDown() {
		return faceDown;
	}

	public void setFaceDown(boolean faceDown) {
		this.faceDown = faceDown;
	}

	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	public int getAttackCount() {
		return attackCount;
	}

	public void setAttackCount(int attackCount) {
		this.attackCount = attackCount;
	}

	public Rectangle getLocation() {
		return location;
	}

	public void setLocation(Rectangle location) {
		this.location = location;
	}
	
	public boolean isHitting(){
		return hitting;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void setDead(boolean dead){
		this.dead = dead;
	}
	
	public void jump(){
		jumpCount++;
	}
	
	public int getJumpCount(){
		return jumpCount;
	}
	
	public abstract void clearHitbox();
	
	public abstract void updateHitting();
	
	public abstract boolean getHit(Rectangle r);
	
	public abstract Rectangle getHitbox();
	
	
}
