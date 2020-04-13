import java.awt.*;

public class NumberTile extends Tile {
	//field for the number of mines around this tile
	private int numMines;
	public static final int SIZE = 20;
	
	//This type of tile is safe and contains a number telling us how many bombs there are around it
	public NumberTile(int x, int y, int maxWidth, int maxHeight, int mines) {
		super(x, y, SIZE, SIZE, maxWidth, maxHeight);
		this.numMines = mines;
	}
	
	//Draw function if the tile was clicked
	@Override
	public void draw1(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
		
		if (numMines > 0) {
			g.drawString("" + numMines, this.getPx() + 5, this.getPy() + 15);
		}
	}
	
	//Draw function if the tile was not clicked
	@Override
	public void draw2(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
	
	//Getter
	public int getNumMines() {
		return this.numMines;
	}
	
	//Setter
	public void setNumMines(int m) {
		this.numMines = m;
	}
}
