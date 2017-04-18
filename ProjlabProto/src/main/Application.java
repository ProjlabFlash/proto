package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Application {

	//Becsomagolt stdin, es stdout
	private static PrintStream stdoutWriter;
	private static BufferedReader stdinReader;
	
	//Az eppen aktualis kimeneti, es bemeneti stream
	private static PrintStream targetOS;
	private static BufferedReader targetIS;
	
	//Fajlokba/-bol valo atiranyitashoz.
	private static FileInputStream fileReader = null;
	private static FileOutputStream fileWriter = null;
	
	//A palya elemei
	private static Map<String, Railway> rails = new HashMap<String, Railway>();
	private static int railsCounter = 0;
	
	private static Map<String, Switch> switches = new HashMap<String, Switch>();
	private static int switchesCounter = 0;
	
	private static Map<String, CrossRailway> crosses = new HashMap<String, CrossRailway>();
	private static int crossesCounter = 0;
	
	private static Map<String, BuildingSpot> buildingSpots = new HashMap<String, BuildingSpot>();
	private static int buildingSpotsCounter = 0;
	
	private static Map<String, Station> stations = new HashMap<String, Station>();
	private static int stationsCounter = 0;
	
	private static Map<String, SimultanStation> simultanStations = new HashMap<String, SimultanStation>();
	
	private static Map<String, Cart> carts = new HashMap<String, Cart>();
	private static int cartsCounter = 0;
	
	private static Map<String, Locomotive> locos = new HashMap<String, Locomotive>();
	private static int locosCounter = 0;
	
	private static Tunnel tunnel = null;
	
	//A prepare train nullazza, amugy pedig az epitendo vonat legutobb letett kocsijat tartalmazza
	private static String lastCart = null; 
	private static Boolean isFinished = true;
	
	public static void main(String[] args) {
		
		targetOS = stdoutWriter = System.out;
		targetIS = stdinReader = new BufferedReader(new InputStreamReader(System.in));
		

		
		List<CommandBase> commands = new ArrayList<CommandBase> ();
		commands.add(new CmdSwitchInput());
		commands.add(new CmdSwitchOutput());
		commands.add(new CmdAddRail());
		commands.add(new CmdAddBuildingSpot());
		commands.add(new CmdAddCrossway());
		commands.add(new CmdAddSwitch());
		commands.add(new CmdAddStation());
		commands.add(new CmdConnectRail());
		//commands.add(new CmdDeleteRail());
		//commands.add(new CmdDeleteStation());
		commands.add(new CmdList());
		//commands.add(new CmdExploreLine());
		//commands.add(new CmdExploreRail());
		//commands.add(new CmdExploreStation());
		//commands.add(new CmdExploreAllrail());
		//commands.add(new CmdToggleSwitch());
		//commands.add(new CmdToggleStation());
		commands.add(new CmdBuildTunnel());
		commands.add(new CmdDestroyTunnel());
		commands.add(new CmdPrepareTrain());
		commands.add(new CmdAddCart());
		commands.add(new CmdAddLoco());
		commands.add(new CmdStep());
		//commands.add(new CmdDeleteLoco());
		commands.add(new CmdExploreLoco());
		commands.add(new CmdExploreCart());
		//commands.add(new CmdTimerStart());
		//commands.add(new CmdTimerEnd());
		//commands.add(new CmdExploreSwitch());
		commands.add(new CmdClearTable());
	
		mainloop:
		while(true) {
			try {
				String line = targetIS.readLine();
				if (line == null)
					if (fileReader != null && fileReader.available() == 0) {
						targetIS = stdinReader;
						continue mainloop;
				}
				String[] params = line.split(" ");
				
				if (params.length == 0)
					continue mainloop;
				
				for (int i = 0; i < commands.size(); i++)
					if (commands.get(i).cmdName.equals(params[0])) {
						commands.get(i).execute(params);
						continue mainloop;
					}
				
				if (params.length == 1) {
					System.out.println("A parancs nem felismerheto!");
					continue mainloop;
				}
				
				String newcmd = params[0] + " " + params[1];
				
				for (int i = 0; i < commands.size(); i++)
					if (commands.get(i).cmdName.equals(newcmd)) {
						commands.get(i).execute(params);
						continue mainloop;
					}
				
				System.out.println("A parancs nem felismerheto!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendMessage(String msg) {
		targetOS.println(msg);
	}
	
	public static void sendMessage(String msg, Station station) {
		Set<Entry<String, Station> > stationSet = stations.entrySet();
		Set<Entry<String, SimultanStation>> simultanStationSet = simultanStations.entrySet();
		String key = null;
		for (Map.Entry<String, Station> entry: stationSet)
			if (entry.getValue() == station) key = entry.getKey();
		if (key == null)
			for (Map.Entry<String, SimultanStation> entry: simultanStationSet)
				if (entry.getValue() == station) key = entry.getKey();
		
		msg.replace("san", key);
		targetOS.println(msg);
	}
	
	public static void sendMessage(String msg, Cart cart) {
		
		Set<Entry<String, Cart> > cartSet = carts.entrySet();
		String key = null;
		for (Map.Entry<String, Cart> entry: cartSet)
			if (entry.getValue() == cart) key = entry.getKey();
		
		msg.replace("mcn", key);
		targetOS.println(msg);
	}
	public static abstract class CommandBase {
		
		public final String cmdName;
		
		public CommandBase (String cmdName) {
			this.cmdName = cmdName;
		}
		
		public abstract void execute(String[] params);
		
	}
	
	
	private static class CmdSwitchInput extends CommandBase {

		public CmdSwitchInput() {
			super("redirect input");
		}

		@Override
		public void execute(String[] params) {
			
			boolean isSilent = false;
			
			if (params.length < 3) {
				//targetOS.println("Nincs eleg parameter!");
				return;
			}
			
			if (params.length > 3) {
				if (params[3].equals("silent"))
					isSilent = true;
			}
			
			if (params[2].equals("stdin")) {
				targetIS = stdinReader;
				if (!isSilent) targetOS.println("A bemenet sikeresen atiranyitva a standard bemenetre");
			}
			else {

				File dir = new File(System.getProperty("user.dir"));
				dir = new File(dir, "testFiles");
				dir = new File(dir, params[2]);
				
				try {

					if (fileReader != null)
						fileReader.close();
					if (!dir.isFile()) throw new FileNotFoundException();
					
					fileReader = new FileInputStream(dir);
					targetIS = new BufferedReader(new InputStreamReader(fileReader, "UTF-8"));
					

					if (!isSilent) {
						targetOS.println("A bemenet sikeresen atiranyitva a leirt fileba:");
						targetOS.println(dir.getAbsolutePath());
					}

				}  catch (FileNotFoundException e) {
					if (!isSilent) targetOS.println("A megadott fajl nem talalhato");
					if (!isSilent) targetOS.println(dir.getAbsolutePath());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static class CmdSwitchOutput extends CommandBase {

		public CmdSwitchOutput() {
			super("redirect output");
		}

		@Override
		public void execute(String[] params) {
			boolean isSilent = false;
			if (params.length < 3) {
				//targetOS.println("Nincs eleg parameter!");
				return;
			}
			
			if (params.length > 3) {
				if (params[3].equals("silent"))
					isSilent = true;
			}
			
			if (params[2].equals("stdout")) {
				targetOS = stdoutWriter;
				if (!isSilent) targetOS.println("A kimenet sikeresen atiranyitva a standard kimenetre");
			}
			else {

				File dir = new File(System.getProperty("user.dir"));
				dir = new File(dir, "dataFiles");
				dir = new File(dir, params[2]);
				
				try {

					if (fileWriter != null)
						fileWriter.close();
					
					dir.createNewFile();
					fileWriter = new FileOutputStream(dir);
					targetOS = new PrintStream(fileWriter);
					

					if (!isSilent) {
						targetOS.println("A kimenet sikeresen atiranyitva a leirt fileba:");
						targetOS.println(dir.getAbsolutePath());
					}

				}  catch (IOException e) {
					if (!isSilent) targetOS.println("Hiba tortent a fajl irasra valo megnyitasakor:");
					if (!isSilent) targetOS.println(dir.getAbsolutePath());
					if (!isSilent) targetOS.println("A parancs hatastalan!");
				}
			}
		}
	}
	
	private static class CmdAddRail extends CommandBase{

		  public CmdAddRail() {
		   super("add rail");
		  }

		  @Override
		  public void execute(String[] params) {
		   
		   Railway tbConnected = null;
		   
		   if (params.length > 2) {
		    
		    tbConnected = rails.get(params[2]);
		    if (tbConnected == null) tbConnected = buildingSpots.get(params[2]);
		    
		    if (tbConnected == null) {
		     sendMessage("Sikertelen. A megadott sin nem letezik.");
		     return;
		    }
		   }
		   
		   String key = "ss" + (++railsCounter);
		   Railway newRailway = new Railway(tbConnected);
		   rails.put(key, newRailway);
		   sendMessage("Sikerult! Az uj sin azonositoja: " + key);
		  }
		 }
	
	private static class CmdAddBuildingSpot extends CommandBase{

		public CmdAddBuildingSpot() {
			super("add buildingspot");
		}

		@Override
		public void execute(String[] params) {
			
			Railway tbConnected = null;
			
			if (params.length > 2) {
				
				tbConnected = rails.get(params[2]);
				if (tbConnected == null) tbConnected = buildingSpots.get(params[2]);
				
				if (tbConnected == null) {
					sendMessage("Sikertelen. A megadott sin nem letezik.");
					return;
				}
			}
			
			String key = "sb" + (++buildingSpotsCounter);
			BuildingSpot newRailway = new BuildingSpot(tbConnected);
			buildingSpots.put(key, newRailway);
			sendMessage("Sikerult! Az uj sin azonositoja: " + key);
		}
	}
	
	private static class CmdAddCrossway extends CommandBase{

		public CmdAddCrossway() {
			super("add crossing");
		}

		@Override
		public void execute(String[] params) {
			
			Railway tbConnected = null;
			
			if (params.length > 2) {
				
				tbConnected = rails.get(params[2]);
				if (tbConnected == null) tbConnected = buildingSpots.get(params[2]);
				
				if (tbConnected == null) {
					sendMessage("Sikertelen. A megadott sin nem letezik.");
					return;
				}
			}
			
			String key = "sc" + (++crossesCounter);
			CrossRailway newRailway = new CrossRailway(tbConnected);
			crosses.put(key, newRailway);
			sendMessage("Sikerult! Az uj sin azonositoja: " + key);
		}
	}
	
	private static class CmdAddSwitch extends CommandBase{

		public CmdAddSwitch() {
			super("add switch");
		}

		@Override
		public void execute(String[] params) {
			
			Railway tbConnected = null;
			
			if (params.length > 2) {
				
				tbConnected = rails.get(params[2]);
				if (tbConnected == null) tbConnected = buildingSpots.get(params[2]);
				
				if (tbConnected == null) {
					sendMessage("Sikertelen. A megadott sin nem letezik.");
					return;
				}
			}
			
			String key = "sv" + (++switchesCounter);
			Switch newRailway = new Switch(tbConnected);
			switches.put(key, newRailway);
			sendMessage("Sikerult! Az uj sin azonositoja: " + key);
		}
	}

	private static class CmdAddStation extends CommandBase{

		public CmdAddStation() {
			super("add station");
		}

		@Override
		public void execute(String[] params) {
			Railway onThis=null;
						
			onThis=rails.get(params[2]);
			if(onThis==null)
			{
				sendMessage("Sikertelen. A megadott sin nem letezik.");
				return;
			}
			
			if(Color.fromString(params[3])==null)
			{
				sendMessage("Sikertelen. Hibas szin.");
				return;
			}
			
			Color szin=Color.fromString(params[3]);
			
			String key = "sa" + (++stationsCounter);
			Station newStation = new Station(onThis, szin);
			stations.put(key, newStation);
			sendMessage("Sikerult! Az uj allomas azonositoja: " + key);
			
		}
	}
	
	private static class CmdConnectRail extends CommandBase{

		public CmdConnectRail() {
			super("connect rail");
		}

		@Override
		public void execute(String[] params) {
			
			if (params.length < 4) {
				targetOS.println("Nincs eleg parameter!");
				return;
			}
			
			Railway rail1 = rails.get(params[2]);
			if (rail1 == null) rail1 = buildingSpots.get(params[2]);
			int secRwayParamNumber = 3;
			int firstParam = 0;
			int secondParam = 0;
			
			if (rail1 == null) {
				
				rail1 = crosses.get(params[2]);
				if (rail1 == null) rail1 = switches.get(params[2]);
				if (rail1 == null) {
					targetOS.println("Sikertelen. A megadott parameter nem megfelelo.");
					return;
				}
				
				secRwayParamNumber++;
				firstParam = Integer.parseInt(params[3]);
				if (!(firstParam == 1 || firstParam == 2) || params[3].contains("s")) {
					targetOS.println("Sikertelen. A megadott parameter nem megfelelo.");
					return;
				}
			}
			
			if (params.length < secRwayParamNumber + 1) {
				targetOS.println("Nincs eleg parameter!");
				return;
			}
			
			Railway rail2 = rails.get(params[secRwayParamNumber]);
			if (rail2 == null) rail2 = buildingSpots.get(params[secRwayParamNumber]);
			if (rail2 == null) {
				
				if (params.length < secRwayParamNumber + 2) {
					targetOS.println("Nincs eleg parameter!");
					return;
				}
				
				rail1 = crosses.get(params[secRwayParamNumber]);
				if (rail1 == null) rail1 = switches.get(params[secRwayParamNumber]);
				if (rail1 == null) {
					targetOS.println("Sikertelen. A megadott parameter nem megfelelo.");
					return;
				}
				
				secRwayParamNumber++;
				secondParam = Integer.parseInt(params[3]);
				if (secondParam != 1 || secondParam != 2) {
					targetOS.println("Sikertelen. A megadott parameter nem megfelelo.");
					return;
				}
			}
			
			if (rail1.checkInsertNeighbour(rail2, firstParam) &&
					rail2.checkInsertNeighbour(rail1, secondParam)) {
				rail1.insertNeighbour(rail2, firstParam);
				rail2.insertNeighbour(rail1, secondParam);
				targetOS.println("Sikerult!");
				return;
			}
			targetOS.println("Sikertelen. A megadott parameter nem megfelelo.");
		}
	}
	
	private static class CmdDeleteRail extends CommandBase{

		public CmdDeleteRail() {
			super("delete rail");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	private static class CmdDeleteStation extends CommandBase{

		public CmdDeleteStation() {
			super("delete station");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	


	private static class CmdList extends CommandBase{

		public CmdList() {
			super("list");
		}

		@Override
		public void execute(String[] params) {
			boolean sent=false;
			if(params.length>1){
			if(params[1].equals("sa"))
			{
				String line="";
				String number=null;
				sendMessage("A jelenleg letezo allomasok azonositoi (sa): ");
				for (Entry<String, Station> entry : stations.entrySet())
				{
					number=entry.getKey().replace("sa", "");
					line=line + number + ", ";
				}
				if(line.length()!=0) line=line.substring(0, line.length()-2);
				else line="";
				sendMessage(line);
				sent=true;
			}
			if(params[1].equals("sb"))
			{
				
				String line="";
				String number=null;
				sendMessage("A jelenleg letezo alagutepetesi helyek azonositoi (sb): ");
				for (Entry<String, BuildingSpot> entry : buildingSpots.entrySet())
				{
					number=entry.getKey().replace("sb", "");
					line=line + number + ", ";
				}
				if(line.length()!=0) line=line.substring(0, line.length()-2);
				else line="";
				sendMessage(line);
				sent=true;
			}
			if(params[1].equals("sc"))
			{
				
				String line="";
				String number=null;
				sendMessage("A jelenleg letezo keresztezodo sinek azonositoi (sc): ");
				for (Entry<String, CrossRailway> entry : crosses.entrySet())
				{
					number=entry.getKey().replace("sc", "");
					line=line + number + ", ";
				}
				if(line.length()!=0) line=line.substring(0, line.length()-2);
				else line="";
				sendMessage(line);
				sent=true;
			}
			if(params[1].equals("sv"))
			{
				
				String line="";
				String number=null;
				sendMessage("A jelenleg letezo valtok azonositoi (sv): ");
				for (Entry<String, Switch> entry : switches.entrySet())
				{
					number=entry.getKey().replace("sv", "");
					line=line + number + ", ";
				}
				if(line.length()!=0) line=line.substring(0, line.length()-2);
				else line="";
				sendMessage(line);
				sent=true;
			}
			if(params[1].equals("ss"))
			{
				
				String line="";
				String number=null;
				sendMessage("A jelenleg letezo sinek azonositoi (ss): ");
				for (Entry<String, Railway> entry : rails.entrySet())
				{
					number=entry.getKey().replace("ss", "");
					line=line + number + ", ";
				}
				if(line.length()!=0) line=line.substring(0, line.length()-2);
				else line="";
				sendMessage(line);
				sent=true;
			}}
			else
			{
				String line="";
				String number=null;
				sendMessage("A jelenleg letezo allomasok azonositoi (sa): ");
				for (Entry<String, Station> entry : stations.entrySet())
				{
					number=entry.getKey().replace("sa", "");
					line=line + number + ", ";
				}
				if(line.length()!=0) line=line.substring(0, line.length()-2);
				else line="";
				sendMessage(line);
				
				line="";
				sendMessage("A jelenleg letezo alagutepetesi helyek azonositoi (sb): ");
				for (Entry<String, BuildingSpot> entry : buildingSpots.entrySet())
				{
					number=entry.getKey().replace("sb", "");
					line=line + number + ", ";
				}
				if(line.length()!=0) line=line.substring(0, line.length()-2);
				else line="";
				sendMessage(line);
				
				line="";
				sendMessage("A jelenleg letezo keresztezodo sinek azonositoi (sc): ");
				for (Entry<String, CrossRailway> entry : crosses.entrySet())
				{
					number=entry.getKey().replace("sc", "");
					line=line + number + ", ";
				}
				if(line.length()!=0) line=line.substring(0, line.length()-2);
				else line="";
				sendMessage(line);
				
				line="";
				sendMessage("A jelenleg letezo valtok azonositoi (sv): ");
				for (Entry<String, Switch> entry : switches.entrySet())
				{
					number=entry.getKey().replace("sv", "");
					line=line + number + ", ";
				}
				if(line.length()!=0) line=line.substring(0, line.length()-2);
				else line="";
				sendMessage(line);
				
				line="";
				sendMessage("A jelenleg letezo sinek azonositoi (ss): ");
				for (Entry<String, Railway> entry : rails.entrySet())
				{
					number=entry.getKey().replace("ss", "");
					line=line + number + ", ";
				}
				if(line.length()!=0) line=line.substring(0, line.length()-2);
				else line="";
				sendMessage(line);
				sent=true;
			}
			if(!sent) sendMessage("Nem letezo tipust nem lehet listazni!");
		}
	}
	
	private static class CmdExploreLine extends CommandBase{

		public CmdExploreLine() {
			super("explore line");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	private static class CmdExploreRail extends CommandBase{

		public CmdExploreRail() {
			super("explore rail");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	
	private static class CmdExploreStation extends CommandBase{

		public CmdExploreStation() {
			super("explore station");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	private static class CmdExploreAllrail extends CommandBase{

		public CmdExploreAllrail() {
			super("explore allrail");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	private static class CmdToggleSwitch extends CommandBase{

		public CmdToggleSwitch() {
			super("toggle switch");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	private static class CmdToggleStation extends CommandBase{

		public CmdToggleStation() {
			super("toggle station");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	




private static class CmdBuildTunnel extends CommandBase{

		public CmdBuildTunnel() {
			super("build tunnel");
		}

		@Override
		public void execute(String[] params) {
			BuildingSpot first = null;
			BuildingSpot second = null;	
			if (params.length == 4) {
			
				
				if (!(params[2].startsWith("sb")))
				{
					sendMessage("Sikertelen. A megadott sin nem letezik.");
					return;
				}
				else
				{
					
					first= buildingSpots.get(params[2]);
					if(first==null)
					{
						sendMessage("Sikertelen. A megadott sin nem letezik.");
						return;
					}
				}
				
				if (!(params[3].startsWith("sb")))
				{
					sendMessage("Sikertelen. A megadott sin nem letezik.");
					return;
				}
				else
				{
					
					second= buildingSpots.get(params[3]);
					if(second==null)
					{
						sendMessage("Sikertelen. A megadott sin nem letezik.");
						return;
					}
				}
				
				if (tunnel!=null) {
					sendMessage("Sikertelen. Mar van megepitve alagut.");
					return;
				}
			}
			
			tunnel.build(first, second);
			sendMessage("Sikerult! Az alaguton mehetnek at a vonatok.");
		}
	}
	


private static class CmdDestroyTunnel extends CommandBase{	
	
		public CmdDestroyTunnel() {
			super("destroy tunnel");
		}

		@Override
		public void execute(String[] params) {
			if(tunnel==null)
				{
					sendMessage("Sikertelen. Meg nincs megepitve alagut.");
					return;
				}
			tunnel.destroy();
			sendMessage("Sikerult! Az alaguton mostmar zarva.");
			
			tunnel = null;
		}
	}

	
	private static class CmdPrepareTrain extends CommandBase{

		public CmdPrepareTrain() {
			super("prepare train");
		}

		@Override
		public void execute(String[] params) {
			lastCart = null;
			isFinished = false;
			targetOS.println("Uj vonat elkezdve.");
		}
	}
	
	private static class CmdAddCart extends CommandBase{

		public CmdAddCart() {
			super("add cart");
		}

		@Override
		public void execute(String[] params) {
			
			if (isFinished) {
				targetOS.println("A legutobbi mozdony elkeszitese ota nem volt prepare train parancs!");
				return;
			}
			
			if (params.length < 5) {
				targetOS.println("Nince eleg parameter!");
				return;
			}
			
			Cart newCart = null;
			
			if (params[2].equals("coal")) {
				
				Railway thisRail = findRail(params[3]);
				Railway lastRail = findRail(params[4]);
				if (thisRail == null) {
					targetOS.println("Sikertelen. Nem letezik a megadott sin.");
					return;
				}
				if (lastRail != null  && !thisRail.getNeighbours().contains(lastRail)) {
					targetOS.println("Sikertelen. A ket megadott sin nem szomszedos.");
					return;
				}
				
				
				if (lastCart != null) {
					
					if (carts.get(lastCart).PreviousRailwaySegment == thisRail) {
						targetOS.println("Sikertelen. A sin parameter hibasan lett megadva.");
						return;
					}
					
					newCart = new CoalCart(thisRail, lastRail, carts.get(lastCart));
				} else {
					newCart = new CoalCart(thisRail, lastRail, null);
				}
			} else {
				
				if (params.length < 6) {
					targetOS.println("Nince eleg parameter!");
					return;
				}
				
				Railway thisRail = findRail(params[2]);
				Railway lastRail = findRail(params[3]);
				if (thisRail == null) {
					targetOS.println("Sikertelen. Nem letezik a megadott sin.");
					return;
				}
				if (lastRail != null  && !thisRail.getNeighbours().contains(lastRail)) {
					targetOS.println("Sikertelen. A ket megadott sin nem szomszedos.");
					return;
				}
				
				Color color = Color.fromString(params[4]);
				if (color == null) {
					targetOS.println("Sikertelen. A szin parameter hibasan lett megadva.");
					return;
				}
				if (!params[5].equalsIgnoreCase("true") && !params[5].equalsIgnoreCase("false")) {
					targetOS.println("Sikertelen. A utasok parameter hibasan lett megadva.");
					return;
				}
				boolean passengers = Boolean.parseBoolean(params[5]);
				
				if (lastCart != null) {
					
					if (carts.get(lastCart).PreviousRailwaySegment == thisRail) {
						targetOS.println("Sikertelen. A sin parameter hibasan lett megadva.");
						return;
					}
					newCart = new Cart(thisRail, lastRail, carts.get(lastCart), color, passengers);
				} else 
					newCart = new Cart(thisRail, lastRail, null, color, passengers);
				
			}
			String key = "mc" + (++cartsCounter);
			carts.put(key, newCart);
			lastCart = key;
			targetOS.println("Sikerult! Az uj kocsi azonositoja: " + key);
		}
		
		private Railway findRail(String which) {
			Railway result = rails.get(which);
			if (result == null) result = crosses.get(which);
			if (result == null) result = switches.get(which);
			if (result == null) result = buildingSpots.get(which);
			return result;
		}
	}
	
	private static class CmdAddLoco extends CommandBase{

		public CmdAddLoco() {
			super("add loco");
		}

		@Override
		public void execute(String[] params) {
			
			if (isFinished) {
				targetOS.println("A legutobbi mozdony elkeszitese ota nem volt prepare train parancs!");
				return;
			}
			
			if (params.length < 5) {
				targetOS.println("Nince eleg parameter!");
				return;
			}
			
			Railway thisRail = findRail(params[2]);
			Railway lastRail = findRail(params[3]);
			if (thisRail == null) {
				targetOS.println("Sikertelen. Nem letezik a megadott sin.");
				return;
			}
			if (lastRail != null && !thisRail.getNeighbours().contains(lastRail)) {
				targetOS.println("Sikertelen. A ket megadott sin nem szomszedos.");
				return;
			}
			
			int speed = -1;
			try {
				speed = Integer.parseInt(params[4]);
			} catch (NumberFormatException e) {
				targetOS.println("Sikertelen. A sebesseg hibasan lett megadva.");
				return;
			}
			
			String key = "ml" + (++locosCounter);
			locos.put(key, new Locomotive(thisRail, lastRail, carts.get(lastCart), speed));
			isFinished = true;
			
			targetOS.println("Sikerult! Az uj mozdony azonositoja: " + key);
		}
		
		private Railway findRail(String which) {
			Railway result = rails.get(which);
			if (result == null) result = crosses.get(which);
			if (result == null) result = switches.get(which);
			if (result == null) result = buildingSpots.get(which);
			return result;
		}
	}
	
	private static class CmdStep extends CommandBase{

		public CmdStep() {
			super("step");
		}

		@Override
		public void execute(String[] params) {
			
			if (params.length < 2) {
				targetOS.println("Nincs eleg parameter!");
				return;
			}
			Locomotive target = locos.get(params[1]);
			if (target == null) {
				targetOS.println("Sikertelen. Nem letezik adott azonositoju mozdony.");
				return;
			}
			Set<Entry<String, Railway> > railSet = rails.entrySet();
			String railkey1 = null, railkey2 = null;
			for (Map.Entry<String, Railway> entry: railSet) {
				if (entry.getValue() == target.CurrentRailwaySegment) railkey1 = entry.getKey();
				if (entry.getValue() == target.CurrentRailwaySegment.next(target.PreviousRailwaySegment)) railkey2 = entry.getKey();
			}
			
			targetOS.println("Ertettem: "+ params[1] +" vonat mozgatasa "+ railkey1 +" sinrol "+ railkey2 +" sinre.");
			target.move();
		}
	}
	
	private static class CmdDeleteLoco extends CommandBase{

		public CmdDeleteLoco() {
			super("delete loco");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	


	private static class CmdExploreLoco extends CommandBase{

		public CmdExploreLoco() {
			super("explore loco");
		}

		@Override
		public void execute(String[] params) {
			Locomotive actual=null;
			actual=locos.get(params[2]);
			
			if(!(params[2].equals("all")) && actual==null)
			{
				sendMessage("Sikertelen. Nem letezik adott azonositoju mozdony.");
				return;
			}
			if(params.length>3)
			{
				//allcart happening pls
				if(params[2].equals("all"))
				{
					String line=null;
					String number=null;
					sendMessage("A jelenleg letezo mozdonyok: ");
					for(String keys:locos.keySet())
					{
						String railkey = null;
						for(Entry<String, Railway> entry: rails.entrySet())
							if (locos.get(keys).CurrentRailwaySegment == entry.getValue()) {
								railkey = entry.getKey();
								break;
							}
						
						line=keys+": Epp az "+railkey+" sinen allok, es "+locos.get(keys).Speed+"-el megyek.";
						sendMessage(line);
						if(params[3].equals("allcart"))
						{
							sendMessage("Engem kovetnek a kovetkezo kocsik:");
							Cart nextCart = locos.get(keys).Pulls;
							
							while (nextCart != null) {
								Set<Entry<String, Cart> > cartsSet = carts.entrySet();
								String key = null;
								for (Map.Entry<String, Cart> entry: cartsSet)
									if (entry.getValue() == nextCart) {
										key = entry.getKey();
										break;
									}
								String eCartParams[] = { "explore", "cart", key};
								new CmdExploreCart().execute(eCartParams);
								nextCart = nextCart.Pulls;
							}
						}
					}
				}
				else
				{
					String line=null;
					line="Ez itt mozdony "+params[2]+".";
					sendMessage(line);
					String railkey = null;
					for(Entry<String, Railway> entry: rails.entrySet())
						if (actual.CurrentRailwaySegment == entry.getValue()) {
							railkey = entry.getKey();
							break;
						}
					
					line="Epp az "+ railkey +" sinen allok, es "+ actual.Speed +"-el megyek.";
					sendMessage(line);
					if(params[3].equals("allcart"))
					{
						sendMessage("Engem kovetnek a kovetkezo kocsik:");
						Cart nextCart = locos.get(params[2]).Pulls;
						
						while (nextCart != null) {
							Set<Entry<String, Cart> > cartsSet = carts.entrySet();
							String key = null;
							for (Map.Entry<String, Cart> entry: cartsSet)
								if (entry.getValue() == nextCart) {
									key = entry.getKey();
									break;
								}
							String eCartParams[] = { "explore", "cart", key};
							new CmdExploreCart().execute(eCartParams);
							nextCart = nextCart.Pulls;
						}
					}
					
				}
			}
			
			else
			{
				//nincs allcart
				if(params[2].equals("all"))
				{
					//all...
					String line=null;
					String number=null;
					sendMessage("A jelenleg letezo mozdonyok: ");
					for(String keys:locos.keySet())
					{
						line=keys+": Epp az "+locos.get(keys).CurrentRailwaySegment+" sinen allok, es "+locos.get(keys).Speed+"-el megyek.";
						sendMessage(line);
						line=null;
					}
					
				}
				else
				{
					String line=null;
					line="Ez itt mozdony "+params[2]+".";
					sendMessage(line);
					line="Epp az "+ actual.CurrentRailwaySegment +" sinen allok, es "+ actual.Speed +"-el megyek.";
					sendMessage(line);
				}
			}
		}
	}
	


	private static class CmdExploreCart extends CommandBase{

		public CmdExploreCart() {
			super("explore cart");
		}
		@Override
		public void execute(String[] params) {
			Cart actual=null;
			actual=carts.get(params[2]);
			if(actual==null)
			{
				sendMessage("Sikertelen. Nem letezik adott azonositoju vagon.");
				return;
			}
			sendMessage("Ez itt az "+params[2]+" vagon.");
			
			String railkey = null;
			for(Entry<String, Railway> entry: rails.entrySet())
				if (actual.CurrentRailwaySegment == entry.getValue()) {
					railkey = entry.getKey();
					break;
				}
			
			
			if(actual instanceof CoalCart)
			{
				sendMessage("Epp az " + railkey + " sinen allok.");
				sendMessage("En egy szenszallito vagon vagyok.");
			}
			else
			{
				
				
				
				sendMessage("Epp az " + railkey + " sinen allok.");
				sendMessage("En egy utasszallito vagon vagyok.");
				String state=null;
				if(actual.getPassengers()) state="tele";
				else state="ures";
				sendMessage("A szinem "+ actual.getColor().toString().toLowerCase() +", es jelenleg epp "+state+" vagyok.");
			}
			
		}
	}
	
	private static class CmdTimerStart extends CommandBase{

		public CmdTimerStart() {
			super("timer start");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	private static class CmdTimerEnd extends CommandBase{

		public CmdTimerEnd() {
			super("timer end");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	private static class CmdExploreSwitch extends CommandBase{

		public CmdExploreSwitch() {
			super("explore switch");
		}

		@Override
		public void execute(String[] params) {
			
			targetOS.println("Ez a valto az ssn iranyaba mutat.");
		}
	}
	
	private static class CmdClearTable extends CommandBase{

		public CmdClearTable() {
			super("clearTable");
		}

		@Override
		public void execute(String[] params) {
			rails = new HashMap<String, Railway>();
			railsCounter = 0;
			
			switches = new HashMap<String, Switch>();
			switchesCounter = 0;
			
			crosses = new HashMap<String, CrossRailway>();
			crossesCounter = 0;
			
			buildingSpots = new HashMap<String, BuildingSpot>();
			buildingSpotsCounter = 0;
			
			stations = new HashMap<String, Station>();
			stationsCounter = 0;
			
			simultanStations = new HashMap<String, SimultanStation>();
			
			carts = new HashMap<String, Cart>();
			cartsCounter = 0;
			
			locos = new HashMap<String, Locomotive>();
			locosCounter = 0;
			
			tunnel = null;
			
			lastCart = null;
			isFinished = true;
			
			targetOS.println("A takaritas megtortent.");
		}
	}
	
	


}
