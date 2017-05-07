package graphics;
import java.awt.Image;
import java.util.ArrayList;
import main.MovingObject;
import main.Railway;
import main.Station;
public class ColoredIcon extends FieldImageIcon {
	private MovingObject Vehicle;
	private Station Station;
	private Image EmptyIcon;
	private static ArrayList<Image> FilledPair = new ArrayList<Image>();
	private static ArrayList<Image> UnfilledPair = new ArrayList<Image>();
	public void setFilled(boolean isFilled)
	{
		if(isFilled)
		{
			if(!FilledPair.contains(CurrentImage))
				CurrentImage = FilledPair.get(UnfilledPair.indexOf(CurrentImage));
		}
		if(!isFilled)
		{
			if(FilledPair.contains(CurrentImage))
				CurrentImage = UnfilledPair.get(FilledPair.indexOf(CurrentImage));
		}
	}
	public void stepToHere(Railway toHere)
	{
		// 2 do nemtom hogy lesz a map megoldva
	}
}
