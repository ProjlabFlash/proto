package graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

public class GameField extends JPanel {

	private Tile SelectedItems[];
	private Tile gameMatrix[][];
	public GameField() {
		
		
		this.setLayout(null);
		this.setBounds(0,30,700,700);
	
		this.setBackground(Color.LIGHT_GRAY);
		
		SelectedItems = new Tile[2];
	}
	
	public void select(int xCoord, int yCoord)
	{
		
		Tile selected = gameMatrix[xCoord / 20][yCoord / 20];
		
		if (SelectedItems[0] == null && selected != null)
		{
			
			SelectedItems[0] = selected;
			SelectedItems[0].setSelected(true);
		}
		else if(SelectedItems[0] != null && SelectedItems[1] == null && selected != null)
		{
			SelectedItems[1] = selected;
			SelectedItems[1].setSelected(true);
		}
		else if((SelectedItems[0] != null && SelectedItems[1] != null) || selected == null)
		{
			SelectedItems[0].setSelected(false);
			SelectedItems[1].setSelected(false);
			SelectedItems[0] = selected;
			SelectedItems[1] = null;
			SelectedItems[0].setSelected(true);
		}
	}
	/**
	public void actionPerformed(ActionEvent ae)
	{
		FieldImageIcon fim = (FieldImageIcon) ae.getSource();
		select(fim);		
	}*/
	
	public void buildTunnel()
	{
		for (int i = 0; i < 2; i++)
			if (!SelectedItems[i].refObj.contains("sb"))
				return;
		//TODO építés megcsinálása
	}
	public void removeTunnel()
	{
		//TODO van-e kesz tunnel
		//FieldImageIcon restores[] = getSelectedItems();
		//restores[0].defaultImage();
		//restores[1].defaultImage();
	}
	public void switchTo()
	{
		//TODO vegbemegy-e a valtas
		//FieldImageIcon switchTo[] = getSelectedItems();
		//switchTo[0].switchTo(switchTo[2].getFieldObject());
	}
	
	//TODO pályaparser
}
