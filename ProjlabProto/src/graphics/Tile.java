package graphics;

import java.awt.Graphics;

public class Tile {

	final public int xCoord;
	final public int yCoord;
	final public String refObj;
	private FieldImageIcon image;
	/**
	 * ctor: initializes attrivutes
	 * @param refObj
	 * @param image
	 * @param xCoord
	 * @param yCoord
	 */
	public Tile(String refObj, FieldImageIcon image, int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.image = image;
		this.refObj = refObj;
	}
	/**
	 * paints the Tile 
	 */
	public void paintTile(Graphics g) {
		image.paintIcon(GameFrame.frame.canvas, g, xCoord * 20, yCoord * 20);
	}
	/**
	 * sets the tile's image selected if the param is true
	 * setsthe tile's image unselected if the paeram is false
	 */
	public void setSelected(boolean b) {
		image.setSelected(b);
	}
	/**
	 * sets the Tiles image to a nother standing of the switch
	 */
	public void switchTo(String thisPosition)
	{
		image.switchTo(thisPosition);
	}
	/**
	 * sets the image to it's default 
	 */
	public void defaultImage() {
		image.defaultImage();
	}
}
