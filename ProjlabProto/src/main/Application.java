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
	
	public static void sendMessage(String msg) {
		targetOS.println(msg);
	}
}
