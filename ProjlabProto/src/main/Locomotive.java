package main;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Locomotive extends MovingObject {
	
	private int Speed;
	
	/**
	 * Konstruktor. Létrehozza a mozdonyt.
	 * @param railwaySegment Jelenlegi sín, ahol a mozdony áll.
	 * @param previousRailwaySegment Elõzõ sín, ahonnal a mozdony jött.
	 * @param nextCart Mozdony által közvetlenül húzott kocsi.
	 * @param speed Mozdony sebessége
	 */
	public Locomotive(Railway railwaySegment, Railway previousRailwaySegment, Cart nextCart, int speed) {
		super(railwaySegment, previousRailwaySegment, nextCart);
		this.Speed = speed;
	}
	
	/**
	 * Meghívásakor a mozdony lép egyet elõre.
	 */
	public void move() {
		
		Railway nextRailwaySegment = CurrentRailwaySegment.next(PreviousRailwaySegment);
		step(nextRailwaySegment);
		PreviousRailwaySegment = CurrentRailwaySegment;
		CurrentRailwaySegment = nextRailwaySegment;
		
	}
	
	/**
	 * Állomáshoz érkezéskor hívódik. Jelzi a mögé kötött vagonnak, hogy állomáshoz érkezett a vonat.
	 * @param station Az az állomás, ahova megérkezett a mozdony.
	 */
	public void ArrivedAtStation(Station station) {
		
		if(Pulls.colorCheck(station) == true) {
			//Application.win();
		}
		
	}
}
