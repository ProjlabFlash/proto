package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
	/**
	 * A program belepesi pontja
	 * @param args A teszteset valos eredmenyet, es elvart eredmenyet tartalmazo szoveges fajlok (pontosan 2 fajl) eleresi utjai ebben a sorrendben
	 */
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Pontosan 2 fajl eleresi utat adjon meg!");
			return;
		}
		
		boolean wrong_path = false;
	    BufferedReader br1 = null, br2 = null;
		try {
			br1 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(args[0]))));
		} catch (FileNotFoundException e1) {
			System.out.println("Az 1. fajl nem talalhato!");
			wrong_path = true;
		}
		try {
			br2 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(args[1]))));
		} catch (FileNotFoundException e1) {
			System.out.println("Az 2. fajl nem talalhato!");
			wrong_path = true;
		}
		if(wrong_path) return;
		
	    String line1, line2;
	    ArrayList<String> wrongLines = new ArrayList<>();
	    ArrayList<Integer> linenumbers = new ArrayList<>();
	    boolean wrong_outputs = false;
	    int linenumber = 0;
	    try {
			while((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null) {
				linenumber++;
				if(!line1.equals(line2)){
					wrong_outputs = true;
					wrongLines.add("Teszt eredmeny: " + line1 + "\nElvart eredmeny: " + line2);
					linenumbers.add(linenumber);
			    }
			}
			if(br1 != null) br1.close();
			if(br2 != null) br2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
	    if(wrong_outputs) {
	    	System.out.println("A teszt hibas eredmenyeket adott:");
	    	for(int i = 0; i < wrongLines.size(); i++) {
	    		System.out.println("-- "+ (i+1) + " -- [" + linenumbers.get(i) + ". sor]");
	    		System.out.println(wrongLines.get(i));
	    	}
	    }
	    else { 
	    	System.out.println("A program kimenete megegyezik az elvart kimenettel");
	    }
	}
}
