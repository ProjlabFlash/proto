package graphics;

import java.awt.Graphics;
import java.awt.Image;

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
		Image img = null; //TODO kép betöltése.
		g.drawImage(img, xCoord * 16, yCoord * 16, 16, 16, null);
	}
	
	public void setSelected(boolean b) {
		image.setSelected(true);
	}
	
	public void build() {
		image.build();
	}

	public void defaultImage() {
		image.defaultImage();
	}
}
