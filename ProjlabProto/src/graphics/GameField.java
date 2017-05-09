package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JPanel;

import main.Controller;
import main.Controller.CommandObserver;

public class GameField extends JPanel {

	private static final long serialVersionUID = 2437682713821930938L;
	private Tile SelectedItems[];
	private Tile gameMatrix[][];
	private Image backgroundImage;
	private Tile builtTunnelTiles[];
	
	public GameField(Image backgroundImage) {
		
		
		this.setLayout(null);
		this.setBounds(0,30,700,700);
	
		this.setBackground(Color.LIGHT_GRAY);
		
		SelectedItems = new Tile[2];
		builtTunnelTiles = new Tile[2];
		gameMatrix = new Tile[35][35];
	
		if (backgroundImage == null)
			System.out.println("A háttérkép nem található!");
		
		this.backgroundImage = backgroundImage;
		
		//TODO ezt: tunnelIcon = ImageIO.read( filepath )
	}
	
	public void select(int xCoord, int yCoord)
	{
		System.out.println(xCoord + "  " + yCoord);
		
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
			if (SelectedItems[0] != null) SelectedItems[0].setSelected(false);
			if (SelectedItems[1] != null) SelectedItems[1].setSelected(false);
			SelectedItems[0] = selected;
			SelectedItems[1] = null;
			if (SelectedItems[0] != null) SelectedItems[0].setSelected(true);
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
			if (SelectedItems[i] == null || !SelectedItems[i].refObj.contains("sb"))
				return;
		
		GameFrame.frame.controller.execute("build tunnel " + SelectedItems[0].refObj + " " + SelectedItems[1].refObj);
		SelectedItems[0].build();
		SelectedItems[1].build();
		
		builtTunnelTiles[0] = SelectedItems[0];
		builtTunnelTiles[1] = SelectedItems[1];
	}
	public void removeTunnel()
	{
		if (builtTunnelTiles[0] == null)
			return;
		
		GameFrame.frame.controller.execute("destroy tunnel");
		builtTunnelTiles[0].defaultImage();
		builtTunnelTiles[1].defaultImage();
		
		builtTunnelTiles[0] = null;
		builtTunnelTiles[1] = null;
	}
	
	public void switchTo()
	{
		if (SelectedItems[0] == null || SelectedItems[1] == null) return;
		SwitchObserver so = new SwitchObserver();
		if (SelectedItems[0].refObj.contains("sv")) {
			Controller.switchWithObserver("toggle switch " + SelectedItems[0].refObj + " " + SelectedItems[1].refObj, so);
			if (so.result = true) {
				SelectedItems[0].switchTo(SelectedItems[1].refObj);
			}
		} else if (SelectedItems[1].refObj.contains("sv")) {
			Controller.switchWithObserver("toggle switch " + SelectedItems[1].refObj + " " + SelectedItems[0].refObj, so);
			if (so.result = true) {
				SelectedItems[1].switchTo(SelectedItems[0].refObj);
			}
		}
	}
	
	private class SwitchObserver extends CommandObserver {

		@Override
		public void notify(boolean result) {
			System.out.println(result);
			this.result = result;
		}
		
	}
	/**
	 * A teljes pálya kirajzolása
	 * @param g
	 */
	public void paintField (Graphics g) {

		g.drawImage(backgroundImage, 0, 0, null);
		
		for (Tile tileRow[] : gameMatrix) {
			for (Tile tile : tileRow) {
				if (tile != null)
					tile.paintTile(g);
			}
		}
		
		//TODO mozgó elemek kirajzolása
	}
	
	public void clearField() {
		SelectedItems = new Tile[2];
		builtTunnelTiles = new Tile[2];
		gameMatrix = new Tile[35][35];
	}
	
	public void mapParser(File file) throws IOException
	{
		
		String line = "";
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			
			while((line = br.readLine()) != null)
			{
				if (line.equals("---stations---")) break;
				
				String columns[] = line.split(" ");
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
			
			while ((line = br.readLine()) != null) {
				
				String columns[] = line.split(" ");
				int x = Integer.parseInt(columns[1]);
				int y = Integer.parseInt(columns[2]);
				Tile newTile = new Tile(columns[0],new FieldImageIcon(columns[3]),x,y);
				gameMatrix[x][y] = newTile;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println(line);
		}
		
	}
}
