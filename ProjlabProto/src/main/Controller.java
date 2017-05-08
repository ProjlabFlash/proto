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
import java.util.Timer;
import java.util.TimerTask;

import graphics.GameFrame;


public class Controller {

	/**
	 * Csomagolo a konzolos ki-/ es bemenet szamara.
	 */
	private static PrintStream stdoutWriter;
	private static BufferedReader stdinReader;
	
	/**
	 * Az eppen akutalis kimeneti, illetve bementi stream. Minden iras vagy olvasas muvelet ezekre vonatkozik.
	 */
	private static PrintStream targetOS;
	private static BufferedReader targetIS;
	
	/**
	 * Az eppen megnyitott fajl(ok).
	 */
	private static FileInputStream fileReader = null;
	private static FileOutputStream fileWriter = null;
	
	/**
	 * A palya elemei, illetve egy hozzajuk tartozo szamlalo az egyedi kulcsok kiosztasahoz
	 */
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
	
	/**
	 * Az egyedi kulcsat megosztja a stations valtozoval.
	 */
	private static Map<String, SimultanStation> simultanStations = new HashMap<String, SimultanStation>();
	
	private static Map<String, Cart> carts = new HashMap<String, Cart>();
	private static int cartsCounter = 0;
	
	private static Map<String, Locomotive> locos = new HashMap<String, Locomotive>();
	private static int locosCounter = 0;
	
	/**
	 * Referencia magara az alagutra. Ebbol mindig csak egy lehet, es ezen keresztul lehet megszuntetni.
	 */
	private static Tunnel tunnel = null;
	
	/**
	 * Ha a mozdonyokat automatikusan szeretnenk leptetni, akkor az ehhez hasznalt idozitoket itt tarolhatjuk.
	 * Mivel kulcs-ertek parokat tartalmaz ezert csak egyszerre egy idozito lehet szamon tartva vonatonkent, tehat
	 * a megfelelo utasitasok mindig ellenorzik, hogy fut-e mar egy idozito, mielott a regi referenciajat elveszitve felulirna azt.
	 */
	private static Map<Locomotive, Timer> timers = new HashMap<Locomotive, Timer>();
	
	/**
	 * A vonat keszitesehez szukseges allapotvaltozok. Ezaltal a vonat elemeit fel lehet sorolni egymas utan,
	 * anelkul hogy ugyelnunk kene arra, hogy jo sorrendben lettek-e osszekotve.
	 */
	private static String lastCart = null; 
	private static Boolean isFinished = true;
	
	/**
	 * Ha false, akkor az aktualis parancs feldolgozasa utan kilep a programbol.
	 */
	private static boolean isRunning = true;
	
	/**
	 * A parancsokat magaba foglalo lista.
	 */
	private static List<CommandBase> commands = new ArrayList<CommandBase> ();
	static {
		commands.add(new CmdSwitchInput());
		commands.add(new CmdSwitchOutput());
		commands.add(new CmdAddRail());
		commands.add(new CmdAddBuildingSpot());
		commands.add(new CmdAddCrossway());
		commands.add(new CmdAddSwitch());
		commands.add(new CmdAddStation());
		commands.add(new CmdConnectRail());
		commands.add(new CmdDeleteRail());
		commands.add(new CmdDeleteStation());
		commands.add(new CmdList());
		commands.add(new CmdExploreLine());
		commands.add(new CmdExploreRail());
		commands.add(new CmdExploreStation());
		commands.add(new CmdExploreAllrail());
		commands.add(new CmdToggleSwitch());
		commands.add(new CmdToggleStation());
		commands.add(new CmdBuildTunnel());
		commands.add(new CmdDestroyTunnel());
		commands.add(new CmdPrepareTrain());
		commands.add(new CmdAddCart());
		commands.add(new CmdAddLoco());
		commands.add(new CmdStep());
		commands.add(new CmdDeleteLoco());
		commands.add(new CmdExploreLoco());
		commands.add(new CmdExploreCart());
		commands.add(new CmdTimerStart());
		commands.add(new CmdTimerEnd());
		commands.add(new CmdExploreSwitch());
		commands.add(new CmdClearTable());
		commands.add(new CmdExit());
	}
	
	private static Object syncObj = new Object();
	
	/**
	 * A program fo hurka. Az aktualis bemenetrol olvas, es soronkent osszeveti a parancsok listajaval.
	 * Amelyiknel egyezest tlal, azt meghivja. Egy sorra, csak egy parancs hivodik.
	 * 
	 * @param args: Nem hasznalt
	 */
	
	

	public static void executeFromInput(String[] args) {
		
		targetOS = stdoutWriter = System.out;
		targetIS = stdinReader = new BufferedReader(new InputStreamReader(System.in));
		
		/**
		 * A hurok megnezi, hogy letezik-e az elso szoban megadott parancs, ha nem akkor osszefuzi azt a masodik szoval
		 * es ujra megnezi, mert akadnak ket szavas parameterek. Ezek miatt ket parameter elso szava nem egyezhet meg, ha az egyik egy szavas.
		 * Ha megtalaltuk a parancsot amire a felhasznalo gondolt, akkor a teljes sort atadjuk neki, es ezzel kesz is vagyunk
		 */
		mainloop:
		while(isRunning) {
			try {
				String line = targetIS.readLine();
				if (line == null)
					if (fileReader != null && fileReader.available() == 0) { //Ha a fajl olvasasa anelkul ert veget, hogy atirnyitottak a bemenetet, akkor ez megfogja, es visszaadja a felhasznalonak a bemenetet.
						targetIS = stdinReader;
						continue mainloop;
				}
				String[] params = line.split(" ");
				
				if (params.length == 0)
					continue mainloop;
				
				for (int i = 0; i < commands.size(); i++)
					if (commands.get(i).cmdName.equals(params[0])) {
						synchronized (syncObj) {
							commands.get(i).execute(params);
						}
						continue mainloop;
					}
				
				if (params.length == 1) {
					System.out.println("A parancs nem felismerheto!");
					continue mainloop;
				}
				
				String newcmd = params[0] + " " + params[1];
				
				for (int i = 0; i < commands.size(); i++)
					if (commands.get(i).cmdName.equals(newcmd)) {
						synchronized (syncObj) {
							commands.get(i).execute(params);
						}
						continue mainloop;
					}
				
				System.out.println("A parancs nem felismerheto!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Egyetlen parancs végrehajtására szolgáló folyamat
	 * @param cmd
	 */
	public void execute(String cmd) {
		String[] params = cmd.split(" ");
		
		for (int i = 0; i < commands.size(); i++)
			if (commands.get(i).cmdName.equals(params[0])) {
				commands.get(i).execute(params);
			}
		
		if (params.length == 1)
			System.out.println("A parancs nem felismerheto!");
		
		String newcmd = params[0] + " " + params[1];
		
		for (int i = 0; i < commands.size(); i++)
			if (commands.get(i).cmdName.equals(newcmd)) {
				commands.get(i).execute(params);
			}
		
		System.out.println("A parancs nem felismerheto!");
	}

	/**
	 * Az osztalyon kivuli objektumok ezen fuggveny segitsegevel kommunikalhatnak a felhasznaloval.
	 * @param msg A kuldeni kivant uzenet.
	 */
	public static void sendMessage(String msg) {
		targetOS.println(msg);
	}
	
	/**
	 * Az osztalyon kivuli objektumok ezen fuggveny segitsegevel kommunikalhatnak a felhasznaloval.
	 * Az uzenetet kiirasa elott atvizsgalja, es az "san" elofordulast benne felulirja a parametrul
	 * kapott allomashoz tartozo kulccsal.
	 * 
	 * @param msg A kuldeni kivant uzenet.
	 * @param station Az allomas aminek a kulcsat akarjuk kozolni a felhasznaloval.
	 */
	public static void sendMessage(String msg, Station station) {
		Set<Entry<String, Station> > stationSet = stations.entrySet();
		Set<Entry<String, SimultanStation>> simultanStationSet = simultanStations.entrySet();
		String key = null;
		for (Map.Entry<String, Station> entry: stationSet)
			if (entry.getValue() == station) key = entry.getKey();
		if (key == null)
			for (Map.Entry<String, SimultanStation> entry: simultanStationSet)
				if (entry.getValue() == station) key = entry.getKey();
		
		msg = msg.replace("san", key);
		targetOS.println(msg);
	}
	
	/**
	 * Az osztalyon kivuli objektumok ezen fuggveny segitsegevel kommunikalhatnak a felhasznaloval.
	 * Az uzenetet kiirasa elott atvizsgalja, es az "mcn" elofordulast benne felulirja a parametrul
	 * kapott kocsihoz tartozo kulccsal.
	 * 
	 * @param msg A kuldeni kivant uzenet.
	 * @param station A kocsi aminek a kulcsat akarjuk kozolni a felhasznaloval.
	 */
	public static void sendMessage(String msg, Cart cart) {
		
		Set<Entry<String, Cart> > cartSet = carts.entrySet();
		String key = null;
		for (Map.Entry<String, Cart> entry: cartSet)
			if (entry.getValue() == cart) key = entry.getKey();
		
		msg = msg.replace("mcn", key);
		targetOS.println(msg);
	}
	
	/**
	 * Ha adott egy sin, es abbol meg akarjuk hatarozni a hozza tartozo kulcsot(altalaba a felhasznaloval valo kozles celjabol),
	 * akkor ez a fuggveny használhato. Atvizsgalja az osszes sínelemet tartalmazo Map-et, es visszaadja az elso elofordulat.
	 * (Megjegyzendo, hogy elmeletileg nem szabadna tobbnek lenni).
	 * 
	 * @param railway A sin referenciaja, aminek a kulcsat tudni akarjuk
	 * @return A parameterul kapott sinhez tartozo kulcs.
	 */
	public static String getStringForRail(Railway railway) {
		String result = null;
		
		for (Entry<String, Railway> entry: rails.entrySet())
			if (entry.getValue() == railway) {
				result = entry.getKey();
				break;
			}
		
		if (result == null)
			for (Entry<String, BuildingSpot> entry: buildingSpots.entrySet())
				if (entry.getValue() == railway) {
					result = entry.getKey();
					break;
				}
		
		if (result == null)
			for (Entry<String, Switch> entry: switches.entrySet())
				if (entry.getValue() == railway) {
					result = entry.getKey();
					break;
				}
		
		if (result == null)
			for (Entry<String, CrossRailway> entry: crosses.entrySet())
				if (entry.getValue() == railway) {
					result = entry.getKey();
					break;
				}
		
		return result;
	}
	
	/**
	 * Egy osztaly ami arra szolgal, hogy alapjaul szolgaljon az osszes parancsnak.
	 * A benne tarolt string alapjan lehet osszehasonlitani a felhasznaloi bemenettel.
	 * Az execute utasitas az, amit a fohurok elindit, ha a felhasznalo ezt az utasitast jelolte ki
	 * Ennek parametere a teljes sor, szokozok mentem darabokra szedve.
	 */
	public static abstract class CommandBase {
		
		public final String cmdName;
		
		public CommandBase (String cmdName) {
			this.cmdName = cmdName;
		}
		
		public abstract void execute(String[] params);
		
	}
	
	/**
	 * A bemenet atiranyitasara szolgalo fajl. A parancs meghivasa utan ha "stdin"-t kap akkor a standard bemenetrol
	 * olvas ezutan minden utasitas, ha pedig egy fajlnevet, akkor a <batch fajl elerese>/testFiles mappahoz relativan kell megadni.
	 * Ezutan a fajlban a sorok olvasa azonnal elkezdodik.
	 */
	private static class CmdSwitchInput extends CommandBase {

		public CmdSwitchInput() {
			super("redirect input");
		}

		/**
		 * @param params[2] A fajl neve/elerese a /testFiles mappahoz viszonitva, vagy "stdin"
		 * @param params[3] opcionalis: Ha "silent", akkor a parancs nem ir a kimenetre
		 */
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
	
	/**
	 * A kimenet atiranyitasara szolgalo osztaly. A parancs meghivasa utan ha "stdout"-t kap akkor a standard kimenetre ir
	 * ezutan minden utasitas, ha pedig egy fajlnevet, akkor a <batch fajl elerese>/dataFiles mappahoz relativan kell megadni.
	 * Ezutan az osszes kimenet, beleertve ezt a parancsot is, ha sikeresen meg lehetett nyitni a fajlt, ebbe ir.
	 */
	private static class CmdSwitchOutput extends CommandBase {

		public CmdSwitchOutput() {
			super("redirect output");
		}

		/**
		 * @param params[2] A fajl neve/elerese a /dataFiles mappahoz viszonitva, vagy "stdout"
		 * @param params[3] opcionalis: Ha "silent", akkor a parancs nem ir a kimenetre
		 */
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
					fileWriter = new FileOutputStream(dir, true);
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
	
	/**
	 * Felvesz egy uj sint a modellbe
	 */
	private static class CmdAddRail extends CommandBase{

		  public CmdAddRail() {
		   super("add rail");
		  }

		  /**
		   * @param params[2] opcionalis, ssn, vagy sbn alaku, es az altala kijelolt palyaelemmel azonnal osszekoti, ha megvan adva. 
		   */
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
		   
		   if (tbConnected != null && tbConnected.getNeighbours().size() == 2) {
			   sendMessage("Sikertelen. A megadott sinhez nem lehet tobbet kotni.");
			   return;
		   }
		   String key = "ss" + (++railsCounter);
		   Railway newRailway = new Railway(tbConnected);
		   rails.put(key, newRailway);
		   sendMessage("Sikerult! Az uj sin azonositoja: " + key);
		  }
		 }
	
	/**
	 * Felvesz egy uj alagutepitesi helyet a modellbe.
	 */
	private static class CmdAddBuildingSpot extends CommandBase{

		public CmdAddBuildingSpot() {
			super("add buildingspot");
		}

		/**
		 * @param params[2] opcionalis, ssn, vagy sbn alaku, es az altala kijelolt palyaelemmel azonnal osszekoti, ha megvan adva.
		 */
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
	
	/**
	 * Felvesz egy uj keresztezodest helyet a modellbe.
	 */
	private static class CmdAddCrossway extends CommandBase{

		public CmdAddCrossway() {
			super("add crossing");
		}

		/**
		 * @param params[2] opcionalis, ssn, vagy sbn alaku, es az altala kijelolt palyaelemmel azonnal osszekoti, ha megvan adva.
		 */
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
	
	/**
	 * Felvesz egy uj valtot a modellbe.
	 */
	private static class CmdAddSwitch extends CommandBase{

		public CmdAddSwitch() {
			super("add switch");
		}

		/**
		 * @param params[2] opcionalis, ssn, vagy sbn alaku, es az altala kijelolt palyaelemmel azonnal osszekoti, ha megvan adva.
		 */
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

	/**
	 * Felvesz egy uj allomast a modellbe. Ezen csak leszallni tudnak az utasok.
	 */
	private static class CmdAddStation extends CommandBase{

		public CmdAddStation() {
			super("add station");
		}

		/**
		 * @param params[2] A sin kulcsa, ami melle tennie kell az allomast.
		 * @param params[3] Az allomas szine.
		 */
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
	
	/**
	 * Osszekot 2 mar letezo sin elemet.
	 */
	private static class CmdConnectRail extends CommandBase{

		public CmdConnectRail() {
			super("connect rail");
		}

		/**
		 * @param params A 2es indexu jeloli az elso sint. Viszont ha ez keresztezodes, vagy valtot jelol ki, akkor
		 * 	a params[3] egy szam kell legyen, ami tovabbi informaciot ad meg rola(a keresztezodesnek, melyik sinparra tegye,
		 * 	es a valtonal meg azt, hogy melyik oldalra). A kovetkezo parameter megadja a kovetkezo sint, ami ugyanilyen modon varhat
		 * 	meg egy parametert.
		 */
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
				
				rail2 = crosses.get(params[secRwayParamNumber]);
				if (rail2 == null) rail2 = switches.get(params[secRwayParamNumber]);
				if (rail2 == null) {
					targetOS.println("Sikertelen. A megadott parameter nem megfelelo.");
					return;
				}
				
				secRwayParamNumber++;
				secondParam = Integer.parseInt(params[secRwayParamNumber]);
				
				if (secondParam != 1 && secondParam != 2) {
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
	
	/**
	 * Kitorol a modellbol egy sint, HA nincs rajta alagut epitve, vagy nem halad rajta vonat.
	 */
	private static class CmdDeleteRail extends CommandBase{

		public CmdDeleteRail() {
			super("delete rail");
		}

		/**
		 * @param params[2] A torolni kivant sin azonositoja.
		 */
		@Override
		public void execute(String[] params) {
			if (params.length > 2) {
				String key = params[2];
				
				Railway rail = rails.remove(key);
				if (rail == null) rail = switches.remove(key);
				if (rail == null) rail = crosses.remove(key);
				if (rail == null) rail = buildingSpots.remove(key);
				
				if (rail == null) {
					targetOS.println("Sikertelen. A megadott sin nem letezik.");
					return;
				}
					
				if (tunnel != null)
					if (tunnel.isTunnel(rail)) {
						targetOS.println("A sin nem torolheto, mert rajta alagut van epitve!");
					}
				
				if (rail.OnMe != null) {
					targetOS.println("Sikertelen. A megadott sinen epp egy vonat tartozkodik!");
					return;
				}
				
				List<Railway> railList = rail.getNeighbours();
				for (Railway r: railList) {
					r.deleteNeighbour(rail);
				}
				
				Station toBeDeleted = rail.station;
				if (toBeDeleted != null)
					for (Entry<String, Station> entry: stations.entrySet())
						if (entry.getValue() == toBeDeleted) {
							stations.remove(entry.getKey());
							toBeDeleted = null;
							break;
						}
				
				if (toBeDeleted != null)
					for (Entry<String, SimultanStation> entry: simultanStations.entrySet())
						if (entry.getValue() == toBeDeleted) {
							simultanStations.remove(entry.getKey());
							toBeDeleted = null;
							break;
						}
				
				targetOS.println("Sikerult!");
			} else {
				targetOS.println("Nincs eleg parameter!");
			}
		}
	}
	
	/**
	 * Kitorol a modellbol egy allomast.
	 */
	private static class CmdDeleteStation extends CommandBase{

		public CmdDeleteStation() {
			super("delete station");
		}

		/**
		 * @param params[2] A torolni kivant allomas azonositoja.
		 */
		@Override
		public void execute(String[] params) {
			if (params.length > 2) {
				
				Station station = stations.get(params[2]);
				if (station == null) simultanStations.get(params[2]);
				else stations.remove(params[2]);
				if (station == null) {
					targetOS.println("Sikertelen. A megadott allomas nem letezik.");
					return;
				} else simultanStations.remove(params[2]);
				
				Railway rail = station.railway;
				rail.setStation(null);
				
				
				targetOS.println("Sikerult!");
			} else {
				targetOS.println("Nincs eleg parameter!");
			}
		}
	}
	
	/**
	 * Kilistazza az osszes azonosito szamot, ami az adott tipushoz tartozik.
	 * Ebbol ugy lesz azonosito, hogy az elotte zarojelben irt betusorozat melle illesztjuk a szamot.
	 */
	private static class CmdList extends CommandBase{

		public CmdList() {
			super("list");
		}

		/**
		 * A tipust jeloli ki. Vagy minden tipust.
		 */
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
	
	/**
	 * Egy vizualis megjelenitest ad a felhasznalonak egy palyaszakaszrol
	 * A palyaszakasz egy vakvagany/valtotol megy egy masikig, vagy onmagaba visszater("-" jeloli a vegen)
	 * ha egy kort alkot a sin.
	 * A kimenet ekeppen nez ki: ss1-ss2-ss3-sc1-ss7-ss8-sv1
	 * A kimenet lehet tobb soros is ha keresztezodest kerdezunk le, vagy valtot, mert ilyenkor az osszes
	 * beloluk indulo/ rajtuk athalado vonalon vegigmegyunk.
	 */
	private static class CmdExploreLine extends CommandBase{

		public CmdExploreLine() {
			super("explore line");
		}

		Railway paramRail = null;
		Railway firstRail = null;
		Railway previousRail = null;
		boolean hasToBeReported = false;
		CmdExploreAllrail toReport = null;
		
		/**
		 * @param params[2] A sin azonositoja aminek megakarjuk hatarozni ezt a szakaszat.
		 */
		@Override
		public void execute(String[] params) {
			
			if (params.length < 3) {
				targetOS.println("Nincs eleg parameter!");
				return;
			}
			
			paramRail = null;
			firstRail = null;
			previousRail = null;
			
			paramRail = rails.get(params[2]);
			if (paramRail != null) {
				getStarterRail();
				writeLine();
				return;
			} 
			
			paramRail = buildingSpots.get(params[2]);
			if (paramRail != null) {
				getStarterRail();
				writeLine();
			} 
			
			paramRail = crosses.get(params[2]);
			if (paramRail != null) {
				CrossRailway cr = crosses.get(params[2]);
				List<Railway> secondList = cr.get2ndNeighbours();
				int num2 = secondList.size();
				
				if (num2 > 0) {
					firstRail = cr;
					previousRail = secondList.get(0);
					stepLoop();
					writeLine();
					targetOS.println("");
				}
				
				List<Railway> firstList = cr.getNeighbours();
				firstList.removeAll(secondList);
				int num1 = firstList.size();
				if (num1 > 0) {
					firstRail = cr;
					previousRail = firstList.get(0);
					stepLoop();
					writeLine();
				}
				
				if (num2 == 0 && num1 == 0)
					targetOS.println(params[2]);
			} 
			
			paramRail = switches.get(params[2]);
			if (paramRail != null) {
				List<Railway> lanes = paramRail.getNeighbours();
				for (Railway line: lanes) {
					firstRail = line;
					previousRail = paramRail;
					stepLoop();
					writeLine();
					targetOS.println("");
				}
				return;
			}
			targetOS.println("");
		}
		
		/**
		 * Ugyanugy vegrehajta a vonalak kiirasat, de emelett meg el is kuldi a who parameterben definialt objektumnak
		 * az osszes kiirt kulcsot.
		 * 
		 * @param who Az objektum referenciaja, amit ertesiteni kell
		 * @param params megfelel a sima executeban leirttal(mert pontosan ezzel hivja meg)
		 */
		public void executeWithReport(CmdExploreAllrail who, String[] params) {
			hasToBeReported = true;
			toReport = who;
			if (who != null) execute(params);
			hasToBeReported = false;
			toReport = null;
		}
		
		/**
		 * A vonal vegere megy.
		 */
		private void stepLoop() {
			int counter = 0;
			while (firstRail != null && !(firstRail instanceof Switch) && counter < 100) {
				Railway temp = firstRail;
				firstRail = firstRail.next(previousRail);
				previousRail = temp;
				counter++;
			}
			Railway temp = firstRail;
			firstRail = previousRail;
			previousRail = temp;
		}
		
		/**
		 * Egy sima sinnel, kitolti az indulo parametereket, majd elindul ezekkel a vonal vegere.
		 */
		private void getStarterRail() {
			List<Railway> rails = paramRail.getNeighbours();
			firstRail = paramRail;
			if (rails.size() < 2) return;
			
			previousRail = paramRail;
			firstRail = rails.get(0);
			stepLoop();
		}
		
		/**
		 * Miutan a vegen vagyunk, zutan bejarja a vonalat, es kiirja az osszes kozben erintett sin azonositojat.
		 */
		private void writeLine() {
			if (previousRail != null && previousRail instanceof Switch) {
				targetOS.print(getStringForRail(previousRail) + "-");
			}
			
			Railway firstout = firstRail;
			
			targetOS.print(getStringForRail(firstRail));
			if (hasToBeReported) toReport.report(getStringForRail(firstRail));
			
			Railway temp = firstRail;
			firstRail = firstRail.next(previousRail);
			previousRail = temp;
			while (!(firstRail instanceof Switch) && firstRail != null && firstRail != firstout) {
				
				targetOS.print("-" + getStringForRail(firstRail));
				if (hasToBeReported) toReport.report(getStringForRail(firstRail));
				
				temp = firstRail;
				firstRail = firstRail.next(previousRail);
				previousRail = temp;
			}
			if (firstRail instanceof Switch)
				targetOS.print("-" + getStringForRail(firstRail));
			if (firstRail == firstout)
				targetOS.print("-");
		}
		
	}
	
	/**
	 * A sinnek par alapveto tulajdonsagat kozli a felhasznaloval.
	 */
	private static class CmdExploreRail extends CommandBase{

		public CmdExploreRail() {
			super("explore rail");
		}

		/**
		 * @param params[2] A sin azonositoja amirol informaciot kerunk.
		 */
		@Override
		public void execute(String[] params) {
			
			if (params.length < 3) {
				targetOS.println("Nincs eleg parameter!");
				return;
			} 
			
			String key = params[2];
			Railway rail = rails.get(key);
			if (rail == null) rail = switches.get(key);
			if (rail == null) rail = crosses.get(key);
			if (rail == null) rail = buildingSpots.get(key);
			if (rail == null) {
				targetOS.println("A megadott sin nem letezik!");
				return;
			}
			
			List<Railway> neighs = rail.getNeighbours();
			targetOS.print("Szomszedok: ");
			for (int i = 0; i < neighs.size(); i++) {
				targetOS.print(getStringForRail(neighs.get(i)));
				if (i != neighs.size() - 1) targetOS.print(" ");
			}
			
			targetOS.println();
			String moKey = null;
			MovingObject c = rail.OnMe;
			
			for (Entry<String, Locomotive> entry: locos.entrySet())
				if (entry.getValue() == c)
					moKey = entry.getKey();
			
			if (moKey == null)
				for (Entry<String, Cart> entry: carts.entrySet())
					if (entry.getValue() == c)
						moKey = entry.getKey();
			
			if (moKey != null) targetOS.println("Vonat: " + moKey);
			else targetOS.println("Vonat: Nincs rajtam.");
			if (rail.station != null) sendMessage("Allomas: san", rail.station);
		}
	}
	
	/**
	 * Az allomasnak par alapveto tulajdonsagat kozli a felhasznaloval.
	 */
	private static class CmdExploreStation extends CommandBase{

		public CmdExploreStation() {
			super("explore station");
		}

		/**
		 * @param params[2] Az allomas azonositoja amirol informaciot kerunk.
		 */
		@Override
		public void execute(String[] params) {
			
			if (params.length < 3) {
				targetOS.println("Nincs eleg parameter!");
				return;
			}
			
			Station station = stations.get(params[2]);
			if (station == null) station = simultanStations.get(params[2]);
			if (station == null) {
				targetOS.println("A megadott allomas nem letezik!");
				return;
			}
			
			targetOS.println("Ez a " + params[2] + " allomas, es " + station.color.toString().toLowerCase() + " szinu.");
			targetOS.println("Az allomas a " + getStringForRail(station.railway) + " mellett van.");
		}
	}
	
	/**
	 * Az explore line-hoz hasonloan kiirja vonalankent a palyat.
	 * Addig irogatja a vonalakat, amig az osszes sint fel nem irja ily modon.
	 */
	private static class CmdExploreAllrail extends CommandBase{

		public CmdExploreAllrail() {
			super("explore allrail");
		}
		
		private Map<String, Railway> toBeMapped = null;

		public void report(String stringForRail) {
			toBeMapped.remove(stringForRail);
		}

		/**
		 * @param params Nem hasznalt.
		 */
		@Override
		public void execute(String[] params) {
			toBeMapped = new HashMap<String, Railway>();
			toBeMapped.putAll(rails);
			toBeMapped.putAll(buildingSpots);
			
			while (!toBeMapped.isEmpty()) {
				String key = toBeMapped.keySet().iterator().next();
				String[] newParams = {"", "", key};
				new CmdExploreLine().executeWithReport(this, newParams);
				targetOS.println("");
			}
			
		}
	}
	
	/**
	 * Atallitja a valtot hogy egy masik sin fele nezzen.
	 */
	private static class CmdToggleSwitch extends CommandBase{

		public CmdToggleSwitch() {
			super("toggle switch");
		}

		/**
		 * @param params[2] A valto azonositoja.
		 * @param params[3] Egy szam, ami kijeloli hogy a valtohoz hanyadiknak hozzakotott sinre valtsunk.
		 */
		@Override
		public void execute(String[] params) {
			
			if (params.length < 4) {
				targetOS.println("Nincs eleg parameter!");
				return;
			}
			
			Switch sw = switches.get(params[2]);
			if (sw == null) {
				targetOS.println("Sikertelen. A megadott valto nem letezik.");
				return;
			}
			
			List<Railway> railList = sw.getThatNeighbour();
			
			Railway switchTo = rails.get(params[3]);
			if (switchTo == null) switchTo = switches.get(params[3]);
			if (switchTo == null) switchTo = crosses.get(params[3]);
			if (switchTo == null) switchTo = buildingSpots.get(params[3]);
			
			if (switchTo != null && railList.contains(switchTo)) {
				
				sw.switchTo(switchTo);
				targetOS.println("Sikerult!");
			} else {
				targetOS.println("Sikertelen. A valto nem allithato a kert helyzetbe, mert nincs ilyen helyzet.");
				return;
			}
		}
	}
	
	/**
	 * Beallitja hogy egy allomas, fel-, vagy leszallo.
	 */
	private static class CmdToggleStation extends CommandBase{

		public CmdToggleStation() {
			super("toggle station");
		}

		/**
		 * @param params[2] Az allomas azonositoja
		 * @param params[3] "true"/"false" (true->felaszallo)
		 */
		@Override
		public void execute(String[] params) {
			
			if (params.length < 4) {
				targetOS.println("Nincs eleg parameter!");
				return;
			}
			
			boolean isSimultan = false; 
			if (params[3].equalsIgnoreCase("true") || params[3].equalsIgnoreCase("false"))
				isSimultan = Boolean.parseBoolean(params[3]);
			else {
				targetOS.println("A negyedik parameter erteke hibas.");
			}
			
			Station station = stations.get(params[2]);
			if (station == null) station = simultanStations.get(params[2]);
			else stations.remove(params[2]);
			if (station == null) {
				targetOS.println("Sikertelen. A megadott allomas nem letezik.");
				return;
			} else simultanStations.remove(params[2]);
			
			
			if (isSimultan) {
				
				simultanStations.put(params[2], new SimultanStation(station.railway, station.color));
				targetOS.println("Sikerult! Az allomason mostmar felszallhatnak az utasok.");
			} else {
				stations.put(params[2], new Station(station.railway, station.color));
				targetOS.println("Sikerult! Az allomason mostmar nem szallhatnak fel az utasok.");
			}
			
		}
	}
	
	/**
	 * Felepit egy alagutat es elvegzi az ezzel kapcsolatos adminisztraciokat.
	 * Ha mar fel van epitve egy, akkor nem epit fel meg egyet.
	 */
	private static class CmdBuildTunnel extends CommandBase{

		public CmdBuildTunnel() {
			super("build tunnel");
		}

		/**
		 * @param params[2] Az egyik epitohely azonositoja(sbn) ami az alagut bejarataul fog szolgalni.
		 * @param params[3] Az masik epitohely azonositoja(sbn) ami az alagut bejarataul fog szolgalni. A sorrend lenyegtelen.
		 */
		@Override
		public void execute(String[] params) {
			BuildingSpot first = null;
			BuildingSpot second = null;	
			if (params.length > 3) {
			
				
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
				tunnel = new Tunnel();
				tunnel.build(first, second);
				sendMessage("Sikerult! Az alaguton mehetnek at a vonatok.");
			}
			
			
		}
	}
	
	/**
	 * Leromoblja az eppen aktualisan felpitett alagutat.
	 */
	private static class CmdDestroyTunnel extends CommandBase{	
	
		public CmdDestroyTunnel() {
			super("destroy tunnel");
		}

		/**
		 * @param params Nem hasznalt
		 */
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

	/**
	 * Ezek utan a kovetkezo add cart, vagy add loco barmelyik pontjat kijelolheti a palyanak,
	 * majd ott kezdi el epiteni az uj vonatot. Ezt ugy kell elkepzelni hogy amikor a felhasznalo kiadja ezt
	 * az utasitast, akkor a korabban epitett vonat befejezettnek tekintetik, es ezutan a kovetkezo add cart,
	 * iletve add loco utasitassal lehelyezheti az uj vonat utolso(/egyetlen) kocsijat, majd a tovabbi ilyen
	 * utasitasok ezt a vonatot fogjak tovabb epiteni.
	 */
	private static class CmdPrepareTrain extends CommandBase{

		public CmdPrepareTrain() {
			super("prepare train");
		}

		/**
		 * @param params Hasznalatlan.
		 */
		@Override
		public void execute(String[] params) {
			lastCart = null;
			isFinished = false;
			targetOS.println("Uj vonat elkezdve.");
		}
	}
	
	/**
	 * Felvesz egy uj kocsit a vonathoz. Ha ez nem az utolso kocsi(elorol nezve, es nem a hozzadas sorrendjet nezve),
	 * akkor a program figyel arra, hogy tenyleg szmszedos sinre akarja-e tenni a felhasznalo.
	 */
	private static class CmdAddCart extends CommandBase{

		public CmdAddCart() {
			super("add cart");
		}

		/**
		 * @param params[2] Ha szenszallitot akarunk: "coal"
		 * @param params[3] Ha szenszallitot akarunk: A mostani sin amire tenni akarjuk.
		 * @param params[4] Ha szenszallitot akarunk: A menetirannyal ellentetes szomszedos sin.
		 * 
		 * @param params[2] Ha utasszallito kocsit akarunk: A mostani sin amire tenni akarjuk.
		 * @param params[3] Ha utasszallito kocsit akarunk: A menetirannyal ellentetes szomszedos sin.
		 * @param params[4] Ha utasszallito kocsit akarunk: A kocsi szine.
		 * @param params[5] Ha utasszallito kocsit akarunk: "true"/"false" ulnek-e benne utasok(true = igen).
		 */
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
	
	/**
	 * Egy uj mozdonyt ad a vonathoz, es ezzel be is fejezi az aktualis vonat epiteset.
	 */
	private static class CmdAddLoco extends CommandBase{

		public CmdAddLoco() {
			super("add loco");
		}

		/**
		 * @param params[2] A sin amire tenni akarjuk.
		 * @param params[3] A menetirannyal szembeni szomszedos sin.
		 * @param params[4] A vonat sebessege, egesz szam(1 = 1 lepes/60 masodperc ha fut az idozito, amugy erdektelen).
		 */
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
	
	/**
	 * Lepteti egyet a vonatot, ami aztan mindent elvegez maga(kocsik huzasa, allomasok figyelese, stb.)
	 */
	private static class CmdStep extends CommandBase{

		public CmdStep() {
			super("step");
		}

		/**
		 * @param params[2] A leptetni kivant vonat azonostioja.
		 */
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
			String railkey1 = getStringForRail(target.CurrentRailwaySegment);
			String railkey2 = getStringForRail(target.CurrentRailwaySegment.next(target.PreviousRailwaySegment));
			
			targetOS.println("Ertettem: "+ params[1] +" vonat mozgatasa "+ railkey1 +" sinrol "+ railkey2 +" sinre.");
			target.move();
		}
	}
	
	/**
	 * Torli az adott vonatot(a mozdonyt az osszes kocsijaval egyutt), HA nincs ra vonatkozo ervenyes idozito.
	 */
	private static class CmdDeleteLoco extends CommandBase{

		public CmdDeleteLoco() {
			super("delete loco");
		}

		/**
		 * @param params[2] A torolni kivant mozdony azonositoja.
		 */
		@Override
		public void execute(String[] params) {
			
			if (params.length > 2) {
				
				Locomotive loco = locos.get(params[2]);
				if (loco == null) {
					targetOS.println("Sikertelen. Nem letezik adott azonositoju mozdony.");
					return;
				}
				Cart nextCart = loco.Pulls;
				locos.remove(params[2]);
				loco.CurrentRailwaySegment.setOnMe(null);
				
				while (nextCart != null) {
					String key = null;
					for (Entry<String, Cart> entry: carts.entrySet())
						if (entry.getValue() == nextCart)
							key = entry.getKey();
					carts.remove(key);
					nextCart.CurrentRailwaySegment.setOnMe(null);
					nextCart = nextCart.Pulls;
				}
				
				targetOS.println("Sikerult! mln mozdony megszunt az osszes kocsival egyutt.");
			} else {
				targetOS.println("Nincs eleg parameter!");
			}
		}
	}

	/**
	 * A mozdonyra, vagy akar a teljes vonatra kapcsolatos informaciokat irja ki a felhasznalo szamara.
	 */
	private static class CmdExploreLoco extends CommandBase{

		public CmdExploreLoco() {
			super("explore loco");
		}

		/**
		 * @param params[2] A mozdony azonositoja, vagy "all" ha az osszes mozdonyt le akarjuk kerdezni.
		 * @param params[3] opcionalis "allcart" Ha a teljes vonatra kivancsiak vagyunk, akkor ez a vonat osszes kocsijatol
		 *  megkerdezi a parametereit es kiiratja veluk.
		 */
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
					sendMessage("A jelenleg letezo mozdonyok: ");
					for(String keys:locos.keySet())
					{
						String railkey = getStringForRail(locos.get(keys).CurrentRailwaySegment);
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
					String railkey = getStringForRail(actual.CurrentRailwaySegment);
					
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
					sendMessage("A jelenleg letezo mozdonyok: ");
					for(String keys:locos.keySet())
					{
						line=keys+": Epp az "+ getStringForRail(locos.get(keys).CurrentRailwaySegment)+" sinen allok, es "+locos.get(keys).Speed+"-el megyek.";
						sendMessage(line);
						line=null;
					}
					
				}
				else
				{
					String line=null;
					line="Ez itt mozdony "+params[2]+".";
					sendMessage(line);
					line="Epp az "+ getStringForRail(actual.CurrentRailwaySegment) +" sinen allok, es "+ actual.Speed +"-el megyek.";
					sendMessage(line);
				}
			}
		}
	}
	
	/**
	 * Kiiratja egy kocsinak a helyzetet, szinet, es hogy vannak-e rajta utasok, ha nem szenszallito.(illetve azt is, hogy az-e.)
	 */
	private static class CmdExploreCart extends CommandBase{

		public CmdExploreCart() {
			super("explore cart");
		}
		
		/**
		 * @param params[2] A lekerdezett kocsi azonositoja.
		 */
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
			
			String railkey = getStringForRail(actual.CurrentRailwaySegment);
			
			if(actual instanceof CoalCart)
			{
				sendMessage("Epp az " + railkey + " sinen allok.");
				sendMessage("En egy szenszallito vagon vagyok.");
			}
			else
			{
				
				
				
				sendMessage("Epp az " + railkey + " sinen allok.");
				String state=null;
				if(actual.getPassengers()) state="tele";
				else state="ures";
				sendMessage("En egy utasszallito vagon vagyok. A szinem "+ actual.getColor().toString().toLowerCase() +", es jelenleg epp "+state+" vagyok.");
			}
			
		}
	}
	
	/**
	 * Elindit egy idozitot ami aztan a vonat sebessegetol fuggoen fogja leptetni azt. Egy vonaton csak egy idozito lehet.
	 */
	private static class CmdTimerStart extends CommandBase{

		public CmdTimerStart() {
			super("timer start");
		}

		/**
		 * @param params[2] A mozdony azonositoja amihez kotjuk az idozitot.
		 */
		@Override
		public void execute(String[] params) {
			
			if (params.length < 3) {
				targetOS.println("Nincs eleg parameter!");
				return;
			}
			Locomotive loco = locos.get(params[2]);
			if (loco == null) {
				targetOS.println("Sikertelen. Nem letezik adott azonositoju mozdony.");
				return;
			}
			if (timers.containsKey(loco)) {
				targetOS.println("Sikertelen. Mar letezik egy idozito erre a vonatra.");
				return;
			}
			
			Timer t = new Timer();
			t.schedule(new MyTask(loco, params[2], t), (60 / loco.Speed) * 1000);
			timers.put(loco, new Timer());
			targetOS.println("Sikerult! Mostantol az " + params[2] + " mozdony emberi beavatkozas nelkul is magatol menni fog.");
		}
	}
	
	/**
	 * A mozdony leptetsehez az idozito ennek az osztalynak a peldanyat hasznalja.
	 * Ez lenyegebe csak egy csomagolo osztaly, viszi magaval a kiiratashoz, a lepteteshez, es
	 * az idoziteshez szukseges informaciokat.
	 */
	private static class MyTask extends TimerTask {
		Timer myTimer;
		private Locomotive loco;
		String key;
		
		MyTask(Locomotive loco, String key, Timer t) {this.loco = loco; this.key = key; myTimer = t;}
		
		/**
		 * A leptetes itt van megvalositva
		 */
		@Override
		public void run() {
			targetOS.println("A "+ key +" vonat mozgatasa "+ getStringForRail(loco.CurrentRailwaySegment) +" sinrol "+ 
					getStringForRail(loco.CurrentRailwaySegment.next(loco.PreviousRailwaySegment)) +" sinre.");
			synchronized (syncObj) {
				loco.move();
			}
			myTimer.schedule(new MyTask(loco, key, myTimer), (60 / loco.Speed) * 1000);
		}
	}
	
	/**
	 * Megallitja a megfelelo vonathoz tartozo idozitot.
	 */
	private static class CmdTimerEnd extends CommandBase{

		public CmdTimerEnd() {
			super("timer end");
		}

		/**
		 * @param params[2] A mozdony azonositoja, amihez tartozik a leallitani kivant idozito.
		 */
		@Override
		public void execute(String[] params) {
			if (params.length < 3) {
				targetOS.println("Nincs eleg parameter!");
				return;
			}
			Locomotive loco = locos.get(params[2]);
			if (loco == null) {
				targetOS.println("Sikertelen. Nem letezik adott azonositoju mozdony.");
				return;
			}
			Timer t = timers.remove(loco);
			if (t == null) {
				targetOS.println("Sikertelen. Mar letezik egy idozito erre a vonatra.");
				return;
			} else {
				targetOS.println("Sikerult! A mozdony mostantol csak az itt kiadott step hatasara fog lepni.");
				t.cancel();
			}
		}
	}
	
	/**
	 * Kiirja, hogy a valto merre all.
	 */
	private static class CmdExploreSwitch extends CommandBase{

		public CmdExploreSwitch() {
			super("explore switch");
		}

		/**
		 * @param params[2] A valto azonositoja amirol tudni akarjuk ezt.
		 */
		@Override
		public void execute(String[] params) {
			
			if (params.length < 3) {
				targetOS.println("Nincs eleg parameter!");
				return;
			}
			
			Switch sw = switches.get(params[2]);
			
			if (sw == null) {
				sendMessage("Sikertelen. A megadott sin nem letezik.");
				return;
			}
			
			Railway cs = sw.getCurrentStanding();
			String cskey = getStringForRail(cs);
			
			targetOS.println("Ez a valto az " + cskey + " iranyaba mutat.");
		}
	}
	
	/**
	 * Letakaritja az asztalt. Ezt ugy eri el, hogy lenyegebe az osszes tarolt erteket eldobja, lenullazza.
	 * Kulon figyelmet fordit az idozitok leallitasara is, es az azonositok kiosztasara szolgalo szamlalot is visszaallitja.
	 */
	private static class CmdClearTable extends CommandBase{

		public CmdClearTable() {
			super("clearTable");
		}

		/**
		 * @param params Nem hasznalt!
		 */
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
			
			for (Entry<Locomotive, Timer> entry: timers.entrySet()) {
				entry.getValue().cancel();
			}
			timers = new HashMap<Locomotive, Timer>();
			
			targetOS.println("A takaritas megtortent.");
		}
	}
	
	/**
	 * A program befejezesere szolgalo parancs. Kiadasa utan ez az utolso parancs amit lefut.
	 */
	private static class CmdExit extends CommandBase{

		public CmdExit() {
			super("exit");
		}

		/**
		 * @param params Nem hasznalt!
		 */
		@Override
		public void execute(String[] params) {
			isRunning = false;
		}
	}
}
