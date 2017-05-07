package graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

public class GameField extends JPanel {

	private FieldImageIcon SelectedItems[];
	private FieldImageIcon GameMatrix[][];
	public GameField() {
		
		
		this.setLayout(null);
		this.setBounds(0,30,700,700);
	
		this.setBackground(Color.LIGHT_GRAY);
		
		SelectedItems = new FieldImageIcon[2];
	}
	
	public FieldImageIcon[] getSelectedItems()
	{
		return SelectedItems;
	}
	public void select(FieldImageIcon selected)
	{
		if (SelectedItems[0] == null)
		{
			
			SelectedItems[0] = selected;
			SelectedItems[0].setSelected(true);
		}
		else if(SelectedItems[0] != null && SelectedItems[1] == null)
		{
			SelectedItems[1] = selected;
			SelectedItems[1].setSelected(true);
		}
		else if(SelectedItems[0] != null && SelectedItems[1] != null)
		{
			SelectedItems[0].setSelected(false);
			SelectedItems[1].setSelected(false);
			SelectedItems[0] = selected;
			SelectedItems[1] = null;
			SelectedItems[0].setSelected(true);
		}
	}
	public void actionPerformed(ActionEvent ae)
	{
		FieldImageIcon fim = (FieldImageIcon) ae.getSource();
		select(fim);		
	}
	public void buildTunnel()
	{
		// 2do buildingspot-e mindketto
		FieldImageIcon buildingspots[] = getSelectedItems();
		buildingspots[0].build();
		buildingspots[1].build();
	}
	public void removeTunnel()
	{
		//2do van-e kesz tunnel
		FieldImageIcon restores[] = getSelectedItems();
		restores[0].defaultImage();
		restores[1].defaultImage();
	}
	public void switchTo()
	{
		//2do vegbemegy-e a valtas
		FieldImageIcon switchTo[] = getSelectedItems();
		switchTo[0].switchTo(switchTo[2].getFieldObject());
	}	
}
