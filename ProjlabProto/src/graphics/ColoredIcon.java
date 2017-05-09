package graphics;
import java.awt.Image;
import java.util.ArrayList;
import main.MovingObject;
import main.Railway;
import main.Station;
public class ColoredIcon extends FieldImageIcon {
	private MovingObject Vehicle;
	private Image EmptyIcon;
	private boolean filled;
	private static ArrayList<Image> FilledPair = new ArrayList<Image>();
	private static ArrayList<Image> UnfilledPair = new ArrayList<Image>();
	
	public ColoredIcon()
	{
			// 2Do Everything
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
