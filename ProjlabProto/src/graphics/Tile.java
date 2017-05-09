package graphics;

import java.awt.Graphics;

public class Tile {

	final public int xCoord;
	final public int yCoord;
	final public String refObj;
	private FieldImageIcon image;
	
	public Tile(String refObj, FieldImageIcon image, int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.image = image;
		this.refObj = refObj;
	}
	
	public void paintTile(Graphics g) {
		image.paintIcon(GameFrame.frame.canvas, g, xCoord * 20, yCoord * 20);
	}
	
	public void setSelected(boolean b) {
		image.setSelected(b);
	}
	
	public void switchTo(String thisPosition)
	{
		image.switchTo(thisPosition);
	}
	
	public void build() {
		image.build();
	}

	public void defaultImage() {
		image.defaultImage();
	}
}
