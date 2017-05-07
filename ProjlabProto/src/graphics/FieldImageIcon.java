package graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.Image;

import javax.swing.*;
import main.*;

public class FieldImageIcon extends ImageIcon{
	protected Railway FieldObject;
	
	protected static Image NullImage;
	protected static Image TunnelImage;
	private static ArrayList<Image> SelectedPair =new ArrayList<Image>();
	private static ArrayList<Image> UnselectedPair =new ArrayList<Image>();

	protected Image CurrentImage;
	protected Image DefaultImage;
	private HashMap<Railway, Image> SwitchImages;

	
	public FieldImageIcon(){
		//2do wtf happened several times
		SwitchImages = new HashMap<Railway, Image>();
	}
	public FieldImageIcon(Image i){
		//2do wtf happened several times
		SwitchImages = new HashMap<Railway, Image>();
		CurrentImage = i;
		this.setImage(i);
	}
	public Railway getObject()
	{
		return FieldObject;
	}
	
	public void setSelected(boolean isSelected)
	{
		if(isSelected)
		{
			if(!SelectedPair.contains(CurrentImage))
					CurrentImage = SelectedPair.get(UnselectedPair.indexOf(CurrentImage));
		}
		if (!isSelected)
		{

			if(SelectedPair.contains(CurrentImage))
					CurrentImage = UnselectedPair.get(SelectedPair.indexOf(CurrentImage));
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
	public Railway getFieldObject() {
		return FieldObject;
	}
	public void defaultImage() {
		this.setImage(DefaultImage);		
	}
	
}
