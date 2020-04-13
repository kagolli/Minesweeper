import java.awt.*;

public class Mine extends Tile {
	public static final int SIZE = 20;
	
	//This type of tile is dangerous and when clicked on, makes you lose the game
	public Mine(int x, int y, int maxWidth, int maxHeight) {
		super(x, y, SIZE, SIZE, maxWidth, maxHeight);
		setIsMine(true);
	}
	
	//Draw function if it was clicked
	@Override
	public void draw1(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
	
	//Draw function if it was not clicked
	@Override
	public void draw2(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
}
