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
	private static int simultanStationsCounter = 0;
	
	private static Map<String, Cart> carts = new HashMap<String, Cart>();
	private static int cartsCounter = 0;
	
	private static Map<String, Locomotive> locos = new HashMap<String, Locomotive>();
	private static int locosCounter = 0;
	
	private static Tunnel tunnel = null;
	
	public static void main(String[] args) {
		
		targetOS = stdoutWriter = System.out;
		targetIS = stdinReader = new BufferedReader(new InputStreamReader(System.in));
		

		
		List<CommandBase> commands = new ArrayList<CommandBase> ();
		commands.add(new CmdSwitchInput());
		commands.add(new CmdSwitchOutput());
		commands.add(new CmdAddRail());
		
		//commands.add(new CmdBuildTunnel());
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
				targetOS.println("Nincs eleg parameter!");
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
					targetOS.println("A megadott fajl nem talalhato");
					targetOS.println(dir.getAbsolutePath());
					
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
				targetOS.println("Nincs eleg parameter!");
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
					targetOS.println("Hiba tortent a fajl irasra valo megnyitasakor:");
					targetOS.println(dir.getAbsolutePath());
					targetOS.println("A parancs hatastalan!");
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
			
			if (params.length > 3) {
				
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
			
			if (params.length > 3) {
				
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
			
			if (params.length > 3) {
				
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
			
			if (params.length > 3) {
				
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
				if (firstParam != 1 || firstParam != 2 || params[3].contains("s")) {
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
			
			if(params[1].equals("a"))
			{
				String line=null;
				String number=null;
				sendMessage("A jelenleg letezo allomasok azonositoi (sa): ");
				for(String keys:stations.keySet())
				{
					number=keys.replace("sa", "");
					line=line + number + ", ";
				}
				line=line.substring(0, line.length()-2);
				sendMessage(line);
			}
			if(params[1].equals("b"))
			{
				
				String line=null;
				String number=null;
				sendMessage("A jelenleg letezo alagutepetesi helyek azonositoi (sb): ");
				for(String keys:buildingSpots.keySet())
				{
					number=keys.replace("sb", "");
					line=line + number + ", ";
				}
				line=line.substring(0, line.length()-2);
				sendMessage(line);
			}
			if(params[1].equals("c"))
			{
				
				String line=null;
				String number=null;
				sendMessage("A jelenleg letezo keresztezodo sinek azonositoi (sc): ");
				for(String keys:crosses.keySet())
				{
					number=keys.replace("sc", "");
					line=line + number + ", ";
				}
				line=line.substring(0, line.length()-2);
				sendMessage(line);
			}
			if(params[1].equals("v"))
			{
				
				String line=null;
				String number=null;
				sendMessage("A jelenleg letezo valtok azonositoi (sv): ");
				for(String keys:stations.keySet())
				{
					number=keys.replace("sv", "");
					line=line + number + ", ";
				}
				line=line.substring(0, line.length()-2);
				sendMessage(line);
			}
			if(params[1].equals("s"))
			{
				
				String line=null;
				String number=null;
				sendMessage("A jelenleg letezo sinek azonositoi (ss): ");
				for(String keys:stations.keySet())
				{
					number=keys.replace("ss", "");
					line=line + number + ", ";
				}
				line=line.substring(0, line.length()-2);
				sendMessage(line);
			}
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
	
	private static class CmdExplore extends CommandBase{

		public CmdExplore() {
			super("explore");
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
			super("prepatre train");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	private static class CmdAddCart extends CommandBase{

		public CmdAddCart() {
			super("add cart");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	private static class CmdAddLoco extends CommandBase{

		public CmdAddLoco() {
			super("add loco");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	private static class CmdStep extends CommandBase{

		public CmdStep() {
			super("step");
		}

		@Override
		public void execute(String[] params) {
			
			
			
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
			
		}
	}
	
	private static class CmdExploreCart extends CommandBase{

		public CmdExploreCart() {
			super("explore cart");
		}

		@Override
		public void execute(String[] params) {
			
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
			
		}
	}
	
	private static class CmdClearTable extends CommandBase{

		public CmdClearTable() {
			super("clear table");
		}

		@Override
		public void execute(String[] params) {
			
		}
	}
	
	


}
