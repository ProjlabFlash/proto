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

	private static final long serialVersionUID = -5672822214797671703L;
	
	protected Railway FieldObject;
	protected String filename;
	protected static Image TunnelImage;
	private ArrayList<Image> SelectedPair = new ArrayList<Image>();
	private ArrayList<Image> UnselectedPair = new ArrayList<Image>();
	protected Image CurrentImage;
	protected Image DefaultImage;
	private boolean selected;
	private HashMap<String, Image> SwitchImages = new HashMap<String, Image>();

	
	public FieldImageIcon(){	
	}
	/**
	 * returns the files absolute path 
	 * paramter: realative path of the file
	 */
	private File getAbsolutePath(String relativePath) {
		File file = new File(System.getProperty("user.dir"));
		file = new File(file, "images");
		String[] pathParts = relativePath.split("\\\\");
		for (int i = 0; i < pathParts.length; i++)
			file = new File(file, pathParts[i]);
		return file;
	}
	/**
	 * ctor for FieldimageIcon
	 * gets the images path
	 */
	public FieldImageIcon(String filepath) throws IOException
	{
		File file = this.getAbsolutePath(filepath);
		filepath = file.getAbsolutePath();
		
		Image i = ImageIO.read(file);
		this.setImage(i);
		String fileparts[] = filepath.split("\\.");
		if (fileparts.length == 0) return;
		filename = fileparts[0];
		CurrentImage = DefaultImage = i;
		UnselectedPair.add(i);
		selected = false;
		try
		{
			Image selectedimage = ImageIO.read(new File(fileparts[0] + "_active.png"));
			SelectedPair.add(selectedimage);
		}
		catch(IOException e)
		{
			
		}		
	}
	/**
	 * ctor or FieldImageIcon
	 * gets additional params for the items names and it's files path
	 */
	public FieldImageIcon(String filepath, String names[], String path[]) throws IOException{
		this(filepath);
		int n = names.length;
		try
		{
		for(int i = 0; i <n; i++)
		{
			
			File file = this.getAbsolutePath(path[i]);
			String filename = file.getAbsolutePath();
			
			Image insert = ImageIO.read(file);
			UnselectedPair.add(insert);
			String fileparts[] = filename.split("\\.");
			if (fileparts.length == 0) continue;
			Image selectedimage = ImageIO.read(new File(fileparts[0] + "_active.png"));
			SelectedPair.add(selectedimage);	
			SwitchImages.put(names[i], insert);
		}
		
		}
		catch(IOException e)
		{
			
		}
	}
	/**
	 * getter for FieldObject - represents the model element
	 */
	public Railway getObject()
	{
		return FieldObject;
	}
	/**
	 * selects the image 
	 * if param = true :switches the image to the selected one
	 * if param = false: switches the image to the unselected one
	 */
	public void setSelected(boolean isSelected) throws IndexOutOfBoundsException
	{
		if(isSelected)
		{
			if(!selected)
			{
				/**
				 * Checking the list
				 */
				if(UnselectedPair.indexOf(CurrentImage) != -1)
				{
					CurrentImage = SelectedPair.get(UnselectedPair.indexOf(CurrentImage));
					this.setImage(CurrentImage);
					selected = true;
				}	
			}
		}
		if (!isSelected)
		{

			if(selected)
			{
				/**
				 * Checking the list
				 */
				if(SelectedPair.indexOf(CurrentImage) != -1)
				{
					CurrentImage = UnselectedPair.get(SelectedPair.indexOf(CurrentImage));
					this.setImage(CurrentImage);
					selected=false;
				}	
			}
		}
	}
	/**
	 * replaces the represented swithch's icon the the one 
	 * that leades to the param
	 */
	public void switchTo(String thisPosition)
	{
		CurrentImage= SwitchImages.get(thisPosition);
		if (CurrentImage != null) this.setImage(CurrentImage);
		
	}
	/**
	 * getter returns the represented object
	 */
	public Railway getFieldObject() 
	{
		return FieldObject;
	}
	/**
	 * sets the image to the default image
	 */
	public void defaultImage() 
	{
		CurrentImage = DefaultImage;
		this.setImage(DefaultImage);		
	}
	/**
	 * setter for the represented Object
	 */
	public void setFieldObject(Railway r)
	{
		FieldObject = r;
	}
}
