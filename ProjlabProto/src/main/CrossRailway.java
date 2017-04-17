package main;

import java.util.ArrayList;

public class CrossRailway extends Railway {
	private ArrayList<Railway> ThatNeighbour2 = new ArrayList<>();
	private ArrayList<Railway> ThisNeighbour2 = new ArrayList<>();
	public ArrayList<Railway> getThatNeighbour2(){
		return ThatNeighbour2;
	}
	public ArrayList<Railway> getThisNeighbour2(){
		return ThisNeighbour2;
	}
	public CrossRailway(Railway this1 ,Railway that1 ,Railway this2 ,Railway that2){
		super(this1);
		ThatNeighbour.add(that1);
		ThisNeighbour2.add(this2);
		ThatNeighbour2.add(that2);		
	}
	public CrossRailway(Railway this1) {
		super(this1);
	}
}
