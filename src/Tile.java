
import java.awt.Graphics;

public abstract class Tile {
	//x and y coordinates
	private int px;
	private int py;
	
	//size
	private int width;
	private int height;
	
	//max coordinates
	private int maxX;
	private int maxY;
	
	//tells us whether or not the tile is a mine
	private boolean isMine;
	
	//tells us whether or not the tile was clicked
	private boolean wasClicked;
	
	public Tile(int x, int y, int width, int height, int maxWidth, int maxHeight) {
		this.px = x;
		this.py = y;
		
		this.width = width;
		this.height = height;
		
		this.maxX = maxWidth - width;
		this.maxY = maxHeight - height;
	}
	
	//Getters
	public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public boolean getIsMine() {
    	return this.isMine;
    }
    
    public boolean getWasClicked() {
    	return this.wasClicked;
    }
    
    //Setters
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }
    
    public void setIsMine(boolean b) {
    	this.isMine = b;
    }
    
    public void setWasClicked(boolean b) {
    	this.wasClicked = b;
    }
    
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }
    
    @Override
    public boolean equals(Object o) {
    	if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Tile other = (Tile) o; 
        if (px != other.px || py != other.py) {
           return false;
        }
        return true;
    }
    
    public abstract void draw1(Graphics g);
    public abstract void draw2(Graphics g);
    
    
}
