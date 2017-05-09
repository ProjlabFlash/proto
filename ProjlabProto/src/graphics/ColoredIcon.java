package graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Controller.MovingObjectObserver;

public class ColoredIcon extends FieldImageIcon {
	private static final long serialVersionUID = -7817118272861870690L;
	
	private boolean filled;
	private static ArrayList<Image> FilledPair = new ArrayList<Image>();
	private static ArrayList<Image> UnfilledPair = new ArrayList<Image>();
	private int x;
	private int y;
	private MoObserver moo;
	
	public ColoredIcon(int xCoord, int yCoord, String imagepath, String key)
	{
		String[] crop = imagepath.split("\\.");
		if(crop.length == 0) return;
		filename = crop[0];
		FieldObject = null;
		
		try {
			DefaultImage = ImageIO.read(new File(imagepath));
		} catch (IOException e) {
			System.err.println("Sikertelen kép parsolás: Vonatelem");
		}
		
		this.setImage(DefaultImage);
		CurrentImage = DefaultImage;	
		
		Image unfilled = null;
		try {
			unfilled = ImageIO.read(new File(filename.replace("full", "empty")));
		} catch (IOException e) {
		}
		
		FilledPair.add(CurrentImage);
		UnfilledPair.add(unfilled);
		filled = false;
		x = xCoord;
		y = yCoord;
		
		moo = new MoObserver(this);
		GameFrame.frame.controller.registerObserver(key, moo);
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setFilled(boolean isFilled)
	{
		if(isFilled)
		{
			if(!filled)
			{
				if(UnfilledPair.indexOf(CurrentImage) != -1)
				{
				CurrentImage = FilledPair.get(UnfilledPair.indexOf(CurrentImage));
				this.setImage(CurrentImage);
				filled = true;
				}
			}
		}
		if(!isFilled)
		{
			if(filled)
			{
				if(FilledPair.indexOf(CurrentImage) != -1)
				{
				CurrentImage = UnfilledPair.get(FilledPair.indexOf(CurrentImage));
				this.setImage(CurrentImage);
				filled = false;
				}
			}
		}
	}
	
	private class MoObserver extends MovingObjectObserver {

		ColoredIcon ci;
		
		private MoObserver(ColoredIcon ci) { this.ci = ci;}
		
		@Override
		public void updatePos(String railKey) {
			Tile tile = GameFrame.frame.field.getTileFromKey(railKey);
			if (tile == null) {
				x = -1;
				y = -1;
			} else {
				x = tile.xCoord;
				y = tile.yCoord;
				GameFrame.frame.canvas.repaint();
			}
			
		}

		@Override
		public void updatePassengers(Boolean value) {
			ci.setFilled(value);
		}
		
	}
	public void stepToHere(String toHere)
	{
		//TODO nemtom hogy lesz a map megoldva
	}
}
