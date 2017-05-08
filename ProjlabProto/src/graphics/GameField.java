package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

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
		
		GameFrame.frame.controller.execute("build tunnel " + SelectedItems[0].refObj + " " + SelectedItems[1].refObj);
		SelectedItems[0].build();
		SelectedItems[1].build();
	}
	public void removeTunnel()
	{
		GameFrame.frame.controller.execute("destroy tunnel");
		SelectedItems[0].defaultImage();
		SelectedItems[1].defaultImage();
	}
	public void switchTo()
	{
		//TODO vegbemegy-e a valtas
		//FieldImageIcon switchTo[] = getSelectedItems();
		//switchTo[0].switchTo(switchTo[2].getFieldObject());
	}
	/**
	 * A teljes pálya kirajzolása
	 * @param g
	 */
	public void paintField (Graphics g) {
		
		for (Tile tileRow[] : gameMatrix) {
			for (Tile tile : tileRow) {
				if (tile != null)
					tile.paintTile(g);
			}
		}
		
		//TODO mozgó elemek kirajzolása
	}
	public void mapParser(String file) throws IOException
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null)
			{
				String columns[] = line.split(",");
				int x = Integer.parseInt(columns[1]);
				int y = Integer.parseInt(columns[2]);
				if (columns.length == 4)
				{
				Tile newTile = new Tile(columns[0],new FieldImageIcon(columns[3]),x,y);
				gameMatrix[x][y] = newTile;
				}
				if(columns.length > 4)
				{
					String paths[] = new String[(columns.length-4) /2];
					String names[] = new String[(columns.length-4) /2];
					for(int i = 4, j = 0; i < columns.length; i= i+2, j++)
					{
						names[j] =columns[i];
						paths[j] = columns[i+1];
					}
					Tile newTile = new Tile(columns[0],new FieldImageIcon(columns[3],names,paths),x,y);
					gameMatrix[x][y] = newTile;
				}
			}
			
			
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
