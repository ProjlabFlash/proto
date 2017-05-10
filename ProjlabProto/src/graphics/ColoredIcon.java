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
	private String key;
	private boolean repaintOnDrawing;
	private MoObserver moo;
	/*
	 * Ctor
	 * initializes all nessesary attributes
	 * 
	 */
	public ColoredIcon(int xCoord, int yCoord, String imagepath, String key, boolean repaintOnDrawing)
	{
		
		this.key = key;
		this.repaintOnDrawing = repaintOnDrawing;
		if(imagepath == null) return;
		filename = imagepath;
		FieldObject = null;	
		try {
			DefaultImage = ImageIO.read(new File(imagepath));
		} catch (IOException e) {
			System.err.println("Sikertelen kép parsolás: Vonatelem");
		}
		
		this.setImage(DefaultImage);
		CurrentImage = DefaultImage;	
		/*
		 * Initializing unfilled pair
		 * And Filled pair 
		 */
		Image unfilled = null;
		try {
			unfilled = ImageIO.read(new File(filename.replace("full", "empty")));
		} catch (IOException e) {
		}
		
		FilledPair.add(CurrentImage);
		UnfilledPair.add(unfilled);
		filled = true;
		x = xCoord;
		y = yCoord;
		
		moo = new MoObserver(this);
		GameFrame.frame.controller.registerObserver(key, moo);
	}
	/*
	 * Getter for X coord 
	 */
	public int getX()
	{
		return x;
	}
	/*
	 * Getter for Y coord 
	 */
	public int getY()
	{
		return y;
	}
	/*
	 * sets the image filled if the parameter is true
	 * sets the image unfilled if the parameter is false 
	 */
	public void setFilled(boolean isFilled)
	{
		if(isFilled)
		{
			if(!filled)
			{
				/*
				 * Checking the list
				 */
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
				/*
				 * Checking the list
				 */
				if(FilledPair.indexOf(CurrentImage) != -1)
				{
				CurrentImage = UnfilledPair.get(FilledPair.indexOf(CurrentImage));
				this.setImage(CurrentImage);
				filled = false;
				}
			}
		}
	}
	/*
	 * private class for handling mouseevents
	 */
	private class MoObserver extends MovingObjectObserver {

		ColoredIcon ci;
		
		private MoObserver(ColoredIcon ci) { this.ci = ci;}
		/*
		 * Updates the position of the image
		 */
		@Override
		public void updatePos(String railKey) {
			Tile tile = GameFrame.frame.field.getTileFromKey(railKey);
			if (tile == null) {
				x = -1;
				y = -1;
			} else {
				x = tile.xCoord;
				y = tile.yCoord;
				if (repaintOnDrawing)
					GameFrame.frame.canvas.repaint();
			}
			
		}
		/*
		 * updates the image according to the passangers
		 */
		@Override
		public void updatePassengers(Boolean value) {
			ci.setFilled(value);
		}
		
	}
}
