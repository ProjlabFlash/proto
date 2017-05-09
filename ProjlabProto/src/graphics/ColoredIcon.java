package graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.MovingObject;
import main.Railway;
import main.Station;
public class ColoredIcon extends FieldImageIcon {
	private MovingObject Vehicle;
	private Image EmptyIcon;
	private boolean filled;
	private static ArrayList<Image> FilledPair = new ArrayList<Image>();
	private static ArrayList<Image> UnfilledPair = new ArrayList<Image>();
	private int x;
	private int y;
	
	public ColoredIcon(int xCoord, int yCoord, String imagepath) throws IOException
	{
		String[] crop = imagepath.split("\\.");
		if(crop.length == 0) return;
		filename = crop[0];
		FieldObject = null;
		DefaultImage = ImageIO.read(new File(imagepath));
		this.setImage(DefaultImage);
		CurrentImage = DefaultImage;		
		Image unfilled = ImageIO.read(new File(filename.replace("full", "empty")));
		FilledPair.add(CurrentImage);
		UnfilledPair.add(unfilled);
		filled = false;
		
		
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
	public void stepToHere(Railway toHere)
	{
		// 2 do nemtom hogy lesz a map megoldva
	}
}
