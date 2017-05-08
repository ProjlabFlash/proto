package graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import main.*;

public class FieldImageIcon extends ImageIcon{
	protected Railway FieldObject;
	protected String filename;
	protected static Image TunnelImage;
	private static ArrayList<Image> SelectedPair = new ArrayList<Image>();
	private static ArrayList<Image> UnselectedPair = new ArrayList<Image>();
	protected Image CurrentImage;
	protected Image DefaultImage;
	private HashMap<String, Image> SwitchImages;

	
	public FieldImageIcon(){
		//2do wtf happened several times
		SwitchImages = new HashMap<String, Image>();
	}
	public FieldImageIcon(String file) throws IOException
	{
		Image i = ImageIO.read(new File(file));
		this.setImage(i);
		String fileparts[] = file.split(".");
		filename = fileparts[0];
		DefaultImage = i;
		UnselectedPair.add(i);
		try
		{
			Image selected = ImageIO.read(new File(file + "_active.png"));
			SelectedPair.add(selected);
		}
		catch(IOException e)
		{
			
		}		
	}
	public FieldImageIcon(String file, String names[], String path[]) throws IOException{
		this(file);
		int n = names.length;
		try
		{
		for(int i = 0; i <n; i++)
		{
			Image insert = ImageIO.read(new File(path[i]));
			UnselectedPair.add(insert);
			String fileparts[] = path[i].split(".");
			filename = fileparts[0];
			Image selected = ImageIO.read(new File(filename + "_active.png"));
			SelectedPair.add(selected);	
			SwitchImages.put(names[i], insert);
		}
		
		}
		catch(IOException e)
		{
			
		}
	}
	public Railway getObject()
	{
		return FieldObject;
	}
	
	public void setSelected(boolean isSelected) throws IndexOutOfBoundsException
	{
		if(isSelected)
		{
			if(!SelectedPair.contains(CurrentImage))
					if(FieldObject != null)
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
	public Railway getFieldObject() 
	{
		return FieldObject;
	}
	public void defaultImage() 
	{
		this.setImage(DefaultImage);		
	}
	public void setFieldObject(Railway r)
	{
		FieldObject = r;
	}
}
