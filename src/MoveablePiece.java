import java.awt.Color;
import java.awt.Graphics;

public class MoveablePiece extends Circle{
	
	private int pos;
	
	public int getCurPos() {
		return pos;
	}
	
	public MoveablePiece(int courtWidth, int courtHeight, Color color) {
		super(courtWidth, courtHeight, color);
	}
	
	public void setPos(int pos, int others) {
		
		this.pos = pos;
		//indicates which direction to move if there are multiple pieces on the same position
		int change = -1;
		if (pos > 12) change = 1;
		
		if (pos == 0) {
			this.setPx(538);
			this.setPy(0);
			
			if (this.color == Color.WHITE) {
				change = -1;
				this.setPy(this.getCourtHeight() - this.getHeight());
			}
			else {
				change = 1;
				this.setPy(0);
			}
			this.setPy(this.getPy() + (change * this.getHeight() * others));
			
			return; 
		} else if (pos == 25) { //-------------------------------------------
			this.setPx(14);
			this.setPy(14 + ((16) * others));
			
			if (this.color == Color.BLACK) {
				this.setPy(this.getCourtHeight() - this.getPy()  - 15);
			}
			
			return;
		}
		
    	
		if (pos <= 12) this.setPy(this.getCourtHeight() - this.getHeight());
		else this.setPy(0);
		
		if (others <= 5) this.setPy(this.getPy() + (change * this.getHeight() * others));
		
		this.setPx(135 + ((pos - 1) * 68));
		
    	if (pos > 6) this.setPx(this.getPx() + 60);
		
    	if (pos > 12) {
			this.setPx((943 * 2) - this.getPx() + 65);
			if (pos > 18) this.setPx(this.getPx() - 56);
		}
		
	}
	
	@Override
    public void draw(Graphics g) {
		if (this.pos == 25) {
			g.setColor(this.color);
	        g.fillRect(this.getPx(), this.getPy(), 89, 15);
		}else {
	        
	        if (this.color == Color.BLACK) {
	        	g.setColor(Color.WHITE);
	            g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	        	g.setColor(Color.BLACK);
	        } else {
	        	g.setColor(Color.BLACK);
	            g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	        	g.setColor(Color.WHITE);
	        	
	        }
	        
	        g.fillOval(this.getPx() + 5, this.getPy() + 5, this.getWidth() - 10, this.getHeight() - 10);
		}
    }
}
