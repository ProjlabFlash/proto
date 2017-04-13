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
import java.util.List;

public class Application {

	private static PrintStream stdoutWriter;
	private static BufferedReader stdinReader;
	private static PrintStream targetOS;
	private static BufferedReader targetIS;
	private static FileInputStream fileReader = null;
	private static FileOutputStream fileWriter = null;
	
	public static void main(String[] args) {
		
		targetOS = stdoutWriter = System.out;
		targetIS = stdinReader = new BufferedReader(new InputStreamReader(System.in));
		
		List<CommandBase> commands = new ArrayList<CommandBase> ();
		commands.add(new CmdSwitchInput());
		commands.add(new CmdSwitchOutput());
		commands.add(new CommandBase.CmdAddRail());
		
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
	
	public static void sendMessage(String msg) {
		targetOS.println(msg);
	}

}
