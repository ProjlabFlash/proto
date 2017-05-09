package graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ColoredIcon extends FieldImageIcon {
	private static final long serialVersionUID = -7817118272861870690L;
	
	private boolean filled;
	private static ArrayList<Image> FilledPair = new ArrayList<Image>();
	private static ArrayList<Image> UnfilledPair = new ArrayList<Image>();
	private int x;
	private int y;
	
	public ColoredIcon(int xCoord, int yCoord, String imagepath)
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
		
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setX(int newX)
	{
		x= newX;
	}
	public void setY(int newY)
	{
		y = newY;
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
	public void stepToHere(String toHere)
	{
		// 2 do nemtom hogy lesz a map megoldva
	}
}
