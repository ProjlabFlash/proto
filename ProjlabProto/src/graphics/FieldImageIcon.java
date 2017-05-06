package graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.Image;

import javax.swing.*;
import main.*;

public class FieldImageIcon extends ImageIcon{
	private Railway FieldObject;
	private static Image NullImage;
	private static Image TunnelImage;
	private Image CurrentImage;
	private HashMap<Railway, Image> SwitchImages;
	private ArrayList<Image> ReplacementImages;
	
	public FieldImageIcon(){
		ReplacementImages = new ArrayList<Image>();
		SwitchImages = new HashMap<Railway, Image>();
	}
	public FieldImageIcon(Image i){
		ReplacementImages = new ArrayList<Image>();
		SwitchImages = new HashMap<Railway, Image>();
		CurrentImage = i;
		this.setImage(i);
	}
	public Railway getObject()
	{
		return FieldObject;
	}
	public void setVisible(boolean isVisible)
	{
		if(isVisible)
			this.setImage(ReplacementImages.get(0));
		if(!isVisible)
			this.setImage(NullImage);
	}
	public void setSelected(boolean isSelected)
	{
		if(isSelected)
		{
			if(ReplacementImages.indexOf(CurrentImage) % 2 != 0)
				if(ReplacementImages.indexOf(CurrentImage) != 0)
					this.setImage(ReplacementImages.get(ReplacementImages.indexOf(CurrentImage)+1));
		}
		if(!isSelected)
		{
			if(ReplacementImages.indexOf(CurrentImage) % 2 == 0)
				if(ReplacementImages.indexOf(CurrentImage) != 0)
					this.setImage(ReplacementImages.get(ReplacementImages.indexOf(CurrentImage)-1));
		}
	}
	public void build()
	{
		this.setImage(TunnelImage);
	}
	public void switchTo(Railway thisPosition)
	{
		this.setImage(SwitchImages.get(thisPosition));
	}
}
