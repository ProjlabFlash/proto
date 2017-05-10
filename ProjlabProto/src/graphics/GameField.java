package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.Controller;
import main.Controller.CommandObserver;

public class GameField extends JPanel {

	private static final long serialVersionUID = 2437682713821930938L;
	private Tile SelectedItems[];
	private Tile gameMatrix[][];
	private Image backgroundImage;
	private Tile builtTunnelTiles[];
	private ImageIcon tunnelIcon;
	private List<ColoredIcon> moIcons;
	
	private Map<String, List<String>> trains;
	private int cartCounter = 0;
	private int trainCounter = 0;
	/*
	 * ctor: initializes the gamfeiled
	 */
	public GameField(Image backgroundImage, Image tunnelImg) {
		
		
		this.setLayout(null);
		this.setBounds(0,30,700,700);
	
		this.setBackground(Color.LIGHT_GRAY);
		
		SelectedItems = new Tile[2];
		builtTunnelTiles = new Tile[2];
		gameMatrix = new Tile[35][35];
	
		if (backgroundImage == null)
			System.out.println("A háttérkép nem található!");
		
		this.backgroundImage = backgroundImage;
		this.tunnelIcon = new ImageIcon();
		this.tunnelIcon.setImage(tunnelImg);
		this.trains = new HashMap<String, List<String>>();
		this.moIcons = new ArrayList<ColoredIcon>() ;
	}
	/**
	 * selects the Tile in the coordinates given as params
	 */
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
	/**
	 * builds a tunnel between the selected elements if allowed
	 */
	public void buildTunnel()
	{
		for (int i = 0; i < 2; i++)
			if (SelectedItems[i] == null || !SelectedItems[i].refObj.contains("sb"))
				return;
		
		GameFrame.frame.controller.execute("build tunnel " + SelectedItems[0].refObj + " " + SelectedItems[1].refObj);
		
		builtTunnelTiles[0] = SelectedItems[0];
		builtTunnelTiles[1] = SelectedItems[1];
	}
	/**
	 * removes an existing tunnel
	 */
	public void removeTunnel()
	{
		if (builtTunnelTiles[0] == null)
			return;
		
		GameFrame.frame.controller.execute("destroy tunnel");
		
		builtTunnelTiles[0] = null;
		builtTunnelTiles[1] = null;
	}
	/**
	 * sets the switch to the position selected by the user
	 */
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
	/**
	 * observer for the switch
	 */
	private class SwitchObserver extends CommandObserver {

		@Override
		public void notify(boolean result) {
			System.out.println(result);
			this.result = result;
		}
		
	}
	/**
	 * A teljes pálya kirajzolása
	 * @param g A használt grafikus kontextus
	 */
	public void paintField (Graphics g) {

		g.drawImage(backgroundImage, 0, 0, null);
		
		for (Tile tileRow[] : gameMatrix) {
			for (Tile tile : tileRow) {
				if (tile != null)
					tile.paintTile(g);
			}
		}
		
		for (ColoredIcon ci: moIcons) {
			if (ci.getX() >= 0 && ci.getY() >= 0)
				ci.paintIcon(GameFrame.frame.canvas, g, ci.getX() * 20, ci.getY() * 20);
		}
		
		if (builtTunnelTiles[0] != null) {
			tunnelIcon.paintIcon(GameFrame.frame.canvas, g, builtTunnelTiles[0].xCoord * 20, builtTunnelTiles[0].yCoord * 20);
		}
		if (builtTunnelTiles[1] != null) {
			tunnelIcon.paintIcon(GameFrame.frame.canvas, g, builtTunnelTiles[1].xCoord * 20, builtTunnelTiles[1].yCoord * 20);
		}
	}
	/**
	 * returns the Tile from it's key string
	 */
	public Tile getTileFromKey(String key) {
		for (int i = 0; i < 35; i++) 
			for (int j = 0; j < 35; j++)
				if (gameMatrix[i][j] != null && gameMatrix[i][j].refObj.equals(key))
					return gameMatrix[i][j];
		return null;
	}
	/**
	 * clears the field
	 */
	public void clearField() {
		SelectedItems = new Tile[2];
		builtTunnelTiles = new Tile[2];
		gameMatrix = new Tile[35][35];
		builtTunnelTiles = new Tile[2];
		moIcons = new ArrayList<ColoredIcon>();
		
		trains = new HashMap<String, List<String>>();
		cartCounter = 0;
		trainCounter = 0;
	}
	/**
	 * parses the map elemnts into field elements (images) in Tiels
	 */
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
			
			if (line != null)
				while((line = br.readLine()) != null) {
					
					if (line.equals("---trains---")) break;
					
					String columns[] = line.split(" ");
					int x = Integer.parseInt(columns[1]);
					int y = Integer.parseInt(columns[2]);
					Tile newTile = new Tile(columns[0],new FieldImageIcon(columns[3]),x,y);
					gameMatrix[x][y] = newTile;
				}
			
			while (line != null) {

				List<String> carts = new ArrayList<String>();
				while ((line = br.readLine()) != null && !line.contains("locomotive")) {
					carts.add(line);
				}
				
				if (line == null) break;
				carts.add(line);
				String[] columns = line.split(" ");
				long milis = Long.parseLong(columns[columns.length - 2]);
				String key = columns[0];
				
				synchronized (trains) { trains.put(key, carts);}
				Timer t = new Timer();
				t.schedule(new TrainStarter(key), milis);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println(line);
		}
		
	}
	/**
	 * private class for starting trains
	 */
	private class TrainStarter extends TimerTask {
		
		String key;
		
		TrainStarter(String key) {this.key = key;}
		//Teszt
		@Override
		public void run() {
			synchronized (trains) {
				List<String> commands = trains.remove(key);
				if (commands == null) {
					System.err.println("Train parsing error!");
					return;
				}
				
				GameFrame.frame.controller.execute("prepare train");
				for (String command: commands) {
					String[] columns = command.split(" ");
					boolean first = true;
					File imageFile = new File (System.getProperty("user.dir"));
					imageFile = new File (imageFile, "images");
					imageFile = new File (imageFile, "MovingObject");
					imageFile = new File (imageFile, columns[columns.length - 1]);
					
					if (!columns[0].equals("locomotive")) {
						GameFrame.frame.controller.execute("add cart " + command);
						String key = "mc" + (++cartCounter);
						moIcons.add(new ColoredIcon(-1, -1, imageFile.getAbsolutePath(), key, first));
						first = false;
					} else {
						GameFrame.frame.controller.execute("add loco " + columns[1] + " " + columns[2] + " " + columns[3]);
						String key = "ml" + (++trainCounter);
						moIcons.add(new ColoredIcon(-1, -1, imageFile.getAbsolutePath(), key, first));
					}
				}
				
				GameFrame.frame.controller.execute("timer start " + "ml" + (trainCounter));
			}
			GameFrame.frame.canvas.repaint();
		}
	}
}
